<template>
  <div>
    <el-row :gutter="20">
      <el-col span="3">
        <div class="logo">
          <el-tooltip class="item" effect="dark" content="订阅服务管理系统" placement="bottom">
            <el-image
              style="width: 100px; height: 100px; margin-left: 30px; margin-top: 20px"
              src="https://i.loli.net/2020/11/25/zN7OmdlIDhnS9AX.png"
              :fit="fill">
            </el-image>
          </el-tooltip>
        </div>
      </el-col>
      <el-col span="21">
        <el-menu
          :default-active="this.$route.path"
          router
          mode="horizontal"
          background-color="white"
          text-color="#222"
          active-text-color="#409EFF"
          style="min-width: 1500px" >
          <el-menu-item v-for="(item,i) in navList" :key="i" :index="item.name" style="font-size:16px">
            {{ item.navItem }}
          </el-menu-item>
          <i class="el-icon-user" style="position: absolute;margin-top: 23px;right: 5%;font-size: 17px">&nbsp;{{showUserName}}</i> 
          <i class="el-icon-date" style="position: absolute;margin-top: 23px;right: 12%;font-size: 17px">&nbsp;{{showNowDate}}</i> 
          <i class="el-icon-switch-button" v-on:click="logout" style="color:#409EFF;float:right;font-size: 25px;padding: 18px"></i>
        </el-menu>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import Cookie from "js-cookie"
  export default {
    name: 'NavMenu',
    data () {
      return {
        navList: [
          {name: '/index', navItem: '个人首页'},
          {name: '/community', navItem: '订阅社区'},
          {name: '/management', navItem: '管理中心'}
        ]
      }
    },
    computed:{
      showUserName() {
        return Cookie.get('username')
      },
      showNowDate() {
        var nowDate = new Date()
        var year = nowDate.getFullYear()
        var month = nowDate.getMonth() + 1
        var day = nowDate.getDate()
        if (month < 10) month = '0' + month
        if (day < 10) day = '0' + day
        return year + '年' + month + '月' + day + '日' 
      }

    },
    methods:{
      // 登出功能
      logout () {
        var _this = this
        var username = Cookie.get('username')
        this.$confirm('是否登出当前用户：' + username + ' ?','提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() =>{
          this.postLogoutLog(username)
          _this.$store.commit('logout')
          _this.$router.replace('/login')
          Cookie.set('username','');
          Cookie.set('id','');
          Cookie.set('auth','');
          Cookie.set('email','');
          this.$message({
            type: 'success',
            message: '用户：' + username + ' 已登出，请重新登录'
          })

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消登出'
          })
        })
      },
      postLogoutLog(username) {
        var _this = this
        this.$axios
          .post('/log/logout',{
            username: username
          })
          .then(successResponse=> {
            console.log("success")
          })
      }
    }
  }
</script>

<style scoped>
  .logo {
    width: 100%;
  }
  a{
    text-decoration: none;
  }

  span {
    pointer-events: none;
  }

  .el-icon-switch-button {
    cursor: pointer;
    outline:0;
  }
</style>
