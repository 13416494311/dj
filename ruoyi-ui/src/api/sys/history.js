import request from '@/utils/request'

// 查询党史列表
export function listHistory(query) {
  return request({
    url: '/sys/history/list',
    method: 'get',
    params: query
  })
}

// 查询党史详细
export function getHistory(historyId) {
  return request({
    url: '/sys/history/' + historyId,
    method: 'get'
  })
}

// 新增党史
export function addHistory(data) {
  return request({
    url: '/sys/history',
    method: 'post',
    data: data
  })
}

// 修改党史
export function updateHistory(data) {
  return request({
    url: '/sys/history',
    method: 'put',
    data: data
  })
}

// 删除党史
export function delHistory(historyId) {
  return request({
    url: '/sys/history/' + historyId,
    method: 'delete'
  })
}

// 导出党史
export function exportHistory(query) {
  return request({
    url: '/sys/history/export',
    method: 'get',
    params: query
  })
}