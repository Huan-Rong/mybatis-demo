package site.bulibucai;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import site.bulibucai.entity.User;
import site.bulibucai.mapper.UserMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlSessionTest {

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeAll
    public static void setSqlSessionFactory() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-configuration.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        Assertions.assertNotNull(sqlSessionFactory);
    }

    @Test
    public void UserMapperTest() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Assertions.assertNotNull(sqlSession);
        try {
            User user = sqlSession.selectOne("site.bulibucai.mapper.UserMapper.selectUser", 1);
            Assertions.assertNotNull(user);
            Assertions.assertEquals("ian", user.getLastName());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void BasicCrudTest() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            System.out.println(mapper);

            // 测试新增
            boolean success = mapper.addUser(new User("lala", "2", "nana@yeah.net"));
            Assertions.assertEquals(true, success);

            // 测试修改
            User user = new User(2, "eric", "1", "zhr@yeah.net");
            success = mapper.updateUser(user);
            Assertions.assertEquals(true, success);

            // 测试删除
            success = mapper.deleteById(1);
            Assertions.assertEquals(false, success);
        } finally {
            sqlSession.commit();
        }
    }

    /**
     * 插入记录之后，获取相应的主键
     */
    @Test
    public void generatedIdTest() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = new User("afasdf", "2", "iii@yeah.net");
            Assertions.assertNull(user.getId());
            boolean success = mapper.addUserAndGetId(user);
            Assertions.assertEquals(true, success);
            Assertions.assertNotNull(user.getId());
        } finally {
            sqlSession.commit();
        }
    }

    /**
     * 多个参数
     */
    @Test
    public void selectByIdAndName() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            // Without @Param Annotation
            User user = mapper.selectByIdAndLastName(3, "lala");
            Assertions.assertNotNull(user);
            System.out.println(user);

            // Using @Param Annotation
            user = mapper.selectByIdAndLastName(4, "iii");
            Assertions.assertNotNull(user);
            System.out.println(user);

            // Using Map<String, Object>
            user = mapper.selectByIdAndLastName(2, "eric");
            Assertions.assertNotNull(user);
            System.out.println(user);

            // Using List<Integer>
            List<Integer> list = new ArrayList<Integer>();
            list.add(4);
            user = mapper.selectByIds(list);
            Assertions.assertNotNull(user);
            System.out.println(user);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 不同的返回值
     */
    @Test
    public void selectAndReturnDiff() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            // 返回 List
            List<User> users = mapper.selectByName("%i%");
            Assertions.assertEquals(2, users.size());
            System.out.println(users);

            // 返回一条记录的 Map 结构
            Map<String, Object> stringObjectMap = mapper.selectByIdReturnMap(2);
            Assertions.assertEquals("eric", stringObjectMap.get("last_name"));
            System.out.println(stringObjectMap);

            // 返回多条记录的 Map 结构
            Map<Integer, User> map = mapper.selectByNameReturnMap("%i%");
            Assertions.assertEquals(2, map.keySet().size());
            System.out.println(map);
        } finally {
            sqlSession.close();
        }
    }
}
