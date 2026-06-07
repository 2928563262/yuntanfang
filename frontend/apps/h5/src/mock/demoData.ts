export interface DemoStall {
  id: number
  name: string
  category: string
  score: string
  sales: number
  avg: number
  time: string
  distance: string
  area: string
  address: string
  hours: string
  announcement: string
  tags: string[]
  open: boolean
  image: string
  cover: string
}

export interface DemoProduct {
  id: number
  stallId: number
  category: string
  name: string
  price: number
  originalPrice?: number
  sales: number
  description: string
  image: string
  status?: 'available' | 'soldout'
}

export interface DemoReview {
  id: number
  stallId: number
  user: string
  rating: number
  content: string
  time: string
}

export interface CartItem {
  productId: number
  stallId: number
  quantity: number
}

export interface DemoOrder {
  id: string
  stallId: number
  stallName: string
  status: 'preparing' | 'pickup' | 'review' | 'refund' | 'done'
  items: Array<{ productId: number; name: string; price: number; quantity: number; image: string }>
  amount: number
  pickupTime: string
  remark: string
  pickupCode: string
  createdAt: string
  reviewed?: boolean
}

const imageBase = 'https://images.unsplash.com/'

export const demoStalls: DemoStall[] = [
  {
    id: 1,
    name: '刘记烧烤',
    category: '烧烤炸串',
    score: '4.8分',
    sales: 312,
    avg: 20,
    time: '20分钟',
    distance: '2.2km',
    area: '北站夜市A区',
    address: '深圳北站中心公园东广场 A12 摊位',
    hours: '18:00-23:30',
    announcement: '今晚烤串满 100 减 6，支持提前预约自提。',
    tags: ['50减3', '100减6', '已备案'],
    open: true,
    image: `${imageBase}photo-1529692236671-f1f6cf9683ba?auto=format&fit=crop&w=260&q=80`,
    cover: `${imageBase}photo-1555939594-58d7cb561ad1?auto=format&fit=crop&w=900&q=70`
  },
  {
    id: 2,
    name: '小林柠檬茶',
    category: '茶饮饮品',
    score: '4.7分',
    sales: 268,
    avg: 15,
    time: '12分钟',
    distance: '880m',
    area: '中心公园东门',
    address: '深圳北站中心公园东门便民摊位 06',
    hours: '14:00-22:00',
    announcement: '公益摊位，今日手打柠檬茶第二杯半价。',
    tags: ['公益摊位', '今日推荐', '满35减5'],
    open: true,
    image: `${imageBase}photo-1551024601-bec78aea704b?auto=format&fit=crop&w=260&q=80`,
    cover: `${imageBase}photo-1525385133512-2f3bdd039054?auto=format&fit=crop&w=900&q=70`
  },
  {
    id: 3,
    name: '老王早食',
    category: '特色小吃',
    score: '4.6分',
    sales: 196,
    avg: 10,
    time: '6分钟',
    distance: '320m',
    area: '大学城临时点',
    address: '大学城夜市便民经营点 B03',
    hours: '06:30-10:30',
    announcement: '明早 6:30 出摊，可提前预约煎饼和豆浆。',
    tags: ['助农摊位', '预约自提', '150减10'],
    open: false,
    image: `${imageBase}photo-1533089860892-a7c6f0a88666?auto=format&fit=crop&w=260&q=80`,
    cover: `${imageBase}photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=900&q=70`
  }
]

export const demoProducts: DemoProduct[] = [
  { id: 101, stallId: 1, category: '招牌热销', name: '多汁爆鸡肉套餐', price: 25, sales: 53, description: '鸡胸肉、烤蔬菜和秘制酱料，适合晚间自提。', image: `${imageBase}photo-1555939594-58d7cb561ad1?auto=format&fit=crop&w=360&q=80` },
  { id: 102, stallId: 1, category: '炭烤牛肉', name: '招牌牛肉炙烤单人套餐', price: 35, originalPrice: 42, sales: 39, description: '炭烤牛肉配时蔬，口味可备注微辣。', image: `${imageBase}photo-1529692236671-f1f6cf9683ba?auto=format&fit=crop&w=360&q=80` },
  { id: 103, stallId: 1, category: '酒水饮料', name: '可口可乐', price: 5, sales: 90, description: '冰镇饮料，建议取餐后尽快饮用。', image: `${imageBase}photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=360&q=80` },
  { id: 201, stallId: 2, category: '招牌饮品', name: '手打柠檬茶', price: 13, sales: 126, description: '现切香水柠檬，茶底清爽。', image: `${imageBase}photo-1551024601-bec78aea704b?auto=format&fit=crop&w=360&q=80` },
  { id: 202, stallId: 2, category: '招牌饮品', name: '暴打鸭屎香柠檬茶', price: 16, sales: 84, description: '鸭屎香茶底，适合少冰。', image: `${imageBase}photo-1621263764928-df1444c5e859?auto=format&fit=crop&w=360&q=80` },
  { id: 301, stallId: 3, category: '早餐', name: '煎饼果子', price: 9, sales: 88, description: '鸡蛋、薄脆和秘制酱，支持加肠。', image: `${imageBase}photo-1533089860892-a7c6f0a88666?auto=format&fit=crop&w=360&q=80` },
  { id: 302, stallId: 3, category: '早餐', name: '现磨豆浆', price: 4, sales: 110, description: '早市现磨，少糖可备注。', image: `${imageBase}photo-1577805947697-89e18249d767?auto=format&fit=crop&w=360&q=80` }
]

export const demoReviews: DemoReview[] = [
  { id: 1, stallId: 1, user: '林同学', rating: 5, content: '提前下单不用排队，烤串味道很足。', time: '今天 19:20' },
  { id: 2, stallId: 1, user: '陈女士', rating: 4, content: '摊位位置好找，备案信息展示清楚。', time: '昨天 21:10' },
  { id: 3, stallId: 2, user: '阿南', rating: 5, content: '柠檬茶很清爽，公益摊位体验不错。', time: '今天 16:05' }
]

export const demoMessages = [
  { id: 1, type: '订单通知', title: '刘记烧烤正在备餐', text: '你的预约单预计 20:10 可取。', time: '刚刚' },
  { id: 2, type: '系统通知', title: '摊位备案信息已更新', text: '附近新增 3 个放心消费摊位。', time: '10:24' },
  { id: 3, type: '活动通知', title: '公益专区今日满减', text: '小林柠檬茶第二杯半价。', time: '昨天' }
]

const CART_KEY = 'ytf_demo_cart'
const ORDERS_KEY = 'ytf_demo_orders'

export function getStall(id: string | number) {
  return demoStalls.find((stall) => stall.id === Number(id)) ?? demoStalls[0]
}

export function getProduct(id: string | number) {
  return demoProducts.find((product) => product.id === Number(id)) ?? demoProducts[0]
}

export function getProductsByStall(stallId: string | number) {
  return demoProducts.filter((product) => product.stallId === Number(stallId))
}

export function getReviewsByStall(stallId: string | number) {
  return demoReviews.filter((review) => review.stallId === Number(stallId))
}

export function readCart(): CartItem[] {
  return readJson<CartItem[]>(CART_KEY, [])
}

export function writeCart(items: CartItem[]) {
  localStorage.setItem(CART_KEY, JSON.stringify(items.filter((item) => item.quantity > 0)))
}

export function addToCart(productId: number, quantity = 1) {
  const product = getProduct(productId)
  const cart = readCart()
  const existing = cart.find((item) => item.productId === productId)
  if (existing) existing.quantity += quantity
  else cart.push({ productId, stallId: product.stallId, quantity })
  writeCart(cart)
}

export function updateCartQuantity(productId: number, quantity: number) {
  const cart = readCart().map((item) => item.productId === productId ? { ...item, quantity } : item)
  writeCart(cart)
}

export function cartDetails() {
  return readCart().map((item) => ({ ...item, product: getProduct(item.productId) }))
}

export function cartTotal() {
  return cartDetails().reduce((sum, item) => sum + item.product.price * item.quantity, 0)
}

export function clearCart() {
  writeCart([])
}

export function readOrders(): DemoOrder[] {
  const existing = readJson<DemoOrder[]>(ORDERS_KEY, [])
  if (existing.length) return existing
  return seedOrders()
}

export function writeOrders(orders: DemoOrder[]) {
  localStorage.setItem(ORDERS_KEY, JSON.stringify(orders))
}

export function createDemoOrder(payload: { pickupTime: string; remark: string }) {
  const items = cartDetails()
  const stall = getStall(items[0]?.stallId ?? 1)
  const order: DemoOrder = {
    id: String(Date.now()),
    stallId: stall.id,
    stallName: stall.name,
    status: 'preparing',
    items: items.map((item) => ({ productId: item.product.id, name: item.product.name, price: item.product.price, quantity: item.quantity, image: item.product.image })),
    amount: cartTotal(),
    pickupTime: payload.pickupTime,
    remark: payload.remark,
    pickupCode: String(Math.floor(1000 + Math.random() * 9000)),
    createdAt: new Date().toLocaleString('zh-CN')
  }
  writeOrders([order, ...readOrders()])
  clearCart()
  return order
}

export function getOrder(id: string | number) {
  return readOrders().find((order) => order.id === String(id)) ?? readOrders()[0]
}

export function markOrderReviewed(id: string | number) {
  const orders = readOrders().map((order) => order.id === String(id) ? { ...order, status: 'done' as const, reviewed: true } : order)
  writeOrders(orders)
}

function seedOrders(): DemoOrder[] {
  const first = getProduct(101)
  const second = getProduct(201)
  return [
    {
      id: 'demo-1001',
      stallId: 1,
      stallName: '刘记烧烤',
      status: 'pickup',
      items: [{ productId: first.id, name: first.name, price: first.price, quantity: 1, image: first.image }],
      amount: first.price,
      pickupTime: '今天 20:10',
      remark: '少辣',
      pickupCode: '3862',
      createdAt: '今天 19:40'
    },
    {
      id: 'demo-1002',
      stallId: 2,
      stallName: '小林柠檬茶',
      status: 'review',
      items: [{ productId: second.id, name: second.name, price: second.price, quantity: 2, image: second.image }],
      amount: second.price * 2,
      pickupTime: '今天 16:30',
      remark: '少冰',
      pickupCode: '7265',
      createdAt: '今天 16:02'
    }
  ]
}

function readJson<T>(key: string, fallback: T): T {
  try {
    const raw = localStorage.getItem(key)
    return raw ? JSON.parse(raw) as T : fallback
  } catch {
    return fallback
  }
}
