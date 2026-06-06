<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">资质与公益身份</p>
        <h1 class="page-title">资质管理</h1>
      </div>
      <RouterLink class="ghost-pill" to="/vendor/apply">补充资料</RouterLink>
    </header>

    <section class="section content-grid">
      <div class="list-stack">
        <article v-for="item in vendorQualifications" :key="item.title" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ item.title }}</h3>
              <p>{{ item.owner }}</p>
            </div>
            <span class="status-tag">{{ item.status }}</span>
          </div>
          <div class="meta-row">
            <span>有效期：{{ item.expireAt }}</span>
            <span>图片/视频上传占位</span>
          </div>
        </article>
      </div>

      <aside class="card">
        <h2>上传要求</h2>
        <div class="list-stack">
          <div class="list-card">
            <h3>材料清晰</h3>
            <p>健康证、居住证、摊车照片、经营场所照片需清晰可识别。</p>
          </div>
          <div class="list-card">
            <h3>公益标签</h3>
            <p>守艺匠人、戎创达人、青春创客、乡野新农人、微光创业者。</p>
          </div>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { vendorQualifications } from '../data/mock'
</script>
