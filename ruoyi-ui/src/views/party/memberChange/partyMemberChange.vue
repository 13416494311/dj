<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">

      <el-form-item label="党员姓名" prop="memberName">
        <el-input
          v-model="queryParams.memberName"
          placeholder="请输入党员姓名"
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

    <el-table v-loading="loading" :data="partyMemberChangeList" @selection-change="handleSelectionChange">
      <el-table-column label="党员姓名" align="center" prop="memberName"/>
      <el-table-column label="党内职务" align="center" prop="partyPositionType" :formatter="partyPositionTypeFormat" />
      <el-table-column label="部门" align="center" prop="deptId" :formatter="deptIdFormat" />
      <el-table-column label="党组织" align="center" prop="partyOrgId" :formatter="partyOrgIdFormat" />
      <el-table-column label="党员类型" align="center" prop="memberType" :formatter="memberTypeFormat"/>
      <el-table-column label="变更类型" align="center" prop="changeType":formatter="changeTypeFormat"/>
      <el-table-column label="审批状态" align="center" prop="auditState" :formatter="auditStateFormat" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
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

    <member-change-detail ref="memberChangeDetail"  />

  </div>
</template>

<script>
  import {
    listPartyMemberChange,
    getPartyMemberChange,
    delPartyMemberChange,
    addPartyMemberChange,
    updatePartyMemberChange,
    exportPartyMemberChange
  } from "@/api/party/memberChange";
  import memberChangeDetail from "./memberChangeDetail";

  export default {
    name: "PartyMemberChange",
    components: {memberChangeDetail},
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
        // 党员变更表表格数据
        partyMemberChangeList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 变更类型字典
        changeTypeOptions: [],
        // 审批状态字典
        auditStateOptions: [],
        // 用户性别字典
        sexOptions: [],
        // 职务字典
        administrativePositionOptions: [],
        // 党内职务字典
        partyPositionTypeOptions: [],
        // 民族字典
        nationOptions: [],
        // 政治面貌字典
        polityOptions: [],
        // 身份字典
        workIdentityOptions: [],
        // 学历字典
        educationOptions: [],
        // 学位字典
        academicDegreeOptions: [],
        // 党员类型字典
        memberTypeOptions: [],
        // 党员状态字典
        memberStatusOptions: [],
        // 流动党员字典
        floatingTypeOptions: [],
        // 党员分组字典
        memberGroupOptions: [],
        // 生活困难字典
        lifeDifficultyOptions: [],
        // 组织认定字典
        cognizanceOptions: [],
        // 经济状况字典
        economicSituationOptions: [],
        // 身体健康情况字典
        physicalHealthOptions: [],
        // 生活困难类型字典
        lifeDifficultyTypeOptions: [],
        // 享受帮扶字典
        enjoyHelpOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          memberUuid: undefined,
          changeType: undefined,
          partyMemberId: undefined,
          auditState: undefined,
          partyOrgId: undefined,
          workNo: undefined,
          avatar: undefined,
          memberName: undefined,
          sex: undefined,
          mobile: undefined,
          identityCard: undefined,
          birthday: undefined,
          companyName: undefined,
          deptId: undefined,
          administrativePosition: undefined,
          title: undefined,
          postId: undefined,
          workingDate: undefined,
          nation: undefined,
          polity: undefined,
          workIdentity: undefined,
          education: undefined,
          academicDegree: undefined,
          nativePlace: undefined,
          homeAddress: undefined,
          housePhone: undefined,
          email: undefined,
          qq: undefined,
          wechat: undefined,
          memberType: undefined,
          memberStatus: undefined,
          joinBranchData: undefined,
          joinData: undefined,
          formalData: undefined,
          floatingType: undefined,
          memberGroup: undefined,
          lifeDifficulty: undefined,
          cognizance: undefined,
          economicSituation: undefined,
          physicalHealth: undefined,
          lifeDifficultyType: undefined,
          enjoyHelp: undefined,
          helpInfo: undefined,
          detail: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          memberUuid: [
            {required: true, message: "用户唯一uuid不能为空", trigger: "blur"}
          ],
          changeType: [
            {required: true, message: "党员变更类型（add：新增 ；edit：修改；del：删除）不能为空", trigger: "blur"}
          ],
          partyMemberId: [
            {required: true, message: "关联党组织成员id不能为空", trigger: "blur"}
          ],
          auditState: [
            {required: true, message: "审批状态不能为空", trigger: "blur"}
          ],
          partyOrgId: [
            {required: true, message: "党组织ID不能为空", trigger: "blur"}
          ],
          workNo: [
            {required: true, message: "工号不能为空", trigger: "blur"}
          ],
          avatar: [
            {required: true, message: "党员照片不能为空", trigger: "blur"}
          ],
          memberName: [
            {required: true, message: "党员姓名不能为空", trigger: "blur"}
          ],
          sex: [
            {required: true, message: "用户性别（0男 1女 2未知）不能为空", trigger: "blur"}
          ],
          mobile: [
            {required: true, message: "手机号不能为空", trigger: "blur"}
          ],
          identityCard: [
            {required: true, message: "身份证号不能为空", trigger: "blur"}
          ],
          birthday: [
            {required: true, message: "出生日期不能为空", trigger: "blur"}
          ],
          companyName: [
            {required: true, message: "所在单位不能为空", trigger: "blur"}
          ],
          deptId: [
            {required: true, message: "部门不能为空", trigger: "blur"}
          ],
          administrativePosition: [
            {required: true, message: "职务不能为空", trigger: "blur"}
          ],
          title: [
            {required: true, message: "职称不能为空", trigger: "blur"}
          ],
          postId: [
            {required: true, message: "岗位不能为空", trigger: "blur"}
          ],
          workingDate: [
            {required: true, message: "参加工作日期不能为空", trigger: "blur"}
          ],
          nation: [
            {required: true, message: "民族不能为空", trigger: "blur"}
          ],
          polity: [
            {required: true, message: "政治面貌不能为空", trigger: "blur"}
          ],
          workIdentity: [
            {required: true, message: "身份不能为空", trigger: "blur"}
          ],
          education: [
            {required: true, message: "学历不能为空", trigger: "blur"}
          ],
          academicDegree: [
            {required: true, message: "学位不能为空", trigger: "blur"}
          ],
          nativePlace: [
            {required: true, message: "籍贯不能为空", trigger: "blur"}
          ],
          homeAddress: [
            {required: true, message: "家庭住址不能为空", trigger: "blur"}
          ],
          housePhone: [
            {required: true, message: "固定电话不能为空", trigger: "blur"}
          ],
          email: [
            {required: true, message: "电子邮箱不能为空", trigger: "blur"}
          ],
          qq: [
            {required: true, message: "qq不能为空", trigger: "blur"}
          ],
          wechat: [
            {required: true, message: "微信号码不能为空", trigger: "blur"}
          ],
          remark: [
            {required: true, message: "备注不能为空", trigger: "blur"}
          ],
          memberType: [
            {required: true, message: "党员类型不能为空", trigger: "blur"}
          ],
          memberStatus: [
            {required: true, message: "党员状态不能为空", trigger: "blur"}
          ],
          joinBranchData: [
            {required: true, message: "加入党支部日期不能为空", trigger: "blur"}
          ],
          joinData: [
            {required: true, message: "加入党日期不能为空", trigger: "blur"}
          ],
          formalData: [
            {required: true, message: "转为正式党员日期不能为空", trigger: "blur"}
          ],
          floatingType: [
            {required: true, message: "流动党员（1：是  0：否）不能为空", trigger: "blur"}
          ],
          memberGroup: [
            {required: true, message: "党员分组不能为空", trigger: "blur"}
          ],
          lifeDifficulty: [
            {required: true, message: "生活困难（1：是  0：否）不能为空", trigger: "blur"}
          ],
          cognizance: [
            {required: true, message: "组织认定（1：是  0：否）不能为空", trigger: "blur"}
          ],
          economicSituation: [
            {required: true, message: "经济状况不能为空", trigger: "blur"}
          ],
          physicalHealth: [
            {required: true, message: "身体健康情况不能为空", trigger: "blur"}
          ],
          lifeDifficultyType: [
            {required: true, message: "生活困难类型不能为空", trigger: "blur"}
          ],
          enjoyHelp: [
            {required: true, message: "享受帮扶不能为空", trigger: "blur"}
          ],
          helpInfo: [
            {required: true, message: "补助情况不能为空", trigger: "blur"}
          ],
          detail: [
            {required: true, message: "具体情况描述不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '20%',
          paddingRight: '20%',
        },
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getDicts("change_type").then(response => {
        this.changeTypeOptions = response.data;
      });
      this.getDicts("audit_state").then(response => {
        this.auditStateOptions = response.data;
      });
      this.getDicts("sys_user_sex").then(response => {
        this.sexOptions = response.data;
      });
      this.getDicts("administrative_position_type").then(response => {
        this.administrativePositionOptions = response.data;
      });
      this.getDicts("party_position_type").then(response => {
        this.partyPositionTypeOptions = response.data;
      });
      this.getDicts("nation_type").then(response => {
        this.nationOptions = response.data;
      });
      this.getDicts("polity_type").then(response => {
        this.polityOptions = response.data;
      });
      this.getDicts("work_identity_type").then(response => {
        this.workIdentityOptions = response.data;
      });
      this.getDicts("education_type").then(response => {
        this.educationOptions = response.data;
      });
      this.getDicts("academic_degree_type").then(response => {
        this.academicDegreeOptions = response.data;
      });
      this.getDicts("party_member_type").then(response => {
        this.memberTypeOptions = response.data;
      });
      this.getDicts("party_member_status").then(response => {
        this.memberStatusOptions = response.data;
      });
      this.getDicts("sys_yes_no").then(response => {
        this.floatingTypeOptions = response.data;
      });
      this.getDicts("party_member_group").then(response => {
        this.memberGroupOptions = response.data;
      });
      this.getDicts("sys_yes_no").then(response => {
        this.lifeDifficultyOptions = response.data;
      });
      this.getDicts("sys_yes_no").then(response => {
        this.cognizanceOptions = response.data;
      });
      this.getDicts("economic_situation_type").then(response => {
        this.economicSituationOptions = response.data;
      });
      this.getDicts("physical_health_type").then(response => {
        this.physicalHealthOptions = response.data;
      });
      this.getDicts("life_difficulty_type").then(response => {
        this.lifeDifficultyTypeOptions = response.data;
      });
      this.getDicts("sys_yes_no").then(response => {
        this.enjoyHelpOptions = response.data;
      });
    },
    methods: {
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党员变更表列表 */
      getList() {
        this.loading = true;
        listPartyMemberChange(this.queryParams).then(response => {
          this.partyMemberChangeList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      auditStateFormat(row, column){
        return this.selectDictLabel(this.auditStateOptions, row.auditState);
      },
      // 变更类型字典翻译
      changeTypeFormat(row, column) {
        return this.selectDictLabel(this.changeTypeOptions, row.changeType);
      },
      // 部门id翻译
      deptIdFormat(row, column,value){
        if(row.sysDept!=null){
          return row.sysDept.deptName;
        }else{
          return "";
        }
      },
      // 党组织id翻译
      partyOrgIdFormat(row, column){
        return row.djPartyOrg.partyOrgFullName;
      },
      // 用户性别字典翻译
      sexFormat(row, column) {
        return this.selectDictLabel(this.sexOptions, row.sex);
      },
      // 职务字典翻译
      administrativePositionFormat(row, column) {
        return this.selectDictLabel(this.administrativePositionOptions, row.administrativePosition);
      },
      // 党内职务字典翻译
      partyPositionTypeFormat(row, column) {
        return this.selectDictLabel(this.partyPositionTypeOptions, row.partyPositionType);
      },
      // 民族字典翻译
      nationFormat(row, column) {
        return this.selectDictLabel(this.nationOptions, row.nation);
      },
      // 政治面貌字典翻译
      polityFormat(row, column) {
        return this.selectDictLabel(this.polityOptions, row.polity);
      },
      // 身份字典翻译
      workIdentityFormat(row, column) {
        return this.selectDictLabel(this.workIdentityOptions, row.workIdentity);
      },
      // 学历字典翻译
      educationFormat(row, column) {
        return this.selectDictLabel(this.educationOptions, row.education);
      },
      // 学位字典翻译
      academicDegreeFormat(row, column) {
        return this.selectDictLabel(this.academicDegreeOptions, row.academicDegree);
      },
      // 党员类型字典翻译
      memberTypeFormat(row, column) {
        return this.selectDictLabel(this.memberTypeOptions, row.memberType);
      },
      // 党员状态字典翻译
      memberStatusFormat(row, column) {
        return this.selectDictLabel(this.memberStatusOptions, row.memberStatus);
      },
      // 流动党员字典翻译
      floatingTypeFormat(row, column) {
        return this.selectDictLabel(this.floatingTypeOptions, row.floatingType);
      },
      // 党员分组字典翻译
      memberGroupFormat(row, column) {
        return this.selectDictLabel(this.memberGroupOptions, row.memberGroup);
      },
      // 生活困难字典翻译
      lifeDifficultyFormat(row, column) {
        return this.selectDictLabel(this.lifeDifficultyOptions, row.lifeDifficulty);
      },
      // 组织认定字典翻译
      cognizanceFormat(row, column) {
        return this.selectDictLabel(this.cognizanceOptions, row.cognizance);
      },
      // 经济状况字典翻译
      economicSituationFormat(row, column) {
        return this.selectDictLabel(this.economicSituationOptions, row.economicSituation);
      },
      // 身体健康情况字典翻译
      physicalHealthFormat(row, column) {
        return this.selectDictLabel(this.physicalHealthOptions, row.physicalHealth);
      },
      // 生活困难类型字典翻译
      lifeDifficultyTypeFormat(row, column) {
        return this.selectDictLabel(this.lifeDifficultyTypeOptions, row.lifeDifficultyType);
      },
      // 享受帮扶字典翻译
      enjoyHelpFormat(row, column) {
        return this.selectDictLabel(this.enjoyHelpOptions, row.enjoyHelp);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          memberId: undefined,
          memberUuid: undefined,
          changeType: undefined,
          partyMemberId: undefined,
          auditState: undefined,
          partyOrgId: undefined,
          workNo: undefined,
          avatar: undefined,
          memberName: undefined,
          sex: undefined,
          mobile: undefined,
          identityCard: undefined,
          birthday: undefined,
          companyName: undefined,
          deptId: undefined,
          administrativePosition: undefined,
          title: undefined,
          postId: undefined,
          workingDate: undefined,
          nation: undefined,
          polity: undefined,
          workIdentity: undefined,
          education: undefined,
          academicDegree: undefined,
          nativePlace: undefined,
          homeAddress: undefined,
          housePhone: undefined,
          email: undefined,
          qq: undefined,
          wechat: undefined,
          remark: undefined,
          memberType: undefined,
          memberStatus: "0",
          joinBranchData: undefined,
          joinData: undefined,
          formalData: undefined,
          floatingType: undefined,
          memberGroup: undefined,
          lifeDifficulty: undefined,
          cognizance: undefined,
          economicSituation: undefined,
          physicalHealth: undefined,
          lifeDifficultyType: undefined,
          enjoyHelp: undefined,
          helpInfo: undefined,
          detail: undefined,
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
        this.ids = selection.map(item => item.memberId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加党员变更表";
      },
      /** 查看按钮操作 */
      handleSee(row){
        this.$refs.memberChangeDetail.handleSee(row);
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const memberId = row.memberId || this.ids
        getPartyMemberChange(memberId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改党员变更表";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.memberId != undefined) {
              updatePartyMemberChange(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addPartyMemberChange(this.form).then(response => {
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
        const memberIds = row.memberId || this.ids;
        this.$confirm('是否确认删除党员变更表编号为"' + memberIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delPartyMemberChange(memberIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党员变更表数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportPartyMemberChange(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
