<template>
  <div class="app-shell">
    <aside class="desktop-nav" aria-label="主导航">
      <div class="desktop-brand">
        <span class="brand-mark">云</span>
        <div>
          <strong>云摊坊</strong>
          <small>{{ session ? `${session.label} · ${session.username}` : '未登录' }}</small>
        </div>
      </div>
      <RouterLink v-for="item in navItems" :key="item.to" :to="item.to" class="desktop-nav-item">
        <van-icon :name="item.icon" />
        <span>{{ item.label }}</span>
      </RouterLink>
      <RouterLink v-if="!session" to="/login" class="desktop-nav-item desktop-auth-item">
        <van-icon name="contact" />
        <span>登录</span>
      </RouterLink>
      <button v-else class="desktop-nav-item desktop-auth-item" type="button" @click="logout">
        <van-icon name="revoke" />
        <span>退出</span>
      </button>
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
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { clearAuthSession, getAuthSession } from '@yuntanfang/shared'

const router = useRouter()
const session = ref(getAuthSession())

window.addEventListener('storage', () => {
  session.value = getAuthSession()
})

router.afterEach(() => {
  session.value = getAuthSession()
})

const navItems = computed(() => [
  { to: '/', icon: 'wap-home', label: '首页' },
  { to: '/stalls', icon: 'search', label: '摊位' },
  { to: '/orders', icon: 'orders-o', label: '订单' },
  ...(session.value?.role === 'vendor' ? [{ to: '/vendor/dashboard', icon: 'shop-o', label: '摊主' }] : []),
  { to: '/profile', icon: 'user-o', label: '我的' }
])

function logout() {
  clearAuthSession()
  session.value = null
  router.push('/login')
}
</script>
