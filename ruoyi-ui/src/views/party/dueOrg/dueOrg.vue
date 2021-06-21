<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="年度" prop="year">
        <el-date-picker
          v-model="queryParams.duePlan.year"
          type="year"
          format="yyyy"
          value-format="yyyy"
          placeholder="选择年度">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="季度" prop="quarter">
        <el-select v-model="queryParams.duePlan.quarter" placeholder="请选择季度" clearable size="small">
          <el-option
            v-for="dict in quarterOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.duePlan.title"
          placeholder="请输入标题"
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

    <el-table :stripe="true"
              :border="true"
              v-loading="loading" :data="dueOrgList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="标题" align="center" prop="duePlan.title"/>
      <el-table-column label="年度" align="center" prop="duePlan.year" :formatter="yearFormat"/>
      <el-table-column label="季度" align="center" prop="duePlan.quarter" :formatter="quarterFormat"/>
      <el-table-column label="党组织" align="center" prop="partyOrg" :formatter="partyOrgFormat"/>
      <el-table-column label="备注" align="center" prop="duePlan.remark"/>
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
            v-hasPermi="['party:dueOrg:edit']"
          >查看</el-button>
          <el-button
            v-if="scope.row.status != 2"
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:dueOrg:edit']"
          >填报</el-button>
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

    <!-- 添加或修改党组织党费对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight"  :close-on-click-modal="false">

      <div :style="bodyStyle">
        <due-card ref="dueCard" :disabled="disabled" :dueOrgId="dueOrgId" @ok="cancel" ></due-card>
      </div>


      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
        <el-button v-if="!disabled" type="primary" @click="submitDue(1)">保 存</el-button>
        <el-button v-if="!disabled" type="primary" @click="submitDue(2)">提 交</el-button>
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import { pageDueOrg,listDueOrg, getDueOrg, delDueOrg, addDueOrg, updateDueOrg, exportDueOrg } from "@/api/party/dueOrg";
  import DueCard from "../due/dueCard";

  export default {
    name: "DueOrg",
    components: {DueCard},
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
        // 党组织党费表格数据
        dueOrgList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          duePlan:{
            year: undefined,
            quarter: undefined,
            title: undefined,
            status: '2',
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          duePlanUuid: [
            { required: true, message: "党费计划UUID不能为空", trigger: "blur" }
          ],
          partyOrgId: [
            { required: true, message: "党组织id不能为空", trigger: "blur" }
          ],
          status: [
            { required: true, message: "状态不能为空", trigger: "blur" }
          ],
        },
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'2%' ,
          paddingRight:'2%',
        },
        // 季度字典
        quarterOptions: [],
        // 是否提交字典
        statusOptions:[],
        disabled: false,
        dueOrgId:'',
      };
    },
    mounted () {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getDicts("quarter_type").then(response => {
        this.quarterOptions = response.data;
      });
      this.getDicts("submit_status").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      submitDue(statue){
        this.$refs.dueCard.submit(statue);
      },
      // 年度字典翻译
      yearFormat(row, column) {
        return row.duePlan.year + "年";
      },
      // 季度字典翻译
      quarterFormat(row, column) {
        return this.selectDictLabel(this.quarterOptions, row.duePlan.quarter);
      },
      partyOrgFormat(row, column) {
        let partyOrgFullName = row.partyOrg.partyOrgFullName;
        return partyOrgFullName.substring(partyOrgFullName.indexOf("/")+1);
      },
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
      },
      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-281+'px';
      },
      /** 查询党组织党费列表 */
      getList() {
        this.loading = true;
        pageDueOrg(this.queryParams).then(response => {
          this.dueOrgList = response.rows;
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
          dueOrgId: undefined,
          duePlanUuid: undefined,
          partyOrgId: undefined,
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
        this.queryParams = {
            pageNum: 1,
            pageSize: 10,
            duePlan:{
              year: undefined,
              quarter: undefined,
              title: undefined,
              status: '2',
            }
        }
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.dueOrgId)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加党组织党费";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.disabled = false;
        this.dueOrgId = row.dueOrgId
        this.open = true;
        this.title = "党组织党费填报";
      },
      handleSee(row) {
        this.reset();
        this.disabled = true;
        this.dueOrgId = row.dueOrgId
        this.open = true;
        this.title = "党组织党费查看";
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.dueOrgId != undefined) {
              updateDueOrg(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addDueOrg(this.form).then(response => {
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
        const dueOrgIds = row.dueOrgId || this.ids;
        this.$confirm('是否确认删除党组织党费编号为"' + dueOrgIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDueOrg(dueOrgIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党组织党费数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportDueOrg(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
