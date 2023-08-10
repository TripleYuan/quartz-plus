import http from '../utils/request'

// 登录API
export const login = (data) => {
    return http.post('/login', data, { withCredentials: true })
}
export const logout = (data) => {
    return http.post('/logout', data, { withCredentials: true })
}

// 菜单API
export const getMenus = () => {
    return http.get('/menu/list', { withCredentials: true })
}
export const getUserMenu = () => {
    return http.get('/menu/getUserMenu', { withCredentials: true })
}

// 任务API
export const getSchedNames = () => {
    return http.get('/job/sched-names', { withCredentials: true })
}
export const getJobs = (params) => {
    return http.get('/job/list', { ...params, ...{ withCredentials: true } })
}
export const triggerJob = (data) => {
    return http.post('/job/trigger', data, { withCredentials: true })
}
export const removeJob = (data) => {
    return http.post('/job/removeLocal', data, { withCredentials: true })
}
export const pauseJob = (data) => {
    return http.post('/job/pause', data, { withCredentials: true })
}
export const resumeJob = (data) => {
    return http.post('/job/resume', data, { withCredentials: true })
}
export const deleteJob = (data) => {
    return http.post('/job/delete', data, { withCredentials: true })
}
export const refreshJob = (data) => {
    return http.post('/job/refresh', data, { withCredentials: true })
}
export const scheduleJob = (data) => {
    return http.post('/job/schedule', data, { withCredentials: true })
}

// 实例API
export const getInstances = (params) => {
    return http.get('/instance/list', { ...params, ...{ withCredentials: true } })
}
export const deleteInstance = (data) => {
    return http.post('/instance/delete', data, { withCredentials: true })
}

// 角色API
export const getRoles = () => {
    return http.get('/role/list', { withCredentials: true })
}
export const addOrUpdateRole = (data) => {
    return http.post('/role/addOrUpdate', data, { withCredentials: true })
}
export const deleteRole = (roleId) => {
    return http.delete('role/delete/' + roleId, { withCredentials: true })
}

// 角色权限API
export const getRolePermissions = (roleId) => {
    return http.get('/role/permission/list/' + roleId, { withCredentials: true })
}
export const addOrUpdateRolePermission = (data) => {
    return http.post('/role/permission/addOrUpdate', data, { withCredentials: true })
}

// 用户API
export const getUsers = () => {
    return http.get('/user/list', { withCredentials: true })
}
export const addOrUpdateUser = (data) => {
    return http.post('/user/addOrUpdate', data, { withCredentials: true })
}
export const deleteUser = (userid) => {
    return http.delete('user/delete/' + userid, { withCredentials: true })
}
export const modifyPassword = (data) => {
    return http.post('/user/pwd', data, { withCredentials: true })
}

// 用户权限API
export const getUserPermissions = (userid) => {
    return http.get('/user/permission/list/' + userid, { withCredentials: true })
}
export const addOrUpdateUserPermission = (data) => {
    return http.post('/user/permission/addOrUpdate', data, { withCredentials: true })
}

// 操作日志API
export const getOperationLogs = (params) => {
    return http.get('/operation-log/list', { ...params, ...{ withCredentials: true } })
}