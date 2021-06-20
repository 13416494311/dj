import request from '@/utils/request'

// 查询绩效考核评分列表
export function listPerformanceScore(query) {
  return request({
    url: '/party/performanceScore/list',
    method: 'get',
    params: query
  })
}

// 查询绩效考核评分详细
export function getPerformanceScore(id) {
  return request({
    url: '/party/performanceScore/' + id,
    method: 'get'
  })
}

// 查询考核评价评分详细
export function getScoreItem(assessmentYearUuid) {
  return request({
    url: '/party/performanceScore/getScoreItem/' + assessmentYearUuid,
    method: 'get'
  })
}

// 新增绩效考核评分
export function addPerformanceScore(data) {
  return request({
    url: '/party/performanceScore',
    method: 'post',
    data: data
  })
}

// 修改绩效考核评分
export function updatePerformanceScore(data) {
  return request({
    url: '/party/performanceScore',
    method: 'put',
    data: data
  })
}

// 删除绩效考核评分
export function delPerformanceScore(id) {
  return request({
    url: '/party/performanceScore/' + id,
    method: 'delete'
  })
}

// 导出绩效考核评分
export function exportPerformanceScore(query) {
  return request({
    url: '/party/performanceScore/export',
    method: 'get',
    params: query
  })
}
