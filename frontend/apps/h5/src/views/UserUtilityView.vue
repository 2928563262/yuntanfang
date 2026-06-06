<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">{{ config.eyebrow }}</p>
        <h1 class="page-title">{{ config.title }}</h1>
      </div>
      <button class="primary-pill">{{ config.action }}</button>
    </header>

    <section class="section content-grid">
      <div class="list-stack">
        <article v-for="row in config.rows" :key="row.title" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ row.title }}</h3>
              <p>{{ row.desc }}</p>
            </div>
            <span class="status-tag">{{ row.status }}</span>
          </div>
        </article>
      </div>

      <aside class="card">
        <h2>能力占位</h2>
        <p class="muted">当前页先完成响应式页面骨架和信息结构，后续按接口逐步接入真实数据。</p>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { userUtilityConfigs } from '../data/mock'

const route = useRoute()
const config = computed(() => userUtilityConfigs[route.path] ?? userUtilityConfigs['/settings'])
</script>
