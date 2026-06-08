<template>
  <main class="page">
    <header class="page-header">
      <button class="ghost-pill" @click="$router.back()">返回</button>
      <RouterLink class="ghost-pill" to="/orders">我的订单</RouterLink>
    </header>

    <section class="hero-card">
      <p class="eyebrow">预约下单</p>
      <h1>{{ stall.name }}</h1>
      <p>{{ stall.address }} · {{ stall.status }}</p>
    </section>

    <section class="section content-grid">
      <form class="card form-list">
        <div class="field-card">
          <label>选择商品</label>
          <select v-model="selectedProduct">
            <option v-for="product in stall.products" :key="product">{{ product }}</option>
          </select>
        </div>
        <div class="field-card">
          <label>数量</label>
          <input v-model.number="quantity" type="number" min="1" />
        </div>
        <div class="field-card">
          <label>取货时间</label>
          <input v-model="pickupTime" />
        </div>
        <div class="field-card">
          <label>联系方式</label>
          <input v-model="contact" placeholder="请输入手机号" />
        </div>
        <p v-if="error" class="form-error">{{ error }}</p>
        <button class="primary-pill" type="button" :disabled="submitting" @click="submitOrder">
          {{ submitting ? '提交中…' : '提交预约' }}
        </button>
      </form>

      <aside class="card">
        <h2>订单确认</h2>
        <div class="list-stack">
          <div class="list-card">
            <h3>{{ selectedProduct }} x{{ quantity }}</h3>
            <p>{{ stall.name }} · {{ pickupTime }}</p>
            <div class="meta-row">
              <span>预估金额 ¥{{ totalAmount }}</span>
              <span>{{ contact || '待填写联系方式' }}</span>
            </div>
          </div>
          <div class="list-card">
            <h3>提交后</h3>
            <p>订单会真实写入后端，你可在「我的订单」看到，摊主登录后也能在工作台看到这单。</p>
          </div>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watchEffect } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { orderApi, stallApi } from '@yuntanfang/api'
import { stalls } from '../data/mock'

const route = useRoute()
const router = useRouter()
const stall = computed(() => stalls.find((item) => item.id === Number(route.params.id)) ?? stalls[0])
const selectedProduct = ref('')
const quantity = ref(1)
const pickupTime = ref('今天 18:30')
const contact = ref('')
const error = ref('')
const submitting = ref(false)
const unitPrice = 16
const totalAmount = computed(() => (quantity.value * unitPrice).toFixed(2))
const vendorId = ref<number | null>(null)

watchEffect(() => {
  selectedProduct.value ||= stall.value.products[0] ?? ''
})

onMounted(async () => {
  try {
    const res = await stallApi.detail(route.params.id as string)
    vendorId.value = res.data.data?.vendorId ?? null
  } catch {
    vendorId.value = null
  }
})

async function submitOrder() {
  error.value = ''
  if (!selectedProduct.value) {
    error.value = '请选择商品'
    return
  }
  if (quantity.value < 1) {
    error.value = '数量至少为 1'
    return
  }
  if (!pickupTime.value.trim()) {
    error.value = '请填写取货时间'
    return
  }

  submitting.value = true
  try {
    await orderApi.create({
      vendorId: vendorId.value ?? 1,
      stallId: stall.value.id,
      stallName: stall.value.name,
      pickupTime: pickupTime.value,
      contactPhone: contact.value,
      items: [{ productName: selectedProduct.value, price: unitPrice, quantity: quantity.value }]
    } as any)
    router.push('/orders')
  } catch (err: any) {
    error.value = err?.response?.data?.message ?? '下单失败，请确认已登录'
  } finally {
    submitting.value = false
  }
}
</script>
