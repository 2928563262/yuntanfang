<template>
  <div class="app-shell">
    <section class="phone-frame">
      <RouterView />
      <van-tabbar route safe-area-inset-bottom>
        <van-tabbar-item v-for="item in navItems" :key="item.to" :to="item.to" :icon="item.icon">
          {{ item.label }}
        </van-tabbar-item>
      </van-tabbar>
    </section>
  </div>
</template>

<script setup lang="ts">
const navItems = [
  { to: '/', icon: 'wap-home', label: '首页' },
  { to: '/stalls', icon: 'search', label: '摊位' },
  { to: '/orders', icon: 'orders-o', label: '订单' },
  { to: '/vendor/dashboard', icon: 'shop-o', label: '摊主' },
  { to: '/profile', icon: 'user-o', label: '我的' }
]
</script>
