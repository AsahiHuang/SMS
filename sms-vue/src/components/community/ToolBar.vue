<template>
  <div style="margin-bottom: 20px; position: relative; justify-content: center;align-items: center">
    <el-radio-group v-model="radio" @change="searchClick" size="medium">
      <el-radio-button label="1" >查看全部</el-radio-button>
      <el-radio-button label="2" >只看我的</el-radio-button>
      <el-radio-button label="3" >收藏夹</el-radio-button>
    </el-radio-group>
    <el-input
      @keyup.enter.native="searchClick"
      placeholder="输入并按下回车搜索..."
      prefix-icon="el-icon-search"
      size="medium"
      style="width: 560px;margin-right: 60px;margin-left: 60px"
      v-model="keywords">
       <template slot="prepend">订阅名称</template>
    </el-input>
        <el-popover
      placement="bottom"
      width="550"
      trigger="click">
      <div class="news">
        <el-menu  mode="horizontal" :default-active="activeIndex" @select="handleSelect" style="width:100%">
          <el-menu-item index="1">回复我的</el-menu-item>
          <el-menu-item index="2">我的评论</el-menu-item>
        </el-menu>
        <el-table :data="news" align="center"  height="500" style="width: 150%" >
          <el-table-column type="expand" >
            <template slot-scope="props">
              <el-form label-position="left" inline class="table-expand">
                <el-form-item label="评论用户：">
                  <span>{{ returnUsername(props.row) }}</span>
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
            align="center"
            width="80">
            <template slot-scope="scope">
              <span>{{returnUsername(scope.row)}}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="时间"
            prop="sendTime"
            align="center"
            width="140">
          </el-table-column>
          <el-table-column
            label="回复内容"
            prop="sendText"
            align="center"
            width="180">
          </el-table-column>
          <el-table-column
            align="center"
            fixed="right"
            label="查看"
            width="100">
            <template slot-scope="scope">
              <el-button size="mini" @click="viewPost(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-button size="medium" icon="el-icon-news" style="margin-left: 20px" slot="reference" @click="loadNews()">评论</el-button>
    </el-popover>
    <el-button size="medium" type="primary" plain  icon="el-icon-edit-outline"  @click="dialogFormVisible = true" style="margin-left: 20px;" >发 帖</el-button>
    <el-dialog
      title="在社区发布多人订阅信息"
      :visible.sync="dialogFormVisible"
      @close="clear">
      <el-form v-model="form" style="text-align: left" ref="dataForm">
        <el-form-item label="订阅名称:" :label-width="formLabelWidth" prop="title">
          <el-input style="width:400px" type="text" v-model="form.title" autocomplete="off" placeholder="输入格式：订阅名称 - 人数"></el-input>
          <br/>
          <i class="el-icon-warning" style="color:#C0C4CC">标题请附上订阅人数信息，如“美团会员 5人”</i>
        </el-form-item>
        <el-form-item label="类别:" :label-width="formLabelWidth" prop="cid">
          <el-select v-model="form.category.id" placeholder="请选择类别:">
            <el-option label="音乐" value="1"></el-option>
            <el-option label="视频" value="2"></el-option>
            <el-option label="购物" value="3"></el-option>
            <el-option label="外卖" value="4"></el-option>
            <el-option label="游戏" value="5"></el-option>
            <el-option label="其他" value="6"></el-option>
          </el-select>
        </el-form-item>        
        <el-form-item label="订阅方式:" :label-width="formLabelWidth" prop="payWay">
            <el-switch v-model="form.payWay"
             active-text="按月付费" 
             inactive-text="按年付费"
             active-value="月"
             inactive-value="年">
          </el-switch>
        </el-form-item>
        <el-form-item label="订阅单价:" :label-width="formLabelWidth" prop="price">
           <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
           <el-input-number v-model="form.price" @change="handleChange" :min="0" :max="1000" :precision="2" label="订阅价格"></el-input-number>
           <label>&nbsp;&nbsp;元/{{form.payWay}}</label>          
        </el-form-item>
        <el-form-item label="订阅长度:" :label-width="formLabelWidth" prop="cycle">
          <label>1&nbsp;x&nbsp;</label>
          <el-input-number v-model="form.cycle" :step="1" :min="1" :max="100"></el-input-number>
           <label>&nbsp;&nbsp;{{form.payWay}}</label>          
        </el-form-item>
        <br/>
        <el-form-item label="总计:" :label-width="formLabelWidth" prop="sum" style="color:#3377aa" >
          <label>{{form.sum=returnSumPrice(form.price,form.cycle)}}&nbsp;元</label>
        </el-form-item>
        <br/>
        <i class="el-icon-info" style="margin-left: 80px;color:#C0C4CC">&nbsp;本帖将由&nbsp;{{this.username}}&nbsp;发布于&nbsp;{{form.createDate=returnNowDate()}}&nbsp;</i>
        <el-form-item prop="id" style="height: 0">
          <el-input type="hidden" v-model="form.id" autocomplete="off"></el-input>
        </el-form-item>        
        <el-form-item prop="username" style="height: 0">
          <el-input type="hidden" v-model="form.user.username" autocomplete="off"></el-input>
        </el-form-item>        
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-popconfirm
          confirmButtonText='已确认'
          cancelButtonText='继续填写'
          icon="el-icon-info"
          iconColor="red"
          title="发布后您将不能再作修改，请确保所填写信息及预留邮箱正确"
          @onConfirm="createPost()"
        >
          <el-button  type="primary"  slot="reference"  >发 布</el-button>
        </el-popconfirm>
      </div>
    </el-dialog>

  </div>

</template>

<script>
  import Cookie from "js-cookie"
  import Moment from "moment"
  export default {
    name: 'ToolBar',
    data () {
      return {
        keywords: '',
        posts: [],
        postLoading: [],
        news: [],
        postId: 0,
        sumPrice: 0,
        dialogFormVisible : false,
        username: Cookie.get('username'),
        radio: '1',
        form: {
          id: '',
          title: '',
          createDate: '',
          payWay: '月',
          price: '0',
          cycle: '0',
          category: {
            id: '',
            name: ''
          },
          user: {
            id: '',
            username: Cookie.get('username') 
          },
          sum: '0',
          available: true 
        },
        formLabelWidth: '150px',
        activeIndex: '1'
      }
    },
    methods: {
      searchClick () {
        this.$emit('onSearch')
      },
      viewPost(item){
        this.$emit('onView')
      },
      // 清空表单
      clear(){
        this.form={
          id: '',
          title: '',
          createDate: '',
          payWay: '月',
          price: '0',
          cycle: '0',
          category: {
            id: '',
            name: ''
          },
          user:{
            id: '',
            username: Cookie.get('username') 
          },
          sum: '0',
          available: true,
          activeIndex: '1'
        }
      },
      //返回当前时间
      returnNowDate(){
        var nowDate = new Date()
        var year = nowDate.getFullYear()
        var month = nowDate.getMonth() + 1
        var day = nowDate.getDate()
        if (month < 10) month = '0' + month
        if (day < 10) day = '0' + day
        return year + '-' + month + '-' +day
      },

      //创建帖子
      createPost(){
        this.$axios
          .post('/posts', {
            id: this.form.id,
            title: this.form.title,
            createDate: this.form.createDate,
            payWay: this.form.payWay,
            price: this.form.price,
            cycle: this.form.cycle,
            category: this.form.category,
            user: this.form.user,
            sum: this.form.sum,
            available: this.form.available
          }).then(resp=> {
            if (resp && resp.status === 200) {
              this.dialogFormVisible = false
              // 调用父组建刷新
              this.$parent.loadPosts()
              this.$notify({
                title: '发布成功',
                message: '已发布的帖可在 “管理中心-订阅管理-多人订阅” 中进行查看、隐藏、删除等操作',
                type: 'success'
              });
              this.clear()
            }
          })
      },
      returnSumPrice(cycle,price){
        return cycle*price.toString()
      },

      returnUsername(item){
        var username = (this.activeIndex === '1') ? (item.username) : (item.toUsername)
        return (username != Cookie.get('username')) ? username : "你(" + Cookie.get('username') +")"
      },
      loadNews(){
        var username = Cookie.get('username')
        // 模式1:回复我的，模式2:我的评论
        var model = this.activeIndex === '1' ? "/news" : "/reviews"
        var url = '/' + username + model 
        this.$axios.get(url).then(resp => {
          if (resp && resp.status === 200) {
            this.news = resp.data
          }
        })
      },
      handleSelect(key, keyPath){
        this.activeIndex = key
        this.loadNews()
      },
      viewPost(item) {
        this.postId = item.pid
        this.radio = '4' 
        this.keywords = ''
        this.$emit('onView')
      }
    }
  }
</script>

<style scoped>
  .table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }

</style>