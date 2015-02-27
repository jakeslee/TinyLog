package asia.gkc.tinylog;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.io.*;
import java.security.*;

/**
 * Created by jakes on 14-12-5.
 */
public class Util {
    public static final int ADMIN = 2;
    private static String deployPath;

    public static String getDeployPath() {
        return deployPath;
    }

    public static void setDeployPath(String deployPath) {
        Util.deployPath = deployPath;
    }

    public static MysqlAdapter newMysqlAdapter() {
        try {
            return MysqlAdapter.createMysqlAdapter();
        } catch (MysqlNotCreatedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNotice() {
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        List<String> conditions = new ArrayList<String>();
        conditions.clear();
        conditions.add("NOTICE");
        ResultObject resultObject = mysqlAdapter.select(conditions, "sys_info");
        try {
            ResultSet resultSet = resultObject.getResultSet();
            if (resultSet != null && resultSet.next()) {
                return resultSet.getString("value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }
        return "";
    }

    public static String toRealname(String username) {
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        ResultObject resultObject = mysqlAdapter.select(username, "user_info");
        try {
            ResultSet resultSet = resultObject.getResultSet();
            if (resultSet != null && resultSet.next()) {
                return resultSet.getString("realname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }
        return "";
    }

    public static int getAuth(String username) {
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        ResultObject resultObject = mysqlAdapter.select(username, "user_auth");
        try {
            ResultSet resultSet = resultObject.getResultSet();
            if (resultSet != null && resultSet.next()) {
                return resultSet.getInt("authority");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }
        return 2;
    }

    public static String getAvatar(String username) {
        File file = new File(deployPath + "/img/avatar/" + username);
        if (file.exists())
            return "img/avatar/" + username;
        return null;
    }

    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static String md5Hex(String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAvatarFromEmail(String email) {
        if (email == null) {
            return "img/avatar/default.png";
        }
        String emailMd5 = md5Hex(email);
        File file = new File(deployPath + "/img/avatar/email/" + emailMd5);
        if (!file.exists()) {
            new Thread(new AvatarThread(email)).start();
            return "http://cn.gravatar.com/avatar/" + emailMd5 + "?s=40&r=x";
        }
        return "img/avatar/email/" + md5Hex(email);
    }

    public static int getNumberOfComment(String articleId) {
        List<String> conditions = new ArrayList<String>();
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        conditions.add("REPLIES");
        conditions.add(articleId);
        ResultObject resultObject = mysqlAdapter.select(conditions, "comment_list");
        try {
            ResultSet resultSet = resultObject.getResultSet();
            if (resultSet != null && resultSet.next()) {
                return resultSet.getInt("replies");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }
        return 0;
    }

    public static List<Article> getLatestArticles(String number) {
        List<String> conditions = new ArrayList<String>();
        List<Article> articles = new ArrayList<Article>();
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        conditions.add("LATEST");
        conditions.add(number);
        ResultObject resultObject = mysqlAdapter.select(conditions, "articles");
        try {
            ResultSet resultSet = resultObject.getResultSet();
            while (resultSet != null && resultSet.next()) {
                Article article = new Article();
                article.setId(resultSet.getInt("article_id"));
                article.setTitle(resultSet.getString("title"));
                article.setTime_md(new SimpleDateFormat("MM-dd").format(resultSet.getTimestamp("post_time")));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }
        return articles;
    }

    public static Article getArticle(String articleId) throws Exception {
        List<String> conditions = new ArrayList<String>();
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        conditions.add("ARTICLE");
        conditions.add(articleId);
        ResultObject resultObject = mysqlAdapter.select(conditions, "articles");
        try {
            ResultSet resultSet = resultObject.getResultSet();
            if (resultSet == null || !resultSet.next())
                throw new Exception("No this article");
            return Util.toArticle(resultSet);
        } catch (SQLException e) {
            throw new Exception("Unknown errors!");
        } finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }
    }

    public static Article toArticle(ResultSet resultSet) {
        return toArticle(resultSet, false);
    }

    public static Article toArticle(ResultSet resultSet, boolean isView) {
        Article article = new Article();
        try {
            //Basic content
            article.setId(resultSet.getInt("article_id"));
            article.setTitle(resultSet.getString("title"));
            article.setSummary(resultSet.getString("summary"));
            article.setContent(resultSet.getString("content"));
            article.setUsername(resultSet.getString("username"));

            //Create tags list
            List<String> tags = new ArrayList<String>();
            for (String tag : resultSet.getString("tags").split("\\s+")) {
                tags.add(tag);
            }
            article.setTags(tags);

            //Create time string
            java.sql.Timestamp timestamp = resultSet.getTimestamp("post_time");
            article.setTime_all(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp));
            article.setTime(new SimpleDateFormat("yyyy-MM-dd").format(timestamp));
            article.setTime_md(new SimpleDateFormat("MM-dd").format(timestamp));

            //Get real name or nickname
            //Get amount of comments
            if (isView) {
                article.setRealname(resultSet.getString("realname"));
                article.setReplies(resultSet.getInt("replies"));
            } else {
                article.setRealname(Util.toRealname(article.getUsername()));
                article.setReplies(Util.getNumberOfComment(Integer.toString(article.getId())));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    public static Message toMessage(ResultSet resultSet) {
        Message message = new Message();
        try {
            message.setId(resultSet.getInt("msg_id"));
            message.setMsg_type(resultSet.getString("msg_type"));
            message.setContent(resultSet.getString("content"));
            java.sql.Timestamp timestamp = resultSet.getTimestamp("post_time");
            message.setTime_all(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp));
            message.setTime(new SimpleDateFormat("yyyy-MM-dd").format(timestamp));
            message.setTime_md(new SimpleDateFormat("MM-dd").format(timestamp));
            message.setUsername(resultSet.getString("username"));
            message.setRead(resultSet.getInt("read"));
            if (message.getMsg_type().equals("2")) {
                message.setTarget(resultSet.getString("target"));
            } else {
                message.setTarget("系统");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static List<Message> getMessages(String username){
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        List<Message> messages = new ArrayList<Message>();
        ResultObject resultObject = mysqlAdapter.select(username, "msg_list");
        try {
            ResultSet resultSet = resultObject.getResultSet();
            while (resultSet != null && resultSet.next()){
                messages.add(toMessage(resultSet));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }
        return messages;
    }

    public static boolean addMessage(Message message){
        MysqlAdapter mysqlAdapter = newMysqlAdapter();
        List<String> condition = new ArrayList<String>();
        condition.add(message.getUsername());
        condition.add(message.getContent());
        condition.add(message.getMsg_type());
        condition.add(message.getTarget());
        if (mysqlAdapter.insert(condition, "msg_list") && mysqlAdapter.closeConnection())
            return true;
        else
            return false;
    }

    public static boolean hasNewMsg(String username){
        List<Message> messages = getMessages(username);
        for (Message message : messages){
            if (message.getRead() == 0)
                return true;
        }
        return false;
    }

    public static Comment toComment(ResultSet resultSet){
        Comment comment = new Comment();
        try {
            comment.setId(resultSet.getInt("comment_id"));
            comment.setTarget(resultSet.getInt("article_id"));
            comment.setContent(resultSet.getString("content"));
            comment.setUsertype(resultSet.getInt("usertype"));
            comment.setUsername(resultSet.getString("nickname"));
            comment.setEmail(resultSet.getString("email"));
            comment.setWebsite(resultSet.getString("website"));
            java.sql.Timestamp timestamp = resultSet.getTimestamp("post_time");
            comment.setTime_all(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp));
            comment.setTime(new SimpleDateFormat("yyyy-MM-dd").format(timestamp));
            comment.setTime_md(new SimpleDateFormat("MM-dd").format(timestamp));
            String avatar;
            if (comment.getUsertype() >= 1){
                avatar = getAvatar(comment.getUsername());
                if (avatar == null)
                    avatar = getAvatarFromEmail(comment.getEmail());
                comment.setNickname(new User(comment.getUsername()).getRealname());
            }else {
                comment.setNickname(comment.getUsername());
                avatar = getAvatarFromEmail(comment.getEmail());
            }
            comment.setAvatar(avatar);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return comment;
    }

    public static List<Comment> getComments(String articleId){
        List<String> conditions = new ArrayList<String>();
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        conditions.clear();
        conditions.add("ARTICLE");
        conditions.add(articleId);
        List<Comment> comments = new ArrayList<Comment>();
        ResultObject resultObject = mysqlAdapter.select(conditions, "comment_list");
        try {
            ResultSet resultSet = resultObject.getResultSet();
            while (resultSet != null && resultSet.next()){
                comments.add(toComment(resultSet));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }
        return comments;
    }

    public static List<Comment> getLatestComments(String number){
        List<String> conditions = new ArrayList<String>();
        List<Comment> comments = new ArrayList<Comment>();
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        conditions.add("LATEST");
        conditions.add(number);
        ResultObject resultObject = mysqlAdapter.select(conditions, "comment_list");
        try {
            ResultSet resultSet = resultObject.getResultSet();
            while (resultSet != null && resultSet.next()){
                comments.add(toComment(resultSet));
            }
            resultObject.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }
        return comments;
    }

    public static Site getSite(){
        return Site.getSite();
    }

    public static boolean isSigned(HttpSession session){
        String username = (String)session.getAttribute("username");
        if (username == null || username.isEmpty())
            return false;
        else
            return true;
    }

    public static boolean isDefined(String string){
        if (string == null || string.isEmpty())
            return false;
        else
            return true;
    }

}
