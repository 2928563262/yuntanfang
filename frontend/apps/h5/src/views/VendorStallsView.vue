<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">摊位预约与出摊</p>
        <h1 class="page-title">出摊管理</h1>
      </div>
      <button class="primary-pill">经营打卡</button>
    </header>

    <section class="section metric-grid">
      <div class="card metric-card"><strong>2</strong><span>本周预约</span></div>
      <div class="card metric-card"><strong>1</strong><span>待审批</span></div>
      <div class="card metric-card"><strong>96%</strong><span>准时出摊</span></div>
      <div class="card metric-card"><strong>0</strong><span>违规记录</span></div>
    </section>

    <section class="section content-grid">
      <div class="list-stack">
        <article v-for="item in vendorReservations" :key="`${item.area}-${item.date}`" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ item.area }}</h3>
              <p>{{ item.date }} · {{ item.period }}</p>
            </div>
            <span class="status-tag">{{ item.status }}</span>
          </div>
          <div class="meta-row">
            <span>地图围栏占位</span>
            <span>经营品类限制占位</span>
          </div>
        </article>
      </div>

      <aside class="card">
        <h2>营业状态</h2>
        <div class="action-grid">
          <button class="ghost-pill">临时休摊</button>
          <button class="ghost-pill">改点</button>
          <button class="ghost-pill">挪位</button>
          <button class="primary-pill">发布日历</button>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { vendorReservations } from '../data/mock'
</script>
