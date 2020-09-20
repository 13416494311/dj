import request from '@/utils/request'

// 查询党员变更表列表
export function listPartyMemberChange(query) {
  return request({
    url: '/memberChange/partyMemberChange/list',
    method: 'get',
    params: query
  })
}

// 查询党员变更表详细
export function getPartyMemberChange(memberId) {
  return request({
    url: '/memberChange/partyMemberChange/' + memberId,
    method: 'get'
  })
}

// 查询党员变更表详细
export function getPartyMemberChangeByMemberUuid(memberUuid) {
  return request({
    url: '/memberChange/partyMemberChange/getByMemberUuid/' + memberUuid,
    method: 'get'
  })
}

// 新增党员变更表
export function addPartyMemberChange(data) {
  return request({
    url: '/memberChange/partyMemberChange',
    method: 'post',
    data: data
  })
}

// 修改党员变更表
export function updatePartyMemberChange(data) {
  return request({
    url: '/memberChange/partyMemberChange',
    method: 'put',
    data: data
  })
}

// 删除党员变更表
export function delPartyMemberChange(memberId) {
  return request({
    url: '/memberChange/partyMemberChange/' + memberId,
    method: 'delete'
  })
}

// 导出党员变更表
export function exportPartyMemberChange(query) {
  return request({
    url: '/memberChange/partyMemberChange/export',
    method: 'get',
    params: query
  })
}
