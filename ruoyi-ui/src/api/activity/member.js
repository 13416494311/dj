import request from '@/utils/request'

// 查询活动参与人列表
export function listMember(query) {
  return request({
    url: '/activity/member/list',
    method: 'get',
    params: query
  })
}

// 查询活动参与人列表
export function listForScreen(query) {
  return request({
    url: '/activity/member/listForScreen',
    method: 'get',
    params: query
  })
}

// 查询活动参与人详细
export function getMember(memberId) {
  return request({
    url: '/activity/member/' + memberId,
    method: 'get'
  })
}

// 新增活动参与人
export function addMember(data) {
  return request({
    url: '/activity/member',
    method: 'post',
    data: data
  })
}

// 新增活动参与人
export function addMemberList(data) {
  return request({
    url: '/activity/member/addByArrange',
    method: 'post',
    data: data
  })
}

// 修改活动参与人
export function updateMember(data) {
  return request({
    url: '/activity/member',
    method: 'put',
    data: data
  })
}
// 修改活动参与人
export function updateMembers(data) {
  return request({
    url: '/activity/member/updateMembers',
    method: 'post',
    data: data
  })
}


// 删除活动参与人
export function delMember(memberId) {
  return request({
    url: '/activity/member/' + memberId,
    method: 'delete'
  })
}

// 导出活动参与人
export function exportMember(query) {
  return request({
    url: '/activity/member/export',
    method: 'get',
    params: query
  })
}
