<template>
  <div>

    <svg-icon class-name="qrcode-icon" icon-class="qrcode" @click="click"/>

    <el-dialog :title="title"
               :visible.sync="open"
               append-to-body
               :close-on-click-modal="false"
    >

      <el-row style="margin-bottom: 20px">
        <el-col :span="24"  style="text-align: center;font-size: 18px;">
          <span style="color: red">安卓手机</span> 请扫下方二维码下载
        </el-col>
      </el-row>
      <el-row style="margin-bottom: 40px">
        <el-col :span="24">
          <div ref="qrCode" id="qrCodeId1" class="level-center"></div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="text-align: center;font-size: 18px;">
          <span style="color: red">苹果手机</span> 请到苹果应用商场，搜索<span style="color: red">“三局广东智慧云党建”</span>下载
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

  import QRCode from 'qrcodejs2'


  export default {
    name: 'AppDownload',
    components: {

    },
    data() {
      return {
        title:"APP下载",
        // 是否显示弹出层
        open: false,
      }
    },
    created() {
      this.getConfigKey("appDownload.android").then(response => {
        this.qrcodeUrl= response.msg;
      });
    },

    methods: {
      click() {
        this.open = true ;
        this.qrCode()
      },
      qrCode() {
        setTimeout(() => {
          document.getElementById("qrCodeId1").innerHTML = "";
          let qrcode = new QRCode(this.$refs.qrCode, {
            text: this.qrcodeUrl,
            width: 300,
            height: 300,
            colorDark: "#000000", //二维码颜色
            colorLight: "#ffffff", //二维码背景色
            correctLevel: QRCode.CorrectLevel.H//容错率，L/M/H
          })
          qrcode.clear(); //清除二维码
          qrcode.makeCode(this.qrcodeUrl); //生成另一个新的二维码
        }, 500);
      },
      // 取消按钮
      cancel() {
        this.open = false;
      },
    }
  }
</script>

<style scoped>

  .qrcode-icon {
    display: inline-block;
    cursor: pointer;
    width: 20px !important;
    height: 20px !important;
    vertical-align: 10px;
  }

  .level-center{
    text-align: center; /*IE*/
    text-align: -moz-center; /*Firefox*/
    text-align: -webkit-center; /*Chrome*/
  }

</style>
