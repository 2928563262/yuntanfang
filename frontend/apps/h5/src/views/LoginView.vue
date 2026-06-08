<template>
  <main class="login-page">
    <section class="login-cover">
      <div class="login-brand">
        <span class="brand-mark">云</span>
        <div>
          <strong>云摊坊</strong>
          <small>智慧摊务平台</small>
        </div>
      </div>
      <p class="eyebrow">账号登录</p>
      <h1>选择身份进入云摊坊</h1>
      <p>使用测试账号进入不同端，先完成前端页面区分，后续接正式登录和权限系统。</p>
    </section>

    <section class="login-workbench">
      <form class="card form-list login-form" @submit.prevent="login">
        <div class="field-card">
          <label>账号</label>
          <input v-model="username" autocomplete="username" placeholder="test1 / test2 / test3" />
        </div>
        <div class="field-card">
          <label>密码</label>
          <input v-model="password" autocomplete="current-password" placeholder="123456" type="password" />
        </div>
        <p v-if="errorMessage" class="form-error">{{ errorMessage }}</p>
        <button class="primary-pill" type="submit">登录</button>
        <p class="muted">test1 为普通用户，test2 为商家，test3 为管理后台账号。</p>
      </form>

      <aside class="card login-accounts">
        <h2>测试账号</h2>
        <div class="list-stack">
          <button v-for="account in mockAccounts" :key="account.username" class="list-card account-card" type="button" @click="fillAccount(account)">
            <div class="list-card-header">
              <div>
                <h3>{{ account.username }}</h3>
                <p>{{ account.label }} · 密码 123456</p>
              </div>
              <span class="status-tag">{{ account.app === 'admin' ? '管理端' : 'H5' }}</span>
            </div>
          </button>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { authApi } from '@yuntanfang/api'
import { mockAccounts, saveAuthSession, type AppRole, type AuthSession, type MockAccount } from '@yuntanfang/shared'

const router = useRouter()
const route = useRoute()
const username = ref('test1')
const password = ref('123456')
const errorMessage = ref('')
const loading = ref(false)

function fillAccount(account: MockAccount) {
  username.value = account.username
  password.value = account.password
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
    const role = data.role as AppRole

    if (role === 'admin') {
      errorMessage.value = 'test3 是管理后台账号，请在管理端(5174)登录'
      return
    }

    const session: AuthSession = {
      username: data.username ?? username.value.trim(),
      role,
      label: role === 'vendor' ? '商家' : '普通用户',
      app: 'h5',
      token: data.token
    }
    saveAuthSession(session)

    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : ''
    router.push(redirect || (role === 'vendor' ? '/vendor/dashboard' : '/'))
  } catch (err: any) {
    errorMessage.value = err?.response?.data?.message ?? '登录失败，请确认后端服务已启动'
  } finally {
    loading.value = false
  }
}
</script>
