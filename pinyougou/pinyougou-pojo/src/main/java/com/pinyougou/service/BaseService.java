package com.pinyougou.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.vo.Specification;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {
    //主键查询
    T findOne(Serializable id);

    //查询全部
    List<T> findAll();

    //条件查询
    List<T> findByWhere(T t);


    /**
     * 分页查询
     * @param pageNUM
     * @param pageSize
     * @return
     */
    PageInfo<T> findPage(Integer pageNUM,Integer pageSize);

    /**
     * 条件分页查询
     * @param pageNUM
     * @param pageSize
     * @return
     */
    PageInfo<T> findPage(Integer pageNUM,Integer pageSize,T t);

    //新增
    void add(T t);

    //更新
    void update(T t);

    //批量删除
    void deleteByIds(Serializable[] ids);
 }
