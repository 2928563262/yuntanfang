<template>
  <RouterView v-if="$route.path === '/login'" />
  <el-container v-else class="layout">
    <el-aside width="236px">
      <div class="brand">云摊坊后台</div>
      <el-menu router :default-active="$route.path">
        <el-menu-item v-for="menu in adminMenus" :key="menu.path" :index="menu.path">
          {{ menu.title }}
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>管理后台</el-breadcrumb-item>
          <el-breadcrumb-item>{{ $route.meta.title }}</el-breadcrumb-item>
        </el-breadcrumb>
        <el-dropdown>
          <span>{{ session?.username ?? '未登录' }}</span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>{{ session?.label ?? '权限占位' }}</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main>
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { clearAuthSession, getAuthSession } from '@yuntanfang/shared'
import { adminMenus } from './router'

const router = useRouter()
const session = ref(getAuthSession())

router.afterEach(() => {
  session.value = getAuthSession()
})

function logout() {
  clearAuthSession()
  session.value = null
  router.push('/login')
}
</script>
