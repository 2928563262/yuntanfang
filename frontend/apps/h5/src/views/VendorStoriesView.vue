<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">摊主故事专区</p>
        <h1 class="page-title">摊主故事</h1>
      </div>
      <button class="primary-pill">新建故事</button>
    </header>

    <section class="section content-grid">
      <div class="card form-list">
        <div class="field-card">
          <label>故事标题</label>
          <input placeholder="例如：凌晨四点的汤底" />
        </div>
        <div class="field-card">
          <label>故事内容</label>
          <textarea placeholder="介绍手艺、公益身份、经营理念或本地文化"></textarea>
        </div>
        <div class="field-card">
          <label>展示标签</label>
          <select>
            <option>守艺匠人</option>
            <option>乡野新农人</option>
            <option>青春创客</option>
            <option>微光创业者</option>
          </select>
        </div>
        <button class="primary-pill" type="button">保存草稿</button>
      </div>

      <aside class="list-stack">
        <article v-for="story in vendorStoryDrafts" :key="story.title" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ story.title }}</h3>
              <p>更新于 {{ story.updatedAt }}</p>
            </div>
            <span class="status-tag">{{ story.status }}</span>
          </div>
        </article>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { vendorStoryDrafts } from '../data/mock'
</script>
