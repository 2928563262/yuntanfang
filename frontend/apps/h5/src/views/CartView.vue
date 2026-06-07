<template>
  <main class="page cart-page">
    <van-nav-bar title="店铺购物车" left-arrow fixed placeholder @click-left="router.back()" />
    <section v-if="items.length === 0" class="empty-panel">
      <van-empty description="购物车还是空的">
        <van-button round type="primary" size="small" @click="router.push('/')">去逛逛</van-button>
      </van-empty>
    </section>
    <section v-else class="cart-list">
      <article v-for="item in items" :key="item.productId" class="cart-item">
        <img :src="item.product.image" :alt="item.product.name" />
        <div>
          <h3>{{ item.product.name }}</h3>
          <p>{{ getStall(item.stallId).name }}</p>
          <strong>¥{{ item.product.price }}</strong>
        </div>
        <van-stepper :model-value="item.quantity" min="0" theme="round" button-size="24" @update:model-value="(value) => changeQuantity(item.productId, Number(value))" />
      </article>
    </section>
    <section class="cart-bar">
      <button type="button" class="cart-total">合计 ¥{{ total }}</button>
      <button type="button" class="checkout-button" :disabled="items.length === 0" @click="router.push('/checkout')">去结算</button>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { cartDetails, cartTotal, getStall, updateCartQuantity } from '../mock/demoData'

const router = useRouter()
const version = ref(0)
const items = computed(() => {
  version.value
  return cartDetails()
})
const total = computed(() => {
  version.value
  return cartTotal()
})

function changeQuantity(productId: number, quantity: number) {
  updateCartQuantity(productId, quantity)
  version.value += 1
}
</script>
