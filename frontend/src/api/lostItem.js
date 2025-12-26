import axios from './axios'

// 获取列表
export const getLostItemList = () => {
  return axios.get('/lost_item')
}

// 获取详情
export const getLostItemDetail = id => {
  return axios.get(`/lost_item/${id}`)
}

// 发布
export const createLostItem = data => {
  return axios.post('/lost_item', data)
}

// 编辑
export const updateLostItem = (id, data) => {
  return axios.put(`/lost_item/${id}`, data)
}

// 删除
export const deleteLostItem = id => {
  return axios.delete(`/lost_item/${id}`)
}

// 更新状态
export const updateLostItemStatus = (id, status) => {
  return axios.put(`/lost_item/${id}/status`, null, {
    params: { status }
  })
}
