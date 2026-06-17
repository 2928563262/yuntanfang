import axios from 'axios'

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
  timestamp: string
}

export interface PageResult<T> {
  total: number
  pageNo: number
  pageSize: number
  records: T[]
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

// 收到未认证时清掉旧 token，避免被过期/假 token 卡住；并跳回登录页
http.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status
    const code = error?.response?.data?.code
    if (status === 401 || code === 401) {
      localStorage.removeItem('ytf_token')
      localStorage.removeItem('ytf_mock_session')
      if (typeof window !== 'undefined' && window.location?.pathname !== '/login') {
        window.location.assign('/login')
      }
    }
    return Promise.reject(error)
  }
)

export interface LoginResult {
  token: string
  tokenType: string
  userId: number
  username: string
  role: string
}

export const authApi = {
  login: (payload: { username: string; password: string }) =>
    http.post<ApiResponse<LoginResult>>('/auth/login', payload),
  register: (payload: { username: string; password: string }) =>
    http.post<ApiResponse<{ userId: number; username: string; role: string }>>('/auth/register', payload),
  me: () => http.get<ApiResponse<{ id: number; username: string; accountType: string; roles: string[] }>>('/auth/me')
}

export const stallApi = {
  nearby: (category?: string, pageNo = 1, pageSize = 20) =>
    http.get<ApiResponse<PageResult<any>>>('/stalls/nearby', { params: { category, pageNo, pageSize } }),
  search: (keyword?: string, category?: string, pageNo = 1, pageSize = 20) =>
    http.get<ApiResponse<PageResult<any>>>('/stalls/search', { params: { keyword, category, pageNo, pageSize } }),
  detail: (id: string | number) => http.get<ApiResponse<any>>(`/stalls/${id}`),
  products: (id: string | number) => http.get<ApiResponse<PageResult<any>>>(`/stalls/${id}/products`),
  reviews: (id: string | number) => http.get<ApiResponse<PageResult<any>>>(`/stalls/${id}/reviews`)
}

export const orderApi = {
  create: (payload: Record<string, any>) => http.post<ApiResponse<any>>('/orders', payload),
  my: (pageNo = 1, pageSize = 50) => http.get<ApiResponse<PageResult<any>>>('/orders/my', { params: { pageNo, pageSize } }),
  detail: (id: string | number) => http.get<ApiResponse<any>>(`/orders/${id}`),
  review: (id: string | number, payload: { rating: number }) => http.post<ApiResponse<any>>(`/orders/${id}/reviews`, payload)
}

export const complaintApi = {
  create: (payload: { vendorId?: number; description: string }) => http.post<ApiResponse<any>>('/complaints', payload),
  my: () => http.get<ApiResponse<PageResult<any>>>('/complaints/my'),
  detail: (id: string | number) => http.get<ApiResponse<any>>(`/complaints/${id}`)
}

export const reviewApi = {
  my: () => http.get<ApiResponse<PageResult<any>>>('/reviews/my')
}

export const interactionApi = {
  follow: (vendorId: number) => http.post<ApiResponse<any>>(`/follows/vendors/${vendorId}`),
  unfollow: (vendorId: number) => http.delete<ApiResponse<any>>(`/follows/vendors/${vendorId}`),
  follows: () => http.get<ApiResponse<PageResult<any>>>('/follows'),
  favorite: (payload: { bizType?: string; bizId: number; bizName?: string }) => http.post<ApiResponse<any>>('/favorites', payload),
  unfavorite: (id: number) => http.delete<ApiResponse<any>>(`/favorites/${id}`),
  favorites: () => http.get<ApiResponse<PageResult<any>>>('/favorites'),
  subscribe: (payload: { vendorId: number; vendorName?: string }) => http.post<ApiResponse<any>>('/subscriptions', payload),
  subscriptions: () => http.get<ApiResponse<PageResult<any>>>('/subscriptions')
}

export const contentApi = {
  policies: () => http.get<ApiResponse<PageResult<any>>>('/policies'),
  notices: () => http.get<ApiResponse<PageResult<any>>>('/notices')
}

export const vendorApi = {
  me: () => http.get<ApiResponse<any>>('/vendor/me'),
  apply: (payload: { vendorName?: string; story?: string }) => http.post<ApiResponse<any>>('/vendor/applications', payload),
  updateMe: (payload: { vendorName?: string; story?: string }) => http.put<ApiResponse<any>>('/vendor/me', payload),
  orders: () => http.get<ApiResponse<PageResult<any>>>('/vendor/orders'),
  updateOrderStatus: (id: string | number, status: string) => http.put<ApiResponse<any>>(`/vendor/orders/${id}/status`, { status }),
  addProduct: (payload: { productName: string; price?: number; categoryId?: number; stallId?: number }) => http.post<ApiResponse<any>>('/vendor/products', payload),
  products: () => http.get<ApiResponse<PageResult<any>>>('/vendor/products'),
  qualifications: () => http.get<ApiResponse<PageResult<any>>>('/vendor/qualifications'),
  addQualification: (payload: { qualificationType: string; mediaUrl?: string }) => http.post<ApiResponse<any>>('/vendor/qualifications', payload),
  specialIdentities: () => http.get<ApiResponse<PageResult<any>>>('/vendor/special-identities'),
  addSpecialIdentity: (payload: { identityType: string }) => http.post<ApiResponse<any>>('/vendor/special-identities', payload),
  reservations: () => http.get<ApiResponse<PageResult<any>>>('/vendor/stall-reservations'),
  reserve: (payload: { stallId: number }) => http.post<ApiResponse<any>>('/vendor/stall-reservations', payload)
}

export const adminApi = {
  overview: () => http.get<ApiResponse<Record<string, number>>>('/admin/dashboard/overview'),
  vendorApplications: () => http.get<ApiResponse<PageResult<any>>>('/admin/vendors/applications'),
  auditVendor: (id: string | number, status: string, reason?: string) => http.put<ApiResponse<any>>(`/admin/vendors/applications/${id}/audit`, { status, reason }),
  qualifications: () => http.get<ApiResponse<PageResult<any>>>('/admin/qualifications'),
  auditQualification: (id: string | number, status: string, reason?: string) => http.put<ApiResponse<any>>(`/admin/qualifications/${id}/audit`, { status, reason }),
  specialIdentities: () => http.get<ApiResponse<PageResult<any>>>('/admin/special-identities'),
  auditSpecialIdentity: (id: string | number, status: string, reason?: string) => http.put<ApiResponse<any>>(`/admin/special-identities/${id}/audit`, { status, reason }),
  reservations: () => http.get<ApiResponse<PageResult<any>>>('/admin/stall-reservations'),
  auditReservation: (id: string | number, status: string, reason?: string) => http.put<ApiResponse<any>>(`/admin/stall-reservations/${id}/audit`, { status, reason }),
  assignComplaint: (id: string | number) => http.put<ApiResponse<any>>(`/admin/complaints/${id}/assign`, {}),
  processComplaint: (id: string | number, status: string) => http.put<ApiResponse<any>>(`/admin/complaints/${id}/process`, { status }),
  createPolicy: (payload: { title: string; content?: string }) => http.post<ApiResponse<any>>('/admin/policies', payload),
  publishPolicy: (id: string | number) => http.post<ApiResponse<any>>(`/admin/policies/${id}/publish`)
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

export interface AgentAction {
  type: 'open_route' | 'create_order' | 'submit_review' | 'submit_complaint' | string
  label: string
  route: string
  payload: Record<string, unknown>
}

export interface AgentChatResult {
  reply: string
  intent: string
  action: AgentAction
  cards: Record<string, unknown>[]
  processSteps: { title: string; status: string; detail: string }[]
  suggestedPrompts: string[]
  status: string
  rawModelOutput: string
}

export const agentApi = {
  chat: (payload: { message: string; history: string[]; context?: Record<string, unknown> }) =>
    http.post<ApiResponse<AgentChatResult>>('/agent/chat', payload),
  parseOrder: (payload: { message: string; history: string[] }) => http.post<ApiResponse<AgentOrderResult>>('/agent/order/parse', payload)
}
