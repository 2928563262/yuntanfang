<template>
  <main class="page product-detail-page">
    <van-nav-bar title="商品详情" left-arrow fixed placeholder @click-left="router.back()" />
    <img class="product-hero-img" :src="product.image" :alt="product.name" />
    <section class="product-detail-card">
      <div class="product-title-line">
        <h1>{{ product.name }}</h1>
        <van-tag type="danger">月售 {{ product.sales }}</van-tag>
      </div>
      <p>{{ product.description }}</p>
      <div class="price-line">
        <strong>¥{{ product.price }}</strong>
        <span v-if="product.originalPrice">¥{{ product.originalPrice }}</span>
      </div>
    </section>
    <section class="merchant-panel">
      <van-cell title="所属摊位" :value="stall.name" is-link @click="router.push(`/stalls/${stall.id}`)" />
      <van-cell title="预约方式" value="预定自提" />
      <van-cell title="食品提示" label="演示页面不做真实支付，提交后生成本地演示订单。" />
    </section>
    <section class="cart-bar">
      <button type="button" class="cart-total" @click="addOnly">加入购物车</button>
      <button type="button" class="checkout-button" @click="buyNow">立即预约</button>
    </section>
  </main>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { addToCart, getProduct, getStall } from '../mock/demoData'

const route = useRoute()
const router = useRouter()
const product = getProduct(String(route.params.id))
const stall = getStall(product.stallId)

function addOnly() {
  addToCart(product.id)
}

function buyNow() {
  addToCart(product.id)
  router.push('/checkout')
}
</script>
