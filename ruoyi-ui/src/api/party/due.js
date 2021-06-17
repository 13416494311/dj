import request from '@/utils/request'

// 查询党员党费列表
export function listDue(query) {
  return request({
    url: '/party/due/list',
    method: 'get',
    params: query
  })
}

// 查询党员党费列表
export function listDueMember(query) {
  return request({
    url: '/party/due/listDueMember',
    method: 'get',
    params: query
  })
}

// 查询党员党费详细
export function getDue(dueMemberId) {
  return request({
    url: '/party/due/' + dueMemberId,
    method: 'get'
  })
}

// 新增党员党费
export function addDue(data) {
  return request({
    url: '/party/due',
    method: 'post',
    data: data
  })
}

// 修改党员党费
export function updateDue(data) {
  return request({
    url: '/party/due',
    method: 'put',
    data: data
  })
}

// 删除党员党费
export function delDue(dueMemberId) {
  return request({
    url: '/party/due/' + dueMemberId,
    method: 'delete'
  })
}

// 导出党员党费
export function exportDue(query) {
  return request({
    url: '/party/due/export',
    method: 'get',
    params: query
  })
}
