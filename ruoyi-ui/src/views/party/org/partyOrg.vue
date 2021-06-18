<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="党组织名称" prop="partyOrgName">
        <el-input
          v-model="queryParams.partyOrgName"
          placeholder="请输入党组织名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button>
        <el-button
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
        >重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['party:org:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-view"
          size="mini"
          @click="showPartyOrg"
        >组织架构图
        </el-button>
      </el-col>

    </el-row>

    <el-table
      v-loading="loading"
      :data="partyOrgList"
      row-key="partyOrgId"
      default-expand-all
      :stripe="true"
      :border="true"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="党组织名称" align="left" prop="partyOrgName" width="300"/>
      <el-table-column label="组织类型" align="center" prop="orgType" :formatter="orgTypeFormat"/>
      <!--<el-table-column label="党组织类别" align="center" prop="partyOrgType" :formatter="partyOrgTypeFormat"/>-->
      <el-table-column label="党组织负责人" align="center" prop="partyOrgPost" :formatter="partyPositionFormat"/>
      <el-table-column label="建立时间" align="center" prop="buildTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.buildTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="排序" align="center" prop="orderNum"/>
      <el-table-column label="禁用状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            active-color="#13ce66"
            inactive-color="#ff4949"
            disabled
          ></el-switch>
        </template>
      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
          >查看
          </el-button>

          <el-button
            size="small"
            type="text"
            icon="el-icon-share"
            @click="handleShare(scope.row)"
            handle
          >分享
          </el-button>
          <el-button
            size="small"
            type="text"
            icon="el-icon-document-copy"
            @click="handleArchives(scope.row)"
            v-hasPermi="['party:org:archives']"
          >档案
          </el-button>

          <el-button
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['party:org:edit']"
          >修改
          </el-button>
          <el-button
            v-if="scope.row.parentId != 0"
            size="small"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['party:org:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改党组织架构对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="150px">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">基本信息</span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="上级党组织" prop="parentId">
                <select-tree :value="form.parentId"
                             :disabled="disabled"
                             :options="partyOrgOptions"
                             vModel="parentId"
                             @selected="setVModelValue"
                             placeholder="请选择上级党组织"
                />

                <!--<treeselect :disabled="disabled" v-model="form.parentId" :options="partyOrgOptions"
                            :normalizer="normalizer" placeholder="请选择父党组织id" />-->


              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="党组织名称" prop="partyOrgName">
                <el-input :disabled="disabled" v-model="form.partyOrgName" placeholder="请输入党组织名称"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="组织类型" prop="orgType">
                <el-select :disabled="disabled" v-model="form.orgType" placeholder="请选择组织类型" style="width: 100%">
                  <el-option
                    v-for="dict in orgTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="党组织类别" prop="partyOrgType">
                <el-select :disabled="disabled" v-model="form.partyOrgType" placeholder="请选择党组织类别" style="width: 100%">
                  <el-option
                    v-for="dict in partyOrgTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="建立时间" prop="buildTime">
                <el-date-picker :disabled="disabled" clearable size="small" style="width: 100%"
                                v-model="form.buildTime"
                                type="date"
                                value-format="yyyy-MM-dd"
                                :picker-options="afterTimeOption"
                                placeholder="选择建立时间">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="活动负责人" prop="leader">
                <el-input :disabled="true" v-model="form.leaderName" placeholder="请选择活动负责人">
                  <el-button :disabled="disabled" slot="append" icon="el-icon-search"
                             @click="openMemberChoose"></el-button>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="phone">
                <el-input :disabled="disabled" v-model="form.phone" placeholder="请输入联系电话"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input :disabled="disabled" v-model="form.email" placeholder="请输入邮箱"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="显示顺序" prop="orderNum">
                <el-input-number :disabled="disabled" v-model="form.orderNum" style="width: 100%"
                                 placeholder="请输入显示顺序"
                                 :min="0" :precision="0" controls-position="right"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态">
                <el-radio-group v-model="form.status">
                  <el-radio
                    :disabled="disabled"
                    v-for="dict in statusOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
                  >{{dict.dictLabel}}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="区域" prop="description">
                <el-cascader :disabled="disabled" style="width: 100%"
                             v-model="form.regionCode"
                             ref="region"
                             :options="regionOptions"
                             :props="{ checkStrictly: true }"
                             placeholder="请输入出发地">
                </el-cascader>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="详细地址" prop="description">
                <el-input :disabled="disabled" v-model="form.address" placeholder="请输入详细地址">
                  <el-button :disabled="disabled" slot="append" icon="el-icon-map-location"
                             @click="openMap"></el-button>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="" prop="description" v-if="mapOpen" style="height: 500px">
                <address-map ref="addressMap" @callbackAddress="setAddress"></address-map>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="简介" prop="description">
                <el-input :disabled="disabled" v-model="form.description" type="textarea"
                          placeholder="请输入简介内容"
                          :autosize="{minRows: 4, maxRows: 4}" style="width: 100%"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <party-org-post v-if="!addFlag" ref="partyOrgPost" :disabled="disabled" />

      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button v-show="!disabled" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <party-org-pic ref="partyOrgPic"/>
    <party-member ref="partyMember" @callbackMember="setMember"/>
    <q-rcode  ref="qrcode"></q-rcode>
    <party-org-archives ref="partyOrgArchives"/>
  </div>
</template>

<script>
  import {listPartyOrg, getPartyOrg, delPartyOrg, addPartyOrg,
    updatePartyOrg, exportPartyOrg,partyOrgTreeselect} from "@/api/party/org";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import partyOrgPic from "./partyOrgPic";
  import partyOrgPost from "./partyOrgPost";
  import addressMap from "./addressMap";
  import selectTree from '../../components/selectTree';
  import partyMember from "../../party/org/partyMemberChoose";
  import {listRegion} from "@/api/system/region";
  import QRcode from "../../../components/QRcode/index";
  import partyOrgArchives from "./partyOrgArchives";

  export default {
    name: "PartyOrg",
    components: {QRcode, Treeselect, partyOrgPic,partyOrgArchives,partyOrgPost, addressMap, selectTree,partyMember},
    data() {
      return {
        disabled: false,
        // 遮罩层
        loading: true,
        // 党组织架构表格数据
        partyOrgList: [],
        // 党组织架构树选项
        partyOrgOptions: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 组织类型字典
        orgTypeOptions: [],
        // 党组织类别字典
        partyOrgTypeOptions: [],
        // 党组织状态字典
        statusOptions: [],
        // 查询参数
        queryParams: {
          parentId: undefined,
          ancestors: undefined,
          partyOrgName: undefined,
          type: undefined,
          buildTime: undefined,
          leader: undefined,
          phone: undefined,
          email: undefined,
          description: undefined,
          orderNum: undefined,
          status: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          parentId: [
            {required: true, message: "上级党组织", trigger: "blur"}
          ],
          partyOrgName: [
            {required: true, message: "党组织名称不能为空", trigger: "blur"}
          ],
          orgType: [
            {required: true, message: "组织类型不能为空", trigger: "blur"}
          ],
          partyOrgType: [
            {required: true, message: "党组织类别不能为空", trigger: "blur"}
          ],
          buildTime: [
            {required: true, message: "建立时间不能为空", trigger: "blur"}
          ],
          phone: [
            {
              pattern: /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/,
              message: "请输入正确的手机号码",
              trigger: "blur"
            }
          ],
          email: [
            {
              type: "email",
              message: "请输入正确的邮箱地址",
              trigger: ["blur", "change"]
            }
          ],
          status: [
            {required: true, message: "党组织状态（0正常 1停用）不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        mapOpen: false,
        afterTimeOption: {
          disabledDate(date) {
            //disabledDate 文档上：设置禁用状态，参数为当前日期，要求返回 Boolean
            return date.getTime() > Date.now();
          }
        },
        //区域
        regionOptions: [],
        addFlag:false,
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    watch:{
      'form.partyOrgUuid'(val){
        if(val!= undefined && !this.addFlag){
          this.$nextTick(()=>{
            this.$refs.partyOrgPost.init(val);
          })
        }
      }
    },
    created() {
      this.getList();
      this.getDicts("org_type").then(response => {
        this.orgTypeOptions = response.data;
      });
      this.getDicts("party_org_type").then(response => {
        this.partyOrgTypeOptions = response.data;
      });
      this.getDicts("sys_normal_disable").then(response => {
        this.statusOptions = response.data;
      });
      listRegion().then(response => {
        this.regionOptions = this.cityTreeData(response.data);
      });
    },
    methods: {
      //下拉树选择后设置值
      setVModelValue(vModel, val) {
        if(val!=null){
          this.form[vModel] =  val;
        }else{
          this.form[vModel] =  undefined;
        }
      },
      /**返回地址*/
      setAddress(val) {
        this.form.address = '';
        this.form.address = val;
        this.mapOpen = false;
      },
      /**显示地图 获取地址**/
      openMap() {
        this.mapOpen = !this.mapOpen
        this.$forceUpdate();
      },
      /**显示组织架构图**/
      showPartyOrg() {
        this.$refs.partyOrgPic.init();
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党组织架构列表 */
      getList() {
        this.loading = true;
        listPartyOrg(this.queryParams).then(response => {
          this.partyOrgList = this.handleTree(response.data, "partyOrgId", "parentId");
          this.loading = false;
        });
      },
      /** 转换党组织架构数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children;
        }
        return {
          id: node.partyOrgId,
          label: node.partyOrgName,
          children: node.children
        };
      },
      /** 查询部门下拉树结构 */
      getTreeselect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });

        /*listPartyOrg().then(response => {
          /!*this.partyOrgOptions = [];
          const data = { partyOrgId: 0, partyOrgName: '顶级节点', children: [] };
          data.children = this.handleTree(response.data, "partyOrgId", "parentId");
          this.partyOrgOptions.push(data);*!/
          this.partyOrgOptions = this.handleTree(response.data, "partyOrgId");
        });*/
      },
      // 组织类型字典翻译
      orgTypeFormat(row, column) {
        return this.selectDictLabel(this.orgTypeOptions, row.orgType);
      },
      partyPositionFormat(row, column) {
        var result = "";
        for(let i in row.partyPositionMemberList){
          if(row.partyPositionMemberList[i].partyPositionType =='1'){
            result = row.partyPositionMemberList[i].memberName
          }
        }
        if(result ==""){
          for(let i in row.partyPositionMemberList){
            if(row.partyPositionMemberList[i].partyPositionType =='2'){
              result += row.partyPositionMemberList[i].memberName+" "
            }
          }
        }
        return result;
      },
      // 党组织类别字典翻译
      partyOrgTypeFormat(row, column) {
        return this.selectDictLabel(this.partyOrgTypeOptions, row.partyOrgType);
      },
      // 党组织状态字典翻译
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.mapOpen = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          partyOrgId: undefined,
          partyOrgUuid:undefined,
          parentId: undefined,
          ancestors: undefined,
          partyOrgName: undefined,
          orgType: undefined,
          partyOrgType: undefined,
          buildTime: undefined,
          leader: undefined,
          leaderName: undefined,
          phone: undefined,
          email: undefined,
          regionCode: undefined,
          address: undefined,
          description: undefined,
          orderNum: undefined,
          status: "0",
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };
        this.resetForm("form");

        this.mapOpen = false;
        this.addFlag = false;
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.form.partyOrgUuid = this.uuid();
        this.addFlag = true;
        this.mapOpen = false;
        this.disabled = false;
        this.getTreeselect();
        this.open = true;
        this.title = "添加党组织架构";
      },
      /** 档案按钮操作 */
      handleArchives(row){
        this.$refs.partyOrgArchives.init(row.partyOrgId);
      },
      /** 分享按钮操作 */
      handleShare(row){
        this.$refs.qrcode.init("党组织信息",this.getBasePath()+"/orgInfo/"+row.partyOrgId);
      },
      /** 查看按钮操作 */
      handleSee(row) {
        this.reset();
        this.mapOpen = false;
        this.disabled = true;
        this.getTreeselect();
        if (row != undefined) {
          this.form.parentId = row.partyOrgId;
        }
        getPartyOrg(row.partyOrgId).then(response => {
          this.form = response.data;
          if(response.data.leaderMember != undefined){
            this.form.leaderName = response.data.leaderMember
          }
          if(response.data.regionCode != undefined){
            this.form.regionCode = response.data.regionCode.split("-");
          }
          this.open = true;
          this.title = "查看党组织架构";
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.mapOpen = false;
        this.disabled = false;
        this.getTreeselect();
        if (row != undefined) {
          this.form.parentId = row.partyOrgId;
        }
        getPartyOrg(row.partyOrgId).then(response => {
          this.form = response.data;
          if(response.data.leaderMember != undefined){
            this.form.leaderName = response.data.leaderMember
          }
          if(response.data.regionCode != undefined){
            this.form.regionCode = response.data.regionCode.split("-");
          }
          this.open = true;
          this.title = "修改党组织架构";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if(this.form.regionCode){
              this.form.regionCode = this.form.regionCode.join("-");
            }
            if (this.form.partyOrgId != undefined) {
              updatePartyOrg(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addPartyOrg(this.form).then(response => {
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
        this.$confirm('是否确认删除党组织架构编号为"' + row.partyOrgId + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delPartyOrg(row.partyOrgId);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      openMemberChoose() {
        this.$refs.partyMember.open = true;
        this.$refs.partyMember.title = "选择负责人";
      },
      setMember(member) {
        this.form.leader = member.memberId;
        this.form.leaderName = member.memberName;
        this.$forceUpdate();
      },
    }
  };
</script>
