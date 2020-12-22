<template>
  <div class="UserManageForm" style="padding: 20px">
    <i class="el-icon-info" style="color: #C0C4CC;font-size: 14px"> 你不能在“用户管理（管理员）”里修改你自己的项目，包括权限及密码。所有用户的ID和用户名具有唯一性，故后台不支持修改。</i>
    <el-table
      :data="users"
      align="center"
      stripe="true"
      height="850"
      style="width: 150%">
      <el-table-column
        align="center"
        prop="id"
        label="用户ID"
        width="80">
      </el-table-column>
      <el-table-column
        align="center"
        prop="username"
        label="用户名"
        width="200">
      </el-table-column>
      <el-table-column
        align="center"
        prop="email"
        label="邮箱地址"
        width="220">
      </el-table-column>
      <el-table-column
        align="center"
        label="身份权限"
        width="150">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.auth"
            active-text="管理员"
            @change="changeAuth(scope.row.username,scope.row.auth)"
            :disabled="ifUserIsMe(scope.row.username)"
            >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="重置密码/删除用户"
        width="260">
        <template slot-scope="scope">
          <el-popover
            placement="bottom"
            title="重置密码"
            width="220">
          <el-input type="password" v-model="password" show-password
                    auto-complete="off" placeholder="输入新密码">
          </el-input>
          <div style="text-align: right; padding-top: 10px">
            <el-button type="primary" size="mini" @click="editUserPassword(scope.row,password)">确定</el-button>
          </div>
          <el-button 
          icon="el-icon-edit-outline"
          :disabled="ifUserIsMe(scope.row.username)"
          size="mini" 
          slot="reference">
          </el-button>
          </el-popover>
          <el-button 
              icon="el-icon-delete"
              @click="deleteUser(scope.row)"
              :disabled="ifUserIsMe(scope.row.username)"
              type="danger" size="mini" ></el-button>
        </template>
      </el-table-column>

    </el-table>
  </div>
</template>

<script>
import Cookie from "js-cookie"
export default {
  name: 'UserManageForm',
  data() {
    return{        
      users: [],
      visible: false,
      password: ''
    }
  },
  mounted: function() {
    this.loadUsers()
  },

  methods: {
    loadUsers() {
      var url = '/users'
      this.$axios.get(url).then(resp => {
        if (resp && resp.status === 200)
          this.users = resp.data
          this.auth = this.users.auth
        }
    )},
    changeAuth(username,auth){
      this.$axios
        .post('/user/updateAuth',{
          username: username,
          // 这里借用salt传递操作对象
          salt: Cookie.get('username')
        })
        .then(successResponse => {
          if (successResponse.status === 200) {
            var messageSt = ""
            if (auth === true)
              messageSt = "已将用户  " + username + "  设置为管理员"
            else
              messageSt = "已取消用户  " + username + "  的管理员权限" 
            this.loadUsers()
            this.$notify({
              title: "操作成功",
              message: messageSt,
              type: "success",
              duration: 1500
            })
          }
        })
        .catch(failResponse => {
        })
    },

    // 当前用户的项目不能作部分修改
    ifUserIsMe(username){
      return (Cookie.get('username') === username) ? true:false
    },
    editUserPassword(item,password){
      if (password === ''){
        this.$notify({
          title: '错误',
          message: '新密码不能为空',
          duration: 1500
        })
        return
      }else{
        var _this = this
        this.$axios
          .post('/user/resetPwd',{
              username: item.username,
              password: password,
              salt: Cookie.get('username')
          })
          .then(successResponse => {
            if (successResponse.status === 200) {
              this.$notify({
                title: '重置成功',
                message: '已设置新的密码',
                type: 'success',
                duration: 1500
              })
              this.password = ''
            }
          })
      }
    },
    deleteUser(item){
      var _this = this
      this.$confirm('删除用户同时将清空该用户的所有信息（包括个人订阅和多人订阅）','警告',{
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
      this.$axios
        .post('/user/delete', 
          {
            id: item.id,
            username: item.username ,
            salt: Cookie.get('username')
          }).then(successResponse => {
          if (successResponse.status === 200){
            this.loadUsers()
            this.$notify({
              title: '删除成功',
              message: '已将用户：' + item.username + "删除",
              type: 'success',
              duration: 1500
            })
          }
        }).catch(failResponse => {
      })
     })
    }
  }

  
}
</script>

<style scoped>

</style>