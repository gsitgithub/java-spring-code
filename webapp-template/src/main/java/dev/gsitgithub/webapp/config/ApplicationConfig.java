package dev.gsitgithub.webapp.config;

import dev.gsitgithub.webapp.config.database.ImportSql;
import dev.gsitgithub.webapp.config.utils.ApplicationEnvironmentUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ErrorHandler;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@ComponentScan(basePackages = {"dev.gsitgithub.webapp.config", "dev.gsitgithub.webapp.service"})
@EnableTransactionManagement
@EnableJpaRepositories("dev.gsitgithub.webapp.repository")
@EnableMBeanExport
@EnableAspectJAutoProxy
public class ApplicationConfig {

    @Value("${application.environment}")
    private String applicationEnvironment;
    @Inject
    private JpaVendorAdapter jpaVendorAdapter;
    @Inject
    private DataSource dataSource;

    // Load environment specific application properties
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties(Environment environment) {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        ClassPathResource[] resources = {
            new ClassPathResource("application_" + environment.getProperty("application.environment") + ".properties")
        };
        pspc.setLocations(resources);
        return pspc;
    }

    // Jdbc Template
    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    // persistence.xml Properties
    Properties persistenceXmlProperties() {
        return new Properties() {
            {  // Hibernate
                setProperty("hibernate.hbm2ddl.auto", "create-drop");
                setProperty("hibernate.globally_quoted_identifiers", "true");

                // Hibernate Envers
                setProperty("org.hibernate.envers.auditTablePrefix", "");
                setProperty("org.hibernate.envers.auditTableSuffix", "_AUD");
                setProperty("org.hibernate.envers.storeDataAtDelete", "true");

                // Use Joda Time
                setProperty("jadira.usertype.autoRegisterUserTypes", "true");
                setProperty("jadira.usertype.javaZone", "jvm");
                setProperty("jadira.usertype.databaseZone", "jvm");
            }
        };
    }

    // Entity Manager Factory
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setJpaProperties(persistenceXmlProperties());
        entityManagerFactory.setPackagesToScan("dev.gsitgithub.webapp.entity");

        return entityManagerFactory;
    }

    // Exception Translation
    @Bean
    public HibernateExceptionTranslator exceptionTranslation() {
        return new HibernateExceptionTranslator();
    }

    // Transaction Manager
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    // JSR-303 Validation
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource reloadableMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("/WEB-INF/messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        if (applicationEnvironment.equals(ApplicationEnvironmentUtils.LOCALHOST)) {
            messageSource.setCacheSeconds(1);
        }
        return messageSource;
    }

    @Bean
    public ImportSql importSql() {
        return new ImportSql();
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);
        scheduler.setThreadNamePrefix("task-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);

        scheduler.setErrorHandler(new ErrorHandler() {
            @Override
            public void handleError(Throwable t) {
//				schedulingLogger.error("Unknown error occurred while executing task.", t);
            }
        });

        scheduler.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//				schedulingLogger.error("Execution of task {} was rejected for unknown reasons.", r);
            }
        });
        return scheduler;
    }
}
