package dev.gsitgithub.db;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.*;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.impl.AbandonedConfig;

import com.haulmont.yarg.exception.InitializationException;

public final class DatasourceCreator {
    private DatasourceCreator() {
    }

    public static DataSource setupDataSource(String driver, String connectURI,
                                             String username,
                                             String password,
                                             Integer maxActive,
                                             Integer maxIdle,
                                             Integer maxWait) {
        try {
            Class.forName(driver);
//            final AbandonedConfig config = new AbandonedConfig();
//            config.setLogAbandoned(true);
//
//            AbandonedObjectPool connectionPool = new AbandonedObjectPool(null, config);
//
//            connectionPool.setMaxIdle(maxIdle);
//            connectionPool.setMaxActive(maxActive);
//            if (maxWait != null) {
//                connectionPool.setMaxWait(maxWait);
//            }
//
//            ConnectionFactory connectionFactory =
//                    new DriverManagerConnectionFactory(connectURI, username, password);
//
//            PoolableConnectionFactory poolableConnectionFactory =
//                    new PoolableConnectionFactory(
//                            connectionFactory, connectionPool, null, null, false, true);
//
//            connectionPool.setFactory(poolableConnectionFactory);
//            PoolingDataSource dataSource =
//                    new PoolingDataSource(connectionPool);

            BasicDataSource dataSource = new BasicDataSource();
            //Set database driver name
            dataSource.setDriverClassName(driver);
            //Set database url
            dataSource.setUrl(connectURI);
            //Set database user
            dataSource.setUsername(username);
            //Set database password
            dataSource.setPassword(password);
            //Set the connection pool size
            dataSource.setInitialSize(maxActive);
            
            FlexyPoolConfiguration flexyPool = new FlexyPoolConfiguration(dataSource);
            return flexyPool.dataSource();
        } catch (ClassNotFoundException e) {
            throw new InitializationException("An error occurred during creation of new datasource object", e);
        }
    }
}