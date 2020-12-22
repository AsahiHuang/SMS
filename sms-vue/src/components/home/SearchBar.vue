<template>
  <div style="margin-bottom: 30px;display: flex;justify-content: center;align-items: center">
    <el-input
      @keyup.enter.native="searchClick"
      placeholder="输入并搜索..."
      prefix-icon="el-icon-search"
      size="medium"
      style="width: 600px;margin-right: 80px"
      v-model="keywords">
       <template slot="prepend">订阅名称</template>
    </el-input>
    <el-button  type="primary" size="medium" icon="el-icon-search" @click="searchClick" plain>搜索</el-button>
  </div>
</template>

<script>
  export default {
    name: 'SearchBar',
    data () {
      return {
        keywords: '',
        subs: [],
        cardLoading: []
      }
    },
    methods: {
      searchClick () {
        this.$emit('onSearch')
      }
    }
  }
</script>

<style scoped>

</style>
