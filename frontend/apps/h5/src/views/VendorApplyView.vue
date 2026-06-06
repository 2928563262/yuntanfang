<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">商家开店</p>
        <h1 class="page-title">摊主入驻申请</h1>
      </div>
      <RouterLink class="ghost-pill" to="/vendor/dashboard">工作台</RouterLink>
    </header>

    <section class="section content-grid">
      <form class="card form-list">
        <div class="field-card">
          <label>手机号</label>
          <input placeholder="请输入手机号" />
        </div>
        <div class="field-card">
          <label>摊位/店铺名称</label>
          <input placeholder="例如：烟火小摊" />
        </div>
        <div class="field-card">
          <label>经营品类</label>
          <select>
            <option>地方特色</option>
            <option>传统小吃</option>
            <option>手工文创</option>
            <option>农家特产</option>
          </select>
        </div>
        <div class="field-card">
          <label>摊主故事</label>
          <textarea placeholder="介绍经营特色、公益身份或非遗故事"></textarea>
        </div>
        <button class="primary-pill" type="button">提交申请</button>
      </form>

      <aside class="card">
        <h2>入驻流程</h2>
        <div class="list-stack">
          <div v-for="step in steps" :key="step" class="list-card">
            <h3>{{ step }}</h3>
            <p>当前阶段先保留表单和状态骨架，后续接入资质上传与审核接口。</p>
          </div>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
const steps = ['选择品类', '填写门店信息', '提交身份信息', '上传资质材料', '签订合同']
</script>
