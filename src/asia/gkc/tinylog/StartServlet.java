package asia.gkc.tinylog;

import javax.servlet.ServletConfig;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by jakes on 14-12-3.
 */
public class StartServlet extends javax.servlet.http.HttpServlet {
    public void init(ServletConfig config) throws SecurityException{
        MysqlAdapter.setConfiguration(
                config.getInitParameter("SERVER_HOST"),
                config.getInitParameter("SERVER_USER"),
                config.getInitParameter("SERVER_PASS"),
                config.getInitParameter("SERVER_DBNAME")
                );
        MysqlAdapter.setAdapter(new MysqlAdapter.QueryAdapter() {
            @Override
            public PreparedStatement insertAdapter(List<String> data, String TableName, MysqlAdapter self) throws SQLException{
                PreparedStatement statement = null;
                if (TableName.equals("articles")){
                    String sql = "INSERT INTO `articles` (`title`, `summary`, `content`, `username`, " +
                            "`tags`, `post_time`) VALUES (?, ?, ?, ?, ?, NOW());";
                    statement = self.getPreparedStatement(sql);
                    statement.setString(1, data.get(0));
                    statement.setString(2, data.get(1));
                    statement.setString(3, data.get(2));
                    statement.setString(4, data.get(3));
                    statement.setString(5, data.get(4));
                }else if (TableName.equals("comment_list")){
                    String sql = "INSERT INTO `comment_list` (`article_id`, `content`, `usertype`, `nickname`, " +
                            "`email`, `website`, `post_time`) VALUES (?, ?, ?, ?, ?, ?, NOW());";
                    statement = self.getPreparedStatement(sql);
                    statement.setInt(1, Integer.parseInt(data.get(0)));
                    statement.setString(2, data.get(1));
                    statement.setInt(3, Integer.parseInt(data.get(2)));
                    statement.setString(4, data.get(3));
                    statement.setString(5, data.get(4));
                    statement.setString(6, data.get(5));
                }else if (TableName.equals("msg_list")){
                    String sql = "INSERT INTO `msg_list` (`username`, `content`, `msg_type`, `target`, `post_time`)" +
                            " VALUES (?, ?, ?, ?, NOW());";
                    statement = self.getPreparedStatement(sql);
                    statement.setString(1, data.get(0));
                    statement.setString(2, data.get(1));
                    statement.setString(3, data.get(2));
                    statement.setString(4, data.get(3));
                }else if (TableName.equals("user_info")) {
                    String sql = "INSERT INTO `user_info` (`username`, `realname`, `email`, `location`) VALUES (?, ?, ?, ?);";
                    statement = self.getPreparedStatement(sql);
                    statement.setString(1, data.get(0));
                    statement.setString(2, data.get(1));
                    statement.setString(3, data.get(2));
                    statement.setString(4, data.get(3));
                }else if (TableName.equals("user_auth")) {
                    String sql = "INSERT INTO `user_auth` (`username`, `password`) VALUES (?, ?);";
                    statement = self.getPreparedStatement(sql);
                    statement.setString(1, data.get(0));
                    statement.setString(2, data.get(1));
                }else if (TableName.equals("sys_info")){
                    String sql = "INSERT INTO `sys_info` (`property`, `value`) VALUES (?, ?);";
                    statement = self.getPreparedStatement(sql);
                    statement.setString(1, data.get(0));
                    statement.setString(2, data.get(1));
                }
                return statement;
            }

            @Override
            public PreparedStatement deleteAdapter(Object data, String TableName, MysqlAdapter self) throws SQLException{
                PreparedStatement statement = null;
                if (TableName.equals("articles")){
                    List<String> cond = (List<String>)data;
                    if(cond.get(0).equals("ID")){
                        String sql = "DELETE FROM articles WHERE article_id=?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setInt(1, Integer.parseInt(cond.get(1)));
                    }else if (cond.get(0).equals("USER")){
                        String sql = "DELETE FROM articles WHERE username=?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setString(1, cond.get(1));
                    }
                }else if (TableName.equals("sys_info")){
                    String sql = "DELETE FROM sys_info WHERE property=?;";
                    statement = self.getPreparedStatement(sql);
                    statement.setString(1, (String)data);
                }else if (TableName.equals("user_auth")){
                    String sql = "DELETE FROM user_auth WHERE username=?;";
                    statement = self.getPreparedStatement(sql);
                    statement.setString(1, (String)data);
                }else if (TableName.equals("comment_list")){
                    List<String> cond = (List<String>)data;
                    if(cond.get(0).equals("ID")){
                        String sql = "DELETE FROM comment_list WHERE comment_id=?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setInt(1, Integer.parseInt(cond.get(1)));
                    }else if (cond.get(0).equals("USER")){
                        String sql = "DELETE FROM comment_list WHERE username=?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setString(1, cond.get(1));
                    }else if (cond.get(0).equals("ARTICLE")){
                        String sql = "DELETE FROM comment_list WHERE article_id=?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setInt(1, Integer.parseInt(cond.get(1)));
                    }
                }else if (TableName.equals("msg_list")) {
                    List<String> cond = (List<String>)data;
                    if(cond.get(0).equals("ID")){
                        String sql = "DELETE FROM msg_list WHERE msg_id=?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setInt(1, Integer.parseInt(cond.get(1)));
                    }else if (cond.get(0).equals("USER")){
                        String sql = "DELETE FROM msg_list WHERE username=?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setString(1, cond.get(1));
                    }else if (cond.get(0).equals("TARGET")){
                        String sql = "DELETE FROM msg_list WHERE msg_type=? AND target=?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setString(1, cond.get(1));
                        statement.setString(2, cond.get(2));
                    }
                }
                return statement;
            }

            @Override
            public PreparedStatement selectAdapter(Object data, String TableName, MysqlAdapter self) throws SQLException{
                PreparedStatement statement = null;
                if (TableName.equals("articles")){
                    List<String> cond = (List<String>)data;
                    if (cond.get(0).equals("SUMMARY")){
                        String sql = "SELECT * FROM article_detail";
                        try {
                            cond.get(1);
                            sql +=  " ORDER BY `post_time` " + cond.get(1) + " LIMIT ?," + Util.getSite().getPageitems();
                            statement = self.getPreparedStatement(sql);
                            statement.setInt(1, Integer.parseInt(cond.get(2)));
                            return statement;
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                            System.out.println("DESC NoT defined");
                        }
                        statement = self.getPreparedStatement(sql);
                    }else if(cond.get(0).equals("ARTICLE")){
                        String sql = "SELECT * FROM article_detail WHERE article_id=?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setInt(1, Integer.parseInt(cond.get(1)));
                    }else if (cond.get(0).equals("LATEST")){
                        String sql = "SELECT article_id, title, post_time FROM articles ORDER BY `post_time` DESC LIMIT ?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setInt(1, Integer.parseInt(cond.get(1)));
                    }else if (cond.get(0).equals("HOT")){

                    }

                }else if (TableName.equals("comment_list")){
                    List<String> cond = (List<String>)data;
                    if (cond.get(0).equals("ARTICLE")){
                        String sql = "SELECT * FROM comment_list WHERE article_id = ? ORDER BY `post_time` DESC;";
                        statement = self.getPreparedStatement(sql);
                        statement.setInt(1, Integer.parseInt(cond.get(1)));
                    }else if (cond.get(0).equals("GUESTBOOK")){
                        String sql = "SELECT * FROM comment_list WHERE article_id = NULL ORDER BY `post_time` DESC;";
                        statement = self.getPreparedStatement(sql);
                    }else if (cond.get(0).equals("REPLIES")){
                        String sql = "SELECT count(*) `replies` FROM comment_list WHERE article_id = ?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setInt(1, Integer.parseInt(cond.get(1)));
                    }else if (cond.get(0).equals("LATEST")) {
                        String sql = "SELECT comment_id, article_id, LEFT(content, 20) `content`, usertype, " +
                                "nickname, email, website, post_time FROM comment_list ORDER BY `post_time` DESC LIMIT ?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setInt(1, Integer.parseInt(cond.get(1)));
                    }
                }else if (TableName.equals("sys_info")){
                    List<String> cond = (List<String>)data;
                    if(cond.get(0).equals("ALL")){
                        String sql = "SELECT * FROM sys_info;";
                        statement = self.getPreparedStatement(sql);
                    }else if (cond.get(0).equals("NOTICE")){
                        String sql = "SELECT * FROM sys_info WHERE property='notice';";
                        statement = self.getPreparedStatement(sql);
                    }
                }else if (TableName.equals("user_auth")){
                    String sql = "SELECT * FROM user_auth WHERE username = ?;";
                    statement = self.getPreparedStatement(sql);
                    statement.setString(1, (String)data);
                }else if (TableName.equals("user_info")){
                    String sql = "SELECT * FROM user_info WHERE username = ?;";
                    statement = self.getPreparedStatement(sql);
                    statement.setString(1, (String)data);
                }else if (TableName.equals("msg_list")){
                    String sql = "SELECT * FROM msg_list WHERE username = ? ORDER BY post_time DESC;";
                    statement = self.getPreparedStatement(sql);
                    statement.setString(1, (String)data);
                }
                return statement;
            }

            @Override
            public PreparedStatement updateAdapter(Object data, String TableName, MysqlAdapter self) throws SQLException{
                PreparedStatement statement = null;
                if (TableName.equals("articles")){
                    List<String> cond = (List<String>)data;
                    if(cond.get(0).equals("ID")){
                        String sql = "UPDATE articles SET title = ?, summary = ?, content = ?, username = ?," +
                                "tags = ?, post_time = NOW() WHERE article_id = ?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setString(1, cond.get(1).toString());
                        statement.setString(2, cond.get(2).toString());
                        statement.setString(3, cond.get(3).toString());
                        statement.setString(4, cond.get(4).toString());
                        statement.setString(5, cond.get(5).toString());
                        statement.setString(6, cond.get(6).toString());
                    }else if (cond.get(0).equals("SOME")){
                        String sql = "UPDATE articles SET ";
                        int count = Integer.parseInt(cond.get(1));
                        for(int i = 0;i < count;++i){
                            String set = cond.get(2 + i);
                            if(set.equals("TITLE"))
                                sql += "title = ?,";
                            else if(set.equals("SUMMARY"))
                                sql += "summary = ?,";
                            else if(set.equals("CONTENT"))
                                sql += "content = ?,";
                            else if(set.equals("USERNAME"))
                                sql += "username = ?,";
                            else if(set.equals("TAGS"))
                                sql += "tags = ?,";
                        }
                        sql += "post_time = NOW() WHERE article_id = ?;";
                        statement = self.getPreparedStatement(sql);
                        for(int i = 0;i < count + 1;++i){
                            statement.setString(i + 1,cond.get(count + 2 + i));
                        }
                    }
                }else if (TableName.equals("user_auth")){
                    String sql = "UPDATE user_auth SET password = ? WHERE username = ?";
                    List<String> cond = (List<String>)data;
                    statement = self.getPreparedStatement(sql);
                    statement.setString(1, cond.get(1));
                    statement.setString(2, cond.get(0));
                }else if (TableName.equals("user_info")){

                }else if (TableName.equals("sys_info")){

                }else if (TableName.equals("comment_list")){

                }else if (TableName.equals("msg_list")){
                    List<String> cond = (List<String>)data;
                    if(cond.get(0).equals("READ")){
                        String sql = "UPDATE msg_list SET `read` = 1 WHERE username = ?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setString(1, cond.get(1));
                    }
                }
                return statement;
            }

            @Override
            public PreparedStatement searchAdapter(Object data, String TableName, MysqlAdapter self) throws SQLException {
                PreparedStatement statement = null;
                if (TableName.equals("articles")){
                    List<String> cond = (List<String>)data;
                    if(cond.get(0).equals("CONTENT")) {
                        String sql = "SELECT * FROM articles WHERE content LIKE ?;";
                        statement = self.getPreparedStatement(sql);
                        statement.setString(1, "%" + cond.get(1) + "%");
                    }
                }
                return statement;
            }
        });
        config.getServletContext().setAttribute("mysql", Util.newMysqlAdapter());
        Util.setDeployPath(config.getServletContext().getRealPath(File.separator));
    }
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
