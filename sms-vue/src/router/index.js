import Vue from 'vue'
import Router from 'vue-router'
// 导入刚才编写的组件
import Login from '@/components/Login'
import Register from '@/components/Register'
import AppIndex from '@/components/home/AppIndex'
import Community from '@/components/community/CommunityIndex'
import Management from '@/components/management/DashBoard'

import Home from '@/components/Home'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [{
      path: '/home',
      name: 'Home',
      component: Home,
      // home页面并不需要被访问
      redirect: '/index',
      children: [{
          path: '/index',
          name: 'AppIndex',
          component: AppIndex,
          // 需要拦截的路由添加元数据，设置requireAuth字段
          meta: {
            requireAuth: true
          }
        },
        {
          path: '/community',
          name: 'Community',
          component: Community,
          meta: {
            requireAuth: true
          }
        },
        {
          path: '/management',
          name: 'Management',
          component: Management,
          meta: {
            requireAuth: true
          }
        }
      ]
    },
    {
      path: '/',
      name: 'index',
      redirect: '/index',
      component: AppIndex,
      meta: {
        requireAuth: true
      }
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    }
  ]
})
