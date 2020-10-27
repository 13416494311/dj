<template>
  <div class="dssdjt-main">
    <div class="dssdjt-box" v-if="open">
      <div class="dssdjt">
        <div class="dssdjt-title"><img src="../../../assets/styles/history/title.png" alt=""/></div>
        <div class="dssdjt-star"></div>
        <div class="dssdjt-content">
          <div class="dssdjt-cont-show">
            <div v-for="history in historyList" :key="history.historyId">
              <h3>{{history.title}}</h3>
              <p>{{history.content}}</p>
            </div>
          </div>
        </div>
        <div class="dssdjt-cont-bg"></div>
      </div>
      <div class="dssdjt-footer" :style="{textAlign:'center'}">
        <el-link @click="cancel" style="color: white">关 闭</el-link>
      </div>
      <div class="dssdjt-close" >
        <el-button @click="cancel" icon="el-icon-close" circle></el-button>
      </div>
    </div>
  </div>

</template>
<style scoped>
  @import '../../../assets/styles/history/history.css';
</style>
<script>
  import { listHistory } from "@/api/sys/history";

  export default {
    data() {
      return {
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: true,
        bodyStyle: {
          overflowY: 'auto',
          height: '',
        },
        historyList:[],
      }
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.getHeight();
      this.getHistoryList();
    },
    methods: {
      getHistoryList(){
        let month = new Date().getMonth()+1;
        let day = new Date().getDate();
        listHistory({month:month,day:day}).then(response => {
          this.historyList = response.rows;

        });
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight  + 'px';
      },

      // 取消按钮
      cancel() {
        this.open = false;
      },


    },

  }
</script>
