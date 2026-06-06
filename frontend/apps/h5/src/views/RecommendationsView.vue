<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">智能推荐</p>
        <h1 class="page-title">适合你的附近摊位</h1>
      </div>
      <RouterLink class="ghost-pill" to="/">返回首页</RouterLink>
    </header>

    <section class="hero-card">
      <p class="eyebrow">规则推荐 · 冷启动版</p>
      <h1>距离、评分、品类热度混合排序</h1>
      <p>先按说明书中的规则公式展示推荐结果，后续接推荐日志、曝光点击和协同过滤服务。</p>
    </section>

    <section class="section content-grid">
      <div class="list-stack">
        <RouterLink v-for="item in recommendationCards" :key="item.stallId" class="list-card" :to="`/stalls/${item.stallId}`">
          <div class="list-card-header">
            <div>
              <h3>{{ item.name }}</h3>
              <p>{{ item.reason }}</p>
            </div>
            <span class="status-tag">匹配 {{ item.score }}</span>
          </div>
          <div class="meta-row">
            <span>预计等待 {{ item.waitTime }}</span>
            <span>曝光记录 mock</span>
          </div>
          <div class="chip-row">
            <span v-for="tag in item.tags" :key="tag" class="chip">{{ tag }}</span>
          </div>
        </RouterLink>
      </div>

      <aside class="card">
        <h2>推荐依据</h2>
        <div class="list-stack">
          <div class="list-card"><h3>距离权重 0.4</h3><p>优先展示附近营业摊位。</p></div>
          <div class="list-card"><h3>评分权重 0.3</h3><p>综合口碑和多维评价得分。</p></div>
          <div class="list-card"><h3>品类热度 0.3</h3><p>结合时段、天气和历史订单。</p></div>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { recommendationCards } from '../data/mock'
</script>
