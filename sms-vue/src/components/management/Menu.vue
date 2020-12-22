<template>
  <el-menu
      default-active="0"
      class="menus"
      @open="handleOpen"
      @select="handleSelect">
      <label style="color:#409EFF">管理中心菜单</label>
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-user"></i>
          <span>用户信息</span>
        </template>
        <el-menu-item-group>
          <template slot="title">用户</template>
          <el-menu-item index="11" @click="changePwdDialog = true">修改密码</el-menu-item>
          <el-drawer 
          title="修改密码"
          size="40%"
          append-to-body="true"
          :visible.sync="changePwdDialog">
           <div class="change-password">
            <el-form :model="changePwdForm" style="text-align: left" :rules="rules">
              <el-form-item prop="password" label="原密码:" :label-width="formLabelWidth">
                <el-input type="password" style="width:300px" v-model="changePwdForm.password" autocomplete="off" show-password></el-input>
              </el-form-item>
              <el-form-item prop="newPassword" label="新密码:" :label-width="formLabelWidth" >
                <el-input type="password" style="width:300px" v-model="changePwdForm.newPassword" auto-complete="off" show-password></el-input>
              </el-form-item>
              <el-form-item prop="newPasswordConfirm" label="重复新密码:" :label-width="formLabelWidth" >
                <el-input type="password" style="width:300px" v-model="changePwdForm.newPasswordConfirm"
                 @keyup.enter.native="onSumitNewPwd"
                 auto-complete="off"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="changePwd_dialog_footer">
              <el-button @click="cancelForm" style="margin-left: 500px">取 消</el-button>
              <el-button type="primary" @click="onSumitNewPwd">确 定</el-button>
              </div>
           </div>
          </el-drawer>
          <el-menu-item index="12" @click="changeEmailDialog = true">修改邮箱</el-menu-item>
          <el-drawer
          title="修改预留邮箱"
          size="40%"
          append-to-body="true"
          :visible.sync="changeEmailDialog">
           <div class="change-email">
            <el-form :model="changeEmailForm"  style="text-align: left" :rules="rules" >
              <el-form-item label="当前邮箱地址:" :label-width="formLabelWidth">
                <span style="width: 400px">{{showEmail()}}</span>
              </el-form-item>
              <el-form-item prop="newEmail" label="新的邮箱地址:" :label-width="formLabelWidth">
                <el-input style="width:300px" v-model="changeEmailForm.newEmail" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item prop="newEmailConfirm" label="重复邮箱地址:" :label-width="formLabelWidth">
                <el-input style="width:300px" v-model="changeEmailForm.newEmailConfirm" auto-complete="off"></el-input>
              </el-form-item>
                <i class="el-icon-info" style="margin-left: 40px;color:#C0C4CC;font-size: 13px; width:380px">&nbsp;邮箱将用于联系、提醒以及协助找回密码，请确保正确。</i>
            </el-form>
            <div slot="footer" class="dialog_footer">
              <el-button @click="cancelForm" style="margin-left: 500px">取 消</el-button>
              <el-button type="primary" @click="onSumitNewEmail">确 定</el-button>
              </div>
           </div>
          </el-drawer>
        </el-menu-item-group>
      </el-submenu>
      <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-collection-tag"></i>
          <span>订阅管理</span>
        </template>
        <el-menu-item-group>
          <template slot="title">用户</template>
          <!-- 个人订阅管理模块 -->
          <el-menu-item index="21" @click="subManagementDialog = true">个人订阅</el-menu-item>
            <el-drawer
              title="个人订阅管理"
              :visible.sync="subManagementDialog"
              append-to-body="true"
              size="60%">
              <sub-manage-form ref="SubManageForm"></sub-manage-form>
            </el-drawer>
          <el-menu-item index="22" @click="postManagementDialog = true">多人订阅</el-menu-item>
            <el-drawer
              title="多人订阅管理"
              :visible.sync="postManagementDialog"
              append-to-body="true"
              size="60%">
              <post-manage-form ref="PostManageForm"></post-manage-form>
            </el-drawer>
        </el-menu-item-group>
      </el-submenu>
      <!-- 管理员模块 -->
      <el-submenu index="3" :hidden="returnAuth()">
        <template slot="title">
          <i class="el-icon-setting"></i>
          <span>管理后台</span>
        </template>
        <el-menu-item-group>
          <template slot="title">管理员</template>
          <el-menu-item index="31" @click="userManagementDialog = true">用户管理</el-menu-item>
          <el-drawer
            title="用户管理（管理员）"
            :visible.sync="userManagementDialog"
            append-to-body="true"
            size="50%">
            <user-manage-form ref="UserManageForm"></user-manage-form>
          </el-drawer>
          <el-menu-item index="32" @click="communityManagementDialog = true">社区管理</el-menu-item>
          <el-drawer
            title="社区管理（管理员）"
            :visible.sync="communityManagementDialog"
            append-to-body="true"
            size="70%">
            <community-manage-form ref="CommunityManageForm"></community-manage-form>
          </el-drawer>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
</template>

<script>
  import Cookie from "js-cookie"
  import SubManageForm from './SubManageForm.vue'
  import PostManageForm from './PostManageForm.vue'
  import UserManageForm from './UserManageForm.vue'
  import CommunityManageForm from './CommunityManageForm.vue'
  export default {
    name: 'Menu',
    components:{SubManageForm,PostManageForm, SubManageForm, UserManageForm, CommunityManageForm},
    data () {
      return {
        username: Cookie.get('username'),
        formLabelWidth: '150px',
        changePwdDialog: false,
        loading: false,
        changePwdForm: {
          password: '',
          newPassword: '',
          newPasswordConfirm: ''
        },
        changeEmailDialog: false,
        changeEmailForm:{
          newEmail: '',
          newEmailConfirm: ''
        },
        subManagementDialog: false,
        postManagementDialog: false,
        userManagementDialog: false,
        communityManagementDialog: false,
        rules: {
          password: [{required: true, message: '原密码不能为空', trigger: 'blur'}],
          newPassword: [{required: true, message: '新密码不能为空', trigger: 'blur'}],
          newPasswordConfirm: [{required: true, message: '请重复输入新密码', trigger: 'blur'}],
          newEmail: [{ required: true, message: '请输入邮箱地址', trigger: 'blur' },
                  { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }],
          newEmailConfirm: [{ required: true, message: '请重复输入邮箱地址', trigger: 'blur' },
                  { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }]
        },
        email: '',
        checked: true
      };
    },
    methods: {
      clear() {
        this.changePwdForm = {
          password: '',
          newPassword: '',
          newPasswordConfirm: ''
        },
        this.changeEmailForm = {
          newEmail: '',
          newEmailConfirm: ''
        }
      },

      handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {});
      },
      cancelForm() {
        this.loading = false;
        this.changePwdDialog = false;
        this.changeEmailDialog = false;
        this.subManagementDialog = false;
        this.communityManagementDialog = false;
        this.clear();
      },
      onSumitNewPwd() {
        var _this = this
        if (this.changePwdForm.password === ''){
          this.$notify({
            title: '错误',
            message: '原密码不能为空！',
            type: 'error'
          })
          this.clear()
          return
        }
        if (this.changePwdForm.newPassword === ''){
          this.$notify({
            title: '错误',
            message: '新密码不能为空！',
            type: 'error'
          })
          this.clear()
          return
        }
        if (this.changePwdForm.newPasswordConfirm === ''){
          this.$notify({
            title: '错误',
            message: '重复密码不能为空！',
            type: 'error'
          })
          this.clear()
          return
        }
        if (this.changePwdForm.newPassword !== this.changePwdForm.newPasswordConfirm){
          this.$notify({
            title: '错误',
            message: '两次输入的密码不一致',
            type: 'error'
          })
          this.clear()
          return
        }
        // 先验证原密码的正确性
        this.$axios
          .post('/login',{
            username: this.username,
            password: this.changePwdForm.password
          })
          .then(successResponse =>{
            if (successResponse.data.code === 200){
              this.$axios
                .post('/changePwdOrEmail',{
                  username: this.username,
                  email: '',
                  password: this.changePwdForm.newPassword
                })
                .then(successResponse => {
                  if (successResponse.data.code === 200){
                    this.$notify({
                      title: '修改成功',
                      message: "已设置新的密码，请重新登录",
                      type: "success",
                      duration: 1500
                    })
                    _this.$router.replace('/login')
                    this.clearUserInfo()
                  }
                }) 
                .catch(failResponse =>{
                })
            } else {
              this.$notify({
                type: 'error',
                title: '修改失败',
                message: '原密码错误，请重新输入',
                duration: 1500
              })
              this.clear();
              return
            }
          })
          .catch(failResponse =>{
          })
      },

      clearUserInfo() {
        Cookie.set('username','')
        Cookie.set('auth','')
        Cookie.set('email','')
      },


      // 修改邮箱
      onSumitNewEmail() {
        var _this = this
        if (this.changeEmailForm.newEmail === ''){
          this.$notify({
            title: '错误',
            message: '新邮箱地址不能为空！',
            type: 'error',
            duration: 1500
          })
          this.clear()
          return
        }
        if (this.changeEmailForm.newEmailConfirm === ''){
          this.$notify({
            title: '错误',
            message: '请重复输入邮箱地址！',
            type: 'error',
            duration: 1500
          })
          this.clear()
          return
        }
        if (this.changeEmailForm.newEmail !== this.changeEmailForm.newEmailConfirm){
          this.$notify({
            title: '错误',
            message: '两次输入的邮箱不一致',
            type: 'error',
            duration: 1500
          })
          this.clear()
          return
        }
        this.$axios
          .post('/changePwdOrEmail',{
            username: this.username,
            email: this.changeEmailForm.newEmail,
            password: ''
          })
          .then(successResponse => {
            if(successResponse.data.code === 200){
              alert("预留邮箱已修改为: " + this.changeEmailForm.newEmail)
              Cookie.set('email',this.changeEmailForm.newEmail)
              this.showEmail()
            }
            this.cancelForm();
          })
          .catch(failResponse =>{
          })
      },
      showEmail() {
        return Cookie.get("email")
      },
      returnAuth() {
        var isAdmin = Cookie.get('auth')
        return (isAdmin === 'false') ? true:false
      }
    }
  }
</script>

<style scoped>
  .menus {
    position: fixed;
    margin-left: 50%;
    left: -900px;
    top: 200px;
    width: 200px;
  }
</style>