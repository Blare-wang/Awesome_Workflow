package com.itblare.workflow.support.model.resp;

/**
 * 用户基本信息DTO
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/8 15:47
 */
public class UserInfoDto {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户姓名
     */
    private String actualName;

    /**
     * 头像
     */
    private String avatar;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActualName() {
        return actualName;
    }

    public void setActualName(String actualName) {
        this.actualName = actualName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
