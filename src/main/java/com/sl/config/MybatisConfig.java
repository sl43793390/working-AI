package com.sl.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class MybatisConfig implements TransactionManagementConfigurer {
	
	@Value("${dbclassName}")
	private String driverClassName;
	
	@Value("${dbDemo1UserName}")
	private String dbDemo1UserName;
	@Value("${dbDemo1Password}")
	private String dbDemo1Password;
	
	
	@Value("${client1Url}")
	private String client1Url;
	
	@Value("${connectionTimeout}")
	private long connectionTimeout;
    //一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟  
	
	@Value("${idleTimeout}")
    private long idleTimeout;
    //一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒以上，  
    //参考MySQL wait_timeout参数（show variables like '%timeout%';）  
	@Value("${maxLifetime}")
    private long maxLifetime;
    //连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count) 
	@Value("${maximumPoolSize}")
    private int maximumPoolSize;
	
	@Bean(destroyMethod = "close")
	@Primary
    public DataSource demo(){  
		HikariConfig config = new HikariConfig();  
        config.setDriverClassName(driverClassName);  
        config.setJdbcUrl(client1Url);  
        config.setUsername(dbDemo1UserName);  
        config.setPassword(dbDemo1Password);  
        config.setConnectionTimeout(connectionTimeout);   
        config.setIdleTimeout(idleTimeout);  
        config.setMaxLifetime(maxLifetime);  
        config.setMaximumPoolSize(maximumPoolSize);  
        HikariDataSource ds = new HikariDataSource(config);  
        return ds; 
	}
	   
    @Bean
    public SqlSessionFactory demoSqlSessionFactory(@Qualifier("demo") DataSource demo) throws Exception {
    	 MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
         sqlSessionFactoryBean.setDataSource(demo);
         PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
         sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapping/*.xml"));
         sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:/mybatis/configuration.xml"));
         return sqlSessionFactoryBean.getObject();
    }
    
    
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
    	DataSourceTransactionManager dynamicDb = new DataSourceTransactionManager(demo());   
        ChainedTransactionManager ctm = new ChainedTransactionManager(dynamicDb);  
        return ctm;  
//    	return new DataSourceTransactionManager(demo());
    }
    
}
