package asia.gkc.tinylog;

import java.util.List;

/**
 * Created by jakes on 14-12-3.
 */
public class Article {
    private int             id;
    private String          title;
    private String          summary;
    private String          content;
    private String          username;
    private String          realname;
    private List<String>    tags;
    private int             replies;
    private String          time;//yyyy-MM-dd
    private String          time_md;//MM-dd
    private String          time_all;//full

    public String getTime_all() {
        return time_all;
    }

    public void setTime_all(String time_all) {
        this.time_all = time_all;
    }

    public String getTime_md() {
        return time_md;
    }

    public void setTime_md(String time_md) {
        this.time_md = time_md;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
