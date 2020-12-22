<template>
  <div class="Panel">
    <el-row class="panel-row" :gutter="20">
      <el-col :span="6">
      <el-card class="table-card">
        <div slot="header" class="card-header">
          <i class="el-icon-data-line" >&nbsp;数据统计</i>
        </div>
        <div class="data-table">
          <data-table ref="dataTable"></data-table>
        </div>
      </el-card>

      </el-col>
      <el-col :span="14">
      <el-card class="log-card">
        <div slot="header" class="log-header">
          <i class="el-icon-receiving">&nbsp;操作记录</i>
        </div>
        <div class="log-table">
          <log-table ref="logTable"></log-table>
        </div>
      </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import DataTable from './DataTable.vue'
import LogTable from './LogTable.vue'
export default {
  name: 'Panel',
  components: {LogTable, DataTable},
  data() {
    return{
    }
  },
  methods: {

  }
}

</script>
<style scoped>
  .panel-row {
    width: 1550px;
    height: 800px;
  }
  .log-header {
    size: 20px;
  }
  .log-card {
    width: 1000px;
    height: 800px;
  }
  .table-card {
    width: 360px;
    height: 520px;
    margin-bottom: 10px;
  }

</style>