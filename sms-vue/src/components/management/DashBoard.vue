<template>
  <el-container>
    <el-aside style="width: 200px;margin-top: 20px">
    </el-aside>
      <Menu></Menu>
    <el-main>
      <el-page-header class="management-area" @back="goBack" content="管理中心面板">
      </el-page-header>
      <panel class="panel-area" ref="panel"></panel>
    </el-main>
  </el-container>
</template>

<script>
  import Menu from './Menu'
  import Panel from './Panel.vue'
  export default {
    name: 'DashBoard',
    components: {Menu,Panel},
    methods: {
      goBack() {
        this.$router.replace('/index')
      },
    }
  }
</script>

<style scoped>
  .management-area {
    width: 1300px;
    margin-left: auto;
    margin-right: auto;
  }
  .panel-area {
    width: 1300px;
    margin-top: 50px;
    margin-left: auto;
    margin-right: auto;
  }
</style>