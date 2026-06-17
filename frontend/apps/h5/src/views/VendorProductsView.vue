<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">商品与媒资</p>
        <h1 class="page-title">商品管理</h1>
      </div>
    </header>

    <section class="section content-grid">
      <div class="list-stack">
        <article v-for="item in products" :key="item.id" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ item.productName }}</h3>
              <p>{{ item.description ?? '—' }}</p>
            </div>
            <span class="status-tag">{{ productStatus(item.status) }}</span>
          </div>
          <div class="meta-row">
            <span>¥{{ item.price ?? '-' }}</span>
            <span>{{ stallName(item.stallId) }}</span>
          </div>
          <div class="action-grid">
            <button class="ghost-pill" type="button" @click="startEdit(item)">编辑</button>
            <button v-if="item.status !== 'on_sale'" class="ghost-pill" type="button" @click="setProductStatus(item, 'on_sale')">上架</button>
            <button v-if="item.status === 'on_sale'" class="ghost-pill" type="button" @click="setProductStatus(item, 'off_sale')">下架</button>
          </div>
        </article>
        <article v-if="!loading && products.length === 0" class="list-card">
          <h3>还没有商品</h3>
          <p>用右侧表单上架第一个商品。</p>
        </article>
      </div>

      <aside class="card form-list">
        <h2>{{ editingId ? '编辑商品' : '上架商品' }}</h2>
        <div class="field-card">
          <label>所属摊位</label>
          <select v-model.number="stallId">
            <option v-for="stall in approvedStalls" :key="stall.stallId" :value="stall.stallId">
              {{ stall.stallName || `摊位 #${stall.stallId}` }}
            </option>
          </select>
        </div>
        <div class="field-card">
          <label>商品名称</label>
          <input v-model="name" placeholder="如：招牌烤串" />
        </div>
        <div class="field-card">
          <label>商品描述</label>
          <textarea v-model="description" placeholder="口味、规格、取货说明"></textarea>
        </div>
        <div class="field-card">
          <label>价格</label>
          <input v-model.number="price" type="number" min="0" />
        </div>
        <p v-if="error" class="form-error">{{ error }}</p>
        <button class="primary-pill" type="button" :disabled="submitting" @click="add">
          {{ submitting ? '提交中…' : editingId ? '保存修改' : '上架商品' }}
        </button>
        <button v-if="editingId" class="ghost-pill" type="button" @click="resetForm">取消编辑</button>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { vendorApi } from '@yuntanfang/api'

const products = ref<any[]>([])
const reservations = ref<any[]>([])
const loading = ref(false)
const name = ref('')
const price = ref<number | null>(null)
const description = ref('')
const stallId = ref<number | null>(null)
const editingId = ref<number | null>(null)
const error = ref('')
const submitting = ref(false)

const approvedStalls = computed(() => reservations.value.filter((item) => item.status === 'approved' && item.stallId))

function stallName(id: number | null | undefined) {
  const stall = reservations.value.find((item) => Number(item.stallId) === Number(id))
  return stall?.stallName ? `所属摊位：${stall.stallName}` : id ? `摊位 #${id}` : '未绑定摊位'
}

function productStatus(status: string) {
  const map: Record<string, string> = {
    on_sale: '在售',
    off_sale: '已下架',
    draft: '草稿'
  }
  return map[status] ?? status
}

function startEdit(item: any) {
  editingId.value = item.id
  stallId.value = item.stallId
  name.value = item.productName ?? ''
  price.value = item.price == null ? null : Number(item.price)
  description.value = item.description ?? ''
  error.value = ''
}

function resetForm() {
  editingId.value = null
  name.value = ''
  price.value = null
  description.value = ''
  stallId.value = approvedStalls.value[0]?.stallId ?? null
  error.value = ''
}

async function setProductStatus(item: any, status: string) {
  error.value = ''
  try {
    await vendorApi.updateProduct(item.id, { status })
    await load()
  } catch (err: any) {
    error.value = err?.response?.data?.message ?? '状态更新失败'
  }
}

async function load() {
  loading.value = true
  try {
    const [productRes, reservationRes] = await Promise.all([vendorApi.products(), vendorApi.reservations()])
    products.value = productRes.data.data?.records ?? []
    reservations.value = reservationRes.data.data?.records ?? []
    stallId.value = stallId.value ?? approvedStalls.value[0]?.stallId ?? null
  } catch {
    products.value = []
    reservations.value = []
  } finally {
    loading.value = false
  }
}

async function add() {
  error.value = ''
  if (!name.value.trim()) {
    error.value = '请填写商品名称'
    return
  }
  if (!stallId.value) {
    error.value = '请先选择已通过审批并释放的摊位'
    return
  }
  submitting.value = true
  try {
    if (editingId.value) {
      await vendorApi.updateProduct(editingId.value, {
        stallId: stallId.value,
        productName: name.value,
        price: price.value ?? undefined,
        description: description.value,
        status: 'on_sale'
      })
    } else {
      await vendorApi.addProduct({ stallId: stallId.value, productName: name.value, price: price.value ?? undefined })
    }
    resetForm()
    await load()
  } catch (err: any) {
    error.value = err?.response?.data?.message ?? '上架失败，请确认以摊主身份登录'
  } finally {
    submitting.value = false
  }
}

onMounted(load)
</script>
