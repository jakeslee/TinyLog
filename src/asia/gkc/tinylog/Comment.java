package asia.gkc.tinylog;

/**
 * Created by jakes on 14-12-4.
 */
public class Comment {
    private int             id;
    private int             target;
    private String          content;
    private int             usertype;
    private String          username;
    private String          nickname;
    private String          avatar;
    private String          email;
    private String          website;
    private String          time;//yyyy-MM-dd
    private String          time_md;//MM-dd
    private String          time_all;//full

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTime_all() {
        return time_all;
    }

    public void setTime_all(String time_all) {
        this.time_all = time_all;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime_md() {
        return time_md;
    }

    public void setTime_md(String time_md) {
        this.time_md = time_md;
    }
}
