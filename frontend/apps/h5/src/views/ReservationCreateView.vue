<template>
  <main class="page">
    <header class="page-header">
      <button class="ghost-pill" @click="$router.back()">返回</button>
      <RouterLink class="ghost-pill" to="/orders">我的订单</RouterLink>
    </header>

    <section v-if="notFound" class="hero-card">
      <p class="eyebrow">预约下单</p>
      <h1>摊位不可预约</h1>
      <p>该摊位不存在或尚未通过审核释放。</p>
    </section>

    <template v-else>
      <section class="hero-card">
        <p class="eyebrow">预约下单</p>
        <h1>{{ stall.stallName }}</h1>
        <p>{{ stall.address || '位置待完善' }} · {{ statusText(stall.businessStatus) }}</p>
      </section>

      <section class="section content-grid">
        <form class="card form-list">
          <div class="field-card">
            <label>选择商品</label>
            <select v-model="selectedProductId">
              <option v-for="p in products" :key="p.id" :value="p.id">{{ p.productName }}（¥{{ p.price ?? '-' }}）</option>
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
          <button class="primary-pill" type="button" :disabled="submitting || products.length === 0" @click="submitOrder">
            {{ submitting ? '提交中…' : '提交预约' }}
          </button>
        </form>

        <aside class="card">
          <h2>订单确认</h2>
          <div class="list-stack">
            <div class="list-card">
              <h3>{{ selectedProduct?.productName ?? '请选择商品' }} x{{ quantity }}</h3>
              <p>{{ stall.stallName }} · {{ pickupTime }}</p>
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
    </template>
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { orderApi, stallApi } from '@yuntanfang/api'

const route = useRoute()
const router = useRouter()
const stall = ref<any>({})
const products = ref<any[]>([])
const notFound = ref(false)
const selectedProductId = ref<number | null>(null)
const quantity = ref(1)
const pickupTime = ref('今天 18:30')
const contact = ref('')
const error = ref('')
const submitting = ref(false)

function statusText(s: string) {
  return s === 'open' ? '营业中' : s === 'closed' ? '休息中' : '暂未营业'
}

const selectedProduct = computed(() => products.value.find((p) => p.id === selectedProductId.value) ?? null)
const totalAmount = computed(() => {
  const price = Number(selectedProduct.value?.price ?? 0)
  return (price * quantity.value).toFixed(2)
})

onMounted(async () => {
  const id = route.params.id as string
  try {
    const d = await stallApi.detail(id)
    stall.value = d.data.data ?? {}
    const p = await stallApi.products(id)
    products.value = p.data.data?.records ?? []
    selectedProductId.value = products.value[0]?.id ?? null
  } catch {
    notFound.value = true
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
      stallId: Number(route.params.id),
      pickupTime: pickupTime.value,
      contactPhone: contact.value,
      items: [
        {
          productId: selectedProduct.value.id,
          productName: selectedProduct.value.productName,
          price: selectedProduct.value.price,
          quantity: quantity.value
        }
      ]
    } as any)
    router.push('/orders')
  } catch (err: any) {
    error.value = err?.response?.data?.message ?? '下单失败，请确认已登录'
  } finally {
    submitting.value = false
  }
}
</script>
