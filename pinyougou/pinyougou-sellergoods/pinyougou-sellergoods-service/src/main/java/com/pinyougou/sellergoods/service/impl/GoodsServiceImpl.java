package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.Goods;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class GoodsServiceImpl extends BaseServiceImpl<TbGoods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsEditMapper goodsEditMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private GoodsDescMapper goodsDescMapper;

    @Override
    public PageInfo<TbGoods> search(Integer pageNum, Integer pageSize, TbGoods goods) {
        //设置分页
        PageHelper.startPage(pageNum, pageSize);
        //创建查询对象
        Example example = new Example(TbGoods.class);

        //创建查询条件对象
        Example.Criteria criteria = example.createCriteria();

        //模糊查询
        if (StringUtils.isNotBlank(goods.getAuditStatus())) {
            criteria.andEqualTo("auditStatus", goods.getAuditStatus());
        }
        if (StringUtils.isNotBlank(goods.getSellerId())) {
            criteria.andEqualTo("sellerId", goods.getSellerId());
        }
        if (StringUtils.isNotBlank(goods.getGoodsName())) {
            criteria.andLike("goodsName", "%"+goods.getGoodsName()+"%");
        }
        criteria.andNotEqualTo("isDelete", 1);


        List<TbGoods> list = goodsMapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Override
    public void addGoods(Goods goods) {
        add(goods.getGoods());
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        goodsEditMapper.insert(goods.getGoodsDesc());
        //保存sku
        saveItemList(goods);

    }

    private void saveItemList(Goods goods){

        if ("1".equals(goods.getGoods().getIsEnableSpec())){
            //启用规格
            if (goods.getItemList()!=null&&goods.getItemList().size()>0){
                for (TbItem tbItem : goods.getItemList()) {
                    //设置tittle
                    String tittle = goods.getGoods().getGoodsName();

                    Map<String,String> specName = JSON.parseObject(tbItem.getSpec(), Map.class);
                    for (Map.Entry<String, String> entry : specName.entrySet()) {
                        tittle += entry.getValue();
                    }
                    tbItem.setTitle(tittle);

                    //设置图片
                    if (StringUtils.isNotBlank(goods.getGoodsDesc().getItemImages())){
                        List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);

                        if (imageList != null && imageList.size()>0){
                            tbItem.setImage(imageList.get(0).get("url")+"");
                        }
                    }
                    //设置三级分类
                    TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
                    tbItem.setCategory(tbItemCat.getName());
                    tbItem.setCategoryid(tbItemCat.getId());

                    //设置卖家
                    TbSeller tbSeller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
                    tbItem.setSeller(tbSeller.getName());
                    tbItem.setSellerId(tbSeller.getSellerId());

                    //品牌
                    TbBrand tbBrand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
                    tbItem.setBrand(tbBrand.getName());

                    tbItem.setGoodsId(goods.getGoods().getId());

                    tbItem.setCreateTime(new Date());
                    tbItem.setUpdateTime(tbItem.getCreateTime());
                    itemMapper.insertSelective(tbItem);
                }

            }else {
                //不启用规格
                /*价格：从spu的价格中复制而来

库存：可以指定9999

是否启用：不启用，0

是否默认：默认，1

规格：{}*/
                TbItem tbItem = new TbItem();
                tbItem.setNum(9999);
                tbItem.setStatus("0");
                tbItem.setIsDefault("1");
                tbItem.setSpec("{}");
                tbItem.setTitle(goods.getGoods().getGoodsName());
                saveValue(goods,tbItem );
                itemMapper.insertSelective(tbItem);
            }
        }

    }




    @Override
    public void updateStatus(String status,Long[] ids) {
        Example example = new Example(TbGoods.class);
        example.createCriteria().andIn("id", Arrays.asList(ids));
        List<TbGoods> tbGoods = goodsMapper.selectByExample(example);
        for (TbGoods tbGood : tbGoods) {
            tbGood.setAuditStatus(status);
            goodsMapper.updateByPrimaryKey(tbGood);
        }

        TbItem tbItem = new TbItem();
        if(status.equals("2")){

           tbItem.setStatus("1");
        }else {
            tbItem.setStatus("0");
        }
        Example example1 = new Example(TbItem.class);
        example.createCriteria().andIn("goodsId",Arrays.asList(ids));

        itemMapper.updateByExampleSelective(tbItem,example );
    }

    @Override
    public Goods findGoods(Long id) {
        Goods goods = new Goods();

        TbGoods tbGoods = findOne(id);
        goods.setGoods(tbGoods);

        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setGoodsDesc(tbGoodsDesc);

        TbItem tbItem = new TbItem();
        tbItem.setGoodsId(id);
        List<TbItem> tbItems = itemMapper.select(tbItem);
        goods.setItemList(tbItems);


        return goods;
    }

    @Override
    public void updateGoods(Goods goods) {
        TbGoods tbGoods = goods.getGoods();
        tbGoods.setAuditStatus("0");
        update(tbGoods);

        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());

        //先删除，后保存sku
        TbItem tbItem = new TbItem();
        tbItem.setGoodsId(goods.getGoods().getId());
        itemMapper.delete(tbItem);

        saveItemList(goods);
    }

    @Override
    public void deleteGoods(Long[] ids) {
        Example example = new Example(TbGoods.class);
        example.createCriteria().andIn("id",Arrays.asList(ids) );
        TbGoods tbGoods = new TbGoods();
        tbGoods.setIsDelete("1");
        goodsMapper.updateByExampleSelective(tbGoods,example);
    }

    private void saveValue(Goods goods,TbItem tbItem){
        //设置图片
        if (StringUtils.isNotBlank(goods.getGoodsDesc().getItemImages())){
            List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);

            if (imageList != null && imageList.size()>0){
                tbItem.setImage(imageList.get(0).get("url")+"");
            }
        }
        //设置三级分类
        TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
        tbItem.setCategory(tbItemCat.getName());
        tbItem.setCategoryid(tbItemCat.getId());

        //设置卖家
        TbSeller tbSeller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        tbItem.setSeller(tbSeller.getName());
        tbItem.setSellerId(tbSeller.getSellerId());

        //品牌
        TbBrand tbBrand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        tbItem.setBrand(tbBrand.getName());

        tbItem.setGoodsId(goods.getGoods().getId());

        tbItem.setCreateTime(new Date());
        tbItem.setUpdateTime(tbItem.getCreateTime());
    }

}
