<template>
  <el-card>
    <div class="header">
      <h2>{{ item.title }}</h2>
      <el-button type="primary" @click="goList">返回失物列表</el-button>
    </div>

    <p>发布者：{{ item.username }}</p>
    <p>类别：{{ item.category }}</p>
    <p>地点：{{ item.lostLocation }}</p>
    
    <p>丢失时间：{{ item.lostTime || '未填写' }}</p>


    <p>描述：{{ item.description }}</p>

    <el-tag :type="item.status === 0 ? 'warning' : 'success'">
      {{ item.status === 0 ? '未找回' : '已找回' }}
    </el-tag>

    <div v-if="item.isOwner" style="margin-top: 15px">
      <el-button type="primary" @click="edit">编辑</el-button>
      <el-button
        v-if="item.status === 0"
        type="success"
        @click="markFound">
        标记已找回
      </el-button>
      <el-button type="danger" @click="remove">删除</el-button>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getLostItemDetail, updateLostItemStatus, deleteLostItem } from '../../api/lostItem'
import dayjs from 'dayjs'


const route = useRoute()
const router = useRouter()
const item = ref({})
const currentUserId = localStorage.getItem('userId')

const goList = () => router.push('/lost_item/list')
const load = async () => {
  const data = await getLostItemDetail(route.params.id)
  item.value = data
}

const edit = () => {
  const redirect = `/lost_item/${route.params.id}`
  router.push({ path: `/lost_item/edit/${route.params.id}`, query: { redirect } })
}

const markFound = async () => {
  await updateLostItemStatus(route.params.id, 1)
  ElMessage.success('状态已更新')
  load()
}

const remove = async () => {
  await ElMessageBox.confirm('确认删除？')
  await deleteLostItem(route.params.id)
  ElMessage.success('删除成功')
  // 如果来自某处跳转过来，优先回到该处，否则返回列表
  const redirect = route.query.redirect
  if (redirect) {
    router.push(redirect)
  } else {
    router.push('/lost_item/list')
  }
}



onMounted(load)
</script>
