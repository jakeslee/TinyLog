package asia.gkc.tinylog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakes on 14-12-8.
 */
@WebServlet(name = "SearchServlet")
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        String keyword = request.getParameter("key");
        List<String> conditions = new ArrayList<String>();
        try {
            if (Util.isDefined(keyword)){
                conditions.add("CONTENT");
                conditions.add(keyword);
                ResultObject resultObject = mysqlAdapter.search(conditions, "articles");
                ResultSet resultSet = resultObject.getResultSet();
                List<Article> articles = new ArrayList<Article>();
                while (resultSet != null && resultSet.next()){
                    articles.add(Util.toArticle(resultSet));
                }
                resultObject.close();
                request.setAttribute("articlelist", articles);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            mysqlAdapter.closeConnection();
        }
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentPage", "search");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
