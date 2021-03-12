<template>
  <div>
    <el-row :gutter="60" style="margin-bottom: 30px">
      <el-col :span="8">
        <div class="archives_card">
          <div class="archives_icon icon_org_name">
          </div>
          <div class="archives_label" >
            <span>党组织名称</span>
          </div>
          <div class="archives_content">
            <span>{{ partyOrg.partyOrgFullName}}</span>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="archives_card">
          <div class="archives_icon icon_hall">
          </div>
          <div class="archives_label" >
            <span class="word-wrap">组织类型</span>
          </div>
          <div class="archives_content">
            <span>{{ orgTypeFormat(partyOrg.orgType)}}</span>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="archives_card">
          <div class="archives_icon icon_category">
          </div>
          <div class="archives_label" >
            <span>党组织类别</span>
          </div>
          <div class="archives_content">
            <span>{{ partyOrgTypeFormat(partyOrg.partyOrgType)}}</span>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="60" style="margin-bottom: 30px">
      <el-col :span="8">
        <div class="archives_card">
          <div class="archives_icon icon_flag">
          </div>
          <div class="archives_label" >
            <span class="word-wrap">成立日期</span>
          </div>
          <div class="archives_content">
            <span>{{ parseTime(partyOrg.buildTime, '{y}-{m}-{d}') }}</span>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="archives_card">
          <div class="archives_icon icon_party-change">
          </div>
          <div class="archives_label" >
            <span class="word-wrap">换届日期</span>
          </div>
          <div class="archives_content">
            <span>{{ partyOrg.params.changeTime==null?'':parseTime(partyOrg.params.changeTime, '{y}-{m}-{d}') }}</span>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="archives_card">
          <div class="archives_icon icon_revoke">
          </div>
          <div class="archives_label" >
            <span class="word-wrap">撤销日期</span>
          </div>
          <div class="archives_content">
            <span> </span>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="60" style="margin-bottom: 30px">
      <el-col :span="8">
        <div class="archives_card">
          <div class="archives_icon icon_calendar">
          </div>
          <div class="archives_label" >
            <span class="word-wrap">考核日期</span>
          </div>
          <div class="archives_content">
            <span></span>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="archives_card">
          <div class="archives_icon icon_medal">
          </div>
          <div class="archives_label" >
            <span class="word-wrap">考核总评</span>
          </div>
          <div class="archives_content">
            <span></span>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="archives_card">
          <div class="archives_icon icon_tree">
          </div>
          <div class="archives_label" >
            <span class="word-wrap">党员数量</span>
          </div>
          <div class="archives_content">
            <span>{{Number(partyOrg.params.formalCount)+Number(partyOrg.params.prepareCount)}}</span>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="60" style="margin-bottom: 30px">
      <el-col :span="8">
        <div class="archives_card">
          <div class="archives_icon icon_member">
          </div>
          <div class="archives_label" >
            <span>联系人</span>
          </div>
          <div class="archives_content">
            <span>{{partyOrg.leaderName}}</span>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="archives_card">
          <div class="archives_icon icon_phone">
          </div>
          <div class="archives_label" >
            <span class="word-wrap">联系电话</span>
          </div>
          <div class="archives_content">
            <span>{{partyOrg.phone}}</span>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="60" style="margin-bottom: 30px">
      <el-col :span="16">
        <div class="archives_card">
          <div class="archives_icon icon_location">
          </div>
          <div class="archives_label" >
            <span>党组织位置</span>
          </div>
          <div class="archives_content">
            <span>{{partyOrg.region}}{{partyOrg.address}}</span>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {getPartyOrgArchives} from "@/api/party/org";

  export default {
    name: 'PanelGroupArchive',
    components: {
    },
    props: {
      partyOrgId: {
        required: true,
        type: Number,
      },
    },
    data() {
      return {
        partyOrg:{
          params:{
            formalCount:0,
            prepareCount:0
          }
        },
        // 组织类型字典
        orgTypeOptions: [],
        // 党组织类别字典
        partyOrgTypeOptions: [],
      }
    },
    created(){
      this.getDicts("org_type").then(response => {
        this.orgTypeOptions = response.data;
      });
      this.getDicts("party_org_type").then(response => {
        this.partyOrgTypeOptions = response.data;
      });
    },
    methods: {
      init(){
        this.reset();
        getPartyOrgArchives(this.partyOrgId).then(response => {
          this.partyOrg = response.data;
          if(response.data.leaderMember != undefined){
            this.partyOrg.leaderName = response.data.leaderMember.memberName
          }
        });
      },
      reset(){
        this.partyOrg = {
          params:{
            formalCount:0,
            prepareCount:0
          }
        }
      },
      // 组织类型字典翻译
      orgTypeFormat(orgType) {
        return this.selectDictLabel(this.orgTypeOptions, orgType);
      },
      // 党组织类别字典翻译
      partyOrgTypeFormat(partyOrgType) {
        return this.selectDictLabel(this.partyOrgTypeOptions, partyOrgType);
      },
    }
  }
</script>

<style scoped>
  @import '../../../assets/styles/archives/archives.css';

</style>
