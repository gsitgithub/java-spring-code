package dev.gsitgithub.db;

import org.apache.commons.dbcp2.BasicDataSource;

import com.vladmihalcea.flexypool.FlexyPoolDataSource;
import com.vladmihalcea.flexypool.adaptor.DBCP2PoolAdapter;
import com.vladmihalcea.flexypool.config.Configuration;
import com.vladmihalcea.flexypool.strategy.IncrementPoolOnTimeoutConnectionAcquiringStrategy;
import com.vladmihalcea.flexypool.strategy.RetryConnectionAcquiringStrategy;

public class FlexyPoolConfiguration {
	private BasicDataSource poolingDataSource;

//    @Value("${flexy.pool.uniqueId}")
    private String uniqueId;

    public FlexyPoolConfiguration(BasicDataSource poolingDataSource) {
		this.poolingDataSource = poolingDataSource;
	}
//    @Bean
    public Configuration<BasicDataSource> configuration() {
        return new Configuration.Builder<BasicDataSource>(
                uniqueId,
                poolingDataSource,
                DBCP2PoolAdapter.FACTORY
        ).build();
    }

//    @Bean(initMethod = "start", destroyMethod = "stop")
    public FlexyPoolDataSource dataSource() {
        Configuration<BasicDataSource> configuration = configuration();
        return new FlexyPoolDataSource<BasicDataSource>(configuration,
                new IncrementPoolOnTimeoutConnectionAcquiringStrategy.Factory(5),
                new RetryConnectionAcquiringStrategy.Factory(2)
        );
    }
}
