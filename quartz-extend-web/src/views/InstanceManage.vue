<template>
    <div class="instancemanage">

        <div class="instancemanage-header">
            <el-select v-model="queryForm.schedName" filterable placeholder="请选择" @change="handleSelectChange" clearable>
                <el-option v-for="item in schedNames" :key="item" :label="item" :value="item">
                </el-option>
            </el-select>
        </div>

        <!-- 实例列表数据 -->
        <el-table :data="tableData" style="width: 100%" height="90%" stripe >
            <el-table-column prop="schedName" label="Quartz实例名称" width="200">
            </el-table-column>
            <el-table-column prop="instanceHost" label="实例地址">
            </el-table-column>
            <el-table-column prop="instancePort" label="实例端口">
            </el-table-column>
            <el-table-column label="操作" width="210" fixed="right">
                <template slot-scope="scope">
                    <el-button type="danger" size="mini" @click="deleteInstance(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <div class="page">
            <el-pagination layout="total, sizes, prev, pager, next" :total="this.total" @current-change="handlePage"
                @size-change="handlePageSizeChange">
            </el-pagination>
        </div>
    </div>
</template>

<script>
import { getSchedNames, getInstances, deleteInstance } from '../api'
export default {
    name: 'InstanceManage',
    data() {
        return {
            schedNames: [],
            tableData: [],
            queryForm: { schedName: '' },
            pageData: { pageNo: 1, pageSize: 10 },
            total: 0
        }
    },
    methods: {
        handleSelectChange(val) {
            this.queryForm.schedName = val
            this.getIntanceList()
        },
        handlePage(val) {
            this.pageData.pageNo = val
            this.getIntanceList()
        },
        handlePageSizeChange(val) {
            this.pageData.pageSize = val
            this.getIntanceList()
        },
        getSchedNameList() {
            getSchedNames().then(({ data }) => {
                if (data.status === 0) {
                    this.schedNames = data.data
                } else {
                    this.$message.error(data.message)
                }
            }).catch((err) => {
                this.$message.error('系统繁忙，请稍后重试~')
            })
        },
        getIntanceList() {
            const params = { params: { ...this.queryForm, ...this.pageData } }
            getInstances(params).then(({ data }) => {
                if (data.status === 0) {
                    const pageResponse = data.data
                    this.tableData = pageResponse.data
                    this.total = pageResponse.total
                } else {
                    this.$message.error(data.message)
                }
            }).catch((err) => {
                this.$message.error('系统繁忙，请稍后重试~')
            })
        },
        deleteInstance(row) {
            const data = { schedName: row.schedName, instanceHost: row.instanceHost, instancePort: row.instancePort }
            deleteInstance(data).then(({ data }) => {
                if (data.status === 0) {
                    this.getIntanceList()
                    this.$message.success('实例已删除')
                } else {
                    this.$message.error(data.message)
                }
            }).catch((err) => {
                this.$message.error('系统繁忙，请稍后重试~')
            })
        },
    },
    mounted() {
        this.getSchedNameList()
        this.getIntanceList()
    }
}
</script>

<style lang="less" scoped>
.instancemanage {
    height: 90%;

    .instancemanage-header {
        margin-bottom: 10px;
        display: flex;
    }

    .page {
        display: flex;
        flex-flow: row-reverse nowrap;
    }
}
</style>