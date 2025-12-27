# 个人开发日志

## 📅 2024年12月26日

### ✅ 已完成功能
1. **招领信息管理模块**
   - 招领信息发布与维护
   - 招领列表和详情展示
   - 招领状态管理

2. **信息检索与筛选功能**
   - 搜索接口设计
   - 分类和状态筛选实现

---

### 🗄️ 第一步：创建招领信息表
```sql
-- 创建招领信息表
DROP TABLE IF EXISTS `found_item`;
CREATE TABLE `found_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `category` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `found_location` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `found_time` datetime DEFAULT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `image_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` int DEFAULT '0' COMMENT '0:待认领 1:已归还',
  `user_id` bigint DEFAULT NULL COMMENT '发布者ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 插入测试数据
INSERT INTO `found_item` (`title`, `category`, `found_location`, `found_time`, `description`, `status`, `user_id`) VALUES
('拾到校园卡一张', '证件', '图书馆一楼', '2025-12-26 10:30:00', '蓝色卡套，姓名：张三，学号：20231234', 0, 2),
('拾到黑色水杯', '个人物品', 'B楼203教室', '2025-12-25 15:20:00', '黑色保温杯，有贴纸', 0, 3),
('拾到钥匙串', '钥匙', '食堂门口', '2025-12-24 18:45:00', '三把钥匙，一个U盘', 1, 2),
('拾到无线耳机', '电子产品', '操场看台', '2025-12-26 08:00:00', '白色无线耳机，右耳一只', 0, 4);
```

---

### ⚙️ 第二步：后端开发（Spring Boot）

#### 1. 创建实体类 `FoundItem.java`

#### 2. 创建Repository接口 `FoundItemRepository.java`

#### 3. 创建Service类 `FoundItemService.java`

#### 4. 创建Controller类 `FoundItemController.java`

---

### 🎨 第三步：前端开发（Vue 3 + Element Plus）

#### 1. 创建API文件 `src/api/found.js`

#### 2. 创建招领列表页面 `src/views/found/FoundList.vue`

#### 3. 创建招领详情页面 `src/views/found/FoundDetail.vue`

#### 4. 创建发布/编辑页面 `src/views/found/FoundPublish.vue`

#### 5. 路由配置示例（在 `src/router/index.js` 中添加）

---

## 📅 2024年12月27日

### ✅ 修复与优化
1. **修复BUG**
   - 招领列表中点击查看按钮会跳转到首页的问题

2. **新增功能**
   - 失物列表添加信息检索与筛选功能
   - 系统管理与维护模块
     - 管理员功能设计
     - 信息审核与删除
     - 简单统计功能

3. **界面优化**
   - 完善个人界面中“我的招领”部分
   - 整体UI界面优化