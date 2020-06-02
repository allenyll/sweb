package com.sw.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.product.Attributes;
import com.sw.product.mapper.AttributesMapper;
import com.sw.product.service.IAttributesService;
import org.springframework.stereotype.Service;

/**
 * 属性值
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-12 17:47:06
 */
@Service("attributesService")
public class AttributesServiceImpl extends ServiceImpl<AttributesMapper, Attributes> implements IAttributesService {

}
