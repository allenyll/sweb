package com.sw.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.order.OrderOperateLog;
import com.sw.common.util.MapUtil;
import com.sw.order.mapper.OrderOperateLogMapper;
import com.sw.order.service.IOrderOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 记录订单操作日志
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-07-16 16:34:06
 */
@Service("orderOperateLogService")
public class OrderOperateLogServiceImpl extends ServiceImpl<OrderOperateLogMapper, OrderOperateLog> implements IOrderOperateLogService {

    @Autowired
    OrderOperateLogMapper orderOperateLogMapper;

    public List<OrderOperateLog> getOperateList(Map<String, Object> map) {
        QueryWrapper<OrderOperateLog> wrapper  = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("FK_ORDER_ID", MapUtil.getString(map, "orderId"));
        return  orderOperateLogMapper.selectList(wrapper);
    }
}
