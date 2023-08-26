<template>
    <el-menu default-active="1-4-1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose"
        background-color="#545c64" text-color="#fff" active-text-color="#ffd04b" :collapse="isCollapse">
        <h3>{{ isCollapse ? "Quartz" : "Quartz任务调度管理系统" }}</h3>
        <el-menu-item v-for="item in noChildren" :key="item.name" :index="item.path" @click="clickMenu(item)">
            <i :class="`el-icon-${item.icon}`"></i>
            <span slot="title">{{ item.label }}</span>
        </el-menu-item>
        <el-submenu v-for="item in hasChildren" :key="item.name" :index="item.name">
            <template slot="title">
                <i :class="`el-icon-${item.icon}`"></i>
                <span slot="title">{{ item.label }}</span>
            </template>
            <el-menu-item-group v-for="subitem in item.children" :key="subitem.path">
                <el-menu-item :index="subitem.path" @click="clickMenu(subitem)">
                    <i :class="`el-icon-${subitem.icon}`"></i>
                    <span slot="title"> {{ subitem.label }}</span>
                </el-menu-item>
            </el-menu-item-group>
        </el-submenu>
    </el-menu>
</template>

<script>
import Cookies from 'js-cookie'
export default {
    data() {
        return {
        }
    },
    methods: {
        handleOpen(key, keyPath) {
            // console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            // console.log(key, keyPath);
        },
        clickMenu(item) {
            // console.log(item)
            if (this.$route.path !== item.path
                && !(this.$route.path === '/home' && item.path === '/')) {
                this.$router.push(item.path)
            }

            this.$store.commit('addTab', item)
        }
    },
    computed: {
        noChildren() {
            return this.menuData.filter(item => !item.children)
        },
        hasChildren() {
            return this.menuData.filter(item => item.children)
        },
        isCollapse() {
            return this.$store.state.menu.isCollapse
        },
        menuData() {
            return this.$store.state.menu.userMenuData
        }
    }
}
</script>

<style lang="less" scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
}

.el-menu {
    border-right: none;
    height: 100vh;

    h3 {
        color: #fff;
        text-align: center;
        line-height: 48px;
        font-size: 16px;
        font-weight: 400px;
    }
}
</style>