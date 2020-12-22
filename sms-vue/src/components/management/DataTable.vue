<template>
  <div class="DataTable" style="padding: 0px">
    <i class="el-icon-pie-chart" >&nbsp;当前个人订阅服务分布图</i>
    <div id="main" :style="{width:'100%',height:'350px'}"></div>
    <br/>
    <span style="margin-top: 70px">总计：{{this.countSum}}个</span>
  </div>
</template>

<script>
import Cookie from "js-cookie"
import * as echarts from 'echarts';
require('echarts/theme/macarons');//引⼊主题
export default {
  name: 'DataTable',
  data () {
    return {
      chartPie: null,
      subs: [],
      countSum: 0
    }
  },
  mounted: function() {
      var _this = this
      var counts = new Array()
        for (var i = 0; i < 6 ; i ++)
          counts[i] = 0
      var username = Cookie.get('username') 
      var url = '/' + username + '/subs'   
      this.$axios.get(url).then(resp => {
        if (resp && resp.status === 200) {
          this.subs = resp.data
          this.countSum = this.subs.length
          for (var i = 0; i < this.subs.length; i++){
            counts[this.subs[i].category.id-1] += 1
          }
          // 图表部分
          var colorlist = ['#FF0033','#0066CC','#FDE200','#FF9900','#339933','#9966CC']
          var myChart = echarts.init(document.getElementById('main'));
          myChart.setOption({
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c}个 ({d}%)'
            },
            legend:{
              data:['音乐类','视频类','购物类','外卖类','游戏类','其他类'],
              left: "center",
              top: "bottom",
              orient: "horizontal"
            },
            series: [
            {
              name: '个人订阅',
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: "center"
               },
              emphasis: {
                label: {
                    show: true,
                    fontSize: '30',
                    fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              color: colorlist,
              data:[
                {value:counts[0], name:'音乐类'},
                {value:counts[1], name:'视频类'},
                {value:counts[2], name:'购物类'},
                {value:counts[3], name:'外卖类'},
                {value:counts[4], name:'游戏类'},
                {value:counts[5], name:'其他类'}
              ]
          }]
        })
      }
    })
   /*ECharts图表*/
  },
  methods: {
    refreshCount(){
      var _this = this
      var count = 0;
      this.loadSubs();
    },
    loadSubs(){
      var _this = this
      var username = Cookie.get('username') 
      var url = '/' + username + '/subs'   
      this.$axios.get(url).then(resp => {
        if (resp && resp.status === 200) {
          this.subs = resp.data
          this.$message("lenght: " + this.subs.length)
          var counts = new Array()
          counts[0] = 0
          counts[1] = 0
          counts[2] = 0
          counts[3] = 0
          counts[4] = 0
          counts[5] = 0
          for (var i = 0; i < this.subs.length; i++){
            counts[this.subs[i].category.id-1] += 1
          }
          this.$message("music:" + counts[0] + "vedio:" + counts[1])
          return counts
        }
      })
    },
    clearCount(){
      this.count.music = 0;
      this.count.vedio = 0;
      this.count.shop = 0;
      this.count.food = 0;
      this.count.game = 0;
      this.count.other = 0;
    }
  }
}
</script>

<style scoped>

</style>