package site.bulibucai;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

/**
 * 使用两种方式来加载 MyBatis 的核心配置，并创建 {@link SqlSessionFactory}
 */
public class EnableMyBatisTest {

    private SqlSessionFactory sqlSessionFactory;

    /**
     * 使用配置文件的方式来加载配置
     */
    @Test
    public void sqlSessionFactoryTest() throws IOException {
        String resource = "mybatis-configuration.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        Assertions.assertNotNull(sqlSessionFactory);
    }

    /**
     * 使用 Java 代码的方式来加载配置
     */
    @Test
    public void sqlSessionFactory2Test() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/ian";
        String username = "root";
        String password = "root";
        DataSource dataSource = new PooledDataSource(driver, url, username, password);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("dev", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        Assertions.assertNotNull(sqlSessionFactory);
    }
}
