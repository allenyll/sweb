package com.sw.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.product.AttrOption;
import com.sw.product.mapper.AttrOptionMapper;
import com.sw.product.service.IAttrOptionService;
import org.springframework.stereotype.Service;

/**
 * 属性选项表,也就是属性的详情
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-12 17:55:00
 */
@Service("attrOptionService")
public class AttrOptionServiceImpl extends ServiceImpl<AttrOptionMapper, AttrOption> implements IAttrOptionService {

}
