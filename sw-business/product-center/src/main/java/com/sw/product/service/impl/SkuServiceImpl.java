package com.sw.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.product.Sku;
import com.sw.product.mapper.SkuMapper;
import com.sw.product.service.ISkuService;
import org.springframework.stereotype.Service;

/**
 * 商品库存单位
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-11 22:07:42
 */
@Service("skuService")
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements ISkuService {

}
