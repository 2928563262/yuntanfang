<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">资质与公益身份</p>
        <h1 class="page-title">资质管理</h1>
      </div>
      <RouterLink class="ghost-pill" to="/vendor/apply">入驻申请</RouterLink>
    </header>

    <section class="section content-grid">
      <div class="list-stack">
        <h2>资质材料</h2>
        <article v-for="item in qualifications" :key="`q-${item.id}`" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ qualificationLabel(item.qualificationType) }}</h3>
              <p>{{ item.mediaUrl || '未上传材料地址' }}</p>
            </div>
            <span class="status-tag" :class="statusClass(item.status)">{{ statusLabel(item.status) }}</span>
          </div>
          <p v-if="item.status === 'rejected' && item.rejectReason" class="form-error">驳回原因：{{ item.rejectReason }}</p>
        </article>

        <h2>公益 / 特殊身份</h2>
        <article v-for="item in identities" :key="`s-${item.id}`" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ identityLabel(item.identityType) }}</h3>
              <p v-if="item.displayOnFront">已在用户端展示公益标签</p>
              <p v-else>认证后将在摊位展示公益标签</p>
            </div>
            <span class="status-tag" :class="statusClass(item.status)">{{ statusLabel(item.status) }}</span>
          </div>
          <p v-if="item.status === 'rejected' && item.rejectReason" class="form-error">驳回原因：{{ item.rejectReason }}</p>
        </article>

        <article v-if="!loading && qualifications.length === 0 && identities.length === 0" class="list-card">
          <h3>还没有提交认证</h3>
          <p>用右侧表单上传资质材料或申请公益身份。</p>
        </article>
      </div>

      <aside class="card form-list">
        <h2>上传资质</h2>
        <div class="field-card">
          <label>资质类型</label>
          <select v-model="qualType">
            <option value="health_certificate">健康证</option>
            <option value="stall_photo">摊车照片</option>
            <option value="venue_photo">经营场所照片</option>
            <option value="id_card">身份证明</option>
          </select>
        </div>
        <div class="field-card">
          <label>材料地址 / 图片链接</label>
          <input v-model="qualUrl" placeholder="粘贴已上传的图片地址" />
        </div>
        <p v-if="qualError" class="form-error">{{ qualError }}</p>
        <button class="primary-pill" type="button" :disabled="submittingQual" @click="addQual">
          {{ submittingQual ? '提交中…' : '提交资质' }}
        </button>

        <h2 style="margin-top:20px">申请公益身份</h2>
        <div class="field-card">
          <label>公益标签</label>
          <select v-model="identityType">
            <option value="craft_guardian">守艺匠人</option>
            <option value="veteran_creator">戎创达人</option>
            <option value="youth_creator">青春创客</option>
            <option value="rural_farmer">乡野新农人</option>
            <option value="light_entrepreneur">微光创业者</option>
          </select>
        </div>
        <p v-if="identityError" class="form-error">{{ identityError }}</p>
        <button class="ghost-pill" type="button" :disabled="submittingIdentity" @click="addIdentity">
          {{ submittingIdentity ? '提交中…' : '申请公益认证' }}
        </button>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { vendorApi } from '@yuntanfang/api'

const qualifications = ref<any[]>([])
const identities = ref<any[]>([])
const loading = ref(false)

const qualType = ref('health_certificate')
const qualUrl = ref('')
const qualError = ref('')
const submittingQual = ref(false)

const identityType = ref('craft_guardian')
const identityError = ref('')
const submittingIdentity = ref(false)

const qualNames: Record<string, string> = {
  health_certificate: '健康证',
  stall_photo: '摊车照片',
  venue_photo: '经营场所照片',
  id_card: '身份证明'
}
const identityNames: Record<string, string> = {
  craft_guardian: '守艺匠人',
  veteran_creator: '戎创达人',
  youth_creator: '青春创客',
  rural_farmer: '乡野新农人',
  light_entrepreneur: '微光创业者',
  public_welfare: '公益身份'
}
function qualificationLabel(t: string) { return qualNames[t] ?? t }
function identityLabel(t: string) { return identityNames[t] ?? t }
function statusLabel(status: string) {
  return status === 'approved' ? '已通过' : status === 'rejected' ? '已驳回' : '待审核'
}
function statusClass(status: string) {
  return status === 'approved' ? 'tag-success' : status === 'rejected' ? 'tag-danger' : 'tag-pending'
}

async function load() {
  loading.value = true
  try {
    const [q, s] = await Promise.all([vendorApi.qualifications(), vendorApi.specialIdentities()])
    qualifications.value = q.data.data?.records ?? []
    identities.value = s.data.data?.records ?? []
  } catch {
    qualifications.value = []
    identities.value = []
  } finally {
    loading.value = false
  }
}

async function addQual() {
  qualError.value = ''
  submittingQual.value = true
  try {
    await vendorApi.addQualification({ qualificationType: qualType.value, mediaUrl: qualUrl.value || undefined })
    qualUrl.value = ''
    await load()
  } catch (err: any) {
    qualError.value = err?.response?.data?.message ?? '提交失败，请确认已通过入驻申请'
  } finally {
    submittingQual.value = false
  }
}

async function addIdentity() {
  identityError.value = ''
  submittingIdentity.value = true
  try {
    await vendorApi.addSpecialIdentity({ identityType: identityType.value })
    await load()
  } catch (err: any) {
    identityError.value = err?.response?.data?.message ?? '提交失败，请确认已通过入驻申请'
  } finally {
    submittingIdentity.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.tag-success { background: #e7f7ec; color: #18a058; }
.tag-danger { background: #fdeceb; color: #d03050; }
.tag-pending { background: #fff4e5; color: #f0883a; }
</style>
