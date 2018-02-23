package top.zzming.dao;

import org.apache.ibatis.annotations.Param;
import top.zzming.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    int insertUserRole(@Param("userId") Integer userId,@Param("roleId") Integer roleId);

    int activeUser(String activeCode);

    User selectByPrimaryKey(Integer userId);

    User selectByUsername(String username);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}