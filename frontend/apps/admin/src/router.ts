import { createRouter, createWebHistory } from 'vue-router'
import LoginView from './views/LoginView.vue'
import WorkbenchView from './views/WorkbenchView.vue'

export const adminMenus = [
  { path: '/dashboard', title: '数据总览' },
  { path: '/vendors/audits', title: '入驻审核' },
  { path: '/qualifications', title: '资质审核' },
  { path: '/special-identities', title: '特殊身份/公益标签审核' },
  { path: '/areas', title: '经营区域管理' },
  { path: '/stalls', title: '摊位管理' },
  { path: '/stall-reservations', title: '摊位预约审批' },
  { path: '/content-audits', title: '内容审核' },
  { path: '/complaints', title: '投诉工单' },
  { path: '/violations', title: '违规记录' },
  { path: '/policies', title: '政策公告' },
  { path: '/notices', title: '通知管理' },
  { path: '/users', title: '用户管理' },
  { path: '/roles', title: '角色权限' },
  { path: '/system', title: '系统设置' }
]

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/dashboard' },
    { path: '/login', component: LoginView, meta: { title: '管理端登录' } },
    ...adminMenus.map((menu) => ({ path: menu.path, component: WorkbenchView, meta: { title: menu.title } }))
  ]
})
