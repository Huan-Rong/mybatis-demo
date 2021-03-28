package site.bulibucai.mapper;

import org.apache.ibatis.annotations.Param;
import site.bulibucai.entity.Employee;

public interface EmployeeMapper {

    Employee selectById(@Param("id") Integer id);

    Employee selectById2(@Param("id") Integer id);

    Employee selectById3(@Param("id") Integer id);
}
