package com.pinyougou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.BaseMapper;
import com.pinyougou.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.util.StringUtil;

import java.io.Serializable;
import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    //spring4.0+版本后才能使用泛型依赖注入
    @Autowired
    private BaseMapper<T> mapper;

    @Override
    public T findOne(Serializable id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    @Override
    public List<T> findByWhere(T t) {
        return mapper.select(t);
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<T> findPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize );
        List<T> tList = mapper.selectAll();
        return new PageInfo<>(tList);
    }

    /**
     * 条件分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param t
     * @return
     */
    @Override
    public PageInfo<T> findPage(Integer pageNum, Integer pageSize, T t) {
        PageHelper.startPage(pageNum,pageSize );
        List<T> tList = mapper.select(t);
        return new PageInfo<>(tList);
    }

    @Override
    public void add(T t) {
        mapper.insertSelective(t);
    }

    @Override
    public void update(T t) {
        mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public void deleteByIds(Serializable[] ids) {
        if (ids != null && ids.length > 0) {
            mapper.deleteByIds(StringUtils.join(ids, ","));
        }
    }
}
