import { createRouter, createWebHistory } from 'vue-router'
import HomeView from './views/HomeView.vue'
import SimpleView from './views/SimpleView.vue'

const routes = [
  { path: '/', component: HomeView, meta: { title: '首页' } },
  { path: '/login', component: SimpleView, meta: { title: '登录/注册', module: '账号' } },
  { path: '/stalls', component: SimpleView, meta: { title: '摊位列表与筛选', module: '消费者' } },
  { path: '/stalls/:id', component: SimpleView, meta: { title: '摊位详情', module: '消费者' } },
  { path: '/orders', component: SimpleView, meta: { title: '我的订单', module: '消费者' } },
  { path: '/orders/:id', component: SimpleView, meta: { title: '订单详情', module: '消费者' } },
  { path: '/reviews/create/:orderId', component: SimpleView, meta: { title: '发布评价', module: '消费者' } },
  { path: '/complaints', component: SimpleView, meta: { title: '我的投诉', module: '消费者' } },
  { path: '/complaints/create', component: SimpleView, meta: { title: '提交投诉', module: '消费者' } },
  { path: '/profile', component: SimpleView, meta: { title: '个人中心', module: '消费者' } },
  { path: '/vendor/apply', component: SimpleView, meta: { title: '摊主入驻申请', module: '商家端' } },
  { path: '/vendor/dashboard', component: SimpleView, meta: { title: '摊主工作台', module: '商家端' } },
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
