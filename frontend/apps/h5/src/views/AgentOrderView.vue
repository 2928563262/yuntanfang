<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">智能订单 Agent</p>
        <h1 class="page-title">一句话预约取餐</h1>
      </div>
      <RouterLink class="ghost-pill" to="/orders">我的订单</RouterLink>
    </header>

    <section class="section content-grid">
      <div class="card agent-panel">
        <div class="agent-messages">
          <article v-for="message in messages" :key="message.content" :class="['agent-bubble', `agent-${message.role}`]">
            {{ message.content }}
          </article>
        </div>
        <div class="agent-input">
          <input v-model="draft" placeholder="例如：帮我订烟火小摊的招牌汤粉，两份，18:30 取" />
          <button class="primary-pill" type="button" :disabled="loading" @click="sendMessage">{{ loading ? '识别中' : '发送' }}</button>
        </div>
      </div>

      <aside class="card">
        <h2>订单确认卡</h2>
        <div class="list-stack">
          <div v-if="orderCard" class="list-card">
            <h3>{{ orderCard.stallName }} · {{ orderSummary }}</h3>
            <p>预计 {{ orderCard.pickupTime }} 取餐。</p>
            <div class="meta-row">
              <span>总价 ¥{{ orderCard.totalAmount }}</span>
              <span>解析置信度 {{ orderCard.confidence }}</span>
              <span>{{ orderCard.status }}</span>
            </div>
          </div>
          <div v-else class="list-card">
            <h3>等待识别</h3>
            <p>输入一句话需求后，这里会生成订单确认卡。</p>
          </div>
          <RouterLink class="primary-pill" to="/stalls/1/reserve">确认预约</RouterLink>
          <RouterLink class="ghost-pill" to="/stalls">降级为商品列表下单</RouterLink>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { agentApi, type AgentOrderResult } from '@yuntanfang/api'
import { agentMessages } from '../data/mock'

const draft = ref('')
const loading = ref(false)
const messages = ref([...agentMessages])
const orderCard = ref<AgentOrderResult | null>(null)

const orderSummary = computed(() => {
  if (!orderCard.value) {
    return ''
  }

  return orderCard.value.items.map((item) => `${item.name} x${item.quantity}`).join('、')
})

async function sendMessage() {
  const content = draft.value.trim()
  if (!content || loading.value) {
    return
  }

  messages.value.push({ role: 'user', content })
  draft.value = ''
  loading.value = true

  try {
    const response = await agentApi.parseOrder({
      message: content,
      history: messages.value.map((item) => `${item.role}: ${item.content}`)
    })
    orderCard.value = response.data.data
    messages.value.push({ role: 'assistant', content: response.data.data.reply })
  } catch {
    messages.value.push({ role: 'assistant', content: '智能点单服务暂不可用，请先使用商品列表下单。' })
  } finally {
    loading.value = false
  }
}
</script>
