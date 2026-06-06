<template>
  <main class="login">
    <section class="login-panel">
      <h1>云摊坊后台</h1>
      <el-form label-position="top">
        <el-form-item label="账号">
          <el-input model-value="admin" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input model-value="password" type="password" />
        </el-form-item>
        <el-button type="primary" @click="$router.push('/dashboard')">登录</el-button>
      </el-form>
    </section>
  </main>
</template>
