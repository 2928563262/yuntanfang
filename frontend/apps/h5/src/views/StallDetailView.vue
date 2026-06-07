<template>
  <main class="page">
    <header class="page-header">
      <button class="ghost-pill" @click="$router.back()">返回</button>
      <RouterLink class="ghost-pill" to="/complaints/create">投诉</RouterLink>
    </header>

    <section class="hero-card">
      <p class="eyebrow">{{ stall.category }} · {{ stall.distance }}</p>
      <h1>{{ stall.name }}</h1>
      <p>{{ stall.story }}</p>
      <div class="hero-actions">
        <RouterLink class="primary-pill" :to="`/stalls/${stall.id}/reserve`">预约下单</RouterLink>
        <button class="ghost-pill">一键导航</button>
      </div>
    </section>

    <section class="section content-grid">
      <div class="list-stack">
        <article class="card">
          <div class="section-head">
            <h2>摊位信息</h2>
            <span class="status-tag">{{ stall.status }}</span>
          </div>
          <div class="meta-row">
            <span>摊主：{{ stall.vendor }}</span>
            <span>评分：{{ stall.rating }}</span>
            <span>{{ stall.address }}</span>
          </div>
          <div class="action-grid section">
            <button class="ghost-pill" type="button" @click="toggleFavorite">{{ isFavorite ? '取消收藏' : '收藏摊位' }}</button>
            <RouterLink class="ghost-pill" to="/favorites">我的收藏</RouterLink>
            <button class="ghost-pill" type="button" @click="toggleSubscription">{{ isSubscribed ? '取消提醒' : '订阅提醒' }}</button>
            <RouterLink class="ghost-pill" to="/stories">摊主故事</RouterLink>
          </div>
        </article>

        <article class="card">
          <div class="section-head">
            <h2>商品</h2>
            <RouterLink class="muted" to="/orders">查看订单</RouterLink>
          </div>
          <div class="list-stack">
            <div v-for="product in stall.products" :key="product" class="list-card">
              <div class="list-card-header">
                <div>
                  <h3>{{ product }}</h3>
                  <p>支持预约取货，后续接入库存、规格、媒资。</p>
                </div>
                <strong>¥12 起</strong>
              </div>
            </div>
          </div>
        </article>

        <article class="card">
          <div class="section-head">
            <h2>顾客评价</h2>
            <RouterLink class="muted" to="/my-reviews">我的评价</RouterLink>
          </div>
          <div class="list-stack">
            <div v-for="review in reviews" :key="`${review.id}-${review.time}`" class="list-card">
              <div class="list-card-header">
                <div>
                  <h3>{{ review.stall }}</h3>
                  <p>{{ review.content }}</p>
                </div>
                <span class="status-tag">{{ review.rating }} 星</span>
              </div>
              <div class="meta-row">
                <span>{{ review.time }}</span>
                <span>{{ review.status }}</span>
              </div>
            </div>
          </div>
        </article>
      </div>

      <aside class="card">
        <h2>评价概览</h2>
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
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { stalls } from '../data/mock'
import { useUserDataStore } from '../stores/userData'

const route = useRoute()
const userData = useUserDataStore()
const stall = computed(() => stalls.find((item) => item.id === Number(route.params.id)) ?? stalls[0])
const reviews = computed(() => userData.reviews.value.filter((item) => item.stallId === stall.value.id))
const isFavorite = computed(() => userData.hasFavorite('摊位', stall.value.id, stall.value.name))
const isSubscribed = computed(() => userData.subscriptions.value.some((item) => item.stallId === stall.value.id))

function toggleFavorite() {
  userData.toggleStallFavorite(stall.value.id)
}

function toggleSubscription() {
  userData.toggleSubscription(stall.value.id)
}
</script>
