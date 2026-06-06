<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">收藏与关注</p>
        <h1 class="page-title">我的收藏</h1>
      </div>
      <RouterLink class="ghost-pill" to="/stalls">去逛逛</RouterLink>
    </header>

    <section class="section chip-row">
      <button v-for="tab in tabs" :key="tab" class="chip" :class="{ active: activeTab === tab }" @click="activeTab = tab">{{ tab }}</button>
    </section>

    <section class="section list-stack">
      <article v-for="item in filteredItems" :key="item.id" class="list-card">
        <div class="list-card-header">
          <div>
            <h3>{{ item.name }}</h3>
            <p>{{ item.desc }}</p>
          </div>
          <span class="status-tag">{{ item.status }}</span>
        </div>
        <div class="meta-row">
          <span>{{ item.type }}</span>
          <span>可取消收藏/关注</span>
        </div>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { userFavorites } from '../data/mock'

const tabs = ['全部', '摊位', '商品', '摊主']
const activeTab = ref('全部')
const filteredItems = computed(() => userFavorites.filter((item) => activeTab.value === '全部' || item.type === activeTab.value))
</script>
