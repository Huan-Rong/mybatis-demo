package site.bulibucai;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import site.bulibucai.entity.Employee;
import site.bulibucai.mapper.EmployeeMapper;

import java.io.IOException;
import java.io.InputStream;

public class EmployeeMapperTest {

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeAll
    public static void beforeAll() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-configuration.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void SqlSessionFactoryTest() {
        Assertions.assertNotNull(sqlSessionFactory);
    }

    @Test
    public void selectByIdTest() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.selectById(1);
            Assertions.assertNotNull(employee);
            Assertions.assertNotNull(employee.getDept());
            Assertions.assertNotNull(employee.getDept().getId());
            System.out.println(employee);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void selectById2Test() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.selectById2(1);
            Assertions.assertNotNull(employee);
            Assertions.assertNotNull(employee.getDept());
            Assertions.assertNotNull(employee.getDept().getId());
            System.out.println(employee);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void selectById3Test() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.selectById3(1);
            System.out.println(employee.getName());
            System.out.println(employee.getDept().getName());
        } finally {
            sqlSession.close();
        }
    }
}
