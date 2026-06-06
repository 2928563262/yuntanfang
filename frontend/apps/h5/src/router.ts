import { createRouter, createWebHistory } from 'vue-router'
import HomeView from './views/HomeView.vue'
import OrdersView from './views/OrdersView.vue'
import SimpleView from './views/SimpleView.vue'
import StallDetailView from './views/StallDetailView.vue'
import StallsView from './views/StallsView.vue'
import UserProfileView from './views/UserProfileView.vue'
import VendorApplyView from './views/VendorApplyView.vue'
import VendorDashboardView from './views/VendorDashboardView.vue'

const routes = [
  { path: '/', component: HomeView, meta: { title: '首页', nav: 'home' } },
  { path: '/login', component: SimpleView, meta: { title: '登录/注册', module: '账号', nav: 'profile' } },
  { path: '/stalls', component: StallsView, meta: { title: '摊位列表与筛选', module: '消费者', nav: 'stalls' } },
  { path: '/stalls/:id', component: StallDetailView, meta: { title: '摊位详情', module: '消费者', nav: 'stalls' } },
  { path: '/orders', component: OrdersView, meta: { title: '我的订单', module: '消费者', nav: 'orders' } },
  { path: '/orders/:id', component: SimpleView, meta: { title: '订单详情', module: '消费者' } },
  { path: '/reviews/create/:orderId', component: SimpleView, meta: { title: '发布评价', module: '消费者' } },
  { path: '/complaints', component: SimpleView, meta: { title: '我的投诉', module: '消费者' } },
  { path: '/complaints/create', component: SimpleView, meta: { title: '提交投诉', module: '消费者' } },
  { path: '/profile', component: UserProfileView, meta: { title: '个人中心', module: '消费者', nav: 'profile' } },
  { path: '/vendor/apply', component: VendorApplyView, meta: { title: '摊主入驻申请', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/dashboard', component: VendorDashboardView, meta: { title: '摊主工作台', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/qualifications', component: SimpleView, meta: { title: '资质管理', module: '商家端' } },
  { path: '/vendor/stalls', component: SimpleView, meta: { title: '摊位预约与出摊管理', module: '商家端' } },
  { path: '/vendor/products', component: SimpleView, meta: { title: '商品管理', module: '商家端' } },
  { path: '/vendor/orders', component: SimpleView, meta: { title: '订单处理', module: '商家端' } },
  { path: '/vendor/reviews', component: SimpleView, meta: { title: '评价回复', module: '商家端' } },
  { path: '/vendor/stories', component: SimpleView, meta: { title: '摊主故事', module: '商家端' } }
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})
