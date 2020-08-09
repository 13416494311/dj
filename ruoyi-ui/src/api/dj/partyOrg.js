import request from '@/utils/request'

// 查询党组织架构列表
export function listPartyOrg(query) {
  return request({
    url: '/dj/partyOrg/list',
    method: 'get',
    params: query
  })
}
// 查询党组织架构列表tree
export function partyOrgTree(query) {
  return request({
    url: '/dj/partyOrg/tree',
    method: 'get',
    params: query
  })
}

// 查询党组织架构列表tree
export function partyOrgTreeselect(query) {
  return request({
    url: '/dj/partyOrg/treeselect',
    method: 'get',
    params: query
  })
}

// 查询党组织架构详细
export function getPartyOrg(partyOrgId) {
  return request({
    url: '/dj/partyOrg/' + partyOrgId,
    method: 'get'
  })
}

// 新增党组织架构
export function addPartyOrg(data) {
  return request({
    url: '/dj/partyOrg',
    method: 'post',
    data: data
  })
}

// 修改党组织架构
export function updatePartyOrg(data) {
  return request({
    url: '/dj/partyOrg',
    method: 'put',
    data: data
  })
}

// 删除党组织架构
export function delPartyOrg(partyOrgId) {
  return request({
    url: '/dj/partyOrg/' + partyOrgId,
    method: 'delete'
  })
}

// 导出党组织架构
export function exportPartyOrg(query) {
  return request({
    url: '/dj/partyOrg/export',
    method: 'get',
    params: query
  })
}
