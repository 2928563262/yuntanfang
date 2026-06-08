# Agent API 待开发清单

## 订单与支付

- `pay_order`：发起 H5 支付、轮询支付状态。
- `cancel_order`：取消待支付/待接单订单。
- `refund_order`：提交退款申请、查询退款进度。
- `show_pickup_code`：展示取餐码和取餐说明。

## 摊位与商品

- `stall_detail`：查看摊位详情、营业时间、位置、评分。
- `product_detail`：查看商品价格、库存、规格、图片。
- `subscribe_stall`：订阅出摊提醒。
- `favorite_stall`：收藏/取消收藏摊位。

## 用户中心

- `show_favorites`：查看收藏关注。
- `show_messages`：查看消息中心。
- `show_wallet`：查看钱包、账单、券。
- `update_profile`：修改手机号、昵称、隐私设置。

## Agent 体验与会话

- 会话列表增加删除按钮，支持删除单个历史会话。
- 删除当前会话后自动切换到最近一条会话；如果没有剩余会话，则创建新会话。
- 删除前需要轻量确认，避免误删。

## 内容与安全

- `upload_evidence`：投诉证据上传。
- `upload_review_media`：评价图片/视频上传。
- `content_safety_check`：评价、投诉、故事内容审核。

## 商家与管理端

- `vendor_accept_order`：商家接单。
- `vendor_update_product`：商家商品上下架。
- `admin_audit_vendor`：管理端审核摊主入驻。
- `admin_handle_complaint`：管理端处理投诉。

## 后续工程化

- 将当前 mock action 接入真实数据库表。
- 增加 Agent action 审计日志。
- 增加用户确认二次校验，敏感操作禁止自动执行。
- 增加端到端测试：自然语言输入到 action 输出。
- 增加按角色的 skill catalog，用户端、商家端、管理端分离。
