<template>
  <main class="page home-page">
    <section class="home-hero" aria-labelledby="home-title">
      <div class="home-hero-copy">
        <p class="eyebrow">云摊坊 · 智慧摊务服务</p>
        <h1 id="home-title">找附近好摊，预约到点取</h1>
        <div class="hero-actions">
          <RouterLink class="primary-pill" to="/stalls">查找摊位</RouterLink>
          <RouterLink class="ghost-pill" to="/orders">我的订单</RouterLink>
        </div>
      </div>

      <div class="home-hero-visual" aria-label="附近摊位预览">
        <div class="market-ticket">
          <span>附近热摊</span>
          <strong>北站中心公园</strong>
          <small>18:00 后陆续出摊</small>
        </div>
      </div>
    </section>

    <section class="home-search-panel" aria-label="搜索">
      <van-search v-model="keyword" shape="round" placeholder="搜索摊位、商品、位置" @search="goSearch" />
      <button class="home-location" type="button" @click="goSearch">附近 2km</button>
    </section>

    <section class="home-section">
      <div class="section-head">
        <h2>常用服务</h2>
      </div>
      <div class="home-action-grid">
        <RouterLink v-for="item in quickActions" :key="item.title" class="home-action-card" :to="item.to">
          <van-icon :name="item.icon" />
          <strong>{{ item.title }}</strong>
          <span>{{ item.desc }}</span>
        </RouterLink>
      </div>
    </section>

    <section class="home-section">
      <div class="section-head">
        <h2>分类筛选</h2>
      </div>
      <div class="chip-row">
        <button
          v-for="item in categoryOptions"
          :key="item"
          :class="['chip', { active: activeCategory === item }]"
          type="button"
          @click="activeCategory = item"
        >
          {{ item }}
        </button>
      </div>
    </section>

    <section class="home-section home-content-grid">
      <div>
        <div class="section-head">
          <h2>附近摊位</h2>
          <RouterLink class="muted" to="/stalls">查看更多</RouterLink>
        </div>
        <div class="home-stall-grid">
          <RouterLink v-for="stall in filteredStalls" :key="stall.id" class="home-stall-card" :to="`/stalls/${stall.id}`">
            <div class="home-stall-top">
              <div>
                <strong>{{ stall.stallName }}</strong>
                <span>{{ stall.vendorName }} · {{ stall.category }}</span>
              </div>
              <em>{{ statusText(stall.businessStatus) }}</em>
            </div>
            <p>{{ stall.description }}</p>
            <div class="meta-row">
              <span>{{ stall.distance }}</span>
              <span>{{ stall.address }}</span>
              <span>评分 {{ stall.rating ?? '-' }}</span>
            </div>
          </RouterLink>
          <article v-if="!loading && filteredStalls.length === 0" class="home-empty">
            <strong>当前分类暂无摊位</strong>
            <span>换个分类看看，或去全部摊位浏览附近好摊。</span>
          </article>
        </div>
      </div>

      <aside class="home-side-panel">
        <div class="section-head">
          <h2>平台公告</h2>
        </div>
        <div class="home-notice-list">
          <RouterLink v-for="notice in notices" :key="notice.id" class="home-notice" to="/messages">
            <strong>{{ notice.title }}</strong>
            <span>{{ notice.content }}</span>
          </RouterLink>
          <article v-if="notices.length === 0" class="home-notice">
            <strong>暂无公告</strong>
            <span>平台公告会显示在这里。</span>
          </article>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { contentApi, stallApi } from '@yuntanfang/api'

const router = useRouter()
const keyword = ref('')
const activeCategory = ref('全部')
const categoryOptions = ['全部', '地方特色', '农家特产', '非遗好物', '甜点饮品', '传统小吃']
const stalls = ref<any[]>([])
const notices = ref<any[]>([])
const loading = ref(false)

const quickActions = [
  { title: '智能点单', desc: '一句话预约', icon: 'chat-o', to: '/agent-order' },
  { title: '附近故事', desc: '摊主动态', icon: 'photo-o', to: '/stories' },
  { title: '我的订单', desc: '查看取餐状态', icon: 'orders-o', to: '/orders' },
  { title: '收藏关注', desc: '常逛摊位', icon: 'star-o', to: '/favorites' },
  { title: '提交投诉', desc: '问题留痕', icon: 'warning-o', to: '/complaints/create' }
]

function statusText(s: string) {
  return s === 'open' ? '营业中' : s === 'closed' ? '休息中' : '暂未营业'
}

const filteredStalls = computed(() => {
  if (activeCategory.value === '全部') {
    return stalls.value
  }
  return stalls.value.filter((s) => s.category === activeCategory.value)
})

async function load() {
  loading.value = true
  try {
    const [s, n] = await Promise.all([stallApi.nearby(), contentApi.notices()])
    stalls.value = s.data.data?.records ?? []
    notices.value = n.data.data?.records ?? []
  } catch {
    stalls.value = []
    notices.value = []
  } finally {
    loading.value = false
  }
}

onMounted(load)

function goSearch() {
  router.push({ path: '/stalls', query: { keyword: keyword.value } })
}
</script>
