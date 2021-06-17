<template>
  <div>
    <div style="text-align: center;font-size: 24px;padding: 8px;">
      {{duePlanTitle}}
    </div>
    <div style="padding: 0px 0px 8px 0px;">
      党组织名称：{{dueOrgName}}
    </div>
    <el-table :stripe="true"
              :border="true"
              :summary-method="getTotal"
              :show-summary="showSum"
              v-loading="loading" :data="dueList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="党员" align="center" prop="memberName"/>
      <el-table-column v-for="(item,index) in monthHead" :key="index" :label="item.label" align="center">
        <el-table-column label="工资" align="center" width="180">
          <template slot-scope="scope">
            <el-input-number style="width:150px"
                             v-model="scope.row.memberDueList[index]['salary']"
                             size="small"
                             @change="((val)=>{changeSalary(val, scope.row,index)})"
                             controls-position="right"
                             :precision="2" :step="1" :min="0" ></el-input-number>
          </template>
        </el-table-column>

        <el-table-column label="比列" align="center">
          <template slot-scope="scope">{{scope.row.memberDueList[index]['ratio']}}</template>
        </el-table-column>

        <el-table-column label="党费" align="center">
          <template slot-scope="scope">{{scope.row.memberDueList[index]['due']}}</template>
        </el-table-column>

      </el-table-column>
      <el-table-column label="合计" align="center" prop="total" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:due:edit']"
          >保存
          </el-button>
        </template>
      </el-table-column>

    </el-table>

    <!-- 添加或修改党员党费对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="100px">
        <el-form-item label="党费计划党组织关联id" prop="dueOrgId">
          <el-input v-model="form.dueOrgId" placeholder="请输入党费计划党组织关联id"/>
        </el-form-item>
        <el-form-item label="党员ID" prop="partyMemberId">
          <el-input v-model="form.partyMemberId" placeholder="请输入党员ID"/>
        </el-form-item>
        <el-form-item label="月份" prop="month">
          <el-input v-model="form.month" placeholder="请输入月份"/>
        </el-form-item>
        <el-form-item label="工资" prop="salary">
          <el-input v-model="form.salary" placeholder="请输入工资"/>
        </el-form-item>
        <el-form-item label="比列" prop="ratio">
          <el-input v-model="form.ratio" placeholder="请输入比列"/>
        </el-form-item>
        <el-form-item label="党费" prop="due">
          <el-input v-model="form.due" placeholder="请输入党费"/>
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
  import {listDueMember,addDue, delDue, exportDue, getDue, listDue, updateDue,updateMemberDue} from "@/api/party/due";
  import { pageDueOrg,listDueOrg, getDueOrg, delDueOrg, addDueOrg, updateDueOrg, exportDueOrg } from "@/api/party/dueOrg";

  export default {
    name: "DueCard",
    props: {
      disabled: {
        type: Boolean,
        default: () => {
          return false
        }
      },
      dueOrgId: {
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
        // 党员党费表格数据
        dueList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          dueOrgId: undefined,
          partyMemberId: undefined,
          month: undefined,
          salary: undefined,
          ratio: undefined,
          due: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          dueOrgId: [
            {required: true, message: "党费计划党组织关联id不能为空", trigger: "blur"}
          ],
          partyMemberId: [
            {required: true, message: "党员ID不能为空", trigger: "blur"}
          ],
          month: [
            {required: true, message: "月份不能为空", trigger: "blur"}
          ],
          salary: [
            {required: true, message: "工资不能为空", trigger: "blur"}
          ],
          ratio: [
            {required: true, message: "比列不能为空", trigger: "blur"}
          ],
          due: [
            {required: true, message: "党费不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '20%',
          paddingRight: '20%',
        },

        monthHead: [],
        dueOrg:{},
        duePlanTitle:"",
        dueOrgName:"",
        showSum:true,
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
    },
    watch:{
      'dueOrgId'(val){
        this.getList();
      }
    },
    methods: {
      refreshTotal(){
        this.showSum=false;
        this.$nextTick().then( ()=> {
            this.showSum=true;
        })
      },
      getTotal(param) {
        const { columns, data } = param;
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
            if(sums[index]&&sums[index]!=0){
              sums[index] = sums[index].toFixed(2)+' 元';
            }else{
              sums[index] = '';
            }
          }
        });
        return sums;
      },
      setMemberTotal(row){
        let total=0;
        for(var i in row.memberDueList){
          if(row.memberDueList[i].due){
            total = total+Number(row.memberDueList[i].due);
          }
        }
        row.total= total
        this.refreshTotal();
        if(total== 0){
          return ""
        }else{
          return total;
        }
      },
      changeSalary(val,row,index){
        row.memberDueList[index]['salary'] = val
        switch (true) {
          case val<= 3000 :
            row.memberDueList[index]['ratio'] = "0.5%";
            row.memberDueList[index]['due'] = (val*0.005).toFixed(0)
            break;
          case val > 3000 && val<= 5000:
            row.memberDueList[index]['ratio'] = "1%";
            row.memberDueList[index]['due'] = (val*0.01).toFixed(0)
            break;
          case val > 5000 && val<= 10000:
            row.memberDueList[index]['ratio'] = "1.5%";
            row.memberDueList[index]['due'] = (val*0.015).toFixed(0)
            break;
          case val > 10000 :
            row.memberDueList[index]['ratio'] = "2%";
            row.memberDueList[index]['due'] = (val*0.02).toFixed(0)
            break;
        }
        this.setMemberTotal(row)

      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党员党费列表 */
      getList() {
        this.loading = true;
        this.queryParams.dueOrgId = this.dueOrgId;
        getDueOrg(this.dueOrgId).then(response => {
          this.duePlanTitle = response.data.duePlan.title;
          let partyOrgFullName = response.data.partyOrg.partyOrgFullName;
          this.dueOrgName = partyOrgFullName.substring(partyOrgFullName.indexOf("/")+1);
          this.createMonthHead(response.data.duePlan.quarter);
          listDueMember(this.queryParams).then(response => {
            this.dueList = response.data;
            for(var i in this.dueList){
              let member = this.dueList[i];
              let total=0
              for(var j in member.memberDueList){
                let memberDue = member.memberDueList[j];
                if(memberDue.salary==null){
                  memberDue.salary = undefined
                }
                total += Number(memberDue.due)
              }
              if( total != 0){
                member.total = total
              }
            }
            this.loading = false;
          });
        });
      },

      //创建月份表头
      createMonthHead(quarter){
        this.monthHead =[
          {
            label: (Number(quarter)-1)*3+1 +'月',
          },
          {
            label: (Number(quarter)-1)*3+2 +'月',
          },
          {
            label: (Number(quarter)-1)*3+3 +'月',
          },
        ];
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          dueMemberId: undefined,
          dueOrgId: undefined,
          partyMemberId: undefined,
          month: undefined,
          salary: undefined,
          ratio: undefined,
          due: undefined,
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
        this.ids = selection.map(item => item.dueMemberId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加党员党费";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        let params = [];
        params.push(row)
        updateMemberDue(params).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存成功");
          }
        })
      },
      submit(status){
        updateMemberDue(this.dueList).then(response => {
          if (response.code === 200) {
            let params = {}
            params.dueOrgId = this.dueOrgId
            params.status = status;
            updateDueOrg(params).then(response => {
              this.msgSuccess("操作成功");
              this.$emit("ok");
            })
          }
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.dueMemberId != undefined) {
              updateDue(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addDue(this.form).then(response => {
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
        const dueMemberIds = row.dueMemberId || this.ids;
        this.$confirm('是否确认删除党员党费编号为"' + dueMemberIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delDue(dueMemberIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党员党费数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportDue(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
