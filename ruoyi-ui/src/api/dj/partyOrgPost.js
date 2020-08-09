import request from '@/utils/request'

// 查询党组织职务列表
export function listPartyOrgPost(query) {
  return request({
    url: '/dj/partyOrgPost/list',
    method: 'get',
    params: query
  })
}

// 查询党组织职务详细
export function getPartyOrgPost(id) {
  return request({
    url: '/dj/partyOrgPost/' + id,
    method: 'get'
  })
}

// 新增党组织职务
export function addPartyOrgPost(data) {
  return request({
    url: '/dj/partyOrgPost',
    method: 'post',
    data: data
  })
}

// 修改党组织职务
export function updatePartyOrgPost(data) {
  return request({
    url: '/dj/partyOrgPost',
    method: 'put',
    data: data
  })
}

// 删除党组织职务
export function delPartyOrgPost(id) {
  return request({
    url: '/dj/partyOrgPost/' + id,
    method: 'delete'
  })
}

// 导出党组织职务
export function exportPartyOrgPost(query) {
  return request({
    url: '/dj/partyOrgPost/export',
    method: 'get',
    params: query
  })
}