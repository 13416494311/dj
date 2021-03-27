<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
<!--      <el-form-item label="考核uuid" prop="assessmentUuid">-->
<!--        <el-input-->
<!--          v-model="queryParams.assessmentUuid"-->
<!--          placeholder="请输入考核uuid"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="assessmentYear_uuid" prop="assessmentyearUuid">-->
<!--        <el-input-->
<!--          v-model="queryParams.assessmentyearUuid"-->
<!--          placeholder="请输入assessmentYearuuid"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="党组织" prop="partyOrgId">-->
<!--        <el-input-->
<!--          v-model="queryParams.partyOrgId"-->
<!--          placeholder="请输入党组织id"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->

      <el-form-item label="考核年份" prop="assessment_year">
        <el-input
          v-model="queryParams.assessment_year"
          placeholder="请输入考核年份"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="考评等级结果 ，1：一级、2：二级、3：三级" prop="assessmentResult">-->
<!--        <el-input-->
<!--          v-model="queryParams.assessmentResult"-->
<!--          placeholder="请输入考评等级结果 ，1：一级、2：二级、3：三级"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="考核状态 0：党支部自评中、1：党委考核中、2考核完毕" prop="orgAssessmentStatus">-->
<!--        <el-select v-model="queryParams.orgAssessmentStatus" placeholder="请选择考核状态 0：党支部自评中、1：党委考核中、2考核完毕" clearable-->
<!--                   size="small">-->
<!--          <el-option label="请选择字典生成" value=""/>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="排名" prop="rank">-->
<!--        <el-input-->
<!--          v-model="queryParams.rank"-->
<!--          placeholder="请输入排名"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

<!--    <el-row :gutter="10" class="mb8">-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['party:assessment:add']"-->
<!--        >新增-->
<!--        </el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['party:assessment:edit']"-->
<!--        >修改-->
<!--        </el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['party:assessment:remove']"-->
<!--        >删除-->
<!--        </el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['party:assessment:export']"-->
<!--        >导出-->
<!--        </el-button>-->
<!--      </el-col>-->
<!--    </el-row>-->

    <el-table :stripe="true" :border="true" v-loading="loading" :data="assessmentList" @selection-change="handleSelectionChange"
              :header-cell-style="{'text-align':'center'}" :default-sort="{prop: 'assessment_year', order: 'ascending'}">
      <el-table-column type="selection" width="55" align="center"/>
<!--      <el-table-column label="id" align="center" prop="id"/>-->
<!--      <el-table-column label="考核uuid" align="center" prop="assessmentUuid"/>-->
<!--      <el-table-column label="assessmentYear_uuid" align="center" prop="assessmentyearUuid"/>-->
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="党组织" width="300" align="left" prop="djPartyOrg.partyOrgFullName"/>
      <el-table-column label="考核年份"  width="80"  align="center" prop="assessment_year"/>
      <el-table-column label="考核名称" align="center" prop="assessmentName"/>
<!--      <el-table-column label="考评等级" align="center" prop="assessmentResult"/>-->
      <el-table-column label="考核状态" align="center" prop="orgAssessmentStatus" :formatter="orgAssessmentStatusFormat"/>
<!--      <el-table-column label="排名" align="center" prop="assessment_rank"/>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:assessment:edit']"
          >自评
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['party:assessment:remove']"
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

    <!-- 添加或修改党组织考核对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
<!--      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="100px">-->
<!--        <el-form-item label="考核uuid" prop="assessmentUuid">-->
<!--          <el-input v-model="form.assessmentUuid" placeholder="请输入考核uuid"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="assessmentYear_uuid" prop="assessmentyearUuid">-->
<!--          <el-input v-model="form.assessmentyearUuid" placeholder="请输入assessmentYear_uuid"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="党组织id" prop="partyOrgId">-->
<!--          <el-input v-model="form.partyOrgId" placeholder="请输入党组织id"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="考核年份" prop="assessment_year">-->
<!--          <el-input v-model="form.assessment_year" placeholder="请输入考核年份"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="考核名称" prop="assessmentName">-->
<!--          <el-input v-model="form.assessmentName" type="textarea" :autosize="{ minRows: 3, maxRows: 6}"-->
<!--                    placeholder="请输入内容"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="考评等级结果 ，1：一级、2：二级、3：三级" prop="assessmentResult">-->
<!--          <el-input v-model="form.assessmentResult" placeholder="请输入考评等级结果 ，1：一级、2：二级、3：三级"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="考核状态 0：党支部自评中、1：党委考核中、2考核完毕">-->
<!--          <el-radio-group v-model="form.orgAssessmentStatus">-->
<!--            <el-radio label="1">请选择字典生成</el-radio>-->
<!--          </el-radio-group>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="排名" prop="assessment_rank">-->
<!--          <el-input v-model="form.assessment_rank" placeholder="请输入排名"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="删除标志" prop="delFlag">-->
<!--          <el-input v-model="form.delFlag" placeholder="请输入删除标志"/>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
      <el-card shadow="always" style="margin-bottom: 30px;">
        <div slot="header" style="height: 25px">
          <span style="font-weight: bold;font-size: 16px">党支部自评</span>
        </div>
      <el-table show-summary :stripe="true" :border="true"  v-loading="loading" :data="assessmentScoreList" @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
<!--        <el-table-column type="selection" width="55" align="center"/>-->
        <!--      <el-table-column label="id" align="center" prop="id"/>-->
        <!--      <el-table-column label="assessment_uuid" align="center" prop="assessmentUuid"/>-->
        <el-table-column label="序号" align="center" type="index" />
        <el-table-column label="考核项目" align="center" prop="item"/>
        <el-table-column label="考核内容" align="left" prop="content"/>
        <el-table-column label="考核指标" align="left" prop="quota"/>
        <el-table-column label="分值" width="50" align="center" prop="score" />
        <el-table-column label="评分标准" align="left" prop="criteria"/>
        <el-table-column label="自评分数" width="80" align="center" prop="selfScore">
        <template slot-scope="scope">
          <el-input v-if="1" v-model="scope.row.selfScore"></el-input>
          <span v-else>{{scope.row.selfScore}}</span>
        </template>
        </el-table-column>
<!--        <el-table-column label="党委评分" align="center" prop="assessorScore"/>-->
<!--        <el-table-column label="排序" align="center" prop="orderNum"/>-->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleDetailedUpdate(scope.row)"
              v-hasPermi="['party:assessmentScore:edit']"
            >保存
            </el-button>
<!--            <el-button-->
<!--              size="mini"-->
<!--              type="text"-->
<!--              icon="el-icon-delete"-->
<!--              @click="handleDeleteScore(scope.row)"-->
<!--              v-hasPermi="['party:assessmentScore:remove']"-->
<!--            >删除-->
<!--            </el-button>-->
          </template>
        </el-table-column>
      </el-table>
        <el-form-item  label="附件">
          <upload-all-file ref="uploadAllFile" :disabled="disabled"/>
        </el-form-item>
      </el-card>

      <el-card shadow="always" style="margin-bottom: 30px;">
        <div slot="header" style="height: 25px">
          <span style="font-weight: bold;font-size: 16px">考核资料</span>
        </div>

        <!--<el-dialog :visible.sync="dialogVisible"
                   append-to-body
                   :close-on-click-modal="false">
          <img width="100%" :src="dialogImageUrl" alt="">
        </el-dialog>-->
        <el-form-item  label="附件">
          <upload-all-file ref="uploadAllFile" :disabled="disabled"/>
        </el-form-item>

      </el-card>

      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">提交党委审核</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listAssessment,
    getAssessment,
    delAssessment,
    addAssessment,
    updateAssessment,
    exportAssessment
  } from "@/api/party/assessment";
  import {
    listAssessmentScore,
    getAssessmentScore,
    delAssessmentScore,
    addAssessmentScore,
    updateAssessmentScore,
    exportAssessmentScore
  } from "@/api/party/assessmentScore";

  import {partyOrgTreeselect, getPartyOrg} from "@/api/party/org";
  import UploadAllFile from "../../upload/uploadAllFile";

  export default {
    name: "Assessment",
    components: {
      UploadAllFile,
    },
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
        // 党组织考核表格数据
        assessmentList: [],
        //党支部考核状态
        orgAssessmentStatusOptions: [],
        // 考核评价评分表格数据
        assessmentScoreList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          assessmentUuid: undefined,
          assessmentyearUuid: undefined,
          partyOrgId: undefined,
          assessment_year: undefined,
          assessmentName: undefined,
          assessmentResult: undefined,
          orgAssessmentStatus: undefined,
          assessment_rank: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '10%',
          paddingRight: '10%',
        },
        //组织架构
        partyOrgOptions: [],
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      //获取评分列表
      this.getAssessmentScoreList()

      //组织架构树
      this.getPartyOrgTreeSelect();
      //党组织考核状态
      this.getDicts("org_assessment_status").then(response => {
        this.orgAssessmentStatusOptions = response.data;
      });
    },
    methods: {
      /** 查询考核评价评分列表 */
      getAssessmentScoreList() {
        this.orgLoading = true;
        listAssessmentScore({"assessmentUuid": this.form.assessmentUuid}).then(response => {
          this.assessmentScoreList = response.rows;
          this.orgLoading = false;
        });
      },
      //下拉树选择后设置值
      setVModelValue(vModel, val) {
        if (val != null) {
          this.queryParams[vModel] = val;
        } else {
          this.queryParams[vModel] = undefined;
        }
        this.handleQuery();
      },
      //获取组织架构树
      getPartyOrgTreeSelect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },
      //获取党组织考核状态字典值
      orgAssessmentStatusFormat(row, column) {
        return this.selectDictLabel(this.orgAssessmentStatusOptions, row.orgAssessmentStatus);
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';},
      /** 查询党组织考核列表 */
      getList() {
        this.loading = true;
        listAssessment(this.queryParams).then(response => {
          this.assessmentList = response.rows;
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
          assessmentUuid: undefined,
          assessmentyearUuid: undefined,
          partyOrgId: undefined,
          assessment_year: undefined,
          assessmentName: undefined,
          assessmentResult: undefined,
          orgAssessmentStatus: "0",
          assessment_rank: undefined,
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
      // 查看按钮操作
      handleSee(row) {
        this.reset();
        this.disabled = true;
        const id = row.id || this.ids
        getAssessment(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "查看活动计划";
        });
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加党组织考核";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getAssessment(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "党支部自评";
        }).then(() => {
          this.getAssessmentScoreList();
        });
      },
      /** 修改分数按钮操作 */
      handleDetailedUpdate(row) {
        updateAssessmentScore(row).then(response => {
          if (response.code === 200) {
            this.msgSuccess("修改成功");
            this.getList();
            this.getAssessmentScoreList();
          } else {
            this.msgError(response.msg);
          }
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          this.msgSuccess(valid);
          if (valid) {
            if (this.form.id != undefined) {
              updateAssessmentScore(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                  this.getAssessmentScoreList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addAssessmentScore(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                  this.getAssessmentScoreList();
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
        this.$confirm('是否确认删除党组织考核编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delAssessment(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 删除按钮操作 */
      handleDeleteScore(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除党组织考核编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delAssessmentScore(ids);
        }).then(() => {
          this.getList();
          this.getAssessmentScoreList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党组织考核数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportAssessment(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
