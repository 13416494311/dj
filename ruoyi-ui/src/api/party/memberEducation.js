import request from '@/utils/request'

// 查询教育经历列表
export function pageMemberEducation(query) {
  return request({
    url: '/party/memberEducation/page',
    method: 'get',
    params: query
  })
}

// 查询教育经历列表
export function listMemberEducation(query) {
  return request({
    url: '/party/memberEducation/list',
    method: 'get',
    params: query
  })
}

// 查询教育经历详细
export function getMemberEducation(educationId) {
  return request({
    url: '/party/memberEducation/' + educationId,
    method: 'get'
  })
}

// 新增教育经历
export function addMemberEducation(data) {
  return request({
    url: '/party/memberEducation',
    method: 'post',
    data: data
  })
}

// 修改教育经历
export function updateMemberEducation(data) {
  return request({
    url: '/party/memberEducation',
    method: 'put',
    data: data
  })
}

// 删除教育经历
export function delMemberEducation(educationId) {
  return request({
    url: '/party/memberEducation/' + educationId,
    method: 'delete'
  })
}

// 导出教育经历
export function exportMemberEducation(query) {
  return request({
    url: '/party/memberEducation/export',
    method: 'get',
    params: query
  })
}
