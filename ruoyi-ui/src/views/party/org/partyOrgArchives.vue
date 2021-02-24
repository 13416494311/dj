<template>
  <div class="app-container">
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false" custom-class="archives">

      <div :style="bodyStyle" >

       <panel-group-archive ref="panelGroupArchive" :partyOrgId="partyOrgId"/>

        <el-tabs type="border-card" style="margin-bottom: 20px">
          <el-tab-pane label="支部党员墙">
            <div class="member-pic" v-for="member in memberList" :key="member.memberId">
              <div class="member-avatar">
                <el-image
                  v-if="member.avatar"
                  style="width: 100%;height: 100%"
                  :src="member.avatar"
                  fit="fill"></el-image>
              </div>
              <div  class="member-position" v-if="member.partyPositionType != null">
                {{partyPositionTypeFormat(member.partyPositionType)}}
              </div>
              <div  class="member-position" v-else>
                {{memberTypeFormat(member.memberType)}}
              </div>
              <div  class="member-name">{{member.memberName}}</div>
            </div>
          </el-tab-pane>
        </el-tabs>

        <el-tabs type="border-card">
          <el-tab-pane label="历史换届">
            <party-change :see="true"/>
          </el-tab-pane>
          <el-tab-pane label="政治生日">
            <political-birthday  :see="true"/>
          </el-tab-pane>
          <el-tab-pane label="组织生活">
            <activity-detailed  :see="true"/>
          </el-tab-pane>
        </el-tabs>
      </div>

      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { listPartyMemberNoPage } from "@/api/party/member";
  import PartyChange from "../partyChange/partyChange";
  import PoliticalBirthday from "../politicalBirthday/politicalBirthday";
  import ActivityDetailed from "../../activity/detailed/activityDetailed";
  import PanelGroupArchive from "./PanelGroupArchive";



  export default {
    components: {PanelGroupArchive, PoliticalBirthday, PartyChange,ActivityDetailed},
    data() {
      return {
        title:'党组织档案',
        // 是否显示弹出层
        open: false,
        bodyStyle:{
          overflowY:'auto',
          overflowX:'hidden',
          height: '',
          marginLeft:'2%' ,
          paddingRight:'2%',
        },
        memberList:[],
        // 职务字典
        partyPositionTypeOptions: [],
        // 党员类型字典
        memberTypeOptions: [],
        partyOrgId:undefined,
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getDicts("party_position_type").then(response => {
        this.partyPositionTypeOptions = response.data;
      });
      this.getDicts("party_member_type").then(response => {
        this.memberTypeOptions = response.data;
      });
    },
    watch:{

    },
    methods: {
      init(partyOrgId){
        this.open = true;
        this.partyOrgId = partyOrgId;
        this.getMemberList();

        this.$nextTick(()=>{
          this.$refs.panelGroupArchive.init(partyOrgId)
        });

      },
      getMemberList(){
        let form = {}
        form.partyOrgId = this.partyOrgId
        listPartyMemberNoPage(form).then(response => {
          this.memberList = response.data;
          for(let i in this.memberList){
            this.memberList[i].avatar =
              this.memberList[i].avatar == null ? undefined : process.env.VUE_APP_BASE_API + this.memberList[i].avatar
          }
        });
      },
      partyPositionTypeFormat(partyPositionType) {
        return this.selectDictLabel(this.partyPositionTypeOptions, partyPositionType);
      },
      memberTypeFormat(memberType) {
        return this.selectDictLabel(this.memberTypeOptions, memberType);
      },
      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-281+'px';
      },
      // 取消按钮
      cancel() {
        this.open = false;
      },
    }
  };
</script>
<style type="text/css">

  @import '../../../assets/styles/member-avatar.css';

  .archives>.el-dialog__body{
    background: url("../../../assets/image/archives/archives-bg.jpg") no-repeat !important;
    background-size: 100% 100% !important;
    background-color: #FFFACD !important;
  }

  .el-tabs--border-card>.el-tabs__header .el-tabs__item.is-active{
   background-color: #cf001c !important;
   color: white  !important;
  }
</style>
