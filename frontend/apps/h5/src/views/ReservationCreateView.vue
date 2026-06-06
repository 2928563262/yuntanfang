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
          <select>
            <option v-for="product in stall.products" :key="product">{{ product }}</option>
          </select>
        </div>
        <div class="field-card">
          <label>数量</label>
          <input type="number" min="1" value="1" />
        </div>
        <div class="field-card">
          <label>取货时间</label>
          <input value="今天 18:30" />
        </div>
        <div class="field-card">
          <label>联系方式</label>
          <input placeholder="请输入手机号" />
        </div>
        <button class="primary-pill" type="button">提交预约</button>
      </form>

      <aside class="card">
        <h2>订单说明</h2>
        <div class="list-stack">
          <div class="list-card">
            <h3>支付占位</h3>
            <p>当前仅创建预约订单，后续接入 H5 支付、回调、退款和对账。</p>
          </div>
          <div class="list-card">
            <h3>取货提醒</h3>
            <p>可订阅摊主出摊提醒，避免错过营业时间。</p>
          </div>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { stalls } from '../data/mock'

const route = useRoute()
const stall = computed(() => stalls.find((item) => item.id === Number(route.params.id)) ?? stalls[0])
</script>
