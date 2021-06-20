<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="考核名称" prop="assessmentName">
        <el-input
          v-model="queryParams.assessmentName"
          placeholder="请输入考核名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="考核年份" prop="year">
        <el-date-picker
          style="width:100%;"
          v-model="queryParams.year"
          type="year"
          format="yyyy"
          value-format="yyyy"
          @change="handleQuery"
          placeholder="选择考核年份">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>


    <el-table :stripe="true"
              :border="true"
              v-loading="loading"
              :data="assessmentyearList"
              @selection-change="handleSelectionChange">
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="考核年份" align="center" prop="year" :formatter="yearFormat"/>
      <el-table-column label="考核名称" align="center" prop="assessmentName"/>
      <el-table-column label="考核状态" align="center" prop="orgAssessmentStatus"
                       :formatter="assessmentYearStatusFormat"/>
      <el-table-column label="绩效考评状态" align="center" prop="performanceAppraisalStatus"
                       :formatter="performanceAppraisalStatusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
          >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-if="scope.row.performanceAppraisalStatus == 1"
            v-hasPermi="['party:performanceScore:edit']"
          >评分
          </el-button>
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

    <!-- 添加或修改党组织考核年对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <div :style="bodyStyle">
        <performance-score-card ref="performanceScoreCard"
                                :disabled="disabled" :assessmentyearId="assessmentyearId" @ok="cancel" ></performance-score-card>
      </div>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-if="!disabled" type="primary" @click="submitForm(1)">保 存</el-button>
        <el-button v-if="!disabled" type="primary" @click="submitForm(2)">提 交</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
  import {
    listAssessmentyear,
    getAssessmentyear,
    delAssessmentyear,
    addAssessmentyear,
    updateAssessmentyear,
    exportAssessmentyear
  } from "@/api/party/assessmentyear";
  import {
    listAssessment,
    getAssessment,
    delAssessment,
    addAssessment,
    updateAssessment,
    exportAssessment
  } from "@/api/party/assessment";
  import PerformanceScoreCard from "./performanceScoreCard";

  export default {
    name: "performanceScore",
    components: {
      PerformanceScoreCard
    },
    data() {
      return {
        //选择考核年份
        years: [],
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
        //党支部类型
        orgTypeOptions: [],
        //党支部考核状态
        orgAssessmentStatusOptions: [],
        //年度考核状态
        assessmentYearStatusOptions: [],
        performanceAppraisalStatusOptions: [],
        // 党组织考核年表格数据
        assessmentyearList: [],
        //选择 参加的党组织数据
        assessmentOrgList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          assessmentyearUuid: undefined,
          year: undefined,
          assessmentName: undefined,
          orgAssessmentStatus: 2,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          year: [
            {required: true, message: "考核年份不能为空", trigger: "blur"}
          ],
          assessmentName: [
            {required: true, message: "考核名称不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },

        //uuid
        assessmentyearId: undefined,
        year: undefined,
        assessmentName: undefined,
        orgLoading: true,
        disabled:false
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      // this.init()
      this.getList();
      this.getDicts("org_type").then(response => {
        this.orgTypeOptions = response.data;
      });
      //党组织考核状态
      this.getDicts("activity_plan_status").then(response => {
        this.assessmentYearStatusOptions = response.data;
      });
      this.getDicts("submit_status").then(response => {
        this.performanceAppraisalStatusOptions = response.data;
      });
      //党组织考核状态
      this.getDicts("org_assessment_status").then(response => {
        this.orgAssessmentStatusOptions = response.data;
      });
    },
    methods: {
      yearFormat(row, column) {
        return row.year + "年";
      },
      orgTypeFormat(row, column) {
        return this.selectDictLabel(this.orgTypeOptions, row.djPartyOrg.orgType);
      },
      assessmentYearStatusFormat(row, column) {
        return this.selectDictLabel(this.assessmentYearStatusOptions, row.orgAssessmentStatus);
      },
      performanceAppraisalStatusFormat(row, column) {
        return this.selectDictLabel(this.performanceAppraisalStatusOptions, row.performanceAppraisalStatus);
      },
      orgAssessmentStatusFormat(row, column) {
        return this.selectDictLabel(this.orgAssessmentStatusOptions, row.orgAssessmentStatus);
      },
      partyOrgFormat(row, column) {
        let partyOrgFullName = row.djPartyOrg.partyOrgFullName;
        return partyOrgFullName.substring(partyOrgFullName.indexOf("/") + 1);
      },
      //调用组织架构返回的参加审核的党支部列表
      getJoinOrgList() {
        this.orgLoading = true;
        listAssessment({"assessmentyearUuid": this.form.assessmentyearUuid}).then(response => {
          this.assessmentOrgList = response.rows;
          this.orgLoading = false;
        });
      },
      /** 删除按钮操作 */
      handleArrangeDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除该数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delAssessment(ids);
        }).then(() => {
          this.getJoinOrgList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      //增加 选择计划参与党组织
      openOrgTransfer() {
        this.$refs.orgTransfer.open = true;
        this.$refs.orgTransfer.title = "选择考核党组织";
        this.$refs.orgTransfer.assessmentyearUuid = this.form.assessmentyearUuid;
        this.$refs.orgTransfer.year = this.form.year;
        this.$refs.orgTransfer.assessmentName = this.form.assessmentName;
        this.$refs.orgTransfer.getPartyOrgSelect(1);
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党组织考核年列表 */
      getList() {
        this.loading = true;
        listAssessmentyear(this.queryParams).then(response => {
          this.assessmentyearList = response.rows;
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
          id: undefined,
          assessmentyearUuid: undefined,
          year: undefined,
          assessmentName: undefined,
          orgAssessmentStatus: "1",
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
        this.ids = selection.map(item => item.id)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.disabled = false;
        this.open = true;
        this.title = "添加年度双项考评";
        this.form.assessmentyearUuid = this.uuid();
        this.getJoinOrgList();
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.disabled = false;
        const id = row.id
        getAssessmentyear(id).then(response => {
          this.form = response.data;
          this.assessmentyearId = id
          this.open = true;
          this.title = "项目绩效考核";
        });
      },
      handleSee(row) {
        this.reset();
        this.disabled = true;
        const id = row.id
        getAssessmentyear(id).then(response => {
          this.form = response.data;
          this.assessmentyearId = id
          this.open = true;
          this.title = "查看项目绩效考核";
        });
      },
      /** 提交按钮 */
      submitForm: function (orgAssessmentStatus) {

        if (orgAssessmentStatus== 2 && this.assessmentOrgList.length == 0) {
          this.msgError("请选择考核党组织!");
          return;
        }
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.orgAssessmentStatus = orgAssessmentStatus;
            if (this.form.id != undefined) {
              updateAssessmentyear(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addAssessmentyear(this.form).then(response => {
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
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除该党组织考核数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delAssessmentyear(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党组织考核年数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportAssessmentyear(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
