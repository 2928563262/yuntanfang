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
            <span class="status-tag">{{ item.status }}</span>
          </div>
          <div class="meta-row">
            <span>¥{{ item.price ?? '-' }}</span>
          </div>
        </article>
        <article v-if="!loading && products.length === 0" class="list-card">
          <h3>还没有商品</h3>
          <p>用右侧表单上架第一个商品。</p>
        </article>
      </div>

      <aside class="card form-list">
        <h2>上架商品</h2>
        <div class="field-card">
          <label>商品名称</label>
          <input v-model="name" placeholder="如：招牌烤串" />
        </div>
        <div class="field-card">
          <label>价格</label>
          <input v-model.number="price" type="number" min="0" />
        </div>
        <p v-if="error" class="form-error">{{ error }}</p>
        <button class="primary-pill" type="button" :disabled="submitting" @click="add">
          {{ submitting ? '提交中…' : '上架商品' }}
        </button>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { vendorApi } from '@yuntanfang/api'

const products = ref<any[]>([])
const loading = ref(false)
const name = ref('')
const price = ref<number | null>(null)
const error = ref('')
const submitting = ref(false)

async function load() {
  loading.value = true
  try {
    const res = await vendorApi.products()
    products.value = res.data.data?.records ?? []
  } catch {
    products.value = []
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
  submitting.value = true
  try {
    await vendorApi.addProduct({ productName: name.value, price: price.value ?? undefined })
    name.value = ''
    price.value = null
    await load()
  } catch (err: any) {
    error.value = err?.response?.data?.message ?? '上架失败，请确认以摊主身份登录'
  } finally {
    submitting.value = false
  }
}

onMounted(load)
</script>
