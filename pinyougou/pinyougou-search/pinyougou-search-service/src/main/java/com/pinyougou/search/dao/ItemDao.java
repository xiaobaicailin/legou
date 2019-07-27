package com.pinyougou.search.dao;

import com.pinyougou.pojo.TbItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemDao extends ElasticsearchRepository<TbItem,Long>{

    //不需要实现上述接口方法，spring data elasticSearch 将对方法进行解析后自行执行对应的 es 操作。
     void deleteTbItemsById(Long[] ids);
}
