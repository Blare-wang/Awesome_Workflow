// package com.itblare.workflow.support.security;
//
//
// import com.itblare.workflow.support.constant.CommonConstant;
// import com.itblare.workflow.support.utils.JWTUtil;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.filter.OncePerRequestFilter;
//
// import javax.servlet.FilterChain;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Objects;
// import java.util.Optional;
// import java.util.stream.Collectors;
//
// /**
//  * JWT 授权
//  *
//  * @author Blare
//  * @version 1.0.0
//  * @since 2021/7/9 9:39
//  */
// public class JWTAuthorizationFilter extends OncePerRequestFilter {
//
//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//         String token = getTokenFromRequestHeader(request);
//         Authentication verifyResult = verifyToken(token, CommonConstant.DEFAULT_SECRET);
//         if (Objects.nonNull(verifyResult)) {
//             // log.info("token令牌验证成功");
//             SecurityContextHolder.getContext().setAuthentication(verifyResult);
//         }
//         // 即便验证失败，也继续调用过滤链，匿名过滤器生成匿名令牌
//         chain.doFilter(request, response);
//     }
//
//     /**
//      * 从请求头获取token
//      *
//      * @param request 请求对象
//      * @return {@link String}
//      * @author Blare
//      */
//     private String getTokenFromRequestHeader(HttpServletRequest request) {
//         String header = request.getHeader(CommonConstant.TOKEN_HEADER);
//         if (Objects.isNull(header) || !header.startsWith(CommonConstant.TOKEN_PREFIX)) {
//             // log.info("请求头不含JWT token， 调用下个过滤器");
//             return null;
//         }
//
//         String token = header.split(" ")[1].trim();
//         return token;
//     }
//
//     /**
//      * 验证token，并生成认证后的token
//      *
//      * @param token  身份令牌
//      * @param secret 密钥
//      * @return {@link UsernamePasswordAuthenticationToken}
//      * @author Blare
//      */
//     private UsernamePasswordAuthenticationToken verifyToken(String token, String secret) {
//         // 认证失败，返回null
//         if (token == null || !JWTUtil.verifyToken(token, secret)) {
//             return null;
//         }
//         // 提取用户名
//         String username = JWTUtil.getUsername(token);
//         // 从token提取角色
//         List<String> roles = JWTUtil.getRole(token);
//         // 权限列表
//         List<GrantedAuthority> authorityList = Optional.ofNullable(roles)
//                 .map(rs -> rs.stream().map(r -> (GrantedAuthority) new SimpleGrantedAuthority(r)).collect(Collectors.toList()))
//                 .orElse(List.of());
//         // 构建认证过的token
//         return new UsernamePasswordAuthenticationToken(username, null, authorityList);
//     }
// }