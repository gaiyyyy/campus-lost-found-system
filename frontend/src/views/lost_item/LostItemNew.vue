<template>
  <el-card class="box">
    <h2>{{ isEdit ? '编辑失物信息' : '发布失物信息' }}</h2>

    <el-form :model="form" ref="formRef" label-width="100px">
      <el-form-item label="标题" prop="title"
        :rules="[{ required: true, message: '请输入标题' }]">
        <el-input v-model="form.title" />
      </el-form-item>

      <el-form-item label="类别">
        <el-input v-model="form.category" />
      </el-form-item>

      <el-form-item label="丢失地点">
        <el-input v-model="form.lostLocation" />
      </el-form-item>

      <el-form-item label="丢失时间">
        <el-date-picker
          v-model="form.lostTime"
          type="datetime" />
      </el-form-item>

      <el-form-item label="描述">
        <el-input type="textarea" v-model="form.description" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submit">
          {{ isEdit ? '保存修改' : '发布' }}
        </el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { createLostItem, updateLostItem, getLostItemDetail } from '../../api/lostItem'
import { useRouter, useRoute } from 'vue-router'
import dayjs from 'dayjs'

const props = defineProps({
  isEdit: Boolean,
  redirectAfterSubmit: { type: String, default: '' }
})

const router = useRouter()
const route = useRoute()
const formRef = ref()

const form = ref({
  title: '',
  category: '',
  lostLocation: '',
  lostTime: null,
  description: ''
})

onMounted(async () => {
  if (props.isEdit) {
    const data = await getLostItemDetail(route.params.id)
    form.value = {
      title: data.title,
      category: data.category,
      lostLocation: data.lostLocation,
      lostTime: data.lostTime ? new Date(data.lostTime) : null,
      description: data.description
    }

  }
})


const submit = async () => {
  try {
    await formRef.value.validate()
  } catch (err) {
    return
  }

  const submitData = {
    ...form.value,
    lostTime: form.value.lostTime
      ? dayjs(form.value.lostTime).format('YYYY-MM-DD HH:mm')
      : null
  }


  // try {
  //   if (props.isEdit) {
  //     await updateLostItem(route.params.id, submitData)
  //     ElMessage.success('修改成功')
  //     router.push(`/lost_item/${route.params.id}`)
  //   } else {
  //     await createLostItem(submitData)
  //     ElMessage.success('发布成功')
  //     router.push('/lost_item/list')
  //   }
  // } catch (err) {
  //   ElMessage.error('提交失败')
  //   console.error(err)
  // }

  try {
    if (props.isEdit) {
      await updateLostItem(route.params.id, submitData)
      ElMessage.success('修改成功')
      // 优先使用 props.redirectAfterSubmit，其次使用路由 query.redirect，最后回 detail
      const redirectTarget = props.redirectAfterSubmit || route.query.redirect || ''
      if (redirectTarget) {
        router.push(redirectTarget)
      } else {
        router.push(`/lost_item/${route.params.id}`)
      }
    } else {
      await createLostItem(submitData)
      ElMessage.success('发布成功')
      router.push('/lost_item/list')
    }
  } catch (err) {
    ElMessage.error('提交失败')
    console.error(err)
  }



}

</script>

<style scoped>
.box { max-width: 600px; margin: 20px auto; }
</style>
