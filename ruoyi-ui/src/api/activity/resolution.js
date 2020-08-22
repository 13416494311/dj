import request from '@/utils/request'

// 查询活动决议列表
export function listResolution(query) {
  return request({
    url: '/activity/resolution/list',
    method: 'get',
    params: query
  })
}

// 查询活动决议详细
export function getResolution(resolutionId) {
  return request({
    url: '/activity/resolution/' + resolutionId,
    method: 'get'
  })
}

// 新增活动决议
export function addResolution(data) {
  return request({
    url: '/activity/resolution',
    method: 'post',
    data: data
  })
}

// 修改活动决议
export function updateResolution(data) {
  return request({
    url: '/activity/resolution',
    method: 'put',
    data: data
  })
}

// 删除活动决议
export function delResolution(resolutionId) {
  return request({
    url: '/activity/resolution/' + resolutionId,
    method: 'delete'
  })
}

// 导出活动决议
export function exportResolution(query) {
  return request({
    url: '/activity/resolution/export',
    method: 'get',
    params: query
  })
}