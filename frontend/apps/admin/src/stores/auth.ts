import { defineStore } from 'pinia'

export const useAdminAuthStore = defineStore('adminAuth', {
  state: () => ({
    token: localStorage.getItem('ytf_admin_token') ?? '',
    role: 'system_admin'
  }),
  getters: {
    canAudit: (state) => ['auditor', 'supervisor', 'system_admin'].includes(state.role)
  }
})
