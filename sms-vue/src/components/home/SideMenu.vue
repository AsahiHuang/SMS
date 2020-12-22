<template>
  <el-menu
    class="categories"
    default-active="0"
    @select="handleSelect"
    active-text-color="#409EFF">
    <label style="color:#409EFF">按照类别查看</label>
    <el-menu-item index="0">
      <i class="el-icon-menu"></i>
      <span slot="title" >&nbsp;全部类型</span>
    </el-menu-item>
    <el-menu-item index="1">
      <span style="font-size: 55px;color: #FF0033">·</span>
      <i class="el-icon-headset"></i>
      <span slot="title">音乐类</span>
    </el-menu-item>
    <el-menu-item index="2">
      <span style="font-size: 55px;color: #0066CC">·</span>
      <i class="el-icon-video-play"></i>
      <span slot="title">视频类</span>
    </el-menu-item>
    <el-menu-item index="3">
      <span style="font-size: 55px;color: #FF9900">·</span>
      <i class="el-icon-shopping-cart-full"></i>
      <span slot="title">购物类</span>
    </el-menu-item>
    <el-menu-item index="4">
      <span style="font-size: 55px;color: #FDE200">·</span>
      <i class="el-icon-fork-spoon"></i>
      <span slot="title">外卖类</span>
    </el-menu-item>
    <el-menu-item index="5">
      <span style="font-size: 55px;color: #339933">·</span>
      <i class="el-icon-basketball"></i>
      <span slot="title">游戏类</span>
    </el-menu-item>
    <el-menu-item index="6">
      <span style="font-size: 55px;color: #9966CC">·</span>
      <i class="el-icon-collection-tag"></i>
      <span slot="title">其他类</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
  export default {
    name: 'SideMenu',
    data () {
      return {
        cid: ''
      }
    },
    methods: {
      handleSelect (key, keyPath) {
        this.cid = key
        this.$emit('indexSelect')
      }
    }
  }
</script>

<style scoped>
  .categories {
    position: fixed;
    margin-left: 50%;
    left: -900px;
    top: 200px;
    width: 200px;
  }
</style>