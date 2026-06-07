<template>
  <main class="page detail-page">
    <section class="shop-cover" :style="{ backgroundImage: `linear-gradient(180deg, rgba(0,0,0,.18), rgba(0,0,0,.48)), url(${stall.cover})` }">
      <div class="shop-top">
        <button type="button" @click="router.back()"><van-icon name="arrow-left" /></button>
        <div>
          <button type="button" @click="router.push('/map')"><van-icon name="location-o" /></button>
          <button type="button" @click="router.push(`/stalls/${stall.id}/cart`)"><van-icon name="cart-o" /></button>
        </div>
      </div>
      <div class="shop-summary">
        <h1>{{ stall.name }}</h1>
        <p><van-tag color="#fff0eb" text-color="#ff6b4a">已备案</van-tag> {{ stall.time }} · {{ stall.distance }} · 人均 ¥{{ stall.avg }}</p>
        <span>{{ stall.announcement }}</span>
      </div>
    </section>

    <van-tabs v-model:active="activeTab" sticky offset-top="0" color="#ff6b4a">
      <van-tab title="点餐">
        <section class="ordering-layout">
          <aside class="product-cats">
            <button v-for="cat in categories" :key="cat" :class="{ active: activeCategory === cat }" type="button" @click="activeCategory = cat">
              {{ cat }}
            </button>
          </aside>
          <div class="product-list">
            <article v-for="product in visibleProducts" :key="product.id" class="product-row">
              <img :src="product.image" :alt="product.name" @click="router.push(`/products/${product.id}`)" />
              <div>
                <h3 @click="router.push(`/products/${product.id}`)">{{ product.name }}</h3>
                <p>{{ product.description }}</p>
                <span>月售 {{ product.sales }}</span>
                <div class="product-buy">
                  <strong>¥{{ product.price }}</strong>
                  <button type="button" @click="handleAdd(product.id)">加入</button>
                </div>
              </div>
            </article>
          </div>
        </section>
      </van-tab>

      <van-tab title="评价">
        <section class="review-panel">
          <div class="rating-card">
            <strong>{{ stall.score }}</strong>
            <span>口味好 · 出摊准时 · 资质清楚</span>
          </div>
          <article v-for="review in reviews" :key="review.id" class="review-card">
            <div>
              <strong>{{ review.user }}</strong>
              <van-rate :model-value="review.rating" readonly size="14" color="#ff6b4a" />
            </div>
            <p>{{ review.content }}</p>
            <span>{{ review.time }}</span>
          </article>
        </section>
      </van-tab>

      <van-tab title="商家">
        <section class="merchant-panel">
          <van-cell title="营业时间" :value="stall.hours" />
          <van-cell title="经营地址" :label="stall.address" is-link @click="router.push('/map')" />
          <van-cell title="商家资质" value="食品备案 · 人员健康证" />
          <van-cell title="店铺公告" :label="stall.announcement" />
          <van-cell title="举报商家" value="演示入口" is-link />
        </section>
      </van-tab>
    </van-tabs>

    <section class="cart-bar">
      <button type="button" class="cart-total" @click="router.push(`/stalls/${stall.id}/cart`)">
        <van-icon name="cart-o" />
        <span>{{ cartCount }} 件 · ¥{{ totalAmount }}</span>
      </button>
      <button type="button" class="checkout-button" @click="router.push('/checkout')">去下单</button>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addToCart, cartDetails, cartTotal, getProductsByStall, getReviewsByStall, getStall } from '../mock/demoData'

const route = useRoute()
const router = useRouter()
const activeTab = ref(0)
const stall = computed(() => getStall(String(route.params.id))).value
const products = getProductsByStall(stall.id)
const reviews = getReviewsByStall(stall.id)
const categories = Array.from(new Set(products.map((product) => product.category)))
const activeCategory = ref(categories[0] ?? '')
const cartVersion = ref(0)

const visibleProducts = computed(() => products.filter((product) => !activeCategory.value || product.category === activeCategory.value))
const cartCount = computed(() => {
  cartVersion.value
  return cartDetails().reduce((sum, item) => sum + item.quantity, 0)
})
const totalAmount = computed(() => {
  cartVersion.value
  return cartTotal()
})

function handleAdd(productId: number) {
  addToCart(productId)
  cartVersion.value += 1
}
</script>
