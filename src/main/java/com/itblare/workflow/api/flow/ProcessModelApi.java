package com.itblare.workflow.api.flow;

import com.itblare.workflow.api.BaseApi;
import com.itblare.workflow.service.ProcessModelService;
import com.itblare.workflow.support.model.PageInfoDto;
import com.itblare.workflow.support.model.req.ModelDto;
import com.itblare.workflow.support.model.req.ModelEditorDto;
import com.itblare.workflow.support.model.req.ModelQueryDto;
import com.itblare.workflow.support.model.resp.ModelInfoDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * 流程模型应用接口
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/12/5 13:41
 */
@RestController
@RequestMapping("/flowable/model")
public class ProcessModelApi implements BaseApi {

    private final ProcessModelService processModelService;

    public ProcessModelApi(ProcessModelService processModelService) {
        this.processModelService = processModelService;
    }

    /**
     * 分页列表
     *
     * @param modelQueryDto 模型查询参数
     * @return {@link List<ModelInfoDto>}
     * @apiNote 根据模型查询参数，获取工作流模型的分页列表
     * @ignoreParams id
     * @author Blare
     */
    @PostMapping(value = "/page")
    public PageInfoDto<ModelInfoDto> pageList(@RequestBody ModelQueryDto modelQueryDto) {
        return processModelService.getPage(modelQueryDto);
    }

    /**
     * 详情
     *
     * @param id 模型ID
     * @return {@link List<ModelInfoDto>}
     * @apiNote 根据模型查询参数，获取工作流模型的分页列表
     * @author Blare
     */
    @GetMapping(value = "/detail/{id}")
    public ModelInfoDto detail(@PathVariable("id") String id) {
        return processModelService.getById(id);
    }

    /**
     * 新增
     *
     * @param modelDto 新增模型参数
     * @author Blare
     * @since 2021-12-15
     */
    @PostMapping("save")
    // @PreAuthorize("@elp.single('flowable:model:save')")
    public void save(@RequestBody ModelDto modelDto) {
        processModelService.saveModel(modelDto);
    }

    /**
     * 复制
     *
     * @param id 模型ID
     * @apiNote 根据模型查询参数，获取工作流模型的分页列表
     * @author Blare
     */
    @PostMapping(value = "/copy/{id}")
    public void copy(@PathVariable("id") String id) {
        processModelService.copyModel(id);
    }

    /**
     * 删除
     *
     * @param ids     模型ID列表
     * @param cascade 级联
     * @apiNote 根据模型查询参数，获取工作流模型的分页列表
     * @author Blare
     */
    @DeleteMapping(value = "/delete/{cascade}")
    public void delete(Set<String> ids, @PathVariable("cascade") boolean cascade) {
        processModelService.deleteModel(ids, cascade);
    }

    /**
     * 保存设计
     *
     * @param modelEditorDto 模型设计参数
     * @author Blare
     * @since 2021-12-15
     */
    @PutMapping(value = "/saveEditor")
    public void saveEditor(ModelEditorDto modelEditorDto) {
        processModelService.saveModelEditor(modelEditorDto);
    }

    /**
     * 部署
     *
     * @param id 模型ID
     * @author Blare
     */
    @PostMapping(value = "/deploy/{id}")
    public void deploy(@PathVariable("id") String id) {
        processModelService.deployModel(id);
    }

    /**
     * 导入
     *
     * @param tenantId 编号
     * @param file     模型文件
     * @author Blare
     */
    @PostMapping(value = "/import")
    public void doImport(@RequestParam(required = false) String tenantId, MultipartFile file) {
        processModelService.doImport(tenantId, file);
    }
}