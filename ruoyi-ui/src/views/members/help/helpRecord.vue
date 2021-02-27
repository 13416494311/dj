<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">帮扶记录</span>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="small"
          @click="handleAdd"
          v-hasPermi="['members:helpRecord:add']"
          style="float: right;margin-top: -5px"
          v-show="!disabled"
        >新增
        </el-button>
      </div>
      <el-table v-loading="loading" :data="helpRecordList" @selection-change="handleSelectionChange">
        <el-table-column label="开始时间" align="center" prop="helpStartTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.helpStartTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="截止时间" align="center" prop="helpEndTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.helpEndTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="帮扶形式" align="center" prop="assistance" />
        <el-table-column label="帮扶记录" align="center" width="400" prop="content" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="small"
              type="text"
              icon="el-icon-search"
              @click="handleSee(scope.row)"
            >查看</el-button>
            <el-button
              v-show="!disabled"
              size="small"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['members:helpRecord:edit']"
            >修改</el-button>
            <el-button
              v-show="!disabled"
              size="small"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['members:helpRecord:remove']"
            >删除</el-button>
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

    </el-card>

    <!-- 添加或修改帮扶记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight"  :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="150px">

        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="helpStartTime">
              <el-date-picker clearable size="small" style="width: 100%"
                              :disabled="disabledSon"
                              v-model="form.helpStartTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择开始时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="截止时间" prop="helpEndTime">
              <el-date-picker clearable size="small" style="width: 100%"
                              :disabled="disabledSon"
                              v-model="form.helpEndTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择截止时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="帮扶形式" prop="assistance">
              <el-input v-model="form.assistance" :disabled="disabledSon" placeholder="请输入帮扶形式" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="帮扶记录" prop="content">
              <el-input v-model="form.content" type="textarea":disabled="disabledSon"
                        :autosize="{ minRows: 10, maxRows: 10}" placeholder="请输入帮扶记录内容" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="附件" prop="">
              <upload-all-file ref="uploadAllFile" :disabled="disabledSon"/>
            </el-form-item>
          </el-col>
        </el-row>



      </el-form>
      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
        <el-button v-show="!disabledSon"  type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listHelpRecord, getHelpRecord, delHelpRecord, addHelpRecord, updateHelpRecord, exportHelpRecord } from "@/api/members/helpRecord";
  import UploadAllFile from "../../upload/uploadAllFile";

  export default {
    name: "HelpRecord",
    components: {UploadAllFile},
    props: {
      disabled: {
        type: Boolean,
        default: () => {
          return true
        }
      },
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
        // 帮扶记录表格数据
        helpRecordList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          helpRecordUuid: undefined,
          helpUuid: undefined,
          helpStartTime: undefined,
          helpEndTime: undefined,
          assistance: undefined,
          content: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          helpStartTime: [
            { required: true, message: "开始时间不能为空", trigger: "blur" }
          ],
          helpEndTime: [
            { required: true, message: "截止时间不能为空", trigger: "blur" }
          ],
          assistance: [
            { required: true, message: "帮扶形式不能为空", trigger: "blur" }
          ],
          content: [
            { required: true, message: "帮扶记录不能为空", trigger: "blur" }
          ],
        },
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'2%' ,
          paddingRight:'2%',
        },
        helpUuid:undefined,
        helpRecordUuid:undefined,
        disabledSon:false,
      };
    },
    mounted () {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
    },
    methods: {
      init(helpUuid){
        this.helpUuid = helpUuid;
        this.getList();
      },
      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-330+'px';
      },
      /** 查询帮扶记录列表 */
      getList() {
        this.loading = true;
        this.queryParams.helpUuid = this.helpUuid;
        listHelpRecord(this.queryParams).then(response => {
          this.helpRecordList = response.rows;
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
          helpRecordId: undefined,
          helpRecordUuid: undefined,
          helpUuid: undefined,
          helpStartTime: undefined,
          helpEndTime: undefined,
          assistance: undefined,
          content: undefined,
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
        this.ids = selection.map(item => item.helpRecordId)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.disabledSon = false;
        this.open = true;
        this.form.helpUuid = this.helpUuid;
        this.form.helpRecordUuid = this.uuid()
        this.title = "添加帮扶记录";
        this.$nextTick(function () {
          this.$refs.uploadAllFile.init(this.form.helpRecordUuid, 'helpRecord') ;
        })
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.disabledSon = false;
        const helpRecordId = row.helpRecordId || this.ids
        getHelpRecord(helpRecordId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改帮扶记录";
        }).then(()=>{
          this.$refs.uploadAllFile.init(this.form.helpRecordUuid, 'helpRecord') ;
        });
      },
      handleSee(row) {
        this.reset();
        this.disabledSon = true;
        const helpRecordId = row.helpRecordId || this.ids
        getHelpRecord(helpRecordId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "查看帮扶记录";
        }).then(()=>{
          this.$refs.uploadAllFile.init(this.form.helpRecordUuid, 'helpRecord') ;
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.helpRecordId != undefined) {
              updateHelpRecord(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addHelpRecord(this.form).then(response => {
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
        const helpRecordIds = row.helpRecordId || this.ids;
        this.$confirm('是否确认删除帮扶记录编号为"' + helpRecordIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delHelpRecord(helpRecordIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有帮扶记录数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportHelpRecord(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
