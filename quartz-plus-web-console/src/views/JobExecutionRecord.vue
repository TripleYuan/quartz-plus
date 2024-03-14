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
                    <el-button type="primary" @click="this.getJobExecutionRecordList">查找</el-button>
                </el-form-item>
            </el-form>
        </div>

        <el-drawer :title="drawerTitle" :visible.sync="drawerVisible" direction="rtl">
          <span>{{ drawerContent }}</span>
        </el-drawer>

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
            <el-table-column prop="costTime" label="任务耗时（毫秒）">
            </el-table-column>
            <el-table-column prop="exception" label="异常信息">
              <template slot-scope="{ row }">
                <div class="truncate-text" @click="showFullContent('异常信息', row.exception)">
                  {{ truncateText(row.exception) }}
                </div>
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
import { getSchedNames, getJobExecutionRecords, } from '../api'
export default {
    name: 'jobExecutionRecordList',
    data() {
        return {
            schedNames: [],
            tableData: [],
            queryForm: { schedName: '', jobName: '' },
            pageData: { pageNo: 1, pageSize: 10 },
            total: 0,
            maxLength: 60,
            drawerTitle: '',
            drawerVisible: false,
            drawerContent: '',
        }
    },
    methods: {
        handleSelectChange(val) {
            this.queryForm.schedName = val
            this.getJobExecutionRecordList()
        },
        handlePage(val) {
            this.pageData.pageNo = val
            this.getJobExecutionRecordList()
        },
        handlePageSizeChange(val) {
            this.pageData.pageSize = val
            this.getJobExecutionRecordList()
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
        getJobExecutionRecordList() {
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
        truncateText(text) {
          if (text && text.length > this.maxLength) {
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
    },
    mounted() {
        this.getSchedNameList()
        this.getJobExecutionRecordList()
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