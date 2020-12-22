<template>
  <div class="CommunityManageForm" style="padding: 20px">
    <div style="width: 800px;margin-left: 250px;margin-bottom: 10px">
      <el-input
        placeholder="选择搜索类型并输入" 
        v-model="keywords" 
        class="input-with-select"
        @keyup.enter.native="searchClick">
        <el-select class="search-select" v-model="select" slot="prepend" placeholder="请选择">
          <el-option label="用户名" value="1"></el-option>
          <el-option label="订阅名称" value="2"></el-option>
        </el-select>
        <el-button slot="append" icon="el-icon-search" @click="searchClick"></el-button>
      </el-input>
    </div>
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
        prop="user.username"
        label="发布者"
        width="120">
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
        prop="available"
        label="状态"
        width="80">
        <template slot-scope="scope">
          <span>{{isAvailable(scope.row.available)}}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        width="150">
        <template slot-scope="scope">
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
          <!-- 评论管理 -->
          <el-popover
            placement="bottom"
            width="550"
            trigger="click">
            <div class="reviews">
              <span>“{{scope.row.title}}” - 评论管理</span>
              <el-table :data="reviews" align="center"  height="500" style="width: 150%" >
                <el-table-column type="expand" >
                  <template slot-scope="props">
                    <el-form label-position="left" inline class="table-expand">
                      <el-form-item label="评论用户：">
                        <span>{{ props.row.username }}</span>
                      </el-form-item>
                      <el-form-item label="评论时间：">
                        <span>{{ props.row.sendTime }}</span>
                      </el-form-item>
                      <el-form-item label="评论内容：">
                        <span>“{{ props.row.sendText }}”</span>
                      </el-form-item>
                      <el-form-item label="评论引用：">
                        <span>“@{{props.row.toUsername}}:{{ props.row.reviewText }}”</span>
                      </el-form-item>
                    </el-form>
                   </template>
                </el-table-column>
                <el-table-column
                  label="用户"
                  prop="username"
                  align="center"
                  width="80">
                </el-table-column>
                <el-table-column
                  label="时间"
                  prop="sendTime"
                  align="center"
                  width="140">
                </el-table-column>
                <el-table-column
                  label="发布内容"
                  prop="sendText"
                  align="center"
                  width="180">
                </el-table-column>
                <el-table-column
                  align="center"
                  fixed="right"
                  label="删除评论"
                  width="100">
                  <template slot-scope="props" >
                    <el-button icon="el-icon-delete"  size="mini" type="danger" @click="deleteReview(props.row.id,scope.row.id)"></el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <el-button size="mini" icon="el-icon-news" slot="reference" @click="loadReviews(scope.row.id)" ></el-button>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import Cookie from "js-cookie"
export default {
  name: 'CommunityManageForm',

  data() {
    return {
      posts: [],
      reviews: [],
      keywords: '',
      select: '1'
    }
  },
  mounted: function() {
    this.loadAllPosts()
  },
  methods: {
    loadAllPosts() {
      var _this = this
      var url =  '/posts'
      this.$axios.get(url).then(resp=> {
        if (resp && resp.status === 200)
          this.posts = resp.data
      })
    },
    searchClick() {
      var _this = this
      var selectValue = this.select
      var keywords = this.keywords
      var url = '/posts'
      url += ((selectValue === '1') ? '/username' : '/title')
      url += '/search?keywords='
      this.$axios
        .get(url + keywords, {})
        .then(resp => {
          if (resp && resp.status === 200) {
            this.posts = resp.data
          }
        })
    },
    deletePost (id) {
      this.$axios
        .post('/posts/delete', {id: id,user : {username: Cookie.get('username')}}).then(resp =>{
          if (resp && resp.status === 200)
            this.loadAllPosts()
            this.$notify({
              title: '删除成功',
              type: 'success',
              duration: 1500
            });
        })
        .catch(failResponse =>{
        })
    },
    
    showStatus(available){
      return '#f0f9eb'
    },

    // 返回评论
    loadReviews(id) {
      var url = '/posts/' + id + '/comments'
      this.$axios.get(url).then(resp => {
        if (resp && resp.status === 200) {
          this.reviews = resp.data
        }
      })
    },
    deleteReview(rid,pid) {
      this.$axios
        .post('/reviews/delete' , {id : rid,username: Cookie.get('username')}).then(resp =>{
          if (resp && resp.status === 200) {
            this.$notify({
              title: '删除成功',
              type: 'success',
              duration: 1500
            })
            this.loadReviews(pid)
          }
        })
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
    },
    isAvailable(available){
      return (available === true) ? "显示" : "隐藏"
    }
  }
}
</script>
<style scoped>
  .search-select  {
    width: 130px;
  }
  .input-with-select {
    background-color: #fff;
  }
</style>