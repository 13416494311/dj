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
          style="float: right;margin-top: -5px"
          v-show="!disabled"
        >新增
        </el-button>
      </div>
      <el-table v-loading="loading" :data="partyOrgPostList"
                @selection-change="handleSelectionChange">
        <el-table-column label="党员姓名" align="center" prop="partyMemberId"
                         :formatter="partyMemberIdFormat"/>
        <el-table-column label="职务" align="center" prop="partyPositionType"
                         :formatter="partyPositionTypeFormat"/>
        <el-table-column label="任职时间" align="center" prop="tenureTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.tenureTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="!disabled" label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="small"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['party:orgPost:edit']"
            >修改
            </el-button>
            <el-button
              size="small"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['party:orgPost:remove']"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改党组织职务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="100px">
        <el-form-item label="党员" prop="partyMemberId">
          <el-input :disabled="true" v-model="form.partyMemberName" placeholder="请选择党员">
            <el-button :disabled="disabled" slot="append" icon="el-icon-search"
                       @click="openMemberChoose"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="职务" prop="partyPositionType">
          <el-select :disabled="disabled" v-model="form.partyPositionType"
                     style="width: 100%" placeholder="请选择职务">
            <el-option
              v-for="dict in partyPositionTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="任职时间" prop="tenureTime">
          <el-date-picker :disabled="disabled"
                          clearable size="small"
                          style="width: 100%"
                          v-model="form.tenureTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          :picker-options="afterTimeOption"
                          placeholder="选择任职时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-show="!disabled" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <party-member ref="partyMember" @callbackMember="setMember"/>
  </div>
</template>

<script>
  import {
    listPartyOrgPost,
    getPartyOrgPost,
    delPartyOrgPost,
    addPartyOrgPost,
    updatePartyOrgPost,
    exportPartyOrgPost
  } from "@/api/party/orgPost";
  import treeTransfer from 'el-tree-transfer'
  import PartyMember from "./partyMemberChoose";

  export default {
    name: "PartyOrgPost",
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
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 党组织职务表格数据
        partyOrgPostList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 职务字典
        partyPositionTypeOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          partyMemberId: undefined,
          partyPositionType: undefined,
          tenureTime: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          partyMemberId: [
            {required: true, message: "党员不能为空", trigger: "blur"}
          ],
          partyPositionType: [
            {required: true, message: "职务不能为空", trigger: "blur"}
          ],
          tenureTime: [
            {required: true, message: "任职时间不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '20%',
          paddingRight: '20%',
        },
        afterTimeOption: {
          disabledDate(date) {
            //disabledDate 文档上：设置禁用状态，参数为当前日期，要求返回 Boolean
            return date.getTime() > Date.now();
          }
        },
        partyOrgUuid:'',
        /*transferOpen: false,
        treeTransferTitle: ["待选","已选"],
        mode: "transfer",
        fromData:[],
        toData:[],*/
      };
    },
    watch: {

    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getList();
      this.getDicts("party_position_type").then(response => {
        this.partyPositionTypeOptions = response.data;
      });
    },
    methods: {
      init(val){
        this.partyOrgUuid=val;
        this.getList();
      },
      setMember(member){
        this.form.partyMemberId = member.memberId;
        this.form.partyMemberName = member.memberName;
        this.$forceUpdate();
      },
      openMemberChoose(){
        this.$refs.partyMember.open = true ;
        this.$refs.partyMember.title = "选择党员";
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党组织职务列表 */
      getList() {
        this.loading = true;
        let params = {};
        params.partyOrgUuid = this.partyOrgUuid;
        listPartyOrgPost(params).then(response => {
          this.partyOrgPostList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      partyMemberIdFormat(row, column) {
        return row.djPartyMember.memberName;
      },
      // 职务字典翻译
      partyPositionTypeFormat(row, column) {
        return this.selectDictLabel(this.partyPositionTypeOptions, row.partyPositionType);
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
          partyOrgUuid: undefined,
          partyMemberId: undefined,
          partyMemberName: undefined,
          partyPositionType: undefined,
          tenureTime: undefined,
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
        this.form.partyOrgUuid = this.partyOrgUuid;
        this.open = true;
        this.title = "添加党组织职务";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.form.partyOrgUuid = this.partyOrgUuid;
        const id = row.id || this.ids
        getPartyOrgPost(id).then(response => {
          this.form = response.data;
          this.form.partyMemberName= this.form.djPartyMember.memberName
          this.open = true;
          this.title = "修改党组织职务";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updatePartyOrgPost(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addPartyOrgPost(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          } else {
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
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除党组织职务编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delPartyOrgPost(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有党组织职务数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportPartyOrgPost(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
