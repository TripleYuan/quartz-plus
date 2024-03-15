import http from '../utils/request'

// 登录API
export const login = (data) => {
    return http.post('/api/login', data, { withCredentials: true })
}
export const logout = (data) => {
    return http.post('/api/logout', data, { withCredentials: true })
}

// 菜单API
export const getMenus = () => {
    return http.get('/api/menus', { withCredentials: true })
}
export const getUserMenu = () => {
    return http.get('/api/user/menus', { withCredentials: true })
}

// 任务API
export const getSchedNames = () => {
    return http.get('/api/job/sched-names', { withCredentials: true })
}
export const getJobs = (params) => {
    return http.get('/api/jobs', { ...params, ...{ withCredentials: true } })
}
export const updateJob = (data) => {
    return http.post('/api/job', data, { withCredentials: true })
}
export const deleteJob = (schedName, jobName, jobGroup) => {
    return http.delete('/api/job/' + schedName + '/' + jobName + '/' + jobGroup, { withCredentials: true })
}
export const removeJob = (schedName, jobName, jobGroup) => {
    return http.delete('/api/local-job' + schedName + '/' + jobName + '/' + jobGroup, { withCredentials: true })
}
export const executeJob = (data) => {
    return http.post('/api/job/execute', data, { withCredentials: true })
}
export const pauseJob = (data) => {
    return http.post('/api/job/pause', data, { withCredentials: true })
}
export const resumeJob = (data) => {
    return http.post('/api/job/resume', data, { withCredentials: true })
}
export const refreshJob = (data) => {
    return http.post('/api/job/refresh', data, { withCredentials: true })
}

// 实例API
export const getInstances = (params) => {
    return http.get('/api/instances', { ...params, ...{ withCredentials: true } })
}
export const deleteInstance = (schedName, host, port) => {
    return http.delete('/api/instance/' + schedName + '/' + host + '/' + port, { withCredentials: true })
}

// 角色API
export const getRoles = () => {
    return http.get('/api/roles', { withCredentials: true })
}
export const addOrUpdateRole = (data) => {
    return http.post('/api/role', data, { withCredentials: true })
}
export const deleteRole = (roleId) => {
    return http.delete('/api/role/' + roleId, { withCredentials: true })
}

// 角色权限API
export const getRolePermissions = (roleId) => {
    return http.get('/api/role/permission/' + roleId, { withCredentials: true })
}
export const addOrUpdateRolePermission = (data) => {
    return http.post('/api/role/permission', data, { withCredentials: true })
}

// 用户API
export const getUsers = () => {
    return http.get('/api/users', { withCredentials: true })
}
export const addOrUpdateUser = (data) => {
    return http.post('/api/user', data, { withCredentials: true })
}
export const deleteUser = (userid) => {
    return http.delete('/api/user/' + userid, { withCredentials: true })
}
export const modifyPassword = (data) => {
    return http.post('/api/user/password', data, { withCredentials: true })
}

// 用户权限API
export const getUserPermissions = (userid) => {
    return http.get('/api/user/permission/' + userid, { withCredentials: true })
}
export const addOrUpdateUserPermission = (data) => {
    return http.post('/api/user/permission', data, { withCredentials: true })
}

// 操作日志API
export const getOperationLogs = (params) => {
    return http.get('/api/logs/operations', { ...params, ...{ withCredentials: true } })
}

// 任务执行记录
export const getJobExecutionRecords = (params) => {
    return http.get('/api/job/execution-records', { ...params, ...{ withCredentials: true } })
}