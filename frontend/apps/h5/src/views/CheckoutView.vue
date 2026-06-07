<template>
  <main class="page checkout-page">
    <van-nav-bar title="提交订单" left-arrow fixed placeholder @click-left="router.back()" />
    <section class="checkout-card">
      <h2>预定自提信息</h2>
      <van-field v-model="pickupTime" label="取餐时间" placeholder="例如 今天 20:10" />
      <van-field v-model="remark" label="备注" placeholder="口味、餐具等要求" />
    </section>
    <section class="checkout-card">
      <h2>商品清单</h2>
      <article v-for="item in items" :key="item.productId" class="checkout-item">
        <span>{{ item.product.name }} ×{{ item.quantity }}</span>
        <strong>¥{{ item.product.price * item.quantity }}</strong>
      </article>
    </section>
    <section class="cart-bar">
      <button type="button" class="cart-total">合计 ¥{{ total }}</button>
      <button type="button" class="checkout-button" @click="submitOrder">提交订单</button>
    </section>
  </main>
</template>

<script setup lang="ts">
import { orderApi } from '@yuntanfang/api'
import { showToast } from 'vant'
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { cartDetails, cartTotal, createDemoOrder } from '../mock/demoData'

const router = useRouter()
const pickupTime = ref('今天 20:10')
const remark = ref('')
const items = computed(() => cartDetails())
const total = computed(() => cartTotal())

async function submitOrder() {
  if (items.value.length === 0) {
    showToast('请先加入商品')
    router.push('/')
    return
  }
  try {
    await orderApi.create({ pickupTime: pickupTime.value, remark: remark.value, items: items.value })
  } catch {
    createDemoOrder({ pickupTime: pickupTime.value, remark: remark.value })
  }
  showToast('订单已提交')
  router.push('/orders')
}
</script>
