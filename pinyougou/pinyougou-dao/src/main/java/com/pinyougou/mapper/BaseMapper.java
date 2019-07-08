package com.pinyougou.mapper;

import com.pinyougou.pojo.TbBrand;
import tk.mybatis.mapper.additional.dialect.oracle.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.base.delete.DeleteMapper;
import tk.mybatis.mapper.common.base.insert.InsertMapper;
import tk.mybatis.mapper.common.ids.DeleteByIdsMapper;

@RegisterMapper
public interface BaseMapper<T> extends  Mapper<T>, InsertListMapper<T>, DeleteByIdsMapper<T> {
}
