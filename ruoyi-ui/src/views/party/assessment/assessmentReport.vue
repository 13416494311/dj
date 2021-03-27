<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">

      <el-form-item label="考核年份" prop="assessment_year">
        <el-input
          v-model="queryParams.assessment_year"
          placeholder="请输入考核年份"
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

    <el-table :stripe="true" :border="true" v-loading="loading" :data="assessmentList"
              :header-cell-style="{'text-align':'center'}"
              :default-sort="{prop: 'assessment_year', order: 'ascending'}">
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="党组织" width="300" align="left" prop="djPartyOrg.partyOrgFullName"/>
      <el-table-column label="考核年份" width="80" align="center" prop="assessment_year"/>
      <el-table-column label="考核名称" align="center" prop="assessmentName"/>
      <!--      <el-table-column label="考评等级" align="center" prop="assessmentResult"/>-->
      <el-table-column label="考核状态" align="center" prop="orgAssessmentStatus" :formatter="orgAssessmentStatusFormat"/>
      <!--      <el-table-column label="排名" align="center" prop="assessment_rank"/>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.orgAssessmentStatus =='3'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:assessment:edit']"
          >述职评议
          </el-button>
          <el-button
            v-if="scope.row.orgAssessmentStatus !='3'"
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:assessment:edit']"
          >查看
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
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="50px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">党支部自评</span>
          </div>
          <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="100px">
            <!--            <el-form-item label="考核uuid" prop="assessmentUuid">-->
            <!--              <el-input v-model="form.assessmentUuid" placeholder="请输入考核uuid"/>-->
            <!--            </el-form-item>-->
            <el-form-item label="评审人" prop="assessorName">
              <el-input :disabled="true" v-model="memberName" placeholder="请选择党员">
                <el-button :disabled="disabled" slot="append" icon="el-icon-search"
                           @click="openMemberChoose"></el-button>
              </el-input>
            </el-form-item>
            <el-form-item label="评审人党组织" prop="partyOrgId">
              <el-input v-model="partyOrgName" placeholder="" :disabled="true"/>
            </el-form-item>
            <el-form-item label="评审员类型">
              <el-select v-model="form.assessorType"
                         style="width:100%;"
                         :disabled="disabled"
                         clearable
                         placeholder="选择评审员类型">
                <el-option
                  v-for="dict in assessorTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="评价内容一：" prop="">
              履行党建工作责任情况。基层党组织书记履行抓党建第一责任人职责、推动工作落实、牵头组织实施抓党建工作，调动班子其他成员履行基层党建责任等情况。
            </el-form-item>
            <el-form-item label="表现程度" prop="evaluationContent1">
              <el-select v-model="form.evaluationContent1"
                         style="width:100%;"
                         :disabled="disabled"
                         clearable
                         placeholder="选择特长类别">
                <el-option
                  v-for="dict in performanceLevelOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="评价内容二：" prop="">
              提高基层党组织建设质量情况。落实“三会一课”等基本制度，发展党员和党员教育管理、主题党日等情况；统筹推进基层党组织建设情况。
            </el-form-item>
            <el-form-item label="表现程度" prop="evaluationContent2">
              <el-select v-model="form.evaluationContent2"
                         style="width:100%;"
                         :disabled="disabled"
                         clearable
                         placeholder="选择特长类别">
                <el-option
                  v-for="dict in performanceLevelOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="评价内容三：" prop="">
              强化政治功能，完成重大任务情况。发挥基层党组织战斗堡垒作用和党员先锋模范作用，总结推广基层党建工作典型经验；推进创新性工作，研究新情况、解决新问题，探索加强基层党建工作情况。
            </el-form-item>
            <el-form-item label="表现程度" prop="evaluationContent3">
              <el-select v-model="form.evaluationContent3"
                         style="width:100%;"
                         :disabled="disabled"
                         clearable
                         placeholder="选择特长类别">
                <el-option
                  v-for="dict in performanceLevelOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="评价内容四：" prop="">
              解决突出问题、破解重点难题情况。深入查摆分析基层党组织书记在履行抓基层党建工作责任中存在的差距和不足、基层党建工作中存在的突出问题和薄弱环节。
            </el-form-item>
            <el-form-item label="表现程度" prop="evaluationContent4">
              <el-select v-model="form.evaluationContent4"
                         style="width:100%;"
                         :disabled="disabled"
                         clearable
                         placeholder="选择特长类别">
                <el-option
                  v-for="dict in performanceLevelOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="评价内容五：" prop="">
              述职报告总结成绩是否符合客观实际，查摆问题、原因分析是否真正把自己摆了进去，整改措施是否目标明确可操作。
            </el-form-item>
            <el-form-item label="表现程度" prop="evaluationContent5">
              <el-select v-model="form.evaluationContent5"
                         style="width:100%;"
                         :disabled="disabled"
                         clearable
                         placeholder="选择特长类别">
                <el-option
                  v-for="dict in performanceLevelOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="综合评分" prop="comprehensiveScore">
              <el-input-number v-model="form.comprehensiveScore" @change="handleChange" :min="1"
                               :max="100"></el-input-number>
            </el-form-item>

          </el-form>
        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px"></span>
          </div>
          <el-table :stripe="true" :border="true" v-loading="loading" :data="assessorList">
<!--            <el-table-column type="selection" width="55" align="center"/>-->
            <!--            <el-table-column label="id" align="center" prop="id" />-->
            <!--            <el-table-column label="考核uuid" align="center" prop="assessmentUuid" />-->
            <el-table-column label="序号" align="center" type="index"/>
            <el-table-column label="评审人" align="center" prop="djPartyMember.memberName"/>
            <el-table-column label="评审人党组织" align="center" prop="djPartyOrg.partyOrgName"/>
            <el-table-column label="评审员类型" align="center" prop="assessorType" :formatter="assessorTypeFormat"/>
<!--            <el-table-column label="内容一" align="center" prop="evaluationContent1" :formatter="performanceLevelFormat"/>-->
<!--            <el-table-column label="内容二" align="center" prop="evaluationContent2" :formatter="performanceLevelFormat"/>-->
<!--            <el-table-column label="表现程度" align="center" prop="evaluationContent3" :formatter="performanceLevelFormat"/>-->
<!--            <el-table-column label="表现程度" align="center" prop="evaluationContent4" :formatter="performanceLevelFormat"/>-->
<!--            <el-table-column label="表现程度" align="center" prop="evaluationContent5"/>-->
            <el-table-column label="综合评分" align="center" prop="comprehensiveScore"/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <!--                <el-button-->
                <!--                  size="mini"-->
                <!--                  type="text"-->
                <!--                  icon="el-icon-edit"-->
                <!--                  @click="handleUpdate(scope.row)"-->
                <!--                  v-hasPermi="['party:assessor:edit']"-->
                <!--                >修改</el-button>-->
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['party:assessor:remove']"
                >删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

        </el-card>


      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button  v-if="this.form.orgAssessmentStatus=='3'" type="primary" @click="submitForAssessor">审核结束
        </el-button>
        <el-button  type="primary" @click="submitForm">保 存</el-button>
        <el-button @click="cancel">关 闭</el-button>
      </div>

    </el-dialog>
    <party-member ref="partyMember" @callbackMember="setMember"/>
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
  import {
    listAssessor,
    getAssessor,
    delAssessor,
    addAssessor,
    updateAssessor,
    exportAssessor
  } from "@/api/party/assessor";

  import {partyOrgTreeselect, getPartyOrg} from "@/api/party/org";
  import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
  import partyMember from "../../party/org/partyMemberChoose";
  import {getPartyMember} from "@/api/party/member";

  export default {
    name: "Assessment",
    components: {
      ElImageViewer,
      partyMember
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
        //述职评议表格数据
        assessorList: [],
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
        rules: {
          assessorId: [
            { required: true, message: "评审人不能为空", trigger: "blur" },
          ],
          assessorType: [
            { required: true, message: "评审员类别不能为空", trigger: "blur" },
          ],
          evaluationContent1: [
            { required: true, message: "评价内容一不能为空", trigger: "blur" },
          ],
          evaluationContent2: [
            { required: true, message: "评价内容二不能为空", trigger: "blur" },
          ],
          evaluationContent3: [
            { required: true, message: "评价内容三不能为空", trigger: "blur" },
          ],
          evaluationContent4: [
            { required: true, message: "评价内容四不能为空", trigger: "blur" },
          ],
          evaluationContent5: [
            { required: true, message: "评价内容五不能为空", trigger: "blur" },
          ],
          comprehensiveScore: [
            { required: true, message: "综合评分不能为空", trigger: "blur" },
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '10%',
          paddingRight: '10%',
        },
        //组织架构
        partyOrgOptions: [],
        assessmentScoreListSelections: [],
        memberName: undefined,
        partyOrgName: undefined,

        //表现程度
        performanceLevelOptions: [],
        //评审员类别
        assessorTypeOptions: [],
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {

      this.getList();
      //组织架构树
      this.getPartyOrgTreeSelect();
      //党组织考核状态
      this.getDicts("org_assessment_status").then(response => {
        this.orgAssessmentStatusOptions = response.data;
      });
      // //表现程度
      this.getDicts("performance_level").then(response => {
        this.performanceLevelOptions = response.data;
      });
      //评审员类型
      this.getDicts("assessor_type").then(response => {
        this.assessorTypeOptions = response.data;
      });
    },
    methods: {
      performanceLevelFormat(row, column) {
        return this.selectDictLabel(this.performanceLevelOptions, row.evaluationContent1);
      },
      assessorTypeFormat(row, column) {
        return this.selectDictLabel(this.assessorTypeOptions, row.assessorType);
      },

      /** 查询考核评价评分列表 */
      getAssessmentAssessorList() {
        listAssessor({"assessmentUuid": this.formAssessment.assessmentUuid}).then(response => {
          this.assessorList = response.rows;
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
      }
      ,
      //获取组织架构树
      getPartyOrgTreeSelect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      }
      ,
      //获取党组织考核状态字典值
      orgAssessmentStatusFormat(row, column) {
        return this.selectDictLabel(this.orgAssessmentStatusOptions, row.orgAssessmentStatus);
      }
      ,
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      }
      ,
      /** 查询党组织考核列表 */
      getList() {
        this.loading = true;
        listAssessment(this.queryParams).then(response => {
          this.assessmentList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      }
      ,
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      }
      ,
      // 表单重置
      reset() {
        this.formAssessment = {
          id: undefined,
          assessmentUuid: undefined,
          assessmentyearUuid: undefined,
          partyOrgId: undefined,
          assessment_year: undefined,
          assessmentName: undefined,
          assessmentResult: undefined,
          orgAssessmentStatus: undefined,
          assessment_rank: undefined,
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };
        this.form = {
          id: undefined,
          assessmentUuid: undefined,
          assessorId: undefined,
          partyOrgId: undefined,
          assessorType: undefined,
          evaluationContent1: undefined,
          evaluationContent2: undefined,
          evaluationContent3: undefined,
          evaluationContent4: undefined,
          evaluationContent5: undefined,
          comprehensiveScore: undefined,
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };
        this.resetForm("form");
        orgAssessmentStatus: undefined;
      }
      ,
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      }
      ,
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      }
      ,
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
        this.assessmentScoreListSelections = selection;
      }
      ,
      // 查看按钮操作
      handleSee(row) {
        this.reset()
        const id = row.id || this.ids
        getAssessment(id).then(response => {
          this.formAssessment = response.data;
          this.open = true;
          this.title = "党支部自评";
        }).then(() => {
          this.getAssessmentScoreList();

        });
      }
      ,
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加党组织考核";
      }
      ,
      /** 自评按钮操作 */
      handleUpdate(row) {
        const id = row.id || this.ids
        getAssessment(id).then(response => {
          this.formAssessment = response.data;
          this.open = true;
          this.title = "党支部述职评议";
        }).then(() => {
          this.form.assessmentUuid = this.formAssessment.assessmentUuid;
          this.form.orgAssessmentStatus = this.formAssessment.orgAssessmentStatus;
          this.getAssessmentAssessorList();
        });
      }
      ,

      /** 提交党委评分 */
      submitForAssessor() {
        this.formAssessment.orgAssessmentStatus = "4";
        if (this.formAssessment.assessmentUuid != undefined) {
          updateAssessment(this.formAssessment).then(response => {
            if (response.code === 200) {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
              this.getAssessmentAssessorList();
            } else {
              this.msgError(response.msg);
              this.open = false;
            }
          });
        }
        // this.msgSuccess(this.form.orgAssessmentStatus);
      }
      ,
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          this.msgSuccess("修改成功111");
          if (valid) {
            if (this.form.id != undefined) {
              updateAssessor(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  // this.open = false;
                  this.getList();
                  this.getAssessmentAssessorList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addAssessor(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  // this.open = false;
                  this.getList();
                  this.getAssessmentAssessorList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      }
      ,
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除党组织考核编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delAssessor(ids);
        }).then(() => {
          this.getList();
          this.getAssessmentAssessorList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      }
      ,

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
      },

      //选择党员
      setMember(member) {
        this.form.assessorId = member.memberId;
        this.memberName = member.memberName;
        getPartyMember(member.memberId).then(response => {
          let partyMember = response.data;
          this.partyOrgName = partyMember.djPartyOrg.partyOrgName;
          this.form.partyOrgId = partyMember.partyOrgId;
          this.$forceUpdate();
        });
      },
      openMemberChoose() {
        this.$refs.partyMember.open = true;
        this.$refs.partyMember.title = "选择党员";
      },
    }
  }
  ;
</script>
