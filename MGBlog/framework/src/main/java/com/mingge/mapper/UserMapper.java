package com.mingge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingge.domain.dto.UserDto;
import com.mingge.domain.dto.UserStatusDto;
import com.mingge.domain.entity.User;
import com.mingge.domain.vo.UserByIdVo;
import com.mingge.domain.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-02 15:30:52
 */
public interface UserMapper extends BaseMapper<User> {
    int modifyId(User user);
    User selectUserByuserNameOrNickName(@Param("userName") String userName,@Param("nickName")String nickName);
    List<UserVo> selectUsers(UserDto userDto);
    User selectByUserName(@Param("userName") String userName);
    User selectByEmail(@Param("email") String email);
    User selectByphonenumber(@Param("phonenumber") String phonenumber);
    Integer addUserAndRole(@Param("userId")Long userId,@Param("roleIds")List<Long> roleIds);

    UserByIdVo selectUserById(@Param("id") Long id);
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
    // 删除用户所具有的角色信息
    Integer deleteRoleByUserId(@Param("userId") Long userId);
    // 添加用户所具有的角色信息
    Integer addRoleUser(@Param("userId")Long userId,@Param("roleIds")List<Long> roleIds);

    Integer updateByUserId(UserStatusDto userStatusDto);
}

