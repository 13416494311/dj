<template>
  <div style="padding: 30px">
    <el-form ref="form" :model="form" :rules="rules" label-width="150px">
      <el-card shadow="always" style="margin-bottom: 30px;">
        <div slot="header" style="height: 25px">
          <span style="font-weight: bold;font-size: 16px">换届提醒</span>
        </div>

        <div class="remind-content">
          {{ remindContent}}
        </div>
      </el-card>

      <el-card shadow="always" style="margin-bottom: 30px;">
        <div slot="header" style="height: 25px">
          <span style="font-weight: bold;font-size: 16px">上一次换届信息</span>
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item label="党组织" prop="partyOrgId">
              <select-tree :value="form.partyOrgId"
                           :disabled="disabled"
                           :options="partyOrgOptions"
                           vModel="partyOrgId"
                           @selected="setVModelValue"
                           placeholder="请选择党组织"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="换届时间" prop="changeTime">
              <el-date-picker clearable size="small" style="width: 100%"
                              v-model="form.changeTime"
                              :disabled="disabled"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="选择换届时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <change-detail ref="changeDetail" :disabled="disabled" />
      </el-card>


    </el-form>
    <div :style="{textAlign:'center'}">
      <el-button v-if="todoStatus=='0'" type="primary" @click="auditResultOpen">审 核</el-button>
      <el-button @click="cancel">关 闭</el-button>
    </div>
    <audit-result ref="auditResult" @ok="submitAudit"/>
  </div>
</template>

<style scoped>
  .remind-content{
    padding: 20px;
    text-indent:2em;
    font-size:14px;
    line-height: 25px;
    color: black;
  }
</style>

<script>
  import {
    listPartyChange,
    getPartyChange,
    delPartyChange,
    addPartyChange,
    updatePartyChange,
    exportPartyChange
  } from "@/api/party/partyChange";
  import {partyOrgTreeselect} from "@/api/party/org";
  import selectTree from '../../components/selectTree';
  import ChangeDetail from "../../party/partyChange/changeDetail";
  import ChooseAuditUser from "../../audit/chooseAuditUser";
  import {listLog} from "@/api/sys/log";
  import {listTodo, getTodo, delTodo, addTodo, updateTodo, exportTodo} from "@/api/sys/todo";
  import auditResult from "../../audit/auditResult";


  export default {
    name: "PartyChangeTodo",
    components: {ChooseAuditUser,ChangeDetail, selectTree, auditResult },
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
        // 党组织换届表格数据
        partyChangeList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          changeUuid: undefined,
          partyOrgId: undefined,
          changeTime: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          partyOrgId: [
            {required: true, message: "党组织不能为空", trigger: "blur"}
          ],
          changeTime: [
            {required: true, message: "换届时间不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        // 党组织架构树选项
        partyOrgOptions: [],
        disabled: false,
        addFlag:false,
        todoId: undefined,
        todoStatus: undefined,
        partyOrgId: undefined,
        lastPartyChangeId: undefined,
        remindContent: undefined,
        partyCommitteeRemindTemplate: undefined,
        partyBranchRemindTemplate: undefined,
      };
    },
    mounted() {

    },
    created() {
      this.init();

      this. getTreeselect()

      this.getConfigKey("party.committee.change.remind").then(response => {
        this.partyCommitteeRemindTemplate= response.msg;
      });
      this.getConfigKey("party.branch.change.remind").then(response => {
        this.partyBranchRemindTemplate= response.msg;
      });

    },
    watch:{

    },
    methods: {
      init() {
        this.todoId = this.$route.query.todoId;
        getTodo(this.todoId).then(response => {
          if (response.data == undefined) {
            this.$store.dispatch('tagsView/delView', this.$route)
            this.$router.go(-1)
          }
          this.todoStatus = response.data.status
          let params = eval('(' + response.data.urlParams + ')');
          this.partyOrgId = params.partyOrgId;
          this.lastPartyChangeId = params.lastPartyChangeId;

        }).then(() => {
          this.reset();
          this.disabled = true;
          getPartyChange(this.lastPartyChangeId).then(response => {
            this.form = response.data;
          }).then(()=>{
            this.$refs.changeDetail.init(this.form.changeUuid, this.form.partyOrgId) ;
            this.$refs.changeDetail.disabled = this.disabled;
            if(this.form.partyOrg && this.form.partyOrg.orgType == '1'){

              this.remindContent = this.partyCommitteeRemindTemplate
                .replace("{partyOrg.partyOrgFullName}",this.form.partyOrg.partyOrgFullName)
                .replace("{partyChange.changeTime}",this.form.changeTime)
            }else {
              this.remindContent = this.partyBranchRemindTemplate
                .replace("{partyOrg.partyOrgFullName}",this.form.partyOrg.partyOrgFullName)
                .replace("{partyChange.changeTime}",this.form.changeTime)
            }
          });

          //待阅打开时设置为已阅
          if(this.todoStatus =='2'){
            updateTodo({"todoId": this.todoId, "status": "3"})
          }

        });
      },
      //下拉树选择后设置值
      setVModelValue(vModel, val) {
        if(val!=null){
          this.form[vModel] =  val;
          this.$refs.changeDetail.init(this.form.changeUuid,val) ;
        }else{
          this.form[vModel] =  undefined;
        }
      },
      /** 查询部门下拉树结构 */
      getTreeselect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },

      // 取消按钮
      cancel() {
        this.$store.dispatch('tagsView/delView', this.$route)
        this.$router.go(-1)
      },
      // 表单重置
      reset() {
        this.form = {
          changeId: undefined,
          changeUuid: undefined,
          partyOrgId: undefined,
          changeTime: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };
        this.resetForm("form");
      },
      auditResultOpen() {
        this.$refs.auditResult.init();
      },
      submitAudit(form) {
        this.form.params = {}
        this.form.params.state = form.state;
        this.form.params.reason = form.reason;
        this.form.params.sysLogId = this.sysLogId;
        this.form.status = form.state;
        updatePartyChange(this.form).then(response => {
          if (response.code === 200) {
            this.msgSuccess("审批成功");
          } else {
            this.msgError(response.msg);
          }
        }).then(() => {
          updateTodo({"todoId": this.todoId, "status": "1"}).then(response => {
            if (response.code === 200) {
              this.$store.dispatch('tagsView/delView', this.$route)
              this.$router.go(-1)
            } else {
              this.msgError(response.msg);
            }
          });
        });
      }
    }
  };
</script>
