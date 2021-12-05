package com.itblare.workflow.service.impl;

import com.itblare.workflow.service.ProcessModelService;
import com.itblare.workflow.support.builder.Builder;
import com.itblare.workflow.support.flowcmd.DeployModelCmd;
import com.itblare.workflow.support.flowcmd.SavedModelEditorCmd;
import com.itblare.workflow.support.model.PageInfoDto;
import com.itblare.workflow.support.model.req.ModelDto;
import com.itblare.workflow.support.model.req.ModelEditorDto;
import com.itblare.workflow.support.model.req.ModelQueryDto;
import com.itblare.workflow.support.model.resp.ModelInfoDto;
import com.itblare.workflow.support.utils.CommonUtil;
import com.itblare.workflow.support.wrapper.impl.ModelListWrapper;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.common.engine.api.query.QueryProperty;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.engine.impl.ModelQueryProperty;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 流程模型服务实现：主要对流程模型进行存储、检索和管理操作
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/14 11:00
 */
@Service
public class ProcessModelServiceImpl extends FlowableFactory implements ProcessModelService {

    private final ModelListWrapper modelListWrapper;

    public ProcessModelServiceImpl(ModelListWrapper modelListWrapper) {
        this.modelListWrapper = modelListWrapper;
    }

    /**
     * 允许排序字段
     */
    private static final Map<String, QueryProperty> ALLOWED_SORT_PROPERTIES = Map.of(
            "id", ModelQueryProperty.MODEL_ID,
            "category", ModelQueryProperty.MODEL_CATEGORY,
            "createTime", ModelQueryProperty.MODEL_CREATE_TIME,
            "key", ModelQueryProperty.MODEL_KEY,
            "lastUpdateTime", ModelQueryProperty.MODEL_LAST_UPDATE_TIME,
            "name", ModelQueryProperty.MODEL_NAME,
            "version", ModelQueryProperty.MODEL_VERSION,
            "tenantId", ModelQueryProperty.MODEL_TENANT_ID
    );

    @Override
    public void saveModel(ModelDto modelDto) {
        if (CommonUtil.isEmptyAfterStrip(modelDto.getKey())) {
            throw new IllegalArgumentException("模型标识 KEY 为空！");
        }
        if (CommonUtil.isEmptyAfterStrip(modelDto.getName())) {
            throw new IllegalArgumentException("模型名称为空！");
        }
        if (repositoryService.createModelQuery().modelKey(modelDto.getKey()).count() > 0) {
            throw new FlowableException("模型标识KEY:‘" + modelDto.getKey() + "’已经存在！");
        }
        final Model model = Builder.of(repositoryService::newModel)
                .with(Model::setKey, modelDto.getKey())
                .with(Model::setName, modelDto.getName())
                .with(Model::setVersion, 1)
                .with(Model::setMetaInfo, modelDto.getDescription())
                .with(Model::setTenantId, modelDto.getTenantId())
                .with(Model::setCategory, modelDto.getCategory())
                .build();
        repositoryService.saveModel(model);
    }

    @Override
    public void copyModel(String id) {
        if (CommonUtil.isEmptyAfterStrip(id)) {
            throw new IllegalArgumentException("待复制模型ID为空！");
        }
        final Model model = this.getNonNullModelById(id);
        final byte[] editorSource = repositoryService.getModelEditorSource(model.getId());
        if (Objects.isNull(editorSource) || editorSource.length == 0) {
            throw new FlowableObjectNotFoundException("未找到ID为‘" + id + "’的模型数据！");
        }
        managementService.executeCommand(new SavedModelEditorCmd("3",
                null,
                null,
                null,
                null,
                null,
                model.getTenantId(),
                editorSource));
    }

    @Override
    public void deleteModel(Set<String> ids, boolean cascade) {
        if (CommonUtil.isEmptyCollection(ids)) {
            throw new IllegalArgumentException("模型ID为空！");
        }
        ids.forEach(modelId -> {
            final Model model = this.getModelById(modelId);
            if (cascade && Objects.nonNull(model)) {
                repositoryService.deleteDeployment(model.getDeploymentId(), true);
            }
            repositoryService.deleteModel(modelId);
        });
    }

    @Override
    public void saveModelEditor(ModelEditorDto modelEditorDto) {
        if (Objects.isNull(modelEditorDto.getId()) || modelEditorDto.getId().isBlank()) {
            throw new IllegalArgumentException("流程设计ID为空！");
        }
        if (Objects.isNull(modelEditorDto.getEditor()) || modelEditorDto.getEditor().isBlank()) {
            throw new IllegalArgumentException("流程设计内容为空！");
        }
        managementService.executeCommand(new SavedModelEditorCmd("1",
                modelEditorDto.getId(),
                null,
                null,
                null,
                null,
                modelEditorDto.getTenantId(),
                modelEditorDto.getEditor().getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void deployModel(String id) {
        if (CommonUtil.isEmptyAfterStrip(id)) {
            throw new IllegalArgumentException("模型ID为空！");
        }
        managementService.executeCommand(new DeployModelCmd(id));
    }

    @Override
    public void doImport(String tenantId, MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new IllegalArgumentException("部署文件为空！");
        }
        String filename = file.getOriginalFilename();
        boolean emptyFilename = Objects.isNull(filename) || filename.isBlank();
        if (emptyFilename) {
            throw new IllegalArgumentException("部署文件文件名为空！");
        }
        boolean isBpmnFile = filename.endsWith(".bpmn20.xml") || filename.endsWith(".bpmn");
        boolean isBpmnPackageFile = filename.endsWith(".bar") || filename.endsWith(".zip");
        if (!isBpmnFile && !isBpmnPackageFile) {
            throw new IllegalArgumentException("部署文件必须是以.bpmn20.xml或.bpmn或.bar或.zip结尾！");
        }
        if (isBpmnFile) {
            try {
                managementService.executeCommand(new SavedModelEditorCmd("2",
                        null,
                        null,
                        null,
                        null,
                        null,
                        tenantId,
                        file.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
                ZipEntry entry = zipInputStream.getNextEntry();
                while (entry != null) {
                    if (!entry.isDirectory()) {
                        String entryName = entry.getName();
                        byte[] bytes = IoUtil.readInputStream(zipInputStream, entryName);
                        managementService.executeCommand(new SavedModelEditorCmd("2",
                                null,
                                null,
                                null,
                                null,
                                null,
                                tenantId,
                                bytes));
                    }
                    entry = zipInputStream.getNextEntry();
                }
            } catch (Exception e) {
                throw new FlowableException("读取压缩包文件流失败！", e);
            }
        }
    }

    @Override
    public Model getModelById(String id) {
        if (CommonUtil.isEmptyAfterStrip(id)) {
            throw new IllegalArgumentException("模型ID为空！");
        }
        return repositoryService.getModel(id);
    }

    @Override
    public Model getNonNullModelById(String id) {
        Model model = this.getModelById(id);
        if (Objects.isNull(model)) {
            throw new FlowableObjectNotFoundException("未找到ID为‘" + id + "’的模型！");
        }
        return model;
    }

    @Override
    public ModelInfoDto getById(String id) {
        final Model model = this.getNonNullModelById(id);
        return modelListWrapper.execute(model);
    }

    @Override
    public PageInfoDto<ModelInfoDto> getPage(ModelQueryDto modelQueryDto) {
        final ModelQuery modelQuery = repositoryService.createModelQuery();
        if (CommonUtil.isNotEmptyAfterStrip(modelQueryDto.getId())) {
            modelQuery.modelId(modelQueryDto.getId());
        }
        if (CommonUtil.isNotEmptyAfterStrip(modelQueryDto.getModelCategory())) {
            modelQuery.modelCategoryLike(CommonUtil.convertToLike(modelQueryDto.getModelCategory()));
        }
        if (CommonUtil.isNotEmptyAfterStrip(modelQueryDto.getModelName())) {
            modelQuery.modelNameLike(modelQueryDto.getModelName());
        }
        if (CommonUtil.isNotEmptyAfterStrip(modelQueryDto.getModelKey())) {
            modelQuery.modelKey(modelQueryDto.getModelKey());
        }
        if (Objects.nonNull(modelQueryDto.getModelVersion())) {
            modelQuery.modelVersion(modelQueryDto.getModelVersion());
        }
        if (Objects.nonNull(modelQueryDto.getLatestVersion()) && modelQueryDto.getLatestVersion()) {
            modelQuery.latestVersion();
        }
        if (CommonUtil.isNotEmptyAfterStrip(modelQueryDto.getDeploymentId())) {
            modelQuery.deploymentId(modelQueryDto.getDeploymentId());
        }
        Optional.ofNullable(modelQueryDto.getDeployed())
                .ifPresent(v -> {
                    if (modelQueryDto.getDeployed()) {
                        modelQuery.deployed();
                    } else {
                        modelQuery.notDeployed();
                    }
                });
        if (CommonUtil.isNotEmptyAfterStrip(modelQueryDto.getTenantId())) {
            modelQuery.modelTenantId(modelQueryDto.getTenantId());
        }

        return pageList(getFlowablePage(modelQueryDto),
                modelQuery,
                ModelListWrapper.class,
                ALLOWED_SORT_PROPERTIES,
                null);
    }
}