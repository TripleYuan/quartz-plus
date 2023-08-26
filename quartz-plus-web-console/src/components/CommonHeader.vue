<template>
    <div class="header-container">
        <el-dialog title="修改密码" :visible.sync="pwdDialogVisible" width="20%" :before-close="handlePwdDialogClose">
            <el-form ref="pwdForm" :model="pwdForm" label-width="120px" :rules="pwdFormRules">
                <el-form-item label="请输入旧密码" prop="oldPwd">
                    <el-input type="password" v-model="pwdForm.oldPwd" placeholder="请输入旧密码"></el-input>
                </el-form-item>
                <el-form-item label="请输入新密码" prop="newPwd">
                    <el-input type="password" v-model="pwdForm.newPwd" placeholder="请输入新密码"></el-input>
                </el-form-item>
            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelPwd()">取 消</el-button>
                <el-button type="primary" @click="submitPwd()">确 定</el-button>
            </span>
        </el-dialog>

        <div class="l-content">
            <el-button style="margin-right: 20px" @click="collapseMenu()" icon="el-icon-menu" size="mini"></el-button>
            <!-- 面包屑 -->
            <el-breadcrumb separator="/">
                <el-breadcrumb-item v-for="item in tags" :key="item.path" :to="{ path: item.path }">
                    {{ item.label }}
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="r-content">
            <el-dropdown @command="handleCommand">
                <span class="el-dropdown-link">
                    <img class="user" src="../assets/user.png" alt="user img" />
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="pwd">修改密码</el-dropdown-item>
                    <el-dropdown-item command="logout">退出</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'
import Cookie from 'js-cookie'
import md5 from 'md5'
import { logout, modifyPassword } from '../api'
export default {
    data() {
        return {
            pwdDialogVisible: false,
            pwdForm: { oldPwd: '', newPwd: '' },
            pwdFormRules: {
                oldPwd: [
                    { required: true, message: '请输入旧密码', trigger: 'blur' },
                ],
                newPwd: [
                    { required: true, message: '请输入新密码', trigger: 'blur' },
                ],
            },
            // 角色列表数据
            tableData: [],
        }
    },
    methods: {
        collapseMenu() {
            this.$store.commit('collapseMenu')
        },
        handleCommand(command) {
            if (command === 'logout') {
                // 登出
                logout().then(({ data }) => {
                    if (data.status === 0) {
                        Cookie.remove('token')
                        this.$router.push({ name: 'login' })
                    } else {
                        this.$message.error(data.message)
                    }
                }).catch((err) => {
                    console.log(err)
                    this.$message.error('系统繁忙，请稍后重试~')
                })
            } else if (command === 'pwd') {
                // 修改密码
                this.pwdDialogVisible = true
            }
        },
        handlePwdDialogClose() {
            this.pwdDialogVisible = false
            this.$refs.pwdForm.resetFields()
        },
        cancelPwd() {
            this.handlePwdDialogClose()
        },
        submitPwd() {
            this.$refs.pwdForm.validate((valid) => {
                if (valid) {
                    const req = { oldPwd: md5(this.pwdForm.oldPwd), newPwd: md5(this.pwdForm.newPwd) }
                    modifyPassword(req).then(({ data }) => {
                        if (data.status === 0) {
                            this.$message.success('密码已修改')
                            this.pwdDialogVisible = false
                            this.$refs.pwdForm.resetFields()
                        } else {
                            this.$message.error(data.message)
                        }
                    }).catch((err) => {
                        this.$message.error('系统繁忙，请稍后重试~')
                        console.log(err)
                    })
                }
            })
        },
    },
    computed: {
        ...mapState({
            tags: state => state.tab.tabsList
        })
    },
}
</script>

<style lang="less" scoped>
.header-container {
    background-color: #333;
    height: 60px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;

    .text {
        color: #fff;
        font-size: 14px;
        margin-left: 10px;
    }

    .r-content {
        .user {
            width: 40px;
            height: 40px;
            border-radius: 50%;
        }
    }

    .l-content {
        display: flex;
        align-items: center;

        /deep/.el-breadcrumb__item {
            .el-breadcrumb__inner {
                font-weight: normal;

                &.is-link {
                    color: #666;
                }
            }

            &:last-child {
                .el-breadcrumb__inner {
                    color: #fff
                }
            }
        }
    }
}
</style>