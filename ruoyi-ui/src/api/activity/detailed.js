import request from '@/utils/request'

// 查询活动详情列表
export function listDetailed(query) {
  return request({
    url: '/activity/detailed/list',
    method: 'get',
    params: query
  })
}

// 查询活动详情列表
export function listDetailedByParam(query) {
  return request({
    url: '/activity/detailed/listByParam',
    method: 'get',
    params: query
  })
}

// 查询活动详情详细
export function getDetailed(detailedId) {
  return request({
    url: '/activity/detailed/' + detailedId,
    method: 'get'
  })
}
// 查询活动详情详细
export function getDetailedByUuid(detailedUuid) {
  return request({
    url: '/activity/detailed/getByUuid/' + detailedUuid,
    method: 'get'
  })
}

// 新增活动详情
export function addDetailed(data) {
  return request({
    url: '/activity/detailed',
    method: 'post',
    data: data
  })
}

// 修改活动详情
export function updateDetailed(data) {
  return request({
    url: '/activity/detailed',
    method: 'put',
    data: data
  })
}

// 删除活动详情
export function delDetailed(activityId) {
  return request({
    url: '/activity/detailed/' + activityId,
    method: 'delete'
  })
}

// 导出活动详情
export function exportDetailed(query) {
  return request({
    url: '/activity/detailed/export',
    method: 'get',
    params: query
  })
}
