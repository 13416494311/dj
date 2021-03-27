import request from '@/utils/request'

// 查询党组织加减分审核列表
export function listAddsubtract(query) {
  return request({
    url: '/party/addsubtract/list',
    method: 'get',
    params: query
  })
}

// 查询党组织加减分审核详细
export function getAddsubtract(addsubtractId) {
  return request({
    url: '/party/addsubtract/' + addsubtractId,
    method: 'get'
  })
}

// 新增党组织加减分审核
export function addAddsubtract(data) {
  return request({
    url: '/party/addsubtract',
    method: 'post',
    data: data
  })
}

// 修改党组织加减分审核
export function updateAddsubtract(data) {
  return request({
    url: '/party/addsubtract',
    method: 'put',
    data: data
  })
}

// 删除党组织加减分审核
export function delAddsubtract(addsubtractId) {
  return request({
    url: '/party/addsubtract/' + addsubtractId,
    method: 'delete'
  })
}

// 导出党组织加减分审核
export function exportAddsubtract(query) {
  return request({
    url: '/party/addsubtract/export',
    method: 'get',
    params: query
  })
}