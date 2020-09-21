import request from '@/utils/request'

// 查询党员流动列表
export function listMemberFlow(query) {
  return request({
    url: '/party/memberFlow/list',
    method: 'get',
    params: query
  })
}

// 查询党员流动详细
export function getMemberFlow(flowId) {
  return request({
    url: '/party/memberFlow/' + flowId,
    method: 'get'
  })
}

// 新增党员流动
export function addMemberFlow(data) {
  return request({
    url: '/party/memberFlow',
    method: 'post',
    data: data
  })
}

// 修改党员流动
export function updateMemberFlow(data) {
  return request({
    url: '/party/memberFlow',
    method: 'put',
    data: data
  })
}

// 删除党员流动
export function delMemberFlow(flowId) {
  return request({
    url: '/party/memberFlow/' + flowId,
    method: 'delete'
  })
}

// 导出党员流动
export function exportMemberFlow(query) {
  return request({
    url: '/party/memberFlow/export',
    method: 'get',
    params: query
  })
}