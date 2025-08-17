<template>
    <PageStateWrapper page-name="OperationLog" :state-config="stateConfig" @state-restored="onStateRestored"
        @need-load-data="onNeedLoadData">

        <div class="operationlog">
            <div class="operationlog-header">
                <el-form :model="queryForm" :inline="true" style="margin-left: 20px">
                    <el-form-item>
                        <el-select v-model="queryForm.username" filterable placeholder="请选择" @change="updateUsername"
                            clearable>
                            <el-option v-for="item in users" :key="item.userid" :label="item.username"
                                :value="item.username">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-date-picker style="margin-left: 10px" type="datetimerange" v-model="optValue"
                            @change="handleDateTimeChange" :picker-options="pickerOptions" range-separator="至"
                            start-placeholder="开始日期" end-placeholder="结束日期" align="right"
                            value-format="yyyy-MM-dd HH:mm:ss">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model="queryForm.apiPath" placeholder="请输入api路径"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button style="margin-left: 10px" type="primary" @click="getList">查找</el-button>
                    </el-form-item>
                </el-form>
            </div>

            <el-drawer :title="drawerTitle" :visible.sync="drawerVisible" direction="rtl">
                <p style="white-space: pre-line; word-wrap: break-word; word-break: break-all;">
                    {{ drawerContent }}
                </p>
            </el-drawer>

            <!-- 列表数据 -->
            <el-table class="operationlog-table" :data="tableData" style="width: 100%" height="90%" stripe>
                <el-table-column prop="username" label="用户名" min-width="40">
                </el-table-column>
                <el-table-column prop="className" label="类名" min-width="60">
                    <template slot-scope="{ row }">
                        <div class="truncate-text" @click="showFullContent('类名', row.className)">
                            {{ truncateText(row.className) }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="methodName" label="方法名" min-width="60">
                    <template slot-scope="{ row }">
                        <div class="truncate-text" @click="showFullContent('方法名', row.methodName)">
                            {{ truncateText(row.methodName) }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="apiPath" label="API路径" min-width="100">
                    <template slot-scope="{ row }">
                        <div class="truncate-text" @click="showFullContent('API路径', row.apiPath)">
                            {{ truncateText(row.apiPath) }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="apiDesc" label="API描述" min-width="100">
                    <template slot-scope="{ row }">
                        <div class="truncate-text" @click="showFullContent('API描述', row.apiDesc)">
                            {{ truncateText(row.apiDesc) }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="inParams" label="请求参数" min-width="100">
                    <template slot-scope="{ row }">
                        <div class="truncate-text" @click="showFullContent('请求参数', row.inParams)">
                            {{ truncateText(row.inParams) }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="outParams" label="响应内容" min-width="100">
                    <template slot-scope="{ row }">
                        <div class="truncate-text" @click="showFullContent('响应内容', row.outParams)">
                            {{ truncateText(row.outParams) }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="spendTimeMillis" label="执行耗时（毫秒）" min-width="60">
                </el-table-column>
                <el-table-column prop="requestTime" label="操作时间" min-width="100">
                </el-table-column>
            </el-table>

            <div class="page">
                <el-pagination layout="total, sizes, prev, pager, next" :total="this.total" @current-change="handlePage"
                    @size-change="handlePageSizeChange">
                </el-pagination>
            </div>
        </div>

    </PageStateWrapper>
</template>

<script>
import { getOperationLogs, getUsers } from '../api'
import PageStateWrapper from '../components/PageStateWrapper.vue'

export default {
    components: {
        PageStateWrapper
    },
    data() {
        return {
            users: [],
            tableData: [],
            queryForm: { username: '', startTime: '', endTime: '', apiPath: '' },
            pageData: { pageNo: 1, pageSize: 10 },
            total: 0,
            isDataLoaded: false,
            stateConfig: {
                queryForm: 'queryForm',
                pageData: 'pageData',
                tableData: 'tableData',
                total: 'total'
            },
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
            optValue: '',
            maxLength: 15,
            drawerTitle: '',
            drawerVisible: false,
            drawerContent: '',
        }
    },
    methods: {
        updateUsername(val) {
            this.queryForm.username = val
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
                    this.isDataLoaded = true
                } else {
                    this.$message.error(data.message)
                }
            }).catch((err) => {
                this.$message.error('系统繁忙，请稍后重试~')
                console.log(err)
            })
        },
        truncateText(text) {
            if (text.length > this.maxLength) {
                return text.slice(0, this.maxLength) + '...';
            } else {
                return text;
            }
        },
        showFullContent(title, content) {
            if (content.length > this.maxLength) {
                this.drawerTitle = title
                this.drawerVisible = true
                this.drawerContent = content
            }
        },

        onStateRestored() {
            this.getUsernames()
        },
        onNeedLoadData() {
            this.getUsernames()
            this.getList()
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

    .operationlog-table {
        .el-tooltip__popper {
            font-size: 14px;
            max-width: 40%
        }
    }

    .page {
        display: flex;
        flex-flow: row-reverse nowrap;
    }
}

.truncate-text {
    cursor: pointer;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
}
</style>