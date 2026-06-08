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

    @PostMapping("/qualifications")
    public ApiResponse<Qualification> qualification(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.addQualification(uid(authorization), str(body, "qualificationType"), str(body, "mediaUrl")));
    }

    @PostMapping("/special-identities")
    public ApiResponse<SpecialIdentity> specialIdentity(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.addSpecialIdentity(uid(authorization), str(body, "identityType")));
    }

    @GetMapping("/stall-reservations")
    public ApiResponse<PageResult<StallReservation>> reservations(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.reservations(uid(authorization)));
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
        return ApiResponse.ok(vendorService.addProduct(uid(authorization), str(body, "productName"), body.get("price"), lng(body, "categoryId")));
    }

    @GetMapping("/products")
    public ApiResponse<com.yuntanfang.common.PageResult<Product>> products(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.products(uid(authorization)));
    }

    @GetMapping("/orders")
    public ApiResponse<PageResult<Order>> orders(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(vendorService.orders(uid(authorization)));
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
