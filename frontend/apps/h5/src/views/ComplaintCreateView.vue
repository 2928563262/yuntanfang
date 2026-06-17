<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">投诉举报</p>
        <h1 class="page-title">提交投诉</h1>
      </div>
      <RouterLink class="ghost-pill" to="/complaints">处理进度</RouterLink>
    </header>

    <section class="section content-grid">
      <form class="card form-list">
        <div class="field-card">
          <label>投诉对象</label>
          <select v-model="targetId">
            <option v-for="stall in stalls" :key="stall.id" :value="stall.vendorId">{{ stall.stallName }}</option>
            <option v-if="prefillVendorId && !stalls.some((stall) => Number(stall.vendorId) === Number(prefillVendorId))" :value="prefillVendorId">
              {{ prefillStallName || `商家 #${prefillVendorId}` }}
            </option>
          </select>
        </div>
        <div class="field-card">
          <label>问题类型</label>
          <select v-model="type">
            <option>卫生问题</option>
            <option>占道经营</option>
            <option>商品质量</option>
            <option>服务态度</option>
          </select>
        </div>
        <div class="field-card">
          <label>问题描述</label>
          <textarea v-model="description" placeholder="请描述问题发生时间、地点、经过"></textarea>
        </div>
        <p v-if="error" class="form-error">{{ error }}</p>
        <button class="primary-pill" type="button" :disabled="submitting" @click="submitComplaint">
          {{ submitting ? '提交中…' : '提交工单' }}
        </button>
      </form>

      <aside class="card">
        <h2>处理规则</h2>
        <div class="list-stack">
          <div v-if="orderId" class="list-card">
            <h3>关联订单</h3>
            <p>订单号 {{ orderId }} · {{ prefillStallName || '订单摊位' }}</p>
          </div>
          <div class="list-card">
            <h3>24 小时响应</h3>
            <p>投诉超时未处理将触发后台告警。</p>
          </div>
          <div class="list-card">
            <h3>监管闭环</h3>
            <p>工单可分派监管人员，记录处理意见和反馈。</p>
          </div>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useRouter } from 'vue-router'
import { complaintApi, stallApi } from '@yuntanfang/api'

const router = useRouter()
const route = useRoute()
const stalls = ref<any[]>([])
const targetId = ref<number | null>(null)
const type = ref('卫生问题')
const description = ref('')
const error = ref('')
const submitting = ref(false)
const orderId = ref(String(route.query.orderId ?? ''))
const prefillVendorId = Number(route.query.vendorId ?? '') || null
const prefillStallName = String(route.query.stallName ?? '')

onMounted(async () => {
  try {
    const res = await stallApi.nearby()
    stalls.value = res.data.data?.records ?? []
    targetId.value = prefillVendorId ?? stalls.value.find((s) => s.vendorId)?.vendorId ?? null
    if (orderId.value && !description.value) {
      description.value = `订单号 ${orderId.value}：`
    }
  } catch {
    stalls.value = []
    targetId.value = prefillVendorId
  }
})

async function submitComplaint() {
  error.value = ''
  if (!description.value.trim()) {
    error.value = '请填写问题描述'
    return
  }
  submitting.value = true
  try {
    await complaintApi.create({
      vendorId: targetId.value ?? undefined,
      description: `[${type.value}] ${description.value}`
    })
    router.push('/complaints')
  } catch (err: any) {
    error.value = err?.response?.data?.message ?? '提交失败，请确认已登录'
  } finally {
    submitting.value = false
  }
}
</script>
