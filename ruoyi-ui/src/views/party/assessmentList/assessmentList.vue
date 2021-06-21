<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="考核项目" prop="item">
        <el-input
          v-model="queryParams.item"
          placeholder="请输入考核项目"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分值" prop="score">
        <el-input-number
          style="width:100%"
          v-model="queryParams.score"
          controls-position="right"
          placeholder="请输入分值"
          clearable
          size="small"
          :precision="0" :step="1" :min="1"
          @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="是否启用" prop="status">
        <el-select v-model="queryParams.status"
                   placeholder="请选择是否启用"
                   style="width: 100%"
                   clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
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
          v-hasPermi="['party:assessmentList:add']"
        >新增
        </el-button>
      </el-col>

    </el-row>

    <el-table :stripe="true"
              :border="true"
              v-loading="loading"
              :data="assessmentListList"
              @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="考核类型" align="center" prop="type"   width="150"
                       :formatter="typeFormat"/>
      <el-table-column label="考核项目" align="center" prop="item" width="150"/>
      <el-table-column label="考核内容" align="left" prop="content"/>
      <el-table-column label="考核指标" align="left" prop="quota" />
      <el-table-column label="分值" align="center" prop="score" width="55"/>
      <el-table-column label="评分标准" align="left" prop="criteria" />
      <el-table-column label="排序" align="center" prop="orderNum" width="55"/>
      <el-table-column label="是否启用" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="120" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:assessmentList:edit']"
          >修改
          </el-button>
          <el-button
            size="small"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['party:assessmentList:remove']"
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

    <!-- 添加或修改党组织考核评价清单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="100px">
        <el-form-item label="考核类型" prop="type">
          <el-select v-model="form.type"
                     style="width: 100%"
                     placeholder="请选择考核类型">
            <el-option
              v-for="dict in assessmentTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="考核项目" prop="item">
          <el-input v-model="form.item" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="考核内容" prop="content">
          <el-input v-model="form.content" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="考核指标" prop="quota">
          <el-input v-model="form.quota" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="分值" prop="score">
          <el-input-number style="width:100%"
                           v-model="form.score"
                           controls-position="right"
                           placeholder="请输入分值"
                           :precision="0" :step="1" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="评分标准" prop="criteria">
          <el-input v-model="form.criteria" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入排序"/>
        </el-form-item>
        <!--        <el-form-item label="删除标志" prop="delFlag">-->
        <!--          <el-input v-model="form.delFlag" placeholder="请输入删除标志"/>-->
        <!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    addAssessmentList,
    delAssessmentList,
    exportAssessmentList,
    getAssessmentList,
    listAssessmentList,
    updateAssessmentList
  } from "@/api/party/assessmentList";

  export default {
    name: "AssessmentList",
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
        // 党组织考核评价清单表格数据
        assessmentListList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 状态数据字典
        statusOptions: [],
        // 考核类型
        assessmentTypeOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          item: undefined,
          content: undefined,
          quota: undefined,
          score: undefined,
          criteria: undefined,
          status: undefined,
          orderNum: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          item: [
            {required: true, message: "考核项目不能为空", trigger: "blur"}
          ],
          /*content: [
            {required: true, message: "考核内容不能为空", trigger: "blur"}
          ],
          quota: [
            {required: true, message: "考核指标不能为空", trigger: "blur"}
          ],*/
          score: [
            {required: true, message: "分值不能为空", trigger: "blur"}
          ],
          /*criteria: [
            {required: true, message: "评分标准不能为空", trigger: "blur"}
          ],*/
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
      this.getDicts("sys_normal_disable").then(response => {
        this.statusOptions = response.data;
      });
      this.getDicts("assessment_type").then(response => {
        this.assessmentTypeOptions = response.data;
      });
    },
    methods: {
      // 党员活动类型字典翻译
      typeFormat(row, column) {
        return this.selectDictLabel(this.assessmentTypeOptions, row.type);
      },
      //改变启用状态
      handleStatusChange(row) {
        let text = row.status === "0" ? "启用" : "停用";
        this.$confirm('确认要"' + text + '"吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return updateAssessmentList({listId: row.listId, status: row.status});
        }).then(() => {
          this.msgSuccess(text + "成功");
        }).catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党组织考核评价清单列表 */
      getList() {
        this.loading = true;
        listAssessmentList(this.queryParams).then(response => {
          this.assessmentListList = response.rows;
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
          listId: undefined,
          item: undefined,
          content: undefined,
          quota: undefined,
          score: undefined,
          criteria: undefined,
          status: "0",
          orderNum: undefined,
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
        this.ids = selection.map(item => item.listId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加党组织考核评价清单";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const listId = row.listId || this.ids
        getAssessmentList(listId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改党组织考核评价清单";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.listId != undefined) {
              updateAssessmentList(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addAssessmentList(this.form).then(response => {
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
        const listIds = row.listId || this.ids;
        this.$confirm('是否确认删除党组织考核评价清单编号为"' + listIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delAssessmentList(listIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党组织考核评价清单数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportAssessmentList(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
