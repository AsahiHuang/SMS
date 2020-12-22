<template>
  <body id="poster">
    <el-form :model="loginForm" :rules="rules" class="login-container" label-position="left"
             label-width="0px">
      <el-image
        style="width: 120px; height: 120px"
        src="https://i.loli.net/2020/11/11/icFzyMJwHGT6aZU.png"
        :fit="fit">
      </el-image>
      <h3 class="login_title"><br/>订阅服务管理系统</h3>
      <el-form-item prop="username">
        <el-input type="text" v-model="loginForm.username"
                  auto-complete="off" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" show-password
                  @keyup.enter.native="login"
                  auto-complete="off" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item style="width: 100%">
        <el-button type="primary" style="width: 100%;background: #409EFF;border: none" v-on:click="login">登录</el-button>
      </el-form-item>
      <el-form-item style="width: 100%">
        <el-button type="primary" style="width: 100%;background: #505458;border: none" v-on:click="register">没有账号？注册一下</el-button>
      </el-form-item>
      <el-form-item>
        <el-popover
          placement="bottom"
          title="找回密码"
          width="300"
          trigger="click"
          v-model="visible"
          content="请通过邮件联系管理员并提供注册用户名及邮箱 管理员将协助你完成密码重置工作">
          <div style="text-align: right; margin: 10px">
            <el-input type="text" v-model="username" auto-complete="off" placeholder="请输入用户名用以验证" style="margin-bottom: 5px"></el-input>
            <el-button size="mini" type="text" @click="visible = false">取消</el-button>
            <el-button type="primary" size="mini" @click="forgetPassword">确定</el-button>
          </div>
          <el-button type="text" slot="reference">忘记密码？</el-button>
         </el-popover>
      </el-form-item>
    </el-form>
  </body>
</template>

<script>
  import Cookie from 'js-cookie'
  export default {
    name: 'Login',
    data () {
      return {
        rules: {
          username: [{required: true, message: '用户名不能为空', trigger: 'blur'}],
          password: [{required: true, message: '密码不能为空', trigger: 'blur'}],
        },
        checked: true,
        loginForm: {
          username: '',
          password: ''
        },
        username: '',
        responseResult: [],
        visible: false
      }
    },
    methods: {
      login () {
        var _this = this
        console.log(this.$store.state)
        this.$axios
          .post('/login', {
            username: this.loginForm.username,
            password: this.loginForm.password
          })
          .then(successResponse => {
            if (successResponse.data.code === 200) {
              // var data = this.loginForm
              this.getUserInfo(this.loginForm.username)
              _this.$store.commit('login', _this.loginForm)
              var path = this.$route.query.redirect
              this.$router.replace({path: path === '/' || path === undefined ? '/index' : path})
              this.$message({
                type: 'success',
                message: '登录成功，欢迎你：' + Cookie.get('username') 
              })
            } else {
              this.$message({
                type: 'error',
                message: '用户名或密码错误，请重试'
              })
              this.clear()
              .catch(failResponse => {
              })
            }
          })
        },
        register (){
          this.$router.replace({path:'/register'})
        },
        clear (){
          this.loginForm.username = '',
          this.loginForm.password = ''
        },

        // 获取用户信息
        getUserInfo(username) {
          Cookie.set('username',username)
          var _this = this
          var url = '/' + username + '/info'
          this.$axios.get(url).then(resp => {
            if (resp && resp.status === 200) {
              var returnUser = resp.data 
              Cookie.set('id',returnUser.id)
              Cookie.set('email',returnUser.email)
              Cookie.set('auth',returnUser.auth)
            }
          })
        },

        // 找回密码
        forgetPassword(){
          var _this = this
          var url = '/user/forgetPwd'
          if (this.username === ''){
            this.$message({
              type: 'error',
              message: '请输入用户名才能帮你找回'
            })
            return
          }
          this.$axios
            .post(url, {
              username: this.username
            })
            .then(successResponse => {
              if (successResponse.data.code === 200) {
                this.visible = false
                this.username = ''
                alert("用户名确认成功\n\n系统已将随机的新密码发送到您的预留邮箱，登录后请尽快修改密码。")
              }else  {
                alert("用户名确认失败\n\n该用户名未注册或已被删除，请尝试重新输入。")
                this.username = ''
              }
            })
            .catch(failResponse => {})
        }
      }
  }
</script>

<style>
  #poster {
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }
  body{
    margin: 0px;
  }
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
  }
  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #606266;
  }

</style>