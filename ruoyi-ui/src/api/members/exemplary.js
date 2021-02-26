import request from '@/utils/request'

// 查询先锋模范列表
export function listExemplary(query) {
  return request({
    url: '/members/exemplary/list',
    method: 'get',
    params: query
  })
}

// 查询先锋模范详细
export function getExemplary(exemplaryId) {
  return request({
    url: '/members/exemplary/' + exemplaryId,
    method: 'get'
  })
}
// 查询先锋模范详细
export function getExemplaryByUuid(exemplaryUuid) {
  return request({
    url: '/members/exemplary/' + exemplaryUuid,
    method: 'get'
  })
}

// 新增先锋模范
export function addExemplary(data) {
  return request({
    url: '/members/exemplary',
    method: 'post',
    data: data
  })
}

// 修改先锋模范
export function updateExemplary(data) {
  return request({
    url: '/members/exemplary',
    method: 'put',
    data: data
  })
}

// 删除先锋模范
export function delExemplary(exemplaryId) {
  return request({
    url: '/members/exemplary/' + exemplaryId,
    method: 'delete'
  })
}

// 导出先锋模范
export function exportExemplary(query) {
  return request({
    url: '/members/exemplary/export',
    method: 'get',
    params: query
  })
}
