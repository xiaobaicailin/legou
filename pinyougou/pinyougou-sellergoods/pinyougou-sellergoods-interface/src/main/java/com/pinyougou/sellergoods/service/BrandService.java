package com.pinyougou.sellergoods.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BaseService;

import java.util.List;
import java.util.Map;

public interface BrandService extends BaseService<TbBrand> {
    List<TbBrand> findAll();


    PageInfo<TbBrand> searchPage(Integer pageNum, Integer pageSize, TbBrand tbBrand);

    List<Map<String,String>> findBrand();
}
