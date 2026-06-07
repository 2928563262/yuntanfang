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
          <select v-model="target">
            <option v-for="stall in stalls" :key="stall.id">{{ stall.name }}</option>
          </select>
        </div>
        <div class="field-card">
          <label>问题类型</label>
          <select v-model="type">
            <option>卫生问题</option>
            <option>占道经营</option>
            <option>商品质量</option>
            <option>服务态度</option>
          </select>
        </div>
        <div class="field-card">
          <label>问题描述</label>
          <textarea v-model="description" placeholder="请描述问题发生时间、地点、经过"></textarea>
        </div>
        <div class="upload-box">
          <strong>举证材料</strong>
          <span>图片/视频上传占位，后续接入对象存储</span>
        </div>
        <p v-if="error" class="form-error">{{ error }}</p>
        <button class="primary-pill" type="button" @click="submitComplaint">提交工单</button>
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

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { stalls } from '../data/mock'
import { useUserDataStore } from '../stores/userData'

const router = useRouter()
const userData = useUserDataStore()
const target = ref(stalls[0].name)
const type = ref('卫生问题')
const description = ref('')
const error = ref('')

function submitComplaint() {
  error.value = ''
  if (!description.value.trim()) {
    error.value = '请填写问题描述'
    return
  }
  userData.addComplaint({
    target: target.value,
    type: type.value,
    description: description.value
  })
  router.push('/complaints')
}
</script>
