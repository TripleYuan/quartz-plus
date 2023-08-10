<template>
    <div class="jobmanage">
        <!-- 编辑Job的弹窗 -->
        <el-dialog title="修改任务执行时间" :visible.sync="jobDialogVisible" width="40%" :before-close="handleJobDialogClose">
            <el-form ref="jobForm" :model="jobForm" label-width="100px" :rules="jobFormRules">
                <el-form-item label="jobName" prop="jobName">
                    <el-input v-model="jobForm.jobName" placeholder="请输入jobName" disabled></el-input>
                </el-form-item>
                <el-form-item label="jobGroup" prop="jobGroup">
                    <el-input v-model="jobForm.jobGroup" placeholder="请输入jobGroup" disabled></el-input>
                </el-form-item>
                <el-form-item label="cron表达式" prop="cron">
                    <el-input v-model="jobForm.cron" placeholder="请输入cron表达式"></el-input>
                </el-form-item>
            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelJob()">取 消</el-button>
                <el-button type="primary" @click="submitJob()">确 定</el-button>
            </span>
        </el-dialog>

        <div class="jobmanage-header">
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
            <el-table-column prop="schedName" label="Quartz实例名" width="200">
            </el-table-column>
            <el-table-column prop="jobName" width="260" label="任务名称">
            </el-table-column>
            <el-table-column prop="jobDesc" label="任务描述">
            </el-table-column>
            <el-table-column prop="prevFireTime" width="160" label="上次执行时间">
            </el-table-column>
            <el-table-column prop="nextFireTime" width="160" label="下次执行时间">
            </el-table-column>
            <el-table-column prop="triggerStateDesc" width="100" label="触发器状态">
            </el-table-column>
            <el-table-column prop="cron" label="cron表达式">
            </el-table-column>
            <el-table-column label="操作" width="280" fixed="right">
                <template slot-scope="scope">
                    <el-button type="primary" size="mini" @click="handlePauseResume(scope.row)">
                        {{ scope.row.triggerState === 'PAUSED' ? '恢复' : '暂停' }}
                    </el-button>
                    <!-- <el-tooltip effect="light" placement="top" content="该操作不会删除Quart实例上的任务, 只是从任务列表中移除数据"> -->
                    <el-button type="warning" size="mini" @click="removeJob(scope.row)">移除</el-button>
                    <!-- </el-tooltip> -->
                    <el-button type="warning" size="mini" @click="handleScheduleJob(scope.row)">修改</el-button>
                    <!-- <el-tooltip effect="light" placement="top" content="该操作会永久删除Quart实例上的任务"> -->
                    <el-button type="danger" size="mini" @click="deleteJob(scope.row)">删除</el-button>
                    <!-- </el-tooltip> -->
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
import { getSchedNames, getJobs, removeJob, pauseJob, resumeJob, deleteJob, refreshJob, scheduleJob } from '../api'
export default {
    name: 'JobManage',
    data() {
        return {
            schedNames: [],
            tableData: [],
            queryForm: { schedName: '', jobName: '' },
            pageData: { pageNo: 1, pageSize: 10 },
            total: 0,
            jobDialogVisible: false,
            jobForm: { schedName: '', triggerName: '', triggerGroup: '', jobName: '', jobGroup: '', cron: '' },
            jobFormRules: {
                jobName: [
                    { required: true, message: '请输入jobName', trigger: 'blur' },
                ],
                jobGroup: [
                    { required: true, message: '请输入jobGroup', trigger: 'blur' },
                ],
                cron: [
                    { required: true, message: '请输入cron表达式', trigger: 'blur' },
                ],
            },
        }
    },
    methods: {
        handleJobDialogClose() {
            this.jobDialogVisible = false
            this.$refs.jobForm.resetFields()
        },
        cancelJob() {
            this.handleJobDialogClose()
        },
        submitJob() {
            this.$refs.jobForm.validate((valid) => {
                if (valid) {
                    scheduleJob(this.jobForm).then(({ data }) => {
                        if (data.status === 0) {
                            this.refreshJob(this.jobForm.schedName, this.jobForm.triggerName, this.jobForm.triggerGroup)
                            this.$message.success('任务已修改')
                            this.getJobList()

                            this.jobDialogVisible = false
                            this.$refs.jobForm.resetFields()
                        } else {
                            this.$message.error('修改失败：' + data.message)
                        }
                    }).catch((err) => {
                        this.$message.error('系统繁忙，请稍后重试~')
                        console.log(err)
                    })
                }
            })
        },
        handleScheduleJob(row) {
            this.jobDialogVisible = true
            this.jobForm = {
                schedName: row.schedName,
                triggerName: row.triggerName,
                triggerGroup: row.triggerGroup,
                jobName: row.jobName,
                jobGroup: row.jobGroup,
                cron: ''
            }
        },

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
        refreshJob(schedName, triggerName, triggerGroup) {
            const data = { schedName: schedName, triggerName: triggerName, triggerGroup: triggerGroup }
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
        removeJob(row) {
            this.$confirm('确认从任务列表中移除此任务? <br/>请注意: 移除操作不会删除远程Quartz实例上的任务, 如果需要删除远程Quartz实例上的任务，请使用删除操作', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'info',
                dangerouslyUseHTMLString: true
            }).then(() => {
                const data = { schedName: row.schedName, triggerName: row.triggerName, triggerGroup: row.triggerGroup }
                removeJob(data).then(({ data }) => {
                    if (data.status === 0) {
                        this.getJobList()
                        this.$message.success('任务已被移除')
                    } else {
                        this.$message.error(data.message)
                    }
                }).catch((err) => {
                    this.$message.error('系统繁忙，请稍后重试~')
                    console.log(err)
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消移除'
                });
            });
        },
        handlePauseResume(row) {
            const data = { schedName: row.schedName, jobName: row.jobName, jobGroup: row.jobGroup }
            if (row.triggerState === 'PAUSED') {
                resumeJob(data).then(({ data }) => {
                    if (data.status === 0) {
                        this.refreshJob(row.schedName, row.triggerName, row.triggerGroup)
                        this.$message.success('任务已恢复')
                    } else {
                        this.$message.error(data.message)
                    }
                }).catch((err) => {
                    this.$message.error('系统繁忙，请稍后重试~')
                })
            } else {
                pauseJob(data).then(({ data }) => {
                    if (data.status === 0) {
                        this.refreshJob(row.schedName, row.triggerName, row.triggerGroup)
                        this.$message.success('任务已暂停')
                    } else {
                        this.$message.error(data.message)
                    }
                }).catch((err) => {
                    this.$message.error('系统繁忙，请稍后重试~')
                })
            }

        },
        resumeJob(row) {
            const data = { schedName: row.schedName, jobName: row.jobName, jobGroup: row.jobGroup }
            resumeJob(data).then(({ data }) => {
                if (data.status === 0) {
                    this.getJobList()
                    this.$message.success('任务已恢复')
                } else {
                    this.$message.error(data.message)
                }
            }).catch((err) => {
                this.$message.error('系统繁忙，请稍后重试~')
            })
        },
        deleteJob(row) {
            this.$confirm('此操作将永久删除Quartz实例上的任务, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                const data = { schedName: row.schedName, jobName: row.jobName, jobGroup: row.jobGroup }
                deleteJob(data).then(({ data }) => {
                    if (data.status === 0) {
                        this.getJobList()
                        this.$message.success('任务已删除')
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
    },
    mounted() {
        this.getSchedNameList()
        this.getJobList()
    }
}
</script>

<style lang="less" scoped>
.jobmanage {
    height: 90%;

    .jobmanage-header {
        margin-bottom: 10px;
        display: flex;
    }

    .page {
        display: flex;
        flex-flow: row-reverse nowrap;
    }
}
</style>