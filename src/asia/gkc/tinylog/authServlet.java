package asia.gkc.tinylog;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jakes on 14-12-5.
 */
@WebServlet(name = "authServlet")
public class authServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //MysqlAdapter mysqlAdapter = (MysqlAdapter)getServletContext().getAttribute("mysql");
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        JSONObject jsonObject = new JSONObject();
        if(action.equals("in")){
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            ResultObject resultObject = mysqlAdapter.select(username, "user_auth");
            try {
                ResultSet resultSet = resultObject.getResultSet();
                jsonObject.put("action", "login");
                if (resultSet != null && resultSet.next()) {
                    if (resultSet.getString("password").equals(password)) {
                        int auth = resultSet.getShort("authority");
                        jsonObject.put("status", "success");
                        session.setAttribute("username", username);
                        resultObject.close();
                        resultObject = mysqlAdapter.select(username, "user_info");
                        resultSet = resultObject.getResultSet();
                        User user = new User();
                        while (resultSet != null && resultSet.next()) {
                            user.setUsername(username);
                            user.setRealname(resultSet.getString("realname"));
                            user.setEmail(resultSet.getString("email"));
                            user.setLocation(resultSet.getString("location"));
                            user.setAuth(auth);
                            user.setNewMsg(Util.hasNewMsg(username));
                        }
                        session.setAttribute("user", user);
                    } else {
                        jsonObject.put("status", "fail");
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }finally {
                resultObject.close();
            }
        }else if (action.equals("out")){
            try {
                session.setAttribute("username", null);
                session.setAttribute("user", null);
                jsonObject.put("action", "logout");
                jsonObject.put("status", "success");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonObject.toString());
        mysqlAdapter.closeConnection();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter printWriter = response.getWriter();
        try {
            printWriter.println(new JSONObject().put("status","fail").put("msg", "Error parameter!").toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
