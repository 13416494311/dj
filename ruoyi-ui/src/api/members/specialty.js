import request from '@/utils/request'

// 查询党员特长列表
export function listSpecialty(query) {
  return request({
    url: '/members/specialty/list',
    method: 'get',
    params: query
  })
}

// 查询党员特长详细
export function getSpecialty(specialtyId) {
  return request({
    url: '/members/specialty/' + specialtyId,
    method: 'get'
  })
}

// 新增党员特长
export function addSpecialty(data) {
  return request({
    url: '/members/specialty',
    method: 'post',
    data: data
  })
}

// 修改党员特长
export function updateSpecialty(data) {
  return request({
    url: '/members/specialty',
    method: 'put',
    data: data
  })
}

// 删除党员特长
export function delSpecialty(specialtyId) {
  return request({
    url: '/members/specialty/' + specialtyId,
    method: 'delete'
  })
}

// 导出党员特长
export function exportSpecialty(query) {
  return request({
    url: '/members/specialty/export',
    method: 'get',
    params: query
  })
}