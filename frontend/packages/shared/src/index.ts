export const roles = ['consumer', 'vendor', 'admin', 'auditor', 'supervisor', 'system_admin'] as const

export type RoleCode = (typeof roles)[number]

export const publicWelfareTags = ['守艺匠人', '戎创达人', '青春创客', '乡野新农人', '微光创业者']

export * from './auth'
