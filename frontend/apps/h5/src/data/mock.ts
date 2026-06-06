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
  { id: 1001, stall: '烟火小摊', status: '待取餐', amount: '32.00', time: '今天 18:30' },
  { id: 1002, stall: '乡野新农人鲜铺', status: '已完成', amount: '46.50', time: '昨天 12:10' }
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
  { title: '摊位出摊', path: '/vendor/stalls', desc: '预约、日历、打卡、临时休摊' },
  { title: '商品管理', path: '/vendor/products', desc: '商品、图片、视频、全景占位' },
  { title: '订单处理', path: '/vendor/orders', desc: '接单、拒单、备货状态' },
  { title: '评价回复', path: '/vendor/reviews', desc: '查看评价并回复用户' }
]
