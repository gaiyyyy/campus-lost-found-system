<template>
  <el-header class="header">
    <div class="logo">校园失物招领平台</div>
    <div class="header-right">
      <el-dropdown>
        <span class="user-info">
          {{ usernameDisplay }}
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="goProfile">个人中心</el-dropdown-item>
            <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-header>

  <el-card class="home-card">
    <h2>欢迎来到校园失物招领平台</h2>
    <p>快速访问失物信息或招领信息</p>
    <el-space direction="horizontal" size="large">
      <el-button type="primary" icon="ArrowRight" @click="goLostList">
        查看失物信息
      </el-button>
      <el-button type="success" icon="ArrowRight" @click="goFoundList">
        查看招领信息
      </el-button>
    </el-space>
  </el-card>

</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()

const goLostList = () => {
  router.push('/lost_item/list')
}

const goFoundList = () => {
  router.push('/found_item/list')
}


const goProfile = () => {
  router.push('/profile')
}

// username display for header dropdown
const usernameDisplay = ref(localStorage.getItem('username') || '个人中心')

const logout = () => {
  localStorage.clear()
  router.push('/login')
}

</script>

<style scoped>
.home-card {
  max-width: 500px;
  margin: 40px auto;
  text-align: center;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 30px;
  background-color: #409eff;
  color: #fff;
}

.logo {
  font-size: 18px;
  font-weight: bold;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.home-btn {
  padding: 6px 12px !important;
  background-color: #ffffff !important;
  color: #2c3e50 !important;
  font-weight: 700 !important;
  border-radius: 8px !important;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
  border: 1px solid rgba(0,0,0,0.06) !important;
}

.home-btn:hover {
  background-color: #f5f7fa !important;
}

.user-info {
  padding: 6px 12px;
  background-color: #ffffff;
  color: #2c3e50;
  font-weight: 700;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
  display: flex;
  align-items: center;
  gap: 6px;
}
</style>
