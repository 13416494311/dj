import request from '@/utils/request'

// 查询帮扶记录列表
export function listHelpRecord(query) {
  return request({
    url: '/members/helpRecord/list',
    method: 'get',
    params: query
  })
}

// 查询帮扶记录详细
export function getHelpRecord(helpRecordId) {
  return request({
    url: '/members/helpRecord/' + helpRecordId,
    method: 'get'
  })
}

// 新增帮扶记录
export function addHelpRecord(data) {
  return request({
    url: '/members/helpRecord',
    method: 'post',
    data: data
  })
}

// 修改帮扶记录
export function updateHelpRecord(data) {
  return request({
    url: '/members/helpRecord',
    method: 'put',
    data: data
  })
}

// 删除帮扶记录
export function delHelpRecord(helpRecordId) {
  return request({
    url: '/members/helpRecord/' + helpRecordId,
    method: 'delete'
  })
}

// 导出帮扶记录
export function exportHelpRecord(query) {
  return request({
    url: '/members/helpRecord/export',
    method: 'get',
    params: query
  })
}