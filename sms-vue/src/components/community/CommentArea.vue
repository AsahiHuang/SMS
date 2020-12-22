<template>
  <div class="CommentArea" style="padding: 10px">
    <div>
      <i class="el-icon-user-solid">{{getUsername()}}</i>
    </div>
    <div class="footer">
      <el-input class="comment-input"
        type="text"
        placeholder="请输入内容"
        maxlength="20"
        show-word-limit
        v-model="text"
       ></el-input>
       <el-button class="comment-commit"
        plain
        type="primary"
        icon="el-icon-s-promotion">
        发 送
       </el-button>
    </div>
  </div>
</template>
<script>
import Cookie from "js-cookie"
export default {
  name: 'CommentArea',
  props: ['sub'],
  data() {
    return {
      item : '' 
    }
  },
  mounted () { // 在mounted时候赋值，子组件只更新一次，后面重新选择后展示此组件的数据，不再更新
    this.item = this.sub
  },
  watch: { // 监听到数据然后赋值
    sub (val) {
      this.item = val
    }
  },
  methods: {
    getUsername() {
      var _this = this
      var username = this.item.user.username 
      return username
    }
  }
}
</script>

<style scoped>
  .footer{
    margin-top: 700px;
  }
  .comment-input{
    width: 75%;
  }
  .comment-commit{
    width: 20%;
    margin-left: 15px;
  }

</style>