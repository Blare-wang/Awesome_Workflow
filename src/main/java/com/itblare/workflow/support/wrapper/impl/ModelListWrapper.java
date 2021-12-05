package com.itblare.workflow.support.wrapper.impl;

import com.itblare.workflow.support.builder.Builder;
import com.itblare.workflow.support.model.resp.ModelInfoDto;
import com.itblare.workflow.support.utils.CommonUtil;
import com.itblare.workflow.support.wrapper.IListWrapper;
import org.flowable.engine.repository.Model;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 模型列表转换接口实现
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/12/5 10:12
 */
@Component
public class ModelListWrapper implements IListWrapper<ModelInfoDto, Model> {

    @Override
    public List<ModelInfoDto> execute(List<Model> modelList) {
        if (Objects.isNull(modelList) || modelList.size() < 1) {
            return List.of();
        }
        return modelList.stream()
                .map(this::createModelInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public ModelInfoDto execute(Model model) {
        return createModelInfoDto(model);
    }

    /**
     * 模型信息封装
     *
     * @param model 模型对象
     * @return {@link ModelInfoDto}
     * @author Blare
     */
    private ModelInfoDto createModelInfoDto(Model model) {
        return Builder.of(ModelInfoDto::new)
                .with(ModelInfoDto::setCategory, model.getCategory())
                .with(ModelInfoDto::setId, model.getId())
                .with(ModelInfoDto::setKey, model.getKey())
                .with(ModelInfoDto::setName, model.getName())
                .with(ModelInfoDto::setDescription, model.getMetaInfo())
                .with(ModelInfoDto::setVersion, model.getVersion())
                .with(ModelInfoDto::setTenantId, model.getTenantId())
                .with(ModelInfoDto::setCreateTime, CommonUtil.Date2LocalDateTime(model.getCreateTime()))
                .with(ModelInfoDto::setLastUpdateTime, CommonUtil.Date2LocalDateTime(model.getLastUpdateTime()))
                .with(ModelInfoDto::setDeployed, CommonUtil.isNotEmptyAfterStrip(model.getDeploymentId()))
                .build();
    }
}