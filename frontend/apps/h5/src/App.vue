<template>
  <RouterView />
  <van-tabbar route>
    <van-tabbar-item to="/" icon="wap-home">首页</van-tabbar-item>
    <van-tabbar-item to="/stalls" icon="search">摊位</van-tabbar-item>
    <van-tabbar-item to="/orders" icon="orders-o">订单</van-tabbar-item>
    <van-tabbar-item to="/vendor/dashboard" icon="shop-o">摊主</van-tabbar-item>
    <van-tabbar-item to="/profile" icon="user-o">我的</van-tabbar-item>
  </van-tabbar>
</template>
