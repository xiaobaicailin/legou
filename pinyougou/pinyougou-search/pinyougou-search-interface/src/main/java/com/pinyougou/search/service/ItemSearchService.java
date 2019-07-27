package com.pinyougou.search.service;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.service.BaseService;

import java.util.List;
import java.util.Map;

public interface ItemSearchService{

    Map<String,Object> search(Map<String, Object> searchMap);

    void importEsItemList(List<TbItem> list);

    void deleteItemByIds(Long[] ids);
}
