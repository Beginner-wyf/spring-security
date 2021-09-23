package com.wyf.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author wangyifan
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        // 创建数据库操作对象
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        // 注入数据源
        jdbcTokenRepository.setDataSource(dataSource);
        // 如果没有手动创建表，我们可以开启自动创建表功能，此处因为创建了，我们设置为false
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //通过 userDetailsService 来获取用户相关信息，需对其进行实现
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //自定义没有权限时的跳转页面
        http.exceptionHandling().accessDeniedPage("/unauth.html");
        //自定义登录页面
        http.formLogin()
                .loginPage("/login.html")           //登录页相对路径
                .loginProcessingUrl("/user/login")  //登陆访问路径
                .defaultSuccessUrl("/test/index")   //登录后的跳转页面
                .permitAll()                        //允许操作--认证
                .and()
                .authorizeRequests()                                //用于配置可访问路径
                .antMatchers("/test/hello","/user/login")//不需要认证即可访问的路径
                .permitAll()
                //.antMatchers("/test/index").hasAuthority("aaa") //有admin权限才可以访问
                //.antMatchers("/test/index").hasAnyAuthority("admin,manager")//权限满足其中之一即可访问
                .antMatchers("/test/index").hasRole("program")//满足当前角色即可访问
                .antMatchers("/test/index").hasAnyRole("program,manager")//满足当前角色中的一个即可访问
                .anyRequest().authenticated()
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())// 传入数据库操作对象
                .tokenValiditySeconds(60)                    // 设置token的有效时长
                .and()
                .csrf().disable();

        http.logout()
                .logoutUrl("/logout")               //设置登出接口
                .logoutSuccessUrl("/test/hello")    //设置登出后跳转接口或页面
                .permitAll();
    }

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //获取加密对象
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("wangyifan")//设置账号
                .password(encoder.encode("123456"))//设置加密密码
                .roles("admin");//设置权限
    }*/


}
