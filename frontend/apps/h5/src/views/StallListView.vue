<template>
  <main class="page stall-search-page">
    <van-nav-bar title="摊位检索" left-arrow fixed placeholder @click-left="router.back()" />

    <section class="search-panel">
      <van-search
        v-model="searchText"
        shape="round"
        placeholder="搜索摊位、商品、店铺"
        show-action
        @search="submitSearch"
        @cancel="submitSearch"
      >
        <template #action>
          <button class="search-action" type="button" @click="submitSearch">搜索</button>
        </template>
      </van-search>
      <p>{{ keyword ? `正在查看“${keyword}”相关摊位` : '已为你展示附近营业摊位' }}</p>
    </section>

    <section class="category-filter" aria-label="分类筛选">
      <button
        v-for="category in categories"
        :key="category"
        :class="{ active: activeCategory === category }"
        type="button"
        @click="activeCategory = category"
      >
        {{ category }}
      </button>
    </section>

    <van-notice-bar
      v-if="isDemoData"
      class="demo-notice"
      color="#ff6b4a"
      background="#fff0eb"
      left-icon="info-o"
      text="接口暂不可用，当前为演示数据"
    />

    <section v-if="loading" class="search-state">
      <van-skeleton title avatar :row="4" />
      <van-skeleton title avatar :row="4" />
    </section>

    <section v-else-if="errorMessage && filteredStalls.length === 0" class="empty-panel">
      <van-empty image="error" description="接口请求失败">
        <p>{{ errorMessage }}</p>
        <van-button round type="primary" size="small" @click="loadStalls">重新加载</van-button>
      </van-empty>
    </section>

    <section v-else-if="filteredStalls.length === 0" class="empty-panel">
      <van-empty image="search" description="暂未找到相关摊位">
        <van-button round type="primary" size="small" @click="clearSearch">查看附近摊位</van-button>
      </van-empty>
    </section>

    <section v-else class="stall-list search-result-list" aria-label="检索结果">
      <article v-for="stall in filteredStalls" :key="stall.id" class="stall-card" @click="goDetail(stall.id)">
        <img class="stall-photo" :src="stall.image" :alt="stall.name" />
        <div class="stall-info">
          <div class="stall-title-row">
            <h3>{{ stall.name }}</h3>
            <van-icon name="arrow" />
          </div>
          <div class="stall-meta">
            <strong>{{ stall.score }}</strong>
            <span>月售 {{ stall.sales }}</span>
            <span>人均 ¥{{ stall.avg }}</span>
          </div>
          <p>{{ stall.time }} · {{ stall.distance }} · {{ stall.area }}</p>
          <div class="stall-tags">
            <span v-for="tag in stall.tags" :key="tag">{{ tag }}</span>
          </div>
        </div>
        <van-tag class="status-tag" :type="stall.open ? 'success' : 'warning'">
          {{ stall.open ? '营业中' : '即将出摊' }}
        </van-tag>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { stallApi } from '@yuntanfang/api'
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { demoStalls } from '../mock/demoData'

interface StallCard {
  id: string | number
  name: string
  score: string
  sales: number
  avg: number
  time: string
  distance: string
  area: string
  tags: string[]
  open: boolean
  image: string
  category?: string
}

const route = useRoute()
const router = useRouter()
const searchText = ref('')
const stalls = ref<StallCard[]>([])
const loading = ref(false)
const errorMessage = ref('')
const isDemoData = ref(false)
const activeCategory = ref('全部')
let requestSeq = 0

const keyword = computed(() => String(route.query.keyword ?? '').trim())
const categories = computed(() => ['全部', ...Array.from(new Set(demoStalls.map((stall) => stall.category)))])
const filteredStalls = computed(() => {
  if (activeCategory.value === '全部') return stalls.value
  return stalls.value.filter((stall) => stall.category === activeCategory.value || stall.tags.includes(activeCategory.value))
})

watch(
  keyword,
  (value) => {
    searchText.value = value
    loadStalls()
  },
  { immediate: true }
)

async function loadStalls() {
  const seq = ++requestSeq
  loading.value = true
  errorMessage.value = ''
  isDemoData.value = false

  try {
    const response = keyword.value ? await stallApi.search(keyword.value) : await stallApi.nearby()
    if (seq !== requestSeq) return
    stalls.value = normalizeStalls(response)
  } catch (error) {
    if (seq !== requestSeq) return
    errorMessage.value = error instanceof Error ? error.message : '无法连接摊位接口'
    isDemoData.value = true
    stalls.value = filterDemoStalls(keyword.value).map((stall, index) => toStallCard(stall, index))
  } finally {
    if (seq === requestSeq) {
      loading.value = false
    }
  }
}

function submitSearch() {
  const nextKeyword = searchText.value.trim()
  router.push({ path: '/stalls', query: nextKeyword ? { keyword: nextKeyword } : {} })
}

function clearSearch() {
  searchText.value = ''
  router.push('/stalls')
}

function goDetail(id: string | number) {
  router.push(`/stalls/${id}`)
}

function normalizeStalls(response: unknown): StallCard[] {
  const payload = unwrapPayload(response)
  const records = Array.isArray(payload) ? payload : Array.isArray(payload?.records) ? payload.records : []
  return records.map((item: any, index: number) => toStallCard(item, index))
}

function unwrapPayload(response: any): any {
  const axiosData = response?.data ?? response
  const apiData = axiosData?.data ?? axiosData
  return apiData?.data ?? apiData
}

function toStallCard(item: any, index: number): StallCard {
  const fallback = demoStalls[index % demoStalls.length]
  const id = item?.id ?? fallback.id
  const status = String(item?.status ?? '').toLowerCase()
  return {
    id,
    name: item?.name ?? item?.stallName ?? `${item?.title ?? fallback.name}`,
    score: item?.score ?? item?.rating ?? fallback.score,
    sales: Number(item?.sales ?? item?.monthlySales ?? fallback.sales),
    avg: Number(item?.avg ?? item?.averagePrice ?? item?.perCapita ?? fallback.avg),
    time: item?.time ?? item?.duration ?? fallback.time,
    distance: item?.distance ?? fallback.distance,
    area: item?.area ?? item?.address ?? fallback.area,
    tags: normalizeTags(item?.tags ?? item?.coupons ?? item?.labels, fallback.tags),
    open: typeof item?.open === 'boolean' ? item.open : status ? status === 'open' : fallback.open,
    image: item?.image ?? item?.cover ?? item?.photo ?? fallback.image,
    category: item?.category ?? fallback.category
  }
}

function normalizeTags(value: unknown, fallback: string[]) {
  if (Array.isArray(value)) return value.map(String).slice(0, 4)
  if (typeof value === 'string' && value.trim()) return value.split(/[,，\s]+/).filter(Boolean).slice(0, 4)
  return fallback
}

function filterDemoStalls(value: string) {
  if (!value) return demoStalls
  const lower = value.toLowerCase()
  const filtered = demoStalls.filter((stall) => {
    return [stall.name, stall.area, ...stall.tags].some((text) => text.toLowerCase().includes(lower))
  })
  return filtered.length > 0 ? filtered : demoStalls
}
</script>
