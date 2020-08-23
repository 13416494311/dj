import request from '@/utils/request'

// 查询活动督办列表
export function listSupervise(query) {
  return request({
    url: '/activity/supervise/list',
    method: 'get',
    params: query
  })
}

// 查询活动督办详细
export function getSupervise(superviseId) {
  return request({
    url: '/activity/supervise/' + superviseId,
    method: 'get'
  })
}

// 新增活动督办
export function addSupervise(data) {
  return request({
    url: '/activity/supervise',
    method: 'post',
    data: data
  })
}

// 修改活动督办
export function updateSupervise(data) {
  return request({
    url: '/activity/supervise',
    method: 'put',
    data: data
  })
}

// 删除活动督办
export function delSupervise(superviseId) {
  return request({
    url: '/activity/supervise/' + superviseId,
    method: 'delete'
  })
}

// 导出活动督办
export function exportSupervise(query) {
  return request({
    url: '/activity/supervise/export',
    method: 'get',
    params: query
  })
}