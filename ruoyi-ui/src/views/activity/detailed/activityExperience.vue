<template>
  <div class="app-container">
    <el-table v-loading="loading" :data="experienceList">
      <el-table-column label="标题" align="center" prop="title" min-width="25%"/>
      <el-table-column label="内容" align="left" prop="content"min-width="40%"/>
      <el-table-column label="党员姓名" align="center" prop="djPartyMember.memberName"min-width="10%"/>
      <el-table-column label="附件" align="center" min-width="25%">
        <template slot-scope="scope">
          <div v-for="(item, index) in scope.row.sysFiles" :key="index">
            <el-row>
              <el-button
                size="mini"
                type="text"
                @click="handleDownload(item)"
                style="float:left;"
              >{{item.fileName}}
              </el-button>
              <br/>
            </el-row>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import {
    listExperience,
    getExperience,
    delExperience,
    addExperience,
    updateExperience,
    exportExperience
  } from "@/api/activity/experience";
  import {downLoadZip} from "@/utils/zipdownload";

  export default {
    name: "Experience",
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
        // 心得体会表格数据
        experienceList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          experienceUuid: undefined,
          detailedUuid: undefined,
          title: undefined,
          content: undefined,
          partyMemberId: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          title: [
            {required: true, message: "标题不能为空", trigger: "blur"}
          ],
          content: [
            {required: true, message: "内容不能为空", trigger: "blur"}
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
      /** 查询心得体会列表 */
      getList() {
        this.loading = true;
        this.experienceList = [];
        listExperience(this.queryParams).then(response => {
          this.experienceList = response.rows;
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
          experienceId: undefined,
          experienceUuid: undefined,
          detailedUuid: undefined,
          title: undefined,
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
        this.ids = selection.map(item => item.experienceId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加心得体会";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const experienceId = row.experienceId || this.ids
        getExperience(experienceId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改心得体会";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.experienceId != undefined) {
              updateExperience(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addExperience(this.form).then(response => {
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
        const experienceIds = row.experienceId || this.ids;
        this.$confirm('是否确认删除心得体会编号为"' + experienceIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delExperience(experienceIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有心得体会数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportExperience(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      handleDownload(file) {
        //console.log(file);
        downLoadZip("/system/file/download/" + file.id);

      },
    }
  };
</script>
