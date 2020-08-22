import request from '@/utils/request'

// 查询建言献策列表
export function listSuggestions(query) {
  return request({
    url: '/activity/suggestions/list',
    method: 'get',
    params: query
  })
}

// 查询建言献策详细
export function getSuggestions(suggestionsId) {
  return request({
    url: '/activity/suggestions/' + suggestionsId,
    method: 'get'
  })
}

// 新增建言献策
export function addSuggestions(data) {
  return request({
    url: '/activity/suggestions',
    method: 'post',
    data: data
  })
}

// 修改建言献策
export function updateSuggestions(data) {
  return request({
    url: '/activity/suggestions',
    method: 'put',
    data: data
  })
}

// 删除建言献策
export function delSuggestions(suggestionsId) {
  return request({
    url: '/activity/suggestions/' + suggestionsId,
    method: 'delete'
  })
}

// 导出建言献策
export function exportSuggestions(query) {
  return request({
    url: '/activity/suggestions/export',
    method: 'get',
    params: query
  })
}