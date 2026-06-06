<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">摊位搜索</p>
        <h1 class="page-title">附近摊位</h1>
      </div>
      <RouterLink class="ghost-pill" to="/">返回首页</RouterLink>
    </header>

    <van-search v-model="keyword" shape="round" placeholder="摊位、商品、位置" />

    <section class="section">
      <div class="chip-row">
        <button v-for="item in categories" :key="item" class="chip" :class="{ active: selected === item }" @click="selected = item">
          {{ item }}
        </button>
      </div>
    </section>

    <section class="section content-grid">
      <div class="list-stack">
        <RouterLink v-for="stall in filteredStalls" :key="stall.id" class="list-card" :to="`/stalls/${stall.id}`">
          <div class="list-card-header">
            <div>
              <h3>{{ stall.name }}</h3>
              <p>{{ stall.address }}</p>
            </div>
            <span class="status-tag">{{ stall.status }}</span>
          </div>
          <div class="meta-row">
            <span>{{ stall.vendor }}</span>
            <span>{{ stall.category }}</span>
            <span>{{ stall.distance }}</span>
            <span>评分 {{ stall.rating }}</span>
          </div>
          <div class="chip-row">
            <span v-for="product in stall.products" :key="product" class="chip">{{ product }}</span>
          </div>
        </RouterLink>
      </div>

      <aside class="card desktop-only">
        <h2>地图导航 mock</h2>
        <p class="muted">后续接入地图 Web API。当前先保留定位、距离、路线规划入口。</p>
        <button class="primary-pill">使用当前位置</button>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { categories, stalls } from '../data/mock'

const route = useRoute()
const keyword = ref(String(route.query.keyword ?? ''))
const selected = ref('')

const filteredStalls = computed(() => {
  return stalls.filter((stall) => {
    const keywordMatched = !keyword.value || [stall.name, stall.vendor, stall.category, ...stall.products].join('').includes(keyword.value)
    const categoryMatched = !selected.value || stall.category === selected.value
    return keywordMatched && categoryMatched
  })
})
</script>
