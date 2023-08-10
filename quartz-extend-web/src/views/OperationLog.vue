<template>
    <div class="operationlog">
        <div class="operationlog-header">
            <el-select v-model="queryForm.username" filterable placeholder="请选择" @change="handleSelectChange" clearable>
                <el-option v-for="item in users" :key="item.userid" :label="item.username" :value="item.username">
                </el-option>
            </el-select>
            <el-date-picker style="margin-left: 10px" type="datetimerange" v-model="optValue"
                @change="handleDateTimeChange" :picker-options="pickerOptions" range-separator="至"
                start-placeholder="开始日期" end-placeholder="结束日期" align="right" value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
            <el-button style="margin-left: 10px" type="primary" @click="getList">查找</el-button>
        </div>
        <!-- 任务列表数据 -->
        <el-table :data="tableData" style="width: 100%" height="90%" stripe>
            <el-table-column prop="username" label="用户名">
            </el-table-column>
            <el-table-column prop="controller" label="controller">
            </el-table-column>
            <el-table-column prop="method" label="method">
            </el-table-column>
            <el-table-column prop="apiDesc" label="api描述">
            </el-table-column>
            <el-table-column prop="operationTime" label="操作时间">
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
import { getOperationLogs, getUsers } from '../api'
export default {
    data() {
        return {
            users: [],
            tableData: [],
            queryForm: { username: '', startTime: '', endTime: '' },
            pageData: { pageNo: 1, pageSize: 10 },
            total: 0,
            pickerOptions: {
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit('pick', [start, end]);
                    }
                }]
            },
            optValue: ''
        }
    },
    methods: {
        handleSelectChange(val) {
            this.queryForm.username = val
            // this.getList()
        },
        handleDateTimeChange(timeArray) {
            if (timeArray) {
                this.queryForm.startTime = timeArray[0]
                this.queryForm.endTime = timeArray[1]
            } else {
                this.queryForm.startTime = ''
                this.queryForm.endTime = ''
            }
        },
        handlePage(val) {
            this.pageData.pageNo = val
            this.getList()
        },
        handlePageSizeChange(val) {
            this.pageData.pageSize = val
            this.getList()
        },
        getUsernames() {
            getUsers().then(({ data }) => {
                if (data.status === 0) {
                    this.users = data.data
                } else {
                    this.$message.error(data.message)
                }
            }).catch((err) => {
                this.$message.error('系统繁忙，请稍后重试~')
                console.log(err)
            })
        },
        getList() {
            const params = { params: { ...this.queryForm, ...this.pageData } }
            getOperationLogs(params).then(({ data }) => {
                if (data.status === 0) {
                    const pageResponse = data.data
                    this.tableData = pageResponse.data
                    this.total = pageResponse.total
                } else {
                    this.$message.error(data.message)
                }
            }).catch((err) => {
                this.$message.error('系统繁忙，请稍后重试~')
                console.log(err)
            })
        }
    },
    mounted() {
        this.getUsernames()
        this.getList()
    }
}
</script>

<style lang="less" scoped>
.operationlog {
    height: 90%;

    .operationlog-header {
        margin-bottom: 10px;
        display: flex;
        justify-content: left;
    }

    .page {
        display: flex;
        flex-flow: row-reverse nowrap;
    }
}
</style>