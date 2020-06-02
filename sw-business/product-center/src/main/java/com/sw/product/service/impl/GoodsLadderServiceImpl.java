package com.sw.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.product.GoodsLadder;
import com.sw.product.mapper.GoodsLadderMapper;
import com.sw.product.service.IGoodsLadderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 商品阶梯价格关联
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-28 17:24:48
 */
@Service("goodsLadderService")
public class GoodsLadderServiceImpl extends ServiceImpl<GoodsLadderMapper, GoodsLadder> implements IGoodsLadderService {

}
