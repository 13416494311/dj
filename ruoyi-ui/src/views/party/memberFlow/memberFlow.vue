<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="党员姓名" prop="flowMemberName">
        <el-input
          v-model="queryParams.flowMemberName"
          placeholder="请输入党员姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流动类型" prop="flowType">
        <el-select v-model="queryParams.flowType" placeholder="请选择流动类型" clearable size="small">
          <el-option
            v-for="dict in flowTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
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
          v-hasPermi="['party:memberFlow:add']"
        >新增
        </el-button>
      </el-col>
    </el-row>

    <el-table :stripe="true"
              :border="true" v-loading="loading" :data="memberFlowList" @selection-change="handleSelectionChange">
      <el-table-column label="姓名党员" align="center" prop="flowMember.memberName"/>
      <el-table-column label="所属组织机构" align="center" prop="flowMember.djPartyOrg.partyOrgFullName"/>
      <el-table-column label="流动类型" align="center" prop="flowType" :formatter="flowTypeFormat"/>
      <el-table-column label="流动原因" align="center" prop="flowReasons"/>
      <el-table-column label="联系方式" align="center" prop="flowContact"/>
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
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
            v-hasPermi="['party:memberFlow:edit']"
          >修改
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

    <!-- 添加或修改党员流动对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="200px">

        <el-row>
          <el-col :span="12">
            <el-form-item label="流动党员" prop="flowMemberId">
              <el-input :disabled="true" v-model="form.flowMemberName" placeholder="请选择流动党员">
                <el-button slot="append" :disabled="disabled " icon="el-icon-search"
                           @click="openMemberChoose"></el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="流动类型" prop="flowType">
              <el-select v-model="form.flowType"
                         style="width: 100%"
                         :disabled="disabled "
                         placeholder="请选择流动类型">
                <el-option
                  v-for="dict in flowTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="流动联系方式" prop="flowContact">
              <el-input :disabled="disabled " v-model="form.flowContact" placeholder="请输入流动联系方式"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="流动原因" prop="flowReasons">
              <el-input :disabled="disabled " v-model="form.flowReasons" type="textarea" :autosize="{ minRows: 3, maxRows: 6}"
                        placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="流动党支部联系人" prop="flowPartyBranchContactPeople">
              <el-input :disabled="disabled "v-model="form.flowPartyBranchContactPeople" placeholder="请输入流动党支部联系人"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="流动党支部联系方式" prop="flowPartyBranchContact">
              <el-input :disabled="disabled "v-model="form.flowPartyBranchContact" placeholder="请输入流动党支部联系方式"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="流出（入）地（单位）" prop="flowCompany">
              <el-input :disabled="disabled "v-model="form.flowCompany" placeholder="请输入流出（入）地（单位）"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="支部联系人" prop="flowCompanyPeople">
              <el-input :disabled="disabled "v-model="form.flowCompanyPeople" placeholder="请输入支部联系人"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支部联系方式" prop="flowCompanyContact">
              <el-input :disabled="disabled "v-model="form.flowCompanyContact" placeholder="请输入支部联系方式"/>
            </el-form-item>
          </el-col>

        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-if="!disabled" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <party-member ref="partyMember" @callbackMember="setMember"/>
  </div>
</template>

<script>
  import {
    listMemberFlow,
    getMemberFlow,
    delMemberFlow,
    addMemberFlow,
    updateMemberFlow,
    exportMemberFlow
  } from "@/api/party/memberFlow";
  import partyMember from "../../party/org/partyMemberChoose";

  export default {
    name: "MemberFlow",
    components: {
      partyMember,
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
        // 党员流动表格数据
        memberFlowList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 流动类型(0:流入 1：流出)字典
        flowTypeOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          flowMemberId: undefined,
          flowMemberName: undefined,
          flowType: undefined,
          flowReasons: undefined,
          flowContact: undefined,
          flowPartyBranchContactPeople: undefined,
          flowPartyBranchContact: undefined,
          flowCompany: undefined,
          flowCompanyPeople: undefined,
          flowCompanyContact: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          flowMemberName: [
            {required: true, message: "流动党员不能为空", trigger: "blur"}
          ],
          flowType: [
            {required: true, message: "流动类型不能为空", trigger: "blur"}
          ],
          flowReasons: [
            {required: true, message: "流动原因不能为空", trigger: "blur"}
          ],
          flowContact: [
            {required: true, message: "流动联系方式不能为空", trigger: "blur"}
          ],

        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '10%',
          paddingRight: '10%',
        },
        disabled: false,
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getDicts("flow_type").then(response => {
        this.flowTypeOptions = response.data;
      });
    },
    methods: {
      setMember(member) {
        this.form.flowMemberId = member.memberId;
        this.form.flowMemberName = member.memberName;
        this.$forceUpdate();
      },
      openMemberChoose() {
        this.$refs.partyMember.open = true;
        this.$refs.partyMember.title = "选择负责人";
        this.$refs.partyMember.queryParams.partyOrgId = this.form.partyOrgId;
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党员流动列表 */
      getList() {
        this.loading = true;
        if(this.queryParams.flowMemberName!=undefined){
          this.queryParams.flowMember={}
          this.queryParams.flowMember.memberName = this.queryParams.flowMemberName
        }
        listMemberFlow(this.queryParams).then(response => {
          this.memberFlowList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 流动类型(0:流入 1：流出)字典翻译
      flowTypeFormat(row, column) {
        return this.selectDictLabel(this.flowTypeOptions, row.flowType);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          flowId: undefined,
          flowMemberId: undefined,
          flowType: undefined,
          flowReasons: undefined,
          flowContact: undefined,
          flowPartyBranchContactPeople: undefined,
          flowPartyBranchContact: undefined,
          flowCompany: undefined,
          flowCompanyPeople: undefined,
          flowCompanyContact: undefined,
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
        this.queryParams ={
          pageNum: 1,
          pageSize: 10,
          flowMemberId: undefined,
          flowMemberName: undefined,
          flowType: undefined,
          flowReasons: undefined,
          flowContact: undefined,
          flowPartyBranchContactPeople: undefined,
          flowPartyBranchContact: undefined,
          flowCompany: undefined,
          flowCompanyPeople: undefined,
          flowCompanyContact: undefined,
        },
          this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.flowId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加党员流动";
      },
      /** 查看按钮操作 */
      handleSee(row) {
        this.reset();
        this.disabled =true;
        const flowId = row.flowId || this.ids
        getMemberFlow(flowId).then(response => {
          this.form = response.data;
          this.form.flowMemberName = this.form.flowMember.memberName
          this.open = true;
          this.title = "查看党员流动";
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.disabled =false;
        const flowId = row.flowId || this.ids
        getMemberFlow(flowId).then(response => {
          this.form = response.data;
          this.form.flowMemberName = this.form.flowMember.memberName
          this.open = true;
          this.title = "修改党员流动";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.flowId != undefined) {
              updateMemberFlow(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addMemberFlow(this.form).then(response => {
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
        const flowIds = row.flowId || this.ids;
        this.$confirm('是否确认删除党员流动编号为"' + flowIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delMemberFlow(flowIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党员流动数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportMemberFlow(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
