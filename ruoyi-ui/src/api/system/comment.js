import request from '@/utils/request'

// 查询点评列表
export function listComment(query) {
  return request({
    url: '/system/comment/list',
    method: 'get',
    params: query
  })
}

export function listCommentTree(query) {
  return request({
    url: '/system/comment/tree',
    method: 'get',
    params: query
  })
}

// 查询点评详细
export function getComment(id) {
  return request({
    url: '/system/comment/' + id,
    method: 'get'
  })
}

// 新增点评
export function addComment(data) {
  return request({
    url: '/system/comment',
    method: 'post',
    data: data
  })
}

// 修改点评
export function updateComment(data) {
  return request({
    url: '/system/comment',
    method: 'put',
    data: data
  })
}

// 删除点评
export function delComment(id) {
  return request({
    url: '/system/comment/' + id,
    method: 'delete'
  })
}

// 导出点评
export function exportComment(query) {
  return request({
    url: '/system/comment/export',
    method: 'get',
    params: query
  })
}
