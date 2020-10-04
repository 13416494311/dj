import request from '@/utils/request'

// 查询待办列表
export function listTodo(query) {
  return request({
    url: '/sys/todo/list',
    method: 'get',
    params: query
  })
}
// 查询待办详细
export function getTodoCount(status) {
  return request({
    url: '/sys/todo/getTodoCount/' + status,
    method: 'get'
  })
}

// 查询待办详细
export function getTodo(todoId) {
  return request({
    url: '/sys/todo/' + todoId,
    method: 'get'
  })
}

// 新增待办
export function addTodo(data) {
  return request({
    url: '/sys/todo',
    method: 'post',
    data: data
  })
}

// 修改待办
export function updateTodo(data) {
  return request({
    url: '/sys/todo',
    method: 'put',
    data: data
  })
}

// 删除待办
export function delTodo(todoId) {
  return request({
    url: '/sys/todo/' + todoId,
    method: 'delete'
  })
}

// 导出待办
export function exportTodo(query) {
  return request({
    url: '/sys/todo/export',
    method: 'get',
    params: query
  })
}
