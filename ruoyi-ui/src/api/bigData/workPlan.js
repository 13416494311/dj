import request from '@/utils/request'

// 查询党建工作计划列表
export function listWorkPlan(query) {
  return request({
    url: '/bigData/workPlan/list',
    method: 'get',
    params: query
  })
}

// 查询党建工作计划详细
export function getWorkPlan(id) {
  return request({
    url: '/bigData/workPlan/' + id,
    method: 'get'
  })
}

// 新增党建工作计划
export function addWorkPlan(data) {
  return request({
    url: '/bigData/workPlan',
    method: 'post',
    data: data
  })
}

// 修改党建工作计划
export function updateWorkPlan(data) {
  return request({
    url: '/bigData/workPlan',
    method: 'put',
    data: data
  })
}

// 删除党建工作计划
export function delWorkPlan(id) {
  return request({
    url: '/bigData/workPlan/' + id,
    method: 'delete'
  })
}

// 导出党建工作计划
export function exportWorkPlan(query) {
  return request({
    url: '/bigData/workPlan/export',
    method: 'get',
    params: query
  })
}