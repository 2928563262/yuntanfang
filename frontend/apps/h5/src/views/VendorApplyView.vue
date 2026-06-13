<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">商家开店</p>
        <h1 class="page-title">摊主入驻申请</h1>
      </div>
      <RouterLink class="ghost-pill" to="/vendor/dashboard">工作台</RouterLink>
    </header>

    <section class="section content-grid">
      <!-- 已提交：展示入驻审核状态 -->
      <div v-if="vendor" class="card form-list">
        <div class="list-card-header">
          <div>
            <h2>{{ vendor.vendorName }}</h2>
            <p>入驻审核状态</p>
          </div>
          <span class="status-tag" :class="statusClass(vendor.status)">{{ statusLabel(vendor.status) }}</span>
        </div>
        <p v-if="vendor.status === 'rejected' && vendor.rejectReason" class="form-error">
          驳回原因：{{ vendor.rejectReason }}
        </p>
        <div class="field-card">
          <label>摊主故事</label>
          <textarea v-model="story" placeholder="介绍经营特色、公益身份或非遗故事"></textarea>
        </div>
        <p v-if="message" class="form-hint">{{ message }}</p>
        <button class="primary-pill" type="button" :disabled="submitting" @click="updateStory">
          {{ submitting ? '保存中…' : '更新摊主故事' }}
        </button>
        <div class="next-steps">
          <RouterLink class="ghost-pill" to="/vendor/qualifications">资质 / 公益认证</RouterLink>
          <RouterLink class="ghost-pill" to="/vendor/stalls">摊位预约</RouterLink>
        </div>
      </div>

      <!-- 加载失败:不要把已入驻摊主误导成重新申请 -->
      <div v-else-if="loadError" class="card form-list">
        <h2>加载失败</h2>
        <p class="form-error">{{ loadError }}</p>
        <button class="primary-pill" type="button" @click="load">重试</button>
      </div>

      <!-- 未提交：填写入驻申请 -->
      <form v-else class="card form-list" @submit.prevent="submit">
        <div class="field-card">
          <label>摊位/店铺名称</label>
          <input v-model="vendorName" placeholder="例如：烟火小摊" />
        </div>
        <div class="field-card">
          <label>经营品类</label>
          <select v-model="category">
            <option>地方特色</option>
            <option>传统小吃</option>
            <option>手工文创</option>
            <option>农家特产</option>
          </select>
        </div>
        <div class="field-card">
          <label>摊主故事</label>
          <textarea v-model="story" placeholder="介绍经营特色、公益身份或非遗故事"></textarea>
        </div>
        <p v-if="error" class="form-error">{{ error }}</p>
        <button class="primary-pill" type="submit" :disabled="submitting">
          {{ submitting ? '提交中…' : '提交入驻申请' }}
        </button>
      </form>

      <aside class="card">
        <h2>入驻流程</h2>
        <div class="list-stack">
          <div v-for="(step, i) in steps" :key="step" class="list-card">
            <h3>{{ i + 1 }}. {{ step }}</h3>
            <p>{{ stepHints[i] }}</p>
          </div>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { vendorApi } from '@yuntanfang/api'

const steps = ['提交入驻申请', '上传资质材料', '公益身份认证', '摊位预约', '后台审核与释放']
const stepHints = [
  '填写店铺名称、经营品类与摊主故事。',
  '健康证、摊车照片、经营场所照片等。',
  '守艺匠人、青春创客等公益标签（可选）。',
  '选择摊位发起预约，等待审批。',
  '四项审核通过后，摊位即在用户端展示、可搜索、可预约。'
]

const vendor = ref<any | null>(null)
const vendorName = ref('')
const category = ref('地方特色')
const story = ref('')
const error = ref('')
const message = ref('')
const loadError = ref('')
const submitting = ref(false)

function statusLabel(status: string) {
  return status === 'approved' ? '已通过' : status === 'rejected' ? '已驳回' : '待审核'
}
function statusClass(status: string) {
  return status === 'approved' ? 'tag-success' : status === 'rejected' ? 'tag-danger' : 'tag-pending'
}

async function load() {
  loadError.value = ''
  try {
    const res = await vendorApi.me()
    vendor.value = res.data.data
    story.value = vendor.value?.story ?? ''
  } catch (err: any) {
    const status = err?.response?.status
    const code = err?.response?.data?.code
    const msg = err?.response?.data?.message ?? ''
    if (status === 401 || code === 401) {
      // 未登录：拦截器会跳转登录页，这里不处理
      return
    }
    if (code === 4001 && (msg.includes('入驻') || msg.includes('摊主'))) {
      // 真·未入驻 → 展示申请表单
      vendor.value = null
    } else {
      // 网络/服务器错误：不要置空诱导重新申请
      loadError.value = '加载失败，请重试'
    }
  }
}

async function submit() {
  error.value = ''
  if (!vendorName.value.trim()) {
    error.value = '请填写摊位/店铺名称'
    return
  }
  submitting.value = true
  try {
    await vendorApi.apply({ vendorName: vendorName.value, story: story.value })
    await load()
  } catch (err: any) {
    error.value = err?.response?.data?.message ?? '提交失败，请确认已登录'
  } finally {
    submitting.value = false
  }
}

async function updateStory() {
  message.value = ''
  submitting.value = true
  try {
    await vendorApi.updateMe({ story: story.value })
    message.value = '已保存'
  } catch (err: any) {
    message.value = err?.response?.data?.message ?? '保存失败'
  } finally {
    submitting.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.tag-success { background: #e7f7ec; color: #18a058; }
.tag-danger { background: #fdeceb; color: #d03050; }
.tag-pending { background: #fff4e5; color: #f0883a; }
.next-steps { display: flex; gap: 12px; margin-top: 12px; }
.form-hint { color: #18a058; margin: 8px 0; }
</style>
