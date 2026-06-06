<template>
  <main class="page">
    <section class="hero-card">
      <p class="eyebrow">账号登录</p>
      <h1>欢迎来到云摊坊</h1>
      <p>登录后可预约下单、收藏摊位、订阅出摊提醒，也可以申请成为摊主。</p>
    </section>

    <section class="section content-grid">
      <form class="card form-list">
        <div class="field-card">
          <label>手机号</label>
          <input placeholder="请输入手机号" />
        </div>
        <div class="field-card">
          <label>验证码</label>
          <div class="inline-field">
            <input placeholder="请输入验证码" />
            <button class="ghost-pill" type="button">获取验证码</button>
          </div>
        </div>
        <button class="primary-pill" type="button">登录/注册</button>
        <p class="muted">登录即表示同意用户协议和隐私政策。当前阶段使用 mock 登录，后续接入 `/api/auth/login`。</p>
      </form>

      <aside class="card">
        <h2>更多入口</h2>
        <div class="list-stack">
          <RouterLink class="list-card" to="/vendor/apply">
            <h3>我是摊主</h3>
            <p>提交入驻备案、资质材料和公益标签。</p>
          </RouterLink>
          <RouterLink class="list-card" to="/stalls">
            <h3>先逛逛</h3>
            <p>浏览附近摊位、今日推荐和特色专区。</p>
          </RouterLink>
        </div>
      </aside>
    </section>
  </main>
</template>
