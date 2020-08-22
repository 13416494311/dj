import request from '@/utils/request'

// 查询活动纪要列表
export function listSummary(query) {
  return request({
    url: '/activity/summary/list',
    method: 'get',
    params: query
  })
}

// 查询活动纪要详细
export function getSummary(summaryId) {
  return request({
    url: '/activity/summary/' + summaryId,
    method: 'get'
  })
}

// 新增活动纪要
export function addSummary(data) {
  return request({
    url: '/activity/summary',
    method: 'post',
    data: data
  })
}

// 修改活动纪要
export function updateSummary(data) {
  return request({
    url: '/activity/summary',
    method: 'put',
    data: data
  })
}

// 删除活动纪要
export function delSummary(summaryId) {
  return request({
    url: '/activity/summary/' + summaryId,
    method: 'delete'
  })
}

// 导出活动纪要
export function exportSummary(query) {
  return request({
    url: '/activity/summary/export',
    method: 'get',
    params: query
  })
}