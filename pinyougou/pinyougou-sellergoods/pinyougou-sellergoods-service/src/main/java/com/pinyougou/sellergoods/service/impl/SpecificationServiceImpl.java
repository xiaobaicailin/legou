package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.SpecificationMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.sellergoods.service.SpecificationService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.Specification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class SpecificationServiceImpl extends BaseServiceImpl<TbSpecification> implements SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;



    @Override
    public PageInfo<TbSpecification> search(Integer pageNum, Integer pageSize, TbSpecification specification) {
        //设置分页
        PageHelper.startPage(pageNum, pageSize);
        //创建查询对象
        Example example = new Example(TbSpecification.class);

        //创建查询条件对象
        Example.Criteria criteria = example.createCriteria();

        //模糊查询
        if (StringUtils.isNotBlank(specification.getSpecName())) {
            criteria.andLike("specName", "%" + specification.getSpecName() + "%");
        }

        List<TbSpecification> list = specificationMapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Override
    public void addSpecification(Specification specification) {
        //保存规格
        add(specification.getSpecification());

        for (TbSpecificationOption tbSpecificationOption : specification.getSpecificationOptionList()) {
            tbSpecificationOption.setSpecId(specification.getSpecification().getId());
        }
        //保存规格选项列表
        specificationOptionMapper.insertList(specification.getSpecificationOptionList());

    }

    @Override
    public Specification findSpecification(Long id) {
        Specification specification = new Specification();
        specification.setSpecification(specificationMapper.selectByPrimaryKey(id));
        TbSpecificationOption tbSpecificationOption = new TbSpecificationOption();
        tbSpecificationOption.setSpecId(id);
        specification.setSpecificationOptionList(specificationOptionMapper.select(tbSpecificationOption));
        return specification;
    }

    @Override
    public void updateSpecification(Specification specification) {
        update(specification.getSpecification());

        //先删除原有规格选项
        TbSpecificationOption tbSpecificationOption = new TbSpecificationOption();
        tbSpecificationOption.setSpecId(specification.getSpecification().getId());
        specificationOptionMapper.delete(tbSpecificationOption);

        //插入更新
        List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
        if (specificationOptionList.size()>0&&specificationOptionList!=null){
            for (TbSpecificationOption specificationOption : specificationOptionList) {
                specificationOption.setSpecId(specification.getSpecification().getId());
            }

            specificationOptionMapper.insertList(specificationOptionList);
        }


    }

    @Override
    public void deleteSpecAndOpt(Long[] ids) {
        //先将规格删除
        deleteByIds(ids);

        //删除规格选项
        Example example  = new Example(TbSpecificationOption.class);
        example.createCriteria().andIn("specId", Arrays.asList(ids));
        specificationOptionMapper.deleteByExample(example);
    }

    @Override
    public List<Map<String, String>> findSelectOptionList() {
        return specificationOptionMapper.selectOption();
    }


}
