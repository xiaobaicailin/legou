package com.pinyougou.solr;

import com.alibaba.fastjson.JSON;
import com.pinyougou.es.dao.ItemElasticSearchDao;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/spring/*.xml")
public class ItemIport2ElasticSearchTest {
@Autowired
private ElasticsearchTemplate elasticsearchTemplate;

@Autowired
private ItemElasticSearchDao itemElasticSearchDao;

@Autowired
private ItemMapper itemMapper;

@Test
    public void createIndexAndMapping(){
        elasticsearchTemplate.createIndex(TbItem.class);
        elasticsearchTemplate.putMapping(TbItem.class);
    }

    @Test
    public void importEs(){
    TbItem param = new TbItem();
    param.setStatus("1");
        List<TbItem> itemList = itemMapper.select(param);

        for (TbItem tbItem : itemList) {
            Map map = JSON.parseObject(tbItem.getSpec(), Map.class);
            tbItem.setSpecMap(map);
        }

        itemElasticSearchDao.saveAll(itemList);
    }

}
