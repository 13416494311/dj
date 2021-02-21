<template>
  <div class="app-container org-info-bg">
    <!--<el-row>
      <el-col :span="24" class="level-center">
        <el-image :src="djPng"></el-image>
      </el-col>
    </el-row>-->
    <el-row style="margin-top: 4.5em">
      <el-col :span="8" class="org-info-lable">
        党组织名称：
      </el-col>
      <el-col :span="16" class="org-info-content">
        {{partyOrg.partyOrgFullName}}
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8"  class="org-info-lable">
        成立时间：
      </el-col>
      <el-col :span="16" class="org-info-content">
        {{ parseTime(partyOrg.buildTime, '{y}-{m}-{d}') }}
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8"  class="org-info-lable">
        换届时间：
      </el-col>
      <el-col :span="16" class="org-info-content">
        {{ partyOrg.params.changeTime==null?'':parseTime(partyOrg.params.changeTime, '{y}-{m}-{d}') }}
      </el-col>
    </el-row>
    <el-row >
      <el-col :span="8"  class="org-info-lable">
        党员简介：
      </el-col>
      <el-col :span="16" class="org-info-content">
        正式党员{{partyOrg.params.formalCount}}人，预备党员{{partyOrg.params.prepareCount}}人。
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px">
      <el-col :span="24" class="level-center">
        <TreeChart class="tree-chart" :json="treeData" />
      </el-col>
    </el-row>


  </div>
</template>

<script>
  import djPng from '@/assets/image/dj.png'
  import defaultAvatar from '@/assets/image/dj-avatar.png'
  import TreeChart from "vue-tree-chart";
  import { getPartyOrgInfo } from "@/api/party/org";

  export default {
    name: "OrgInfo",
    components: {
      TreeChart
    },
    data() {
      return {
        djPng,
        defaultAvatar,
        treeData: {},
        partyOrg:{
          params:{
            formalCount:0,
            prepareCount:0
          }
        },

      };
    },
    computed:{

    },
    mounted() {

    },
    created() {
      this.getOrgInfo();
    },
    methods: {
      getOrgInfo(){
        let partyOrgId = this.$route.path.replace('/orgInfo/','');
        getPartyOrgInfo(partyOrgId).then(response => {
          this.partyOrg = response.data;
          console.log(response.data)
          this.createTreeData(response.data.partyPositionMemberList);
        });
      },
      createTreeData(partyPositionMemberList){
        let data = {
          name: '书记',
          image_url: defaultAvatar,
          children: [
            {
              name: '副书记',
              image_url: defaultAvatar,
              children: [
                {
                  name: '组织委员',
                  image_url: defaultAvatar
                },
                {
                  name: '宣传委员',
                  image_url: defaultAvatar
                },
                {
                  name: '纪检委员',
                  image_url: defaultAvatar
                }
              ]
            }
          ]
        }
        for(let i in partyPositionMemberList){

          let member = partyPositionMemberList[i]

          if(member.partyPositionType =='1'){
            data.name = member.partyPositionTypeText+":"+member.memberName;
            if(member.avatar != null){
              data.image_url = process.env.VUE_APP_BASE_API + member.avatar
            }
          }

          if(member.partyPositionType =='2'){
            data.children[0].name = member.partyPositionTypeText+":"+member.memberName;
            if(member.avatar != null){
              data.children[0].image_url = process.env.VUE_APP_BASE_API + member.avatar
            }
          }

          if(member.partyPositionType =='3'){
            data.children[0].children[0].name = member.partyPositionTypeText+":"+member.memberName;
            if(member.avatar != null){
              data.children[0].children[0].image_url = process.env.VUE_APP_BASE_API + member.avatar
            }
          }

          if(member.partyPositionType =='4'){
            data.children[0].children[1].name = member.partyPositionTypeText+":"+member.memberName;
            if(member.avatar != null){
              data.children[0].children[1].image_url = process.env.VUE_APP_BASE_API + member.avatar
            }
          }

          if(member.partyPositionType =='5'){
            data.children[0].children[2].name = member.partyPositionTypeText+":"+member.memberName;
            if(member.avatar != null){
              data.children[0].children[2].image_url = process.env.VUE_APP_BASE_API + member.avatar
            }
          }
        }
        this.treeData = data;
        this.$forceUpdate()
      },

    }
  };
</script>

<style scoped>
  .org-info-bg {
    height: 100%;
    width: 100%;
    background: url("../../../assets/image/orgInfo-bg.jpg") no-repeat;
    background-size: 100% 100%;
  }

  .level-center{
    text-align: center; /*IE*/
    text-align: -moz-center; /*Firefox*/
    text-align: -webkit-center; /*Chrome*/
  }

  .org-info-lable{
    text-align: right;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    font-weight: 600;
    color: #000000;
    line-height: 30px;
    padding: 0 12px 0 0;
    box-sizing: border-box;
  }

  .org-info-content{
    text-align: left;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 30px;
    padding: 0 12px 0 0;
    box-sizing: border-box;
  }

  .tree-chart >>> td{
    padding: 0 0 10px 0 !important;
  }

  .tree-chart >>> .node{
    width:auto !important;
  }

  /*.tree-chart >>> .avat{
    width:3em !important;
    height:3em !important;
  }*/

  .tree-chart >>> .avat img{
    background-color:rgb(165, 27, 27);
  }
  .tree-chart >>> .name{
    font-size: 12px;
    color: #000000;
  }
</style>
