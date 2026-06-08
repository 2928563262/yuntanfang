<template>
  <main class="login">
    <section class="login-panel">
      <p class="eyebrow">管理端登录</p>
      <h1>云摊坊后台</h1>
      <p class="login-copy">当前阶段使用测试账号进入后台，后续替换为正式 RBAC 登录。</p>

      <el-form label-position="top" @submit.prevent>
        <el-form-item label="账号">
          <el-input v-model="username" autocomplete="username" placeholder="test3" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="password" autocomplete="current-password" placeholder="123456" show-password type="password" />
        </el-form-item>
        <el-alert v-if="errorMessage" :title="errorMessage" type="error" show-icon :closable="false" />
        <el-button class="login-button" type="primary" @click="login">登录后台</el-button>
      </el-form>

      <button class="admin-test-account" type="button" @click="fillAdmin">
        test3 / 123456 / 管理后台
      </button>
    </section>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '@yuntanfang/api'
import { saveAuthSession, type AuthSession } from '@yuntanfang/shared'

const router = useRouter()
const username = ref('test3')
const password = ref('123456')
const errorMessage = ref('')
const loading = ref(false)

function fillAdmin() {
  username.value = 'test3'
  password.value = '123456'
  errorMessage.value = ''
}

async function login() {
  if (loading.value) {
    return
  }
  errorMessage.value = ''
  loading.value = true
  try {
    const res = await authApi.login({ username: username.value.trim(), password: password.value })
    const data = res.data.data

    if (data.role !== 'admin') {
      errorMessage.value = '请使用管理后台账号 test3 / 123456'
      return
    }

    const session: AuthSession = {
      username: data.username ?? username.value.trim(),
      role: 'admin',
      label: '管理后台',
      app: 'admin',
      token: data.token
    }
    saveAuthSession(session)
    router.push('/dashboard')
  } catch (err: any) {
    errorMessage.value = err?.response?.data?.message ?? '登录失败，请确认后端服务已启动'
  } finally {
    loading.value = false
  }
}
</script>
