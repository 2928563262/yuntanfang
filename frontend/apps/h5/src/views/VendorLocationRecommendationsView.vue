<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">选址推荐</p>
        <h1 class="page-title">人流、竞争与空位建议</h1>
      </div>
      <RouterLink class="ghost-pill" to="/vendor/stalls">出摊管理</RouterLink>
    </header>

    <section class="section content-grid">
      <div class="list-stack">
        <article v-for="item in locationRecommendations" :key="item.area" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ item.area }}</h3>
              <p>{{ item.reason }}</p>
            </div>
            <span class="status-tag">人流 {{ item.trafficScore }}</span>
          </div>
          <div class="metric-grid">
            <div class="metric-card"><strong>{{ item.competition }}</strong><span>竞争指数</span></div>
            <div class="metric-card"><strong>{{ item.sameCategory }}</strong><span>同品类摊位</span></div>
            <div class="metric-card"><strong>{{ item.available }}</strong><span>可用点位</span></div>
            <div class="metric-card"><strong>{{ Math.round(item.trafficScore * 0.5 + item.available * 4) }}</strong><span>推荐分</span></div>
          </div>
        </article>
      </div>

      <aside class="card">
        <h2>地图视图 mock</h2>
        <div class="market-map vendor-map">
          <span class="market-pin pin-one">A区</span>
          <span class="market-pin pin-two">B区</span>
          <span class="market-pin pin-three">C区</span>
        </div>
        <p class="muted">后续接政务区域围栏、时段、品类限制和预约审批。</p>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { locationRecommendations } from '../data/mock'
</script>
