import request from '@/utils/request'

// 查询党组织党费列表
export function listDueOrg(query) {
  return request({
    url: '/party/dueOrg/list',
    method: 'get',
    params: query
  })
}
// 查询党组织党费列表
export function pageDueOrg(data) {
  return request({
    url: '/party/dueOrg/page',
    method: 'post',
    data: data
  })
}
// 查询党组织党费详细
export function getDueOrg(dueOrgId) {
  return request({
    url: '/party/dueOrg/' + dueOrgId,
    method: 'get'
  })
}

// 新增党组织党费
export function addDueOrg(data) {
  return request({
    url: '/party/dueOrg',
    method: 'post',
    data: data
  })
}


// 新增党组织党费
export function addDueOrgList(data) {
  return request({
    url: '/party/dueOrg/addList',
    method: 'post',
    data: data
  })
}


// 修改党组织党费
export function updateDueOrg(data) {
  return request({
    url: '/party/dueOrg',
    method: 'put',
    data: data
  })
}

// 删除党组织党费
export function delDueOrg(dueOrgId) {
  return request({
    url: '/party/dueOrg/' + dueOrgId,
    method: 'delete'
  })
}

// 导出党组织党费
export function exportDueOrg(query) {
  return request({
    url: '/party/dueOrg/export',
    method: 'get',
    params: query
  })
}
