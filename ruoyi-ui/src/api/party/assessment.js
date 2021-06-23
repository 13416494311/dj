import request from '@/utils/request'


// 查询党组织考核列表
export function listAssessmentRank(data) {
  return request({
    url: '/party/assessment/listRank',
    method: 'post',
    data: data
  })
}

// 查询党组织考核列表
export function listAssessment(data) {
  return request({
    url: '/party/assessment/list',
    method: 'post',
    data: data
  })
}

// 查询党组织考核列表
export function listAssessment1(data) {
  return request({
    url: '/party/assessment/list1',
    method: 'post',
    data: data
  })
}

// 查询党组织考核详细
export function getAssessment(id) {
  return request({
    url: '/party/assessment/' + id,
    method: 'get'
  })
}
// 查询党组织考核详细
export function getAssessmentByUuid(id) {
  return request({
    url: '/party/assessment/getAssessmentByUuid' + id,
    method: 'get'
  })
}

// 新增党组织考核
export function addAssessment(data) {
  return request({
    url: '/party/assessment',
    method: 'post',
    data: data
  })
}

// 新增党组织考核
export function addAssessmentList(data) {
  return request({
    url: '/party/assessment/addByOrg',
    method: 'post',
    data: data
  })
}

// 修改党组织考核
export function updateAssessment(data) {
  return request({
    url: '/party/assessment',
    method: 'put',
    data: data
  })
}

// 提交党委代办
export function createAssessmentTodo(data) {
  return request({
    url: '/party/assessment/createAssessmentTodo',
    method: 'post',
    data: data
  })
}

// 删除党组织考核
export function delAssessment(id) {
  return request({
    url: '/party/assessment/' + id,
    method: 'delete'
  })
}

// 导出党组织考核
export function exportAssessment(query) {
  return request({
    url: '/party/assessment/export',
    method: 'get',
    params: query
  })
}
