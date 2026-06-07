<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">系统 Agent</p>
        <h1 class="page-title">想做什么，直接说</h1>
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
        <div class="chip-row">
          <button v-for="prompt in suggestedPrompts" :key="prompt" class="chip" type="button" @click="usePrompt(prompt)">
            {{ prompt }}
          </button>
        </div>
        <div class="agent-input">
          <input v-model="draft" placeholder="问我可以做什么，或直接说你的需求" @keydown.enter="sendMessage" />
          <button class="primary-pill" type="button" :disabled="loading" @click="sendMessage">{{ loading ? '处理中' : '发送' }}</button>
        </div>
      </div>

      <aside class="card">
        <h2>Agent 操作台</h2>
        <div class="list-stack">
          <div v-if="agentResult" class="list-card">
            <h3>{{ actionTitle }}</h3>
            <p>{{ agentResult.reply }}</p>
            <div class="meta-row">
              <span>{{ agentResult.intent }}</span>
              <span>{{ agentResult.status }}</span>
            </div>
          </div>
          <div v-else class="list-card">
            <h3>等待指令</h3>
            <p>我可以帮你找摊位、下单、评价、投诉，也可以告诉你功能入口。</p>
          </div>
          <div v-if="agentResult?.cards.length" class="list-stack">
            <div v-for="(card, index) in agentResult.cards" :key="index" class="list-card agent-result-card">
              <strong>{{ cardTitle(card) }}</strong>
              <span v-for="line in cardLines(card)" :key="line">{{ line }}</span>
            </div>
          </div>
          <button class="primary-pill" type="button" :disabled="!agentResult?.action" @click="runAction">
            {{ agentResult?.action.label ?? '等待操作' }}
          </button>
          <RouterLink class="ghost-pill" to="/stalls">手动浏览摊位</RouterLink>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { agentApi, type AgentChatResult } from '@yuntanfang/api'
import { useUserDataStore } from '../stores/userData'

type AgentMessage = { role: 'assistant' | 'user'; content: string }

const router = useRouter()
const userData = useUserDataStore()
const draft = ref('')
const loading = ref(false)
const messages = ref<AgentMessage[]>([
  { role: 'assistant', content: '你可以问我入口在哪，也可以直接让我找摊位、下单、评价或投诉。' }
])
const agentResult = ref<AgentChatResult | null>(null)
const suggestedPrompts = ref(['帮我找附近摊位', '我想预约晚餐', '订单怎么评价', '我要提交投诉'])

const actionTitle = computed(() => agentResult.value?.action.label ?? '建议操作')

async function sendMessage() {
  const content = draft.value.trim()
  if (!content || loading.value) {
    return
  }

  messages.value.push({ role: 'user', content })
  draft.value = ''
  loading.value = true

  try {
    const response = await agentApi.chat({
      message: content,
      history: messages.value.map((item) => `${item.role}: ${item.content}`)
    })
    agentResult.value = response.data.data
    suggestedPrompts.value = response.data.data.suggestedPrompts
    messages.value.push({ role: 'assistant', content: response.data.data.reply })
  } catch {
    messages.value.push({ role: 'assistant', content: 'Agent 服务暂不可用，请先使用页面入口操作。' })
  } finally {
    loading.value = false
  }
}

function usePrompt(prompt: string) {
  draft.value = prompt
  sendMessage()
}

function runAction() {
  if (!agentResult.value?.action) {
    return
  }

  const action = agentResult.value.action
  const payload = action.payload
  if (action.type === 'create_order') {
    const quantity = toNumber(payload.quantity, 1)
    const amount = toText(payload.totalAmount, (quantity * 16).toFixed(2))
    const order = userData.createOrder({
      stallId: stallIdFromName(toText(payload.stallName, '烟火小摊')),
      product: toText(payload.productName, '招牌汤粉'),
      quantity,
      pickupTime: toText(payload.pickupTime, '今天 19:00'),
      contact: '',
      amount,
      price: (Number(amount) / Math.max(quantity, 1)).toFixed(2)
    })
    router.push(`/orders/${order.id}`)
    return
  }

  if (action.type === 'submit_review') {
    userData.addReview({
      orderId: toNumber(payload.orderId, 1002),
      rating: toNumber(payload.rating, 5),
      content: toText(payload.content, '整体体验不错，取餐流程顺畅。')
    })
    router.push('/my-reviews')
    return
  }

  if (action.type === 'submit_complaint') {
    userData.addComplaint({
      target: toText(payload.target, '烟火小摊'),
      type: toText(payload.type, '服务问题'),
      description: toText(payload.description, '通过 Agent 提交投诉。')
    })
    router.push('/complaints')
    return
  }

  router.push(action.route || '/')
}

function cardTitle(card: Record<string, unknown>) {
  return toText(card.name, toText(card.stallName, toText(card.target, toText(card.topic, '结果'))))
}

function cardLines(card: Record<string, unknown>) {
  return Object.entries(card)
    .filter(([key]) => !['id', 'orderId', 'complaintId', 'reviewId', 'name', 'stallName', 'target', 'topic'].includes(key))
    .slice(0, 5)
    .map(([key, value]) => `${key}: ${Array.isArray(value) ? value.join('、') : value}`)
}

function toText(value: unknown, fallback: string) {
  return typeof value === 'string' && value.trim() ? value : fallback
}

function toNumber(value: unknown, fallback: number) {
  const parsed = Number(value)
  return Number.isFinite(parsed) ? parsed : fallback
}

function stallIdFromName(name: string) {
  if (name.includes('鲜')) return 2
  if (name.includes('糖画')) return 3
  return 1
}
</script>
