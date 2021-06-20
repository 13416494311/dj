<template>
  <div>
    <div style="text-align: center;font-size: 24px;padding: 8px;">
      {{duePlanTitle}}
    </div>
    <el-form ref="form" :model="form" :rules="rules" >
      <el-table :stripe="true"
                :border="true"
                :summary-method="getTotal"
                :show-summary="showSum"
                v-loading="loading" :data="assessmentList" @selection-change="handleSelectionChange">
        <el-table-column label="序号" align="center" type="index"/>
        <el-table-column label="党组织名称" align="center" prop="djPartyOrg.partyOrgFullName"/>
        <el-table-column v-for="(item,index) in scoreItemList" :key="index" :label="item" align="center">
          <template slot-scope="scope">

          </template>

        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['party:performanceScore:edit']"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['party:performanceScore:remove']"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>

  </div>
</template>

<script>
  import {
    listPerformanceScore,
    getPerformanceScore,
    delPerformanceScore,
    addPerformanceScore,
    updatePerformanceScore,
    exportPerformanceScore,
    getScoreItem
  } from "@/api/party/performanceScore";
  import {
    listAssessmentyear,
    getAssessmentyear,
    delAssessmentyear,
    addAssessmentyear,
    updateAssessmentyear,
    exportAssessmentyear
  } from "@/api/party/assessmentyear";
 import {
    addAssessment,
    delAssessment,
    exportAssessment,
    getAssessment,
    listAssessment,
    listAssessment1,
    updateAssessment,
  } from "@/api/party/assessment";

  export default {
    name: "PerformanceScoreCard",
    props: {
      disabled: {
        type: Boolean,
        default: () => {
          return false
        }
      },
      assessmentyearId: {
        type: Number,
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
        // 绩效考核评分表格数据
        performanceScoreList: [],
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
          orderNum: undefined,
          score: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          assessmentUuid: [
            {required: true, message: "assessment_uuid不能为空", trigger: "blur"}
          ],
          item: [
            {required: true, message: "考核项目不能为空", trigger: "blur"}
          ],
          orderNum: [
            {required: true, message: "排序不能为空", trigger: "blur"}
          ],
          score: [
            {required: true, message: "分数不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        duePlanTitle: "",
        assessmentList: [],
        showSum: true,
        scoreItemList:[]
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    watch: {
      'assessmentyearId'(val) {
        this.getList();
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getTotal(param) {
        const {columns, data} = param;
        const sums = [];
        columns.forEach((column, index) => {
          if (index === 1) {
            sums[index] = '合计';
            return;
          }
          const values = data.map(item => Number(item[column.property]));
          if (column.property === 'total') {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            if (sums[index] && sums[index] != 0) {
              sums[index] = sums[index].toFixed(2) + ' 元';
            } else {
              sums[index] = '';
            }
          }
        });
        return sums;
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询绩效考核评分列表 */
      getList() {
        this.loading = true;
        getAssessmentyear(this.assessmentyearId).then(response => {
          this.duePlanTitle = response.data.assessmentName;
          getScoreItem(response.data.assessmentyearUuid).then(response => {
            this.scoreItemList = response.data;
            this.loading = false;
          })

          let params={}
          params.assessmentyearUuid = response.data.assessmentyearUuid
          params.djPartyOrg ={
            performanceAppraisal:'Y'
          }
          listAssessment1(params).then(response => {
            this.assessmentList = response.rows;
            this.total = response.total;
            this.loading = false;
          });

        })

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
          orderNum: undefined,
          score: undefined,
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
        this.title = "添加绩效考核评分";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getPerformanceScore(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改绩效考核评分";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updatePerformanceScore(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addPerformanceScore(this.form).then(response => {
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
        this.$confirm('是否确认删除绩效考核评分编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delPerformanceScore(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有绩效考核评分数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportPerformanceScore(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
