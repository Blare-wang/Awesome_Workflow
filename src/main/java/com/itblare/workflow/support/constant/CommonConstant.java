package com.itblare.workflow.support.constant;

/**
 * 公共常量
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/7 16:42
 */
public class CommonConstant {

    /**
     * 标记，统一封装响应
     */
    public static final String RESPONSE_WRAPPER_ANN = "Response_Wrapper_ANN";

    /**
     * 携带token的请求头名字
     */
    public final static String TOKEN_HEADER = "Authorization";

    /**
     * token的前缀
     */
    public final static String TOKEN_PREFIX = "Bearer ";

    /**
     * 默认密钥
     */
    public final static String DEFAULT_SECRET = "mySecret";

    /**
     * 用户身份
     */
    public final static String ROLES_CLAIM = "roles";

    /**
     * token有效期,单位分钟；
     */
    public final static long EXPIRE_TIME = 5 * 60 * 1000;

    /**
     * 设置Remember-me功能后的token有效期
     */
    public final static long EXPIRE_TIME_REMEMBER = 7 * 24 * 60 * 60 * 1000;

}