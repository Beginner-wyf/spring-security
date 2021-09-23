package com.wyf.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wyf.springsecurity.entity.Users;
import com.wyf.springsecurity.mapper.UserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangyifan
 */
@Service
public class UserDetailImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    /**
     * 根据用户名获取UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getUserName, userName);
        Users user = userMapper.selectOne(wrapper);
        if (user == null) {
            //数据库没有当前对象，认证失败
            throw new UsernameNotFoundException("用户" + userName + "不存在");
        }
        List<GrantedAuthority> role = AuthorityUtils.commaSeparatedStringToAuthorityList("Role_program1,ROLE_manager");
        return new User(user.getUserName(), new BCryptPasswordEncoder().encode(user.getPassWord()), role);
    }
}
