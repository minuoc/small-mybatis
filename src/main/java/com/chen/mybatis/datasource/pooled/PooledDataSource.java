package com.chen.mybatis.datasource.pooled;

import com.chen.mybatis.datasource.unpooled.UnpooledDataSource;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 有连接池的数据源
 */
public class PooledDataSource implements DataSource {


    private org.slf4j.Logger logger = LoggerFactory.getLogger(PooledDataSource.class);

    private final PoolState state = new PoolState(this);

    private final UnpooledDataSource dataSource;

    /**
     * 活跃连接数
     */
    protected int poolMaximumActiveConnections = 10;

    /**
     * 空闲连接数
     */
    protected int poolMaximumIdleConnections = 5;
    /**
     * 在被强制返回之前 池中连接被检查的时间
     */
    protected int poolMaximumCheckoutTime = 20000;

    /**
     * 这是给连接池一个打印日志状态机会的低层次设置，还有重试尝试获得连接，这些情况下往往需要很长的时间 为了避免连接池没有配置时静默失败
     */
    protected int poolTimeToWait = 20000;

    protected String poolPingQuery = "NO PING QUERY SET";

    /**
     * 开启或者禁用侦测查询
     */
    protected boolean poolPingEnabled = false;

    /**
     * 用来配置poolPingQuery 多次时间被用一次
     */
    protected int poolPingConnectionNotUsedFor = 0;


    private int expectedConnectionTypeCode;

    public PooledDataSource(UnpooledDataSource dataSource) {
        this.dataSource = dataSource;
    }


    protected void puhConnection(PooledConnection connection) {

        synchronized (state) {
            state.activeConnections.remove(connection);
            // 判断链接是否有效
            if (connection.isValid()) {
                // 如果空闲链接小于设定的数量 也就是太少时
                if (state.idleConnections.size() < poolMaximumIdleConnections
                && connection.getConnectionTypeCode() == expectedConnectionTypeCode) {
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (!connection.getRealConnection())
                }
            }

        }

    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
