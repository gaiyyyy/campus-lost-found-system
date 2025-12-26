<template>
  <el-container class="profile-container">
    <!-- Header -->
    <el-header class="header">
      <div class="logo">校园失物招领平台</div>

      <div class="header-right">
        <el-dropdown>
          <span class="user-info">
            {{ username }}
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="showChangeUsername = true">修改用户名</el-dropdown-item>
              <el-dropdown-item @click="showChangePassword = true">修改密码</el-dropdown-item>
              <el-dropdown-item>切换角色</el-dropdown-item>
              <el-dropdown-item divided @click="logout">
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

        <el-button class="home-btn" @click="goHome">返回首页</el-button>
      </div>
    </el-header>

    <!-- 修改用户名对话框 -->
    <el-dialog v-model="showChangeUsername" title="修改用户名">
      <el-form>
        <el-form-item label="新用户名">
          <el-input v-model="usernameForm.newUsername" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showChangeUsername = false">取消</el-button>
        <el-button type="primary" @click="submitChangeUsername">保存</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="showChangePassword" title="修改密码">
      <el-form>
        <el-form-item label="旧密码">
          <el-input v-model="passwordForm.oldPassword" type="password" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passwordForm.newPassword" type="password" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showChangePassword = false">取消</el-button>
        <el-button type="primary" @click="submitChangePassword">保存</el-button>
      </template>
    </el-dialog>

    <!-- Main -->
    <el-container>
      <!-- Sidebar -->
      <el-aside width="220px" class="aside">
        <el-menu
          :default-active="activeMenu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="lost">
            我发布的失物
          </el-menu-item>
          <el-menu-item index="found">
            我发布的招领
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- Content -->
      <el-main class="main">
        <!-- 我发布的失物 -->
        <div v-if="activeMenu === 'lost'">
          <h2 style="margin-bottom: 16px">我发布的失物</h2>

          <el-table
            :data="lostList"
            border
            stripe
            v-loading="loading"
          >
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="category" label="类别" />
            <el-table-column prop="lostLocation" label="丢失地点" />
            <el-table-column prop="lostTime" label="丢失时间" />

            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag
                  :type="row.status === 0 ? 'warning' : 'success'"
                >
                  {{ row.status === 0 ? '未找回' : '已找回' }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button
                  v-if="row.isOwner"
                  size="small"
                  type="primary"
                  @click="editLost(row.id)"
                >
                  编辑
                </el-button>

                <el-button
                  v-if="row.isOwner"
                  size="small"
                  type="danger"
                  @click="deleteLost(row.id)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>

          </el-table>

          <el-empty
            v-if="!loading && lostList.length === 0"
            description="暂无失物信息"
          />
        </div>

        <!-- 招领模块（占位） -->
        <div v-else>
          <h2>我发布的招领</h2>
          <el-empty description="该模块由组员负责开发" />
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'
import axios from '../api/axios'
import { getMyLostItems, deleteLostItem } from '../api/lostItem'
import { updateUsername, updatePassword } from '../api/user'

const router = useRouter()

/* 基础状态 */
const activeMenu = ref('lost')
const username = ref('')
const userId = localStorage.getItem('userId')
const showChangeUsername = ref(false)
const showChangePassword = ref(false)

const usernameForm = ref({ newUsername: '' })
const passwordForm = ref({ oldPassword: '', newPassword: '' })

/* 失物列表 */
const lostList = ref([])
const loading = ref(false)

onMounted(() => {
  username.value = localStorage.getItem('username') || '未登录用户'
  fetchMyLostItems()
})

/* 获取当前用户发布的失物 */
const fetchMyLostItems = async () => {
  loading.value = true
  try {
    const res = await getMyLostItems()
    // 只显示当前用户自己的
    lostList.value = res.filter(item => item.isOwner)
    console.log('我的失物列表:', lostList.value)
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}



/* 编辑失物（复用已有页面） */
// const editLost = (id) => {
//   router.push(`/lost_item/edit/${id}`)
// }
const editLost = (id) => {
  router.push({ path: `/lost_item/edit/${id}`, query: { redirect: '/profile' } })
}


/* 删除失物 */
const deleteLost = async (id) => {
  try {
    await ElMessageBox.confirm(
      '确认删除该失物信息吗？删除后不可恢复',
      '提示',
      { type: 'warning' }
    )

    await deleteLostItem(id)
    ElMessage.success('删除成功')
    fetchMyLostItems()
  } catch (err) {
    // 取消或失败都不需要处理
  }
}

/* 修改用户名 */
const submitChangeUsername = async () => {
  if (!usernameForm.value.newUsername) {
    ElMessage.error('用户名不能为空')
    return
  }

  try {
    const res = await updateUsername(usernameForm.value.newUsername)
    if (!res || res.message !== '修改成功') {
      ElMessage.error(res?.message || '修改失败')
      return
    }
    ElMessage.success('修改成功')
    // 更新本地显示
    username.value = usernameForm.value.newUsername
    localStorage.setItem('username', usernameForm.value.newUsername)
    showChangeUsername.value = false
    usernameForm.value.newUsername = ''
  } catch (err) {
    // axios 拦截器会显示错误信息
  }
}

/* 修改密码 */
const submitChangePassword = async () => {
  if (!passwordForm.value.oldPassword || !passwordForm.value.newPassword) {
    ElMessage.error('请填写旧密码和新密码')
    return
  }

  try {
    const res = await updatePassword(passwordForm.value.oldPassword, passwordForm.value.newPassword)
    if (!res || res.message !== '修改成功') {
      ElMessage.error(res?.message || '修改失败')
      return
    }
    ElMessage.success('修改成功')
    showChangePassword.value = false
    passwordForm.value.oldPassword = ''
    passwordForm.value.newPassword = ''
  } catch (err) {
    // 错误由拦截器显示
  }
}

/* 菜单切换 */
const handleMenuSelect = (index) => {
  activeMenu.value = index
}

// 返回首页
const goHome = () => {
  router.push('/home')
};

/* 退出登录 */
const logout = () => {
  localStorage.clear()
  router.push('/login')
}
</script>

<style scoped>
.profile-container {
  height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 30px;
  background-color: #409eff;
  color: #fff;
}

.logo {
  font-size: 18px;
  font-weight: bold;
}

.user-info {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-info {
  padding: 6px 12px;
  background-color: #ffffff;
  color: #2c3e50;
  font-weight: 700;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
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

.aside {
  border-right: 1px solid #eee;
}

.main {
  padding: 20px;
  background-color: #f5f7fa;
}
</style>
