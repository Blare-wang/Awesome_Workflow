package com.itblare.workflow.support.model.resp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 模型信息
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/14 10:42
 */
public class ModelInfoDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -3414599956466240151L;

    /**
     * 模型ID
     */
    private String id;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 模型KEY
     */
    private String key;

    /**
     * 模型分类
     */
    private String category;

    /**
     * 模型描述
     */
    private String description;

    /**
     * 业务系统标识ID
     */
    private String tenantId;

    /**
     * 模型内容
     */
    private String editor;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最近更新时间
     */
    private LocalDateTime lastUpdateTime;

    /**
     * 是否已部署
     */
    private Boolean deployed;

    /**
     * 当前版本号
     */
    private Integer version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getDeployed() {
        return deployed;
    }

    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}