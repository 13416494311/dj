<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">活动决议</span>
        <el-button
          v-if="!disabled"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          style="float: right;margin-top: -5px"
        >新增
        </el-button>
      </div>
      <el-table v-loading="loading" :data="resolutionList" >
        <el-table-column label="决议内容" align="left" prop="recordContent"  min-width="50%"/>
        <el-table-column label="完成情况" align="center" prop="resolutionStatus"
                         :formatter="resolutionStatusFormat"  min-width="10%" />
        <el-table-column label="创建时间" align="center" prop="createTime" min-width="20%">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="!disabled" label="操作" align="center" class-name="small-padding fixed-width"min-width="20%" >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['activity:resolution:edit']"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['activity:resolution:remove']"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-card>

      <!-- 添加或修改活动决议对话框 -->
      <el-dialog :title="title" :visible.sync="open"  append-to-body
                 @open="init" :close-on-click-modal="false">
        <el-form ref="form" :model="form" :rules="rules"  label-width="100px">
          <el-form-item label="决议内容" prop="recordContent">
            <el-input v-model="form.recordContent" type="textarea" :autosize="{ minRows: 6, maxRows: 6}"
                      placeholder="请输入内容"/>
          </el-form-item>
          <el-form-item label="完成情况" prop="resolutionStatus" >
            <el-radio-group v-model="form.resolutionStatus">
              <el-radio
                v-for="dict in resolutionStatusOptions"
                :key="dict.dictValue"
                :label="dict.dictValue"
              >{{dict.dictLabel}}
              </el-radio>
            </el-radio-group>
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
    listResolution,
    getResolution,
    delResolution,
    addResolution,
    updateResolution,
    exportResolution
  } from "@/api/activity/resolution";

  export default {
    name: "Resolution",
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
        // 活动决议表格数据
        resolutionList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 完成情况字典
        resolutionStatusOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          detailedUuid: undefined,
          recordContent: undefined,
          resolutionStatus: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          recordContent: [
            { required: true, message: "决议内容不能为空", trigger: "blur" }
          ],
          resolutionStatus: [
            { required: true, message: "完成情况不能为空", trigger: "blur" }
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
      this.getDicts("resolution_status").then(response => {
        this.resolutionStatusOptions = response.data;
      });
    },
    methods: {
      init(detailedUuid){
        this.queryParams.detailedUuid = detailedUuid;
        this.getList();
        this.detailedUuid = detailedUuid;

        //this.getHeight();
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询活动决议列表 */
      getList() {
        this.loading = true;
        listResolution(this.queryParams).then(response => {
          this.resolutionList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 完成情况字典翻译
      resolutionStatusFormat(row, column) {
        return this.selectDictLabel(this.resolutionStatusOptions, row.resolutionStatus);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          resolutionId: undefined,
          detailedUuid: undefined,
          recordContent: undefined,
          resolutionStatus: "1",
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
        this.ids = selection.map(item => item.resolutionId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.form.detailedUuid= this.detailedUuid
        this.open = true;
        this.title = "添加活动决议";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.form.detailedUuid= this.detailedUuid
        const resolutionId = row.resolutionId || this.ids
        getResolution(resolutionId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改活动决议";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.resolutionId != undefined) {
              updateResolution(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addResolution(this.form).then(response => {
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
        const resolutionIds = row.resolutionId || this.ids;
        this.$confirm('是否确认删除活动决议编号为"' + resolutionIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delResolution(resolutionIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有活动决议数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportResolution(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
