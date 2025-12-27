<template>
  <div class="found-publish">
    <div class="header">
      <h1>{{ isEdit ? '编辑招领信息' : '发布招领信息' }}</h1>
      <el-button @click="goBack">返回列表</el-button>
    </div>
    
    <el-card class="publish-form">
      <el-form 
        ref="formRef" 
        :model="form" 
        :rules="rules" 
        label-width="100px" 
        label-position="top"
      >
        <el-form-item label="物品标题" prop="title">
          <el-input 
            v-model="form.title" 
            placeholder="请输入物品标题"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="物品分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择分类">
            <el-option label="证件" value="证件"></el-option>
            <el-option label="钱包" value="钱包"></el-option>
            <el-option label="钥匙" value="钥匙"></el-option>
            <el-option label="电子产品" value="电子产品"></el-option>
            <el-option label="个人物品" value="个人物品"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="拾到地点" prop="foundLocation">
          <el-input 
            v-model="form.foundLocation" 
            placeholder="请输入拾到地点"
            maxlength="100"
          />
        </el-form-item>
        
        <el-form-item label="拾到时间">
          <el-date-picker
            v-model="form.foundTime"
            type="datetime"
            placeholder="请选择拾到时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        
        <el-form-item label="物品描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="5"
            placeholder="请详细描述物品特征、颜色、品牌等信息"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="物品图片">
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :show-file-list="false"
          >
            <div v-if="form.imageUrl" class="image-preview">
              <el-image
                :src="form.imageUrl"
                fit="cover"
                style="width: 200px; height: 200px;"
              />
              <div class="image-actions">
                <el-button type="danger" @click="removeImage">删除图片</el-button>
              </div>
            </div>
            <el-button v-else type="primary">点击上传图片</el-button>
          </el-upload>
          <div v-if="form.imageUrl" class="image-url">
            图片地址：{{ form.imageUrl }}
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            {{ isEdit ? '更新' : '发布' }}
          </el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { publishFoundItem, updateFoundItem, getFoundDetail } from '../../api/found'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)

// 上传地址（暂时设为空，如果需要上传功能再实现）
const uploadUrl = ref('')

const isEdit = computed(() => route.name === 'FoundEdit' || route.params.id)

// 表单数据
const form = reactive({
  id: null,
  title: '',
  category: '',
  foundLocation: '',
  foundTime: '',
  description: '',
  imageUrl: ''
})

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入物品标题', trigger: 'blur' },
    { min: 2, max: 100, message: '标题长度在2到100个字符之间', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择物品分类', trigger: 'change' }
  ],
  foundLocation: [
    { required: true, message: '请输入拾到地点', trigger: 'blur' },
    { min: 2, max: 100, message: '地点长度在2到100个字符之间', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入物品描述', trigger: 'blur' },
    { min: 10, max: 500, message: '描述长度在10到500个字符之间', trigger: 'blur' }
  ]
}

// 加载招领详情（编辑时使用）
const loadFoundDetail = async (id) => {
  try {
    const data = await getFoundDetail(id)
    // 填充表单数据
    form.id = data.id
    form.title = data.title
    form.category = data.category
    form.foundLocation = data.foundLocation
    form.foundTime = data.foundTime
    form.description = data.description
    form.imageUrl = data.imageUrl
  } catch (error) {
    console.error('加载失败:', error)
    ElMessage.error('加载信息失败')
    router.push('/found')
  }
}

// 处理上传成功
const handleUploadSuccess = (response, file) => {
  // 根据实际的API响应结构调整
  if (response && response.data && response.data.url) {
    form.imageUrl = response.data.url
    ElMessage.success('图片上传成功')
  } else {
    // 如果后端直接返回URL字符串
    form.imageUrl = response
    ElMessage.success('图片上传成功')
  }
}

// 处理上传失败
const handleUploadError = (error, file) => {
  console.error('上传失败:', error)
  ElMessage.error('图片上传失败')
}

// 删除图片
const removeImage = () => {
  form.imageUrl = ''
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    // 验证表单
    await formRef.value.validate()
    submitting.value = true
    
    const formData = {
      title: form.title,
      category: form.category,
      foundLocation: form.foundLocation,
      foundTime: form.foundTime,
      description: form.description,
      imageUrl: form.imageUrl
    }
    
    if (isEdit.value) {
      // 编辑模式
      await updateFoundItem(form.id, formData)
      ElMessage.success('更新成功')
    } else {
      // 发布模式
      await publishFoundItem(formData)
      ElMessage.success('发布成功')
    }
    
    // 提交成功后跳转到列表页
    router.push('/found')
  } catch (error) {
    if (error.name !== 'ValidationError') {
      console.error('提交失败:', error)
      ElMessage.error(error.message || (isEdit.value ? '更新失败' : '发布失败'))
    }
  } finally {
    submitting.value = false
  }
}

// 返回列表
const goBack = () => {
  router.push('/found')
}

// 初始化
onMounted(() => {
  // 如果是编辑模式，加载数据
  if (route.params.id) {
    loadFoundDetail(route.params.id)
  }
})
</script>

<style scoped>
.found-publish {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.header h1 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.publish-form {
  padding: 20px;
}

/* 表单样式 */
.el-form-item {
  margin-bottom: 24px;
}

/* 图片上传样式 */
.upload-demo {
  width: 100%;
}

.image-preview {
  margin-bottom: 10px;
}

.image-actions {
  margin-top: 10px;
}

.image-url {
  margin-top: 10px;
  font-size: 12px;
  color: #666;
  word-break: break-all;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .found-publish {
    padding: 10px;
  }
  
  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .header h1 {
    font-size: 20px;
  }
}
</style>