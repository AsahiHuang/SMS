<template>
  <div class="SubManageForm" style="padding: 20px">
    <el-button size="mini" type="primary" plain icon="el-icon-circle-plus-outline" style="margin-left: 975px" @click=editSub()>添加</el-button>
    <el-table
      :data="subs"
      align="center"
      stripe="true"
      height="850"
      style="width: 150%">
      <el-table-column
        type="index"
        width="40">
      </el-table-column>
      <el-table-column
        prop="title"
        label="名称"
        width="150">
      </el-table-column>
      <el-table-column
        align="center"
        label="类型"
        width="100">
        <template slot-scope="scope">
          <el-tag size="medium"
          :type="tagColor(scope.row.category.id)"
          :effect="tagEffect(scope.row.category.id)">
            {{ scope.row.category.name }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="overDate"
        label="到期时间"
        width="120">
      </el-table-column>
      <el-table-column
        align="center"
        prop="payWay"
        label="付款方式"
        width="120">
      </el-table-column>
      <el-table-column
        align="center"
        prop="price"
        label="价格(元)"
        width="120">
      </el-table-column>
      <el-table-column
        prop="comment"
        label="备注"
        width="260">
      </el-table-column>
      <el-table-column
        align="center"
        label="修改/删除"
        width="200">
        <template slot-scope="scope">
          <el-button 
          icon="el-icon-edit-outline"
          size="mini" @click="editSub(scope.row)"></el-button>
          <el-popconfirm
            confirmButtonText='确认'
            cancelButtonText='取消'
            icon="el-icon-warning"
            iconColor="red"
            title="确认删除？"
            @onConfirm="deleteSub(scope.row)">
            <el-button 
              slot="reference"
              icon="el-icon-delete"
              type="danger" size="mini" ></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
          <sub-edit-form @onSubmit="loadSubs()" ref="edit"></sub-edit-form>
    </el-table>
  </div>
</template>

<script>
import Cookie from "js-cookie"
import SubEditForm from "./SubEditForm"
export default {
  name: 'SubManageForm',
  components: {SubEditForm},
  data() {
    return {
      subs: [] ,
      EditDrawerDialog: false
    }
  },
  mounted: function (){
    this.loadSubs()
  },
  methods: {
    loadSubs() {
      var _this = this
      var username = Cookie.get('username')
      var url = '/' + username + '/subs'
      this.$axios.get(url).then(resp => {
        if (resp && resp.status === 200) {
          this.subs = resp.data
        }
      })      
    },
    editSub (item) {
      this.$refs.edit.dialogFormVisible = true
      this.$refs.edit.form = {
        id: item.id,
        title: item.title,
        category: {
          id: item.category.id.toString(),
          name: item.category.name,
          cover: item.category.cover
        },
        overDate: item.overDate,
        price: item.price,
        payWay: item.payWay,
        comment: item.comment,
        user: Cookie.get('username')
      }
    },    
    deleteSub (item) {
      this.$axios
        .post('/delete', {
          id: item.id,
          title: item.title,
          user: Cookie.get('username')
          })
        .then(resp => {
          if (resp && resp.status === 200){
            this.loadSubs()
            this.$notify({
              title: '删除成功',
              type: 'success',
              duration: 1500
            });            
          }
        })
        .catch(failResponse => {
      })
    },
    tagColor(id){
      var colors = ["danger","","warning","warning","success","info"]
        return colors[id-1] 
    },
    tagEffect(id){
      if (id === 4)
        return "plain"
      else
        return "light"
    }

  }
}
</script>

<style>

</style>