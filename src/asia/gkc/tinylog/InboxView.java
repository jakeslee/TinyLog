package asia.gkc.tinylog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakes on 14-12-23.
 */
@WebServlet(name = "InboxView")
public class InboxView extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        if (Util.isSigned(session)){
            request.setAttribute("msgs", Util.getMessages((String)session.getAttribute("username")));
            ((User)session.getAttribute("user")).setNewMsg(false);
            List<String> condition = new ArrayList<String>();
            condition.add("READ");
            condition.add((String)session.getAttribute("username"));
            mysqlAdapter.update(condition, "msg_list");
        }

        mysqlAdapter.closeConnection();
        request.setAttribute("currentPage", "inbox");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
