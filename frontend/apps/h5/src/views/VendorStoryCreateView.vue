<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">故事发布</p>
        <h1 class="page-title">发布今日摊主动态</h1>
      </div>
      <RouterLink class="ghost-pill" to="/vendor/stories">故事列表</RouterLink>
    </header>

    <section class="section content-grid">
      <form class="card form-list">
        <div class="field-card">
          <label>故事文案</label>
          <textarea placeholder="记录今天的出摊、手艺、备货和顾客故事，500 字以内。"></textarea>
        </div>
        <div class="upload-box">
          <strong>上传 1-9 张图片</strong>
          <span>对象存储接入前先保留上传占位</span>
        </div>
        <div class="field-card">
          <label>关联摊位</label>
          <select>
            <option>默林的广东肠粉早餐店</option>
            <option>北站中心公园东门临时点位</option>
          </select>
        </div>
        <div class="field-card">
          <label>位置展示</label>
          <select>
            <option>展示当前位置</option>
            <option>不展示位置</option>
          </select>
        </div>
        <button class="primary-pill" type="button">提交审核</button>
      </form>

      <aside class="card">
        <h2>发布规则</h2>
        <div class="list-stack">
          <div class="list-card"><h3>图文审核</h3><p>发布后进入内容安全审核，审核通过后展示。</p></div>
          <div class="list-card"><h3>关注推送</h3><p>关注你的用户可收到新动态提醒。</p></div>
        </div>
      </aside>
    </section>
  </main>
</template>
