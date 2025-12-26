技术栈（满足老师第 1 条要求）
前端：Vue 3 + vue-router 4+Element Plus + Axios+Vite+JS
后端：Spring Boot（Java）
数据库：MySQL
代码托管：GitHub
开发方式：AI 辅助 + 手动编码（Vibe Coding）

# 12.17

### 1.设计项目内容，组内分工，设计数据库

# 12.19 完成用户注册与登录模块接口与前端基础

### 1.建立数据库campus_lost_found

### 2.建立表user，具体字段如下：

![image-20251219102902301](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20251219102902301.png)

### 3.完成注册接口，并测试

### ![image-20251219102935513](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20251219102935513.png)4.完成 JWT 登录接口，并测试

![image-20251219112731799](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20251219112731799.png)

### 5.完成登录状态保持和受保护接口

测试受保护接口：
![image-20251219114428803](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20251219114428803.png)

### 6.完整注册登录流程：

![image-20251219113137465](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20251219113137465.png)

### 7.前端

完成注册页、登录页、简单profile界面（登录状态保持，显示用户名）

![image-20251219230106539](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20251219230106539.png)



![image-20251219230116114](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20251219230116114.png)![image-20251219230157129](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20251219230157129.png)

# 12.22

## 1.建立表lost_item

## 2.完成后端接口并测试

发布失物（POST /lost_item）
编辑失物（PUT /lost_item/{id}）
删除失物（DELETE /lost_item/{id}）
列表查询（GET /lost_item）
详情查询（GET /lost_item/{id}）

# 12.23

## 1.完成失物列表前端

![image-20251223204521341](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20251223204521341.png)

# 12.24

## 1.失物信息前端（详情页、添加、编辑）

![image-20251224102822212](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20251224102822212.png)

![image-20251224102834054](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20251224102834054.png)

![image-20251224102850652](C:\Users\LENOVO\AppData\Roaming\Typora\typora-user-images\image-20251224102850652.png)

## 2.解决时间字段的类型不匹配问题

后端实体 LostItem 中是

```
private LocalDateTime lostTime;
```

前端现在提交

```
lostTime: dayjs(form.value.lostTime).format('YYYY-MM-DD HH:mm:ss')
```

会导致Spring Boot 的 Jackson **无法自动把这种字符串（没有时区）转换成 LocalDateTime**，返回 400

解决：
后端实体保持 LocalDateTime

前端处理 lostTime,手动格式化为 yyyy-MM-dd HH:mm 字符串再提交





美化页面