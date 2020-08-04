package com.frank.config;

import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 第一个数据源配置（chat_log_{header..detail}_{0..511}）
 *
 * @author Created by hawei
 * @create 2019/11/4 3:25 PM
 */
@Configuration
@MapperScan(basePackages = "com.wekj.cag.dao.first", sqlSessionTemplateRef = "firstSqlSessionTemplate")
public class DS1Config {

    @Bean(name = "firstDataSource")
    @ConfigurationProperties(prefix = "sharding.datasource.first")
    @Primary
    public DataSource firstDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "firstSqlSessionFactory")
    @Primary
    public SqlSessionFactory firstSqlSessionFactory(@Qualifier("firstDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("com.frank.entity");
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/first/*.xml"));
        bean.setVfs(SpringBootVFS.class);
        return bean.getObject();
    }

    @Bean(name = "firstTransactionManager")
    @Primary
    public DataSourceTransactionManager firstTransactionManager(@Qualifier("firstDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "firstSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate firstSqlSessionTemplate(@Qualifier("firstSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
