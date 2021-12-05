// package com.itblare.workflow.support.security;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.itblare.workflow.support.result.ResponseDataFactory;
// import org.springframework.http.HttpStatus;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.AuthenticationEntryPoint;
// import org.springframework.stereotype.Component;
//
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
//
// /**
//  * 理未认证用户访问未授权资源时抛出的异常
//  *
//  * @author Blare
//  * @version 1.0.0
//  * @since 2021/7/9 10:03
//  */
// @Component
// public class AjaxAuthenticationEntryPoint implements AuthenticationEntryPoint {
//     @Override
//     public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//         httpServletResponse.setCharacterEncoding("utf-8");
//         new ObjectMapper().writeValue(httpServletResponse.getWriter(), ResponseDataFactory.errorCodeMessage(HttpStatus.UNAUTHORIZED.value(), "Anonymous user, please login before accessing"));
//     }
// }