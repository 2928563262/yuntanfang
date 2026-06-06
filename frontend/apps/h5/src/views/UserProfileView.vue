<template>
  <main class="page proto-page">
    <section class="screen-top profile-top">
      <div class="status-bar">
        <span>12:00</span>
        <span class="status-icons">▮▮▮ wifi ▰</span>
      </div>
      <div class="profile-gear">
        <RouterLink to="/settings"><van-icon name="setting-o" /></RouterLink>
      </div>
      <div class="profile-user">
        <div class="avatar"></div>
        <RouterLink to="/settings" class="profile-name">
          <strong>用户名</strong>
          <van-icon name="arrow" />
        </RouterLink>
      </div>
    </section>

    <section class="wallet-panel">
      <RouterLink v-for="item in walletItems" :key="item.path" :to="item.path" class="wallet-item">
        <van-icon :name="item.icon" />
        <strong>{{ item.value }}</strong>
        <span>{{ item.label }}</span>
      </RouterLink>
    </section>

    <section class="proto-card">
      <h2>功能服务</h2>
      <div class="service-grid">
        <RouterLink v-for="item in services" :key="item.path" :to="item.path" class="service-item">
          <span :class="['service-icon', item.tone]"><van-icon :name="item.icon" /></span>
          <strong>{{ item.label }}</strong>
        </RouterLink>
      </div>
    </section>

    <section class="proto-card">
      <h2>更多推荐</h2>
      <div class="service-grid mini">
        <RouterLink v-for="item in recommends" :key="item.path" :to="item.path" class="service-item">
          <span :class="['service-icon', item.tone]"><van-icon :name="item.icon" /></span>
          <strong>{{ item.label }}</strong>
        </RouterLink>
      </div>
    </section>
  </main>
</template>

<script setup lang="ts">
const walletItems = [
  { icon: 'balance-o', value: '', label: '我的钱包', path: '/wallet' },
  { icon: 'coupon-o', value: '3', label: '红包', path: '/wallet' },
  { icon: 'coupon', value: '5', label: '代金券', path: '/wallet' },
  { icon: 'bill-o', value: '', label: '支付账单', path: '/wallet' }
]

const services = [
  { icon: 'like', label: '我的收藏', path: '/favorites', tone: 'hot' },
  { icon: 'footprint', label: '我的足迹', path: '/footprints', tone: 'green' },
  { icon: 'edit', label: '我的评价', path: '/my-reviews', tone: 'pink' },
  { icon: 'service-o', label: '帮助与客服', path: '/help', tone: 'blue' },
  { icon: 'coupon', label: '发票助手', path: '/wallet', tone: 'orange' },
  { icon: 'chat-o', label: '消息中心', path: '/messages', tone: 'blue' },
  { icon: 'warning-o', label: '我的投诉', path: '/complaints', tone: 'pink' },
  { icon: 'setting-o', label: '设置', path: '/settings', tone: 'gray' }
]

const recommends = [
  { icon: 'shop-o', label: '商家开店', path: '/vendor/apply', tone: 'purple' },
  { icon: 'guide-o', label: '同城跑腿', path: '/feedback', tone: 'orange' }
]
</script>
