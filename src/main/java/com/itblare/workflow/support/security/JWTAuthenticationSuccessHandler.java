// package com.itblare.workflow.support.security;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.itblare.workflow.support.constant.CommonConstant;
// import com.itblare.workflow.support.result.ResponseDataFactory;
// import com.itblare.workflow.support.utils.JWTUtil;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
// import org.springframework.stereotype.Component;
//
// import javax.servlet.FilterChain;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import java.util.Collection;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;
// import java.util.stream.Collectors;
//
// /**
//  * Jwt 身份认证
//  *
//  * @author Blare
//  * @version 1.0.0
//  * @since 2021/7/8 17:52
//  */
// @Component
// public class JWTAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//     @Override
//     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//         AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
//     }
//
//     @Override
//     public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
//
//         // 提取用户名，准备写入token
//         String username = authentication.getName();
//         // 提取角色，转为List<String>对象，写入token
//         Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//         Set<String> roles = Optional.ofNullable(authorities).orElse(List.of()).stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
//         // 创建token
//         String token = JWTUtil.createToken(username, "", List.copyOf(roles), CommonConstant.DEFAULT_SECRET, true);
//         httpServletResponse.setCharacterEncoding("utf-8");
//         // 为了跨域，把token放到响应头WWW-Authenticate里
//         httpServletResponse.setHeader("WWW-Authenticate", CommonConstant.TOKEN_PREFIX + token);
//         // 写入响应里
//         new ObjectMapper().writeValue(httpServletResponse.getWriter(), ResponseDataFactory.success());
//     }
// }