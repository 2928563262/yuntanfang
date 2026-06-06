<template>
  <main class="page login-page">
    <section class="hero-card">
      <p class="eyebrow">账号登录</p>
      <h1>选择身份进入云摊坊</h1>
      <p>当前阶段使用测试账号区分端，后续再接正式验证码、权限和后端登录接口。</p>
    </section>

    <section class="section content-grid">
      <form class="card form-list" @submit.prevent="login">
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

      <aside class="card">
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
import { mockAccounts, mockLogin, saveAuthSession, type MockAccount } from '@yuntanfang/shared'

const router = useRouter()
const route = useRoute()
const username = ref('test1')
const password = ref('123456')
const errorMessage = ref('')

function fillAccount(account: MockAccount) {
  username.value = account.username
  password.value = account.password
  errorMessage.value = ''
}

function login() {
  const session = mockLogin(username.value, password.value)

  if (!session) {
    errorMessage.value = '账号或密码错误'
    return
  }

  if (session.app === 'admin') {
    errorMessage.value = 'test3 是管理后台账号，请在管理端登录'
    return
  }

  saveAuthSession(session)
  const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : ''
  router.push(redirect || (session.role === 'vendor' ? '/vendor/dashboard' : '/'))
}
</script>
