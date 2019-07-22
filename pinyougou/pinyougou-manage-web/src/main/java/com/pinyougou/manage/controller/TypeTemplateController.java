package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/typeTemplate")
@RestController
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;

    @GetMapping("/findAll.do")
    public List<TbTypeTemplate> findAll(){
        return typeTemplateService.findAll();
    }

    /**
     * 新增
     * @param typeTemplate 实体
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result add(@RequestBody TbTypeTemplate typeTemplate){
        try {
            typeTemplateService.add(typeTemplate);

            return Result.ok("成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("失败");
    }

    /**
     * 根据主键查询
     * @param id 主键
     * @return 实体
     */
    @GetMapping("/findOne/{id}")
    public TbTypeTemplate findOne(@PathVariable Long id){
        return typeTemplateService.findOne(id);
    }

    /**
     * 修改
     * @param typeTemplate 实体
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody TbTypeTemplate typeTemplate){
        try {
            typeTemplateService.update(typeTemplate);
            return Result.ok("成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("失败");
    }

    /**
     * 根据主键数组批量删除
     * @param ids 主键数组
     * @return 实体
     */
    @GetMapping("/delete")
    public Result delete(Long[] ids){
        try {
            typeTemplateService.deleteByIds(ids);
            return Result.ok("成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("失败");
    }

    /**
     * 根据条件搜索
     * @param pageNum 页号
     * @param pageSize 页面大小
     * @param typeTemplate 搜索条件
     * @return 分页信息
     */
    @PostMapping("/search")
    public PageInfo<TbTypeTemplate> search(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                           @RequestBody TbTypeTemplate typeTemplate) {
        return typeTemplateService.search(pageNum, pageSize, typeTemplate);
    }

}
