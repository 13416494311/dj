<template>
  <div>
    <el-table v-loading="loading"
              :data="todoList"
              :show-header="false"
              @row-dblclick="jumpTodo">
      <el-table-column label="待办标题" align="left" prop="title"
                       min-width="50%"
                       :show-overflow-tooltip='true'>
        <template slot-scope="scope">
          <el-link :underline="false" @click="jumpTodo(scope.row)"> {{scope.row.title}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="待办类型" align="left" prop="type"
                       width="80" :formatter="typeFormat"/>
      <el-table-column label="创建人" align="left"  prop="createUser.userName"   width="80" class-name="small-padding fixed-width">
      </el-table-column>
      <el-table-column label="创建时间" align="left" prop="createTime"  width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
  import {listTodo, getTodo, delTodo, addTodo, updateTodo, exportTodo} from "@/api/sys/todo";

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
          pageSize: 5,
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
      };
    },
    mounted() {

    },
    created() {
      //this.getList();
      this.getDicts("sys_todo_type").then(response => {
        this.typeOptions = response.data;
      });
    },
    methods: {
      init(status) {
        this.queryParams.status = status;
        this.getList()
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
      /** 待办跳转 */
      jumpTodo(row) {
        this.reset();
        const todoId = row.todoId || this.ids
        getTodo(todoId).then(response => {
          this.form = response.data;
          let params = eval('(' + this.form.urlParams + ')');
          params.todoId = this.form.todoId;
          this.$router.push({path: "/"+this.form.urlPath, query: {"todoId": this.form.todoId}})
        });
      },


    }
  };
</script>
