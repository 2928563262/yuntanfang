<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">用户评价</p>
        <h1 class="page-title">评价回复</h1>
      </div>
      <button class="ghost-pill" type="button" @click="load">刷新</button>
    </header>

    <section class="section content-grid">
      <div class="list-stack">
        <article v-for="review in reviews" :key="review.id" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ review.userName || `用户 ${review.userId}` }}</h3>
              <p>{{ review.content }}</p>
              <p v-if="review.reply" class="muted">已回复：{{ review.reply }}</p>
            </div>
            <span class="status-tag">{{ review.reply ? '已回复' : '待回复' }}</span>
          </div>
          <div class="meta-row">
            <span>评分 {{ review.rating }} 星</span>
            <span>{{ review.stallName || '摊位评价' }}</span>
          </div>
          <textarea v-model="drafts[review.id]" placeholder="回复用户评价"></textarea>
          <p v-if="errors[review.id]" class="form-error">{{ errors[review.id] }}</p>
          <button class="primary-pill" type="button" @click="reply(review.id)">提交回复</button>
        </article>
        <article v-if="!loading && reviews.length === 0" class="list-card">
          <h3>暂无评价</h3>
          <p>用户完成订单评价后会出现在这里。</p>
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
import { onMounted, reactive, ref } from 'vue'
import { vendorApi } from '@yuntanfang/api'

const reviews = ref<any[]>([])
const drafts = reactive<Record<number, string>>({})
const errors = reactive<Record<number, string>>({})
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    const res = await vendorApi.reviews()
    reviews.value = res.data.data?.records ?? []
    reviews.value.forEach((review) => {
      drafts[review.id] = review.reply ?? ''
    })
  } finally {
    loading.value = false
  }
}

async function reply(id: number) {
  errors[id] = ''
  try {
    await vendorApi.replyReview(id, drafts[id] || '')
    await load()
  } catch (err: any) {
    errors[id] = err?.response?.data?.message ?? '回复失败'
  }
}

onMounted(load)
</script>
