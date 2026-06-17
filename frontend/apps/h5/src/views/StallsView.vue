<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">摊位搜索</p>
        <h1 class="page-title">附近摊位</h1>
      </div>
      <RouterLink class="ghost-pill" to="/">返回首页</RouterLink>
    </header>

    <van-search v-model="keyword" shape="round" placeholder="摊位、商品、位置" @search="load" />

    <section class="section">
      <div class="chip-row">
        <button v-for="item in categories" :key="item" class="chip" :class="{ active: selected === item }" @click="selectCategory(item)">
          {{ item }}
        </button>
      </div>
    </section>

    <section class="section content-grid">
      <div class="list-stack">
        <RouterLink v-for="stall in stalls" :key="stall.id" class="list-card" :to="`/stalls/${stall.id}`">
          <div class="list-card-header">
            <div>
              <h3>{{ stall.stallName }}</h3>
              <p>{{ stall.address }}</p>
            </div>
            <span class="status-tag">{{ statusText(stall.businessStatus) }}</span>
          </div>
          <div class="meta-row">
            <span>{{ stall.vendorName }}</span>
            <span>{{ stall.category }}</span>
            <span>{{ stall.distance }}</span>
            <span>评分 {{ stall.rating ?? '-' }}</span>
          </div>
          <p class="muted">{{ stall.description }}</p>
        </RouterLink>
        <article v-if="!loading && stalls.length === 0" class="list-card">
          <h3>暂无摊位</h3>
          <p>换个关键词或分类试试。</p>
        </article>
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
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { stallApi } from '@yuntanfang/api'

const route = useRoute()
const keyword = ref(String(route.query.keyword ?? ''))
const selected = ref('全部')
const categories = ['全部', '地方特色', '农家特产', '非遗好物', '甜点饮品', '传统小吃']
const stalls = ref<any[]>([])
const loading = ref(false)

function statusText(s: string) {
  return s === 'open' ? '营业中' : s === 'closed' ? '休息中' : '暂未营业'
}

function selectCategory(item: string) {
  selected.value = item
  load()
}

async function load() {
  loading.value = true
  try {
    const category = selected.value === '全部' ? undefined : selected.value
    const res = await stallApi.search(keyword.value || undefined, category)
    stalls.value = res.data.data?.records ?? []
  } catch {
    stalls.value = []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
