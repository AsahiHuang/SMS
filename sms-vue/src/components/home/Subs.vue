<template>
  <div class="Subs">
    <el-row style="height: 850px;">
      <!--<search-bar></search-bar>-->
      <search-bar @onSearch="searchResult" ref="searchBar"></search-bar>
      <el-tooltip effect="dark" placement="right"
                  v-for="item in subs.slice((currentPage-1)*pagesize,currentPage*pagesize)"
                  :key="item.id">
        <p slot="content" style="font-size: 15px;margin-bottom: 6px;">{{item.title}}</p>
        <p slot="content" style="font-size: 13px;margin-bottom: 6px">
          <span>{{item.overDate}} 到期</span> 
        </p>
        <p slot="content" style="width: 200px" class="category">类别: {{item.category.name}}</p>
        <p slot="content" style="width: 200px" class="price">价格: {{item.price}} 元/{{item.payWay}}</p>
        <p slot="content" style="width: 250px" class="comment">备注: {{item.comment}}</p>
        <el-card style="width: 220px;margin-bottom: 20px;height: 220px;float: left;margin-right: 30px" class="sub"
                 bodyStyle="padding:10px" shadow="hover">
          <!-- 点击修改订阅信息 -->
          <div class="cover" @click="editSub(item)">
            <img :src="item.category.cover">
          </div>
          <div class="info">
            <div class="title">
              <a href="">{{item.title}}</a>
              <br/>
              <a href="">价格：{{item.price}} 元/{{item.payWay}} </a>
              <br/>
              <a href="">到期时间：{{item.overDate}}</a>
              <el-progress :percentage="dateForPercent(item.overDate,item.payWay)"
                           :color="customColorMethod"
                           :format="format">
              </el-progress>
            </div>
            <i class="el-icon-delete" @click="deleteSub(item)"></i>
          </div>
        </el-card>
      </el-tooltip>
      <edit-form @onSubmit="loadSubs()" ref="edit"></edit-form>
    </el-row>
    <el-row>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pagesize"
        :total="subs.length">
      </el-pagination>
    </el-row>
  </div>
</template>

<script>
  import EditForm from './EditFrom'
  import SearchBar from './SearchBar'
  import Cookie from "js-cookie"
  export default {
    name: 'Subs',
    components: {EditForm,SearchBar},
    data () {
      return {
        subs: [],
        currentPage: 1,
        pagesize: 14, 
      }
    },
     mounted: function () {
      this.loadSubs()
    },
    methods: {
        // 进度条到90时注释临期
        format(percentage) {
          if (percentage >=90){
            return '🙁❗️'
          }else if (percentage >= 70){
            return ' 😮';
          }else{
            return ' 🙂'
          }
        },
        // 根据百分比改变进度条颜色
        customColorMethod(percentage) {
          if (percentage >= 90){
            // 红
            return '#f56c6c';
          }else if (percentage >= 70){
            // 黄
            return '#e6a23c'
          }else{
            //蓝
            return '#1989fa'
          }
      },

      // 加载所有订阅，通过用户名查询
      loadSubs () {
        var _this = this
        var username = Cookie.get('username') 
        var url = '/' + username + '/subs'   
        this.$axios.get(url).then(resp => {
          if (resp && resp.status === 200) {
            this.subs = resp.data
          }
        })
      },


      /**
       *    日期百分比计算部分
       *    通过当前时间和结束时间的时间差结合订阅类型换算百分比显示
       * 
       */
      // 获取当前时间，格式:yyyy-MM-dd
      returnNowDate(){
        var nowDate = new Date()
        var year = nowDate.getFullYear()
        var month = nowDate.getMonth() + 1
        var day = nowDate.getDate()
        if (month < 10) month = '0' + month
        if (day < 10) day = '0' + day
        return year + '-' + month + '-' +day
      },
      
      // 计算时间差 sDate:开始时间，eDate:结束时间，格式:yyyy-MM-dd
      dateDiff(sDate,eDate){
        var sMonth = sDate.substring(5,sDate.lastIndexOf ('-'));  
        var sDay = sDate.substring(sDate.length,sDate.lastIndexOf ('-')+1);  
        var sYear = sDate.substring(0,sDate.indexOf ('-'));  
  
        var eMonth = eDate.substring(5,eDate.lastIndexOf ('-'));  
        var eDay = eDate.substring(eDate.length,eDate.lastIndexOf ('-')+1);  
        var eYear = eDate.substring(0,eDate.indexOf ('-'));  
  
        var diff=((Date.parse(eMonth+'/'+eDay+'/'+eYear)- Date.parse(sMonth+'/'+sDay+'/'+sYear))/86400000);   
        return Math.abs(diff); 
      },


      /**
       *  计算离订阅结束的剩余百分比
       *  根据月或年一周期作参照，超过一个月或一年的扩大一倍
       *  Asahi Huang 2020.11.17
       */

      dateForPercent (eDate,str) {
        var nowDate = this.returnNowDate()
        var diffDays = this.dateDiff(nowDate,eDate)
        var per = 0
        var n = 0
        if (diffDays <= 0){
          return per=100
        }
        else {
          var i = 1
          if (str === "月"){
            n = 30
            if (diffDays >= n)
              i = (diffDays/n)  + 1
          } else if (str == "年"){
            n = 365
            if (diffDays >= n)
              i = (diffDays/n) + 1
          }
          per = 100 * (diffDays / (n * i))
        }
			 	return 100-per;
       },
      showProgressStatus : function(dataDiff){
        return "success";

      },
      handleCurrentChange: function (currentPage) {
        this.currentPage = currentPage
        console.log(this.currentPage)
      },
      searchResult () {
        var _this = this
        var username = Cookie.get('username') 
        this.$axios
          .get('/' + username + '/search?keywords=' + this.$refs.searchBar.keywords, {
          }).then(resp => {
          if (resp && resp.status === 200) {
            this.subs = resp.data
          }
        })
      },
      deleteSub (item) {
        this.$confirm('此操作将删除该订阅信息, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            this.$axios
              .post('/delete', {
                id: item.id,
                title: item.title,
                user: Cookie.get('username')
              }).then(resp => {
              if (resp && resp.status === 200) {
                this.$message({
                  type: 'success',
                  message: item.title + '删除成功'
                })
                this.loadSubs()
              }
            })
          }
        ).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
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
    }
  }
</script>

<style scoped>
  .cover {
    width:100%;
    margin-bottom: 7px;
    overflow: hidden;
    cursor: pointer;
  }

  img {
    width: 100%;
    display: block;
    border: 0;
    /*margin: 0 auto;*/
  }

  .title {
    font-size: 14px;
    text-align: left;
  }
  .el-icon-delete {
    color: #409EFF;
    cursor: pointer;
    float: right;
  }

  .switch {
    display: flex;
    position: absolute;
    left: 780px;
    top: 25px;
  }

  a {
    text-decoration: none;
  }

  a:link, a:visited, a:focus {
    color: #3377aa;
  }
</style>