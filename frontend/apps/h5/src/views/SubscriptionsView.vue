<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">出摊提醒</p>
        <h1 class="page-title">提醒订阅</h1>
      </div>
      <RouterLink class="ghost-pill" to="/stalls">添加订阅</RouterLink>
    </header>

    <section class="section content-grid">
      <div class="list-stack">
        <article v-for="item in subscriptions" :key="item.id" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ item.vendorName ?? ('摊主#' + item.vendorId) }}</h3>
              <p>出摊提醒 · 站内信</p>
            </div>
            <span class="status-tag">{{ item.status === 'active' ? '已开启' : '已暂停' }}</span>
          </div>
        </article>
        <article v-if="!loading && subscriptions.length === 0" class="list-card">
          <h3>还没有订阅</h3>
          <p>在摊位详情页点「订阅提醒」，开摊时会通知你。</p>
        </article>
      </div>

      <aside class="card">
        <h2>通知渠道</h2>
        <div class="list-stack">
          <div class="list-card">
            <h3>站内信</h3>
            <p>默认开启，记录在系统通知中。</p>
          </div>
          <div class="list-card">
            <h3>网页通知</h3>
            <p>需要浏览器授权，后续接入 NotificationService。</p>
          </div>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { interactionApi } from '@yuntanfang/api'

const subscriptions = ref<any[]>([])
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    const res = await interactionApi.subscriptions()
    subscriptions.value = res.data.data?.records ?? []
  } catch {
    subscriptions.value = []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
