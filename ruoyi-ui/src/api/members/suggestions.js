import request from '@/utils/request'

// 查询党员建议列表
export function listSuggestions(query) {
  return request({
    url: '/members/suggestions/list',
    method: 'get',
    params: query
  })
}

// 查询党员建议详细
export function getSuggestions(suggestionsId) {
  return request({
    url: '/members/suggestions/' + suggestionsId,
    method: 'get'
  })
}

// 新增党员建议
export function addSuggestions(data) {
  return request({
    url: '/members/suggestions',
    method: 'post',
    data: data
  })
}

// 修改党员建议
export function updateSuggestions(data) {
  return request({
    url: '/members/suggestions',
    method: 'put',
    data: data
  })
}

// 删除党员建议
export function delSuggestions(suggestionsId) {
  return request({
    url: '/members/suggestions/' + suggestionsId,
    method: 'delete'
  })
}

// 导出党员建议
export function exportSuggestions(query) {
  return request({
    url: '/members/suggestions/export',
    method: 'get',
    params: query
  })
}