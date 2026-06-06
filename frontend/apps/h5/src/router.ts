import { createRouter, createWebHistory } from 'vue-router'
import HomeView from './views/HomeView.vue'
import LoginView from './views/LoginView.vue'
import ComplaintCreateView from './views/ComplaintCreateView.vue'
import ComplaintsView from './views/ComplaintsView.vue'
import FavoritesView from './views/FavoritesView.vue'
import OrderDetailView from './views/OrderDetailView.vue'
import OrdersView from './views/OrdersView.vue'
import ReservationCreateView from './views/ReservationCreateView.vue'
import ReviewCreateView from './views/ReviewCreateView.vue'
import SimpleView from './views/SimpleView.vue'
import StallDetailView from './views/StallDetailView.vue'
import StallsView from './views/StallsView.vue'
import SubscriptionsView from './views/SubscriptionsView.vue'
import UserProfileView from './views/UserProfileView.vue'
import VendorApplyView from './views/VendorApplyView.vue'
import VendorDashboardView from './views/VendorDashboardView.vue'
import VendorOrdersView from './views/VendorOrdersView.vue'
import VendorProductsView from './views/VendorProductsView.vue'
import VendorQualificationsView from './views/VendorQualificationsView.vue'
import VendorReviewsView from './views/VendorReviewsView.vue'
import VendorStallsView from './views/VendorStallsView.vue'
import VendorStoriesView from './views/VendorStoriesView.vue'

const routes = [
  { path: '/', component: HomeView, meta: { title: '首页', nav: 'home' } },
  { path: '/login', component: LoginView, meta: { title: '登录/注册', module: '账号', nav: 'profile' } },
  { path: '/stalls', component: StallsView, meta: { title: '摊位列表与筛选', module: '消费者', nav: 'stalls' } },
  { path: '/stalls/:id', component: StallDetailView, meta: { title: '摊位详情', module: '消费者', nav: 'stalls' } },
  { path: '/stalls/:id/reserve', component: ReservationCreateView, meta: { title: '预约下单', module: '消费者', nav: 'stalls' } },
  { path: '/orders', component: OrdersView, meta: { title: '我的订单', module: '消费者', nav: 'orders' } },
  { path: '/orders/:id', component: OrderDetailView, meta: { title: '订单详情', module: '消费者', nav: 'orders' } },
  { path: '/reviews/create/:orderId', component: ReviewCreateView, meta: { title: '发布评价', module: '消费者', nav: 'orders' } },
  { path: '/complaints', component: ComplaintsView, meta: { title: '我的投诉', module: '消费者', nav: 'profile' } },
  { path: '/complaints/create', component: ComplaintCreateView, meta: { title: '提交投诉', module: '消费者', nav: 'profile' } },
  { path: '/favorites', component: FavoritesView, meta: { title: '收藏关注', module: '消费者', nav: 'profile' } },
  { path: '/subscriptions', component: SubscriptionsView, meta: { title: '出摊提醒', module: '消费者', nav: 'profile' } },
  { path: '/profile', component: UserProfileView, meta: { title: '个人中心', module: '消费者', nav: 'profile' } },
  { path: '/vendor/apply', component: VendorApplyView, meta: { title: '摊主入驻申请', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/dashboard', component: VendorDashboardView, meta: { title: '摊主工作台', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/qualifications', component: VendorQualificationsView, meta: { title: '资质管理', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/stalls', component: VendorStallsView, meta: { title: '摊位预约与出摊管理', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/products', component: VendorProductsView, meta: { title: '商品管理', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/orders', component: VendorOrdersView, meta: { title: '订单处理', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/reviews', component: VendorReviewsView, meta: { title: '评价回复', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/stories', component: VendorStoriesView, meta: { title: '摊主故事', module: '商家端', nav: 'vendor' } }
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})
