<template>
  <main class="page">
    <section class="hero-card">
      <p class="eyebrow">云摊坊 · 附近烟火气</p>
      <h1>找摊位、看商品、预约取餐，一站完成</h1>
      <p>面向市民消费者和本地摊主，聚合附近摊位、今日推荐、公益专区和合规经营信息。</p>
      <div class="hero-actions">
        <RouterLink class="primary-pill" to="/stalls">查找摊位</RouterLink>
        <RouterLink class="ghost-pill" to="/vendor/apply">摊主入驻</RouterLink>
      </div>
    </section>

    <section class="section">
      <van-search v-model="keyword" shape="round" placeholder="搜索摊位、商品、摊主" @search="goSearch" />
    </section>

    <section class="section">
      <div class="section-head">
        <h2>特色专区</h2>
        <RouterLink class="muted" to="/stalls">全部</RouterLink>
      </div>
      <div class="zone-grid">
        <RouterLink v-for="zone in featureZones" :key="zone.title" :class="['zone-card', `zone-${zone.tone}`]" to="/stalls">
          <strong>{{ zone.title }}</strong>
          <span>{{ zone.desc }}</span>
        </RouterLink>
      </div>
    </section>

    <section class="section">
      <div class="section-head">
        <h2>分类筛选</h2>
      </div>
      <div class="chip-row">
        <RouterLink v-for="item in categories" :key="item" class="chip" to="/stalls">{{ item }}</RouterLink>
      </div>
    </section>

    <section class="section content-grid">
      <div>
        <div class="section-head">
          <h2>附近摊位</h2>
          <RouterLink class="muted" to="/stalls">查看更多</RouterLink>
        </div>
        <div class="list-stack">
          <RouterLink v-for="stall in stalls" :key="stall.id" class="list-card" :to="`/stalls/${stall.id}`">
            <div class="list-card-header">
              <div>
                <h3>{{ stall.name }}</h3>
                <p>{{ stall.story }}</p>
              </div>
              <span class="status-tag">{{ stall.status }}</span>
            </div>
            <div class="meta-row">
              <span>{{ stall.category }}</span>
              <span>{{ stall.distance }}</span>
              <span>评分 {{ stall.rating }}</span>
            </div>
          </RouterLink>
        </div>
      </div>

      <aside class="card desktop-only">
        <div class="section-head">
          <h2>今日经营概览</h2>
        </div>
        <div class="metric-grid">
          <div class="metric-card">
            <strong>36</strong>
            <span>预约订单</span>
          </div>
          <div class="metric-card">
            <strong>12</strong>
            <span>出摊摊位</span>
          </div>
          <div class="metric-card">
            <strong>4.8</strong>
            <span>平均评分</span>
          </div>
          <div class="metric-card">
            <strong>3</strong>
            <span>待处理投诉</span>
          </div>
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

function goSearch() {
  router.push({ path: '/stalls', query: { keyword: keyword.value } })
}
</script>
