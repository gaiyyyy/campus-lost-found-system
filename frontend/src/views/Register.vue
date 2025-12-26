<template>
  <el-card class="register-card">
    <h2>注册</h2>
    <el-form :model="registerForm" ref="registerFormRef">
      <el-form-item label="用户名">
        <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="registerForm.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="联系方式">
        <el-input v-model="registerForm.contact" placeholder="请输入联系方式"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleRegister">注册</el-button>
        <el-button type="text" @click="$router.push('/login')">已有账号？登录</el-button>
      </el-form-item>
    </el-form>
    <el-alert v-if="errorMessage" type="error" :title="errorMessage" show-icon></el-alert>
    <el-alert v-if="successMessage" type="success" :title="successMessage" show-icon></el-alert>
  </el-card>
</template>

<script>
import axios from '../api/axios'
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'Register',
  setup() {
    const router = useRouter()

    const registerForm = reactive({
      username: '',
      password: '',
      contact: ''
    })

    const registerFormRef = ref(null)
    const errorMessage = ref('')
    const successMessage = ref('')

    const handleRegister = async () => {
      errorMessage.value = ''
      successMessage.value = ''
      
      if (!registerForm.username || !registerForm.password) {
        errorMessage.value = '用户名或密码不能为空'
        return
      }

      try {
        const res = await axios.post('http://localhost:8080/api/register', registerForm)

        if (res.data === '注册成功') {
          successMessage.value = '注册成功，即将跳转登录页'
          setTimeout(() => {
            router.push('/login')
          }, 1000)
        } else {
          errorMessage.value = res.data
        }
      } catch (err) {
        errorMessage.value = '注册失败'
      }
    }

    return { registerForm, registerFormRef, handleRegister, errorMessage, successMessage }
  }
}
</script>

<style scoped>
.register-card {
  width: 400px;
  margin: 100px auto;
  padding: 20px;
}
</style>
