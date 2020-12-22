<template>
  <div class="PostManageForm" style="padding: 20px">
    <i class="el-icon-info" style="color: #C0C4CC;font-size: 14px">订阅社区发布的多人订阅自发布后不能修改，已失效的请删除或到“订阅社区-发帖”重新发布。</i>
    <el-table
      :data="posts"
      align="center"
      height="850"
      style="width: 150%"
      :row-class-name="tableRowClassName">
      <el-table-column
        type="index"
        width="60">
      </el-table-column>
      <el-table-column
        prop="title"
        label="标题"
        width="220">
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
        prop="createDate"
        label="发布时间"
        width="120">
      </el-table-column>
      <el-table-column
        align="center"
        prop="cycle"
        label="订阅时长"
        width="100">
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
        width="100">
      </el-table-column>
      <el-table-column
        align="center"
        prop="sum"
        label="总价(元)"
        width="100">
      </el-table-column>
      <el-table-column
        align="center"
        label="隐藏/删除"
        width="160">
        <template slot-scope="scope">
          <el-button
            @click="hidePost(scope.row)"
            icon="el-icon-view"
            size="mini">
          </el-button>
          <el-popconfirm
            confirmButtonText='确认'
            cancelButtonText='取消'
            icon="el-icon-warning"
            iconColor="red"
            title="确认删除？"
            @onConfirm="deletePost(scope.row.id)">
            <el-button 
              slot="reference"
              icon="el-icon-delete"
              type="danger" size="mini" ></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>    


  </div>
</template>

<script>
import Cookie from "js-cookie"
export default {
  name: 'PostManageForm',

  data() {
    return {
      posts: [],
    }
  },
  mounted: function (){
    this.loadPosts()
  },
  methods: {
    loadPosts() {
      var _this = this
      var username = Cookie.get('username')
      var url = '/' + username + '/posts'
      this.$axios.get(url).then(resp=> {
        if (resp && resp.status === 200)
          this.posts = resp.data
      })
    },
    deletePost (id) {
      this.$axios
        .post('/posts/delete', {id: id,user : {username: Cookie.get('username')}}).then(resp =>{
          if (resp && resp.status === 200)
            this.loadPosts()
            this.$notify({
              title: '删除成功',
              type: 'success'
            });
        })
        .catch(failResponse =>{
        })
    },
    hidePost(item) {
      var _this = this
      this.$axios
        .post('/post/available',{
          id: item.id,
        })
        .then(successResponse => {
          var messageSt = ""
          if (successResponse.status === 200) {
            if (item.available === true)
              messageSt = "已将“" + item.title + "”设置为隐藏"
            else
              messageSt = "已取消“" + item.title + "”隐藏"
            this.loadPosts()
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
    showStatus(available){
      return '#f0f9eb'
    },


    // 下面两个都是用来展示类别颜色标签
    tagColor(id){
      var colors = ["danger","","warning","warning","success","info"]
        return colors[id-1] 
    },
    tagEffect(id){
      if (id === 4)
        return "plain";
      else
        return "light";
    },
    tableRowClassName({row}) {
      if (row.available === false)
        return 'unavailable-row';
      return ''
    }
  }
}
</script>

<style>
  .el-table .unavailable-row {
    background: #EBEEF5;
  }
</style>