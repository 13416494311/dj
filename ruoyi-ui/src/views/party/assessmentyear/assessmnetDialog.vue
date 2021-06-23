<template>
  <div  class="app-container">
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight"  :close-on-click-modal="false">

      <div :style="bodyStyle">

        <assessment-card v-if="type==1" ref="assessmentCard"
                         :assessmentId="assessmentId" />

        <performance-score-card v-if="type==2" ref="performanceScoreCard"
                                :disabled="disabled"
                                :assessmentId="assessmentId"
                                :assessmentyearId="assessmentyearId"/>
      </div>


      <div slot="footer" class="dialog-footer"  :style="{textAlign:'center'}">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>


  import AssessmentCard from "../assessment/assessmentCard";
  import PerformanceScoreCard from "../performanceScore/performanceScoreCard";
  export default {
    name: "AssessmentDialog",
    components: {
      PerformanceScoreCard,
      AssessmentCard
    },
    props: {
      disabled: {
        type: Boolean,
        default: () => {
          return true
        }
      },
      type: {
        type: Number,
      },
      assessmentId: {
        type: Number,
      },
      assessmentyearId:{
        type: Number,
      },
    },
    data() {
      return {
        // 是否显示弹出层
        open:false,
        // 弹出层标题
        title: "",
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
          padding:'20px',
        },
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {

    },
    watch:{

    },
    methods: {
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      cancel() {
        this.open = false;
      },
    }
  }

</script>
