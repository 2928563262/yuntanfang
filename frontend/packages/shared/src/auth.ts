export type AppRole = 'consumer' | 'vendor' | 'admin'

export interface MockAccount {
  username: string
  password: string
  role: AppRole
  label: string
  app: 'h5' | 'admin'
  entry: string
}

export interface AuthSession {
  username: string
  role: AppRole
  label: string
  app: 'h5' | 'admin'
  token: string
}

const AUTH_KEY = 'ytf_mock_session'

export const mockAccounts: MockAccount[] = [
  { username: 'test1', password: '123456', role: 'consumer', label: '普通用户', app: 'h5', entry: '/' },
  { username: 'test2', password: '123456', role: 'vendor', label: '商家', app: 'h5', entry: '/vendor/dashboard' },
  { username: 'test4', password: '123456', role: 'vendor', label: '待审核摊主', app: 'h5', entry: '/vendor/dashboard' },
  { username: 'test3', password: '123456', role: 'admin', label: '管理后台', app: 'admin', entry: '/dashboard' }
]

export function mockLogin(username: string, password: string): AuthSession | null {
  const account = mockAccounts.find((item) => item.username === username.trim() && item.password === password)

  if (!account) {
    return null
  }

  return {
    username: account.username,
    role: account.role,
    label: account.label,
    app: account.app,
    token: `mock-token-${account.username}`
  }
}

export function saveAuthSession(session: AuthSession) {
  localStorage.setItem(AUTH_KEY, JSON.stringify(session))
  localStorage.setItem('ytf_token', session.token)
}

export function getAuthSession(): AuthSession | null {
  const raw = localStorage.getItem(AUTH_KEY)
  if (!raw) {
    return null
  }

  try {
    return JSON.parse(raw) as AuthSession
  } catch {
    clearAuthSession()
    return null
  }
}

export function clearAuthSession() {
  localStorage.removeItem(AUTH_KEY)
  localStorage.removeItem('ytf_token')
}
