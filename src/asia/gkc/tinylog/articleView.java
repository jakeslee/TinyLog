package asia.gkc.tinylog;

import org.json.JSONException;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakes on 14-12-4.
 */
@WebServlet(name = "articleView")
public class articleView extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        String article_id = request.getParameter("id");
        MysqlAdapter.printPool();
        if (article_id == null || article_id.isEmpty()){
            PrintWriter printWriter = response.getWriter();
            try {
                printWriter.println(new JSONObject().put("status","fail").put("msg", "Error parameter!").toString());
            }catch (JSONException e){
                e.printStackTrace();
            }
            return;
        }
        List<String> conditions = new ArrayList<String>();
        conditions.add("ARTICLE");
        conditions.add(article_id);
        ResultObject resultObject = mysqlAdapter.select(conditions, "articles");
        try {
            ResultSet resultSet = resultObject.getResultSet();
            if (resultSet != null && resultSet.next()){
                request.setAttribute("article", Util.toArticle(resultSet, true));
                request.setAttribute("comments", Util.getComments(article_id));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }
        request.setAttribute("currentPage", "article");
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}
