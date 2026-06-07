<template>
  <main class="page module-page">
    <section class="module-hero profile">
      <div>
        <p>个人中心</p>
        <h1>钱包、收藏和客服集中入口</h1>
        <span>订单、评价、投诉和设置先做静态演示入口。</span>
      </div>
      <button type="button" @click="router.push('/messages')">消息</button>
    </section>
    <section class="profile-board">
      <div class="profile-card">
        <img src="https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=160&q=80" alt="用户头像" />
        <div>
          <h2>用户名称</h2>
          <span>已认证 · 本月下单 {{ orderCount }} 次</span>
        </div>
        <van-icon name="setting-o" />
      </div>
      <div class="wallet-grid">
        <button type="button" @click="router.push('/orders')"><span>单</span><strong>{{ orderCount }}</strong><small>我的订单</small></button>
        <button type="button"><span>藏</span><strong>8</strong><small>我的收藏</small></button>
        <button type="button"><span>迹</span><strong>16</strong><small>我的足迹</small></button>
        <button type="button"><span>评</span><strong>5</strong><small>我的评价</small></button>
      </div>
      <div class="service-panel">
        <h2>功能服务</h2>
        <div>
          <button v-for="item in services" :key="item.label" type="button" @click="go(item.path)">
            <span class="service-mark">{{ item.mark }}</span>
            <span>{{ item.label }}</span>
          </button>
        </div>
      </div>
    </section>
  </main>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { readOrders } from '../mock/demoData'

const router = useRouter()
const orderCount = readOrders().length
const services = [
  { mark: '单', label: '全部订单', path: '/orders' },
  { mark: '信', label: '消息中心', path: '/messages' },
  { mark: '图', label: '附近地图', path: '/map' },
  { mark: '诉', label: '我的投诉', path: '/complaints' },
  { mark: '客', label: '联系客服', path: '/messages' },
  { mark: '设', label: '系统设置', path: '/profile' }
]

function go(path: string) {
  router.push(path)
}
</script>
