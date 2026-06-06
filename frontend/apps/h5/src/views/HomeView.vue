<template>
  <main class="page home">
    <section class="top">
      <div>
        <p class="eyebrow">云摊坊</p>
        <h1>附近摊位与今日推荐</h1>
      </div>
      <van-button size="small" type="primary" to="/login">登录</van-button>
    </section>

    <van-search v-model="keyword" placeholder="搜索摊位、商品、摊主" @search="goSearch" />

    <section class="grid">
      <button v-for="item in zones" :key="item" class="zone">{{ item }}</button>
    </section>

    <section class="list">
      <article v-for="stall in stalls" :key="stall.id" class="stall" @click="$router.push(`/stalls/${stall.id}`)">
        <div>
          <strong>{{ stall.name }}</strong>
          <span>{{ stall.category }}</span>
        </div>
        <van-tag type="success">{{ stall.status }}</van-tag>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const keyword = ref('')
const zones = ['助农专区', '非遗传承', '公益摊位', '烧烤炸串', '甜点饮品', '手工文创']
const stalls = [
  { id: 1, name: '烟火小摊', category: '地方特色', status: '营业中' },
  { id: 2, name: '乡野新农人鲜铺', category: '农家特产', status: '今日推荐' }
]

function goSearch() {
  router.push({ path: '/stalls', query: { keyword: keyword.value } })
}
</script>
