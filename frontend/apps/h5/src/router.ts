import { createRouter, createWebHistory } from 'vue-router'
import { getAuthSession } from '@yuntanfang/shared'
import HomeView from './views/HomeView.vue'
import LoginView from './views/LoginView.vue'
import AgentOrderView from './views/AgentOrderView.vue'
import ComplaintCreateView from './views/ComplaintCreateView.vue'
import ComplaintsView from './views/ComplaintsView.vue'
import FavoritesView from './views/FavoritesView.vue'
import OrderDetailView from './views/OrderDetailView.vue'
import OrdersView from './views/OrdersView.vue'
import ReservationCreateView from './views/ReservationCreateView.vue'
import RecommendationsView from './views/RecommendationsView.vue'
import ReviewCreateView from './views/ReviewCreateView.vue'
import StallDetailView from './views/StallDetailView.vue'
import StallsView from './views/StallsView.vue'
import StoriesFeedView from './views/StoriesFeedView.vue'
import SubscriptionsView from './views/SubscriptionsView.vue'
import UserProfileView from './views/UserProfileView.vue'
import UserUtilityView from './views/UserUtilityView.vue'
import VendorApplyView from './views/VendorApplyView.vue'
import VendorDashboardView from './views/VendorDashboardView.vue'
import VendorLocationRecommendationsView from './views/VendorLocationRecommendationsView.vue'
import VendorOrderDetailView from './views/VendorOrderDetailView.vue'
import VendorOrdersView from './views/VendorOrdersView.vue'
import VendorProductsView from './views/VendorProductsView.vue'
import VendorQualificationsView from './views/VendorQualificationsView.vue'
import VendorReviewsView from './views/VendorReviewsView.vue'
import VendorStallsView from './views/VendorStallsView.vue'
import VendorStoriesView from './views/VendorStoriesView.vue'
import VendorStoryCreateView from './views/VendorStoryCreateView.vue'
import VendorUtilityView from './views/VendorUtilityView.vue'

const routes = [
  { path: '/', component: HomeView, meta: { title: '首页', nav: 'home' } },
  { path: '/login', component: LoginView, meta: { title: '登录/注册', module: '账号', nav: 'profile' } },
  { path: '/recommendations', component: RecommendationsView, meta: { title: '智能推荐', module: '消费者', nav: 'home' } },
  { path: '/agent-order', component: AgentOrderView, meta: { title: '系统 Agent', module: '消费者', nav: 'agent' } },
  { path: '/stories', component: StoriesFeedView, meta: { title: '摊主故事', module: '消费者', nav: 'home' } },
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
  { path: '/messages', component: UserUtilityView, meta: { title: '消息中心', module: '消费者', nav: 'profile' } },
  { path: '/help', component: UserUtilityView, meta: { title: '帮助与客服', module: '消费者', nav: 'profile' } },
  { path: '/feedback', component: UserUtilityView, meta: { title: '意见反馈', module: '消费者', nav: 'profile' } },
  { path: '/footprints', component: UserUtilityView, meta: { title: '我的足迹', module: '消费者', nav: 'profile' } },
  { path: '/my-reviews', component: UserUtilityView, meta: { title: '我的评价', module: '消费者', nav: 'profile' } },
  { path: '/wallet', component: UserUtilityView, meta: { title: '我的钱包', module: '消费者', nav: 'profile' } },
  { path: '/settings', component: UserUtilityView, meta: { title: '设置', module: '消费者', nav: 'profile' } },
  { path: '/profile', component: UserProfileView, meta: { title: '个人中心', module: '消费者', nav: 'profile' } },
  { path: '/vendor/apply', component: VendorApplyView, meta: { title: '摊主入驻申请', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/dashboard', component: VendorDashboardView, meta: { title: '摊主工作台', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/location-recommendations', component: VendorLocationRecommendationsView, meta: { title: '选址推荐', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/qualifications', component: VendorQualificationsView, meta: { title: '资质管理', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/stalls', component: VendorStallsView, meta: { title: '摊位预约与出摊管理', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/products', component: VendorProductsView, meta: { title: '商品管理', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/orders', component: VendorOrdersView, meta: { title: '订单处理', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/orders/:id', component: VendorOrderDetailView, meta: { title: '商家订单详情', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/reviews', component: VendorReviewsView, meta: { title: '评价回复', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/stories', component: VendorStoriesView, meta: { title: '摊主故事', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/stories/create', component: VendorStoryCreateView, meta: { title: '发布摊主故事', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/profile', component: VendorUtilityView, meta: { title: '店铺信息', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/activities', component: VendorUtilityView, meta: { title: '营销活动', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/share', component: VendorUtilityView, meta: { title: '分享推广', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/settings', component: VendorUtilityView, meta: { title: '商家设置', module: '商家端', nav: 'vendor' } },
  { path: '/vendor/help', component: VendorUtilityView, meta: { title: '商家帮助中心', module: '商家端', nav: 'vendor' } }
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const session = getAuthSession()

  if (!session && to.path !== '/login') {
    return { path: '/login', query: { redirect: to.fullPath } }
  }

  if (session && to.path === '/login') {
    return session.role === 'vendor' ? '/vendor/dashboard' : '/'
  }

  if (to.path.startsWith('/vendor') && session?.role !== 'vendor') {
    return { path: '/', replace: true }
  }

  return true
})
