<template>
  <main class="page vendor-me">
    <section class="screen-top screen-top-red vendor-top">
      <div class="status-bar">
        <span>12:00</span>
        <span class="status-icons">▮▮▮ wifi ▰</span>
      </div>
      <div class="vendor-top-actions">
        <RouterLink to="/messages"><van-icon name="chat-o" /></RouterLink>
        <strong>我的</strong>
        <RouterLink to="/vendor/settings"><van-icon name="setting-o" /></RouterLink>
      </div>
      <RouterLink to="/vendor/profile" class="vendor-shop-card">
        <div class="vendor-logo"></div>
        <div>
          <h1>默林的广东肠粉早餐店</h1>
          <p>账号：+8613612345678</p>
        </div>
        <van-icon name="arrow" />
      </RouterLink>
    </section>

    <section class="vendor-list">
      <RouterLink v-for="item in primaryItems" :key="item.path" :to="item.path" class="vendor-list-item">
        <span :class="['vendor-list-icon', item.tone]"><van-icon :name="item.icon" /></span>
        <strong>{{ item.label }}</strong>
        <em v-if="item.badge">{{ item.badge }}</em>
        <van-icon name="arrow" />
      </RouterLink>
    </section>

    <section class="vendor-list">
      <RouterLink v-for="item in secondaryItems" :key="item.path" :to="item.path" class="vendor-list-item">
        <span :class="['vendor-list-icon', item.tone]"><van-icon :name="item.icon" /></span>
        <strong>{{ item.label }}</strong>
        <em v-if="item.badge">{{ item.badge }}</em>
        <van-icon name="arrow" />
      </RouterLink>
    </section>

    <section class="vendor-list">
      <RouterLink to="/vendor/help" class="vendor-list-item">
        <span class="vendor-list-icon blue"><van-icon name="service-o" /></span>
        <strong>帮助中心</strong>
        <van-icon name="arrow" />
      </RouterLink>
    </section>
  </main>
</template>

<script setup lang="ts">
interface VendorEntry {
  icon: string
  label: string
  path: string
  tone: string
  badge?: string
}

const primaryItems: VendorEntry[] = [
  { icon: 'volume-o', label: '店铺公告', path: '/vendor/profile', tone: 'red' },
  { icon: 'shop-o', label: '店铺管理', path: '/vendor/profile', tone: 'orange' },
  { icon: 'todo-list-o', label: '店铺店招', path: '/vendor/stories', tone: 'orange', badge: '审核中' },
  { icon: 'photo-o', label: '门店环境', path: '/vendor/qualifications', tone: 'yellow' },
  { icon: 'completed-o', label: '到店自取', path: '/vendor/stalls', tone: 'green' }
]

const secondaryItems: VendorEntry[] = [
  { icon: 'certificate', label: '资质信息', path: '/vendor/qualifications', tone: 'pink' },
  { icon: 'balance-o', label: '账户信息', path: '/vendor/settings', tone: 'cyan' },
  { icon: 'fire-o', label: '营销活动', path: '/vendor/activities', tone: 'red' },
  { icon: 'share-o', label: '分享推广', path: '/vendor/share', tone: 'purple' }
]
</script>
