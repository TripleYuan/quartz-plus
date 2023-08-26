import Cookies from "js-cookie"
import { getUserMenu, getMenus } from "@/api"

const sysMenus = [
    {
        id: '',
        name: 'sys-manage',
        label: '系统管理',
        icon: 's-tools',
        children: [
            {
                id: '',
                name: 'user-manage',
                label: '用户管理',
                path: '/user-manage',
                icon: 'user',
                url: 'UserManage.vue',
            },
            {
                id: '',
                name: 'role-manage',
                label: '角色管理',
                path: '/role-manage',
                icon: 's-custom',
                url: 'RoleManage.vue',
            },
            {
                id: '',
                name: 'operation-log',
                label: '操作日志',
                path: '/operation-log',
                icon: 'grape',
                url: 'OperationLog.vue',
            },
        ]
    },
    {
        id: '',
        name: 'job-list',
        label: '任务列表',
        path: '/job-list',
        icon: 'sugar',
        url: 'JobList.vue',
    },
    {
        id: '',
        name: 'job-manage',
        label: '任务管理',
        path: '/job-manage',
        icon: 'ice-cream',
        url: 'JobManage.vue',
    },
    {
        id: '',
        name: 'instance-manage',
        label: '实例管理',
        path: '/instance-manage',
        icon: 'coffee',
        url: 'InstanceManage.vue',
    },
]

export default {
    state: {
        isCollapse: false,
        userMenuData: [],
        sysMenuData: []
    },
    mutations: {
        collapseMenu(state) {
            state.isCollapse = !state.isCollapse
        },

        initMenu(state, router) {
            // 初始化系统菜单（填充id，剔除隐藏的菜单）
            getMenus().then(({ data }) => {
                if (data.status !== 0) {
                    const msg = data.status === 401 ? '未登录，无法获取菜单' : '获取菜单失败'
                    console.log(msg)
                    return
                }
                state.sysMenuData = []
                const menuMap = new Map()
                data.data.map(item => menuMap.set(item.menuCode, item.menuId));
                const sysMenus1 = JSON.parse(JSON.stringify(sysMenus))
                sysMenus1.forEach(item => {
                    if (menuMap.has(item.name)) {
                        item.id = menuMap.get(item.name)
                        if (item.children) {
                            item.children = item.children.filter(item => menuMap.has(item.name))
                                .map(item => {
                                    item.id = menuMap.get(item.name)
                                    return item
                                })
                        }
                        state.sysMenuData.push(item)
                    }
                })
            }).catch((err) => {
                console.log(err)
            })

            // 设置用户菜单
            getUserMenu().then(({ data }) => {
                if (data.status !== 0) {
                    const msg = data.status === 401 ? '未登录，无法获取菜单' : '获取菜单失败'
                    console.log(msg)
                    return
                }
                state.userMenuData = []
                const menuMap = new Map()
                data.data.map(item => menuMap.set(item.menuCode, item.menuId));
                const sysMenus2 = JSON.parse(JSON.stringify(sysMenus))
                sysMenus2.forEach(item => {
                    if (item.children) {
                        item.children = item.children.filter(item => menuMap.has(item.name))
                        if (item.children.length > 0) {
                            state.userMenuData.push(item)
                        }
                    } else {
                        if (menuMap.has(item.name)) {
                            state.userMenuData.push(item)
                        }
                    }
                })
                // 注册路由
                const menuArray = []
                state.userMenuData.forEach(item => {
                    if (item.children) {
                        item.children = item.children.map(item => {
                            item.component = () => import(`../views/${item.url}`)
                            return item
                        })
                        menuArray.push(...item.children)
                    } else {
                        item.component = () => import(`../views/${item.url}`)
                        menuArray.push(item)
                    }
                })
                menuArray.forEach((item) => {
                    router.addRoute('Main', item)
                })
            }).catch((err) => {
                console.log(err)
            })
        }
    }
}