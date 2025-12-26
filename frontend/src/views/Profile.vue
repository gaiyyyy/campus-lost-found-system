<template>
  <el-card>
    <h2>用户信息</h2>
    <p>用户名：{{ user.username }}</p>
    <p>角色：{{ user.role }}</p>
    <el-button type="danger" @click="logout">退出登录</el-button>
    <el-button type="info"  @click="goHome">返回首页</el-button>
  </el-card>
</template>

<script>
import axios from '../api/axios'
import { reactive } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'Profile',
  setup() {
    const user = reactive({ username: '', role: '' })
    const router = useRouter()

    const fetchProfile = async () => {
      try {
        const res = await axios.get('/api/profile')
        user.username = res.data.username
        user.role = res.data.role
      } catch (err) {
        console.log(err)
      }
    }

    const logout = () => {
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      router.push('/login')
    }

    const goHome = () => {
      router.push('/home')
    };

    fetchProfile()

    return { user, logout, goHome }
  }
}
</script>
