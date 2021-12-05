package com.itblare.workflow.support.model.req;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 查询参数基类
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/12/2 22:17
 */
public class BaseQueryDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -72438043747795373L;

    /**
     * 页码
     *
     * @mock 1
     */
    private Integer pageNo = 1;

    /**
     * 页数据量
     *
     * @mock 10
     */
    private Integer pageSize = 10;

    /**
     * 编号
     */
    private String tenantId;

    /**
     * 排序规则
     *
     * @mock {"id": "desc", "createTime": "asc"}
     */
    private Map<String, String> orderRule;

    /**
     * 用户ID
     *
     * @mock 1
     */
    private String userId;

    /**
     * 用户组
     *
     * @mock ["1", "2"]
     */
    private Set<String> groups;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Map<String, String> getOrderRule() {
        return orderRule;
    }

    public void setOrderRule(Map<String, String> orderRule) {
        this.orderRule = orderRule;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }

    /**
     * 页偏移量
     */
    public int getOffset() {
        return (pageNo - 1) * pageSize;
    }
}