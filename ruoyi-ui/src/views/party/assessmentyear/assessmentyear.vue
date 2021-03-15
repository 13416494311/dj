<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="考核名称" prop="assessmentName">
        <el-input
          v-model="queryParams.assessmentName"
          placeholder="请输入考核名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="考核状态" prop="orgAssessmentStatus">-->
<!--        <el-select v-model="queryParams.orgAssessmentStatus" placeholder="请选择考核状态" clearable size="small">-->
<!--          <el-option label="请选择字典生成" value=""/>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
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
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['party:assessmentyear:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['party:assessmentyear:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['party:assessmentyear:export']"
        >导出
        </el-button>
      </el-col>
    </el-row>

    <el-table :stripe="true"
              :border="true" v-loading="loading" :data="assessmentyearList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" align="center" type="index"/>
      <!--      <el-table-column label="id" align="center" prop="id"/>-->
      <!--      <el-table-column label="UUID" align="center" prop="assessmentyearUuid"/>-->
      <el-table-column label="考核年份" align="center" prop="year"/>
      <el-table-column label="考核名称" align="center" prop="assessmentName"/>
      <el-table-column label="考核状态" align="center" prop="orgAssessmentStatus" :formatter="orgAssessmentStatusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:assessmentyear:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
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
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="100px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">考核基本信息</span>
          </div>


          <!--        <el-form-item label="UUID" prop="assessmentyearUuid">-->
          <!--          <el-input v-model="form.assessmentyearUuid" type="textarea" :autosize="{ minRows: 3, maxRows: 6}"-->
          <!--                    placeholder="请输入内容"/>-->
          <!--        </el-form-item>-->
          <el-form-item label="考核年份" prop="year">
            <!--          <el-input v-model="form.year" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入内容"/>-->
            <!--          <el-select v-model="form.recentYear" @change="yearChange" placeholder="请选择" clearable>-->
            <!--            <el-option-->
            <!--              v-for="item in years"-->
            <!--              :key="item.value"-->
            <!--              :label="item.label"-->
            <!--              :value="item.value">-->
            <!--            </el-option>-->
            <!--          </el-select>-->
            <el-date-picker
              v-model="form.year"
              type="year"
              format="yyyy"
              value-format="yyyy"
              placeholder="选择年">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="考核名称" prop="assessmentName">
            <el-input v-model="form.assessmentName" type="textarea" :autosize="{ minRows: 3, maxRows: 6}"
                      placeholder="请输入内容"/>
          </el-form-item>
          <!--        <el-form-item label="考核状态">-->
          <!--          <el-radio-group v-model="form.orgAssessmentStatus">-->
          <!--            <el-radio label="1">请选择字典生成</el-radio>-->
          <!--          </el-radio-group>-->
          <!--        </el-form-item>-->
          <!--        <el-form-item label="删除标志" prop="delFlag">-->
          <!--          <el-input v-model="form.delFlag" placeholder="请输入删除标志"/>-->
          <!--        </el-form-item>-->
        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">计划参与考核党支部</span>
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="openOrgTransfer"
              style="float: right;margin-top: -5px"
            >新增
            </el-button>
          </div>

          <el-table v-loading="orgLoading" :data="assessmentOrgList">
            <el-table-column label="党组织名称" align="center" prop="djPartyOrg.partyOrgFullName"/>
            <el-table-column label="党组织类型" align="center" prop="djPartyOrg.orgType"
                             :formatter="orgTypeFormat"/>
            <el-table-column label="建立时间" align="center" prop="djPartyOrg.buildTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.djPartyOrg.buildTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
<!--            <el-table-column label="党组织简介" align="center" prop="djPartyOrg.description"/>-->
<!--            <el-table-column label="计划状态" align="center" prop="delFlag"-->
<!--                             :formatter="delFlagFormat"/>-->
            <el-table-column v-if="!disabled && this.form.activitySources =='3'" label="操作" align="center"
                             class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  v-if="!cancelDisabled"
                  @click="handleArrangeDelete(scope.row)"
                >删除
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  v-if="cancelDisabled"
                  @click="handleArrangeCancel(scope.row)"
                >取消
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">发 布</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
      <org-transfer ref="orgTransfer" @callbackOrg="getJoinOrgList"/>
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
  import OrgTransfer from "./orgTransfer";
  import {listAssessment} from "../../../api/party/assessment";

  export default {
    name: "Assessmentyear",
    components: {
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
        //党支部考核状态
        orgAssessmentStatusOptions: [],
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
          marginLeft: '10%',
          paddingRight: '10%',
        },

        //uuid
        assessmentyearUuid: undefined,
        year: undefined,
        assessmentName: undefined,
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
      this.getDicts("org_assessment_status").then(response => {
        this.orgAssessmentStatusOptions = response.data;
      });
    },
    methods: {
      orgTypeFormat(row, column) {
        return this.selectDictLabel(this.orgTypeOptions, row.djPartyOrg.orgType);
      },
      orgAssessmentStatusFormat(row, column) {
        return this.selectDictLabel(this.orgAssessmentStatusOptions, row.orgAssessmentStatus);
      },
      //获取是十年内的年份
      // init(){
      //   var myDate = new Date;
      //   var year = myDate.getFullYear();//获取当前年
      //   this.initSelectYear(year)
      //   this.form.recentYear = year;
      // },
      // initSelectYear(year){
      //   this.years = [];
      //   for(let i=0;i<10;i++){
      //     this.years.push({value:(year - i),label:(year - i)+ "年"});
      //   }
      // },
      // yearChange(value){
      //   this.form.recentYear = value
      // },
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
          return delArrange(ids);
        }).then(() => {
          this.getJoinOrgList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      handleArrangeCancel(row) {
        const ids = row.id;
        this.$confirm('是否确认取消该党组织计划活动?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delArrange(ids);
        }).then(() => {
          this.getJoinOrgList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      //增加 选择计划参与党组织
      openOrgTransfer() {
        this.$refs.orgTransfer.open = true;
        this.$refs.orgTransfer.title = "选择计划参与党组织";
        this.$refs.orgTransfer.assessmentyearUuid = this.form.assessmentyearUuid;
        this.$refs.orgTransfer.year = this.form.year;
        this.$refs.orgTransfer.assessmentName = this.form.assessmentName;
        /*if(this.user.djPartyMember){
          this.$refs.orgTransfer.getPartyOrgSelect(this.user.djPartyMember.djPartyOrg.partyOrgId);
        }else{
          this.$refs.orgTransfer.getPartyOrgSelect(1);
        }*/
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
        this.open = true;
        this.title = "添加党组织年度考核";
        this.form.assessmentyearUuid = this.uuid();
        this.getJoinOrgList();
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getAssessmentyear(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改党组织年度考核";
        }).then(() => {
          this.getJoinOrgList();
        });

      },
      /** 提交按钮 */
      submitForm: function () {
        listAssessment(this.queryParams).then(response => {
          this.assessmentList = response.rows;
          this.total = response.total;
          this.loading = false;
        });

        if (this.assessmentOrgList.length == 0) {
          this.msgError("请选择计划参与党组织!");
          return;
        }
        this.$refs["form"].validate(valid => {
          if (valid) {
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
        this.$confirm('是否确认删除党组织考核年编号为"' + ids + '"的数据项?', "警告", {
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
