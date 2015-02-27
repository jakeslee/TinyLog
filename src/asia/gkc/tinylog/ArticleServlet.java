package asia.gkc.tinylog;

import org.json.JSONArray;
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
 * Created by jakes on 14-12-4.
 */
@WebServlet(name = "ArticleServlet")
public class ArticleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        HttpSession session = request.getSession();
        JSONObject jsonObject = new JSONObject();
        List<String> conditions = new ArrayList<String>();
        ResultObject resultObject = new ResultObject(null,null);
        try {
            jsonObject.put("action", action);
            if (!Util.isSigned(session)){
                throw new Exception("Permission denied!");
            }
            if(action.equals("new")){
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                String tags = request.getParameter("tags");
                if (Util.isDefined(title) && Util.isDefined(content)){
                    conditions.add(title);
                    try {
                        conditions.add(content.substring(0, 255));
                    }catch (StringIndexOutOfBoundsException e){
                        e.printStackTrace();
                        conditions.add(content);
                    }
                    conditions.add(content);
                    conditions.add((String)session.getAttribute("username"));
                    conditions.add(tags != null ? tags : "");
                    if (mysqlAdapter.insert(conditions, "articles")){
                        jsonObject.put("status", "success");
                    }else {
                        throw new Exception("Unknown errors!");
                    }
                }else {
                    throw new Exception("Incorrect parameter!");
                }
            }else if (action.equals("edit")){
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                String tags = request.getParameter("tags");
                String article_id = request.getParameter("id");
                if (Util.isDefined(article_id) && Util.isDefined(title) && Util.isDefined(content)){
                    Article article = Util.getArticle(article_id);
                    if (Util.getAuth((String)session.getAttribute("username")) == Util.ADMIN ||
                            article.getUsername().equals(session.getAttribute("username"))){
                        //ID
                        conditions.add("ID");
                        //TITLE
                        conditions.add(title);
                        //SUMMARY
                        try {
                            conditions.add(content.substring(0, 255));
                        }catch (StringIndexOutOfBoundsException e){
                            e.printStackTrace();
                            conditions.add(content);
                        }
                        //CONTENT
                        conditions.add(content);
                        //USERNAME
                        conditions.add((String)session.getAttribute("username"));
                        //TAGS
                        conditions.add(tags != null ? tags : "");
                        //ID
                        conditions.add(article_id);
                        if (mysqlAdapter.update(conditions, "articles")){
                            jsonObject.put("status", "success");
                        }else
                            throw new Exception("Unknown errors!");
                    }else
                        throw new Exception("Permission denied!");
                }else
                    throw new Exception("Incorrect parameter!");
            }else if (action.equals("delete")){
                String article_id = request.getParameter("id");
                if(Util.isDefined(article_id) && Util.isSigned(session)){
                    conditions.add("ARTICLE");
                    conditions.add(article_id);
                    resultObject = mysqlAdapter.select(conditions, "articles");
                    ResultSet resultSet = resultObject.getResultSet();
                    if (resultSet == null || !resultSet.next())
                        throw new Exception("No this article");
                    Article article = Util.toArticle(resultSet);
                    if (article.getUsername().equals(session.getAttribute("username"))){
                        conditions.clear();
                        conditions.add("ID");
                        conditions.add(article_id);
                        if (mysqlAdapter.delete(conditions, "articles")){
                            jsonObject.put("status", "success");
                        }else
                            throw new Exception("Unknown errors!");
                    }else {
                        throw new Exception("Permission denied!");
                    }
                }else {
                    throw new Exception("Incorrect parameter or user!");
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            jsonObject.put("status", "fail");
            jsonObject.put("msg", e.getMessage());
        }finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonObject.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //MysqlAdapter mysqlAdapter = (MysqlAdapter)getServletContext().getAttribute("mysql");
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        ResultObject resultObject = new ResultObject(null,null);
        try {
            String article_id = request.getParameter("id");
            if(article_id == null || article_id.isEmpty()){
                System.out.println("ID is empty or not defined.");
                return;
            }
            String detail = request.getParameter("detail");
            List<String> conds = new ArrayList<String>();
            conds.add("ARTICLE");
            conds.add(article_id);
            JSONObject jsonObject = new JSONObject();
            resultObject = mysqlAdapter.select(conds, "articles");
            ResultSet resultSet = resultObject.getResultSet();
            if (resultSet == null || !resultSet.next())
                throw new SQLException("ResultSet is null");

            jsonObject.put("id", resultSet.getInt("article_id"));
            jsonObject.put("title", resultSet.getString("title"));
            jsonObject.put("content", resultSet.getString("content"));
            jsonObject.put("time", resultSet.getTimestamp("post_time"));

            if(detail != null && !detail.isEmpty()){
                jsonObject.put("summary", resultSet.getString("summary"));
                jsonObject.put("tags", resultSet.getString("tags"));
            }

            PrintWriter printWriter = response.getWriter();
            printWriter.print(jsonObject.toString());
        }catch (SQLException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }
    }
}
