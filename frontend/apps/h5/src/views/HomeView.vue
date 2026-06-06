<template>
  <main class="page home-page">
    <section class="home-hero" aria-labelledby="home-title">
      <div class="home-hero-copy">
        <p class="eyebrow">云摊坊 · 智慧摊务服务</p>
        <h1 id="home-title">找附近好摊，预约到点取</h1>
        <div class="hero-actions">
          <RouterLink class="primary-pill" to="/stalls">查找摊位</RouterLink>
          <RouterLink class="ghost-pill" to="/orders">我的订单</RouterLink>
        </div>
      </div>

      <div class="home-hero-visual" aria-label="附近摊位预览">
        <div class="market-ticket">
          <span>附近热摊</span>
          <strong>北站中心公园</strong>
          <small>18:00 后陆续出摊</small>
        </div>
        <div class="market-map">
          <span class="market-pin pin-one">汤粉</span>
          <span class="market-pin pin-two">鲜铺</span>
          <span class="market-pin pin-three">糖画</span>
        </div>
      </div>
    </section>

    <section class="home-search-panel" aria-label="搜索">
      <van-search v-model="keyword" shape="round" placeholder="搜索摊位、商品、位置" @search="goSearch" />
      <button class="home-location" type="button" @click="goSearch">附近 2km</button>
    </section>

    <section class="home-section">
      <div class="section-head">
        <h2>常用服务</h2>
      </div>
      <div class="home-action-grid">
        <RouterLink v-for="item in quickActions" :key="item.title" class="home-action-card" :to="item.to">
          <van-icon :name="item.icon" />
          <strong>{{ item.title }}</strong>
          <span>{{ item.desc }}</span>
        </RouterLink>
      </div>
    </section>

    <section class="home-section">
      <div class="section-head">
        <h2>特色专区</h2>
        <RouterLink class="muted" to="/stalls">全部</RouterLink>
      </div>
      <div class="home-zone-grid">
        <RouterLink v-for="zone in featureZones" :key="zone.title" :class="['home-zone-card', `zone-${zone.tone}`]" to="/stalls">
          <strong>{{ zone.title }}</strong>
          <span>{{ zone.desc }}</span>
        </RouterLink>
      </div>
    </section>

    <section class="home-section">
      <div class="section-head">
        <h2>分类筛选</h2>
      </div>
      <div class="chip-row">
        <button
          v-for="item in categoryOptions"
          :key="item"
          :class="['chip', { active: activeCategory === item }]"
          type="button"
          @click="activeCategory = item"
        >
          {{ item }}
        </button>
      </div>
    </section>

    <section class="home-section home-content-grid">
      <div>
        <div class="section-head">
          <h2>附近摊位</h2>
          <RouterLink class="muted" to="/stalls">查看更多</RouterLink>
        </div>
        <div class="home-stall-grid">
          <RouterLink v-for="stall in filteredStalls" :key="stall.id" class="home-stall-card" :to="`/stalls/${stall.id}`">
            <div class="home-stall-top">
              <div>
                <strong>{{ stall.name }}</strong>
                <span>{{ stall.vendor }} · {{ stall.category }}</span>
              </div>
              <em>{{ stall.status }}</em>
            </div>
            <p>{{ stall.story }}</p>
            <div class="home-product-row">
              <span v-for="product in stall.products" :key="product">{{ product }}</span>
            </div>
            <div class="meta-row">
              <span>{{ stall.distance }}</span>
              <span>{{ stall.address }}</span>
              <span>评分 {{ stall.rating }}</span>
            </div>
          </RouterLink>
          <article v-if="filteredStalls.length === 0" class="home-empty">
            <strong>当前分类暂无摊位</strong>
            <span>换个分类看看，或去全部摊位浏览附近好摊。</span>
          </article>
        </div>
      </div>

      <aside class="home-side-panel">
        <div class="section-head">
          <h2>平台提醒</h2>
        </div>
        <div class="home-notice-list">
          <RouterLink v-for="notice in notices" :key="notice.title" class="home-notice" :to="notice.to">
            <strong>{{ notice.title }}</strong>
            <span>{{ notice.desc }}</span>
          </RouterLink>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { categories, featureZones, stalls } from '../data/mock'

const router = useRouter()
const keyword = ref('')
const activeCategory = ref('全部')
const categoryOptions = ['全部', ...categories]

const filteredStalls = computed(() => {
  if (activeCategory.value === '全部') {
    return stalls
  }

  return stalls.filter((stall) => stall.category === activeCategory.value)
})

const quickActions = [
  { title: '附近摊位', desc: '按距离找摊', icon: 'location-o', to: '/stalls' },
  { title: '预约取餐', desc: '先下单后取', icon: 'todo-list-o', to: '/stalls/1/reserve' },
  { title: '我的订单', desc: '查看取餐状态', icon: 'orders-o', to: '/orders' },
  { title: '收藏关注', desc: '常逛摊位', icon: 'star-o', to: '/favorites' },
  { title: '提交投诉', desc: '问题留痕', icon: 'warning-o', to: '/complaints/create' },
  { title: '消息中心', desc: '提醒通知', icon: 'chat-o', to: '/messages' }
]

const notices = [
  { title: '营业提醒', desc: '烟火小摊 17:30 北站中心公园东门出摊', to: '/messages' },
  { title: '取餐提示', desc: '预约成功后可在订单里查看取餐时间和摊位地址', to: '/orders' },
  { title: '公益专区', desc: '微光创业者与助农摊位优先展示', to: '/stalls' }
]

function goSearch() {
  router.push({ path: '/stalls', query: { keyword: keyword.value } })
}
</script>
