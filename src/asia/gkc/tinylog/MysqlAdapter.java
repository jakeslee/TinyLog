package asia.gkc.tinylog;

import java.sql.*;
import java.util.*;

/**
 * Created by jakes on 14-11-30.
 */
public class MysqlAdapter {
    private static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    private static Map<Connection, Integer> pool = null;
    private static String DB_HOST, DB_NAME;
    private static String DB_USER, DB_PASS;
    private static QueryAdapter queryAdapter;
    private Connection conn = null;

    private int poolSize = 10;


    static {
        try {
            Class.forName(JDBC_DRIVER);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public interface QueryAdapter {
        public PreparedStatement insertAdapter(List<String> data, String TableName, MysqlAdapter self) throws SQLException;
        public PreparedStatement deleteAdapter(Object data, String TableName, MysqlAdapter self) throws SQLException;
        public PreparedStatement selectAdapter(Object data, String TableName, MysqlAdapter self) throws SQLException;
        public PreparedStatement updateAdapter(Object data, String TableName, MysqlAdapter self) throws SQLException;
        public PreparedStatement searchAdapter(Object data, String TableName, MysqlAdapter self) throws SQLException;
    }

    public static void setConfiguration(String hostname, String username, String password, String database_name){
        DB_NAME = database_name;
        DB_HOST = hostname;
        DB_USER = username;
        DB_PASS = password;
    }

    public static void setAdapter(QueryAdapter adapter){
        queryAdapter = adapter;
    }

    public static MysqlAdapter createMysqlAdapter() throws MysqlNotCreatedException {
        return new MysqlAdapter();
    }

    public MysqlAdapter() throws MysqlNotCreatedException{
        if(pool == null && !initPool())
            throw new MysqlNotCreatedException("Can't create Mysql connection.");
        try {
            conn = getConnectionFromPool();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public MysqlAdapter(String hostname, String username, String password, String database_name)
            throws MysqlNotCreatedException{
        this.DB_NAME = database_name;
        this.DB_HOST = hostname;
        this.DB_USER = username;
        this.DB_PASS = password;
        if(pool == null && !initPool())
            throw new MysqlNotCreatedException("Can't create Mysql connection.");
        try {
            conn = getConnectionFromPool();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private synchronized boolean initPool(){
        System.out.println("初始化连接池");
        pool = new HashMap<Connection, Integer>();
        String DB_URL = "jdbc:mysql://" + DB_HOST + "/" +DB_NAME + "?useUnicode=true&characterEncoding=utf-8";
        try {
            for (int i = 0;i < poolSize;++i){
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                pool.put(conn, 0);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        if (pool.size() <= 0){
            return false;
        }else {
            return true;
        }
    }

    private Connection getConnectionFromPool() throws Exception{
        if ((pool == null || pool.size() <= 0) && !initPool()){
            throw new Exception("Cannot create pool.");
        }
        Map.Entry<Connection, Integer> minEntry = null;
        synchronized (pool) {
            for (Map.Entry<Connection, Integer> entry : pool.entrySet()) {
                if (minEntry == null || entry.getValue().compareTo(minEntry.getValue()) < 0) {
                    minEntry = entry;
                }
            }
            minEntry.setValue(minEntry.getValue()+1);
        }
        return minEntry.getKey();
    }

    private boolean releaseConnection(){
        synchronized (pool){
            int value = pool.get(conn);
            if (value <= 0)
                return false;
            pool.put(conn, --value);
        }
        return true;
    }

    private void deleteConnection(Connection connection){
        System.out.println("删除连接中");
        if (connection == null)
            return;
        synchronized (pool){
            pool.remove(connection);
            try {
                if (!connection.isClosed())
                    connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private void destroyPool(){
        synchronized (pool) {
            pool.clear();
            for (Connection connection : pool.keySet()) {
                deleteConnection(connection);
            }
        }
    }

    public static void printPool(){
        for (Integer integer : pool.values()) {
            System.out.print(integer + "|");
        }
        System.out.println();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeConnection();
    }

    public boolean closeConnection(){
        releaseConnection();
        conn = null;
        return true;
    }

    public boolean queryUpdate(String sql){
        try {
            PreparedStatement state = conn.prepareStatement(sql);
            state.executeUpdate();
            state.close();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet queryQuery(String sql){
        try {
            PreparedStatement state = conn.prepareStatement(sql);
            return state.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(List<String> data, String toTable){
        try {
            PreparedStatement state = queryAdapter.insertAdapter(data, toTable, this);
            System.out.println(state);
            state.executeUpdate();
            state.close();
        }catch (SQLException e){
            return  false;
        }
        return true;
    }

    public boolean delete(Object condition, String fromTable){
        try {
            PreparedStatement state = queryAdapter.deleteAdapter(condition, fromTable, this);
            System.out.println(state.toString());
            state.executeUpdate();
            state.close();
        }catch (SQLException e){
            return  false;
        }
        return true;
    }

    public ResultObject select(Object condition, String fromTable){
        try {

            PreparedStatement state = queryAdapter.selectAdapter(condition, fromTable, this);
            System.out.println(state);
            return new ResultObject(state.executeQuery(), state);
        }catch (SQLException e){
            return new ResultObject(null, null);
        }
    }

    public boolean update(List<String> data, String onTable){
        try {
            PreparedStatement state = queryAdapter.updateAdapter(data, onTable, this);
            System.out.println(state.toString());
            state.executeUpdate();
            state.close();
        }catch (SQLException e){
            e.printStackTrace();
            return  false;
        }
        return true;
    }

    public ResultObject search(Object data, String onTable){
        try {
            PreparedStatement state = queryAdapter.searchAdapter(data, onTable, this);
            System.out.println(state.toString());
            return new ResultObject(state.executeQuery(), state);
        }catch (SQLException e){
            return new ResultObject(null, null);
        }

    }

    public PreparedStatement getPreparedStatement(String sql){
        try {

            while (conn == null || !conn.isValid(1)){
                System.out.println("连接失效！");
                deleteConnection(conn);
                conn = getConnectionFromPool();
            }
            return conn.prepareStatement(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

class ResultObject {
    private ResultSet resultSet;
    private PreparedStatement statement;

    public ResultObject(ResultSet resultSet, PreparedStatement statement) {
        this.resultSet = resultSet;
        this.statement = statement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public boolean close(){
        try {
            if (resultSet == null || statement == null)
                return true;
            resultSet.close();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}

class MysqlNotCreatedException extends Exception{
    public MysqlNotCreatedException(String msg){
        super(msg);
    }
}
