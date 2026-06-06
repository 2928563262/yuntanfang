<template>
  <main class="page">
    <van-nav-bar :title="String($route.meta.title)" left-arrow @click-left="$router.back()" />
    <section class="panel">
      <p class="eyebrow">{{ $route.meta.module ?? '云摊坊' }}</p>
      <h2>{{ $route.meta.title }}</h2>
      <p>页面骨架已接入路由、权限占位和 API 封装，后续按业务补表单、列表、上传、地图与订单状态流转。</p>
      <van-cell-group inset>
        <van-cell title="接口状态" value="mock" />
        <van-cell title="权限角色" value="可扩展" />
        <van-cell title="移动端适配" value="已预留" />
      </van-cell-group>
    </section>
  </main>
</template>
