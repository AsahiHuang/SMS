<template>
  <el-container>
     <el-aside style="width: 200px;margib-top: 20px">
         <SideMenu @indexSelect="listByCategory" ref="sideMenu"></SideMenu>
    </el-aside>

    <el-main style="width: 800px; height: 939px">
        <posts class="posts-area" ref="postsArea"></posts>
    </el-main>
  </el-container>
</template>

<script>
  import SideMenu from '../home/SideMenu'
  import Posts from './Posts'
  import Cookie from 'js-cookie'

  export default {
    name: 'CommunityIndex',
    components: {SideMenu,Posts},
    methods: {
      listByCategory(){
        var _this = this
        var cid = this.$refs.sideMenu.cid
        var url = '/categories/' + cid + '/posts'
        this.$axios.get(url).then(resp => {
          if (resp && resp.status === 200) {
            _this.$refs.postsArea.posts = resp.data
          }
        })
      } 
    }
  }
</script>

<style scoped>
  .posts-area{
    width: 1350px;
    margin-left: auto;
    margin-right: auto;
  }

</style>