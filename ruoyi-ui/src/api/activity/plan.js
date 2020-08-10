import request from '@/utils/request'

// 查询活动计划列表
export function listPlan(query) {
  return request({
    url: '/activity/plan/list',
    method: 'get',
    params: query
  })
}

// 查询活动计划详细
export function getPlan(planId) {
  return request({
    url: '/activity/plan/' + planId,
    method: 'get'
  })
}

// 新增活动计划
export function addPlan(data) {
  return request({
    url: '/activity/plan',
    method: 'post',
    data: data
  })
}

// 修改活动计划
export function updatePlan(data) {
  return request({
    url: '/activity/plan',
    method: 'put',
    data: data
  })
}

// 删除活动计划
export function delPlan(planId) {
  return request({
    url: '/activity/plan/' + planId,
    method: 'delete'
  })
}

// 导出活动计划
export function exportPlan(query) {
  return request({
    url: '/activity/plan/export',
    method: 'get',
    params: query
  })
}