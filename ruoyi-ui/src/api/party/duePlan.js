import request from '@/utils/request'

// 查询党费计划列表
export function listDuePlan(query) {
  return request({
    url: '/party/duePlan/list',
    method: 'get',
    params: query
  })
}

// 查询党费计划详细
export function getDuePlan(duePlanId) {
  return request({
    url: '/party/duePlan/' + duePlanId,
    method: 'get'
  })
}

// 新增党费计划
export function addDuePlan(data) {
  return request({
    url: '/party/duePlan',
    method: 'post',
    data: data
  })
}

// 修改党费计划
export function updateDuePlan(data) {
  return request({
    url: '/party/duePlan',
    method: 'put',
    data: data
  })
}

// 删除党费计划
export function delDuePlan(duePlanId) {
  return request({
    url: '/party/duePlan/' + duePlanId,
    method: 'delete'
  })
}

// 导出党费计划
export function exportDuePlan(query) {
  return request({
    url: '/party/duePlan/export',
    method: 'get',
    params: query
  })
}