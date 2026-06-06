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

export const vendorTasks = [
  { title: '入驻审核', value: '待补充资质', status: 'warning' },
  { title: '今日订单', value: '8 单', status: 'active' },
  { title: '摊位预约', value: '2 个待确认', status: 'active' },
  { title: '评价回复', value: '3 条未回复', status: 'warning' }
]

export const vendorMenu = [
  { title: '商家开店', path: '/vendor/apply', desc: '线上备案、身份和资质材料' },
  { title: '资质管理', path: '/vendor/qualifications', desc: '健康证、摊车照片、经营照片' },
  { title: '摊位出摊', path: '/vendor/stalls', desc: '预约、日历、打卡、临时休摊' },
  { title: '商品管理', path: '/vendor/products', desc: '商品、图片、视频、全景占位' },
  { title: '订单处理', path: '/vendor/orders', desc: '接单、拒单、备货状态' },
  { title: '评价回复', path: '/vendor/reviews', desc: '查看评价并回复用户' }
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
