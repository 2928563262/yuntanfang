# 前端页面缺口盘点

依据：`云摊坊_功能数据设计说明书.md`、当前 H5 路由、当前管理后台路由。

## 当前已覆盖页面

### H5 普通用户

- 登录：`/login`
- 首页：`/`
- 智能推荐：`/recommendations`
- 智能点单：`/agent-order`
- 摊主故事流：`/stories`
- 摊位列表与筛选：`/stalls`
- 摊位详情：`/stalls/:id`
- 预约下单：`/stalls/:id/reserve`
- 我的订单：`/orders`
- 订单详情：`/orders/:id`
- 发布评价：`/reviews/create/:orderId`
- 投诉列表：`/complaints`
- 提交投诉：`/complaints/create`
- 收藏关注：`/favorites`
- 出摊提醒：`/subscriptions`
- 我的页：`/profile`
- 消息、帮助、反馈、足迹、评价、钱包、设置：`/messages`、`/help`、`/feedback`、`/footprints`、`/my-reviews`、`/wallet`、`/settings`

### H5 商家端

- 商家工作台：`/vendor/dashboard`
- 入驻申请：`/vendor/apply`
- 选址推荐：`/vendor/location-recommendations`
- 资质管理：`/vendor/qualifications`
- 摊位预约与出摊：`/vendor/stalls`
- 商品管理：`/vendor/products`
- 订单处理：`/vendor/orders`
- 评价回复：`/vendor/reviews`
- 摊主故事：`/vendor/stories`
- 故事发布：`/vendor/stories/create`
- 店铺信息、营销、分享、设置、帮助：`/vendor/profile`、`/vendor/activities`、`/vendor/share`、`/vendor/settings`、`/vendor/help`

### 管理后台

- 登录：`/login`
- 数据总览：`/dashboard`
- 入驻审核：`/vendors/audits`
- 资质审核：`/qualifications`
- 特殊身份/公益标签审核：`/special-identities`
- 经营区域：`/areas`
- 摊位管理：`/stalls`
- 摊位预约审批：`/stall-reservations`
- 内容审核：`/content-audits`
- 投诉工单：`/complaints`
- 违规记录：`/violations`
- 政策公告：`/policies`
- 通知管理：`/notices`
- 推荐监控：`/recommendation-logs`
- Agent 会话：`/agent-sessions`
- 标签管理：`/vendor-tags`
- 信用分管理：`/credit-scores`
- 数据上报：`/reports`
- 政务数据导入：`/data-imports`
- 用户、角色、系统：`/users`、`/roles`、`/system`

## 已有但仍是骨架的页面

- 管理后台大多数模块共用 `WorkbenchView`，现在是列表/筛选/指标 mock，需要按模块拆成真实业务页。
- 商家端商品、订单、评价、摊位、故事已有页面，但缺完整创建/编辑/详情/状态流转。
- 用户端钱包、设置、帮助、消息、足迹、我的评价目前是通用配置页，需要独立交互。
- 地图、支付、上传、消息推送、内容审核、权限均为占位交互。

## 说明书要求但当前缺失的页面

### 普通用户需要新增

- 推荐首页/推荐结果页：展示推荐摊位、推荐理由、等待时间、口碑标签。已新增骨架：`/recommendations`。
- Agent 智能点单页：聊天消息列表、输入框、订单确认卡片、降级商品选择入口。已新增骨架：`/agent-order`。
- 摊主主页：头像、昵称、从业年限、手艺简介、口碑标签、荣誉标签、关注按钮、故事流。
- 故事信息流页：关注摊主动态、附近热门故事、瀑布流/列表。已新增骨架：`/stories`。
- 评价列表页：摊位详情内评价区或独立评价页，支持排序、图片、点赞、多维评分展示。
- 推荐曝光反馈埋点不需要独立页面，但前端需要点击/曝光记录逻辑。

### 商家端需要新增

- 选址推荐页：地图+列表双视图、人流评分、竞争指数、同质化预警、可用摊位数。已新增骨架：`/vendor/location-recommendations`。
- 摊位申请/选位页：区域筛选、时段筛选、摊位详情、申请状态。
- 商品新增/编辑页：图片、价格、分类、库存、描述、上下架。
- 订单接单详情页：接单、拒单、备货、完成、退款处理。
- 故事发布/编辑页：1-9 张图片、文案、位置展示开关、发布状态。已新增发布骨架：`/vendor/stories/create`。
- 我的荣誉/信用分页：官方荣誉标签、信用分明细、加减分记录。
- 资质上传详情页：健康证、摊车照片、经营场所照片的上传和审核记录。

### 管理后台需要新增或拆分

- 推荐算法配置/监控页：推荐日志、曝光点击、转化、规则权重。已新增通用管理页：`/recommendation-logs`。
- Agent 会话监控页：会话列表、解析结果、失败降级、订单生成结果。已新增通用管理页：`/agent-sessions`。
- 故事内容审核详情页：图文预览、审核动作、下架原因。
- 差评预警页：低于 3 分评价列表、处理状态、关联摊位。
- 标签管理页：AI 口碑标签审核、官方荣誉标签颁发、有效期、撤销。已新增通用管理页：`/vendor-tags`。
- 信用分管理页：摊主信用分、加减分明细、违规联动。已新增通用管理页：`/credit-scores`。
- 政务区域地图页：围栏绘制/查看、责任人、经营时段、允许品类。
- 摊位分配看板：待审核申请、自动匹配建议、分配历史。
- 数据上报/报表页：周报/月报、摊位使用率、投诉率、信用分分布、导出。已新增通用管理页：`/reports`。
- 政务数据导入页：Excel/CSV 上传、字段映射、导入结果。已新增通用管理页：`/data-imports`。

## 推荐开发顺序

1. 用户登录后的普通用户闭环：推荐首页、摊位详情评价区、订单确认、评价列表。
2. 商家经营闭环：商品新增编辑、订单处理详情、摊位申请/选位、故事发布。
3. 管理监管闭环：区域地图、摊位分配、投诉派单、违规与信用分。
4. 增强能力：推荐日志、Agent 点单、标签生成/审核、报表上报。
