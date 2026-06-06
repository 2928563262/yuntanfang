export type AdminModule =
  | 'dashboard'
  | 'vendorAudit'
  | 'qualification'
  | 'specialIdentity'
  | 'area'
  | 'stall'
  | 'reservation'
  | 'contentAudit'
  | 'complaint'
  | 'violation'
  | 'policy'
  | 'notice'
  | 'user'
  | 'role'
  | 'system'

export interface AdminMetric {
  label: string
  value: string | number
  hint: string
}

export interface AdminColumn {
  prop: string
  label: string
  width?: number
}

export interface AdminModuleConfig {
  module: AdminModule
  title: string
  eyebrow: string
  primaryAction: string
  filters: string[]
  metrics: AdminMetric[]
  columns: AdminColumn[]
  rows: Record<string, string | number>[]
}

const baseMetrics: AdminMetric[] = [
  { label: '商家数', value: 128, hint: '较昨日 +6' },
  { label: '用户数', value: 3206, hint: '本周新增 214' },
  { label: '摊位使用率', value: '68%', hint: '高峰 19:00' },
  { label: '投诉待处理', value: 3, hint: '1 条临近超时' }
]

const auditColumns: AdminColumn[] = [
  { prop: 'id', label: '编号', width: 90 },
  { prop: 'name', label: '主体名称' },
  { prop: 'owner', label: '提交人', width: 120 },
  { prop: 'category', label: '品类', width: 120 },
  { prop: 'status', label: '状态', width: 120 },
  { prop: 'updatedAt', label: '更新时间', width: 160 }
]

export const adminModuleConfigs: Record<string, AdminModuleConfig> = {
  '/dashboard': {
    module: 'dashboard',
    title: '数据总览',
    eyebrow: '运营与监管态势',
    primaryAction: '导出日报',
    filters: ['今日', '本周', '本月', '全部区域'],
    metrics: baseMetrics,
    columns: [
      { prop: 'name', label: '指标' },
      { prop: 'value', label: '当前值', width: 140 },
      { prop: 'trend', label: '趋势', width: 140 },
      { prop: 'owner', label: '责任模块', width: 160 }
    ],
    rows: [
      { name: '今日订单', value: 86, trend: '+12%', owner: '订单管理' },
      { name: '待审核商家', value: 9, trend: '-3', owner: '商家管理' },
      { name: '投诉处理时效', value: '92%', trend: '+4%', owner: '监管处理' }
    ]
  },
  '/vendors/audits': {
    module: 'vendorAudit',
    title: '入驻审核',
    eyebrow: '商家开店申请',
    primaryAction: '批量分派',
    filters: ['待线上初审', '待线下审核', '待上传合同', '审核通过'],
    metrics: [
      { label: '待初审', value: 8, hint: '2 小时内新增' },
      { label: '待线下审核', value: 5, hint: '监管协同' },
      { label: '待上传合同', value: 3, hint: '需提醒商家' },
      { label: '今日通过', value: 12, hint: '通过率 78%' }
    ],
    columns: auditColumns,
    rows: [
      { id: 10001, name: '烟火小摊', owner: '林师傅', category: '地方特色', status: '待线上初审', updatedAt: '今天 10:21' },
      { id: 10002, name: '乡野新农人鲜铺', owner: '陈小禾', category: '农家特产', status: '待线下审核', updatedAt: '今天 09:40' },
      { id: 10003, name: '守艺糖画铺', owner: '周老师', category: '非遗好物', status: '待上传合同', updatedAt: '昨天 18:10' }
    ]
  },
  '/qualifications': {
    module: 'qualification',
    title: '资质审核',
    eyebrow: '证照与经营材料',
    primaryAction: '批量审核',
    filters: ['健康证', '摊车照片', '经营场所', '商品照片'],
    metrics: [
      { label: '待审核材料', value: 21, hint: '健康证 7' },
      { label: '需补充', value: 6, hint: '图片不清晰' },
      { label: '今日通过', value: 18, hint: '平均 12 分钟' },
      { label: '过期提醒', value: 4, hint: '30 天内到期' }
    ],
    columns: auditColumns,
    rows: [
      { id: 20001, name: '健康证', owner: '林师傅', category: '证照', status: '待审核', updatedAt: '今天 11:00' },
      { id: 20002, name: '摊车照片', owner: '陈小禾', category: '场地', status: '需补充', updatedAt: '今天 08:22' }
    ]
  },
  '/special-identities': {
    module: 'specialIdentity',
    title: '特殊身份/公益标签审核',
    eyebrow: '公益标签与身份权益',
    primaryAction: '配置标签',
    filters: ['守艺匠人', '戎创达人', '青春创客', '乡野新农人', '微光创业者'],
    metrics: [
      { label: '待审核', value: 11, hint: '公益标签 8' },
      { label: '展示中', value: 42, hint: '前台可见' },
      { label: '驳回', value: 3, hint: '材料不足' },
      { label: '权益配置', value: 5, hint: '标签字典' }
    ],
    columns: auditColumns,
    rows: [
      { id: 30001, name: '守艺匠人认证', owner: '周老师', category: '非遗传承', status: '待审核', updatedAt: '今天 12:30' },
      { id: 30002, name: '乡野新农人认证', owner: '陈小禾', category: '助农农户', status: '审核通过', updatedAt: '昨天 16:45' }
    ]
  },
  '/complaints': {
    module: 'complaint',
    title: '投诉工单',
    eyebrow: '接收、分派、处理、反馈',
    primaryAction: '分派监管员',
    filters: ['待分派', '处理中', '临近超时', '已办结'],
    metrics: [
      { label: '待分派', value: 5, hint: '需运营处理' },
      { label: '处理中', value: 12, hint: '监管跟进' },
      { label: '临近超时', value: 1, hint: '24 小时规则' },
      { label: '今日办结', value: 9, hint: '满意率 96%' }
    ],
    columns: [
      { prop: 'id', label: '工单号', width: 100 },
      { prop: 'name', label: '投诉对象' },
      { prop: 'owner', label: '投诉人', width: 120 },
      { prop: 'category', label: '问题类型', width: 140 },
      { prop: 'status', label: '状态', width: 120 },
      { prop: 'updatedAt', label: '更新时间', width: 160 }
    ],
    rows: [
      { id: 70001, name: '烟火小摊', owner: '陈同学', category: '卫生问题', status: '待分派', updatedAt: '今天 13:20' },
      { id: 70002, name: '市民广场摊区', owner: '匿名用户', category: '占道经营', status: '处理中', updatedAt: '今天 10:15' }
    ]
  },
  '/content-audits': {
    module: 'contentAudit',
    title: '内容审核',
    eyebrow: '商品、评价、故事与活动',
    primaryAction: '批量通过',
    filters: ['商品', '评价', '摊主故事', '营销活动'],
    metrics: [
      { label: '待审核', value: 32, hint: '商品 18' },
      { label: '机审通过', value: 86, hint: '内容安全 mock' },
      { label: '人工复核', value: 7, hint: '敏感词命中' },
      { label: '今日拒绝', value: 4, hint: '图片不合规' }
    ],
    columns: auditColumns,
    rows: [
      { id: 60001, name: '手作糍粑商品图', owner: '林师傅', category: '商品', status: '待审核', updatedAt: '今天 14:05' },
      { id: 60002, name: '摊主故事', owner: '周老师', category: '故事', status: '人工复核', updatedAt: '今天 11:15' }
    ]
  }
}

const defaultConfig: AdminModuleConfig = {
  module: 'system',
  title: '业务管理',
  eyebrow: '模块占位',
  primaryAction: '新建',
  filters: ['全部', '待处理', '已处理', '已关闭'],
  metrics: baseMetrics,
  columns: auditColumns,
  rows: [
    { id: 1, name: '示例记录', owner: '系统', category: '通用', status: '待处理', updatedAt: '今天 09:00' },
    { id: 2, name: '业务配置', owner: '运营', category: '通用', status: '已处理', updatedAt: '昨天 18:00' }
  ]
}

export function getAdminModuleConfig(path: string): AdminModuleConfig {
  return adminModuleConfigs[path] ?? {
    ...defaultConfig,
    title: pathTitleMap[path] ?? defaultConfig.title
  }
}

const pathTitleMap: Record<string, string> = {
  '/areas': '经营区域管理',
  '/stalls': '摊位管理',
  '/stall-reservations': '摊位预约审批',
  '/violations': '违规记录',
  '/policies': '政策公告',
  '/notices': '通知管理',
  '/users': '用户管理',
  '/roles': '角色权限',
  '/system': '系统设置'
}
