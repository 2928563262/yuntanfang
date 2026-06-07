<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">投诉举报</p>
        <h1 class="page-title">我的投诉</h1>
      </div>
      <RouterLink class="primary-pill" to="/complaints/create">提交投诉</RouterLink>
    </header>

    <section class="section list-stack">
      <article v-for="item in userData.complaints.value" :key="item.id" class="list-card">
        <div class="list-card-header">
          <div>
            <h3>{{ item.target }}</h3>
            <p>{{ item.description }}</p>
          </div>
          <span class="status-tag">{{ item.status }}</span>
        </div>
        <div class="meta-row">
          <span>工单 {{ item.id }}</span>
          <span>{{ item.type }}</span>
          <span>{{ item.updatedAt }}</span>
        </div>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { useUserDataStore } from '../stores/userData'

const userData = useUserDataStore()
</script>
