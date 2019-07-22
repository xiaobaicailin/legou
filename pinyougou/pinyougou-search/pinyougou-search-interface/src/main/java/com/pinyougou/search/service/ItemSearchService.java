package com.pinyougou.search.service;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.service.BaseService;

import java.util.Map;

public interface ItemSearchService{

    Map<String,Object> search(Map<String, Object> searchMap);
}
