<template>
  <div class="app-container">


    <el-table v-loading="loading" :data="todoList" >
      <el-table-column label="待办类型" align="center" prop="type"
                       min-width="15%" :formatter="typeFormat" />
      <el-table-column label="待办标题" align="center" prop="title"
                       min-width="55%"/>

      <el-table-column label="创建时间" align="center" prop="createTime" min-width="15%">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="15%" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="jumpTodo(scope.row)"
          >办理</el-button>
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

  </div>
</template>

<script>
  import { listTodo, getTodo, delTodo, addTodo, updateTodo, exportTodo } from "@/api/sys/todo";

  export default {
    name: "Todo",
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
        // 待办表格数据
        todoList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 待办类型字典
        typeOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          type: undefined,
          title: undefined,
          urlName: undefined,
          urlPath: undefined,
          urlParams: undefined,
          status: undefined,
          userId: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
        },
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'20%' ,
          paddingRight:'20%',
        },
      };
    },
    mounted () {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getDicts("sys_todo_type").then(response => {
        this.typeOptions = response.data;
      });
    },
    methods: {
      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-281+'px';
      },
      /** 查询待办列表 */
      getList() {
        this.loading = true;
        listTodo(this.queryParams).then(response => {
          this.todoList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 待办类型字典翻译
      typeFormat(row, column) {
        return this.selectDictLabel(this.typeOptions, row.type);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          todoId: undefined,
          type: undefined,
          title: undefined,
          urlName: undefined,
          urlPath: undefined,
          urlParams: undefined,
          status: "0",
          userId: undefined,
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
        this.ids = selection.map(item => item.todoId)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加待办";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const todoId = row.todoId || this.ids
        getTodo(todoId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改待办";
        });
      },
      /** 待办跳转 */
      jumpTodo(row) {
        this.reset();
        const todoId = row.todoId || this.ids
        getTodo(todoId).then(response => {
          this.form = response.data;
          let params = eval('('+this.form.urlParams+')');
          params.todoId=this.form.todoId;
          this.$router.push({ path: this.form.urlPath, query:{"todoId":this.form.todoId}  })
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.todoId != undefined) {
              updateTodo(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addTodo(this.form).then(response => {
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
        const todoIds = row.todoId || this.ids;
        this.$confirm('是否确认删除待办编号为"' + todoIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTodo(todoIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有待办数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTodo(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
