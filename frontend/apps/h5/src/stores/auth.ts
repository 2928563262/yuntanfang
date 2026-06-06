import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('ytf_token') ?? '',
    role: 'consumer'
  }),
  actions: {
    setSession(token: string, role: string) {
      this.token = token
      this.role = role
      localStorage.setItem('ytf_token', token)
    }
  }
})
