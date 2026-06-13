<template>
  <main class="page">
    <header class="page-header">
      <button class="ghost-pill" @click="$router.back()">返回</button>
      <RouterLink class="ghost-pill" to="/complaints/create">投诉</RouterLink>
    </header>

    <section class="hero-card">
      <p class="eyebrow">{{ stall.category }} · {{ stall.distance }}</p>
      <h1>{{ stall.stallName }}</h1>
      <p>{{ stall.description }}</p>
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
            <span class="status-tag">{{ statusText(stall.businessStatus) }}</span>
          </div>
          <div class="meta-row">
            <span>摊主：{{ stall.vendorName }}</span>
            <span>评分：{{ stall.rating ?? '-' }}</span>
            <span>{{ stall.address }}</span>
          </div>
          <div class="action-grid section">
            <button class="ghost-pill" type="button" :disabled="favorited" @click="addFavorite">{{ favorited ? '已收藏' : '收藏摊位' }}</button>
            <RouterLink class="ghost-pill" to="/favorites">我的收藏</RouterLink>
            <button class="ghost-pill" type="button" :disabled="subscribed" @click="addSubscribe">{{ subscribed ? '已订阅' : '订阅提醒' }}</button>
            <RouterLink class="ghost-pill" to="/stories">摊主故事</RouterLink>
          </div>
        </article>

        <article class="card">
          <div class="section-head">
            <h2>商品</h2>
            <RouterLink class="muted" to="/orders">查看订单</RouterLink>
          </div>
          <div class="list-stack">
            <div v-for="product in products" :key="product.id" class="list-card">
              <div class="list-card-header">
                <div>
                  <h3>{{ product.productName }}</h3>
                  <p>{{ product.description ?? '支持预约取货' }}</p>
                </div>
                <strong>¥{{ product.price ?? '-' }}</strong>
              </div>
            </div>
            <p v-if="products.length === 0" class="muted">该摊位暂未上架商品。</p>
          </div>
        </article>

        <article class="card">
          <div class="section-head">
            <h2>顾客评价</h2>
            <RouterLink class="muted" to="/my-reviews">我的评价</RouterLink>
          </div>
          <div class="list-stack">
            <div v-for="review in reviews" :key="review.id" class="list-card">
              <div class="list-card-header">
                <div>
                  <h3>{{ review.userName ?? '匿名用户' }}</h3>
                  <p>{{ review.content }}</p>
                </div>
                <span class="status-tag">{{ review.rating }} 星</span>
              </div>
            </div>
            <p v-if="reviews.length === 0" class="muted">暂无评价。</p>
          </div>
        </article>
      </div>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { interactionApi, stallApi } from '@yuntanfang/api'

const route = useRoute()
const stall = ref<any>({})
const products = ref<any[]>([])
const reviews = ref<any[]>([])
const favorited = ref(false)
const subscribed = ref(false)

function statusText(s: string) {
  return s === 'open' ? '营业中' : s === 'closed' ? '休息中' : '暂未营业'
}

async function load() {
  const id = route.params.id as string
  try {
    const [d, p, r] = await Promise.all([stallApi.detail(id), stallApi.products(id), stallApi.reviews(id)])
    stall.value = d.data.data ?? {}
    products.value = p.data.data?.records ?? []
    reviews.value = r.data.data?.records ?? []
  } catch {
    stall.value = {}
  }
}

async function addFavorite() {
  try {
    await interactionApi.favorite({ bizType: 'stall', bizId: Number(stall.value.id), bizName: stall.value.stallName })
    favorited.value = true
  } catch {
    // 未登录等情况忽略
  }
}

async function addSubscribe() {
  try {
    if (stall.value.vendorId) {
      await interactionApi.subscribe({ vendorId: Number(stall.value.vendorId), vendorName: stall.value.vendorName })
      subscribed.value = true
    }
  } catch {
    // 忽略
  }
}

onMounted(load)
</script>
