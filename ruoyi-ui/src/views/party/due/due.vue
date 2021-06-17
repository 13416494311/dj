<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="党费计划党组织关联id" prop="dueOrgId">
        <el-input
          v-model="queryParams.dueOrgId"
          placeholder="请输入党费计划党组织关联id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="党员ID" prop="partyMemberId">
        <el-input
          v-model="queryParams.partyMemberId"
          placeholder="请输入党员ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="月份" prop="month">
        <el-input
          v-model="queryParams.month"
          placeholder="请输入月份"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工资" prop="salary">
        <el-input
          v-model="queryParams.salary"
          placeholder="请输入工资"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="比列" prop="ratio">
        <el-input
          v-model="queryParams.ratio"
          placeholder="请输入比列"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="党费" prop="due">
        <el-input
          v-model="queryParams.due"
          placeholder="请输入党费"
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
          v-hasPermi="['party:due:add']"
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
          v-hasPermi="['party:due:edit']"
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
          v-hasPermi="['party:due:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['party:due:export']"
        >导出
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="dueList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="党费" align="center" prop="dueMemberId"/>
      <el-table-column label="党费计划党组织关联id" align="center" prop="dueOrgId"/>
      <el-table-column label="党员ID" align="center" prop="partyMemberId"/>
      <el-table-column label="月份" align="center" prop="month"/>
      <el-table-column label="工资" align="center" prop="salary"/>
      <el-table-column label="比列" align="center" prop="ratio"/>
      <el-table-column label="党费" align="center" prop="due"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:due:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['party:due:remove']"
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

    <!-- 添加或修改党员党费对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="100px">
        <el-form-item label="党费计划党组织关联id" prop="dueOrgId">
          <el-input v-model="form.dueOrgId" placeholder="请输入党费计划党组织关联id"/>
        </el-form-item>
        <el-form-item label="党员ID" prop="partyMemberId">
          <el-input v-model="form.partyMemberId" placeholder="请输入党员ID"/>
        </el-form-item>
        <el-form-item label="月份" prop="month">
          <el-input v-model="form.month" placeholder="请输入月份"/>
        </el-form-item>
        <el-form-item label="工资" prop="salary">
          <el-input v-model="form.salary" placeholder="请输入工资"/>
        </el-form-item>
        <el-form-item label="比列" prop="ratio">
          <el-input v-model="form.ratio" placeholder="请输入比列"/>
        </el-form-item>
        <el-form-item label="党费" prop="due">
          <el-input v-model="form.due" placeholder="请输入党费"/>
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {addDue, delDue, exportDue, getDue, listDue, updateDue} from "@/api/party/due";

  export default {
    name: "Due",
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
        // 党员党费表格数据
        dueList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          dueOrgId: undefined,
          partyMemberId: undefined,
          month: undefined,
          salary: undefined,
          ratio: undefined,
          due: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          dueOrgId: [
            {required: true, message: "党费计划党组织关联id不能为空", trigger: "blur"}
          ],
          partyMemberId: [
            {required: true, message: "党员ID不能为空", trigger: "blur"}
          ],
          month: [
            {required: true, message: "月份不能为空", trigger: "blur"}
          ],
          salary: [
            {required: true, message: "工资不能为空", trigger: "blur"}
          ],
          ratio: [
            {required: true, message: "比列不能为空", trigger: "blur"}
          ],
          due: [
            {required: true, message: "党费不能为空", trigger: "blur"}
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
    },
    methods: {
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党员党费列表 */
      getList() {
        this.loading = true;
        listDue(this.queryParams).then(response => {
          this.dueList = response.rows;
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
          dueMemberId: undefined,
          dueOrgId: undefined,
          partyMemberId: undefined,
          month: undefined,
          salary: undefined,
          ratio: undefined,
          due: undefined,
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
        this.ids = selection.map(item => item.dueMemberId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加党员党费";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const dueMemberId = row.dueMemberId || this.ids
        getDue(dueMemberId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改党员党费";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.dueMemberId != undefined) {
              updateDue(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addDue(this.form).then(response => {
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
        const dueMemberIds = row.dueMemberId || this.ids;
        this.$confirm('是否确认删除党员党费编号为"' + dueMemberIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delDue(dueMemberIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党员党费数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportDue(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
