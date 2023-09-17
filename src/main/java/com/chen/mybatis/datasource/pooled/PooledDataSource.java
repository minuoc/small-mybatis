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


    protected void puhConnection(PooledConnection connection) throws SQLException {

        synchronized (state) {
            state.activeConnections.remove(connection);
            // 判断链接是否有效
            if (connection.isValid()) {
                // 如果空闲链接小于设定的数量 也就是太少时
                if (state.idleConnections.size() < poolMaximumIdleConnections
                && connection.getConnectionTypeCode() == expectedConnectionTypeCode) {
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (!connection.getRealConnection().getAutoCommit()) {
                        connection.getRealConnection().rollback();
                    }
                    //实例化一个新的DB 连接，加入到idle 列表
                    PooledConnection newConnection = new PooledConnection(connection.getRealConnection(), this);
                    state.idleConnections.add(newConnection);
                    newConnection.setCreatedTimestamp(connection.getCreatedTimestamp());
                    newConnection.setLastUsedTimestamp(connection.getLastUsedTimestamp());
                    logger.info("Returned connection " + newConnection.getRealConnection() + " to pool.");

                    state.notifyAll();

                } else {
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (!connection.getRealConnection().getAutoCommit()) {
                        connection.getRealConnection().rollback();
                    }
                    // 将connection 关闭
                    connection.getRealConnection().close();
                    logger.info("Closed connection " + connection.getRealHashCode() + ".");
                    connection.invalidate();

                }
            } else {
                logger.info("A bad connection (" + connection.getRealConnection() + ") attempted to return to the pool, discarding connection.");
                state.badConnectionCount ++;
            }

        }

    }


    private PooledConnection popConnection(String username, String password) throws SQLException {
        boolean countedWait = false;
        PooledConnection conn = null;
        long t = System.currentTimeMillis();
        int localBadConnectionCount = 0;
        while (conn == null ){
            synchronized (state) {
                //如果有空闲连接：返回第一个
                if (!state.idleConnections.isEmpty()) {
                    conn = state.idleConnections.remove(0);
                    logger.info("Checked out connection " + conn.getRealConnection() + " from pool.");
                }
                // 如果无空闲连接：创建新的连接
                else {
                    // 活跃 连接数不足
                    if (state.activeConnections.size() < poolMaximumActiveConnections) {
                        conn = new PooledConnection(dataSource.getConnection(), this);
                        logger.info("Created connection " + conn.getRealHashCode() + ".");
                    } else {
                        //活跃连接数 已满, 获取活跃连接列表的第一个 也就是最老的一个连接
                        PooledConnection oldestActiveConnection = state.activeConnections.get(0);
                        long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
                        // 如果checkout时间过长, 则这个连接标记为过期
                        if (longestCheckoutTime > poolMaximumCheckoutTime) {
                            state.claimedOverdueConnectionCount ++;
                            state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                            state.accumulatedCheckoutTime += longestCheckoutTime;
                            state.activeConnections.remove(oldestActiveConnection);
                            if (!oldestActiveConnection.getRealConnection().getAutoCommit()) {
                                oldestActiveConnection.getRealConnection().rollback();
                            }
                            //删除掉最老的连接 然后重新实例化一个新的连接
                            conn = new PooledConnection(oldestActiveConnection.getRealConnection(),this);
                            oldestActiveConnection.invalidate();
                            logger.info("Claimed overdue connection " + conn.getRealHashCode() + ".");
                        } else {
                            // 如果checkout超时时间不够长,则等待
                            try {
                                if (!countedWait) {
                                    state.hadToWaitCount ++;
                                    countedWait = true;
                                }
                                logger.info("Waiting as long as " + poolTimeToWait + " milliseconds for connection.");
                                long wt = System.currentTimeMillis();
                                state.wait(poolTimeToWait);
                                state.accumulatedCheckoutTime += System.currentTimeMillis() - wt;
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                }

            }
        }
        if (conn == null) {
            logger.debug("PooledDataSource: Unknown severe error condition. The connection pool return a null connection.");
            throw new SQLException("PooledDataSource: Unknown severe error condition. The connection pool return a null connection.");
        }

        return conn;
    }


    public void forceCloseAll(){
        synchronized (state) {

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

    public boolean pingConnection(PooledConnection connection) {

        return true;
    }
}
