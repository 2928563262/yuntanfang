<template>
  <main class="page message-page">
    <van-nav-bar title="消息中心" left-arrow fixed placeholder @click-left="router.back()" />
    <section class="message-cats">
      <button v-for="cat in cats" :key="cat.label" type="button">
        <span>{{ cat.mark }}</span>
        <strong>{{ cat.label }}</strong>
        <small>{{ cat.count }} 条</small>
      </button>
    </section>
    <section class="message-list">
      <article v-for="message in demoMessages" :key="message.id" class="message-card">
        <van-tag color="#fff0eb" text-color="#ff6b4a">{{ message.type }}</van-tag>
        <h3>{{ message.title }}</h3>
        <p>{{ message.text }}</p>
        <span>{{ message.time }}</span>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { demoMessages } from '../mock/demoData'

const router = useRouter()
const cats = [
  { mark: '系', label: '系统通知', count: 3 },
  { mark: '单', label: '订单通知', count: 2 },
  { mark: '活', label: '活动通知', count: 1 }
]
</script>
