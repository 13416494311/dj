import request from '@/utils/request'

// 查询党员信息列表
export function listPartyMember(query) {
  return request({
    url: '/party/member/list',
    method: 'get',
    params: query
  })
}

// 查询党员信息列表
export function listPartyMemberNoPage(query) {
  return request({
    url: '/party/member/listNoPage',
    method: 'get',
    params: query
  })
}

//
export function getMemberCount() {
  return request({
    url: '/party/member/getMemberCount',
    method: 'get'
  })
}

// 查询党员信息详细
export function getPartyMember(memberId) {
  return request({
    url: '/party/member/' + memberId,
    method: 'get'
  })
}
// 查询党员信息详细
export function getPartyMemberRadarData(memberId) {
  return request({
    url: '/party/member/getPartyMemberRadarData/' + memberId,
    method: 'get'
  })
}

// 新增党员信息
export function addPartyMember(data) {
  return request({
    url: '/party/member',
    method: 'post',
    data: data
  })
}
// 新增党员信息
export function addPartyMember1(data) {
  return request({
    url: '/party/member/add',
    method: 'post',
    data: data
  })
}

// 修改党员信息
export function updatePartyMember(data) {
  return request({
    url: '/party/member',
    method: 'put',
    data: data
  })
}
// 修改党员信息
export function updatePartyMember1(data) {
  return request({
    url: '/party/member/update',
    method: 'put',
    data: data
  })
}

// 排序党员信息
export function orderPartyMember(data) {
  return request({
    url: '/party/member/order',
    method: 'put',
    data: data
  })
}

export function updatePartyMemberAvatar(data) {
  return request({
    url: '/party/member/updateAvatar',
    method: 'post',
    data: data
  })
}
// 删除党员信息
export function delPartyMember(data) {
  return request({
    url: '/party/member/del',
    method: 'post',
    data: data
  })
}

export function enablePartyMember(data) {
  return request({
    url: '/party/member/enable',
    method: 'post',
    data: data
  })
}

// 导出党员信息
export function exportPartyMember(query) {
  return request({
    url: '/party/member/export',
    method: 'get',
    params: query
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/party/member/avatar',
    method: 'post',
    data: data
  })
}

// 用户头像上传
export function checkPartyMemberUnique(data) {
  return request({
    url: '/party/member/checkPartyMemberUniquePc',
    method: 'post',
    data: data
  })
}

export function listPartyOrgPositionType(partyOrgUuid) {
  return request({
    url: '/party/member/listPartyOrgPositionType/'+partyOrgUuid,
    method: 'get',
  })
}
