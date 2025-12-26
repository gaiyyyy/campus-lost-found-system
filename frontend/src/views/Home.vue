<template>
  <el-card class="home-card">
    <h2>校园失物招领平台</h2>

    <el-space direction="vertical" size="large">
      <el-button type="primary" @click="goLostList">
        查看失物信息
      </el-button>

      <el-button type="success" @click="goFoundList">
        查看招领信息
      </el-button>


      <el-button type="info" @click="goProfile">
        个人中心
      </el-button>
    </el-space>
  </el-card>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goLostList = () => {
  router.push('/lost_item/list');
};

const goFoundList = () => {
  router.push('/found_item/list');
};


const goProfile = () => {
  router.push('/profile');
};
</script>

<style scoped>
.home-card {
  max-width: 500px;
  margin: 40px auto;
  text-align: center;
}
</style>
