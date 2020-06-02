package com.sw.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.product.Brand;
import com.sw.product.mapper.BrandMapper;
import com.sw.product.service.IBrandService;
import org.springframework.stereotype.Service;

/**
 * 商品品牌
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-03-21 10:04:09
 */
@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

}
