<template>
  <main class="page map-page">
    <van-nav-bar title="附近摊位地图" left-arrow fixed placeholder @click-left="router.back()" />
    <section class="fake-map">
      <div class="map-pin main">深圳北站中心公园</div>
      <div class="map-pin p1">刘记烧烤</div>
      <div class="map-pin p2">小林柠檬茶</div>
      <div class="map-pin p3">老王早食</div>
    </section>
    <section class="map-list">
      <article v-for="stall in demoStalls" :key="stall.id" class="map-stall-card" @click="router.push(`/stalls/${stall.id}`)">
        <strong>{{ stall.name }}</strong>
        <p>{{ stall.address }}</p>
        <span>{{ stall.distance }} · {{ stall.hours }}</span>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { demoStalls } from '../mock/demoData'

const router = useRouter()
</script>
