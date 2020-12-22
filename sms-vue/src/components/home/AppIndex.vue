<template>
  <el-container>
    <el-aside style="width: 200px;margin-top: 20px">
      <switch></switch>
      <!--<SideMenu></SideMenu>-->
      <!-- 分类查询 -->
      <side-menu @indexSelect="listByCategory" ref="sideMenu"></side-menu>
    </el-aside>
    <el-main>
      <!--<subs></subs>-->
      <subs class="subs-area" ref="subsArea"></subs>
    </el-main>
  </el-container>
</template>

<script>
  import SideMenu from './SideMenu'
  import Subs from './Subs'
  import Cookie from "js-cookie"
  export default {
    name: 'AppIndex',
    components: {SideMenu,Subs},
    methods: {
      listByCategory () {
        var _this = this
        var cid = this.$refs.sideMenu.cid
        var user = Cookie.get('username')
        var url = '/' + user + '/categories/' + cid + '/subs'
        this.$axios.get(url).then(resp => {
          if (resp && resp.status === 200) {
            _this.$refs.subsArea.subs = resp.data
          }
        })
      },
    }
  }
</script>

<style scoped>
  .subs-area {
    width: 1350px;
    margin-left: auto;
    margin-right: auto;
  }

</style>