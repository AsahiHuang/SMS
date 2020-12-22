<template>
  <body id="paper">
  <el-form :model="registerForm" :rules="rules" class="login-container" label-position="left"
           label-width="0px" v-loading="loading">
    <el-image
      style="width: 120px; height: 120px"
      src="https://i.loli.net/2020/11/11/icFzyMJwHGT6aZU.png"
      :fit="fit">
    </el-image>
    <h3 class="login_title"><br/>用户注册</h3>
    <el-form-item prop="username">
      <el-input type="text" v-model="registerForm.username"
                auto-complete="off" placeholder="用户名"></el-input>
    </el-form-item>
    <el-form-item prop="email">
      <el-input type="text" v-model="registerForm.email"
                auto-complete="off" placeholder="邮箱"></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input type="password" v-model="registerForm.password" show-password
                auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <el-form-item prop="passwordConfirm">
      <el-input type="password" v-model="registerForm.passwordConfirm"
                @keyup.enter.native="register"
                auto-complete="off" placeholder="确认密码"></el-input>
    </el-form-item>

    <el-form-item style="width: 100%">
      <el-button type="primary" style="width: 100%;background: #409EFF;border: none;" v-on:click="register">注册</el-button>
    </el-form-item>
    <el-form-item style="width: 100%">
      <el-button type="primary" style="width: 100%;background: #505458;border: none;" v-on:click="tologin">返回</el-button>
    </el-form-item>
  </el-form>
  </body>
</template>

<script>
export default{
  name: 'Register',
  data () {
    return {
      rules: {
        username: [{required: true, message: '用户名不能为空', trigger: 'blur'}],
        password: [{required: true, message: '密码不能为空', trigger: 'blur'}],
        email: [{required: true, message: '邮箱不能为空', trigger: 'blur'},
        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }],
        passwordConfirm: [{required: true, message: '密码不能为空', trigger: 'blur'}]
      },
      checked: true,
      registerForm: {
        username: '',
        email: '',
        password: '',
        passwordConfirm: ''
      },
      loading: false
    }
  },
  methods: {
    register () {
      var _this = this
      if (this.registerForm.username === ''){
        this.$message({
          message: '请输入用户名',
          type: 'error'
        })
        return
      }
      if (this.registerForm.email === ''){
        this.$message({
          message: '请输入邮箱',
          type: 'error'
        })
        return
      }

      if (this.registerForm.password !== this.registerForm.passwordConfirm) {
        this.$message({
          message: '两次输入的密码不一致',
          type: 'error'
        })
        return
      }
      this.$axios
        .post('/register',{
            username:this.registerForm.username,
            email:this.registerForm.email,
            password:this.registerForm.password,
          })
        .then(successResponse => {
            if(successResponse.data.code === 200){
              alert("恭喜您，注册成功，现在你可以登录使用啦↖(^ω^)↗")
              this.$router.replace('/login')
            }
            else if (successResponse.data.code === 400) {
              this.$message({
              message: '该用户名已存在，请更换一个',
              type: 'error'
            })
            }
          })
        .catch(failResponse => {
          })
    },
    tologin () {
      this.$router.replace('/login')
    }
  }
}
</script>

<style>
  #paper {
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }
  body{
    margin: -5px 0px;
  }
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 160px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
  }
  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }
  .login_remember {
    margin: 0px 0px 35px 0px;
    text-align: left;
  }
</style>