package io.codextension.pi.boot;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.concurrent.Executor;

/**
 * Created by eelkhour on 24.02.2017.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"io.codextension.pi.repository"})
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = "io.codextension.pi")
public class BootApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BootApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BootApplication.class, args);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    // Of course , you can define the Executor too
    @Bean
    public Executor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:h2:~/data/pi", "sa", "sa");
        return dataSource;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter bean = new HibernateJpaVendorAdapter();
        bean.setDatabase(Database.H2);
        bean.setGenerateDdl(true);
        return bean;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setPersistenceUnitName("PiUnit");
        bean.setDataSource(dataSource);
        bean.setJpaVendorAdapter(jpaVendorAdapter);
        bean.setPackagesToScan("io.codextension.pi.model");
        return bean;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @PostConstruct
    public void init() {
        LOG.info("Enabling GPIO ...");
        Gpio.wiringPiSetupGpio();

        GpioUtil.export(5, GpioUtil.DIRECTION_OUT);
        GpioUtil.export(6, GpioUtil.DIRECTION_OUT);
        GpioUtil.export(13, GpioUtil.DIRECTION_OUT);

        Gpio.pinMode(5, Gpio.OUTPUT);
        Gpio.pinMode(6, Gpio.OUTPUT);
        Gpio.pinMode(13, Gpio.OUTPUT);

    }


    @PreDestroy
    public void destroy() {
        GpioUtil.unexport(5);
        GpioUtil.unexport(6);
        GpioUtil.unexport(13);
    }
}
