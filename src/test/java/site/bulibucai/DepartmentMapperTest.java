package site.bulibucai;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import site.bulibucai.entity.Department;
import site.bulibucai.entity.Employee;
import site.bulibucai.mapper.DepartmentMapper;
import site.bulibucai.mapper.EmployeeMapper;

import java.io.IOException;
import java.io.InputStream;

public class DepartmentMapperTest {

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeAll
    public static void beforeAll() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-configuration.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void selectByIdReturnEmps() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
            Department department = mapper.selectByIdReturnEmps(1);
            System.out.println(department.getName());
            System.out.println(department.getEmps());
        } finally {
            sqlSession.close();
        }
    }
}
