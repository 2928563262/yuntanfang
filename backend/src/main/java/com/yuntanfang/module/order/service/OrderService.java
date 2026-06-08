package com.yuntanfang.module.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntanfang.common.BusinessException;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.order.entity.Order;
import com.yuntanfang.module.order.entity.OrderItem;
import com.yuntanfang.module.order.entity.OrderStatusLog;
import com.yuntanfang.module.order.mapper.OrderItemMapper;
import com.yuntanfang.module.order.mapper.OrderMapper;
import com.yuntanfang.module.order.mapper.OrderStatusLogMapper;
import com.yuntanfang.module.review.entity.Review;
import com.yuntanfang.module.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderStatusLogMapper orderStatusLogMapper;
    private final ReviewMapper reviewMapper;

    @Transactional
    public Order create(Long userId, Long vendorId, Long stallId, String stallName,
                        String pickupTime, String contactPhone, String remark,
                        List<Map<String, Object>> items) {
        if (userId == null) {
            throw new BusinessException("未登录");
        }
        Order order = new Order();
        order.setUserId(userId);
        order.setVendorId(vendorId);
        order.setStallId(stallId);
        order.setStallName(stallName);
        order.setPickupTime(pickupTime);
        order.setContactPhone(contactPhone);
        order.setRemark(remark);
        order.setOrderStatus("created");
        orderMapper.insert(order);

        BigDecimal total = BigDecimal.ZERO;
        if (items != null) {
            for (Map<String, Object> item : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                if (item.get("productId") != null) {
                    orderItem.setProductId(Long.valueOf(String.valueOf(item.get("productId"))));
                }
                orderItem.setProductName(item.get("productName") == null ? null : String.valueOf(item.get("productName")));
                int quantity = item.get("quantity") == null ? 1 : Integer.parseInt(String.valueOf(item.get("quantity")));
                orderItem.setQuantity(quantity);
                BigDecimal price = item.get("price") == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(item.get("price")));
                orderItem.setPrice(price);
                total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
                orderItemMapper.insert(orderItem);
            }
        }
        order.setTotalAmount(total);
        orderMapper.updateById(order);
        writeLog(order.getId(), "created");
        return order;
    }

    public PageResult<Order> my(Long userId, long pageNo, long pageSize) {
        if (userId == null) {
            throw new BusinessException("未登录");
        }
        Page<Order> page = orderMapper.selectPage(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapper<Order>().eq(Order::getUserId, userId).orderByDesc(Order::getId));
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    public PageResult<Map<String, Object>> myWithItems(Long userId, long pageNo, long pageSize) {
        PageResult<Order> page = my(userId, pageNo, pageSize);
        List<Map<String, Object>> records = page.records().stream()
                .map(order -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", order.getId());
                    row.put("userId", order.getUserId());
                    row.put("vendorId", order.getVendorId());
                    row.put("stallId", order.getStallId());
                    row.put("stallName", order.getStallName());
                    row.put("orderStatus", order.getOrderStatus());
                    row.put("totalAmount", order.getTotalAmount());
                    row.put("pickupTime", order.getPickupTime());
                    row.put("contactPhone", order.getContactPhone());
                    row.put("remark", order.getRemark());
                    row.put("createdAt", order.getCreatedAt());
                    row.put("updatedAt", order.getUpdatedAt());
                    row.put("items", orderItemMapper.selectList(
                            new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId())));
                    return row;
                })
                .toList();
        return new PageResult<>(page.total(), page.pageNo(), page.pageSize(), records);
    }

    public Map<String, Object> detail(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id));
        List<OrderStatusLog> logs = orderStatusLogMapper.selectList(
                new LambdaQueryWrapper<OrderStatusLog>().eq(OrderStatusLog::getOrderId, id).orderByAsc(OrderStatusLog::getId));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("order", order);
        result.put("items", items);
        result.put("statusLogs", logs);
        return result;
    }

    @Transactional
    public Review review(Long orderId, Long userId, Integer rating) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        Review review = new Review();
        review.setOrderId(orderId);
        review.setUserId(userId);
        review.setRating(rating == null ? 5 : rating);
        review.setStatus("published");
        reviewMapper.insert(review);
        order.setOrderStatus("reviewed");
        orderMapper.updateById(order);
        writeLog(orderId, "reviewed");
        return review;
    }

    private void writeLog(Long orderId, String status) {
        OrderStatusLog log = new OrderStatusLog();
        log.setOrderId(orderId);
        log.setOrderStatus(status);
        orderStatusLogMapper.insert(log);
    }
}
