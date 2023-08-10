<template>
    <el-form ref="form" label-width="70px" class="login-container" :model="form" :rules="rules" :inline="true">
        <h3 class="login-title">系统登录</h3>
        <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入账号" @keydown.enter.native="handleLogin"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" placeholder="请输入密码" type="password" @keydown.enter.native="handleLogin"></el-input>
        </el-form-item>
        <el-form-item style="display:flex; justify-content:center">
            <el-button type="primary" @click="handleLogin">登录</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
import { login, getUserMenu } from '../api'
import md5 from 'md5'
import Cookies from 'js-cookie'
export default {
    data() {
        return {
            form: {
                username: '',
                password: ''
            },
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                ],
            }
        }
    },
    methods: {
        handleLogin() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    const data = { username: this.form.username, password: md5(this.form.password) }
                    login(data).then(({ data }) => {
                        if (data.status === 0) {
                            // 登录成功
                            this.$store.commit('initMenu', this.$router)
                            Cookies.set('token', '123456')
                            this.$router.push('/home')
                        } else {
                            this.$message.error(data.message)
                        }
                    }).catch((err) => {
                        this.$message.error('系统繁忙，请稍后重试login~')
                    })
                }
            })
        }
    }
}
</script>

<style lang="less" scoped>
.login-container {
    width: 350px;
    border: 1px solid #eaeaea;
    margin: 180px auto;
    padding: 35px 35px 15px 35px;
    background-color: #fff;
    border-radius: 15px;
    box-shadow: 0 0 25px #cac6c6;
    box-sizing: border-box;

    .login-title {
        margin-bottom: 40px;
        text-align: center;
        color: #505458;
    }

    .el-input {
        width: 198px;
    }
}
</style>