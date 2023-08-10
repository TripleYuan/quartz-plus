<template>
    <div class="tabs">
        <el-tag v-for="(item, index) in tags" :key="item.name" :closable="item.name !== 'home'"
            :effect="$route.name === item.name ? 'dark' : 'plain'" @click="clickTab(item)"
            @close="closeTab(item, index)" size="small">
            {{ item.label }}
        </el-tag>
    </div>
</template>

<script>
import { mapState, mapMutations } from 'vuex'
export default {
    data() {
        return {}
    },
    computed: {
        ...mapState({
            tags: state => state.tab.tabsList
        })
    },
    methods: {
        clickTab(item) {
            this.$router.push({ name: item.name })
        },
        ...mapMutations(['removeTab']),
        closeTab(item, index) {
            this.removeTab(item)
            // 删除非选中的tab
            if (item.name !== this.$route.name) {
                return
            }
            if (index === this.tags.length) {
                // 删除的是最后一个tab
                this.$router.push({ name: this.tags[index - 1].name })
            } else {
                this.$router.push({ name: this.tags[index].name })
            }
        }
    }
}
</script>

<style lang="less" scoped>
.tabs {
    padding: 20px;

    .el-tag {
        margin-left: 15px;
        cursor: pointer;
    }
}
</style>