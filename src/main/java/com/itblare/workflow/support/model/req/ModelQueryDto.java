package com.itblare.workflow.support.model.req;

/**
 * 模型查询参数
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/14 10:28
 */
public class ModelQueryDto extends BaseQueryDto {

    private static final long serialVersionUID = 727400536972487981L;

    /**
     * 模型ID
     */
    private String id;

    /**
     * 模型分类
     */
    private String modelCategory;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 模型key
     */
    private String modelKey;

    /**
     * 模拟版本
     */
    private Integer modelVersion;

    /**
     * 是否最新版本
     */
    private Boolean latestVersion = false;

    /**
     * 是否部署
     */
    private Boolean deployed;

    /**
     * 部署ID
     */
    private String deploymentId;

    /**
     * 编号
     */
    private String tenantId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelCategory() {
        return modelCategory;
    }

    public void setModelCategory(String modelCategory) {
        this.modelCategory = modelCategory;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelKey() {
        return modelKey;
    }

    public void setModelKey(String modelKey) {
        this.modelKey = modelKey;
    }

    public Integer getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(Integer modelVersion) {
        this.modelVersion = modelVersion;
    }

    public Boolean getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(Boolean latestVersion) {
        this.latestVersion = latestVersion;
    }

    public Boolean getDeployed() {
        return deployed;
    }

    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}