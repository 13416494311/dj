<template>
  <div>
    <div style="text-align: center;font-size: 24px;padding: 8px;">
      {{duePlanTitle}}
    </div>
    <el-form ref="form" :model="form" :rules="rules" >
      <el-table :stripe="true"
                :border="true"
                v-loading="loading" :data="assessmentList" >
        <el-table-column label="序号" align="center" type="index"/>
        <el-table-column label="党组织名称" width="180" align="center"  :formatter="partyOrgFormat"
                         prop="djPartyOrg.partyOrgFullName"/>
        <el-table-column v-for="(item,index) in scoreItemList"
                         :key="index"
                         :label="item.item+'('+item.score+')'"
                         width="180"
                         align="center">

          <template slot-scope="scope">
            <el-form-item v-if="!disabled"
                          :prop="createProp(scope.$index,index)"
                          :rules="[{validator: checkScore, trigger: 'blur'}]">
              <el-input-number style="width:150px"
                               v-model="scope.row.djOrgAssessmentListScoreList[index]['performanceAppraisalScore']"
                               size="small"
                               @change="((val)=>{changePerformanceAppraisalScore(val, scope.row,index)})"
                               controls-position="right"
                               :precision="1" :step="0.5"
                               :min="0"></el-input-number>
            </el-form-item>

            <div v-if="disabled">
              {{scope.row.djOrgAssessmentListScoreList[index]['performanceAppraisalScore']==undefined?'':scope.row.djOrgAssessmentListScoreList[index]['performanceAppraisalScore'].toFixed(1)+' 分'}}
            </div>
          </template>

        </el-table-column>

        <el-table-column label="总分" align="center" prop="performanceAppraisalScore"  :formatter="scoreFormat"/>

        <el-table-column v-if="!disabled" label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['party:assessmentScore:edit']"
            >保存
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>

  </div>
</template>

<script>
  import {
    addAssessmentScore,
    delAssessmentScore,
    exportAssessmentScore,
    getAssessmentScore,
    listAssessmentScore,
    updateAssessmentScore,
    updateAssessmentScoreList,
    updateAssessmentList,
    getScoreItem
  } from "@/api/party/assessmentScore";
  import {
    listAssessmentyear,
    getAssessmentyear,
    delAssessmentyear,
    addAssessmentyear,
    updateAssessmentyear,
    exportAssessmentyear
  } from "@/api/party/assessmentyear";
 import {
    addAssessment,
    delAssessment,
    exportAssessment,
    getAssessment,
    listAssessment,
    listAssessment1,
    updateAssessment,
  } from "@/api/party/assessment";

  export default {
    name: "PerformanceScoreCard",
    props: {
      disabled: {
        type: Boolean,
        default: () => {
          return false
        }
      },
      assessmentyearId: {
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
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          assessmentUuid: undefined,
          item: undefined,
          orderNum: undefined,
          score: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          assessmentUuid: [
            {required: true, message: "assessment_uuid不能为空", trigger: "blur"}
          ],
          item: [
            {required: true, message: "考核项目不能为空", trigger: "blur"}
          ],
          orderNum: [
            {required: true, message: "排序不能为空", trigger: "blur"}
          ],
          score: [
            {required: true, message: "分数不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        duePlanTitle: "",
        assessmentList: [],
        showSum: true,
        scoreItemList:[],
        performanceAppraisalScoreRequired: false,
        performanceAppraisalScore:'',
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    watch: {
      'assessmentyearId'(val) {
        this.getList();
      }
    },
    created() {
      this.getList();
    },
    methods: {
      //绩效评分验证
      checkScore(rule, value, callback) {
        let filed = rule.field
        let fileds = filed.split("-");
        let performanceAppraisalScore = this.assessmentList[fileds[1]].djOrgAssessmentListScoreList[fileds[2]].performanceAppraisalScore;
        let score = this.scoreItemList[fileds[2]].score;
        if(performanceAppraisalScore>score){
          callback(new Error("评分不能大于"+score+"分!"));
        }else{
          if (!performanceAppraisalScore && this.performanceAppraisalScoreRequired) {
            callback(new Error("评分不能为空!"));
          } else {
            callback()
          }
        }

      },
      createProp(rowIndex,headIndex) {
        return "score-" + rowIndex + "-"+headIndex;
      },
      changePerformanceAppraisalScore(val, row, index) {
        let total = 0;
        for (let i in row.djOrgAssessmentListScoreList) {
          if (row.djOrgAssessmentListScoreList[i].performanceAppraisalScore) {
            total = total + Number(row.djOrgAssessmentListScoreList[i].performanceAppraisalScore);
          }
        }
        row.performanceAppraisalScore = total
        if (total == 0) {
          return ""
        } else {
          return total;
        }
      },
      scoreFormat(row, column,cellValue, index) {
        return cellValue==undefined?'':cellValue.toFixed(1) + " 分";
      },
      partyOrgFormat(row, column) {
        let partyOrgFullName = row.djPartyOrg.partyOrgFullName;
        return partyOrgFullName.substring(partyOrgFullName.indexOf("/") + 1);
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询绩效考核评分列表 */
      getList() {
        this.loading = true;
        getAssessmentyear(this.assessmentyearId).then(response => {
          this.duePlanTitle = response.data.assessmentName;
          getScoreItem(response.data.assessmentyearUuid).then(response => {
            this.scoreItemList = response.data;
            this.loading = false;
          })
          let params={}
          params.assessmentyearUuid = response.data.assessmentyearUuid
          params.djPartyOrg ={
            performanceAppraisal:'Y'
          }
          listAssessment1(params).then(response => {
            this.assessmentList = response.rows;
            for (let i in this.assessmentList) {
              let djOrgAssessmentListScoreList = this.assessmentList[i].djOrgAssessmentListScoreList
              for (let j in djOrgAssessmentListScoreList) {
                if (!djOrgAssessmentListScoreList[j].performanceAppraisalScore) {
                  djOrgAssessmentListScoreList[j].performanceAppraisalScore = undefined
                }
              }
            }
            this.total = response.total;
            this.loading = false;
          });

        })
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
          assessmentUuid: undefined,
          item: undefined,
          orderNum: undefined,
          score: undefined,
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };
        this.resetForm("form");
      },

      /** 修改按钮操作 */
      handleUpdate(row) {
        let params = [];
        params.push(row)
        updateAssessmentList(params).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存成功");
            this.getList();
          } else {
            this.msgError(response.msg);
          }
        });
      },

      submit(status) {
        if (status == 2) {
          this.performanceAppraisalScoreRequired = true;
        } else {
          this.performanceAppraisalScoreRequired = false;
        }
        this.$nextTick(()=>{
          this.$refs["form"].validate((valid ,object)=> {
            if (valid) {
              for(let i in this.assessmentList){
                this.assessmentList[i].performanceAppraisalStatus = status
              }
              updateAssessmentList(this.assessmentList).then(response => {
                if (response.code === 200) {
                  let params = {}
                  params.id = this.assessmentyearId;
                  params.performanceAppraisalStatus = status;
                  updateAssessmentyear(params).then(response => {
                    console.log(response)
                    if (response.code === 200) {
                      this.msgSuccess("操作成功");
                      this.$emit("ok");
                    } else {
                      this.msgError(response.msg);
                    }
                  })
                }
              })
            }
          })
        })
      },


    }
  };
</script>
