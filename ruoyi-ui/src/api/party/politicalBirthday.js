import request from '@/utils/request'

// 查询政治生日列表
export function listPoliticalBirthday(query) {
  return request({
    url: '/party/politicalBirthday/list',
    method: 'get',
    params: query
  })
}

// 查询政治生日详细
export function getPoliticalBirthday(birthdayId) {
  return request({
    url: '/party/politicalBirthday/' + birthdayId,
    method: 'get'
  })
}

// 新增政治生日
export function addPoliticalBirthday(data) {
  return request({
    url: '/party/politicalBirthday',
    method: 'post',
    data: data
  })
}

// 修改政治生日
export function updatePoliticalBirthday(data) {
  return request({
    url: '/party/politicalBirthday',
    method: 'put',
    data: data
  })
}

// 删除政治生日
export function delPoliticalBirthday(birthdayId) {
  return request({
    url: '/party/politicalBirthday/' + birthdayId,
    method: 'delete'
  })
}

// 导出政治生日
export function exportPoliticalBirthday(query) {
  return request({
    url: '/party/politicalBirthday/export',
    method: 'get',
    params: query
  })
}