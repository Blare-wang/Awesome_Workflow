package com.itblare.workflow.support.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.SpringProperties;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * WEB安全配置
 * <p>
 * 注解@EnableWebSecurity：启用Spring Security的Web安全支持，并提供Spring MVC集成，
 * 该注解和 @Configuration 注解一起使用, 注解 WebSecurityConfigurer 类型的类，
 * 或者利用@EnableWebSecurity 注解继承 WebSecurityConfigurerAdapter的类，这样就构成了 Spring Security 的配置。
 * <p>
 * 抽象类 WebSecurityConfigurerAdapter：WebSecurityConfigurerAdapter 提供了一种便利的方式去创建 WebSecurityConfigurer的实例，
 * 只需要重写 WebSecurityConfigurerAdapter 的方法，即可配置拦截什么URL、设置什么权限等安全控制。
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/9 10:00
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // @Autowired
    // private AjaxAuthenticationEntryPoint ajaxAuthenticationEntryPoint;
    // @Autowired
    // private JWTAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    // @Autowired
    // private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    // spring security5+后密码策略变更了。必须使用 PasswordEncoder 方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors()
                .and()
                //不使用防跨站攻击
                .csrf().disable()
                //不使用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //允许静态资源无授权访问
                .authorizeRequests().antMatchers(HttpMethod.GET, "/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                .and()
                //允许登录接口、注册接口访问
                .authorizeRequests().antMatchers("/flowable/**","/admin/login", "/admin/register").permitAll()
                .and()
                //配置跨域的option请求，跨域请求之前都会进行一次option请求
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                .and()
                // 其余没有配置的请求全部需要鉴权认证
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/")
                // .successHandler(jwtAuthenticationSuccessHandler)
                // .failureHandler(ajaxAuthenticationFailureHandler)
                .permitAll();
        // .and()
        //添加JWT身份认证的filter
        // .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
        //添加自定义未授权的处理器
        // .exceptionHandling().accessDeniedHandler(restfulAccessDeniedHandler);
        //添加自定义未登录的处理器
        // .exceptionHandling().authenticationEntryPoint("ajaxAuthenticationEntryPoint");
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}