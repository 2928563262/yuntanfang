<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">摊主故事</p>
        <h1 class="page-title">附近烟火动态</h1>
      </div>
      <RouterLink class="ghost-pill" to="/favorites">我的关注</RouterLink>
    </header>

    <section class="story-grid">
      <article v-for="story in vendorStoryFeed" :key="story.id" class="story-card">
        <div class="story-image">{{ story.stall.slice(0, 2) }}</div>
        <div class="story-body">
          <div class="list-card-header">
            <div>
              <h3>{{ story.vendor }}</h3>
              <p>{{ story.stall }} · {{ story.time }}</p>
            </div>
            <span class="status-tag">{{ story.likes }} 赞</span>
          </div>
          <p>{{ story.content }}</p>
          <div class="chip-row">
            <span v-for="tag in story.tags" :key="tag" class="chip">{{ tag }}</span>
            <span class="chip">{{ story.comments }} 评论</span>
          </div>
        </div>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { vendorStoryFeed } from '../data/mock'
</script>
