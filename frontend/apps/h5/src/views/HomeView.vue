<template>
  <main class="page home-page">
    <section class="home-hero" aria-labelledby="home-title">
      <div class="home-hero-copy">
        <p class="eyebrow">云摊坊 · 智慧摊务服务</p>
        <h1 id="home-title">找附近好摊，预约到点取</h1>
        <p>
          聚合摊位位置、商品推荐、预约下单、投诉反馈和摊主入驻服务，让市民和摊主在同一平台完成日常摊务。
        </p>
        <div class="hero-actions">
          <RouterLink class="primary-pill" to="/stalls">查找摊位</RouterLink>
          <RouterLink class="ghost-pill" to="/vendor/apply">摊主入驻</RouterLink>
        </div>
      </div>

      <div class="home-hero-board" aria-label="今日概览">
        <div v-for="item in metrics" :key="item.label" class="home-metric">
          <strong>{{ item.value }}</strong>
          <span>{{ item.label }}</span>
        </div>
      </div>
    </section>

    <section class="home-search-panel" aria-label="搜索">
      <van-search v-model="keyword" shape="round" placeholder="搜索摊位、商品、摊主" @search="goSearch" />
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
        <RouterLink v-for="item in categories" :key="item" class="chip" to="/stalls">{{ item }}</RouterLink>
      </div>
    </section>

    <section class="home-section home-content-grid">
      <div>
        <div class="section-head">
          <h2>附近摊位</h2>
          <RouterLink class="muted" to="/stalls">查看更多</RouterLink>
        </div>
        <div class="home-stall-grid">
          <RouterLink v-for="stall in stalls" :key="stall.id" class="home-stall-card" :to="`/stalls/${stall.id}`">
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
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { categories, featureZones, stalls } from '../data/mock'

const router = useRouter()
const keyword = ref('')

const metrics = [
  { value: '36', label: '今日预约' },
  { value: '12', label: '出摊摊位' },
  { value: '4.8', label: '平均评分' },
  { value: '3', label: '待处理投诉' }
]

const quickActions = [
  { title: '附近摊位', desc: '按距离找摊', icon: 'location-o', to: '/stalls' },
  { title: '预约取餐', desc: '先下单后取', icon: 'todo-list-o', to: '/stalls/1/reserve' },
  { title: '我的订单', desc: '查看取餐状态', icon: 'orders-o', to: '/orders' },
  { title: '收藏关注', desc: '常逛摊位', icon: 'star-o', to: '/favorites' },
  { title: '提交投诉', desc: '问题留痕', icon: 'warning-o', to: '/complaints/create' },
  { title: '摊主服务', desc: '入驻经营', icon: 'shop-o', to: '/vendor/dashboard' }
]

const notices = [
  { title: '营业提醒', desc: '烟火小摊 17:30 北站中心公园东门出摊', to: '/messages' },
  { title: '合规提示', desc: '摊主入驻需补充身份、健康证和摊车照片', to: '/vendor/apply' },
  { title: '公益专区', desc: '微光创业者与助农摊位优先展示', to: '/stalls' }
]

function goSearch() {
  router.push({ path: '/stalls', query: { keyword: keyword.value } })
}
</script>
