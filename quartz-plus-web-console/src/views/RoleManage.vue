<template>
    <div class="role-manage">
        <!-- 新增|编辑角色的弹窗 -->
        <el-dialog :title="roleTitle" :visible.sync="roleDialogVisible" width="40%"
            :before-close="handleRoleDialogClose">
            <el-form ref="roleForm" :model="roleForm" label-width="80px" :rules="roleFormRules">
                <el-form-item label="角色名称" prop="roleName">
                    <el-input v-model="roleForm.roleName" placeholder="请输入角色名称"></el-input>
                </el-form-item>
                <el-form-item label="角色描述" prop="roleDesc">
                    <el-input v-model="roleForm.roleDesc" placeholder="请输入角色描述"></el-input>
                </el-form-item>
            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelRole()">取 消</el-button>
                <el-button type="primary" @click="submitRole()">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 分配权限的弹窗 -->
        <el-dialog title="分配权限" :visible.sync="permissionDialogVisible" width="40%"
            :before-close="handlePermissionDialogClose">
            <el-tree ref="permissionTree" :props="defaultProps" node-key="id" :data="permissionData" default-expand-all
                show-checkbox>
            </el-tree>

            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelPermission()">取 消</el-button>
                <el-button type="primary" @click="submitPermission()">确 定</el-button>
            </span>
        </el-dialog>

        <div class="role-manage-header">
            <el-button type="primary" @click="handleAddRole()"> 新增角色 </el-button>
        </div>

        <el-table :data="tableData" style="width: 100%" stripe>
            <el-table-column prop="roleName" label="角色名称"></el-table-column>
            <el-table-column prop="roleDesc" label="角色描述"></el-table-column>
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
import { getRoles, addOrUpdateRole, deleteRole, getRolePermissions, addOrUpdateRolePermission } from '../api'
export default {
    data() {
        return {
            // 与角色操作有关的属性
            roleTitle: '',
            roleDialogVisible: false,
            permissionDialogVisible: false,
            roleForm: { roleName: '', roleDesc: '' },
            roleFormRules: {
                roleName: [
                    { required: true, message: '请输入角色名', trigger: 'blur' },
                ],
                roleDesc: [
                    { required: true, message: '请输入角色描述', trigger: 'blur' },
                ],
            },
            // 角色列表数据
            tableData: [],
            // 与权限操作有关的属性
            defaultProps: {
                children: 'children',
                label: 'label'
            },
            selectedRoleId: null
        }
    },
    methods: {
        // 角色操作
        handleRoleDialogClose() {
            this.roleDialogVisible = false
            this.$refs.roleForm.resetFields()
        },
        cancelRole() {
            this.handleRoleDialogClose()
        },
        submitRole() {
            this.$refs.roleForm.validate((valid) => {
                if (valid) {
                    addOrUpdateRole(this.roleForm).then(({ data }) => {
                        if (data.status === 0) {
                            this.getList()
                        } else {
                            this.$message.error(data.message)
                        }
                    }).catch((err) => {
                        this.$message.error('系统繁忙，请稍后重试~')
                    })
                    this.roleDialogVisible = false
                    this.$refs.roleForm.resetFields()
                }
            })
        },
        handleAddRole() {
            this.roleDialogVisible = true
            this.roleTitle = '添加角色'
            this.roleForm = { roleName: '', roleDesc: '' }
        },
        handleEdit(row) {
            this.roleDialogVisible = true
            this.roleTitle = '编辑角色'
            // deep cody
            this.roleForm = JSON.parse(JSON.stringify(row))
        },
        handleDel(row) {
            this.$confirm(`注意：删除角色${row.roleName}将导致使用该角色的用户失去角色下的权限，是否继续?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                deleteRole(row.roleId).then(({ data }) => {
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
            this.selectedRoleId = row.roleId
            getRolePermissions(row.roleId).then(({ data }) => {
                this.permissionDialogVisible = true
                if (data.status === 0) {
                    const keys = data.data.map(item => item.menuId)
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
            const menuIds = this.$refs.permissionTree.getCheckedKeys()
            const data = { roleId: this.selectedRoleId, menuIds: menuIds }
            addOrUpdateRolePermission(data).then(({ data }) => {
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

        // 获取角色列表
        getList() {
            getRoles().then(({ data }) => {
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
    computed: {
        permissionData() {
            return this.$store.state.menu.sysMenuData
        }
    },
    mounted() {
        this.getList()
    }
}
</script>