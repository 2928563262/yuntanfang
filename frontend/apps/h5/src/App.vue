<template>
  <div class="app-shell">
    <aside class="desktop-nav" aria-label="主导航">
      <div class="desktop-brand">
        <span class="brand-mark">云</span>
        <div>
          <strong>云摊坊</strong>
          <small>智慧摊务平台</small>
        </div>
      </div>
      <RouterLink v-for="item in navItems" :key="item.to" :to="item.to" class="desktop-nav-item">
        <van-icon :name="item.icon" />
        <span>{{ item.label }}</span>
      </RouterLink>
    </aside>

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
  { to: '/profile', icon: 'user-o', label: '我的' }
]
</script>
