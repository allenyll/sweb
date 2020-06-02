package com.sw.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.entity.order.Order;
import com.sw.common.util.DataResponse;

import java.util.List;
import java.util.Map;

public interface IOrderService extends IService<Order> {

    /**
     * 获取订单数量
     * @param params
     * @return
     */
    int selectCount(Map<String, Object> params);

    /**
     * 分页查询订单
     * @param params
     * @return
     */
    List<Map<String, Object>> getOrderPage(Map<String, Object> params);

    /**
     * 删除订单
     * @param params
     * @return
     */
    DataResponse deleteOrder(Map<String, Object> params);

    /**
     * 订单发货
     * @param params
     * @return
     */
    DataResponse deliveryOrder(Map<String, Object> params);

    /**
     * 关闭订单
     * @param params
     * @return
     */
    DataResponse closeOrder(Map<String, Object> params);

    /**
     * 修改订单价格
     * @param params
     * @return
     */
    DataResponse updateMoneyInfo(Map<String, Object> params);

    /**
     * 修改订单收货人信息
     * @param params
     * @return
     */
    DataResponse updateReceiverInfo(Map<String, Object> params);

    /**
     * 修改订单备注
     * @param params
     * @return
     */
    DataResponse updateOrderNote(Map<String, Object> params);

    /**
     * 创建订单
     * @param order
     * @param param
     */
    void createOrder(Order order, Map<String, Object> param);

    /**
     * 获取未支付订单数量
     * @param params
     * @return
     */
    int getUnPayNum(Map<String, Object> params);

    /**
     * 获取未收货订单数量
     * @param params
     * @return
     */
    int getUnReceiveNum(Map<String, Object> params);

    /**
     * 获取已发货订单数量
     * @param params
     * @return
     */
    int getDeliveryNum(Map<String, Object> params);

    /**
     * 获取已收货订单数量
     * @param params
     * @return
     */
    int getReceiveNum(Map<String, Object> params);

    /**
     * 获取未评价订单数量
     * @param params
     * @return
     */
    int getAppraisesNum(Map<String, Object> params);

    /**
     * 获取已完成订单数量
     * @param params
     * @return
     */
    int getFinishNum(Map<String, Object> params);

    /**
     * 获取订单列表
     * @param params
     * @return
     */
    List<Order> getOrderList(Map<String, Object> params);
}
