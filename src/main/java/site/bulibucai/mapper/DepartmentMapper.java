package site.bulibucai.mapper;

import org.apache.ibatis.annotations.Param;
import site.bulibucai.entity.Department;

public interface DepartmentMapper {
    Department selectById(@Param("id") Integer id);

    Department selectByIdReturnEmps(@Param("id") Integer id);
}
