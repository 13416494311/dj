import request from '@/utils/request'

// 查询考核评价评分列表
export function listAssessmentScore(query) {
  return request({
    url: '/party/assessmentScore/list',
    method: 'get',
    params: query
  })
}

// 查询考核评价评分详细
export function getAssessmentScore(id) {
  return request({
    url: '/party/assessmentScore/' + id,
    method: 'get'
  })
}

// 新增考核评价评分
export function addAssessmentScore(data) {
  return request({
    url: '/party/assessmentScore',
    method: 'post',
    data: data
  })
}

// 修改考核评价评分
export function updateAssessmentScore(data) {
  return request({
    url: '/party/assessmentScore',
    method: 'put',
    data: data
  })
}
// 修改考核评价评分
export function updateAssessmentScoreList(data) {
  return request({
    url: '/party/assessmentScore/updateAssessmentScoreList',
    method: 'post',
    data: data
  })
}

// 删除考核评价评分
export function delAssessmentScore(id) {
  return request({
    url: '/party/assessmentScore/' + id,
    method: 'delete'
  })
}

// 导出考核评价评分
export function exportAssessmentScore(query) {
  return request({
    url: '/party/assessmentScore/export',
    method: 'get',
    params: query
  })
}
