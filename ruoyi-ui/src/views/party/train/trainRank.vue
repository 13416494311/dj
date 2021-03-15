<template>
  <div>
    <el-dialog :title="title" :visible.sync="open" width="460px" append-to-body custom-class="train-rank">
      <div :style="bodyStyle">
        <el-card shadow="never" style="margin-bottom: 20px;">
          <el-row>
            <el-col  :span="20">
              <span class="total-score-label">
                成长总积分
              </span>
            </el-col>
            <el-col  :span="4">
              <span class="total-score">
                {{totalScore}}
              </span>
            </el-col>
          </el-row>
        </el-card>

        <el-row style="margin-bottom: 20px;">
          <el-col  :span="24">
            <el-button-group style="width: 100%">
              <el-button
                type="primary"
                size="mini"
                :plain="rankType != '1'"
                class="rank-type-btn"
                @click="chooseRankType(1)"
              >总积分
              </el-button>
              <el-button
                type="primary"
                size="mini"
                :plain="rankType != '2'"
                class="rank-type-btn"
                @click="chooseRankType(2)"
              >年度积分
              </el-button>
            </el-button-group>
          </el-col>
        </el-row>

        <el-card shadow="never" style="margin-bottom: 20px;">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-row>
                <el-col :span="24" class="train-rank-info total-info">
                  {{infoScore}}
                </el-col>
              </el-row>
              <el-row>
                <el-col v-if="rankType == '1'" :span="24" class="train-rank-info">
                  学习积分
                </el-col>
                <el-col v-if="rankType == '2'" :span="24" class="train-rank-info">
                  {{year}}年度学习积分
                </el-col>
              </el-row>
            </el-col>

            <el-col :span="8">
              <el-row>
                <el-col :span="24" class="train-rank-info  rank-info">
                  {{infoAllRank}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24" class="train-rank-info">
                  您在公司排名
                </el-col>
              </el-row>
            </el-col>

            <el-col :span="8">
              <el-row>
                <el-col :span="24" class="train-rank-info rank-info">
                  {{infoSelfRank}}
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24" class="train-rank-info">
                  您在支部排名
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </el-card>

        <el-card shadow="never" style="margin-bottom: 20px;">
          <el-row style="margin-bottom: 20px;line-height: 20px">
            <el-col :span="12"  class="train-rank-self-label">
              本支部学习标兵
            </el-col>
            <el-col :span="12" class="train-rank-self-content">
              本支部 <span style="font-size: 18px;font-style: oblique;">{{partyOrgCount}}</span> 人参与排名
            </el-col>
          </el-row>
          <el-table v-loading="loading"
                    :stripe="true"
                    :data="trainRankList"
                    :show-header="false">

            <el-table-column label="序号" align="center" type="rank"  width="80">
              <template slot-scope="scope">
                <span v-if="scope.row.rank ==1" class="rank-index" style="color: gold">{{scope.row.rank}}st </span>
                <span v-else-if="scope.row.rank ==2" class="rank-index" style="color: silver">{{scope.row.rank}}nd  </span>
                <span v-else-if="scope.row.rank ==3"class="rank-index"  style="color: rgb(186,110,64)">{{scope.row.rank}}rd  </span>
                <span v-else class="rank-index"  style="color: red">{{scope.row.rank}}th </span>
              </template>
            </el-table-column>
            <el-table-column label="党员姓名" align="center"  prop="partyMember.memberName"/>
            <el-table-column label="积分" align="center"  prop="score"  width="80" :formatter="scoreFormat"/>
          </el-table>
        </el-card>


      </div>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="text" style="color: white" @click="cancel">关 闭</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<style>
  .train-rank>.el-dialog__header{
    background-color: rgb(252 103 63);
  }
  .train-rank>.el-dialog__body{
    background-color: rgb(252 103 63);
  }
  .train-rank>.el-dialog__footer{
    background-color: rgb(252 103 63);
  }

  .total-score-label{
    font-size: 16px;
    font-weight: 600;
    color: black;
    line-height: 32px;
  }
  .total-score{
    font-size: 32px;
    font-weight: bold;
    color: red ;
    line-height: 32px;
  }

  .rank-type-btn{
    width: 50%;
    height: 50px;
    font-size: 16px;
    letter-spacing: 5px;
  }


  .train-rank-info{
    font-size: 12px;
    color:  #606266;
    text-align: center;
    line-height: 25px;
  }

  .total-info{
    font-size: 20px;
    color: red ;
    font-style: oblique;
  }

  .rank-info{
    font-size: 16px;
    color: black;
    font-style: oblique;
  }

  .train-rank-self-label{
    font-size: 14px;
    font-weight: 600;
    color: black;
  }

  .train-rank-self-content{
    font-size: 12px;
    color:  #606266;
    text-align: right;
  }

  .rank-index {
    font-size: 20px;
    font-style: oblique;
    font-weight: 500;
  }



</style>
<script>
  import {
    addTrainMember,
    addTrainMembers,
    delTrainMember,
    exportTrainMember,
    getTrainMember,
    listTrainMember,
    updateTrainMember,
    getTrainRankInfo
  } from "@/api/party/trainMember";


  export default {
    name: "TrainRank",
    components: {},
    data() {
      return {
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        // 遮罩层
        loading: true,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 8,
        },
        trainRankTab: "0",
        rankType : "1",

        totalScore:'',

        infoScore:'',
        infoAllRank:'',
        infoSelfRank:'',

        trainRankList: [],
        partyOrgCount: '',

      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
      this.getHeight();
    },
    created() {
      this.year = new Date().getFullYear();

    },
    methods: {
      init() {
        this.open = true;
        this.getTrainRankInfo();
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询参与培训党员列表 */
      getTrainRankInfo() {
        this.loading = true ;
        getTrainRankInfo(this.rankType).then(response => {
          this.totalScore = response.data.totalScore;
          this.infoScore = response.data.infoScore;
          this.infoAllRank = response.data.infoAllRank;
          this.infoSelfRank = response.data.infoSelfRank;
          this.trainRankList = response.data.trainRankList;
          this.partyOrgCount = response.data.partyOrgCount;
          this.loading = false ;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      chooseRankType(rankType){
        this.rankType = rankType;
        this.getTrainRankInfo();
      },
      scoreFormat(row, column,val){
        return val +"分";
      },
    }
  };
</script>
