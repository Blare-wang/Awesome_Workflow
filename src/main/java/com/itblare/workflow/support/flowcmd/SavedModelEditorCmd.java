package com.itblare.workflow.support.flowcmd;

import com.itblare.workflow.support.utils.FlowableUtil;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.repository.Model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;

/**
 * CMD方式进行模型保存
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/13 15:59
 */
public class SavedModelEditorCmd implements Command<String>, Serializable {

    private static final long serialVersionUID = 2460004354895210140L;

    /**
     * 1：模型流程图维护
     * 2：导入模型
     * 3：复制模型
     */
    private final String type;

    /**
     * 模型ID
     */
    private final String modelId;

    /**
     * 模型key
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
    private final String tenantId;

    /**
     * 模型二进制文件
     */
    private final byte[] editor;

    public SavedModelEditorCmd(String type, String modelId, String key, String name, String category, String description, String tenantId, byte[] editor) {
        this.type = type;
        this.modelId = modelId;
        this.key = key;
        this.name = name;
        this.category = category;
        this.description = description;
        this.tenantId = tenantId;
        this.editor = editor;
    }

    @Override
    public String execute(CommandContext commandContext) {
        if ("1".equals(this.type)) {
            return maintain(commandContext);
        } else if ("2".equals(this.type) || "3".equals(this.type)) {
            return importOrCopy(commandContext);
        } else {
            throw new FlowableException("操作类型错误：type=" + type);
        }
    }

    /**
     * 模型维护
     *
     * @param commandContext 命令上下文
     * @author Blare
     */
    private String maintain(CommandContext commandContext) {
        if (Objects.isNull(modelId) || modelId.isBlank()) {
            throw new FlowableException("待维护模型ID不可为空！");
        }
        if (Objects.isNull(editor) || editor.length == 0) {
            throw new FlowableException("待维护模型文件内容不可为空！");
        }
        // 流程引擎配置
        ProcessEngineConfiguration processEngineConfiguration = CommandContextUtil.getProcessEngineConfiguration(commandContext);
        // 存储库服务
        RepositoryService repositoryService = processEngineConfiguration.getRepositoryService();
        // 模型
        final Model model = repositoryService.getModel(modelId);
        if (Objects.isNull(model)) {
            throw new FlowableException("该 id 为‘" + modelId + "’的模型不存在！");
        }
        if (Objects.nonNull(model.getDeploymentId()) && !model.getDeploymentId().isBlank()) {
            throw new FlowableException("已发布的模型不允许再进行更新！");
        }
        // xml解析文件，得到Bpmn模型
        final BpmnModel bpmnModel = FlowableUtil.parsingFileToBpmnModel(editor);
        key = bpmnModel.getMainProcess().getId();
        name = bpmnModel.getMainProcess().getName();
        category = bpmnModel.getTargetNamespace();
        description = bpmnModel.getMainProcess().getDocumentation();
        if (Objects.isNull(key) || key.isBlank()) {
            throw new FlowableException("流程 id 为空！");
        }
        if (!key.equals(model.getKey())) {
            throw new FlowableException("不能修改模型ID");
        }
        model.setName(name);
        model.setCategory(category);
        model.setMetaInfo(description);
        // 保存model
        repositoryService.saveModel(model);
        // 保存模型资源和流程图
        addModelEditorSourceAndSourceExtra(processEngineConfiguration, repositoryService, editor, bpmnModel, model.getId());

        return model.getId();
    }

    /**
     * 模型导入或复制
     *
     * @param commandContext 命令上下文
     * @return {@link String}
     * @author Blare
     */
    private String importOrCopy(CommandContext commandContext) {
        if (Objects.isNull(editor) || editor.length == 0) {
            throw new FlowableException("流程文件及内容不能为空！");
        }
        // 流程引擎配置
        ProcessEngineConfiguration processEngineConfiguration = CommandContextUtil.getProcessEngineConfiguration(commandContext);
        // 存储库服务
        RepositoryService repositoryService = processEngineConfiguration.getRepositoryService();
        // 模型
        BpmnModel bpmnModel = FlowableUtil.parsingFileToBpmnModel(editor);
        key = bpmnModel.getMainProcess().getId();
        name = bpmnModel.getMainProcess().getName();
        category = bpmnModel.getTargetNamespace();
        description = bpmnModel.getMainProcess().getDocumentation();
        if (Objects.isNull(key) || key.isBlank()) {
            throw new FlowableException("流程 id 为空！");
        }
        // 模型
        Model model;
        if (Objects.isNull(tenantId) || tenantId.isBlank()) {
            model = repositoryService.createModelQuery().modelKey(key).latestVersion().singleResult();
        } else {
            model = repositoryService.createModelQuery().modelKey(key).modelTenantId(tenantId).latestVersion().singleResult();
        }
        // 待保存的模型
        Model saveModel;
        // 没有查询到最新版本信息，则产生一个新的版本
        if (Objects.isNull(model)) {
            saveModel = repositoryService.newModel();
            saveModel.setVersion(1);
        } else {
            // 复制：已发布的重新产生一个版本，模型复制的也要重新产生一个版本
            if ("3".equals(type) || (Objects.nonNull(model.getDeploymentId()) && !model.getDeploymentId().isBlank())) {
                description = "复制(key=" + key + "):" + description;
                saveModel = repositoryService.newModel();
                saveModel.setVersion(model.getVersion() + 1);
            }
            // 导入：未发布的直接修改旧的版本
            else {
                saveModel = model;
            }
        }
        saveModel.setKey(key);
        saveModel.setName(name);
        saveModel.setCategory(category);
        saveModel.setMetaInfo(description);
        if (Objects.nonNull(tenantId) && !tenantId.isBlank()) {
            saveModel.setTenantId(tenantId);
        }
        // 保存model
        repositoryService.saveModel(saveModel);
        // 保存模型资源和流程图
        addModelEditorSourceAndSourceExtra(processEngineConfiguration, repositoryService, editor, bpmnModel, saveModel.getId());
        return saveModel.getId();
    }

    /**
     * 保存模型资源和流程图
     *
     * @param processEngineConfiguration 流程引擎配置
     * @param repositoryService          存储库服务
     * @param editor                     模型资源
     * @param bpmnModel                  bpmn模型对象
     * @param id                         模型ID
     * @author Blare
     */
    private void addModelEditorSourceAndSourceExtra(ProcessEngineConfiguration processEngineConfiguration, RepositoryService repositoryService, byte[] editor, BpmnModel bpmnModel, String id) {
        if (Objects.isNull(editor)) {
            return;
        }
        // 保存模型资源
        repositoryService.addModelEditorSource(id, editor);
        // 流程图
        final InputStream diagram = processEngineConfiguration.getProcessDiagramGenerator().generateDiagram(bpmnModel,
                "png",
                Collections.emptyList(),
                Collections.emptyList(),
                processEngineConfiguration.getActivityFontName(),
                processEngineConfiguration.getLabelFontName(),
                processEngineConfiguration.getAnnotationFontName(),
                processEngineConfiguration.getClassLoader(),
                1.0,
                true);
        try {
            repositoryService.addModelEditorSourceExtra(id, IOUtils.toByteArray(diagram));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}