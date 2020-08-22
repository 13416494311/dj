<template>
  <div>

    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">活动纪要</span>
        <el-button
          v-if="!disabled"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          style="float: right;margin-top: -5px"
        >新增</el-button>
      </div>
      <el-table v-loading="loading" :data="summaryList">
        <el-table-column label="纪要内容" align="left" prop="recordContent" min-width="60%"/>
        <el-table-column label="创建时间" align="center" prop="createTime" min-width="20%">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="20%">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改活动纪要对话框 -->
    <el-dialog :title="title" :visible.sync="open"  append-to-body
               @open="init" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">

        <el-form-item label="纪要内容" prop="recordContent">
          <el-input v-model="form.recordContent" type="textarea" :autosize="{ minRows: 6, maxRows: 6}"
                    placeholder="请输入纪要内容"/>
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
  import {listSummary, getSummary, delSummary, addSummary, updateSummary, exportSummary} from "@/api/activity/summary";

  export default {
    name: "Summary",
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
        // 活动纪要表格数据
        summaryList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          detailedUuid: undefined,
          recordContent: undefined,
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          recordContent: [
            { required: true, message: "纪要内容不能为空", trigger: "blur" }
          ],
        },
        disabled:false,
        detailedUuid:undefined,
      };
    },
    mounted() {
      //window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
    },
    methods: {
      init(detailedUuid){
        this.queryParams.detailedUuid = detailedUuid;
        this.getList();
        this.detailedUuid = detailedUuid;

      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询活动纪要列表 */
      getList() {
        this.loading = true;
        listSummary(this.queryParams).then(response => {
          this.summaryList = response.rows;
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
          summaryId: undefined,
          detailedUuid: undefined,
          recordContent: undefined,
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
        this.ids = selection.map(item => item.summaryId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.form.detailedUuid= this.detailedUuid
        this.open = true;
        this.title = "添加活动纪要";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.form.detailedUuid= this.detailedUuid
        const summaryId = row.summaryId || this.ids
        getSummary(summaryId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改活动纪要";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.summaryId != undefined) {
              updateSummary(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addSummary(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }else {
            setTimeout(() => {
              var isError = document.getElementsByClassName("is-error");
              isError[0].querySelector('input').focus();
            }, 100);
            return false;
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const summaryIds = row.summaryId || this.ids;
        this.$confirm('是否确认删除活动纪要编号为"' + summaryIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delSummary(summaryIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有活动纪要数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportSummary(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
