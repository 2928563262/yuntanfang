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
          <article v-for="message in activeSession.messages" :key="message.id" :class="['agent-bubble', `agent-${message.role}`]">
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

      <aside class="agent-side">
        <section class="card">
          <h2>Agent 操作台</h2>
          <div class="list-stack">
            <div class="list-card">
              <h3>{{ panelTitle }}</h3>
              <p>{{ panelText }}</p>
              <div class="meta-row">
                <span>{{ agentResult?.intent ?? '等待输入' }}</span>
                <span>{{ agentResult?.status ?? 'idle' }}</span>
              </div>
            </div>

            <div class="agent-flow">
              <div v-for="step in visibleSteps" :key="step.title" :class="['agent-flow-step', `agent-flow-${step.status}`]">
                <span></span>
                <div>
                  <strong>{{ step.title }}</strong>
                  <small>{{ step.detail }}</small>
                </div>
              </div>
            </div>

            <div v-if="agentResult?.cards.length" class="list-stack">
              <div v-for="(card, index) in agentResult.cards" :key="index" class="list-card agent-result-card">
                <strong>{{ cardTitle(card) }}</strong>
                <span v-for="line in cardLines(card)" :key="line">{{ line }}</span>
              </div>
            </div>

            <button class="primary-pill" type="button" :disabled="!canRunAction" @click="runAction">
              {{ agentResult?.action.label ?? '等待操作' }}
            </button>
            <RouterLink class="ghost-pill" to="/stalls">手动浏览摊位</RouterLink>
          </div>
        </section>

        <section class="card agent-session-panel">
          <div class="section-head">
            <h2>会话列表</h2>
            <button class="ghost-pill" type="button" @click="createSession">新会话</button>
          </div>
          <div class="list-stack">
            <button
              v-for="session in sessions"
              :key="session.id"
              :class="['agent-session-item', { active: session.id === activeSessionId }]"
              type="button"
              @click="switchSession(session.id)"
            >
              <strong>{{ session.title }}</strong>
              <span>{{ formatTime(session.updatedAt) }}</span>
            </button>
          </div>
        </section>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { agentApi, complaintApi, orderApi, type AgentChatResult } from '@yuntanfang/api'

type AgentMessage = { id: string; role: 'assistant' | 'user'; content: string }
type AgentSession = {
  id: string
  title: string
  updatedAt: number
  messages: AgentMessage[]
  result: AgentChatResult | null
  suggestedPrompts: string[]
}

const storageKey = 'ytf_agent_sessions_v1'
const ttl = 1000 * 60 * 60 * 24
const router = useRouter()
const draft = ref('')
const loading = ref(false)
const activeSessionId = ref('')
const sessions = ref<AgentSession[]>(readSessions())

if (sessions.value.length === 0) {
  sessions.value = [newSession()]
}
activeSessionId.value = sessions.value[0].id

const activeSession = computed(() => sessions.value.find((item) => item.id === activeSessionId.value) ?? sessions.value[0])
const agentResult = computed(() => activeSession.value.result)
const suggestedPrompts = computed(() => activeSession.value.suggestedPrompts)
const canRunAction = computed(() => {
  const type = agentResult.value?.action.type
  return Boolean(type && !['ask_clarification', 'none'].includes(type))
})
const panelTitle = computed(() => {
  if (loading.value) return '正在处理'
  return agentResult.value?.action.label ?? '等待指令'
})
const panelText = computed(() => {
  if (loading.value) return 'Agent 正在识别意图、检查参数并准备调用功能 API。'
  return agentResult.value?.reply ?? '我可以帮你找摊位、下单、评价、投诉，也可以告诉你功能入口。'
})
const visibleSteps = computed(() => {
  if (loading.value) {
    return [
      { title: '识别用户意图', status: 'running', detail: '分析你的输入' },
      { title: '检查必需参数', status: 'pending', detail: '确认是否需要追问' },
      { title: '调用功能 API', status: 'pending', detail: '等待参数满足后执行' }
    ]
  }
  return agentResult.value?.processSteps ?? [
    { title: '等待输入', status: 'pending', detail: '发送需求后开始运行' }
  ]
})

watch(
  sessions,
  () => {
    localStorage.setItem(storageKey, JSON.stringify(sessions.value))
  },
  { deep: true }
)

async function sendMessage() {
  const content = draft.value.trim()
  if (!content || loading.value) {
    return
  }

  pushMessage('user', content)
  activeSession.value.title = content.slice(0, 18)
  draft.value = ''
  loading.value = true

  try {
    const result = await chatWithRetry(content)
    activeSession.value.result = result
    activeSession.value.suggestedPrompts = result.suggestedPrompts
    pushMessage('assistant', result.reply)
  } catch {
    pushMessage('assistant', 'Agent 功能暂时不可用，请稍后再试。')
    activeSession.value.result = null
  } finally {
    loading.value = false
    touchSession()
  }
}

async function chatWithRetry(content: string) {
  let lastError: unknown
  for (let attempt = 1; attempt <= 3; attempt++) {
    try {
      const response = await agentApi.chat({
        message: content,
        history: activeSession.value.messages.map((item) => `${item.role}: ${item.content}`),
        context: { currentRoute: '/agent-order', sessionId: activeSession.value.id }
      })
      return response.data.data
    } catch (error) {
      lastError = error
    }
  }
  throw lastError
}

function usePrompt(prompt: string) {
  draft.value = prompt
  sendMessage()
}

async function runAction() {
  if (!agentResult.value?.action) {
    return
  }

  const action = agentResult.value.action
  const payload = action.payload
  if (action.type === 'create_order') {
    const product = strictText(payload.productName)
    if (!product) {
      pushMessage('assistant', '还缺商品名，请先告诉我要预约哪个商品。')
      return
    }
    const quantity = toNumber(payload.quantity, 1)
    const orderPayload = isRecord(payload.orderPayload)
      ? payload.orderPayload
      : {
          vendorId: toNumber(payload.vendorId, 0),
          stallId: stallIdFromName(toText(payload.stallName, '烟火小摊')),
          stallName: toText(payload.stallName, '烟火小摊'),
          pickupTime: toText(payload.pickupTime, '今天 19:00'),
          contactPhone: '',
          remark: 'Agent 创建订单草稿',
          items: [
            {
              productId: toNumber(payload.productId, 0) || undefined,
              productName: product,
              quantity,
              price: toText(payload.price, '0.00')
            }
          ]
        }
    try {
      const response = await orderApi.create(orderPayload)
      const result = response.data.data
      const order = result.order ?? result
      pushMessage('assistant', `订单已创建，订单号 ${order.id}。`)
      router.push(`/orders/${order.id}`)
    } catch {
      pushMessage('assistant', '订单创建失败。请确认已用普通用户账号登录，然后再试一次。')
    }
    return
  }

  if (action.type === 'submit_review') {
    const orderId = toNumber(payload.orderId, 0)
    const rating = toNumber(payload.rating, 0)
    if (orderId <= 0 || rating <= 0) {
      pushMessage('assistant', '还缺订单或评分信息，请说明具体订单号，以及你想给几星。')
      return
    }
    try {
      await orderApi.review(orderId, {
        rating,
        content: toText(payload.content, '整体体验不错，取餐流程顺畅。')
      })
      pushMessage('assistant', `评价已发布，订单号 ${orderId}。`)
      router.push('/my-reviews')
    } catch {
      pushMessage('assistant', '评价发布失败。请确认订单号正确且当前账号可以评价该订单。')
    }
    return
  }

  if (action.type === 'submit_complaint') {
    const target = strictText(payload.target)
    const type = strictText(payload.type)
    const description = strictText(payload.description)
    if (!target || (!type && !description)) {
      pushMessage('assistant', '还缺投诉对象或问题描述，请补充要投诉哪个对象以及发生了什么。')
      return
    }
    try {
      await complaintApi.create({
        vendorId: toNumber(payload.vendorId, 0) || undefined,
        orderId: toNumber(payload.orderId, 0) || undefined,
        type: type || '其他问题',
        description: description || target
      })
      pushMessage('assistant', `投诉已提交：${target}。`)
      router.push('/complaints')
    } catch {
      pushMessage('assistant', '投诉提交失败。请确认已用普通用户账号登录，然后再试一次。')
    }
    return
  }

  router.push(action.route || '/')
}

function pushMessage(role: AgentMessage['role'], content: string) {
  activeSession.value.messages.push({ id: `${Date.now()}-${Math.random()}`, role, content })
  touchSession()
}

function createSession() {
  const session = newSession()
  sessions.value.unshift(session)
  activeSessionId.value = session.id
}

function switchSession(id: string) {
  activeSessionId.value = id
}

function touchSession() {
  activeSession.value.updatedAt = Date.now()
  sessions.value = [activeSession.value, ...sessions.value.filter((item) => item.id !== activeSession.value.id)].slice(0, 12)
}

function newSession(): AgentSession {
  return {
    id: `${Date.now()}-${Math.random()}`,
    title: '新的会话',
    updatedAt: Date.now(),
    messages: [{ id: 'welcome', role: 'assistant', content: '我是云摊坊小助手，可以帮你处理常用事务。' }],
    result: null,
    suggestedPrompts: ['找地方特色摊位', '预约一份招牌烤串', '评价订单 1 五星', '投诉烟火小摊卫生问题']
  }
}

function readSessions() {
  const raw = localStorage.getItem(storageKey)
  if (!raw) {
    return []
  }
  try {
    const stored = JSON.parse(raw) as AgentSession[]
    return stored.filter((item) => Date.now() - item.updatedAt < ttl)
  } catch {
    return []
  }
}

function cardTitle(card: Record<string, unknown>) {
  return toText(card.name, toText(card.stallName, toText(card.target, toText(card.topic, '结果'))))
}

function cardLines(card: Record<string, unknown>) {
  const labels: Record<string, string> = {
    vendor: '摊主',
    category: '分类',
    status: '状态',
    distance: '距离',
    rating: '评分',
    address: '地址',
    products: '商品',
    productName: '商品',
    quantity: '数量',
    pickupTime: '取货时间',
    totalAmount: '金额',
    type: '类型',
    description: '说明',
    content: '内容',
    route: '入口'
  }
  return Object.entries(card)
    .filter(([key]) => !['id', 'orderId', 'complaintId', 'reviewId', 'name', 'stallName', 'target', 'topic', 'orderPayload'].includes(key))
    .slice(0, 8)
    .map(([key, value]) => `${labels[key] ?? key}：${Array.isArray(value) ? value.join('、') : value}`)
}

function formatTime(value: number) {
  const date = new Date(value)
  return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

function toText(value: unknown, fallback: string) {
  return typeof value === 'string' && value.trim() ? value : fallback
}

function strictText(value: unknown) {
  return typeof value === 'string' ? value.trim() : ''
}

function isRecord(value: unknown): value is Record<string, any> {
  return Boolean(value && typeof value === 'object' && !Array.isArray(value))
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
