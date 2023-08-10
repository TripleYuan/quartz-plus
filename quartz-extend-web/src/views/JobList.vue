<template>
    <div class="joblist">

        <div class="joblist-header">
            <el-select v-model="queryForm.schedName" filterable placeholder="请选择" @change="handleSelectChange"
                clearable>
                <el-option v-for="item in schedNames" :key="item" :label="item" :value="item">
                </el-option>
            </el-select>
            <el-form :model="queryForm" :inline="true" style="margin-left: 20px">
                <el-form-item>
                    <el-input v-model="queryForm.jobName" placeholder="请输入任务名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="this.getJobList">查找</el-button>
                </el-form-item>
            </el-form>
        </div>

        <!-- 任务列表数据 -->
        <el-table :data="tableData" style="width: 100%" height="90%" stripe>
            <el-table-column prop="schedName" label="Quartz实例名">
            </el-table-column>
            <el-table-column prop="jobName" label="任务名称">
            </el-table-column>
            <el-table-column prop="jobDesc" label="任务描述">
            </el-table-column>
            <el-table-column prop="prevFireTime" label="上次执行时间">
            </el-table-column>
            <el-table-column prop="nextFireTime" label="下次执行时间">
            </el-table-column>
            <el-table-column prop="triggerState" label="触发器状态">
            </el-table-column>
            <el-table-column prop="cron" label="cron表达式">
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="primary" size="mini" @click="tiggerJob(scope.row)">立即执行</el-button>
                    <el-button type="primary" size="mini" @click="refreshJob(scope.row)">刷新</el-button>
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
import { getSchedNames, getJobs, triggerJob, refreshJob } from '../api'
export default {
    name: 'JobList',
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
            this.getJobList()
        },
        handlePage(val) {
            this.pageData.pageNo = val
            this.getJobList()
        },
        handlePageSizeChange(val) {
            this.pageData.pageSize = val
            this.getJobList()
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
        getJobList() {
            const params = { params: { ...this.queryForm, ...this.pageData } }
            getJobs(params).then(({ data }) => {
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
        tiggerJob(row) {
            const data = { schedName: row.schedName, jobName: row.jobName, jobGroup: row.jobGroup }
            triggerJob(data).then(({ data }) => {
                if (data.status === 0) {
                    this.getSchedNameList()
                    this.getJobList()
                    this.$message.success('任务执行成功')
                } else {
                    this.$message.error(data.message)
                }
            }).catch((err) => {
                this.$message.error('系统繁忙，请稍后重试~')
            })
        },
        refreshJob(row) {
            const data = { schedName: row.schedName, triggerName: row.triggerName, triggerGroup: row.triggerGroup }
            refreshJob(data).then(({ data }) => {
                if (data.status === 0) {
                    this.getJobList()
                    this.$message.success('任务状态已刷新')
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
        this.getJobList()
    }
}
</script>

<style lang="less" scoped>
.joblist {
    height: 90%;

    .joblist-header {
        margin-bottom: 10px;
        display: flex;
    }

    .page {
        display: flex;
        flex-flow: row-reverse nowrap;
    }
}
</style>