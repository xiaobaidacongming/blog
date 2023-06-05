package com.mingge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingge.constants.SystemConstants;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.UserAddDto;
import com.mingge.domain.dto.UserDto;
import com.mingge.domain.dto.UserPutDto;
import com.mingge.domain.dto.UserStatusDto;
import com.mingge.domain.entity.Role;
import com.mingge.domain.entity.User;
import com.mingge.domain.vo.*;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.exception.SystemException;
import com.mingge.mapper.RoleMapper;
import com.mingge.mapper.UserMapper;
import com.mingge.service.UserService;
import com.mingge.utils.BeanCopyUtils;
import com.mingge.utils.RedisCache;
import com.mingge.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-10-03 22:11:57
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id 从SecurityContext获取
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override  // 改进==》保证数据一致性
    public ResponseResult updateUserInfo(User user) {
        // 再严谨一些就是更新完用户信息之后，删除redis中的缓存，重新将用户信息添加到缓存中
        // 当前做法用户重新登陆也可以看到最新数据
        return userMapper.modifyId(user) > 0 ?ResponseResult.okResult(): ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_USER_INFO_ERROR);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        // 传过来的用户注册信息不能为空  null || “ ”
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        // 对用户名和昵称是否存在的判断
        User selectUser = userMapper.selectUserByuserNameOrNickName(user.getUserName(), user.getNickName());
        if (selectUser != null){
            if (StringUtils.hasText(selectUser.getUserName())) {
                throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
            }else if (StringUtils.hasText(selectUser.getNickName())){
                throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
            }
        }
        // 进行密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        // 将信息保存到数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUsers(UserDto userDto) {
        PageHelper.startPage(userDto.getPageNum(),userDto.getPageSize());
        List<UserVo> userVos = userMapper.selectUsers(userDto);
        PageInfo<UserVo> pageInfo = new PageInfo<>(userVos);
        PageVo pageVo = new PageVo(pageInfo.getList(),pageInfo.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult addUser(UserAddDto userDto) {
        // 判断用户名和密码是否为空
        isNull(userDto);
        // 判断用户名、手机号、邮箱是否已存在
        idExist(userDto);
        // 加密密码
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        // 添加用户
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        userMapper.insert(user);
        // 添加用户所对应的角色信息
        userMapper.addUserAndRole(user.getId(),userDto.getRoleIds());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(Long id) {
        // 删除用户（逻辑删除） => 不用删除所关联的权限信息
        return userMapper.deleteById(id) > 0 ? ResponseResult.okResult(): ResponseResult.errorResult(AppHttpCodeEnum.ERROR);
    }

    @Override
    public ResponseResult getUser(Long id) {
        // 获取用户信息
        UserByIdVo userVo =userMapper.selectUserById(id);
        // 用户所关联的角色id列表
        List<Long> roleIds = userMapper.selectRoleIdsByUserId(id);
        // 所有角色的列表
        List<Role> roles = roleMapper.selectList(null);
        return ResponseResult.okResult(new UserAndRolesVo(roleIds,roles,userVo));
    }

    @Override
    @Transactional
    public ResponseResult modifyUser(UserPutDto userDto) {
        // 修改用户信息
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        userMapper.updateById(user);
        // 修改角色信息  先删除再添加
        userMapper.deleteRoleByUserId(user.getId());
        userMapper.addRoleUser(user.getId(),userDto.getRoleIds());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult modifyUserStatus(UserStatusDto userStatusDto) {
        userMapper.updateByUserId(userStatusDto);
        return ResponseResult.okResult();
    }

    private void isNull(UserAddDto userDto){
        if (!StringUtils.hasText(userDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!StringUtils.hasText(userDto.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
    }
    private void idExist(UserAddDto userDto){
        if (StringUtils.hasText(userDto.getPhonenumber())){
            if (userMapper.selectByphonenumber(userDto.getPhonenumber())!=null){
                throw new SystemException(AppHttpCodeEnum.PHONE_EXIST);
            }
        }
        if (StringUtils.hasText(userDto.getEmail())){
            User user = userMapper.selectByEmail(userDto.getEmail());
            if (user != null) throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if (userMapper.selectByUserName(userDto.getUserName())!=null){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
    }
}
