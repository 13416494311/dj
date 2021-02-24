<template>
  <div class="app-container">
    <el-dialog :title="title"
               :visible.sync="open"
               append-to-body
               :close-on-click-modal="false"
    >
      <div ref="qrCode" id ="qrCodeId" class="level-center"></div>
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
        setTimeout(() => {
          document.getElementById("qrCodeId").innerHTML = "";
          let qrcode = new QRCode(this.$refs.qrCode, {
            text: qrcodeUrl,
            width: 300,
            height: 300,
            colorDark: "#000000", //二维码颜色
            colorLight: "#ffffff", //二维码背景色
            correctLevel: QRCode.CorrectLevel.H//容错率，L/M/H
          })
          qrcode.clear(); //清除二维码
          qrcode.makeCode(qrcodeUrl); //生成另一个新的二维码
        }, 500);
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
