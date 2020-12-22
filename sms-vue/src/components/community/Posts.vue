<template>

<div class="main" >
  <div class="toolbar">
    <tool-bar @onSearch="searchResult" @onLoad="changeLoad" @onView="viewPost"  ref="toolBar"></tool-bar>
  </div>
  <div class='posts' style="height:850px;overflow-y:auto;">
    <el-row> 
    <div class="posts" style="margin-bottom: 50px; display: flex;justify-content: center;align-items: center; ">
    <el-timeline >
      <el-timeline-item :timestamp="item.createDate" placement="top" type='info' size='large' style="padding: 150px"
        v-for="item in posts" :key="item.id">
        <el-card style="width:800px;height: 240px; float: left; margin-right: 20px;" shadow="hover" >
          <el-row gutter="4">
            <el-col :span="4" style="width: 300px">
              <div class="cover">
              <span style="float: left; font-size: 16px; margin-bottom: 2px; padding: 5px; color:#909399;">订阅名称：</span>
              <span style="float: left; font-size: 16px; margin-bottom: 2px; padding: 5px; color:#409EFF;">{{item.title}}</span>
               <img :src="item.category.cover" style="width:300px" >
              </div>
            </el-col>
            <el-col :span="14" style="width: 100px float: right">
                <!-- 评论模块 -->
                <el-popover
                  placement="right"
                  :title="getCommentTitle(item.title)"
                  width="400"
                  height="800"
                  trigger="click">
                  <el-container style="height:500px;overflow-y:auto;" >
                    <el-header>
                      <i class="el-icon-user-solid">{{item.user.username}}(发布者)</i>
                      <el-button size="mini" type="text" @click="reviewNew(item)"  >发表新评论</el-button>
                      <el-divider><i class="el-icon-chat-dot-square"></i></el-divider>
                    </el-header>
                    <el-main v-loading="loading">
                      <div class="comment-area"  v-for="comment in comments" :key="comment.id" >
                        <i :class="returnUserIcon(item.user.username,comment.username)">&nbsp;{{returnUserName(item.user.username,comment.username)}}</i>
                        <el-button size="mini" type="text" @click="reviewUser(item,comment)" >回复他</el-button>
                        <el-card shadow="hover">
                          <span>
                            <!-- 引用回复模块 -->
                            <blockquote style="color:#C0C4CC" :hidden="hasReviewText(comment.reviewText)">
                              <span style="color: #C0C4CC">
                                回复：“</span>
                              <el-button type="text" size="mini">@{{comment.toUsername}}：</el-button>
                              {{comment.reviewText}}”
                              <!-- 对话回溯功能实现 -->
                              <el-popover
                                title="对话回溯"
                                placement="left"
                                width="260"
                                trigger="click">
                                <!-- 历史评论显示模块 -->
                                <div class="comment-history">
                                  <br/>
                                  <el-timeline>
                                    <el-timeline-item
                                      v-for="historyComment in historyComments" :key="historyComment.id"
                                      :timestamp="historyComment.sendTime"
                                      :icon="returnUserIcon(item.user.username,historyComment.username)"
                                      size="large">
                                      <span style="color: #C0C4CC">{{returnUserName(item.user.username,historyComment.username)}}</span>：{{historyComment.sendText}}
                                    </el-timeline-item>
                                  </el-timeline>
                                </div>
                                <el-button slot="reference" type="text" size="mini" icon="el-icon-more" @click="loadHistoryComment(comment.reviewId)"></el-button>
                              </el-popover>
                              </blockquote>
                            {{comment.sendText}}
                          </span><br/>
                        <i class="el-icon-time" style="float: right;color: #C0C4CC;">{{comment.sendTime}}</i> </el-card>
                      </div>
                    </el-main>
                    <el-footer>
                      <el-input class="comment-input"
                        type="text"
                        :placeholder="reviewHolder"
                        maxlength="20"
                        show-word-limit
                        v-model="inputText"></el-input>
                      <el-button class="comment-commit"
                        @click="review(item)"
                        plain
                        type="primary"> 回 复 </el-button>
                    </el-footer>
                  </el-container>
                    <el-button  size="medium"  icon="el-icon-chat-line-square" circle  slot="reference" @click="loadComments(item)" style="margin-left: 5px; float: right" >
                    </el-button>
                </el-popover>
              <el-tooltip content="发邮件联系" placement="top" effect="light">
                <el-button  size="medium" type="primary" icon="el-icon-message" circle @click="contact(item)" style="margin-left: 5px; float: right" >
                </el-button>
              </el-tooltip>
              <el-tooltip content="收藏" placement="top" effect="light">
                <el-button  size="medium" icon="el-icon-star-off" type="warning" circle @click="collectIt(item)"  style="margin-left: 5px; float: right;" >
                </el-button>
              </el-tooltip>
              <el-row style=" float: left;">
                <el-col :span="8" style="width: 300px">
                  <span style="float:left; padding: 8px; font-size: 14px; color:#909399;">订阅类别：</span>
                  <span style="float:left; padding: 8px; font-size: 14px">{{item.category.name}}</span>
                </el-col>
                <el-col :span="8" style="width: 300px">
                  <span style="float:left; padding: 8px; font-size: 14px; color:#909399;">订阅单价：</span>
                  <span style="float:left; padding: 8px; font-size: 14px">{{item.price}}/{{item.payWay}} 1人</span>
                </el-col>
                <el-col :span="8" style="width: 300px">
                  <span style="float:left; padding: 8px; font-size: 14px; color:#909399;">订阅时长：</span>
                  <span style="float:left; padding: 8px; font-size: 14px">{{item.cycle}}  * 1 {{item.payWay}}，发布时间:{{item.createDate}}</span>
               </el-col>
               <el-col :span="8" style="width: 300px">
                  <i class="el-icon-s-claim" style="float:left; padding: 10px; font-size: 17px; color:#909399;"> 总计：</i>
                  <span style="float:left; padding: 8px; font-size: 17px; color:#409EFF">{{item.sum}} 元</span>
                </el-col>
                <el-col :span="14" style="width: 200px; float: right;">
                  <i class="el-icon-user-solid" style="float: right; color: #C0C4CC"> {{item.user.username}},发布与{{item.createDate}}</i>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </el-card>
      </el-timeline-item>
    </el-timeline>
    </div>
      <p style="color: #C0C4CC"> - ╮(￣▽￣"")╭再也没有了 - </p>
      <el-link icon='el-icon-upload2' href="#topAnchor" style="right:0;bottom:0">回到顶部</el-link>
  </el-row>
  </div>
  
</div>
</template>
<script>
import ToolBar from './ToolBar.vue'
import Cookie from 'js-cookie'
import CommentArea from './CommentArea.vue'
import { duration } from 'moment'
export default {
  name: 'Posts',
  components:{ToolBar, CommentArea},
  data () {
      return {
        posts: [],
        comments: [],
        historyComments: [],
        inputText: '',
        reviewHolder: '',
        reviewId: 0,
        reviewUsername: '',
        reviewUserText: '',
        loading : false
      }
    },
    mounted: function() {
      this.loadPosts()
    },
    methods: {
      loadPosts() {
        var url = '/posts/available'
        this.$axios.get(url).then(resp => {
          if (resp && resp.status === 200){
            this.posts = resp.data
          }
        })
      },
      // 发送邮件联系
      contact(item){
        var url = 'mailto:' + item.user.email + '?subject=关于您发布的多人订阅%20\"' + item.title + '\"%20的咨询'
        window.open(url,'_self')
      },

      // 搜索
      searchResult(){
        var radio = this.$refs.toolBar.radio
        var username = Cookie.get('username') 
        var keywords = this.$refs.toolBar.keywords
        var id =  this.$refs.toolBar.postId
        var searchWord = (keywords === '') ? "":("中 - 搜索：" + keywords)
        var url = '/' 
        var messageSt = '当前显示'
        if (radio === '2'){
          messageSt += '我的' 
          username = Cookie.get('username')
          url += (username + '/')  
        }else if (radio === '3'){
          messageSt += '收藏夹'
          url += (username + '/collected/') 
        }else 
          messageSt += '全部' 
        this.$notify({
          title: messageSt + searchWord,
          type: 'success',
          duration: 1500
        });
        /**
         * 实现三种模式下搜索，对应的url情况：
         * url:
         *     我的：/{username}/posts/search?keywords
         *     收藏夹：/{username}/collected/posts/search?keywords
         *     全部：/posts/search?keywords
         */
        url += 'posts/search?keywords='
        this.$axios
          .get(url + keywords, {})
          .then(resp => {
            if (resp && resp.status === 200){
              this.posts = resp.data
            }
          })
      },
      viewPost(){
        var _this = this
        var pid = this.$refs.toolBar.postId
        var url = '/posts/' + pid
        this.$axios
          .get(url, {})
          .then(resp => {
            if (resp && resp.status === 200) {
              if (resp.data.length === 0 ){
                this.$notify({
                  title: '查看失败',
                  message: '该帖可能已被隐藏或删除',
                  type: 'error',
                  duration: 1500
                })
                this.$refs.toolBar.radio = '1'
                this.loadPosts()
                return
              }
              this.posts = resp.data
              this.$notify({
                title: '查看成功',
                type: 'success',
                duration: 1500
              })
            }
            else{
            }
          })
      },

      /**
       *  评论区模块
       */
      // 加载评论
      loadComments(item){
        var url = '/posts/' + item.id + '/comments'
        this.reviewHolder = '发表新评论'
        this.reviewUsername = item.user.username
        this.reviewUserText = ''
        this.reviewId = 0
        this.inputText = ''
        this.$axios.get(url).then(resp => {
          if (resp && resp.status === 200) {
            this.comments = resp.data
          }
        })
      },
      // 加载历史评论
      loadHistoryComment(reviewId){
        var url = '/comments/' + reviewId + '/history'
        this.$axios.get(url).then(resp => {
          if (resp && resp.status === 200) {
            this.historyComments = resp.data
          }
        })
      },
      // 发表新评论
      reviewNew(item){
        this.loadComments(item)
      },

      // 普通回复，直接回复给发布者
      review(item){
        this.loading = true
        var messageSt = (this.reviewUserText === '' ? "发表":"回复")
        if (this.inputText === ''){
          this.$notify.error({
            title:"评论" + messageSt + "失败",
            message: "请输入内容"
          })
          return
        }
        this.$axios
          .post('/posts/comments',{
            pid: item.id,
            username: Cookie.get('username'),
            toUsername: this.reviewUsername,
            sendTime: this.getNowTime(),
            sendText: this.inputText,
            reviewText: this.reviewUserText,
            reviewId: this.reviewId
          }).then(resp => {
            if (resp && resp.status === 200) {
              this.$notify({
                title: "评论" + messageSt + "成功",
                type: "success",
                duration: 1500
              })
              this.loading = false
              this.loadComments(item)
            }
          })
      },
      // 引用回复，同时在回复框显示回复对象
      reviewUser(item,comment){
        this.reviewId = comment.id
        this.reviewUsername = comment.username
        this.reviewUserText = comment.sendText
        this.displayReviewName(comment.username)
      },

      // 得到评论标题
      getCommentTitle(title) {
        return "“" + title + "”的评论" 
      },
      // 判断评论是否有回复需要引用
      hasReviewText(reviewText){
        return (reviewText === '')
      },
      // 显示回复用户名
      displayReviewName(username) {
        this.reviewUsername = username
        this.reviewHolder =  "回复：" + this.reviewUsername
      },

      // 返回回复者头像（区分发布者）
      returnUserIcon(postUsername,commentUsername){
        return (postUsername === commentUsername) ? "el-icon-user-solid":"el-icon-user"
      },
      // 返回回复用户名（区分发布者）
      returnUserName(postUsername,commentUsername) {
        return (postUsername === commentUsername) ? postUsername + "(发布者)":commentUsername
      },
      // 返回当前时间
      getNowTime() {
        var yy = new Date().getFullYear();
		    var mm = new Date().getMonth()+1;
		    var dd = new Date().getDate();
		    var hh = new Date().getHours();
		    var mf = new Date().getMinutes()<10 ? '0'+new Date().getMinutes() : new Date().getMinutes();
        var ss = new Date().getSeconds()<10 ? '0'+new Date().getSeconds() : new Date().getSeconds();
		    return yy+'-'+mm+'-'+dd+' '+hh+':'+mf+':'+ss;
      },


      /**
       *  收藏模块
       */


      collectIt(item){
        this.$axios
          .post('/collect',{
            username: Cookie.get('username'),
            post: item
          })
          .then(successResponse => {
            if (successResponse.data.code === 200) {
              this.$notify({
                title: "收藏成功",
                type: "success",
                message : "已添加：" + item.title + " 到收藏夹。",
                duration: 1500
              })
            }else {
              this.$notify({
                title: "取消成功",
                type: "success",
                message : "已将：" + item.title + " 从收藏夹移除。",
                duration: 1500
              })
            }
          })
      },
      isCollect(id){
        var url = '/post/' + id + '/' + Cookie.get('username') + '/collected'
        this.$axios
          .get(url,{}).then(resp => {
            if (resp && resp.status === 200) {
              if (resp.data.collected === 'true')
                return "el-icon-star-on"
            }
        })
      }
    }
}
</script>
<style scoped>
  .cover{
    width:100%;
    margin-bottom: 0px;
  }
  .timeline {
    width: 100%;

  }
  .el-row {
    margin-bottom: 20px;
  }

  .el-col {
    border-radius: 4px;
  }

    .el-header .el-footer {
    text-align: center;
    line-height: 10px;
  }
  .el-main{
    margin-bottom: 20px;
  }

  .comment-input{
    width: 79%;
  }
  .comment-commit{
    width: 20%;
  }

</style>
