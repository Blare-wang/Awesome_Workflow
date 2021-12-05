package com.itblare.workflow.support.model.req;

import java.io.Serial;
import java.io.Serializable;

/**
 * 模型参数
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/14 9:56
 */
public class ModelDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -9186520863646506441L;
    /**
     * 模型ID
     */
    private String id;

    /**
     * 模型KEY
     */
    private String key;

    /**
     * 模型名称
     */
    private String name;

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
     * 新版本号
     */
    private boolean newVersion;

    /**
     * 级联
     */
    private boolean cascade;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isNewVersion() {
        return newVersion;
    }

    public void setNewVersion(boolean newVersion) {
        this.newVersion = newVersion;
    }

    public boolean isCascade() {
        return cascade;
    }

    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }
}