export const featureZones = [
  { title: '助农专区', desc: '乡野新农人鲜货', tone: 'green' },
  { title: '非遗传承', desc: '守艺匠人与传统小吃', tone: 'amber' },
  { title: '公益摊位', desc: '微光创业者展示', tone: 'rose' },
  { title: '今日推荐', desc: '附近高评分摊位', tone: 'blue' }
]

export const categories = ['烧烤炸串', '甜点饮品', '地方特色', '传统小吃', '手工文创', '农家特产', '非遗好物']

export const stalls = [
  {
    id: 1,
    name: '烟火小摊',
    vendor: '林师傅',
    category: '地方特色',
    status: '营业中',
    distance: '680m',
    rating: '4.8',
    address: '北站中心公园东门',
    story: '主打现做小吃和本地特色汤粉，支持预约取餐。',
    products: ['招牌汤粉', '手作糍粑', '冰糖绿豆沙']
  },
  {
    id: 2,
    name: '乡野新农人鲜铺',
    vendor: '陈小禾',
    category: '农家特产',
    status: '今日推荐',
    distance: '1.2km',
    rating: '4.7',
    address: '市民广场南侧临时摊区',
    story: '助农农户直供，售卖当季蔬果、土鸡蛋和手工酱菜。',
    products: ['当季蔬果', '农家土鸡蛋', '手工辣酱']
  },
  {
    id: 3,
    name: '守艺糖画铺',
    vendor: '周老师',
    category: '非遗好物',
    status: '即将出摊',
    distance: '2.0km',
    rating: '4.9',
    address: '老街口文创夜市',
    story: '非遗糖画体验摊，适合亲子互动和城市文化打卡。',
    products: ['生肖糖画', '定制糖牌', '糖画体验']
  }
]

export const recommendationCards = [
  { stallId: 1, name: '烟火小摊', score: '92', reason: '你常浏览地方特色，且距离最近', waitTime: '约 8 分钟', tags: ['热汤粉', '取餐快', '夜市热摊'] },
  { stallId: 2, name: '乡野新农人鲜铺', score: '86', reason: '助农专区高评分，适合午市采购', waitTime: '约 5 分钟', tags: ['新鲜蔬果', '农家直供', '高复购'] },
  { stallId: 3, name: '守艺糖画铺', score: '81', reason: '附近热门故事互动高，适合亲子打卡', waitTime: '约 12 分钟', tags: ['非遗体验', '亲子', '故事热度'] }
]

export const agentMessages = [
  { role: 'assistant', content: '我可以帮你把“老样子”“张阿姨的煎饼”解析成预约订单。' },
  { role: 'user', content: '帮我订烟火小摊的招牌汤粉，两份，18:30 取。' },
  { role: 'assistant', content: '已识别：烟火小摊，招牌汤粉 x2，预计等待 8 分钟。' }
]

export const vendorStoryFeed = [
  { id: 1, vendor: '林师傅', stall: '烟火小摊', content: '凌晨四点熬汤底，今天第一锅汤粉已经准备好。', likes: 128, comments: 18, time: '今天 07:20', tags: ['手作', '热汤粉'] },
  { id: 2, vendor: '陈小禾', stall: '乡野新农人鲜铺', content: '今天带来刚采的番茄和土鸡蛋，午市在市民广场南侧。', likes: 96, comments: 11, time: '今天 10:10', tags: ['助农', '鲜货'] },
  { id: 3, vendor: '周老师', stall: '守艺糖画铺', content: '周六下午做生肖糖画体验，欢迎带孩子来老街口。', likes: 203, comments: 34, time: '昨天 18:40', tags: ['非遗', '亲子'] }
]

export const userOrders = [
  {
    id: 1001,
    stall: '烟火小摊',
    status: '待取餐',
    amount: '32.00',
    time: '今天 18:30',
    contact: '138****6201',
    address: '北站中心公园东门',
    items: [
      { name: '招牌汤粉', quantity: 2, price: '16.00' }
    ],
    timeline: ['订单已创建', '摊主已接单', '备货中', '待取餐']
  },
  {
    id: 1002,
    stall: '乡野新农人鲜铺',
    status: '已完成',
    amount: '46.50',
    time: '昨天 12:10',
    contact: '136****9128',
    address: '市民广场南侧临时摊区',
    items: [
      { name: '当季蔬果', quantity: 1, price: '28.50' },
      { name: '手工辣酱', quantity: 1, price: '18.00' }
    ],
    timeline: ['订单已创建', '摊主已接单', '已取餐', '已完成']
  }
]

export const userComplaints = [
  { id: 5001, target: '烟火小摊', type: '卫生问题', status: '处理中', updatedAt: '今天 14:20', description: '现场卫生需要进一步核实。' },
  { id: 5002, target: '市民广场摊区', type: '占道经营', status: '已办结', updatedAt: '昨天 16:30', description: '监管人员已现场处理并反馈。' }
]

export const reviewMetrics = ['卫生', '服务', '品质', '性价比']

export const stallReviews = [
  { user: '陈同学', stallId: 1, rating: 5, content: '汤粉很热，取餐也快，卫生和服务都不错。', time: '今天 19:20', likes: 12 },
  { user: '周女士', stallId: 1, rating: 4, content: '糍粑口感不错，希望晚一点还有库存。', time: '昨天 20:10', likes: 8 },
  { user: '李先生', stallId: 2, rating: 5, content: '蔬菜新鲜，土鸡蛋品质稳定。', time: '昨天 12:40', likes: 9 }
]

export const userFavorites = [
  { id: 1, type: '摊位', name: '烟火小摊', desc: '地方特色 · 680m', status: '营业中' },
  { id: 2, type: '商品', name: '手作糍粑', desc: '烟火小摊 · ¥12 起', status: '已收藏' },
  { id: 3, type: '摊主', name: '周老师', desc: '守艺糖画铺 · 非遗好物', status: '已关注' }
]

export const userSubscriptions = [
  { id: 1, stall: '烟火小摊', time: '每天 17:30', channel: '站内信', status: '已开启' },
  { id: 2, stall: '守艺糖画铺', time: '周六 16:00', channel: '网页通知', status: '待授权' }
]

export const paymentRecords = [
  { id: 8001, orderId: 1001, type: 'H5 支付', status: '待支付', amount: '32.00', updatedAt: '今天 18:10' },
  { id: 8002, orderId: 1002, type: '退款', status: '无退款', amount: '0.00', updatedAt: '昨天 12:40' }
]

export const vendorTasks = [
  { title: '入驻审核', value: '待补充资质', status: 'warning' },
  { title: '今日订单', value: '8 单', status: 'active' },
  { title: '摊位预约', value: '2 个待确认', status: 'active' },
  { title: '评价回复', value: '3 条未回复', status: 'warning' }
]

export const vendorMenu = [
  { title: '商家开店', path: '/vendor/apply', desc: '线上备案、身份和资质材料' },
  { title: '资质管理', path: '/vendor/qualifications', desc: '健康证、摊车照片、经营照片' },
  { title: '选址推荐', path: '/vendor/location-recommendations', desc: '人流评分、竞争指数和可用点位' },
  { title: '摊位出摊', path: '/vendor/stalls', desc: '预约、日历、打卡、临时休摊' },
  { title: '商品管理', path: '/vendor/products', desc: '商品、图片、视频、全景占位' },
  { title: '订单处理', path: '/vendor/orders', desc: '接单、拒单、备货状态' },
  { title: '评价回复', path: '/vendor/reviews', desc: '查看评价并回复用户' },
  { title: '店铺信息', path: '/vendor/profile', desc: '公告、时段、环境与资料' },
  { title: '营销活动', path: '/vendor/activities', desc: '代金券、推荐位和自建活动' },
  { title: '分享推广', path: '/vendor/share', desc: '摊位链接、二维码、海报占位' },
  { title: '商家设置', path: '/vendor/settings', desc: '账号安全、打印机、快捷短语' },
  { title: '帮助中心', path: '/vendor/help', desc: '开店、资质、订单处理说明' }
]

export const locationRecommendations = [
  { area: '北站中心公园东门', trafficScore: 88, competition: '中', sameCategory: 2, available: 6, reason: '夜市人流稳定，同品类摊位可控。' },
  { area: '市民广场南侧临时摊区', trafficScore: 76, competition: '低', sameCategory: 1, available: 4, reason: '午市客流集中，适合农家特产。' },
  { area: '老街口文创夜市', trafficScore: 82, competition: '高', sameCategory: 5, available: 2, reason: '游客多，但同质化偏高，建议差异化商品。' }
]

export const vendorQualifications = [
  { title: '健康证', owner: '林师傅', status: '审核通过', expireAt: '2027-05-20' },
  { title: '摊车照片', owner: '烟火小摊', status: '待补充', expireAt: '长期有效' },
  { title: '经营场所照片', owner: '北站中心公园东门', status: '审核中', expireAt: '长期有效' },
  { title: '特殊身份认证', owner: '守艺匠人', status: '待审核', expireAt: '长期有效' }
]

export const vendorReservations = [
  { area: '北站中心公园东门', period: '18:00-22:00', date: '今天', status: '已确认' },
  { area: '市民广场南侧临时摊区', period: '10:00-14:00', date: '明天', status: '待审批' },
  { area: '老街口文创夜市', period: '17:30-23:00', date: '周六', status: '可预约' }
]

export const vendorProducts = [
  { name: '招牌汤粉', price: '16.00', category: '地方特色', status: '在售中', stock: 68 },
  { name: '手作糍粑', price: '12.00', category: '传统小吃', status: '待审核', stock: 40 },
  { name: '冰糖绿豆沙', price: '8.00', category: '甜点饮品', status: '已下架', stock: 0 }
]

export const vendorOrders = [
  { id: 9001, buyer: '陈同学', items: '招牌汤粉 x2', status: '待接单', pickupTime: '今天 18:30', amount: '32.00' },
  { id: 9002, buyer: '周女士', items: '手作糍粑 x1', status: '备货中', pickupTime: '今天 19:00', amount: '12.00' },
  { id: 9003, buyer: '李先生', items: '冰糖绿豆沙 x3', status: '已完成', pickupTime: '昨天 20:10', amount: '24.00' }
]

export const vendorReviews = [
  { user: '陈同学', rating: 5, content: '汤粉很热，取餐也快，摊主服务很好。', status: '待回复' },
  { user: '周女士', rating: 4, content: '糍粑口感不错，希望晚一点还有库存。', status: '已回复' }
]

export const vendorStoryDrafts = [
  { title: '凌晨四点的汤底', status: '草稿', updatedAt: '今天 09:10' },
  { title: '从老街学来的手作糍粑', status: '待审核', updatedAt: '昨天 21:20' }
]

export const userUtilityConfigs: Record<string, { title: string; eyebrow: string; action: string; rows: { title: string; desc: string; status: string }[] }> = {
  '/messages': {
    title: '消息中心',
    eyebrow: '通知、站内信、系统反馈',
    action: '全部已读',
    rows: [
      { title: '出摊提醒', desc: '烟火小摊将在 17:30 出摊', status: '未读' },
      { title: '投诉进度', desc: '工单 5001 已分派监管人员', status: '处理中' },
      { title: '系统通知', desc: '公益摊位专区规则已更新', status: '已读' }
    ]
  },
  '/help': {
    title: '帮助与客服',
    eyebrow: '在线客服、电话客服、常见问题',
    action: '联系在线客服',
    rows: [
      { title: '预约下单怎么取消', desc: '订单未接单前可取消，接单后需联系摊主。', status: 'FAQ' },
      { title: '如何提交投诉', desc: '进入我的投诉，填写对象、问题类型和举证材料。', status: 'FAQ' },
      { title: '如何成为摊主', desc: '提交入驻备案、资质材料和特殊身份认证。', status: 'FAQ' }
    ]
  },
  '/feedback': {
    title: '意见反馈',
    eyebrow: '产品建议与问题反馈',
    action: '提交反馈',
    rows: [
      { title: '地图定位不准确', desc: '记录设备、浏览器和定位权限状态。', status: '待提交' },
      { title: '页面体验建议', desc: '收集移动端与 PC 端体验问题。', status: '草稿' }
    ]
  },
  '/footprints': {
    title: '我的足迹',
    eyebrow: '最近浏览摊位和商品',
    action: '清空足迹',
    rows: [
      { title: '烟火小摊', desc: '今天 18:02 浏览摊位详情', status: '摊位' },
      { title: '手作糍粑', desc: '今天 17:40 浏览商品', status: '商品' },
      { title: '守艺糖画铺', desc: '昨天 21:16 浏览非遗专区', status: '摊位' }
    ]
  },
  '/my-reviews': {
    title: '我的评价',
    eyebrow: '已发布评价与待评价订单',
    action: '去评价',
    rows: [
      { title: '烟火小摊', desc: '卫生 5 星，服务 5 星，品质 5 星', status: '已发布' },
      { title: '乡野新农人鲜铺', desc: '订单已完成，等待评价', status: '待评价' }
    ]
  },
  '/wallet': {
    title: '我的钱包',
    eyebrow: '支付账单、红包、代金券、发票',
    action: '账单明细',
    rows: [
      { title: '支付账单', desc: 'H5 支付、退款、对账记录占位', status: 'mock' },
      { title: '红包/代金券', desc: '商家券和平台活动券占位', status: 'mock' },
      { title: '发票助手', desc: '发票抬头、税号和开票记录占位', status: 'mock' }
    ]
  },
  '/settings': {
    title: '设置',
    eyebrow: '账号、安全、隐私、关于我们',
    action: '退出登录',
    rows: [
      { title: '账号与安全', desc: '手机号、登录密码、注销账号', status: '可配置' },
      { title: '隐私管理', desc: '定位、通知、个性化推荐授权', status: '可配置' },
      { title: '关于我们', desc: '平台介绍、版本信息、服务协议', status: '信息' }
    ]
  }
}

export const vendorUtilityConfigs: Record<string, { title: string; eyebrow: string; action: string; rows: { title: string; desc: string; status: string }[] }> = {
  '/vendor/profile': {
    title: '店铺信息',
    eyebrow: '名称、公告、营业时段、门店环境',
    action: '保存信息',
    rows: [
      { title: '店铺名称', desc: '默林的广东肠粉早餐店', status: '已完善' },
      { title: '店铺公告', desc: '今日 18:00 北站中心公园出摊', status: '展示中' },
      { title: '门店环境', desc: '摊车、经营场所、商品照片', status: '待补充' }
    ]
  },
  '/vendor/activities': {
    title: '营销活动',
    eyebrow: '自建活动、推荐位、代金券',
    action: '发布活动',
    rows: [
      { title: '满 30 减 5', desc: '代金券活动，待内容审核', status: '待审核' },
      { title: '今日推荐申请', desc: '申请进入首页今日推荐', status: '审核通过' }
    ]
  },
  '/vendor/share': {
    title: '分享推广',
    eyebrow: '摊位链接、二维码、海报占位',
    action: '生成海报',
    rows: [
      { title: '摊位链接', desc: 'https://yuntanfang.local/stalls/1', status: '可复制' },
      { title: '二维码', desc: '二维码图片生成占位', status: '待生成' }
    ]
  },
  '/vendor/settings': {
    title: '商家设置',
    eyebrow: '账号安全、打印机、快捷短语',
    action: '保存设置',
    rows: [
      { title: '账号与安全', desc: '手机号、邮箱、登录密码、交易密码', status: '可配置' },
      { title: '云打印机', desc: '打印设备绑定与测试', status: '未绑定' },
      { title: '回评快捷短语', desc: '评价回复模板', status: '3 条' }
    ]
  },
  '/vendor/help': {
    title: '商家帮助中心',
    eyebrow: '开店、资质、订单、提现说明',
    action: '联系平台',
    rows: [
      { title: '如何选择品类', desc: '按主营商品选择经营品类，影响审核与展示。', status: 'FAQ' },
      { title: '资质审核多久', desc: '工作日通常 24 小时内完成初审。', status: 'FAQ' },
      { title: '订单拒单规则', desc: '拒单需填写原因，影响服务评分。', status: 'FAQ' }
    ]
  }
}
