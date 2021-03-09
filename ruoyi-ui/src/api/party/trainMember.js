import request from '@/utils/request'

// 查询参与培训党员列表
export function listTrainMember(query) {
  return request({
    url: '/party/trainMember/list',
    method: 'get',
    params: query
  })
}

// 查询参与培训党员详细
export function getTrainMember(trainMemberId) {
  return request({
    url: '/party/trainMember/' + trainMemberId,
    method: 'get'
  })
}

// 新增参与培训党员
export function addTrainMember(data) {
  return request({
    url: '/party/trainMember',
    method: 'post',
    data: data
  })
}
// 新增参与培训党员
export function addTrainMembers(data) {
  return request({
    url: '/party/trainMember/addTrainMembers',
    method: 'post',
    data: data
  })
}

// 修改参与培训党员
export function updateTrainMember(data) {
  return request({
    url: '/party/trainMember',
    method: 'put',
    data: data
  })
}
// 修改参与培训党员
export function updateTrainMembers(data) {
  return request({
    url: '/party/trainMember/updateMembers',
    method: 'post',
    data: data
  })
}

// 删除参与培训党员
export function delTrainMember(trainMemberId) {
  return request({
    url: '/party/trainMember/' + trainMemberId,
    method: 'delete'
  })
}

// 导出参与培训党员
export function exportTrainMember(query) {
  return request({
    url: '/party/trainMember/export',
    method: 'get',
    params: query
  })
}
