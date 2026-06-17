<template>
  <section class="workspace">
    <div class="toolbar">
      <div>
        <p class="eyebrow">{{ config.eyebrow }}</p>
        <h1>{{ config.title }}</h1>
      </div>
      <el-button v-if="!live" type="primary" disabled>{{ config.primaryAction }}</el-button>
      <el-button v-else :loading="loading" @click="reload">刷新</el-button>
    </div>

    <div class="metrics">
      <el-card v-for="metric in displayMetrics" :key="metric.label" shadow="never">
        <span>{{ metric.label }}</span>
        <strong>{{ metric.value }}</strong>
        <small>{{ metric.hint }}</small>
      </el-card>
    </div>

    <!-- 真实审核队列：入驻 / 资质 / 公益标签 / 摊位预约 -->
    <el-card v-if="live" shadow="never" class="table-card">
      <template #header>
        <div class="table-head">
          <strong>{{ config.title }}列表</strong>
          <small class="muted">共 {{ rows.length }} 条 · 待审核 {{ pendingCount }} 条</small>
        </div>
      </template>
      <el-table :data="rows" border v-loading="loading">
        <el-table-column
          v-for="column in live.columns"
          :key="column.prop"
          :prop="column.prop"
          :label="column.label"
          :width="column.width"
          show-overflow-tooltip
        >
          <template v-if="column.type === 'status'" #default="{ row }">
            <el-tag :type="statusMeta(row.status).type" effect="light">{{ statusMeta(row.status).label }}</el-tag>
            <el-tag v-if="row.displayOnFront" type="success" effect="plain" size="small" style="margin-left:6px">前台展示</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openDetail(row)">详情</el-button>
            <el-button
              size="small"
              type="primary"
              :disabled="row.status === 'approved'"
              @click="approve(row)"
            >通过</el-button>
            <el-button
              size="small"
              type="danger"
              plain
              :disabled="row.status === 'rejected'"
              @click="reject(row)"
            >驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && rows.length === 0" description="暂无待处理记录" />
    </el-card>

    <el-card v-if="selectedRow" shadow="never" class="audit-detail table-card">
      <template #header>
        <div class="table-head">
          <strong>{{ detailTitle }}</strong>
          <el-button size="small" @click="closeDetail">关闭</el-button>
        </div>
      </template>
      <div>
        <el-alert
          title="审核顺序：入驻审核 → 资质审核 → 特殊身份/公益标签审核 → 摊位预约审批。摊位预约审批通过后才会释放到用户端。"
          type="info"
          :closable="false"
          show-icon
        />
        <div class="detail-grid">
          <div v-for="item in detailFields(selectedRow)" :key="item.label" class="detail-row">
            <span>{{ item.label }}</span>
            <strong>{{ item.value || '-' }}</strong>
          </div>
        </div>
        <div class="drawer-actions">
          <el-button
            type="primary"
            :disabled="selectedRow.status === 'approved'"
            @click="approveSelected"
          >通过</el-button>
          <el-button
            type="danger"
            plain
            :disabled="selectedRow.status === 'rejected'"
            @click="rejectSelected"
          >驳回</el-button>
        </div>
      </div>
    </el-card>

    <!-- 其余模块：占位（mock 列表，未接入后端） -->
    <template v-else>
      <el-alert
        title="该模块为示例占位数据，尚未接入后端接口；下方操作按钮不可用。已接入的真实审核在「入驻审核 / 资质审核 / 特殊身份·公益标签审核 / 摊位预约审批」四个菜单。"
        type="info"
        :closable="false"
        show-icon
        style="margin-bottom:12px"
      />
      <el-card shadow="never" class="filter-card">
        <el-form inline>
          <el-form-item label="关键词">
            <el-input placeholder="搜索名称、提交人、编号" clearable />
          </el-form-item>
          <el-form-item label="状态">
            <el-select model-value="" placeholder="全部状态" clearable>
              <el-option v-for="filter in config.filters" :key="filter" :label="filter" :value="filter" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间">
            <el-date-picker type="daterange" start-placeholder="开始日期" end-placeholder="结束日期" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary">查询</el-button>
            <el-button>重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-card shadow="never" class="table-card">
        <template #header>
          <div class="table-head">
            <strong>{{ config.title }}列表</strong>
            <div>
              <el-button size="small" disabled>导出</el-button>
              <el-button size="small" type="primary" disabled>批量处理</el-button>
            </div>
          </div>
        </template>
        <el-table :data="config.rows" border>
          <el-table-column v-for="column in config.columns" :key="column.prop" :prop="column.prop" :label="column.label" :width="column.width" />
          <el-table-column label="操作" width="210" fixed="right">
            <template #default>
              <el-button size="small" disabled>详情</el-button>
              <el-button size="small" type="primary" disabled>处理</el-button>
              <el-button size="small" disabled>记录</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-row">
          <el-pagination layout="prev, pager, next" :total="80" />
        </div>
      </el-card>

      <el-card v-if="config.module === 'dashboard'" shadow="never" class="flow-card">
        <template #header>待办流程</template>
        <div class="flow-grid">
          <div v-for="item in flows" :key="item.title" class="flow-item">
            <span>{{ item.title }}</span>
            <strong>{{ item.count }}</strong>
            <small>{{ item.hint }}</small>
          </div>
        </div>
      </el-card>
    </template>
  </section>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminApi } from '@yuntanfang/api'
import { getAdminModuleConfig } from '../data/mock'

const route = useRoute()
const config = computed(() => getAdminModuleConfig(route.path))

interface LiveColumn {
  prop: string
  label: string
  width?: number
  type?: 'status'
}
interface LiveConfig {
  fetch: () => Promise<any>
  audit: (id: number, status: string, reason?: string) => Promise<any>
  columns: LiveColumn[]
}

const liveConfigs: Record<string, LiveConfig> = {
  '/vendors/audits': {
    fetch: () => adminApi.vendorApplications(),
    audit: (id, status, reason) => adminApi.auditVendor(id, status, reason),
    columns: [
      { prop: 'id', label: '编号', width: 80 },
      { prop: 'vendorName', label: '摊主/店铺', width: 160 },
      { prop: 'story', label: '摊主故事' },
      { prop: 'status', label: '状态', width: 150, type: 'status' },
      { prop: 'rejectReason', label: '驳回原因', width: 160 }
    ]
  },
  '/qualifications': {
    fetch: () => adminApi.qualifications(),
    audit: (id, status, reason) => adminApi.auditQualification(id, status, reason),
    columns: [
      { prop: 'id', label: '编号', width: 80 },
      { prop: 'vendorName', label: '提交摊主', width: 160 },
      { prop: 'qualificationType', label: '资质类型', width: 160 },
      { prop: 'mediaUrl', label: '材料地址' },
      { prop: 'status', label: '状态', width: 150, type: 'status' },
      { prop: 'rejectReason', label: '驳回原因', width: 160 }
    ]
  },
  '/special-identities': {
    fetch: () => adminApi.specialIdentities(),
    audit: (id, status, reason) => adminApi.auditSpecialIdentity(id, status, reason),
    columns: [
      { prop: 'id', label: '编号', width: 80 },
      { prop: 'vendorName', label: '提交摊主', width: 160 },
      { prop: 'identityType', label: '身份类型', width: 160 },
      { prop: 'publicWelfareTagId', label: '公益标签ID', width: 120 },
      { prop: 'status', label: '状态', width: 200, type: 'status' },
      { prop: 'rejectReason', label: '驳回原因', width: 160 }
    ]
  },
  '/stall-reservations': {
    fetch: () => adminApi.reservations(),
    audit: (id, status, reason) => adminApi.auditReservation(id, status, reason),
    columns: [
      { prop: 'id', label: '编号', width: 80 },
      { prop: 'vendorName', label: '预约摊主', width: 160 },
      { prop: 'stallName', label: '摊位', width: 180 },
      { prop: 'stallId', label: '摊位ID', width: 90 },
      { prop: 'status', label: '状态', width: 130, type: 'status' },
      { prop: 'rejectReason', label: '驳回原因', width: 160 }
    ]
  }
}

const live = computed<LiveConfig | null>(() => liveConfigs[route.path] ?? null)
const rows = ref<any[]>([])
const overview = ref<Record<string, number>>({})
const loading = ref(false)
const selectedRow = ref<any | null>(null)
const pendingCount = computed(() => rows.value.filter((r) => r.status === 'pending').length)
const detailTitle = computed(() => `${config.value.title}详情`)
const displayMetrics = computed(() => {
  if (route.path !== '/dashboard') {
    return config.value.metrics
  }
  return [
    { label: '商家数', value: overview.value.vendors ?? '-', hint: '真实商家总量' },
    { label: '用户数', value: overview.value.users ?? '-', hint: '真实用户总量' },
    { label: '摊位数', value: overview.value.stalls ?? '-', hint: '已建摊位总量' },
    { label: '订单数', value: overview.value.orders ?? '-', hint: '真实订单总量' },
    { label: '商品数', value: overview.value.products ?? '-', hint: '真实商品总量' },
    { label: '投诉数', value: overview.value.complaints ?? '-', hint: '真实投诉总量' }
  ]
})

function statusMeta(status: string) {
  switch (status) {
    case 'approved':
      return { type: 'success' as const, label: '已通过' }
    case 'rejected':
      return { type: 'danger' as const, label: '已驳回' }
    default:
      return { type: 'warning' as const, label: '待审核' }
  }
}

function openDetail(row: any) {
  selectedRow.value = row
}

function closeDetail() {
  selectedRow.value = null
}

function approveSelected() {
  if (selectedRow.value) {
    approve(selectedRow.value)
  }
}

function rejectSelected() {
  if (selectedRow.value) {
    reject(selectedRow.value)
  }
}

function detailFields(row: any) {
  return [
    { label: '编号', value: row.id },
    { label: '摊主/商家', value: row.vendorName },
    { label: '状态', value: statusMeta(row.status).label },
    { label: '摊主故事', value: row.story },
    { label: '资质类型', value: row.qualificationType },
    { label: '材料地址', value: row.mediaUrl },
    { label: '身份类型', value: row.identityType },
    { label: '公益标签ID', value: row.publicWelfareTagId },
    { label: '摊位', value: row.stallName },
    { label: '摊位ID', value: row.stallId },
    { label: '审核意见', value: row.auditOpinion },
    { label: '驳回原因', value: row.rejectReason },
    { label: '更新时间', value: row.updatedAt }
  ].filter((item) => item.value !== undefined && item.value !== null && item.value !== '')
}

async function reload() {
  if (route.path === '/dashboard') {
    loading.value = true
    try {
      const res = await adminApi.overview()
      overview.value = res.data.data ?? {}
    } catch (err: any) {
      ElMessage.error(err?.response?.data?.message ?? '加载失败')
      overview.value = {}
    } finally {
      loading.value = false
    }
    return
  }
  const cfg = live.value
  if (!cfg) return
  loading.value = true
  try {
    const res = await cfg.fetch()
    rows.value = res.data.data?.records ?? []
  } catch (err: any) {
    ElMessage.error(err?.response?.data?.message ?? '加载失败')
    rows.value = []
  } finally {
    loading.value = false
  }
}

async function approve(row: any) {
  const cfg = live.value
  if (!cfg) return
  try {
    await cfg.audit(row.id, 'approved')
    ElMessage.success('已通过')
    selectedRow.value = null
    reload()
  } catch (err: any) {
    ElMessage.error(err?.response?.data?.message ?? '操作失败')
  }
}

async function reject(row: any) {
  const cfg = live.value
  if (!cfg) return
  try {
    const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回', {
      confirmButtonText: '确认驳回',
      cancelButtonText: '取消',
      inputPlaceholder: '例如：材料不清晰，请重新上传'
    })
    await cfg.audit(row.id, 'rejected', value)
    ElMessage.success('已驳回')
    selectedRow.value = null
    reload()
  } catch (err: any) {
    if (err === 'cancel' || err === 'close') return
    ElMessage.error(err?.response?.data?.message ?? '操作失败')
  }
}

watch(
  () => route.path,
  () => {
    if (live.value || route.path === '/dashboard') reload()
  },
  { immediate: true }
)

const flows = [
  { title: '商家入驻审核', count: 8, hint: '待线上初审' },
  { title: '资质材料审核', count: 21, hint: '含 6 条需补充' },
  { title: '投诉工单', count: 5, hint: '1 条临近超时' },
  { title: '内容审核', count: 32, hint: '商品与故事' }
]
</script>

<style scoped>
.muted {
  color: #909399;
}
.table-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.audit-detail {
  display: grid;
  gap: 18px;
}
.detail-grid {
  display: grid;
  gap: 10px;
}
.detail-row {
  display: grid;
  grid-template-columns: 96px 1fr;
  gap: 14px;
}
.detail-row span {
  color: #8a5a44;
  font-weight: 700;
}
.detail-row strong {
  color: #273238;
  font-weight: 600;
  line-height: 1.55;
  word-break: break-word;
}
.drawer-actions {
  display: flex;
  gap: 10px;
}
</style>
