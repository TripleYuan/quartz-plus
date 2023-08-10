<template>
    <div class="user-manage">
        <!-- 新增|编辑角色的弹窗 -->
        <el-dialog :title="userTitle" :visible.sync="userDialogVisible" width="40%"
            :before-close="handleUserDialogClose">
            <el-form ref="userForm" :model="userForm" label-width="80px" :rules="userFormRules">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="userForm.username" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item label="用户类型" prop="userType">
                    <el-select v-model="userForm.userType" placeholder="请选择用户类型">
                        <el-option label="管理员" :value=1></el-option>
                        <el-option label="普通用户" :value=0></el-option>
                    </el-select>
                </el-form-item>
            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelUser()">取 消</el-button>
                <el-button type="primary" @click="submitUser()">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 分配权限的弹窗 -->
        <el-dialog title="分配权限" :visible.sync="permissionDialogVisible" width="40%"
            :before-close="handlePermissionDialogClose">
            <el-tree ref="permissionTree" :props="defaultProps" node-key="id" :data="permissionData" show-checkbox>
            </el-tree>

            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelPermission()">取 消</el-button>
                <el-button type="primary" @click="submitPermission()">确 定</el-button>
            </span>
        </el-dialog>

        <div class="role-manage-header">
            <el-button type="primary" @click="handleAddUser()"> 新增用户 </el-button>
        </div>

        <el-table :data="tableData" style="width: 100%" stripe>
            <el-table-column prop="username" label="用户名"></el-table-column>
            <el-table-column prop="userType" label="用户类型">
                <template slot-scope="scope">
                    <span style="margin-left: 10px">{{ scope.row.userType === 1 ? '管理员' : '普通用户' }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button type="warning" size="mini" @click="handleAssignPermission(scope.row)">分配权限</el-button>
                    <el-button type="danger" size="mini" @click="handleDel(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
import {
    getUsers, addOrUpdateUser, deleteUser, getUserPermissions, addOrUpdateUserPermission,
    getRoles, getRolePermissions
} from '../api'
export default {
    data() {
        return {
            // 与角色操作有关的属性
            userTitle: '',
            userDialogVisible: false,
            permissionDialogVisible: false,
            userForm: { username: '', userType: '' },
            userFormRules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                ],
                userType: [
                    { required: true, message: '请选择用户类型', trigger: 'change' },
                ],
            },
            // 角色列表数据
            tableData: [],
            // 与权限操作有关的属性
            defaultProps: {
                children: 'children',
                label: 'label',
            },
            permissionData: [],
            selectedUserId: null
        }
    },
    methods: {
        // 角色操作
        handleUserDialogClose() {
            this.userDialogVisible = false
            this.$refs.userForm.resetFields()
        },
        cancelUser() {
            this.handleUserDialogClose()
        },
        submitUser() {
            this.$refs.userForm.validate((valid) => {
                if (valid) {
                    addOrUpdateUser(this.userForm).then(({ data }) => {
                        if (data.status === 0) {
                            this.getList()
                        } else {
                            this.$message.error(data.message)
                        }
                    }).catch((err) => {
                        this.$message.error('系统繁忙，请稍后重试~')
                    })
                    this.userDialogVisible = false
                    this.$refs.userForm.resetFields()
                }
            })
        },
        handleAddUser() {
            this.userDialogVisible = true
            this.userTitle = '添加用户'
            this.userForm = { username: '', userType: '' }
        },
        handleEdit(row) {
            this.userDialogVisible = true
            this.userTitle = '编辑用户'
            // deep cody
            this.userForm = JSON.parse(JSON.stringify(row))
        },
        handleDel(row) {
            this.$confirm(`此操作将永久用户${row.username}，是否继续?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                deleteUser(row.userid).then(({ data }) => {
                    if (data.status === 0) {
                        this.getList()
                    } else {
                        this.$message.error(data.message)
                    }
                }).catch((err) => {
                    this.$message.error('系统繁忙，请稍后重试~')
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },

        // 权限操作
        handlePermissionDialogClose() {
            this.permissionDialogVisible = false
        },
        handleAssignPermission(row) {
            this.selectedUserId = row.userid
            this.permissionData = []

            // 设置权限数据（角色）
            getRoles().then(({ data }) => {
                if (data.status === 0) {
                    data.data.forEach(item => {
                        this.permissionData.push({ id: item.roleId, label: item.roleName })
                    })
                } else {
                    this.$message.error(data.message)
                }

            }).catch((err) => {
                this.$message.error('系统繁忙，请稍后重试~')
                console.log(err)
            })

            // 设置已分配的角色
            getUserPermissions(row.userid).then(({ data }) => {
                this.permissionDialogVisible = true
                if (data.status === 0) {
                    const keys = data.data.map(item => item.roleId)
                    this.$nextTick(() => this.$refs.permissionTree.setCheckedKeys(keys, false))
                } else {
                    this.$message.error(data.message)
                }
            }).catch((err) => {
                this.$message.error('系统繁忙，请稍后重试~')
                console.log(err)
            })
        },
        cancelPermission() {
            this.handlePermissionDialogClose()
        },
        submitPermission() {
            const roleIds = this.$refs.permissionTree.getCheckedKeys()
            const data = { userid: this.selectedUserId, roleIds: roleIds }
            addOrUpdateUserPermission(data).then(({ data }) => {
                if (data.status === 0) {
                    this.getList()
                } else {
                    this.$message.error(data.message)
                }
                this.permissionDialogVisible = false
            }).catch((err) => {
                this.$message.error('系统繁忙，请稍后重试~')
            })
        },
        // loadPermissionNode(node, resolve) {
        //     if (node.level === 0) {
        //         return resolve(node.data)
        //     }

        //     if (node.level > 1) {
        //         return node.data.children ? resolve(node.data.children) : resolve([])
        //     }

        //     getRolePermissions(node.data.id).then(({ data }) => {
        //         let nodeData = []
        //         if (data.status === 0) {
        //             const menuIds = data.data.map(item => item.menuId)
        //             const menuData = JSON.parse(JSON.stringify(this.$store.state.menu.sysMenuData))
        //             menuData.forEach(item => {
        //                 if (menuIds.indexOf(item.id) !== -1) {
        //                     if (item.children) {
        //                         item.children = item.children.filter(item => menuIds.indexOf(item.id) !== -1)
        //                         if (item.children.length > 0) {
        //                             nodeData.push(item)
        //                         }
        //                     } else {
        //                         item.disabled = true
        //                         nodeData.push(item)
        //                     }
        //                 }
        //             })
        //         } else {
        //             this.$message.error('获取角色权限失败：' + data.message)
        //         }
        //         resolve(nodeData)
        //     }).catch((err) => {
        //         this.$message.error('系统繁忙，请稍后重试~')
        //         console.log(err)
        //     })
        // },

        // 获取角色列表
        getList() {
            getUsers().then(({ data }) => {
                if (data.status === 0) {
                    this.tableData = data.data
                } else {
                    this.$message.error(data.message)
                }
            }).catch((err) => {
                this.$message.error('系统繁忙，请稍后重试~')
            })
        },
    },
    mounted() {
        this.getList()
    }
}
</script>