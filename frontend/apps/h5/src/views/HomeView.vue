<template>
  <main class="page home-proto">
    <section class="screen-top home-top">
      <div class="status-bar">
        <span>12:00</span>
        <span class="status-icons">▮▮▮ wifi ▰</span>
      </div>
      <div class="home-location">
        <span>深圳北站中心公园</span>
        <van-icon name="cart-o" />
      </div>
      <van-search v-model="keyword" shape="round" placeholder="搜索摊位、商品、摊主" @search="goSearch" />
      <div class="home-tabs">
        <button class="active">推荐</button>
        <button>附近</button>
        <button>分类</button>
        <button>公益</button>
      </div>
    </section>

    <section class="quick-grid">
      <RouterLink v-for="item in quickEntries" :key="item.label" :to="item.path" class="quick-item">
        <span :class="['quick-icon', item.tone]"><van-icon :name="item.icon" /></span>
        <strong>{{ item.label }}</strong>
      </RouterLink>
    </section>

    <section class="section">
      <div class="section-title-row">
        <strong>今日推荐</strong>
        <RouterLink to="/stalls">更多</RouterLink>
      </div>
      <div class="mini-shop-row">
        <RouterLink v-for="stall in stalls" :key="stall.id" :to="`/stalls/${stall.id}`" class="mini-shop">
          <div class="food-thumb"></div>
          <strong>{{ stall.name }}</strong>
          <span>{{ stall.category }}</span>
        </RouterLink>
      </div>
    </section>

    <section class="section">
      <div class="section-title-row">
        <strong>附近摊位</strong>
        <RouterLink to="/stalls">筛选</RouterLink>
      </div>
      <div class="home-shop-list">
        <RouterLink v-for="stall in stalls" :key="stall.id" :to="`/stalls/${stall.id}`" class="home-shop-card">
          <div class="shop-img"></div>
          <div>
            <h3>{{ stall.name }}</h3>
            <p>{{ stall.story }}</p>
            <div class="meta-row">
              <span>{{ stall.distance }}</span>
              <span>{{ stall.rating }} 分</span>
              <span>{{ stall.status }}</span>
            </div>
          </div>
        </RouterLink>
      </div>
    </section>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { stalls } from '../data/mock'

const router = useRouter()
const keyword = ref('')

const quickEntries = [
  { icon: 'fire-o', label: '热卖小吃', path: '/stalls', tone: 'red' },
  { icon: 'gift-o', label: '公益摊位', path: '/stalls', tone: 'orange' },
  { icon: 'flower-o', label: '助农专区', path: '/stalls', tone: 'green' },
  { icon: 'shop-o', label: '商家入驻', path: '/vendor/apply', tone: 'pink' },
  { icon: 'medal-o', label: '非遗好物', path: '/stalls', tone: 'purple' },
  { icon: 'location-o', label: '附近地图', path: '/stalls', tone: 'blue' },
  { icon: 'coupon-o', label: '优惠活动', path: '/wallet', tone: 'orange' },
  { icon: 'chat-o', label: '消息提醒', path: '/messages', tone: 'green' }
]

function goSearch() {
  router.push({ path: '/stalls', query: { keyword: keyword.value } })
}
</script>
