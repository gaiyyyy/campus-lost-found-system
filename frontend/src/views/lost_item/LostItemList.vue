<template>
  <el-card>
    <div class="header">
      <h2>失物列表</h2>
      <el-button type="primary" @click="goNew" v-if="currentUsername">
        发布
      </el-button>


      <el-button type="info"  @click="goHome">返回首页</el-button>
    </div>

    <el-table :data="list" style="width: 100%">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="category" label="类别" />
      <el-table-column prop="lostLocation" label="丢失地点" />
      <el-table-column prop="username" label="发布者" />

      <el-table-column label="状态">
        <template #default="{ row }">
          <el-tag :type="row.status === 0 ? 'warning' : 'success'">
            {{ row.status === 0 ? '未找回' : '已找回' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button size="small" @click="goDetail(row.id)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getLostItemList } from '../../api/lostItem'

const router = useRouter()
const list = ref([])

// 当前登录用户的用户名
const currentUsername = localStorage.getItem('username')

const load = async () => {
  const data = await getLostItemList()
  list.value = data
}

const goDetail = id => router.push(`/lost_item/${id}`)
const goNew = () => router.push('/lost_item/new')
const goHome = () => {
  router.push('/home')
};

onMounted(load)
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}
</style>
