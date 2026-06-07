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
  detail: (id: string | number) => http.get(`/stalls/${id}`),
  products: (id: string | number) => http.get(`/stalls/${id}/products`),
  reviews: (id: string | number) => http.get(`/stalls/${id}/reviews`)
}

export const orderApi = {
  create: (payload: unknown) => http.post('/orders', payload),
  list: () => http.get('/orders'),
  detail: (id: string | number) => http.get(`/orders/${id}`)
}
