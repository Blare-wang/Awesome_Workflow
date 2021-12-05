package com.itblare.workflow.support.config;

import com.itblare.workflow.support.interceptor.ResponseWrapperInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * WebMvc 信息配置
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/7 15:40
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private ResponseWrapperInterceptor responseWrapperInterceptor;

    /**
     * 注册拦截器
     *
     * @param registry 注册机
     * @author Blare
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 设置拦截器
        registry.addInterceptor(responseWrapperInterceptor);
    }

    /**
     * 跨域配置会覆盖默认的配置，
     * 因此需要实现addResourceHandlers方法，增加默认配置静态路径
     *
     * @param registry 资源注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /*
         * 注意allowedOrigins("*")和allowCredentials(true)为true时候会出现错误
         * 需要改成allowedOriginPatterns("*")或者单独指定接口allowedOrigins("http//www.baidu.com")
         */
        registry.addMapping("/**") //项目中的所有接口都支持跨域
                .allowedOriginPatterns("*") //所有地址都可以访问，也可以配置具体地址
                // .allowedOrigins("*")
                .allowedMethods("*") //"GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"
                .allowCredentials(true);
    }
}