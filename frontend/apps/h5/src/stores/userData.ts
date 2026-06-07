import { computed, reactive, watch } from 'vue'
import {
  paymentRecords,
  stallReviews,
  stalls,
  userComplaints,
  userFavorites,
  userOrders,
  userSubscriptions
} from '../data/mock'

export interface UserOrderItem {
  name: string
  quantity: number
  price: string
}

export interface UserOrder {
  id: number
  stallId: number
  stall: string
  status: '待支付' | '待接单' | '备货中' | '待取餐' | '已完成' | '已取消' | '退款中'
  amount: string
  time: string
  contact: string
  address: string
  pickupCode: string
  items: UserOrderItem[]
  timeline: string[]
  createdAt: string
}

export interface UserComplaint {
  id: number
  target: string
  type: string
  status: string
  updatedAt: string
  description: string
}

export interface UserFavorite {
  id: number
  refId: number
  type: '摊位' | '商品' | '摊主'
  name: string
  desc: string
  status: string
}

export interface UserSubscription {
  id: number
  stallId: number
  stall: string
  time: string
  channel: string
  status: string
}

export interface UserReview {
  id: number
  orderId: number
  stallId: number
  stall: string
  rating: number
  content: string
  time: string
  status: string
}

const storageKey = 'ytf_user_data_v1'

const initialState = {
  orders: userOrders.map((order) => ({
    ...order,
    stallId: stalls.find((stall) => stall.name === order.stall)?.id ?? 1,
    pickupCode: `YT${order.id}`,
    createdAt: order.time,
    status: order.status as UserOrder['status']
  })) satisfies UserOrder[],
  complaints: [...userComplaints] as UserComplaint[],
  favorites: userFavorites.map((item) => ({ ...item, refId: item.id })) as UserFavorite[],
  subscriptions: userSubscriptions.map((item) => ({
    ...item,
    stallId: stalls.find((stall) => stall.name === item.stall)?.id ?? item.id
  })) as UserSubscription[],
  reviews: stallReviews.map((review, index) => ({
    id: 7001 + index,
    orderId: index === 0 ? 1002 : 0,
    stallId: review.stallId,
    stall: stalls.find((stall) => stall.id === review.stallId)?.name ?? '附近摊位',
    rating: review.rating,
    content: review.content,
    time: review.time,
    status: '已发布'
  })) as UserReview[]
}

type UserState = typeof initialState

function readState(): UserState {
  const raw = localStorage.getItem(storageKey)
  if (!raw) {
    return initialState
  }

  try {
    return { ...initialState, ...JSON.parse(raw) }
  } catch {
    return initialState
  }
}

const state = reactive<UserState>(readState())

watch(
  state,
  () => {
    localStorage.setItem(storageKey, JSON.stringify(state))
  },
  { deep: true }
)

function nextId(items: { id: number }[]) {
  return Math.max(0, ...items.map((item) => item.id)) + 1
}

function nowLabel() {
  return '刚刚'
}

export function useUserDataStore() {
  const orders = computed(() => [...state.orders].sort((a, b) => b.id - a.id))
  const complaints = computed(() => [...state.complaints].sort((a, b) => b.id - a.id))
  const favorites = computed(() => state.favorites)
  const subscriptions = computed(() => state.subscriptions)
  const reviews = computed(() => [...state.reviews].sort((a, b) => b.id - a.id))

  function findOrder(id: number) {
    return state.orders.find((order) => order.id === id) ?? state.orders[0]
  }

  function createOrder(input: {
    stallId: number
    product: string
    quantity: number
    pickupTime: string
    contact: string
    amount: string
    price: string
  }) {
    const stall = stalls.find((item) => item.id === input.stallId) ?? stalls[0]
    const id = nextId(state.orders)
    const order: UserOrder = {
      id,
      stallId: stall.id,
      stall: stall.name,
      status: '待支付',
      amount: input.amount,
      time: input.pickupTime,
      contact: input.contact || '未填写联系方式',
      address: stall.address,
      pickupCode: `YT${id}`,
      items: [{ name: input.product, quantity: input.quantity, price: input.price }],
      timeline: ['订单已创建', '待支付'],
      createdAt: nowLabel()
    }
    state.orders.unshift(order)
    return order
  }

  function createOrderFromAgent(input: {
    stallName: string
    items: { name: string; quantity: number }[]
    pickupTime: string
    totalAmount: string
  }) {
    const stall = stalls.find((item) => item.name === input.stallName) ?? stalls[0]
    const firstItem = input.items[0] ?? { name: stall.products[0], quantity: 1 }
    return createOrder({
      stallId: stall.id,
      product: firstItem.name,
      quantity: firstItem.quantity,
      pickupTime: input.pickupTime,
      contact: '138****6201',
      amount: input.totalAmount,
      price: (Number(input.totalAmount) / Math.max(firstItem.quantity, 1)).toFixed(2)
    })
  }

  function payOrder(id: number) {
    const order = findOrder(id)
    order.status = '待取餐'
    order.timeline = ['订单已创建', '支付成功', '摊主已接单', '待取餐']
  }

  function completeOrder(id: number) {
    const order = findOrder(id)
    order.status = '已完成'
    order.timeline = [...new Set([...order.timeline, '已取餐', '已完成'])]
  }

  function cancelOrder(id: number) {
    const order = findOrder(id)
    order.status = '已取消'
    order.timeline = [...new Set([...order.timeline, '已取消'])]
  }

  function refundOrder(id: number) {
    const order = findOrder(id)
    order.status = '退款中'
    order.timeline = [...new Set([...order.timeline, '退款申请已提交'])]
  }

  function addComplaint(input: { target: string; type: string; description: string }) {
    const complaint: UserComplaint = {
      id: nextId(state.complaints),
      target: input.target,
      type: input.type,
      status: '处理中',
      updatedAt: nowLabel(),
      description: input.description || '用户已提交问题描述，等待平台核实。'
    }
    state.complaints.unshift(complaint)
    return complaint
  }

  function addReview(input: { orderId: number; rating: number; content: string }) {
    const order = findOrder(input.orderId)
    const review: UserReview = {
      id: nextId(state.reviews),
      orderId: order.id,
      stallId: order.stallId,
      stall: order.stall,
      rating: input.rating,
      content: input.content || '整体体验不错，取餐流程顺畅。',
      time: nowLabel(),
      status: '已发布'
    }
    state.reviews.unshift(review)
    completeOrder(order.id)
    return review
  }

  function hasFavorite(type: UserFavorite['type'], refId: number, name?: string) {
    return state.favorites.some((item) => item.type === type && (item.refId === refId || item.name === name))
  }

  function toggleStallFavorite(stallId: number) {
    const stall = stalls.find((item) => item.id === stallId) ?? stalls[0]
    const index = state.favorites.findIndex((item) => item.type === '摊位' && item.refId === stall.id)
    if (index >= 0) {
      state.favorites.splice(index, 1)
      return false
    }
    state.favorites.unshift({
      id: nextId(state.favorites),
      refId: stall.id,
      type: '摊位',
      name: stall.name,
      desc: `${stall.category} · ${stall.distance}`,
      status: '已收藏'
    })
    return true
  }

  function removeFavorite(id: number) {
    const index = state.favorites.findIndex((item) => item.id === id)
    if (index >= 0) {
      state.favorites.splice(index, 1)
    }
  }

  function toggleSubscription(stallId: number) {
    const stall = stalls.find((item) => item.id === stallId) ?? stalls[0]
    const index = state.subscriptions.findIndex((item) => item.stallId === stall.id)
    if (index >= 0) {
      state.subscriptions.splice(index, 1)
      return false
    }
    state.subscriptions.unshift({
      id: nextId(state.subscriptions),
      stallId: stall.id,
      stall: stall.name,
      time: '每天 17:30',
      channel: '站内信',
      status: '已开启'
    })
    return true
  }

  function toggleSubscriptionStatus(id: number) {
    const item = state.subscriptions.find((subscription) => subscription.id === id)
    if (item) {
      item.status = item.status === '已开启' ? '已暂停' : '已开启'
    }
  }

  return {
    orders,
    complaints,
    favorites,
    subscriptions,
    reviews,
    payments: paymentRecords,
    findOrder,
    createOrder,
    createOrderFromAgent,
    payOrder,
    completeOrder,
    cancelOrder,
    refundOrder,
    addComplaint,
    addReview,
    hasFavorite,
    toggleStallFavorite,
    removeFavorite,
    toggleSubscription,
    toggleSubscriptionStatus
  }
}
