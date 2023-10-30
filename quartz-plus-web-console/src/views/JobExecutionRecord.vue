<template>
    <div class="jobExecutionRecordList">

        <div class="jobExecutionRecordList-header">
            <el-select v-model="queryForm.schedName" filterable placeholder="请选择" @change="handleSelectChange" clearable>
                <el-option v-for="item in schedNames" :key="item" :label="item" :value="item">
                </el-option>
            </el-select>
            <el-form :model="queryForm" :inline="true" style="margin-left: 20px">
                <el-form-item>
                    <el-input v-model="queryForm.jobName" placeholder="请输入任务名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="this.getjobExecutionRecordList">查找</el-button>
                </el-form-item>
            </el-form>
        </div>

        <!-- 任务列表数据 -->
        <el-table :data="tableData" style="width: 100%" height="90%" stripe>
            <el-table-column prop="schedName" label="Quartz实例名">
            </el-table-column>
            <el-table-column prop="jobName" label="任务名称">
            </el-table-column>
            <el-table-column prop="status" label="任务状态">
                <template slot-scope="scope">
                    {{ scope.row.status === 1 ? '执行中' : scope.row.status === 2 ? '成功' : '失败' }}
                </template>
            </el-table-column>
            <el-table-column prop="startTime" label="开始执行时间">
            </el-table-column>
            <el-table-column prop="endTime" label="结束执行时间">
            </el-table-column>
            <el-table-column prop="costTime" label="任务执行耗时（毫秒）">
            </el-table-column>
            <el-table-column prop="exception" label="异常信息">
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
import { getSchedNames, getJobExecutionRecords, } from '../api'
export default {
    name: 'jobExecutionRecordList',
    data() {
        return {
            schedNames: [],
            tableData: [],
            queryForm: { schedName: '', jobName: '' },
            pageData: { pageNo: 1, pageSize: 10 },
            total: 0
        }
    },
    methods: {
        handleSelectChange(val) {
            this.queryForm.schedName = val
            this.getjobExecutionRecordList()
        },
        handlePage(val) {
            this.pageData.pageNo = val
            this.getjobExecutionRecordList()
        },
        handlePageSizeChange(val) {
            this.pageData.pageSize = val
            this.getjobExecutionRecordList()
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
        getjobExecutionRecordList() {
            const params = { params: { ...this.queryForm, ...this.pageData } }
            getJobExecutionRecords(params).then(({ data }) => {
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

    },
    mounted() {
        this.getSchedNameList()
        this.getjobExecutionRecordList()
    }
}
</script>

<style lang="less" scoped>
.jobExecutionRecordList {
    height: 90%;

    .jobExecutionRecordList-header {
        margin-bottom: 10px;
        display: flex;
    }

    .page {
        display: flex;
        flex-flow: row-reverse nowrap;
    }
}
</style>