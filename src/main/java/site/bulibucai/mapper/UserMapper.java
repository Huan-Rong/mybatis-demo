package site.bulibucai.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import site.bulibucai.entity.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    // BASIC CRUD
    User selectUser(Integer id);

    boolean addUser(User user);

    boolean updateUser(User user);

    boolean deleteById(Integer id);

    // 获取新增记录的主键
    boolean addUserAndGetId(User user);

    // 多个参数
    User selectByIdAndLastName(Integer id, String lastName);

    User selectByIdAndLastNameUsingParamAnn(@Param("id") Integer id, @Param("lastName") String lastName);

    User selectByIdAndNameUsingMap(Map<String, Object> map);

    User selectByIds(List<Integer> ids);

    // 不同的返回值
    List<User> selectByName(@Param("lastName") String lastName);

    Map<String, Object> selectByIdReturnMap(@Param("id") Integer id);

    @MapKey("id")
    Map<Integer, User> selectByNameReturnMap(@Param("lastName") String lastName);
}
