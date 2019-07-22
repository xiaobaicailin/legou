package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@RequestMapping("/brand")
@RestController
public class BrandController {

    @Reference
    private BrandService brandService;

        @GetMapping("/selectOptionList.do")
        public List<Map<String,String>> selectOptionList(){
             return brandService.findBrand();
        }


    @PostMapping("/search.do")
    public PageInfo<TbBrand> search(@RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "5") Integer pageSize,@RequestBody TbBrand tbBrand){
       return brandService.searchPage(pageNum,pageSize,tbBrand);
    }

    @GetMapping("/deleteList.do")
    public Result deleteList(Long[] ids){
        try {
            brandService.deleteByIds(ids);
            return Result.ok("成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("失败");
    }


    /**
     * 更新数据
     * @param tbBrand
     * @return
     */
    @PostMapping("/update.do")
    public Result update(@RequestBody TbBrand tbBrand){
        try {
            brandService.update(tbBrand);
            return Result.ok("成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  Result.fail("失败");
    }


    /**
     * 回显数据
     * @param id
     * @return
     */
    @GetMapping("/findOne/{id}")
    public TbBrand findOne(@PathVariable Long id){
       return brandService.findOne(id);
    }

    /**
     * 新建
     * @param tbBrand
     * @return
     */
    @PostMapping("/add.do")
    public Result add(@RequestBody TbBrand tbBrand){
        try {
            brandService.add(tbBrand);
            return Result.ok("成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("失败");
    }

 /*   *//**
     * 分页
     * @param pageNum
     * @param pageSize
     * @return
     *//*
    @GetMapping("/findPage.do")
    public PageInfo<TbBrand> allPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "5") Integer pageSize){
        return brandService.findPage(pageNum,pageSize);
    }
*/

/*    @GetMapping("/findAll.do")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }*/
}
