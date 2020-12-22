<template>
  <div>
    <!-- 添加订阅信息按钮 -->
    <el-tooltip class="item" effect="dark" content="点击添加订阅信息" placement="top">
      <i class="el-icon-circle-plus-outline"  @click="dialogFormVisible = true" style="margin: 63px" ></i>
    </el-tooltip>

    <!-- 表单 -->
    <el-dialog
      title="添加/修改订阅服务信息"
      :visible.sync="dialogFormVisible"
      @close="clear">
      <el-form v-model="form" style="text-align: left" ref="dataForm" >
        <el-form-item label="订阅名称:" :label-width="formLabelWidth" prop="title">
          <el-input style="width:400px" type="text" v-model="form.title" autocomplete="off" placeholder="请输入名称" ></el-input>
        </el-form-item>
        <el-form-item label="订阅方式:" :label-width="formLabelWidth" prop="payWay">
            <el-switch v-model="form.payWay"
             active-text="按月付费" 
             inactive-text="按年付费"
             active-value="月"
             inactive-value="年">
          </el-switch>
        </el-form-item>
        <el-form-item label="订阅价格:" :label-width="formLabelWidth" prop="price">
           <el-input-number v-model="form.price" @change="handleChange" :min="0" :max="1000" :precision="2" label="订阅价格"></el-input-number>
           <label>&nbsp;&nbsp;元/{{form.payWay}}</label>
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
        <el-form-item label="订阅结束日期:" :label-width="formLabelWidth" prop="overDate"> 
          <el-date-picker  type="date" value-format="yyyy-MM-dd" v-model="form.overDate"
            :picker-options="pickerOptions"
            placeholder="选择结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注:" :label-width="formLabelWidth" prop="comment"> 
              <el-input
                style="width:400px"
                type="textarea"
                :autosize="{ minRows: 5, maxRows: 8}"
                placeholder="请输入内容"
                v-model="form.comment"
                maxlength="100"
                show-word-limit>
              </el-input>
        </el-form-item>
        <el-form-item prop="id" style="height: 0">
          <el-input type="hidden" v-model="form.id" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import Cookie from "js-cookie"
  import Moment from "moment"
  export default {
    name: 'EditForm',
    data () {
      return {
        dialogFormVisible: false,
        form: {
          id: '',
          title: '',
          category: {
            id: '',
            name: ''
          },
          overDate: '',
          price: '0',
          payWay: '月',
          comment: ''
        },
        formLabelWidth: '150px',
        pickerOptions: {
          disabledDate(time) {
          // 设置今天以及今天之后的日期不可选择
           return time.getTime() < Date.now() - 8.64e7;
          // 设置选择今天以及今天之前的日期
          // return time.getTime() > Date.now();
          }
        },
      }
    },
    methods: {
      clear () {
        // 清空同时刷新一次页面
        this.form = {
          id: '',
          title: '',
          category: {
            id: '',
            name: ''
          },
          overDate: '',
          price: '0',
          payWay: '月',
          comment: ''
        }
      },
      onSubmit () {
        
        this.$axios
          .post('/subs', {
            id: this.form.id,
            title: this.form.title,
            category: this.form.category,
            overDate: this.form.overDate,
            price: this.form.price,
            payWay: this.form.payWay,
            comment: this.form.comment,
            user: Cookie.get('username')
          }).then(resp => {
          if (resp && resp.status === 200) {
            this.dialogFormVisible = false
            this.$emit('onSubmit')
          }
        })
      }
    }
  }
</script>

<style scoped>
  .el-icon-circle-plus-outline {
    margin: 50px 0 0 20px;
    font-size: 100px;
    float: left;
    cursor: pointer;
    color: #409EFF;
  }
</style>
