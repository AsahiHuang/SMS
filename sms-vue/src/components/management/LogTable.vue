<template>
  <div class="LogTable" style="padding: 0px">
    <el-button size="mini" type="primary" plain icon="el-icon-refresh" style="margin-left: 855px" @click=refreshLogs()>刷新</el-button>
    <el-table
      :data="logs"
      align="center"
      stripe="true"
      height="680"
      style="width: 100%"
      :default-sort = "{prop: 'id', order: 'descending'}">
      <el-table-column
        align="center"
        label="编号"
        sortable
        prop="id"
        width="80">
      </el-table-column>
      <el-table-column
        align="center"
        prop="type"
        label="类型"
        width="150"
        :filters="logFilters"
        :filter-method="filterHandler">
      </el-table-column>
      <el-table-column
        align="center"
        prop="time"
        label="时间(年-月-日T时:分:秒:毫秒)"
        width="200">
      </el-table-column>
      <el-table-column
        align="center"
        prop="name"
        label="操作对象名称"
        width="200">
      </el-table-column>
      <el-table-column
        align="center"
        prop="username"
        label="所属用户"
        width="100">
      </el-table-column>
      <el-table-column
        align="center"
        prop="handle"
        label="操作结果"
        width="120">
      </el-table-column>
      <el-table-column
        align="center"
        prop="executor"
        label="操作者类型"
        width="120">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import Cookie from "js-cookie"
export default {
  name: 'LogTable',
  data() {
    return {
      logs: [],
      logFilters: [
        {text: '到期删除',value: '个人-自动删除'},
        {text: '个人订阅删除',value: '个人-删除'},
        {text: '个人订阅添加/修改',value: '个人-添加/修改'},
        {text: '多人订阅删除',value: '多人-删除'},
        {text: '多人订阅修改状态',value: '多人-修改状态'},
        {text: '登录记录',value: '用户-登录'},
        {text: '修改个人信息',value: '用户-修改信息'}
      ]
    }
  },
  mounted: function (){
    this.loadLogs()
  },
  methods: {
    loadLogs() {
      var _this = this
      var username = Cookie.get('username')
      var url = '/' + username + '/logs'
      this.$axios.get(url).then(resp => {
        if (resp && resp.status === 200) {
          this.logs = resp.data
          this.changeLogFilters(this.logfilters)
        }
      })
      .catch(failResponse =>{})
    },
    refreshLogs(){
      var _this = this
      this.loadLogs()
      this.$notify({
        title: '刷新成功',
        type: 'success',
        duration: 1500
      })
    },
    changeLogFilters(logfilters) {
      var _this = this
      if (Cookie.get('auth') === 'true'){
        this.logFilters.push(
          {text: '*删除用户' , value: '用户-删除'},
          {text: '*修改权限' , value: '用户-修改权限'},
          {text: '*用户注册' , value: '用户-注册'})
      }
    },
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    }
  }
}
</script>
<style scoped>

</style>