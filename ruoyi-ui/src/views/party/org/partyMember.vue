<template>
  <el-dialog :title="title" :visible.sync="open" width="70%" append-to-body
             @open="getHeight" :close-on-click-modal="false">
    <div :style="bodyStyle">
      <el-row :gutter="20">
        <el-col :span="12" :xs="24">
          <div class="head-container">
            <el-input
              v-model="partyOrg.partyOrgName"
              placeholder="请输入党组织架构名称"
              clearable
              size="small"
              prefix-icon="el-icon-search"
              style="margin-bottom: 20px"
            />
          </div>
          <div class="head-container">
            <el-tree
              :data="partyOrgOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              ref="tree"
              default-expand-all
              @node-click="handleNodeClick"
            />
          </div>
        </el-col>
        <el-col :span="12" :xs="24">
          <div class="head-container">
            <el-input
              v-model="queryParams.memberName"
              placeholder="请输入党员姓名"
              clearable
              size="small"
              prefix-icon="el-icon-search"
              style="margin-bottom: 20px"
            />
          </div>
          <div class="head-container">
            <el-radio-group v-model="member.memberId">
              <el-radio
                style="width: 100%;margin-bottom: 10px"
                @change="chooseMember(item)"
                v-for="(item,index) in partyMemberList"
                :label="item.memberId" :key="item.memberId">
                {{item.memberName}}
              </el-radio>
            </el-radio-group>

          </div>


        </el-col>
      </el-row>
    </div>

    <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>


<style lang="scss" scoped>

  .head-container {
    overflow: auto;
  }

</style>

<script>
  import { listPartyMember } from "@/api/party/member";
  import { partyOrgTreeselect } from "@/api/party/org";
  import selectTree from '../../components/selectTree';

  export default {
    name: "PartyMember",
    components: {selectTree},
    data() {
      return {
        // 党员信息表格数据
        partyMemberList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 党组织架构树选项
        partyOrgOptions: [],
        // 查询参数
        queryParams: {
          memberName: undefined,
          memberType: undefined,
        },
        defaultProps: {
          value: 'id',          // 唯一标识
          label: 'label',       // 标签显示
          children: 'children', // 子级
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        member:{
          memberId:undefined,
          memberName:undefined,
        },
        partyOrg:{
          partyOrgId:undefined,
          partyOrgName:undefined,
        },

      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    watch: {
      // 根据名称筛选部门树
      'partyOrg.partyOrgName'(val) {
        this.$refs.tree.filter(val);
      },
      //
      'queryParams.memberName'(val) {
        this.getList();
      },

    },
    created() {
      this.getList();
      this.getPartyOrgTreeselect();

    },
    methods: {
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.partyOrgId = data.id;
        this.getList();
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询党组织下拉树结构 */
      getPartyOrgTreeselect() {
        partyOrgTreeselect().then(response => {
          this.partyOrgOptions = this.treeInitData(response.data);
        });
      },
      /** 查询党员信息列表 */
      getList() {
        listPartyMember(this.queryParams).then(response => {
          this.partyMemberList = response.rows;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
      },
      chooseMember(item){
        this.member.memberId = item.memberId
        this.member.memberName = item.memberName
      },
      /** 确定按钮 */
      submitForm: function () {
        this.$emit("callbackMember",this.member)
        this.open = false;
      },

    }
  };
</script>
