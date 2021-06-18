import request from '@/utils/request'

// 查询党组织换届列表
export function listPartyChange(query) {
  return request({
    url: '/party/partyChange/list',
    method: 'get',
    params: query
  })
}

// 查询党组织换届详细
export function getPartyChange(changeId) {
  return request({
    url: '/party/partyChange/' + changeId,
    method: 'get'
  })
}

// 新增党组织换届
export function addPartyChange(data) {
  return request({
    url: '/party/partyChange',
    method: 'post',
    data: data
  })
}

// 修改党组织换届
export function updatePartyChange(data) {
  return request({
    url: '/party/partyChange',
    method: 'put',
    data: data
  })
}


// 提醒党组织换届
export function remindPartyChange(data) {
  return request({
    url: '/party/partyChange/remind',
    method: 'post',
    data: data
  })
}

// 删除党组织换届
export function delPartyChange(changeId) {
  return request({
    url: '/party/partyChange/' + changeId,
    method: 'delete'
  })
}

// 导出党组织换届
export function exportPartyChange(query) {
  return request({
    url: '/party/partyChange/export',
    method: 'get',
    params: query
  })
}
