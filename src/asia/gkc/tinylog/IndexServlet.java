package asia.gkc.tinylog;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jakes on 14-12-3.
 */
public class IndexServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        List<Article> articles = new ArrayList<Article>();
        List<String> cons = new ArrayList<String>();
        cons.add("SUMMARY");
        cons.add("DESC");
        String page = request.getParameter("page");
        if (Util.isDefined(page)){
            cons.add(page);
            request.setAttribute("page", page);
        }else {
            cons.add("0");
        }

        ResultObject resultObject = mysqlAdapter.select(cons, "articles");
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ResultSet resultSet = resultObject.getResultSet();
            while (resultSet != null && resultSet.next()){
                articles.add(Util.toArticle(resultSet, true));
            }

            request.setAttribute("articlelist", articles);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }

        MysqlAdapter.printPool();
        request.setAttribute("currentPage", "index");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
