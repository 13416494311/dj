<template>
  <div>
    <el-card shadow="always" style="margin-bottom: 30px;">
      <div slot="header" style="height: 25px">
        <span style="font-weight: bold;font-size: 16px">党组织职务</span>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="small"
          @click="handleAdd"
          v-hasPermi="['party:changeDetail:add']"
          style="float: right;margin-top: -5px"
          v-show="!disabled"
        >新增
        </el-button>
      </div>
      <el-table v-loading="loading" :data="changeDetailList" >
        <el-table-column label="换届职位" align="center" prop="changeType" :formatter="changeTypeFormat" />
        <el-table-column label="换届前" align="center" prop="preMember.memberName" />
        <el-table-column label="换届后" align="center" prop="curMember.memberName" />
        <el-table-column  v-if="!disabled" label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['party:changeDetail:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['party:changeDetail:remove']"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-card>


    <!-- 添加或修改换届职位详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight"  :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle"  label-width="100px">

        <el-form-item label="换届职位"  prop="changeType">
          <el-select v-model="form.changeType"
                     @change="changeTypeChange"
                     style="width: 100%"
                     placeholder="请选择换届职位">
            <el-option
              v-for="dict in changeTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="换届前" prop="preMemberId">
          <el-input :disabled="true" v-model="form.preMemberName" placeholder="换届前党员" />
        </el-form-item>
        <el-form-item label="换届后" prop="curMemberId">
          <el-input :disabled="true" v-model="form.curMemberName" placeholder="请选择换届后党员">
            <el-button :disabled="disabled" slot="append" icon="el-icon-search"
                       @click="openMemberChoose"></el-button>
          </el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <party-member ref="partyMember" @callbackMember="setMember"/>
  </div>
</template>

<script>
  import {
    addChangeDetail,
    delChangeDetail,
    exportChangeDetail,
    getChangeDetail,
    listChangeDetail,
    updateChangeDetail
  } from "@/api/party/changeDetail";
  import treeTransfer from 'el-tree-transfer'
  import PartyMember from "../org/partyMemberChoose";
  import { listPartyMember } from "@/api/party/member";
  export default {
    name: "ChangeDetail",
    components:{
      PartyMember,
      treeTransfer
    },
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
        loading: false,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 换届职位详情表格数据
        changeDetailList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 换届职位字典
        changeTypeOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          changeUuid: undefined,
          partyOrgId: undefined,
          changeType: undefined,
          preMemberId: undefined,
          curMemberId: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          changeType: [
            { required: true, message: "换届职位不能为空", trigger: "blur" }
          ],
          curMemberId: [
            { required: true, message: "换届后memberid不能为空", trigger: "blur" }
          ],
        },
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'20%' ,
          paddingRight:'20%',
        },
        changeUuid:undefined,
        partyOrgId:undefined,
      };
    },
    mounted () {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getDicts("party_position_type").then(response => {
        this.changeTypeOptions = response.data;
      });
    },
    methods: {
      init(changeUuid,partyOrgId){
        this.changeUuid = changeUuid;
        this.partyOrgId = partyOrgId;
        this.getList();
      },
      changeTypeChange(value){
        for(let i in this.changeDetailList){
          if(this.changeDetailList[i].changeType ==value){
            this.form.changeType = undefined;
            this.msgInfo("您已选择改职位!");
            return;
          }
        }
        let queryParams = {}
        queryParams.partyOrgId =this.form.partyOrgId
        queryParams.partyPositionType = value
        listPartyMember(queryParams).then(response => {
          let partyMemberList = response.rows;
          if(partyMemberList&&partyMemberList.length > 0){
            this.form.preMemberId =  partyMemberList[0].memberId;
            this.form.preMemberName =  partyMemberList[0].memberName;
            this.$forceUpdate();
          }else{
            this.form.preMemberId =  undefined;
            this.form.preMemberName =  undefined;
          }
        });
      },
      setMember(member){
        this.form.curMemberId = member.memberId;
        this.form.curMemberName = member.memberName;
        this.$forceUpdate();
      },
      openMemberChoose(){
        this.$refs.partyMember.open = true ;
        this.$refs.partyMember.title = "选择党员";
      },
      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-281+'px';
      },
      /** 查询换届职位详情列表 */
      getList() {
        this.loading = true;
        this.queryParams.changeUuid = this.changeUuid;
        this.queryParams.partyOrgId = this.partyOrgId;
        listChangeDetail(this.queryParams).then(response => {
          this.changeDetailList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 换届职位字典翻译
      changeTypeFormat(row, column) {
        return this.selectDictLabel(this.changeTypeOptions, row.changeType);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          changeDetailId: undefined,
          changeUuid: this.changeUuid,
          partyOrgId: this.partyOrgId,
          changeType: undefined,
          preMemberId: undefined,
          preMemberName: undefined,
          curMemberId: undefined,
          curMemberName: undefined,
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
        this.ids = selection.map(item => item.changeDetailId)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        if(!this.partyOrgId){
          this.msgInfo("请先选择换届党组织!");
          return ;
        }
        this.reset();
        this.open = true;
        this.title = "添加换届职位详情";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const changeDetailId = row.changeDetailId || this.ids
        getChangeDetail(changeDetailId).then(response => {
          this.form = response.data;
          this.form.preMemberName = this.form.preMember!=undefined?this.form.preMember.memberName:undefined
          this.form.curMemberName = this.form.curMember.memberName
          this.open = true;
          this.title = "修改换届职位详情";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.changeDetailId != undefined) {
              updateChangeDetail(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addChangeDetail(this.form).then(response => {
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
        const changeDetailIds = row.changeDetailId || this.ids;
        this.$confirm('是否确认删除换届职位详情编号为"' + changeDetailIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delChangeDetail(changeDetailIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有换届职位详情数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportChangeDetail(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
