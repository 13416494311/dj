<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
<!--      <el-form-item label="assessment_uuid" prop="assessmentUuid">-->
<!--        <el-input-->
<!--          v-model="queryParams.assessmentUuid"-->
<!--          placeholder="请输入assessment_uuid"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="分值" prop="score">
        <el-input
          v-model="queryParams.score"
          placeholder="请输入分值"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="自评分数" prop="selfScore">
        <el-input
          v-model="queryParams.selfScore"
          placeholder="请输入自评分数"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="党委评分" prop="assessorScore">
        <el-input
          v-model="queryParams.assessorScore"
          placeholder="请输入党委评分"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="排序" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入排序"
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
          v-hasPermi="['party:assessmentScore:add']"
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
          v-hasPermi="['party:assessmentScore:edit']"
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
          v-hasPermi="['party:assessmentScore:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['party:assessmentScore:export']"
        >导出
        </el-button>
      </el-col>
    </el-row>

    <el-table :stripe="true" :border="true" v-loading="loading" :data="assessmentScoreList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center"/>-->
<!--      <el-table-column label="id" align="center" prop="id"/>-->
<!--      <el-table-column label="assessment_uuid" align="center" prop="assessmentUuid"/>-->

      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="考核项目" align="center" prop="item"/>
      <el-table-column label="考核内容" align="center" prop="content"/>
      <el-table-column label="考核指标" align="center" prop="quota"/>
      <el-table-column label="分值" align="center" prop="score"/>
      <el-table-column label="评分标准" align="center" prop="criteria"/>
      <el-table-column label="自评分数" align="center" prop="selfScore"/>
      <el-table-column label="党委评分" align="center" prop="assessorScore"/>
      <el-table-column label="排序" align="center" prop="orderNum"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:assessmentScore:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['party:assessmentScore:remove']"
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

    <!-- 添加或修改考核评价评分对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="100px">
        <el-form-item label="assessment_uuid" prop="assessmentUuid">
          <el-input v-model="form.assessmentUuid" placeholder="请输入assessment_uuid"/>
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
          <el-input v-model="form.score" placeholder="请输入分值"/>
        </el-form-item>
        <el-form-item label="评分标准" prop="criteria">
          <el-input v-model="form.criteria" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="自评分数" prop="selfScore">
          <el-input v-model="form.selfScore" placeholder="请输入自评分数"/>
        </el-form-item>
        <el-form-item label="党委评分" prop="assessorScore">
          <el-input v-model="form.assessorScore" placeholder="请输入党委评分"/>
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入排序"/>
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
  import {
    listAssessmentScore,
    getAssessmentScore,
    delAssessmentScore,
    addAssessmentScore,
    updateAssessmentScore,
    exportAssessmentScore
  } from "@/api/party/assessmentScore";

  export default {
    name: "AssessmentScore",
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
          item: undefined,
          content: undefined,
          quota: undefined,
          score: undefined,
          criteria: undefined,
          selfScore: undefined,
          assessorScore: undefined,
          orderNum: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
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
      /** 查询考核评价评分列表 */
      getList() {
        this.loading = true;
        listAssessmentScore(this.queryParams).then(response => {
          this.assessmentScoreList = response.rows;
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
          item: undefined,
          content: undefined,
          quota: undefined,
          score: undefined,
          criteria: undefined,
          selfScore: undefined,
          assessorScore: undefined,
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
        this.ids = selection.map(item => item.id)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加考核评价评分";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getAssessmentScore(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改考核评价评分";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateAssessmentScore(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
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
        this.$confirm('是否确认删除考核评价评分编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delAssessmentScore(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有考核评价评分数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportAssessmentScore(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
