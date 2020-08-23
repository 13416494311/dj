<template>
  <div class="app-container">

    <el-dialog  :visible.sync="tableOpen" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <div :style="bodyStyle">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动督办</span>
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              style="float: right;margin-top: -5px"
            >新增
            </el-button>
          </div>
          <el-table v-loading="loading" :data="superviseList">
            <el-table-column label="督办内容" align="left" prop="content" min-width="60%"/>
            <el-table-column label="创建时间" align="center" prop="createTime" min-width="20%"/>
            <el-table-column label="督办人" align="center" prop="djPartyMember.memberName" min-width="20%"/>
          </el-table>
        </el-card>
      </div>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button @click="tableCancel">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 添加或修改活动督办对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body
               :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="督办内容" prop="content">
          <el-input v-model="form.content" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入内容"/>
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
    listSupervise,
    getSupervise,
    delSupervise,
    addSupervise,
    updateSupervise,
    exportSupervise
  } from "@/api/activity/supervise";
  import { getUserProfile } from "@/api/system/user";

  export default {
    name: "Supervise",
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
        // 活动督办表格数据
        superviseList: [],
        tableTitle: "",
        tableOpen: false,
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          detailedUuid: undefined,
          content: undefined,
          partyMemberId: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          content: [
            {required: true, message: "督办内容不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        detailedUuid:undefined,
        user: {},
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getUser();
    },
    methods: {
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data;
        });
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询活动督办列表 */
      getList() {
        this.loading = true;
        listSupervise(this.queryParams).then(response => {
          this.superviseList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      tableCancel(){
        this.tableOpen = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          superviseId: undefined,
          detailedUuid: undefined,
          content: undefined,
          partyMemberId: undefined,
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
        this.ids = selection.map(item => item.superviseId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加活动督办";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const superviseId = row.superviseId || this.ids
        getSupervise(superviseId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改活动督办";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.detailedUuid = this.detailedUuid;
            this.form.partyMemberId = this.user.partyMemberId
            if (this.form.superviseId != undefined) {
              updateSupervise(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addSupervise(this.form).then(response => {
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
        const superviseIds = row.superviseId || this.ids;
        this.$confirm('是否确认删除活动督办编号为"' + superviseIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delSupervise(superviseIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有活动督办数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportSupervise(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
