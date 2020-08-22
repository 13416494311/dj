import request from '@/utils/request'

// 查询心得体会列表
export function listExperience(query) {
  return request({
    url: '/activity/experience/list',
    method: 'get',
    params: query
  })
}

// 查询心得体会详细
export function getExperience(experienceId) {
  return request({
    url: '/activity/experience/' + experienceId,
    method: 'get'
  })
}

// 新增心得体会
export function addExperience(data) {
  return request({
    url: '/activity/experience',
    method: 'post',
    data: data
  })
}

// 修改心得体会
export function updateExperience(data) {
  return request({
    url: '/activity/experience',
    method: 'put',
    data: data
  })
}

// 删除心得体会
export function delExperience(experienceId) {
  return request({
    url: '/activity/experience/' + experienceId,
    method: 'delete'
  })
}

// 导出心得体会
export function exportExperience(query) {
  return request({
    url: '/activity/experience/export',
    method: 'get',
    params: query
  })
}