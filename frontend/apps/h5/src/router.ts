import { createRouter, createWebHistory } from 'vue-router'
import HomeView from './views/HomeView.vue'
import SimpleView from './views/SimpleView.vue'
import CartView from './views/CartView.vue'
import CheckoutView from './views/CheckoutView.vue'
import MapView from './views/MapView.vue'
import MessageCenterView from './views/MessageCenterView.vue'
import OrderDetailView from './views/OrderDetailView.vue'
import OrdersView from './views/OrdersView.vue'
import ProductDetailView from './views/ProductDetailView.vue'
import ProfileView from './views/ProfileView.vue'
import ReviewCreateView from './views/ReviewCreateView.vue'
import StallDetailView from './views/StallDetailView.vue'
import StallListView from './views/StallListView.vue'

const routes = [
  { path: '/', component: HomeView, meta: { title: '首页' } },
  { path: '/login', component: SimpleView, meta: { title: '登录/注册', module: '账号' } },
  { path: '/stalls', component: StallListView, meta: { title: '摊位列表与筛选', module: '消费者' } },
  { path: '/stalls/:id', component: StallDetailView, meta: { title: '摊位详情', module: '消费者' } },
  { path: '/stalls/:id/cart', component: CartView, meta: { title: '店铺购物车', module: '消费者' } },
  { path: '/products/:id', component: ProductDetailView, meta: { title: '商品详情', module: '消费者' } },
  { path: '/cart', component: CartView, meta: { title: '购物车', module: '消费者' } },
  { path: '/checkout', component: CheckoutView, meta: { title: '提交订单', module: '消费者' } },
  { path: '/orders', component: OrdersView, meta: { title: '我的订单', module: '消费者' } },
  { path: '/orders/:id', component: OrderDetailView, meta: { title: '订单详情', module: '消费者' } },
  { path: '/reviews/create/:orderId', component: ReviewCreateView, meta: { title: '发布评价', module: '消费者' } },
  { path: '/profile', component: ProfileView, meta: { title: '个人中心', module: '消费者' } },
  { path: '/messages', component: MessageCenterView, meta: { title: '消息中心', module: '消费者' } },
  { path: '/map', component: MapView, meta: { title: '地图页面', module: '消费者' } },
  { path: '/complaints', component: SimpleView, meta: { title: '我的投诉', module: '消费者' } },
  { path: '/complaints/create', component: SimpleView, meta: { title: '提交投诉', module: '消费者' } },
  { path: '/vendor/:pathMatch(.*)*', redirect: '/login' }
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})
