package com.itblare.workflow.support.utils;

import liquibase.pro.packaged.E;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * 常用工具
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/8 15:53
 */
public class CommonUtil {

    /**
     * 字符串去首尾空格后，不为空
     *
     * @param content 待检测字符串内容
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean isNotEmptyAfterStrip(String content) {
        return Objects.nonNull(content) && !content.strip().isEmpty();
    }

    /**
     * 字符串去首尾空格后，为空
     *
     * @param content 待检测字符串内容
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean isEmptyAfterStrip(String content) {
        return Objects.isNull(content) || content.strip().isEmpty();
    }

    /**
     * 集合为空
     *
     * @param collection 集合对象
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean isEmptyCollection(Collection<?> collection) {
        return Objects.isNull(collection) || collection.isEmpty();
    }

    public static String convertToLike(String likeStr) {
        return "%" + likeStr + "%";
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @param fmt  自定义格式，eg：yyyy-MM-dd HH:mm:ss
     * @return {@link String}
     * @author Blare
     */
    public static String formatDate(Date date, String fmt) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat myFormat = new SimpleDateFormat(fmt);
        return myFormat.format(date);
    }

    public static LocalDateTime Date2LocalDateTime(Date date) {
        return Optional.ofNullable(date)
                .map(t -> LocalDateTime.ofInstant(t.toInstant(), ZoneId.systemDefault()))
                .orElse(null);
    }

    /**
     * WorkFlowUserDto及其子类对象注入用户信息
     *
     * @param currentUser 当前登录对象
     * @param t           待注入值对象
     * @author Blare
     */
    /*
    public static <T extends WorkFlowUserDto> void injectUserInfo(UserDetailsModel currentUser, T t) {

        UserService userServiceImpl = (UserService) ApplicationContextObtainBeanConfig.getBean(UserService.class);
        final Long userId = currentUser.getId();
        List<Role> roleList = currentUser.getUser().getRole();
        Set<String> roleIds = null;
        if (!CollectionUtils.isEmpty(roleList)) {
            roleIds = roleList.stream().map(r -> String.valueOf(r.getId())).collect(Collectors.toSet());
        } else {
            Set<GroupInfoDto> roles = userServiceImpl.getRoleInfoByUserId(String.valueOf(userId));
            if (!CollectionUtils.isEmpty(roles)) {
                roleIds = roles.stream().map(r -> String.valueOf(r.getId())).collect(Collectors.toSet());
            }
        }
        t.setUserId(String.valueOf(userId));
        t.setGroups(roleIds);
    }*/
}