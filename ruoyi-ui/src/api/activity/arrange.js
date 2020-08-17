import request from '@/utils/request'

// 查询活动安排列表
export function listArrange(query) {
  return request({
    url: '/activity/arrange/list',
    method: 'get',
    params: query
  })
}
// 查询活动安排列表
export function listArrangeByParam(query) {
  return request({
    url: '/activity/arrange/listByParam',
    method: 'get',
    params: query
  })
}

// 查询活动安排详细
export function getArrange(id) {
  return request({
    url: '/activity/arrange/' + id,
    method: 'get'
  })
}

// 新增活动安排
export function addArrange(data) {
  return request({
    url: '/activity/arrange',
    method: 'post',
    data: data
  })
}

// 新增活动安排
export function addArrangeList(data) {
  return request({
    url: '/activity/arrange/addByPlan',
    method: 'post',
    data: data
  })
}

// 修改活动安排
export function updateArrange(data) {
  return request({
    url: '/activity/arrange',
    method: 'put',
    data: data
  })
}

// 删除活动安排
export function delArrange(id) {
  return request({
    url: '/activity/arrange/' + id,
    method: 'delete'
  })
}

// 导出活动安排
export function exportArrange(query) {
  return request({
    url: '/activity/arrange/export',
    method: 'get',
    params: query
  })
}
