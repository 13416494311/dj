import request from '@/utils/request'

// 查询党员帮扶列表
export function listHelp(query) {
  return request({
    url: '/members/help/list',
    method: 'get',
    params: query
  })
}

// 查询党员帮扶详细
export function getHelp(helpId) {
  return request({
    url: '/members/help/' + helpId,
    method: 'get'
  })
}

// 新增党员帮扶
export function addHelp(data) {
  return request({
    url: '/members/help',
    method: 'post',
    data: data
  })
}

// 修改党员帮扶
export function updateHelp(data) {
  return request({
    url: '/members/help',
    method: 'put',
    data: data
  })
}

// 删除党员帮扶
export function delHelp(helpId) {
  return request({
    url: '/members/help/' + helpId,
    method: 'delete'
  })
}

// 导出党员帮扶
export function exportHelp(query) {
  return request({
    url: '/members/help/export',
    method: 'get',
    params: query
  })
}