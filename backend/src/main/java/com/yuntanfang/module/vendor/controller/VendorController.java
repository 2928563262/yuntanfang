package com.yuntanfang.module.vendor.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.order.entity.Order;
import com.yuntanfang.module.product.entity.Product;
import com.yuntanfang.module.vendor.entity.Qualification;
import com.yuntanfang.module.vendor.entity.SpecialIdentity;
import com.yuntanfang.module.vendor.entity.StallCheckin;
import com.yuntanfang.module.vendor.entity.StallReservation;
import com.yuntanfang.module.vendor.entity.StallSchedule;
import com.yuntanfang.module.vendor.entity.Vendor;
import com.yuntanfang.module.vendor.service.VendorService;
import com.yuntanfang.security.AuthSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;
    private final AuthSupport authSupport;

    private Long uid(String authorization) {
        return authSupport.currentUserId(authorization);
    }

    private static String str(Map<String, Object> body, String key) {
        Object value = body.get(key);
        return value == null ? null : String.valueOf(value);
    }

    private static Long lng(Map<String, Object> body, String key) {
        Object value = body.get(key);
        return value == null ? null : Long.valueOf(String.valueOf(value));
    }

    @PostMapping("/applications")
    public ApiResponse<Vendor> apply(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.apply(uid(authorization), str(body, "vendorName"), str(body, "story")));
    }

    @GetMapping("/me")
    public ApiResponse<Vendor> me(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.me(uid(authorization)));
    }

    @PutMapping("/me")
    public ApiResponse<Vendor> updateMe(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.updateMe(uid(authorization), str(body, "vendorName"), str(body, "story")));
    }

    @GetMapping("/qualifications")
    public ApiResponse<PageResult<Qualification>> qualifications(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.qualifications(uid(authorization)));
    }

    @PostMapping("/qualifications")
    public ApiResponse<Qualification> qualification(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.addQualification(uid(authorization), str(body, "qualificationType"), str(body, "mediaUrl")));
    }

    @GetMapping("/special-identities")
    public ApiResponse<PageResult<SpecialIdentity>> specialIdentities(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.specialIdentities(uid(authorization)));
    }

    @PostMapping("/special-identities")
    public ApiResponse<SpecialIdentity> specialIdentity(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.addSpecialIdentity(uid(authorization), str(body, "identityType")));
    }

    @GetMapping("/stall-reservations")
    public ApiResponse<PageResult<Map<String, Object>>> reservations(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.reservationsWithStall(uid(authorization)));
    }

    @PostMapping("/stall-reservations")
    public ApiResponse<StallReservation> reserve(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.reserve(uid(authorization), lng(body, "stallId")));
    }

    @PostMapping("/schedules")
    public ApiResponse<StallSchedule> schedule(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.schedule(uid(authorization), lng(body, "stallId"), str(body, "scheduleDate")));
    }

    @PostMapping("/checkins")
    public ApiResponse<StallCheckin> checkin(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.checkin(uid(authorization), lng(body, "stallId"), body.get("latitude"), body.get("longitude")));
    }

    @PostMapping("/products")
    public ApiResponse<Product> product(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.addProduct(uid(authorization), lng(body, "stallId"), str(body, "productName"), body.get("price"), lng(body, "categoryId")));
    }

    @PutMapping("/products/{id}")
    public ApiResponse<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.updateProduct(uid(authorization), id, str(body, "productName"), body.get("price"),
                str(body, "description"), str(body, "status"), lng(body, "stallId"), lng(body, "categoryId")));
    }

    @GetMapping("/products")
    public ApiResponse<com.yuntanfang.common.PageResult<Product>> products(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.products(uid(authorization)));
    }

    @GetMapping("/orders")
    public ApiResponse<PageResult<Map<String, Object>>> orders(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.ordersWithItems(uid(authorization)));
    }

    @GetMapping("/orders/{id}")
    public ApiResponse<Map<String, Object>> orderDetail(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.orderDetail(uid(authorization), id));
    }

    @PutMapping("/orders/{id}/status")
    public ApiResponse<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.updateOrderStatus(uid(authorization), id, str(body, "status")));
    }

    @PostMapping("/reviews/{id}/reply")
    public ApiResponse<Map<String, Object>> replyReview(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.replyReview(uid(authorization), id, str(body, "reply")));
    }
}
