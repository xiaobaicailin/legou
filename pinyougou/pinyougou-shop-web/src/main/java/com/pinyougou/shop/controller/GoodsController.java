package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    @GetMapping("/updateStatus")
    public Result updateStatus(String status,Long[] ids){

        try {
            goodsService.updateStatus(status,ids);
          return   Result.ok("成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
       return Result.fail("失败");
    }

    /**
     * 新增
     * @param goods 实体
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result add(@RequestBody Goods goods){
        try {
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            goods.getGoods().setSellerId(sellerId);

            goods.getGoods().setAuditStatus("0");
            goodsService.addGoods(goods);

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
    public Goods findOne(@PathVariable Long id){
        return goodsService.findGoods(id);
    }

    /**
     * 修改
     * @param goods 实体
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods){
        try {
            goodsService.updateGoods(goods);
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
            goodsService.deleteByIds(ids);
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
     * @param goods 搜索条件
     * @return 分页信息
     */
    @PostMapping("/search")
    public PageInfo<TbGoods> search(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                           @RequestBody TbGoods goods) {
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(sellerId);
        return goodsService.search(pageNum, pageSize, goods);
    }

}
