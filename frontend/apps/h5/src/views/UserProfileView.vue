<template>
  <main class="page profile-page">
    <section class="hero-card">
      <p class="eyebrow">个人中心</p>
      <h1>{{ session?.username ?? '用户名' }}</h1>
      <p>{{ session?.label ?? '普通用户' }} · 收藏、足迹、评价、钱包和投诉进度集中管理。</p>
    </section>

    <section class="section metric-grid">
      <RouterLink v-for="metric in metrics" :key="metric.path" class="card metric-card" :to="metric.path">
        <strong>{{ metric.value }}</strong>
        <span>{{ metric.label }}</span>
      </RouterLink>
    </section>

    <section class="section action-grid">
      <RouterLink v-for="item in actions" :key="item.path" class="list-card" :to="item.path">
        <h3>{{ item.title }}</h3>
        <p>{{ item.desc }}</p>
      </RouterLink>
    </section>

    <section class="section">
      <button class="list-card logout-card" type="button" @click="logout">
        <h3>退出登录</h3>
      </button>
    </section>
  </main>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { clearAuthSession, getAuthSession } from '@yuntanfang/shared'

const router = useRouter()
const session = getAuthSession()

const metrics = [
  { value: 8, label: '我的收藏', path: '/favorites' },
  { value: 12, label: '浏览足迹', path: '/footprints' },
  { value: 5, label: '我的评价', path: '/my-reviews' },
  { value: 2, label: '投诉进度', path: '/complaints' }
]

const actions = [
  { title: '我的订单', path: '/orders', desc: '查看预约、取餐和退款状态' },
  { title: '智能点单', path: '/agent-order', desc: '用一句话生成预约订单' },
  { title: '摊主故事', path: '/stories', desc: '浏览关注摊主和附近热门动态' },
  { title: '我的钱包', path: '/wallet', desc: '支付账单、红包、代金券、发票' },
  { title: '收藏关注', path: '/favorites', desc: '管理收藏摊位、商品和关注摊主' },
  { title: '出摊提醒', path: '/subscriptions', desc: '订阅摊主出摊和营业提醒' },
  { title: '我的足迹', path: '/footprints', desc: '最近浏览摊位和商品' },
  { title: '我的评价', path: '/my-reviews', desc: '已发布评价和待评价订单' },
  { title: '消息中心', path: '/messages', desc: '系统通知、站内信和提醒' },
  { title: '我的投诉', path: '/complaints', desc: '提交投诉并跟踪处理进度' },
  { title: '帮助与客服', path: '/help', desc: '在线客服、意见反馈、常见问题' },
  { title: '意见反馈', path: '/feedback', desc: '提交产品建议和问题反馈' },
  { title: '设置', path: '/settings', desc: '账号与安全、隐私管理' }
]

function logout() {
  clearAuthSession()
  router.push('/login')
}
</script>
