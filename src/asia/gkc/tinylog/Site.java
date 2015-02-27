package asia.gkc.tinylog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakes on 14-12-8.
 */
public class Site {
    private static String name = "";
    private static String motto = "";
    private static String homeurl = "";
    private static String notice = "";

    private static int pageitems = 10;
    private static int newestarts = 6;
    private static int newestcmts = 5;

    private static Site site;

    private static List<String> conditions = new ArrayList<String>();

    public Site() {
        if (!Util.isDefined(name) || !Util.isDefined(motto) || !Util.isDefined(homeurl) || !Util.isDefined(notice)){
            MysqlAdapter mysqlAdapter = Util.newMysqlAdapter();
            conditions.add("ALL");
            ResultObject resultObject = mysqlAdapter.select(conditions, "sys_info");
            try {
                ResultSet resultSet = resultObject.getResultSet();
                while (resultSet != null && resultSet.next()){
                    if ("name".equals(resultSet.getString("property"))){
                        name = resultSet.getString("value");
                    }else if ("motto".equals(resultSet.getString("property"))){
                        motto = resultSet.getString("value");
                    }else if ("homeurl".equals(resultSet.getString("property"))){
                        homeurl = resultSet.getString("value");
                    }else if ("notice".equals(resultSet.getString("property"))){
                        notice = resultSet.getString("value");
                    }else if ("pageitems".equals(resultSet.getString("property"))){
                        pageitems = Integer.parseInt(resultSet.getString("value"));
                    }else if ("newestarts".equals(resultSet.getString("property"))){
                        newestarts = Integer.parseInt(resultSet.getString("value"));
                    }else if ("newestcmts".equals(resultSet.getString("property"))){
                        newestcmts = Integer.parseInt(resultSet.getString("value"));
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                resultObject.close();
            }
            mysqlAdapter.closeConnection();
        }
    }

    public void refresh(){
        motto = "";
        site = null;
    }

    public String getName() {
        return name;
    }

    public String getMotto() {
        return motto;
    }

    public String getHomeurl() {
        return homeurl;
    }

    public String getNotice() {
        return notice;
    }

    public static int getPageitems() {
        return pageitems;
    }

    public static Site getSite() {
        if (site == null)
            site = new Site();
        return site;
    }

    public static int getNewestcmts() {
        return newestcmts;
    }

    public static int getNewestarts() {
        return newestarts;
    }
}
