package com.sw.uac.config;

import com.sw.uac.entity.JwtAuthenticationEntryPoint;
import com.sw.uac.filter.JwtAuthenticationTokenFilter;
import com.sw.uac.properties.IgnoredUrlsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * spring security 的安全配置类
 * @Author: yu.leilei
 * @Date: 上午 11:20 2018/5/25 0025
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    IgnoredUrlsProperties ignoredUrlsProperties;

    @Resource
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 用户名密码认证方法
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(this.userDetailsService)
                // 设置密码加密方式
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 装载BCrypt密码编码器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    /**
     * token请求授权
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 参数配置以外的url都需要认证和鉴别
        for(String url:ignoredUrlsProperties.getUrls()){
            httpSecurity.authorizeRequests().antMatchers(url).permitAll();
        }
        httpSecurity
                .cors().and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                // 未授权处理
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // 允许对于网站静态资源的无授权访问
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                // swagger start
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // swagger end
                // 对于获取token的rest api要允许匿名访问
                // .antMatchers("/api-uac/uac/getUserInfo").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 添加JWT filter
        httpSecurity
                //将token验证添加在密码验证前面
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // 禁用缓存
        httpSecurity.headers().cacheControl();

    }
}
