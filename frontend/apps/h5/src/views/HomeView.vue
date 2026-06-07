<template>
  <main class="page home-page">
    <section class="home-hero">
      <div class="hero-bar">
        <button class="location-chip" type="button" @click="goMap">
          <van-icon name="location-o" />
          <span>深圳北站中心公园</span>
          <van-icon name="arrow-down" />
        </button>
        <button class="cart-button" type="button" @click="goCart" aria-label="购物车">
          <van-icon name="cart-o" />
        </button>
      </div>

      <form class="hero-search" @submit.prevent="goSearch">
        <input v-model="keyword" type="search" placeholder="搜索摊位、商品、店铺" aria-label="搜索摊位、商品、店铺" />
        <button type="submit">搜索</button>
      </form>

      <div class="hero-copy">
        <p>云摊坊用户端</p>
        <h1>发现身边的烟火小摊</h1>
        <span>政府备案 · 实时出摊 · 预约不跑空</span>
      </div>

      <div class="hot-keys" aria-label="热门搜索">
        <button v-for="tag in hotKeys" :key="tag" type="button" @click="searchTag(tag)">{{ tag }}</button>
      </div>
    </section>

    <section class="category-grid" aria-label="摊位分类">
      <button v-for="item in categories" :key="item.label" class="category-item" type="button" @click="searchTag(item.label)">
        <span :class="['category-icon', item.tone]">
          {{ item.mark }}
        </span>
        <strong>{{ item.label }}</strong>
      </button>
    </section>

    <section class="feature-strip" aria-label="特色专区">
      <button v-for="feature in features" :key="feature.title" class="feature-card" type="button" @click="searchTag(feature.title)">
        <span>{{ feature.title }}</span>
        <strong>{{ feature.subtitle }}</strong>
        <img :src="feature.image" :alt="feature.title" />
      </button>
    </section>

    <section class="section-head nearby-head">
      <div>
        <p>附近营业中</p>
        <h2>离你最近的放心小摊</h2>
      </div>
      <button type="button" @click="goSearch">全部</button>
    </section>

    <div class="sort-tabs" aria-label="排序方式">
      <button v-for="tab in sortTabs" :key="tab" :class="{ active: activeSort === tab }" type="button" @click="activeSort = tab">
        {{ tab }}
      </button>
      <button class="more-sort" type="button" aria-label="更多筛选">
        <van-icon name="filter-o" />
      </button>
    </div>

    <section class="stall-list" aria-label="摊位列表">
      <article v-for="stall in stalls" :key="stall.id" class="stall-card" @click="router.push(`/stalls/${stall.id}`)">
        <img class="stall-photo" :src="stall.image" :alt="stall.name" />
        <div class="stall-info">
          <div class="stall-title-row">
            <h3>{{ stall.name }}</h3>
            <van-icon name="ellipsis" />
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

    <div class="floating-actions" aria-label="快捷操作">
      <button type="button" @click="scrollTop" aria-label="回到顶部">
        <van-icon name="back-top" />
      </button>
      <button type="button" @click="goCart" aria-label="购物车">
        <van-icon name="cart-o" />
      </button>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const keyword = ref('')
const activeSort = ref('综合排序')

const hotKeys = ['麻辣小龙虾', '奶茶', '菠萝炒饭', '烧烤生蚝']
const sortTabs = ['综合排序', '距离最近', '好评优先', '销量优先']

const imageBase = 'https://images.unsplash.com/'

const categories = [
  { label: '烧烤炸串', mark: '烤', tone: 'coral' },
  { label: '特色小吃', mark: '味', tone: 'orange' },
  { label: '卤味熟食', mark: '卤', tone: 'green' },
  { label: '面食粉类', mark: '面', tone: 'rose' },
  { label: '茶饮饮品', mark: '茶', tone: 'purple' },
  { label: '甜品冰品', mark: '甜', tone: 'blue' },
  { label: '快餐简餐', mark: '快', tone: 'red' },
  { label: '全部分类', mark: '全', tone: 'mint' }
]

const features = [
  {
    title: '今日推荐',
    subtitle: '热门摊位',
    image: `${imageBase}photo-1544025162-d76694265947?auto=format&fit=crop&w=240&q=80`
  },
  {
    title: '公益专区',
    subtitle: '爱心摊位',
    image: `${imageBase}photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=240&q=80`
  },
  {
    title: '特色专区',
    subtitle: '非遗手作',
    image: `${imageBase}photo-1555939594-58d7cb561ad1?auto=format&fit=crop&w=240&q=80`
  }
]

const stalls = [
  {
    id: 1,
    name: '刘记烧烤',
    score: '4.8分',
    sales: 312,
    avg: 20,
    time: '20分钟',
    distance: '2.2km',
    area: '北站夜市A区',
    tags: ['50减3', '100减6', '已备案'],
    open: true,
    image: `${imageBase}photo-1529692236671-f1f6cf9683ba?auto=format&fit=crop&w=240&q=80`
  },
  {
    id: 2,
    name: '小林柠檬茶',
    score: '4.7分',
    sales: 268,
    avg: 15,
    time: '12分钟',
    distance: '880m',
    area: '中心公园东门',
    tags: ['公益摊位', '今日推荐', '满35减5'],
    open: true,
    image: `${imageBase}photo-1551024601-bec78aea704b?auto=format&fit=crop&w=240&q=80`
  },
  {
    id: 3,
    name: '老王早食',
    score: '4.6分',
    sales: 196,
    avg: 10,
    time: '6分钟',
    distance: '320m',
    area: '大学城临时点',
    tags: ['助农摊位', '预约自提', '150减10'],
    open: false,
    image: `${imageBase}photo-1533089860892-a7c6f0a88666?auto=format&fit=crop&w=240&q=80`
  }
]

function goSearch() {
  const nextKeyword = keyword.value.trim()
  router.push({ path: '/stalls', query: nextKeyword ? { keyword: nextKeyword } : {} })
}

function searchTag(tag: string) {
  keyword.value = tag
  goSearch()
}

function goCart() {
  router.push('/cart')
}

function goMap() {
  router.push('/map')
}

function scrollTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}
</script>
