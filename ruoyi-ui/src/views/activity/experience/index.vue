<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
                                                                                                                                            <el-form-item label="心得体会uuid" prop="experienceUuid">
                            <el-input
                                    v-model="queryParams.experienceUuid"
                                    placeholder="请输入心得体会uuid"
                                    clearable
                                    size="small"
                                    @keyup.enter.native="handleQuery"
                            />
                        </el-form-item>
                                                                                                                                                    <el-form-item label="活动uuid" prop="detailedUuid">
                            <el-input
                                    v-model="queryParams.detailedUuid"
                                    placeholder="请输入活动uuid"
                                    clearable
                                    size="small"
                                    @keyup.enter.native="handleQuery"
                            />
                        </el-form-item>
                                                                                                                                                                                                                                                                                                                                                                    <el-form-item label="党员id" prop="partyMemberId">
                            <el-input
                                    v-model="queryParams.partyMemberId"
                                    placeholder="请输入党员id"
                                    clearable
                                    size="small"
                                    @keyup.enter.native="handleQuery"
                            />
                        </el-form-item>
                                                                                                                                                                                                        <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                        type="primary"
                        icon="el-icon-plus"
                        size="mini"
                        @click="handleAdd"
                        v-hasPermi="['activity:experience:add']"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        icon="el-icon-edit"
                        size="mini"
                        :disabled="single"
                        @click="handleUpdate"
                        v-hasPermi="['activity:experience:edit']"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        icon="el-icon-delete"
                        size="mini"
                        :disabled="multiple"
                        @click="handleDelete"
                        v-hasPermi="['activity:experience:remove']"
                >删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        icon="el-icon-download"
                        size="mini"
                        @click="handleExport"
                        v-hasPermi="['activity:experience:export']"
                >导出</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" :data="experienceList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
                                                                                <el-table-column label="心得体会ID" align="center" prop="experienceId" />
                                                                                                <el-table-column label="心得体会uuid" align="center" prop="experienceUuid" />
                                                                                                <el-table-column label="活动uuid" align="center" prop="detailedUuid" />
                                                                                                <el-table-column label="标题" align="center" prop="title" />
                                                                                                <el-table-column label="内容" align="center" prop="content" />
                                                                                                <el-table-column label="党员id" align="center" prop="partyMemberId" />
                                                                                                                                                                                                                                                                                                                                                    <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-edit"
                            @click="handleUpdate(scope.row)"
                            v-hasPermi="['activity:experience:edit']"
                    >修改</el-button>
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)"
                            v-hasPermi="['activity:experience:remove']"
                    >删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination
                v-show="total>0"
                :total="total"
                :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize"
                @pagination="getList"
        />

        <!-- 添加或修改心得体会对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
                   @open="getHeight"  :close-on-click-modal="false">
            <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="100px">
                                                                                                                                                                                                                    <el-form-item label="心得体会uuid" prop="experienceUuid">
                                    <el-input v-model="form.experienceUuid" placeholder="请输入心得体会uuid" />
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="活动uuid" prop="detailedUuid">
                                    <el-input v-model="form.detailedUuid" placeholder="请输入活动uuid" />
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="标题" prop="title">
                                    <el-input v-model="form.title" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入内容" />
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="内容" prop="content">
                                    <el-input v-model="form.content" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入内容" />
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="党员id" prop="partyMemberId">
                                    <el-input v-model="form.partyMemberId" placeholder="请输入党员id" />
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="删除标志" prop="delFlag">
                                    <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
                                </el-form-item>
                                                                                                                                                                                                                                                                                                                                                                                                                                    </el-form>
            <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import { listExperience, getExperience, delExperience, addExperience, updateExperience, exportExperience } from "@/api/activity/experience";

    export default {
        name: "Experience",
        data() {
            return {
                // 遮罩层
                loading: true,
                // 选中数组
                ids: [],
                // 非单个禁用
                single: true,
                // 非多个禁用
                multiple: true,
                // 总条数
                total: 0,
                // 心得体会表格数据
                    experienceList: [],
                // 弹出层标题
                title: "",
                // 是否显示弹出层
                open: false,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    // 查询参数
                queryParams: {
                    pageNum: 1,
                    pageSize: 10,
                                                                            experienceUuid: undefined,
                                                                detailedUuid: undefined,
                                                                title: undefined,
                                                                content: undefined,
                                                                partyMemberId: undefined,
                                                                                                                                                                                },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                                                                                                                                                                                                                        title: [
                        { required: true, message: "标题不能为空", trigger: "blur" }
                    ],
                                                                                                                                content: [
                        { required: true, message: "内容不能为空", trigger: "blur" }
                    ],
                                                                                                                                                                                                                                                                        },
            bodyStyle:{
                overflowY:'auto',
                        height: '',
                        marginLeft:'20%' ,
                        paddingRight:'20%',
            },
        };
        },
        mounted () {
            window.addEventListener('resize', this.getHeight);
        },
        created() {
            this.getList();
                                                                                                                                                                                                                                                                                                                                        },
        methods: {
            /** 对话框自适应高度 */
            getHeight(){
                this.bodyStyle.height=window.innerHeight-281+'px';
            },
            /** 查询心得体会列表 */
            getList() {
                this.loading = true;
                listExperience(this.queryParams).then(response => {
                    this.experienceList = response.rows;
                    this.total = response.total;
                    this.loading = false;
                });
            },
                                                                                                                                                                                                                                                                                                                                            // 取消按钮
            cancel() {
                this.open = false;
                this.reset();
            },
            // 表单重置
            reset() {
                this.form = {
                                                            experienceId: undefined,
                                                                                experienceUuid: undefined,
                                                                                detailedUuid: undefined,
                                                                                title: undefined,
                                                                                content: undefined,
                                                                                partyMemberId: undefined,
                                                                                delFlag: undefined,
                                                                                createBy: undefined,
                                                                                createTime: undefined,
                                                                                updateBy: undefined,
                                                                                updateTime: undefined
                                                };
                this.resetForm("form");
            },
            /** 搜索按钮操作 */
            handleQuery() {
                this.queryParams.pageNum = 1;
                this.getList();
            },
            /** 重置按钮操作 */
            resetQuery() {
                this.resetForm("queryForm");
                this.handleQuery();
            },
            // 多选框选中数据
            handleSelectionChange(selection) {
                this.ids = selection.map(item => item.experienceId)
                this.single = selection.length!=1
                this.multiple = !selection.length
            },
            /** 新增按钮操作 */
            handleAdd() {
                this.reset();
                this.open = true;
                this.title = "添加心得体会";
            },
            /** 修改按钮操作 */
            handleUpdate(row) {
                this.reset();
                const experienceId = row.experienceId || this.ids
                getExperience(experienceId).then(response => {
                    this.form = response.data;
                    this.open = true;
                    this.title = "修改心得体会";
                });
            },
            /** 提交按钮 */
            submitForm: function() {
                this.$refs["form"].validate(valid => {
                    if (valid) {
                        if (this.form.experienceId != undefined) {
                            updateExperience(this.form).then(response => {
                                if (response.code === 200) {
                                    this.msgSuccess("修改成功");
                                    this.open = false;
                                    this.getList();
                                } else {
                                    this.msgError(response.msg);
                                }
                            });
                        } else {
                            addExperience(this.form).then(response => {
                                if (response.code === 200) {
                                    this.msgSuccess("新增成功");
                                    this.open = false;
                                    this.getList();
                                } else {
                                    this.msgError(response.msg);
                                }
                            });
                        }
                    }
                });
            },
            /** 删除按钮操作 */
            handleDelete(row) {
                const experienceIds = row.experienceId || this.ids;
                this.$confirm('是否确认删除心得体会编号为"' + experienceIds + '"的数据项?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(function() {
                    return delExperience(experienceIds);
                }).then(() => {
                    this.getList();
                    this.msgSuccess("删除成功");
                }).catch(function() {});
            },
            /** 导出按钮操作 */
            handleExport() {
                const queryParams = this.queryParams;
                this.$confirm('是否确认导出所有心得体会数据项?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(function() {
                    return exportExperience(queryParams);
                }).then(response => {
                    this.download(response.msg);
                }).catch(function() {});
            }
        }
    };
</script>
