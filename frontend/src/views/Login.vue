<template>
  <el-card class="login-card">
    <h2>登录</h2>
    <el-form :model="loginForm" ref="loginFormRef">
      <el-form-item label="用户名">
        <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleLogin">登录</el-button>
        <el-button type="text" @click="$router.push('/register')">没有账号？注册</el-button>
      </el-form-item>
    </el-form>
    <el-alert v-if="errorMessage" type="error" :title="errorMessage" show-icon></el-alert>
  </el-card>
</template>

<script>
import { useRouter } from 'vue-router'
import { reactive, ref } from 'vue'
import axios from '../api/axios'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const loginForm = reactive({
      username: '',
      password: ''
    })

    const loginFormRef = ref(null)
    const errorMessage = ref('')

    const handleLogin = async () => {
      errorMessage.value = ''
      try {
        const res = await axios.post('/api/login', loginForm)
        
        // 调试：查看登录响应数据
        console.log('登录响应数据:', res)
        
        // 保存用户信息到 localStorage
        localStorage.setItem('token', res.token)
        localStorage.setItem('username', res.username)
        
        // 关键：保存角色信息
        if (res.role) {
          localStorage.setItem('role', res.role)
          console.log('保存角色:', res.role)
        } else {
          console.warn('登录响应中没有role字段，默认为user')
          localStorage.setItem('role', 'user') // 默认设为普通用户
        }
        
        // 如果有用户ID也保存
        if (res.id) {
          localStorage.setItem('userId', res.id)
        }
        
        // 登录成功提示
        const roleDisplay = res.role === 'admin' ? '管理员' : '用户'
        alert(`登录成功，欢迎 ${res.username} (${roleDisplay})`)
        
        // 跳转到首页，不是个人中心
        router.push('/home')
        
      } catch (err) {
        console.error('登录错误:', err)
        if (err.response && err.response.data && err.response.data.message) {
          errorMessage.value = err.response.data.message
        } else {
          errorMessage.value = '登录失败'
        }
      }
    }

    return { loginForm, loginFormRef, handleLogin, errorMessage }
  }
}
</script>

<style scoped>
.login-card {
  width: 400px;
  margin: 100px auto;
  padding: 20px;
}
</style>