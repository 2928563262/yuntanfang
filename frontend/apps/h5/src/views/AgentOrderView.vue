<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">智能订单 Agent</p>
        <h1 class="page-title">一句话预约取餐</h1>
      </div>
      <RouterLink class="ghost-pill" to="/orders">我的订单</RouterLink>
    </header>

    <section class="section content-grid">
      <div class="card agent-panel">
        <div class="agent-messages">
          <article v-for="message in agentMessages" :key="message.content" :class="['agent-bubble', `agent-${message.role}`]">
            {{ message.content }}
          </article>
        </div>
        <div class="agent-input">
          <input v-model="draft" placeholder="例如：帮我订烟火小摊的招牌汤粉，两份，18:30 取" />
          <button class="primary-pill" type="button" @click="sendMock">发送</button>
        </div>
      </div>

      <aside class="card">
        <h2>订单确认卡</h2>
        <div class="list-stack">
          <div class="list-card">
            <h3>烟火小摊 · 招牌汤粉 x2</h3>
            <p>预计 18:30 取餐，等待约 8 分钟。</p>
            <div class="meta-row"><span>总价 ¥32.00</span><span>解析置信度 0.92</span></div>
          </div>
          <RouterLink class="primary-pill" to="/stalls/1/reserve">确认预约</RouterLink>
          <RouterLink class="ghost-pill" to="/stalls">降级为商品列表下单</RouterLink>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { agentMessages } from '../data/mock'

const draft = ref('')

function sendMock() {
  draft.value = ''
}
</script>
