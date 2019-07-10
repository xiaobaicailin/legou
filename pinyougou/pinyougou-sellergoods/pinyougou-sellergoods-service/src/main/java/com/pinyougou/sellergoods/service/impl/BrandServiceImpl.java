package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl extends BaseServiceImpl<TbBrand> implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.queryAll();
    }

    @Override
    public PageInfo<TbBrand> searchPage(Integer pageNum, Integer pageSize, TbBrand tbBrand) {

        Example example = new Example(TbBrand.class);
        Example.Criteria criteria = example.createCriteria();
        if (tbBrand.getName()!=null){
            criteria.andLike("name", "%"+tbBrand.getName()+"%");
        }
        if (tbBrand.getFirstChar()!=null){
            criteria.andEqualTo("firstChar",tbBrand.getFirstChar());
        }
        PageHelper.startPage(pageNum,pageSize );
        List<TbBrand> tbBrands = brandMapper.selectByExample(example);
        return new PageInfo<>(tbBrands);
    }


}
