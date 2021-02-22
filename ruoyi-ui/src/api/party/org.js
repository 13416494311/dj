import request from '@/utils/request'

// 查询党组织架构列表
export function listPartyOrg(query) {
  return request({
    url: '/party/org/list',
    method: 'get',
    params: query
  })
}

//
export function getOrgCount() {
  return request({
    url: '/party/org/getOrgCount',
    method: 'get'
  })
}

export function getOrgMemberChartData() {
  return request({
    url: '/party/org/getOrgMemberChartData',
    method: 'get'
  })
}

// 查询党组织架构列表
export function childrenListPartyOrg(partyOrgId) {
  return request({
    url: '/party/org/childrenList/' + partyOrgId,
    method: 'get',
  })
}
// 查询党组织架构列表tree
export function partyOrgTree(query) {
  return request({
    url: '/party/org/tree',
    method: 'get',
    params: query
  })
}

// 查询党组织架构列表tree
export function partyOrgTreeselect(query) {
  return request({
    url: '/party/org/treeselect',
    method: 'get',
    params: query
  })
}

// 查询党组织架构列表tree
export function partyOrgTreeselectByEdit(query) {
  return request({
    url: '/party/org/treeselectByEdit',
    method: 'get',
    params: query
  })
}

// 查询党组织架构详细
export function getPartyOrgInfo(partyOrgId) {
  return request({
    url: '/party/org/getPartyOrgInfo/' + partyOrgId,
    method: 'get'
  })
}

export function getPartyOrgArchives(partyOrgId) {
  return request({
    url: '/party/org/getPartyOrgArchives/' + partyOrgId,
    method: 'get'
  })
}

// 查询党组织架构详细
export function getPartyOrg(partyOrgId) {
  return request({
    url: '/party/org/' + partyOrgId,
    method: 'get'
  })
}
// 查询党组织架构详细
export function getPartyOrgs(partyOrgIds) {
  return request({
    url: '/party/org/getPartyOrgs/' + partyOrgIds,
    method: 'get'
  })
}

// 新增党组织架构
export function addPartyOrg(data) {
  return request({
    url: '/party/org',
    method: 'post',
    data: data
  })
}

// 修改党组织架构
export function updatePartyOrg(data) {
  return request({
    url: '/party/org',
    method: 'put',
    data: data
  })
}

// 删除党组织架构
export function delPartyOrg(partyOrgId) {
  return request({
    url: '/party/org/' + partyOrgId,
    method: 'delete'
  })
}

// 导出党组织架构
export function exportPartyOrg(query) {
  return request({
    url: '/party/org/export',
    method: 'get',
    params: query
  })
}
