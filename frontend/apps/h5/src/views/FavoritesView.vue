<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">收藏与关注</p>
        <h1 class="page-title">我的收藏</h1>
      </div>
      <RouterLink class="ghost-pill" to="/stalls">去逛逛</RouterLink>
    </header>

    <section class="section list-stack">
      <article v-for="item in favorites" :key="item.id" class="list-card">
        <div class="list-card-header">
          <div>
            <h3>{{ item.bizName ?? ('摊位#' + item.bizId) }}</h3>
            <p>{{ bizTypeText(item.bizType) }}</p>
          </div>
          <span class="status-tag">已收藏</span>
        </div>
        <div class="meta-row">
          <RouterLink v-if="item.bizType === 'stall'" class="ghost-pill" :to="`/stalls/${item.bizId}`">查看摊位</RouterLink>
          <button class="ghost-pill" type="button" @click="remove(item.id)">取消收藏</button>
        </div>
      </article>
      <article v-if="!loading && favorites.length === 0" class="list-card">
        <h3>还没有收藏</h3>
        <p>在摊位详情页点「收藏摊位」，这里就会出现。</p>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { interactionApi } from '@yuntanfang/api'

const favorites = ref<any[]>([])
const loading = ref(false)

function bizTypeText(t: string) {
  return t === 'stall' ? '摊位' : t === 'product' ? '商品' : t === 'vendor' ? '摊主' : t
}

async function load() {
  loading.value = true
  try {
    const res = await interactionApi.favorites()
    favorites.value = res.data.data?.records ?? []
  } catch {
    favorites.value = []
  } finally {
    loading.value = false
  }
}

async function remove(id: number) {
  try {
    await interactionApi.unfavorite(id)
    await load()
  } catch {
    // 忽略
  }
}

onMounted(load)
</script>
