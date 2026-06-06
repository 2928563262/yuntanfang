<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">用户评价</p>
        <h1 class="page-title">评价回复</h1>
      </div>
      <button class="ghost-pill">快捷短语</button>
    </header>

    <section class="section content-grid">
      <div class="list-stack">
        <article v-for="review in vendorReviews" :key="review.user" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ review.user }}</h3>
              <p>{{ review.content }}</p>
            </div>
            <span class="status-tag">{{ review.status }}</span>
          </div>
          <div class="meta-row">
            <span>评分 {{ review.rating }} 星</span>
            <span>图文/视频评价占位</span>
          </div>
          <textarea placeholder="回复用户评价"></textarea>
        </article>
      </div>

      <aside class="card">
        <h2>评分概览</h2>
        <div class="metric-grid">
          <div class="metric-card"><strong>4.9</strong><span>卫生</span></div>
          <div class="metric-card"><strong>4.8</strong><span>服务</span></div>
          <div class="metric-card"><strong>4.7</strong><span>品质</span></div>
          <div class="metric-card"><strong>4.8</strong><span>性价比</span></div>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { vendorReviews } from '../data/mock'
</script>
