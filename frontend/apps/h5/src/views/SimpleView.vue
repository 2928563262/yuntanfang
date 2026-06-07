<template>
  <main class="page module-page">
    <van-nav-bar :title="title" left-arrow fixed placeholder @click-left="router.back()" />

    <section class="module-hero" :class="scene.key">
      <div>
        <p>{{ scene.kicker }}</p>
        <h1>{{ scene.heading }}</h1>
        <span>{{ scene.summary }}</span>
      </div>
      <button type="button" @click="primaryAction">{{ scene.action }}</button>
    </section>

    <section v-if="scene.key === 'login'" class="login-card">
      <van-field v-model="loginForm.phone" label="手机号" placeholder="请输入手机号" />
      <van-field v-model="loginForm.code" label="验证码" placeholder="请输入验证码" />
      <van-button block round type="primary" @click="router.push('/')">登录并体验</van-button>
      <p>支持验证码登录、微信授权和密码登录，演示时可直接进入首页。</p>
    </section>

    <section v-else-if="scene.key === 'orders'" class="order-stack">
      <div class="filter-row">
        <button v-for="tab in orderTabs" :key="tab" :class="{ active: activeOrderTab === tab }" type="button" @click="activeOrderTab = tab">
          {{ tab }}
        </button>
      </div>
      <article v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-top">
          <strong>{{ order.shop }}</strong>
          <van-tag :type="order.type">{{ order.status }}</van-tag>
        </div>
        <div class="order-body">
          <img :src="order.image" :alt="order.shop" />
          <div>
            <p>{{ order.items }}</p>
            <span>{{ order.note }}</span>
            <strong>实付款 ¥{{ order.price }}</strong>
          </div>
        </div>
        <div class="order-actions">
          <button type="button">再来一单</button>
          <button class="primary" type="button">查看详情</button>
        </div>
      </article>
    </section>

    <section v-else-if="scene.key === 'profile'" class="profile-board">
      <div class="profile-card">
        <img src="https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=160&q=80" alt="用户头像" />
        <div>
          <h2>用户名称</h2>
          <span>已认证 · 本月下单 5 次</span>
        </div>
        <van-icon name="setting-o" />
      </div>
      <div class="wallet-grid">
        <button v-for="item in walletItems" :key="item.label" type="button">
          <span>{{ item.mark }}</span>
          <strong>{{ item.value }}</strong>
          <small>{{ item.label }}</small>
        </button>
      </div>
      <div class="service-panel">
        <h2>功能服务</h2>
        <div>
          <button v-for="item in profileServices" :key="item.label" type="button">
            <span class="service-mark">{{ item.mark }}</span>
            <span>{{ item.label }}</span>
          </button>
        </div>
      </div>
    </section>

    <section v-else class="module-content">
      <div class="quick-grid">
        <button v-for="item in scene.cards" :key="item.title" type="button">
          <span :class="['quick-icon', item.tone]">
            {{ item.mark }}
          </span>
          <strong>{{ item.title }}</strong>
          <small>{{ item.text }}</small>
        </button>
      </div>

      <article class="demo-card">
        <div class="demo-image" :style="{ backgroundImage: `url(${scene.image})` }"></div>
        <div>
          <van-tag type="success">{{ scene.badge }}</van-tag>
          <h2>{{ scene.demoTitle }}</h2>
          <p>{{ scene.demoText }}</p>
        </div>
      </article>

      <section class="timeline-panel">
        <h2>{{ scene.flowTitle }}</h2>
        <div class="flow-list">
          <div v-for="step in scene.flow" :key="step.title">
            <span>{{ step.time }}</span>
            <strong>{{ step.title }}</strong>
            <p>{{ step.text }}</p>
          </div>
        </div>
      </section>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const activeOrderTab = ref('全部')
const loginForm = reactive({ phone: '138 0000 0000', code: '123456' })

const title = computed(() => String(route.meta.title ?? '云摊坊'))
const moduleName = computed(() => String(route.meta.module ?? '消费者'))
const orderTabs = ['全部', '备餐中', '待取货', '退款']
const img = 'https://images.unsplash.com/'

const scenes = {
  login: {
    key: 'login',
    kicker: '账号服务',
    heading: '登录云摊坊',
    summary: '验证码登录 · 微信授权 · 资料自动带入',
    action: '返回首页',
    image: `${img}photo-1556745757-8d76bdb6984b?auto=format&fit=crop&w=360&q=80`,
    cards: [],
    badge: '账号',
    demoTitle: '便捷登录',
    demoText: '演示环境支持一键进入。',
    flowTitle: '登录流程',
    flow: []
  },
  stalls: {
    key: 'stalls',
    kicker: '消费者入口',
    heading: '摊位列表与筛选',
    summary: '按距离、品类、优惠和备案状态快速筛选。',
    action: '查看首页',
    badge: '已备案',
    demoTitle: '老吴烧烤',
    demoText: '20分钟 · 2.2km · 月售312 · 人均¥45，支持预约自提和到店选购。',
    flowTitle: '筛选路径',
    image: `${img}photo-1529692236671-f1f6cf9683ba?auto=format&fit=crop&w=420&q=80`,
    cards: [
      { mark: '近', tone: 'orange', title: '附近优先', text: '实时定位附近摊点' },
      { mark: '备', tone: 'green', title: '政府备案', text: '资质信息可追溯' },
      { mark: '券', tone: 'red', title: '优惠活动', text: '满减和公益标签' }
    ],
    flow: [
      { time: '01', title: '定位区域', text: '默认展示当前公园、夜市和学校周边摊点。' },
      { time: '02', title: '查看商家', text: '进入店铺后可看菜单、评价、资质和公告。' },
      { time: '03', title: '预约自提', text: '减少排队等待，避免跑空。' }
    ]
  },
  detail: {
    key: 'detail',
    kicker: '店铺主页',
    heading: '菜单、评价和商家资质',
    summary: '参考原型中的店铺点餐页，突出热销商品和购物车。',
    action: '去下单',
    badge: '招牌热销',
    demoTitle: '多汁爆鸡肉套餐',
    demoText: '主料鸡胸肉，月售53，支持规格选择、加购和备注。',
    flowTitle: '点餐流程',
    image: `${img}photo-1555939594-58d7cb561ad1?auto=format&fit=crop&w=420&q=80`,
    cards: [
      { mark: '热', tone: 'red', title: '招牌热销', text: '高频商品靠前展示' },
      { mark: '评', tone: 'blue', title: '评价4.5', text: '真实评价辅助决策' },
      { mark: '证', tone: 'green', title: '商家资质', text: '食品安全信息可查' }
    ],
    flow: [
      { time: '01', title: '选择商品', text: '按分类浏览炭烤、饮品、炸货和套餐。' },
      { time: '02', title: '加入购物车', text: '底部购物车固定展示数量与价格。' },
      { time: '03', title: '提交订单', text: '选择自提时间并备注口味。' }
    ]
  },
  pending: {
    key: 'pending',
    kicker: '功能占位',
    heading: '该功能演示阶段暂未开放',
    summary: '当前 H5 专注用户端消费流程，其他模块后续再接入。',
    action: '返回首页',
    badge: '待建设',
    demoTitle: '用户端优先',
    demoText: '本轮演示保留搜索、点餐、下单、订单和评价主流程。',
    flowTitle: '后续计划',
    image: `${img}photo-1556745757-8d76bdb6984b?auto=format&fit=crop&w=420&q=80`,
    cards: [
      { mark: '搜', tone: 'orange', title: '检索摊位', text: '用户按关键词找到附近摊位' },
      { mark: '订', tone: 'green', title: '预约自提', text: '提交订单并查看状态' },
      { mark: '评', tone: 'blue', title: '评价反馈', text: '完成消费后的用户评价' }
    ],
    flow: [
      { time: '01', title: '用户端主流程', text: '先保证消费者演示链路清晰稳定。' },
      { time: '02', title: '低优先级页面', text: '钱包、设置、投诉等后续逐步完善。' },
      { time: '03', title: '其他端独立规划', text: '不在当前用户端界面混入管理入口。' }
    ]
  }
}

const scene = computed(() => {
  if (route.path === '/login') return scenes.login
  if (route.path.startsWith('/orders')) return {
    ...scenes.stalls,
    key: 'orders',
    kicker: '订单中心',
    heading: '订单进度清楚可追踪',
    summary: '展示备餐中、待取货、待评价和退款协商状态。',
    action: '继续逛逛'
  }
  if (route.path === '/profile') return {
    ...scenes.stalls,
    key: 'profile',
    kicker: '个人中心',
    heading: '钱包、收藏和客服集中入口',
    summary: '参考原型我的页面，保留橙色头部和功能宫格。',
    action: '返回首页'
  }
  if (route.path.includes('/stalls/') || route.path.includes('/reviews')) return scenes.detail
  return scenes.pending
})

const orders = [
  {
    id: 1,
    shop: '老吴烧烤',
    status: '待付款',
    type: 'warning' as const,
    items: '烤鸡翅、可口可乐、招牌牛肉',
    note: '距自提约 8 分钟',
    price: 80,
    image: `${img}photo-1529692236671-f1f6cf9683ba?auto=format&fit=crop&w=160&q=80`
  },
  {
    id: 2,
    shop: '小林柠檬茶',
    status: '商家备餐中',
    type: 'primary' as const,
    items: '手打柠檬茶 ×2',
    note: '预计 12:30 前完成',
    price: 25,
    image: `${img}photo-1551024601-bec78aea704b?auto=format&fit=crop&w=160&q=80`
  },
  {
    id: 3,
    shop: '老王早食',
    status: '交易成功',
    type: 'success' as const,
    items: '煎饼果子、豆浆',
    note: '欢迎评价本次消费体验',
    price: 18,
    image: `${img}photo-1533089860892-a7c6f0a88666?auto=format&fit=crop&w=160&q=80`
  }
]

const walletItems = [
  { mark: '包', value: '¥128', label: '我的钱包' },
  { mark: '红', value: '3', label: '红包' },
  { mark: '券', value: '5', label: '代金券' },
  { mark: '账', value: '12', label: '支付账单' }
]

const profileServices = [
  { mark: '藏', label: '我的收藏' },
  { mark: '迹', label: '我的足迹' },
  { mark: '评', label: '我的评价' },
  { mark: '客', label: '帮助与客服' },
  { mark: '诉', label: '我的投诉' },
  { mark: '设', label: '系统设置' }
]

function primaryAction() {
  if (scene.value.key === 'detail') {
    router.push('/orders')
    return
  }
  router.push('/')
}
</script>
