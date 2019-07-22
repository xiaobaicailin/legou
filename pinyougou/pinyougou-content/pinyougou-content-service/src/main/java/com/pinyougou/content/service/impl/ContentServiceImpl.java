package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.ContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.service.impl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

@Service
public class ContentServiceImpl extends BaseServiceImpl<TbContent> implements ContentService {

    private static final String REDIS_CONTENT = "CONTENT_LIST";

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public PageInfo<TbContent> search(Integer pageNum, Integer pageSize, TbContent content) {
        //设置分页
        PageHelper.startPage(pageNum, pageSize);
        //创建查询对象
        Example example = new Example(TbContent.class);

        //创建查询条件对象
        Example.Criteria criteria = example.createCriteria();

        //模糊查询
        /**if (StringUtils.isNotBlank(content.getProperty())) {
            criteria.andLike("property", "%" + content.getProperty() + "%");
        }*/

        List<TbContent> list = contentMapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {
        List<TbContent> contentList = null;
        try {
             contentList = (List<TbContent>) redisTemplate.boundHashOps(REDIS_CONTENT).get(categoryId);
             if (contentList != null&&contentList.size()>0){
                 return contentList;
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Example example = new Example(TbContent.class);
        example.createCriteria().andEqualTo("categoryId", categoryId).andEqualTo("status","1" );
        example.orderBy("sortOrder").desc();
        contentList = contentMapper.selectByExample(example);

        try {
            redisTemplate.boundHashOps(REDIS_CONTENT).put(categoryId, contentList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentList;
    }

    public void add(TbContent content){
        super.add(content);

        try {
            redisTemplate.boundHashOps(REDIS_CONTENT).delete(content.getCategoryId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void  update( TbContent content){
        TbContent oldContent = findOne(content.getId());
        super.update(content);

        if (!oldContent.getCategoryId().equals(content.getCategoryId())){
            try {
                redisTemplate.boundHashOps(REDIS_CONTENT).delete(oldContent.getCategoryId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        redisTemplate.boundHashOps(REDIS_CONTENT).delete(content.getCategoryId());
    }

    public void deleteByIds(Long[] ids){
        Example example = new Example(TbContent.class);
        example.createCriteria().andIn("id", Arrays.asList(ids));
        List<TbContent> tbContents = contentMapper.selectByExample(example);
        for (TbContent tbContent : tbContents) {
            try {
                redisTemplate.boundHashOps(REDIS_CONTENT).delete(tbContent.getCategoryId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.deleteByIds(ids);
    }

}
