package com.itblare.workflow.support.flowcmd;

import com.itblare.workflow.support.utils.CommonUtil;
import com.itblare.workflow.support.utils.FlowableUtil;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.Model;
import org.flowable.form.engine.impl.persistence.entity.FormDefinitionEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * CMD方式进行模型部署
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/13 16:50
 */
public class DeployModelCmd implements Command<Deployment>, Serializable {

    private static final long serialVersionUID = -5005111238268263861L;

    /**
     * 模型ID
     */
    protected String id;

    public DeployModelCmd(String id) {
        this.id = id;
    }

    @Override
    public Deployment execute(CommandContext commandContext) {

        // 流程引擎配置
        ProcessEngineConfigurationImpl processEngineConfiguration = CommandContextUtil.getProcessEngineConfiguration(commandContext);
        // 存储库服务
        RepositoryService repositoryService = processEngineConfiguration.getRepositoryService();
        // 模型
        Model model = repositoryService.getModel(id);
        if (Objects.isNull(model)) {
            throw new FlowableObjectNotFoundException("未找到 id 为‘" + id + "’的模型!", Model.class);
        }
        if (CommonUtil.isNotEmptyAfterStrip(model.getDeploymentId())) {
            throw new FlowableException("该 id 为‘" + id + "’的模型已经部署过了！");
        }

        if (!model.hasEditorSource()) {
            throw new FlowableObjectNotFoundException("该 id 为‘" + id + "’的模型没有可用的资源 ！", String.class);
        }
        // 模型的资源
        byte[] bpmnBytes = processEngineConfiguration.getModelEntityManager().findEditorSourceByModelId(id);
        if (Objects.isNull(bpmnBytes)) {
            throw new FlowableObjectNotFoundException("该 id 为‘" + id + "’的模型没有可用的资源 ！", String.class);
        }
        // 检测管理表单是否已部署
        checkedDeployedForm(bpmnBytes);
        // 模型名称
        String fileName = model.getId() + ".bpmn20.xml";
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(bpmnBytes)) {
            // 部署构建器
            final DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                    .addInputStream(fileName, bais)
                    .name(fileName)
                    .category(model.getCategory())
                    .tenantId(Optional.ofNullable(model.getTenantId()).orElse(""));
            // 进行部署
            final Deployment deployment = deploymentBuilder.deploy();
            if (Objects.nonNull(deployment)) {
                model.setDeploymentId(deployment.getId());
            }
            return deployment;
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return null;
    }

    /**
     * 已部署表单检测
     *
     * @param bpmnBytes 部署文件
     * @author Blare
     */
    private void checkedDeployedForm(byte[] bpmnBytes) {
        // xml解析文件，得到Bpmn模型
        final BpmnModel bpmnModel = FlowableUtil.parsingFileToBpmnModel(bpmnBytes);
        // 流程对象
        final Process process = bpmnModel.getMainProcess();
        // 流程元素
        final Collection<FlowElement> flowElements = process.getFlowElements();
        if (Objects.nonNull(flowElements)) {
            flowElements.forEach(fe -> {
                String formKey = null;
                if (fe instanceof StartEvent) {
                    // 启动元素
                    final String startEventFormKey = ((StartEvent) fe).getFormKey();
                    if (Objects.nonNull(startEventFormKey) && !startEventFormKey.isBlank()) {
                        formKey = startEventFormKey;
                    }
                } else if (fe instanceof UserTask) {
                    // 任务元素
                    final String userTaskFormKey = ((UserTask) fe).getFormKey();
                    if (Objects.nonNull(userTaskFormKey) && !userTaskFormKey.isBlank()) {
                        formKey = userTaskFormKey;
                    }
                }
                if (Objects.nonNull(formKey) && !formKey.isBlank()) {
                    final FormDefinitionEntity formDefinition = org.flowable.form.engine.impl.util.CommandContextUtil.getFormDefinitionEntityManager().findLatestFormDefinitionByKey(formKey);
                    if (Objects.isNull(formDefinition)) {
                        throw new FlowableObjectNotFoundException("该 id 为‘" + id + "’的模型关联的表单: ‘" + formKey + "’尚未部署！", String.class);
                    }
                }
            });
        }
    }

}