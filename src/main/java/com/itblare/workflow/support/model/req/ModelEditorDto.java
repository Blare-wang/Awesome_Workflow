package com.itblare.workflow.support.model.req;

import java.io.Serializable;

/**
 * 模型公用参数
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/14 10:06
 */
public class ModelEditorDto implements Serializable {

    private static final long serialVersionUID = 2592199055701943897L;

    /**
     * 模型ID
     */
    private String id;

    /**
     * 业务系统标识ID
     */
    private String tenantId;

    /**
     * 模型内容
     */
    private String editor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}