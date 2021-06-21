<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="年度" prop="year">
        <el-date-picker
          v-model="queryParams.year"
          type="year"
          format="yyyy"
          value-format="yyyy"
          placeholder="选择年度">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="季度" prop="quarter">
        <el-select v-model="queryParams.quarter" placeholder="请选择季度" clearable size="small">
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
          v-model="queryParams.title"
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['party:duePlan:add']"
        >新增
        </el-button>
      </el-col>
    </el-row>

    <el-table :stripe="true"
              :border="true"
              v-loading="loading"
              :data="duePlanList"
              @selection-change="handleSelectionChange">
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="标题" align="center" prop="title"/>
      <el-table-column label="年度" align="center" prop="year" :formatter="yearFormat"/>
      <el-table-column label="季度" align="center" prop="quarter" :formatter="quarterFormat"/>
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat"/>
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
            v-if="scope.row.status == '1'"
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:duePlan:edit']"
          >修改
          </el-button>
          <el-button
            v-if="scope.row.status == '1'"
            size="small"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['party:duePlan:remove']"
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

    <!-- 添加或修改党费计划对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="150px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">党费管理</span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="年度" prop="year">
                <el-date-picker
                  :disabled="disabled"
                  style="width: 100%"
                  v-model="form.year"
                  type="year"
                  format="yyyy"
                  value-format="yyyy"
                  placeholder="选择年度">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="季度" prop="quarter">
                <el-select
                  :disabled="disabled"
                  v-model="form.quarter" style="width: 100%"placeholder="请选择季度">
                  <el-option
                    v-for="dict in quarterOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="标题" prop="title">
                <el-input :disabled="disabled" v-model="form.title" placeholder="请输入标题"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="备注" prop="remark">
                <el-input :disabled="disabled"
                          v-model="form.remark"
                          type="textarea"
                          :autosize="{ minRows: 3, maxRows: 6}"
                          placeholder="请输入备注"/>
              </el-form-item>
            </el-col>
          </el-row>

        </el-card>

        <due-org-card ref="dueOrgCard" :disabled="disabled" :duePlanUuid="duePlanUuid"></due-org-card>


      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-if="!disabled" type="primary" @click="submitForm(1)">保 存</el-button>
        <el-button v-if="!disabled" type="primary" @click="submitForm(2)">发 布</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {listDuePlan, getDuePlan, delDuePlan, addDuePlan, updateDuePlan, exportDuePlan} from "@/api/party/duePlan";
  import DueOrgCard from "../dueOrg/dueOrgCard";

  export default {
    name: "DuePlan",
    components: {DueOrgCard},
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
        // 党费计划表格数据
        duePlanList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 季度字典
        quarterOptions: [],
        // 是否发布字典
        statusOptions:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          duePlanUuid: undefined,
          year: undefined,
          quarter: undefined,
          title: undefined,
          status: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          year: [
            {required: true, message: "年度不能为空", trigger: "blur"}
          ],
          quarter: [
            {required: true, message: "季度不能为空", trigger: "blur"}
          ],
          title: [
            {required: true, message: "党费标题不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        disabled: false,
        duePlanUuid:"",
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getDicts("quarter_type").then(response => {
        this.quarterOptions = response.data;
      });
      this.getDicts("activity_plan_status").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党费计划列表 */
      getList() {
        this.loading = true;
        listDuePlan(this.queryParams).then(response => {
          this.duePlanList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 年度字典翻译
      yearFormat(row, column) {
        return row.year + "年";
      },
      // 季度字典翻译
      quarterFormat(row, column) {
        return this.selectDictLabel(this.quarterOptions, row.quarter);
      },
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.duePlanUuid = undefined;
        this.form = {
          duePlanId: undefined,
          duePlanUuid: undefined,
          year: undefined,
          quarter: undefined,
          title: undefined,
          remark: undefined,
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
        this.ids = selection.map(item => item.duePlanId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.disabled = false;
        this.form.duePlanUuid = this.uuid();
        this.duePlanUuid = this.form.duePlanUuid;
        this.title = "添加党费管理";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.disabled = false;
        const duePlanId = row.duePlanId || this.ids
        getDuePlan(duePlanId).then(response => {
          this.form = response.data;
          this.form.quarter = this.form.quarter +'';
          this.open = true;
          this.title = "修改党费管理";
        });
      },
      handleSee(row) {
        this.reset();
        this.disabled = true;
        const duePlanId = row.duePlanId || this.ids
        getDuePlan(duePlanId).then(response => {
          this.form = response.data;
          this.form.quarter = this.form.quarter +'';
          this.open = true;
          this.title = "查看党费管理";
        });
      },
      /** 提交按钮 */
      submitForm: function (status) {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.status = status
            if (this.form.duePlanId != undefined) {
              updateDuePlan(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addDuePlan(this.form).then(response => {
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
        const duePlanIds = row.duePlanId || this.ids;
        this.$confirm('是否确认删除该数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delDuePlan(duePlanIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党费计划数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportDuePlan(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
