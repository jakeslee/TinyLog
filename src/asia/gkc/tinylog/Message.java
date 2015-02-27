package asia.gkc.tinylog;

/**
 * Created by jakes on 14-12-23.
 */
public class Message {
    private int             id;
    private String          username;
    private String          msg_type;
    private String          content;
    private String          target;
    private int             read;
    private String          time;//yyyy-MM-dd
    private String          time_md;//MM-dd
    private String          time_all;//full

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
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

    public String getTime_all() {
        return time_all;
    }

    public void setTime_all(String time_all) {
        this.time_all = time_all;
    }
}
