import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

// 使用一用户对象而不是仅仅用一个布尔变量
export default new Vuex.Store({
  state: {
    user: {
      // 判断本地存储是否有user对象存在
      // 如果存在取出并获得username值，否则把username设置为空。
       username: window.localStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.localStorage.getItem('user' || '[]')).username
      // username: window.localStorage.getItem('username') == null ? '' : JSON.parse(window.localStorage.getItem('username' || '[]'))
    }
  },
  mutations: {
    login(state, user) {
      state.user = user
      window.localStorage.setItem('user', JSON.stringify(user))
    },
    logout (state) {
      state.user = [] 
      window.localStorage.removeItem('user')
    }
  }
})
