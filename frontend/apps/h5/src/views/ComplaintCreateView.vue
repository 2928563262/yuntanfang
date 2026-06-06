<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">投诉举报</p>
        <h1 class="page-title">提交投诉</h1>
      </div>
      <RouterLink class="ghost-pill" to="/complaints">处理进度</RouterLink>
    </header>

    <section class="section content-grid">
      <form class="card form-list">
        <div class="field-card">
          <label>投诉对象</label>
          <select>
            <option>烟火小摊</option>
            <option>乡野新农人鲜铺</option>
            <option>守艺糖画铺</option>
          </select>
        </div>
        <div class="field-card">
          <label>问题类型</label>
          <select>
            <option>卫生问题</option>
            <option>占道经营</option>
            <option>商品质量</option>
            <option>服务态度</option>
          </select>
        </div>
        <div class="field-card">
          <label>问题描述</label>
          <textarea placeholder="请描述问题发生时间、地点、经过"></textarea>
        </div>
        <div class="upload-box">
          <strong>举证材料</strong>
          <span>图片/视频上传占位，后续接入对象存储</span>
        </div>
        <button class="primary-pill" type="button">提交工单</button>
      </form>

      <aside class="card">
        <h2>处理规则</h2>
        <div class="list-stack">
          <div class="list-card">
            <h3>24 小时响应</h3>
            <p>投诉超时未处理将触发后台告警。</p>
          </div>
          <div class="list-card">
            <h3>监管闭环</h3>
            <p>工单可分派监管人员，记录处理意见和反馈。</p>
          </div>
        </div>
      </aside>
    </section>
  </main>
</template>
