package com.itblare.workflow.support.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itblare.workflow.support.constant.CommonConstant;
import com.itblare.workflow.support.exceptions.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * JSON Web Token 工具
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/8 17:15
 */
public class JWTUtil {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    /**
     * 创建Token
     *
     * @param username   用户名
     * @param nikeName   姓名
     * @param role       身份
     * @param secret     密钥
     * @param rememberMe 记住我
     * @return {@link String}
     * @throws JWTCreationException JWT创建异常
     * @author Blare
     */
    public static String createToken(String username, String nikeName, List<?> role, String secret, boolean rememberMe) {

        Date expireDate = rememberMe ? new Date(System.currentTimeMillis() + CommonConstant.EXPIRE_TIME_REMEMBER) : new Date(System.currentTimeMillis() + CommonConstant.EXPIRE_TIME);
        try {
            // 创建签名
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withExpiresAt(expireDate)
                    .withClaim("username", username)
                    .withClaim("nikeName", nikeName)
                    .withClaim(CommonConstant.ROLES_CLAIM, role)
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            if (logger.isDebugEnabled()) {
                ex.printStackTrace();
            }
            logger.warn("Token create failed");
            throw new JwtException("Token create failed");
        }
    }

    /**
     * 验证token
     *
     * @param token  用户令牌
     * @param secret 密钥
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean verifyToken(String token, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 构建JWT验证器，token合法同时pyload必须含有私有字段username且值一致，token过期也会验证失败
            JWTVerifier verifier = JWT.require(algorithm).build();
            // 验证token
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            if (logger.isDebugEnabled()) {
                ex.printStackTrace();
            }
            logger.warn("Token verification failed");
            return false;
        }
    }

    /**
     * 获取用户名
     *
     * @param token 用户令牌
     * @return {@link String}
     * @author Blare
     */
    public static String getUsername(String token) {
        try {
            // 因此获取载荷信息不需要密钥
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException ex) {
            if (logger.isDebugEnabled()) {
                ex.printStackTrace();
            }
            logger.warn("Token decoding failed when extracting user name");
            throw new JwtException("Token decoding failed when extracting user name");
        }
    }

    /**
     * 获取用户姓名
     *
     * @param token 用户令牌
     * @return {@link String}
     * @author Blare
     */
    public static String getNikeName(String token) {
        try {
            // 因此获取载荷信息不需要密钥
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("nikeName").asString();
        } catch (JWTDecodeException ex) {
            if (logger.isDebugEnabled()) {
                ex.printStackTrace();
            }
            logger.warn("Token decoding failed when extracting user nike Name");
            throw new JwtException("Token decoding failed when extracting user nike Name");
        }
    }

    /**
     * 获取用户身份
     *
     * @param token 用户令牌
     * @return {@link List<String>}
     * @author Blare
     */
    public static List<String> getRole(String token) {
        try {
            // 因此获取载荷信息不需要密钥
            DecodedJWT jwt = JWT.decode(token);
            // asList方法需要指定容器元素的类型
            return jwt.getClaim(CommonConstant.ROLES_CLAIM).asList(String.class);
        } catch (JWTDecodeException ex) {
            if (logger.isDebugEnabled()) {
                ex.printStackTrace();
            }
            logger.warn("Token decoding failed when extracting identity");
            throw new JwtException("Token decoding failed when extracting identity");
        }
    }
}