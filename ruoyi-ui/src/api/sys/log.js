import request from '@/utils/request'

// 查询操作日志列表
export function listLog(query) {
  return request({
    url: '/sys/log/list',
    method: 'get',
    params: query
  })
}

// 查询操作日志详细
export function getLog(id) {
  return request({
    url: '/sys/log/' + id,
    method: 'get'
  })
}

// 新增操作日志
export function addLog(data) {
  return request({
    url: '/sys/log',
    method: 'post',
    data: data
  })
}

// 修改操作日志
export function updateLog(data) {
  return request({
    url: '/sys/log',
    method: 'put',
    data: data
  })
}

// 删除操作日志
export function delLog(id) {
  return request({
    url: '/sys/log/' + id,
    method: 'delete'
  })
}

// 导出操作日志
export function exportLog(query) {
  return request({
    url: '/sys/log/export',
    method: 'get',
    params: query
  })
}