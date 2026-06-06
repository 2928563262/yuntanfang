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
  },
  '/areas': {
    module: 'area',
    title: '经营区域管理',
    eyebrow: '地图围栏、禁摆区域、经营时段',
    primaryAction: '新增区域',
    filters: ['启用中', '禁摆区', '临时区域', '待审核'],
    metrics: [
      { label: '合法区域', value: 18, hint: '本月新增 2' },
      { label: '禁摆区域', value: 6, hint: '监管配置' },
      { label: '可用摊位', value: 126, hint: '空闲 42' },
      { label: '品类限制', value: 9, hint: '需运营维护' }
    ],
    columns: auditColumns,
    rows: [
      { id: 41001, name: '北站中心公园东门', owner: '运营一组', category: '晚市', status: '启用中', updatedAt: '今天 09:30' },
      { id: 41002, name: '市民广场南侧临时摊区', owner: '监管一组', category: '午市', status: '启用中', updatedAt: '昨天 17:20' }
    ]
  },
  '/stalls': {
    module: 'stall',
    title: '摊位管理',
    eyebrow: '摊位数量、状态、占用与营业',
    primaryAction: '新增摊位',
    filters: ['空闲', '已预约', '营业中', '停用'],
    metrics: [
      { label: '摊位总数', value: 126, hint: '覆盖 18 区' },
      { label: '营业中', value: 48, hint: '高峰中' },
      { label: '已预约', value: 36, hint: '未来 7 天' },
      { label: '停用', value: 4, hint: '维护中' }
    ],
    columns: auditColumns,
    rows: [
      { id: 42001, name: 'A-018', owner: '烟火小摊', category: '地方特色', status: '营业中', updatedAt: '今天 18:00' },
      { id: 42002, name: 'B-006', owner: '未分配', category: '农家特产', status: '空闲', updatedAt: '今天 10:00' }
    ]
  },
  '/stall-reservations': {
    module: 'reservation',
    title: '摊位预约审批',
    eyebrow: '经营区域、时段、天数审批',
    primaryAction: '批量审批',
    filters: ['待审批', '已通过', '已拒绝', '已取消'],
    metrics: [
      { label: '待审批', value: 14, hint: '今日新增 5' },
      { label: '已通过', value: 62, hint: '本周' },
      { label: '冲突预约', value: 3, hint: '需人工分配' },
      { label: '改点申请', value: 4, hint: '待处理' }
    ],
    columns: auditColumns,
    rows: [
      { id: 43001, name: '北站 A-018 预约', owner: '林师傅', category: '18:00-22:00', status: '待审批', updatedAt: '今天 13:40' },
      { id: 43002, name: '广场 B-006 预约', owner: '陈小禾', category: '10:00-14:00', status: '冲突', updatedAt: '今天 11:12' }
    ]
  },
  '/violations': {
    module: 'violation',
    title: '违规记录',
    eyebrow: '监管处理、整改和信用记录',
    primaryAction: '新增记录',
    filters: ['待整改', '整改中', '已完成', '申诉中'],
    metrics: [
      { label: '待整改', value: 5, hint: '需复查' },
      { label: '本月记录', value: 18, hint: '较上月 -6' },
      { label: '申诉中', value: 2, hint: '监管复核' },
      { label: '信用扣分', value: 7, hint: '影响推荐' }
    ],
    columns: auditColumns,
    rows: [
      { id: 80001, name: '占道经营', owner: '某流动摊位', category: '区域违规', status: '待整改', updatedAt: '今天 15:00' },
      { id: 80002, name: '证照过期', owner: '示例摊主', category: '资质违规', status: '整改中', updatedAt: '昨天 09:30' }
    ]
  },
  '/policies': {
    module: 'policy',
    title: '政策公告',
    eyebrow: '政策、公告、运营规则发布',
    primaryAction: '发布公告',
    filters: ['草稿', '待发布', '已发布', '已下线'],
    metrics: [
      { label: '已发布', value: 24, hint: '前台可见' },
      { label: '草稿', value: 6, hint: '待编辑' },
      { label: '本周阅读', value: 1260, hint: '+18%' },
      { label: '置顶公告', value: 3, hint: '首页展示' }
    ],
    columns: auditColumns,
    rows: [
      { id: 90001, name: '夜市经营规范', owner: '运营一组', category: '政策', status: '已发布', updatedAt: '今天 09:00' },
      { id: 90002, name: '公益摊位扶持说明', owner: '平台运营', category: '公告', status: '草稿', updatedAt: '昨天 18:20' }
    ]
  },
  '/notices': {
    module: 'notice',
    title: '通知管理',
    eyebrow: '站内信、网页通知、推送记录',
    primaryAction: '新建通知',
    filters: ['待发送', '发送中', '已发送', '失败'],
    metrics: [
      { label: '待发送', value: 4, hint: '定时任务' },
      { label: '今日推送', value: 12, hint: '成功率 98%' },
      { label: '失败', value: 1, hint: '需重试' },
      { label: '订阅用户', value: 860, hint: '网页通知授权' }
    ],
    columns: auditColumns,
    rows: [
      { id: 91001, name: '出摊提醒', owner: '系统', category: '站内信', status: '已发送', updatedAt: '今天 17:30' },
      { id: 91002, name: '审核结果通知', owner: '审核系统', category: '网页通知', status: '待发送', updatedAt: '今天 12:00' }
    ]
  },
  '/users': {
    module: 'user',
    title: '用户管理',
    eyebrow: '消费者、摊主、运营、监管账号',
    primaryAction: '新增用户',
    filters: ['消费者', '摊主', '审核人员', '监管人员', '系统管理员'],
    metrics: [
      { label: '用户总数', value: 3206, hint: '本周 +214' },
      { label: '摊主账号', value: 128, hint: '活跃 96' },
      { label: '监管人员', value: 18, hint: '分区管理' },
      { label: '冻结账号', value: 3, hint: '风险处理' }
    ],
    columns: auditColumns,
    rows: [
      { id: 100001, name: '陈同学', owner: 'consumer', category: '消费者', status: '正常', updatedAt: '今天 16:20' },
      { id: 100002, name: '林师傅', owner: 'vendor', category: '摊主', status: '正常', updatedAt: '今天 10:21' }
    ]
  },
  '/roles': {
    module: 'role',
    title: '角色权限',
    eyebrow: 'RBAC 菜单、接口和数据权限',
    primaryAction: '新增角色',
    filters: ['consumer', 'vendor', 'admin', 'auditor', 'supervisor', 'system_admin'],
    metrics: [
      { label: '角色数', value: 6, hint: '基础角色' },
      { label: '权限点', value: 42, hint: '接口占位' },
      { label: '菜单权限', value: 15, hint: '后台菜单' },
      { label: '数据权限', value: 4, hint: '区域/角色' }
    ],
    columns: auditColumns,
    rows: [
      { id: 110001, name: 'system_admin', owner: '系统', category: '系统管理员', status: '启用', updatedAt: '初始化' },
      { id: 110002, name: 'supervisor', owner: '监管', category: '监管人员', status: '启用', updatedAt: '初始化' }
    ]
  },
  '/system': {
    module: 'system',
    title: '系统设置',
    eyebrow: '第三方接口、安全、日志、备份',
    primaryAction: '保存配置',
    filters: ['地图', '支付', '短信', '对象存储', '内容安全'],
    metrics: [
      { label: '接口适配器', value: 6, hint: '当前 mock' },
      { label: '环境变量', value: 12, hint: '.env.example' },
      { label: '备份策略', value: '待配置', hint: '文档占位' },
      { label: '安全项', value: 8, hint: 'JWT/RBAC' }
    ],
    columns: auditColumns,
    rows: [
      { id: 120001, name: 'MapService', owner: 'integration', category: '地图', status: 'mock', updatedAt: '初始化' },
      { id: 120002, name: 'PaymentService', owner: 'integration', category: '支付', status: 'mock', updatedAt: '初始化' }
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
