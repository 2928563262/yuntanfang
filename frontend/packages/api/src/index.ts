import axios from 'axios'

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
  timestamp: string
}

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api',
  timeout: 10000
})

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('ytf_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const authApi = {
  login: (payload: { username: string; password: string }) => http.post<ApiResponse<{ token: string; role: string }>>('/auth/login', payload),
  me: () => http.get<ApiResponse<{ id: number; username: string; roles: string[] }>>('/auth/me')
}

export const stallApi = {
  nearby: () => http.get('/stalls/nearby'),
  search: (keyword: string) => http.get('/stalls/search', { params: { keyword } }),
  detail: (id: string | number) => http.get(`/stalls/${id}`)
}

export interface AgentOrderItem {
  name: string
  quantity: number
  note: string
}

export interface AgentOrderResult {
  reply: string
  stallName: string
  items: AgentOrderItem[]
  pickupTime: string
  totalAmount: string
  confidence: string
  status: string
  rawModelOutput: string
}

export const agentApi = {
  parseOrder: (payload: { message: string; history: string[] }) => http.post<ApiResponse<AgentOrderResult>>('/agent/order/parse', payload)
}
