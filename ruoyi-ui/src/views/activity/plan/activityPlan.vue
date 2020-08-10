<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
                                                                                                                                            <el-form-item label="活动唯一uuid" prop="planUuid">
                            <el-input
                                    v-model="queryParams.planUuid"
                                    placeholder="请输入活动唯一uuid"
                                    clearable
                                    size="small"
                                    @keyup.enter.native="handleQuery"
                            />
                        </el-form-item>
                                                                                                                                                    <el-form-item label="活动来源" prop="activitySources">
                            <el-select v-model="queryParams.activitySources" placeholder="请选择活动来源" clearable size="small">
                                <el-option
                                        v-for="dict in activitySourcesOptions"
                                        :key="dict.dictValue"
                                        :label="dict.dictLabel"
                                        :value="dict.dictValue"
                                />
                            </el-select>
                        </el-form-item>
                                                                                                                                                    <el-form-item label="活动主题" prop="activityTheme">
                            <el-input
                                    v-model="queryParams.activityTheme"
                                    placeholder="请输入活动主题"
                                    clearable
                                    size="small"
                                    @keyup.enter.native="handleQuery"
                            />
                        </el-form-item>
                                                                                                                                                    <el-form-item label="党员活动类型" prop="activityType">
                            <el-select v-model="queryParams.activityType" placeholder="请选择党员活动类型" clearable size="small">
                                <el-option
                                        v-for="dict in activityTypeOptions"
                                        :key="dict.dictValue"
                                        :label="dict.dictLabel"
                                        :value="dict.dictValue"
                                />
                            </el-select>
                        </el-form-item>
                                                                                                                                                    <el-form-item label="召开周期" prop="conveneCycle">
                            <el-select v-model="queryParams.conveneCycle" placeholder="请选择召开周期" clearable size="small">
                                <el-option
                                        v-for="dict in conveneCycleOptions"
                                        :key="dict.dictValue"
                                        :label="dict.dictLabel"
                                        :value="dict.dictValue"
                                />
                            </el-select>
                        </el-form-item>
                                                                                                                                                    <el-form-item label="周期开始日期" prop="cycleStartTime">
                            <el-date-picker clearable size="small" style="width: 200px"
                                            v-model="queryParams.cycleStartTime"
                                            type="date"
                                            value-format="yyyy-MM-dd"
                                            placeholder="选择周期开始日期">
                            </el-date-picker>
                        </el-form-item>
                                                                                                                                                    <el-form-item label="周期结束日期" prop="cycleEndTime">
                            <el-date-picker clearable size="small" style="width: 200px"
                                            v-model="queryParams.cycleEndTime"
                                            type="date"
                                            value-format="yyyy-MM-dd"
                                            placeholder="选择周期结束日期">
                            </el-date-picker>
                        </el-form-item>
                                                                                                                                                    <el-form-item label="周期开始年份" prop="cycleStartYear">
                            <el-select v-model="queryParams.cycleStartYear" placeholder="请选择周期开始年份" clearable size="small">
                                <el-option
                                        v-for="dict in cycleStartYearOptions"
                                        :key="dict.dictValue"
                                        :label="dict.dictLabel"
                                        :value="dict.dictValue"
                                />
                            </el-select>
                        </el-form-item>
                                                                                                                                                    <el-form-item label="周期开始年份(季度/月份)" prop="cycleStartYearUnit">
                            <el-select v-model="queryParams.cycleStartYearUnit" placeholder="请选择周期开始年份(季度/月份)" clearable size="small">
                                <el-option
                                        v-for="dict in cycleStartYearUnitOptions"
                                        :key="dict.dictValue"
                                        :label="dict.dictLabel"
                                        :value="dict.dictValue"
                                />
                            </el-select>
                        </el-form-item>
                                                                                                                                                    <el-form-item label="周期截止年份" prop="cycleEndYear">
                            <el-select v-model="queryParams.cycleEndYear" placeholder="请选择周期截止年份" clearable size="small">
                                <el-option
                                        v-for="dict in cycleEndYearOptions"
                                        :key="dict.dictValue"
                                        :label="dict.dictLabel"
                                        :value="dict.dictValue"
                                />
                            </el-select>
                        </el-form-item>
                                                                                                                                                    <el-form-item label="周期截止年份(季度/月份)" prop="cycleEndYearUnit">
                            <el-select v-model="queryParams.cycleEndYearUnit" placeholder="请选择周期截止年份(季度/月份)" clearable size="small">
                                <el-option
                                        v-for="dict in cycleEndYearUnitOptions"
                                        :key="dict.dictValue"
                                        :label="dict.dictLabel"
                                        :value="dict.dictValue"
                                />
                            </el-select>
                        </el-form-item>
                                                                                                                                                                                                                                                            <el-form-item label="计划状态" prop="status">
                            <el-select v-model="queryParams.status" placeholder="请选择计划状态" clearable size="small">
                                <el-option label="请选择字典生成" value="" />
                            </el-select>
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
                        v-hasPermi="['activity:plan:add']"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        icon="el-icon-edit"
                        size="mini"
                        :disabled="single"
                        @click="handleUpdate"
                        v-hasPermi="['activity:plan:edit']"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        icon="el-icon-delete"
                        size="mini"
                        :disabled="multiple"
                        @click="handleDelete"
                        v-hasPermi="['activity:plan:remove']"
                >删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        icon="el-icon-download"
                        size="mini"
                        @click="handleExport"
                        v-hasPermi="['activity:plan:export']"
                >导出</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" :data="planList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
                                                                                <el-table-column label="活动计划ID" align="center" prop="planId" />
                                                                                                <el-table-column label="活动唯一uuid" align="center" prop="planUuid" />
                                                                                                <el-table-column label="活动来源" align="center" prop="activitySources" :formatter="activitySourcesFormat" />
                                                                                                <el-table-column label="活动主题" align="center" prop="activityTheme" />
                                                                                                <el-table-column label="党员活动类型" align="center" prop="activityType" :formatter="activityTypeFormat" />
                                                                                                <el-table-column label="召开周期" align="center" prop="conveneCycle" :formatter="conveneCycleFormat" />
                                                                                                <el-table-column label="周期开始日期" align="center" prop="cycleStartTime" width="180">
                        <template slot-scope="scope">
                            <span>{{ parseTime(scope.row.cycleStartTime, '{y}-{m}-{d}') }}</span>
                        </template>
                    </el-table-column>
                                                                                                <el-table-column label="周期结束日期" align="center" prop="cycleEndTime" width="180">
                        <template slot-scope="scope">
                            <span>{{ parseTime(scope.row.cycleEndTime, '{y}-{m}-{d}') }}</span>
                        </template>
                    </el-table-column>
                                                                                                <el-table-column label="周期开始年份" align="center" prop="cycleStartYear" :formatter="cycleStartYearFormat" />
                                                                                                <el-table-column label="周期开始年份(季度/月份)" align="center" prop="cycleStartYearUnit" :formatter="cycleStartYearUnitFormat" />
                                                                                                <el-table-column label="周期截止年份" align="center" prop="cycleEndYear" :formatter="cycleEndYearFormat" />
                                                                                                <el-table-column label="周期截止年份(季度/月份)" align="center" prop="cycleEndYearUnit" :formatter="cycleEndYearUnitFormat" />
                                                                                                <el-table-column label="活动计划要求" align="center" prop="actPlanRequirements" />
                                                                                                <el-table-column label="计划状态" align="center" prop="status" />
                                                                                                                                                                                                                                                                                                                                                    <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-edit"
                            @click="handleUpdate(scope.row)"
                            v-hasPermi="['activity:plan:edit']"
                    >修改</el-button>
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)"
                            v-hasPermi="['activity:plan:remove']"
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

        <!-- 添加或修改活动计划对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
                   @open="getHeight"  :close-on-click-modal="false">
            <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="100px">
                                                                                                                                                                                                                    <el-form-item label="活动唯一uuid" prop="planUuid">
                                    <el-input v-model="form.planUuid" placeholder="请输入活动唯一uuid" />
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="活动来源">
                                    <el-select v-model="form.activitySources" placeholder="请选择活动来源">
                                        <el-option
                                                v-for="dict in activitySourcesOptions"
                                                :key="dict.dictValue"
                                                :label="dict.dictLabel"
                                                :value="dict.dictValue"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="活动主题" prop="activityTheme">
                                    <el-input v-model="form.activityTheme" placeholder="请输入活动主题" />
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="党员活动类型">
                                    <el-select v-model="form.activityType" placeholder="请选择党员活动类型">
                                        <el-option
                                                v-for="dict in activityTypeOptions"
                                                :key="dict.dictValue"
                                                :label="dict.dictLabel"
                                                :value="dict.dictValue"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="召开周期">
                                    <el-select v-model="form.conveneCycle" placeholder="请选择召开周期">
                                        <el-option
                                                v-for="dict in conveneCycleOptions"
                                                :key="dict.dictValue"
                                                :label="dict.dictLabel"
                                                :value="dict.dictValue"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="周期开始日期" prop="cycleStartTime">
                                    <el-date-picker clearable size="small" style="width: 200px"
                                                    v-model="form.cycleStartTime"
                                                    type="date"
                                                    value-format="yyyy-MM-dd"
                                                    placeholder="选择周期开始日期">
                                    </el-date-picker>
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="周期结束日期" prop="cycleEndTime">
                                    <el-date-picker clearable size="small" style="width: 200px"
                                                    v-model="form.cycleEndTime"
                                                    type="date"
                                                    value-format="yyyy-MM-dd"
                                                    placeholder="选择周期结束日期">
                                    </el-date-picker>
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="周期开始年份">
                                    <el-select v-model="form.cycleStartYear" placeholder="请选择周期开始年份">
                                        <el-option
                                                v-for="dict in cycleStartYearOptions"
                                                :key="dict.dictValue"
                                                :label="dict.dictLabel"
                                                :value="dict.dictValue"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="周期开始年份(季度/月份)">
                                    <el-select v-model="form.cycleStartYearUnit" placeholder="请选择周期开始年份(季度/月份)">
                                        <el-option
                                                v-for="dict in cycleStartYearUnitOptions"
                                                :key="dict.dictValue"
                                                :label="dict.dictLabel"
                                                :value="dict.dictValue"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="周期截止年份">
                                    <el-select v-model="form.cycleEndYear" placeholder="请选择周期截止年份">
                                        <el-option
                                                v-for="dict in cycleEndYearOptions"
                                                :key="dict.dictValue"
                                                :label="dict.dictLabel"
                                                :value="dict.dictValue"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="周期截止年份(季度/月份)">
                                    <el-select v-model="form.cycleEndYearUnit" placeholder="请选择周期截止年份(季度/月份)">
                                        <el-option
                                                v-for="dict in cycleEndYearUnitOptions"
                                                :key="dict.dictValue"
                                                :label="dict.dictLabel"
                                                :value="dict.dictValue"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="活动计划要求" prop="actPlanRequirements">
                                    <el-input v-model="form.actPlanRequirements" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入内容" />
                                </el-form-item>
                                                                                                                                                                                                                                                        <el-form-item label="计划状态">
                                    <el-radio-group v-model="form.status">
                                        <el-radio label="1">请选择字典生成</el-radio>
                                    </el-radio-group>
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
    import { listPlan, getPlan, delPlan, addPlan, updatePlan, exportPlan } from "@/api/activity/plan";

    export default {
        name: "Plan",
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
                // 活动计划表格数据
                    planList: [],
                // 弹出层标题
                title: "",
                // 是否显示弹出层
                open: false,
                                                                                                                                                                                                                                                            // 活动来源字典
                            activitySourcesOptions: [],
                                                                                                                                                                                                    // 党员活动类型字典
                            activityTypeOptions: [],
                                                                                                                        // 召开周期字典
                            conveneCycleOptions: [],
                                                                                                                                                                                                                                                                                // 周期开始年份字典
                            cycleStartYearOptions: [],
                                                                                                                        // 周期开始年份(季度/月份)字典
                            cycleStartYearUnitOptions: [],
                                                                                                                        // 周期截止年份字典
                            cycleEndYearOptions: [],
                                                                                                                        // 周期截止年份(季度/月份)字典
                            cycleEndYearUnitOptions: [],
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        // 查询参数
                queryParams: {
                    pageNum: 1,
                    pageSize: 10,
                                                                            planUuid: undefined,
                                                                activitySources: undefined,
                                                                activityTheme: undefined,
                                                                activityType: undefined,
                                                                conveneCycle: undefined,
                                                                cycleStartTime: undefined,
                                                                cycleEndTime: undefined,
                                                                cycleStartYear: undefined,
                                                                cycleStartYearUnit: undefined,
                                                                cycleEndYear: undefined,
                                                                cycleEndYearUnit: undefined,
                                                                actPlanRequirements: undefined,
                                                                status: undefined,
                                                                                                                                                                                },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                                                                                                                                                planUuid: [
                        { required: true, message: "活动唯一uuid不能为空", trigger: "blur" }
                    ],
                                                                                                                                activitySources: [
                        { required: true, message: "活动来源不能为空", trigger: "blur" }
                    ],
                                                                                                                                activityTheme: [
                        { required: true, message: "活动主题不能为空", trigger: "blur" }
                    ],
                                                                                                                                activityType: [
                        { required: true, message: "党员活动类型不能为空", trigger: "blur" }
                    ],
                                                                                                                                conveneCycle: [
                        { required: true, message: "召开周期不能为空", trigger: "blur" }
                    ],
                                                                                                                                cycleStartTime: [
                        { required: true, message: "周期开始日期不能为空", trigger: "blur" }
                    ],
                                                                                                                                cycleEndTime: [
                        { required: true, message: "周期结束日期不能为空", trigger: "blur" }
                    ],
                                                                                                                                cycleStartYear: [
                        { required: true, message: "周期开始年份不能为空", trigger: "blur" }
                    ],
                                                                                                                                cycleStartYearUnit: [
                        { required: true, message: "周期开始年份(季度/月份)不能为空", trigger: "blur" }
                    ],
                                                                                                                                cycleEndYear: [
                        { required: true, message: "周期截止年份不能为空", trigger: "blur" }
                    ],
                                                                                                                                cycleEndYearUnit: [
                        { required: true, message: "周期截止年份(季度/月份)不能为空", trigger: "blur" }
                    ],
                                                                                                                                actPlanRequirements: [
                        { required: true, message: "活动计划要求不能为空", trigger: "blur" }
                    ],
                                                                                                                                status: [
                        { required: true, message: "计划状态不能为空", trigger: "blur" }
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
                                                                                                        this.getDicts("activity_sources").then(response => {
                        this.activitySourcesOptions = response.data;
                    });
                                                                                            this.getDicts("activity_type").then(response => {
                        this.activityTypeOptions = response.data;
                    });
                                                                this.getDicts("convene_cycle").then(response => {
                        this.conveneCycleOptions = response.data;
                    });
                                                                                                                        this.getDicts("quarter_type").then(response => {
                        this.cycleStartYearOptions = response.data;
                    });
                                                                this.getDicts("quarter_type").then(response => {
                        this.cycleStartYearUnitOptions = response.data;
                    });
                                                                this.getDicts("quarter_type").then(response => {
                        this.cycleEndYearOptions = response.data;
                    });
                                                                this.getDicts("quarter_type").then(response => {
                        this.cycleEndYearUnitOptions = response.data;
                    });
                                                                                                                                                                                                                                        },
        methods: {
            /** 对话框自适应高度 */
            getHeight(){
                this.bodyStyle.height=window.innerHeight-281+'px';
            },
            /** 查询活动计划列表 */
            getList() {
                this.loading = true;
                listPlan(this.queryParams).then(response => {
                    this.planList = response.rows;
                    this.total = response.total;
                    this.loading = false;
                });
            },
                                                                                                                                                // 活动来源字典翻译
                        activitySourcesFormat(row, column) {
                        return this.selectDictLabel(this.activitySourcesOptions, row.activitySources);
                    },
                                                                                                                                    // 党员活动类型字典翻译
                        activityTypeFormat(row, column) {
                        return this.selectDictLabel(this.activityTypeOptions, row.activityType);
                    },
                                                                                                        // 召开周期字典翻译
                        conveneCycleFormat(row, column) {
                        return this.selectDictLabel(this.conveneCycleOptions, row.conveneCycle);
                    },
                                                                                                                                                                // 周期开始年份字典翻译
                        cycleStartYearFormat(row, column) {
                        return this.selectDictLabel(this.cycleStartYearOptions, row.cycleStartYear);
                    },
                                                                                                        // 周期开始年份(季度/月份)字典翻译
                        cycleStartYearUnitFormat(row, column) {
                        return this.selectDictLabel(this.cycleStartYearUnitOptions, row.cycleStartYearUnit);
                    },
                                                                                                        // 周期截止年份字典翻译
                        cycleEndYearFormat(row, column) {
                        return this.selectDictLabel(this.cycleEndYearOptions, row.cycleEndYear);
                    },
                                                                                                        // 周期截止年份(季度/月份)字典翻译
                        cycleEndYearUnitFormat(row, column) {
                        return this.selectDictLabel(this.cycleEndYearUnitOptions, row.cycleEndYearUnit);
                    },
                                                                                                                                                                                                                                            // 取消按钮
            cancel() {
                this.open = false;
                this.reset();
            },
            // 表单重置
            reset() {
                this.form = {
                                                            planId: undefined,
                                                                                planUuid: undefined,
                                                                                activitySources: undefined,
                                                                                activityTheme: undefined,
                                                                                activityType: undefined,
                                                                                conveneCycle: undefined,
                                                                                cycleStartTime: undefined,
                                                                                cycleEndTime: undefined,
                                                                                cycleStartYear: undefined,
                                                                                cycleStartYearUnit: undefined,
                                                                                cycleEndYear: undefined,
                                                                                cycleEndYearUnit: undefined,
                                                                                actPlanRequirements: undefined,
                                                                                status: "0",
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
                this.ids = selection.map(item => item.planId)
                this.single = selection.length!=1
                this.multiple = !selection.length
            },
            /** 新增按钮操作 */
            handleAdd() {
                this.reset();
                this.open = true;
                this.title = "添加活动计划";
            },
            /** 修改按钮操作 */
            handleUpdate(row) {
                this.reset();
                const planId = row.planId || this.ids
                getPlan(planId).then(response => {
                    this.form = response.data;
                    this.open = true;
                    this.title = "修改活动计划";
                });
            },
            /** 提交按钮 */
            submitForm: function() {
                this.$refs["form"].validate(valid => {
                    if (valid) {
                        if (this.form.planId != undefined) {
                            updatePlan(this.form).then(response => {
                                if (response.code === 200) {
                                    this.msgSuccess("修改成功");
                                    this.open = false;
                                    this.getList();
                                } else {
                                    this.msgError(response.msg);
                                }
                            });
                        } else {
                            addPlan(this.form).then(response => {
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
                const planIds = row.planId || this.ids;
                this.$confirm('是否确认删除活动计划编号为"' + planIds + '"的数据项?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(function() {
                    return delPlan(planIds);
                }).then(() => {
                    this.getList();
                    this.msgSuccess("删除成功");
                }).catch(function() {});
            },
            /** 导出按钮操作 */
            handleExport() {
                const queryParams = this.queryParams;
                this.$confirm('是否确认导出所有活动计划数据项?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(function() {
                    return exportPlan(queryParams);
                }).then(response => {
                    this.download(response.msg);
                }).catch(function() {});
            }
        }
    };
</script>
