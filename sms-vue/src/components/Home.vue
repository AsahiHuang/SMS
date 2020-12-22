<template>
  <div>
    <el-header>
      <NavMenu></NavMenu>

    </el-header>
    <router-view/>
  </div>
</template>

<script>
  import  NavMenu from './common/NavMenu'
  export default {
    name: 'Home',
    components: {NavMenu}
  }
</script>

<style scoped>

</style>