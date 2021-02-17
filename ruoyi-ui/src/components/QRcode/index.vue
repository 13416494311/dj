<template>
  <div class="app-container">
    <el-dialog :title="title"
               :visible.sync="open"
               append-to-body
               :close-on-click-modal="false"
    >
      <div id="qrcode" class="level-center"></div>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import QRCode from 'qrcodejs2'
  export default {
    name: "QRcode",
    data() {
      return {
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
      };
    },
    mounted() {
    },
    created() {
    },
    methods: {
      qrCode(qrcodeUrl) {
        this.$nextTick(function () {
          document.getElementById("qrcode").innerHTML = "";
          let qrcode = new QRCode("qrcode", {
            width: 300,
            height: 300,
            text: qrcodeUrl,
            colorDark: "#333333",
            colorLight: "#ffffff"
          });
        });
      },
      init(title,qrcodeUrl){
        this.open=true;
        this.title= title;
        this.qrCode(qrcodeUrl);
      },
      // 取消按钮
      cancel() {
        this.open = false;
      },

    }
  };
</script>

<style scoped>
  .level-center{
    text-align: center; /*IE*/
    text-align: -moz-center; /*Firefox*/
    text-align: -webkit-center; /*Chrome*/
  }
</style>
