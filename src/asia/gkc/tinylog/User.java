package asia.gkc.tinylog;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jakes on 14-12-5.
 */
public class User {
    private String username;
    private String realname;
    private String email;
    private String location;
    private String avatar;
    private int auth;
    private boolean newMsg;

    public User() {
    }

    public User(String username) {
        this.username = username;
        MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
        ResultObject resultObject = mysqlAdapter.select(username, "user_info");
        ResultSet resultSet = resultObject.getResultSet();
        try {
            if (resultSet != null && resultSet.next()){
                setRealname(resultSet.getString("realname"));
                setEmail(resultSet.getString("email"));
                setLocation(resultSet.getString("location"));
                setAuth(auth);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            resultObject.close();
            mysqlAdapter.closeConnection();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", avatar='" + avatar + '\'' +
                ", auth=" + auth +
                ", newMsg=" + newMsg +
                '}';
    }

    public String getAvatar() {
        File file = new File(Util.getDeployPath() + "/img/avatar/" + avatar);
        if (file.exists())
            return "img/avatar/" + avatar;
        else
            return Util.getAvatarFromEmail(email);
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public boolean isNewMsg() {
        return newMsg;
    }

    public void setNewMsg(boolean newMsg) {
        this.newMsg = newMsg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
