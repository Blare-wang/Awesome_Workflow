package com.itblare.workflow.service;

import com.itblare.workflow.support.model.PageInfoDto;
import com.itblare.workflow.support.model.req.ModelDto;
import com.itblare.workflow.support.model.req.ModelEditorDto;
import com.itblare.workflow.support.model.req.ModelQueryDto;
import com.itblare.workflow.support.model.resp.ModelInfoDto;
import org.flowable.engine.repository.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * 流程模型服务
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/14 9:52
 */
public interface ProcessModelService extends BaseQueryService<Model> {

    /**
     * 新增流程模型
     *
     * @param modelDto 模型新增参数
     * @author Blare
     */
    void saveModel(ModelDto modelDto);

    /**
     * 复制流程模型
     *
     * @param id 模型ID
     * @author Blare
     */
    void copyModel(String id);

    /**
     * 删除流程模型
     *
     * @param ids     模型ID列表
     * @param cascade 是否级联
     * @author Blare
     */
    void deleteModel(Set<String> ids, boolean cascade);

    /**
     * 保存流程设计
     *
     * @param modelEditorDto 模型编辑参数
     * @author Blare
     */
    void saveModelEditor(ModelEditorDto modelEditorDto);

    /**
     * 部署流程模型
     *
     * @param id 模型ID
     * @author Blare
     */
    void deployModel(String id);

    /**
     * 导入流程模型
     *
     * @param tenantId 租户编号
     * @param file     模型文件
     * @author Blare
     */
    void doImport(String tenantId, MultipartFile file);

    /**
     * 获取流程模型
     *
     * @param id 模型ID
     * @return {@link Model}
     * @author Blare
     */
    Model getModelById(String id);

    /**
     * 获取非空流程模型
     *
     * @param id 模型ID
     * @return {@link Model}
     * @author Blare
     */
    Model getNonNullModelById(String id);

    /**
     * 查询流程模型详情
     *
     * @param id 模型ID
     * @return {@link ModelInfoDto}
     * @author Blare
     */
    ModelInfoDto getById(String id);

    /**
     * 查询流程模型列表
     *
     * @param modelQueryDto 模型查询参数
     * @return {@link PageInfoDto<ModelInfoDto>}
     * @author Blare
     */
    PageInfoDto<ModelInfoDto> getPage(ModelQueryDto modelQueryDto);
}