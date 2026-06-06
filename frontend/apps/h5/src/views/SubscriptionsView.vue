<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">出摊提醒</p>
        <h1 class="page-title">提醒订阅</h1>
      </div>
      <RouterLink class="ghost-pill" to="/stalls">添加订阅</RouterLink>
    </header>

    <section class="section content-grid">
      <div class="list-stack">
        <article v-for="item in userSubscriptions" :key="item.id" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ item.stall }}</h3>
              <p>{{ item.time }} · {{ item.channel }}</p>
            </div>
            <span class="status-tag">{{ item.status }}</span>
          </div>
          <div class="action-grid">
            <button class="ghost-pill">暂停</button>
            <button class="ghost-pill">修改</button>
            <button class="primary-pill">授权通知</button>
          </div>
        </article>
      </div>

      <aside class="card">
        <h2>通知渠道</h2>
        <div class="list-stack">
          <div class="list-card">
            <h3>站内信</h3>
            <p>默认开启，记录在系统通知中。</p>
          </div>
          <div class="list-card">
            <h3>网页通知</h3>
            <p>需要浏览器授权，后续接入 NotificationService。</p>
          </div>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { userSubscriptions } from '../data/mock'
</script>
