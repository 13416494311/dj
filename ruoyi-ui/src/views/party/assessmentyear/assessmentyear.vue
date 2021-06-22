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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['party:assessmentyear:add']"
        >新增
        </el-button>
      </el-col>
    </el-row>

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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
          >查看
          </el-button>
          <el-button
            size="small"
            type="text"
            icon="el-icon-edit"
            v-if="scope.row.orgAssessmentStatus == 1"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:assessmentyear:edit']"
          >修改
          </el-button>
          <el-button
            size="small"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-if="scope.row.orgAssessmentStatus == 1"
            v-hasPermi="['party:assessmentyear:remove']"
          >删除
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
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="150px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>
          <el-form-item label="考核年份" prop="year">
            <el-date-picker
              :disabled="disabled"
              style="width: 50%;"
              v-model="form.year"
              type="year"
              format="yyyy"
              value-format="yyyy"
              placeholder="选择年">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="考核名称" prop="assessmentName">
            <el-input :disabled="disabled"
                      v-model="form.assessmentName" type="textarea" :autosize="{ minRows: 3, maxRows: 6}"
                      placeholder="请输入内容"/>
          </el-form-item>
        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">党支部考评</span>
            <el-button
              v-if="!disabled"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="openOrgTransfer"
              style="float: right;margin-top: -5px"
            >新增
            </el-button>
          </div>
          <el-table :stripe="true"
                    :border="true"
                    v-loading="orgLoading" :data="assessmentOrgList">
            <el-table-column label="序号" align="center" type="index"/>
            <el-table-column label="党组织名称" align="center"
                             width="200"
                             prop="djPartyOrg.partyOrgFullName" :formatter="partyOrgFormat"/>
            <!--<el-table-column label="党组织类型" align="center" prop="djPartyOrg.orgType"
                             :formatter="orgTypeFormat"/>-->
            <el-table-column label="考评状态" align="center" prop="orgAssessmentStatus"
                             :formatter="orgAssessmentStatusFormat"/>
            <!--<el-table-column label="自评得分" align="center" prop="assessmentSelfScore"
                             :formatter="scoreFormat"/>-->
            <el-table-column label="党委评分" align="center" prop="assessmentScore"
                             :formatter="scoreFormat">
              <template slot-scope="scope">
                <el-button @click="assessmentScoreDataShow(scope.row)" type="text">
                  {{ scoreFormat1(scope.row.assessmentScore)}}</el-button>
              </template>
            </el-table-column>
            <el-table-column label="是否项目绩效考核" align="center" prop="djPartyOrg.performanceAppraisal"
                             :formatter="performanceAppraisalFormat"/>
            <el-table-column label="项目绩效状态" align="center" prop="performanceAppraisalStatus"
                             :formatter="performanceAppraisalStatusFormat"/>
            <el-table-column label="项目绩效得分" align="center" prop="performanceAppraisalScore">
              <template slot-scope="scope">
                <el-button @click="performanceAppraisalScoreDataShow(scope.row)" type="text">
                  {{ scoreFormat1(scope.row.performanceAppraisalScore)}}</el-button>
              </template>
            </el-table-column>
            <el-table-column label="总分" align="center" prop="score">
              <template slot-scope="scope">
                {{ scope.row.score==undefined?'':scope.row.score+' 分'}}
              </template>
            </el-table-column>
            <el-table-column label="排名" align="center" prop="rankNum" :formatter="rankNumFormat"/>

            <el-table-column  v-if="!disabled" label="操作" align="center"
                              class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="small"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleArrangeDelete(scope.row)"
                >删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-if="!disabled" type="primary" @click="submitForm(1)">保 存</el-button>
        <el-button v-if="!disabled" type="primary" @click="submitForm(2)">发 布</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
      <org-transfer ref="orgTransfer" @callbackOrg="getJoinOrgList"/>
    </el-dialog>


    <assessment-dialog ref="assessmentDialog"
                       :type="type"
                       :assessmentId="assessmentId"
                       :assessmentyearId="assessmentyearId"
    ></assessment-dialog>

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
  import OrgTransfer from "./orgTransfer";
  import {
    listAssessment,
    listAssessmentRank,
    getAssessment,
    delAssessment,
    addAssessment,
    updateAssessment,
    exportAssessment
  } from "@/api/party/assessment";
  import AssessmentDialog from "./assessmnetDialog";

  export default {
    name: "Assessmentyear",
    components: {
      AssessmentDialog,
      OrgTransfer,
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
        //绩效考核状态
        performanceAppraisalStatusOptions: [],
        // 是否字典
        performanceAppraisalOptions:[],
        //党支部考核状态
        orgAssessmentStatusOptions: [],
        //年度考核状态
        assessmentYearStatusOptions: [],
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
          orgAssessmentStatus: undefined,
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
        assessmentyearUuid: undefined,
        year: undefined,
        assessmentName: undefined,
        orgLoading: true,
        disabled:false,
        assessmentDialogOpen:false,
        assessmentId:undefined,
        assessmentyearId:undefined,
        type:undefined,
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
      //党组织考核状态
      this.getDicts("org_assessment_status").then(response => {
        this.orgAssessmentStatusOptions = response.data;
      });
      //绩效考核状态
      this.getDicts("submit_status").then(response => {
        this.performanceAppraisalStatusOptions = response.data;
      });
      //系统是否
      this.getDicts("sys_yes_no").then(response => {
        this.performanceAppraisalOptions = response.data;
      });
    },
    methods: {
      assessmentScoreDataShow(row){
        this.assessmentId = row.id;
        this.type = 1 ;
        this.$refs.assessmentDialog.open = true
        this.$refs.assessmentDialog.title = "党委评分"
      },
      performanceAppraisalScoreDataShow(row){
        this.assessmentyearId = this.form.id
        this.assessmentId = row.id;
        this.type = 2 ;
        this.$refs.assessmentDialog.open = true
        this.$refs.assessmentDialog.title = "项目绩效考核"
      },
      performanceAppraisalFormat(row, column) {
        return this.selectDictLabel(this.performanceAppraisalOptions, row.djPartyOrg.performanceAppraisal);
      },
      rankNumFormat(row, column,cellValue, index) {
         if(row.score && row.score !== 0){
           return row.rankNum;
         }else{
           return '';
         }
      },
      performanceAppraisalStatusFormat(row, column) {
        return this.selectDictLabel(this.performanceAppraisalStatusOptions, row.performanceAppraisalStatus);
      },
      scoreFormat(row, column,cellValue, index) {
        return cellValue==undefined?'':cellValue.toFixed(1) + " 分";
      },
      scoreFormat1(cellValue) {
        return cellValue==undefined?'':cellValue.toFixed(1) + " 分";
      },
      yearFormat(row, column) {
        return row.year + "年";
      },
      orgTypeFormat(row, column) {
        return this.selectDictLabel(this.orgTypeOptions, row.djPartyOrg.orgType);
      },
      assessmentYearStatusFormat(row, column) {
        return this.selectDictLabel(this.assessmentYearStatusOptions, row.orgAssessmentStatus);
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
        listAssessmentRank({"assessmentyearUuid": this.form.assessmentyearUuid}).then(response => {
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
        const id = row.id || this.ids
        getAssessmentyear(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改年度双项考评";
        }).then(() => {
          this.getJoinOrgList();
        });
      },
      handleSee(row) {
        this.reset();
        this.disabled = true;
        const id = row.id || this.ids
        getAssessmentyear(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "年度双项考评结果";
        }).then(() => {
          this.getJoinOrgList();
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
            console.log(orgAssessmentStatus==2)
            if(orgAssessmentStatus==2){
              this.form.performanceAppraisalStatus = 1
            }
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
