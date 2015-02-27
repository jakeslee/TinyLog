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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakes on 14-12-6.
 */
@WebServlet(name = "commentServlet")
public class CommentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //MysqlAdapter mysqlAdapter = (MysqlAdapter)getServletContext().getAttribute("mysql");
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        JSONObject jsonObject = new JSONObject();
        List<String> conditions = new ArrayList<String>();
        if(action.equals("new")){
            String content = request.getParameter("content");
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String type = request.getParameter("type");
            String target = request.getParameter("target");
            try {
                jsonObject.put("action", "new");
                System.out.println(content + username + email + type + target);
                if ((content == null || content.isEmpty()) ||
                        (type == null || type.isEmpty()) ||
                        (target == null || target.isEmpty())){
                    jsonObject.put("status", "fail");
                    jsonObject.put("msg", "Error Parameter!");

                }else {
                    if (Util.isSigned(session)){
                        username = (String)session.getAttribute("username");
                        email = ((User)session.getAttribute("user")).getEmail();
                    }else if (username == null || username.isEmpty()){
                        jsonObject.put("status", "fail");
                        jsonObject.put("msg", "Error Parameter!");
                        throw new Exception("Error username!");
                    }

                    //article_id
                    if (type.equals("0")){
                        conditions.add(type);
                    }else {
                        conditions.add(target);
                    }
                    //content
                    conditions.add(content);
                    //usertype
                    //System.out.println(((String)session.getAttribute("username")) + session.getAttribute("user"));
                    if (!Util.isSigned(session)){
                        conditions.add("0");
                    }else {
                        conditions.add(Integer.toString(((User)session.getAttribute("user")).getAuth()));
                    }
                    System.out.println("USER:" + username);
                    //username
                    conditions.add(username);
                    //email
                    conditions.add(email);
                    //website
                    conditions.add("");
                    System.out.println(content);
                    if (mysqlAdapter.insert(conditions, "comment_list")){
                        jsonObject.put("status", "success");
                    }else {
                        jsonObject.put("status", "fail");
                        jsonObject.put("msg", "Insert fail!");
                    }

                    if (!type.equals("0")){
                        Message message = new Message();
                        Article article = Util.getArticle(target);
                        message.setContent(
                                username + " 评论了你的文章《<a href=\"/articleView?id=" + target + "\">" + article.getTitle() +"</a>》");
                        message.setTarget(target);
                        message.setMsg_type("1");
                        message.setUsername(article.getUsername());
                        Util.addMessage(message);
                    }

                }
            }catch (JSONException e){
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (action.equals("delete")){
            String article_id = request.getParameter("target");
            try {
                jsonObject.put("action", "delete");
                if (article_id == null || article_id.isEmpty()){
                    jsonObject.put("status", "fail");
                    jsonObject.put("msg", "Error Parameter!");
                }else {
                    conditions.add("ID");
                    conditions.add(article_id);
                    if (mysqlAdapter.delete(conditions, "comment_list"))
                        jsonObject.put("status", "success");
                    else{
                        jsonObject.put("msg", "Delete fail!");
                        jsonObject.put("status", "fail");
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else if (action.equals("edit")){
            try {
                jsonObject.put("action", "edit");
                jsonObject.put("msg", "Edit fail!");
                jsonObject.put("status", "fail");//Pause~~~

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonObject.toString());
        mysqlAdapter.closeConnection();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
