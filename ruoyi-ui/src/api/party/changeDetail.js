import request from '@/utils/request'

// 查询换届职位详情列表
export function listChangeDetail(query) {
  return request({
    url: '/party/changeDetail/list',
    method: 'get',
    params: query
  })
}

// 查询换届职位详情详细
export function getChangeDetail(changeDetailId) {
  return request({
    url: '/party/changeDetail/' + changeDetailId,
    method: 'get'
  })
}

// 新增换届职位详情
export function addChangeDetail(data) {
  return request({
    url: '/party/changeDetail',
    method: 'post',
    data: data
  })
}

// 修改换届职位详情
export function updateChangeDetail(data) {
  return request({
    url: '/party/changeDetail',
    method: 'put',
    data: data
  })
}

// 删除换届职位详情
export function delChangeDetail(changeDetailId) {
  return request({
    url: '/party/changeDetail/' + changeDetailId,
    method: 'delete'
  })
}

// 导出换届职位详情
export function exportChangeDetail(query) {
  return request({
    url: '/party/changeDetail/export',
    method: 'get',
    params: query
  })
}