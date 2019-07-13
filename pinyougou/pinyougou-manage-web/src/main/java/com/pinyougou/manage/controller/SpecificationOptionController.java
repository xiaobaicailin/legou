package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.sellergoods.service.SpecificationOptionService;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/specificationOption")
@RestController
public class SpecificationOptionController {

    @Reference
    private SpecificationOptionService specificationOptionService;

    /**
     * 新增
     * @param specificationOption 实体
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result add(@RequestBody TbSpecificationOption specificationOption){
        try {
            specificationOptionService.add(specificationOption);

            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail();
    }

    /**
     * 根据主键查询
     * @param id 主键
     * @return 实体
     */
    @GetMapping("/findOne/{id}")
    public TbSpecificationOption findOne(@PathVariable Long id){
        return specificationOptionService.findOne(id);
    }

    /**
     * 修改
     * @param specificationOption 实体
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody TbSpecificationOption specificationOption){
        try {
            specificationOptionService.update(specificationOption);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail();
    }

    /**
     * 根据主键数组批量删除
     * @param ids 主键数组
     * @return 实体
     */
    @GetMapping("/delete")
    public Result delete(Long[] ids){
        try {
            specificationOptionService.deleteByIds(ids);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail();
    }

    /**
     * 根据条件搜索
     * @param pageNum 页号
     * @param pageSize 页面大小
     * @param specificationOption 搜索条件
     * @return 分页信息
     */
    @PostMapping("/search")
    public PageInfo<TbSpecificationOption> search(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                           @RequestBody TbSpecificationOption specificationOption) {
        return specificationOptionService.search(pageNum, pageSize, specificationOption);
    }

}
