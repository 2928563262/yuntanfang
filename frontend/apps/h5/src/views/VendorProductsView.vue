<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">商品与媒资</p>
        <h1 class="page-title">商品管理</h1>
      </div>
      <button class="primary-pill">上架商品</button>
    </header>

    <section class="section content-grid">
      <div class="list-stack">
        <article v-for="item in vendorProducts" :key="item.name" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ item.name }}</h3>
              <p>{{ item.category }} · 库存 {{ item.stock }}</p>
            </div>
            <span class="status-tag">{{ item.status }}</span>
          </div>
          <div class="meta-row">
            <span>¥{{ item.price }}</span>
            <span>图片/视频/全景图占位</span>
          </div>
        </article>
      </div>

      <aside class="card">
        <h2>审核提示</h2>
        <p class="muted">商品上架后进入内容审核，审核通过后展示在摊位详情和搜索结果中。</p>
        <div class="chip-row">
          <span class="chip">价格</span>
          <span class="chip">品类</span>
          <span class="chip">图片</span>
          <span class="chip">视频</span>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { vendorProducts } from '../data/mock'
</script>
