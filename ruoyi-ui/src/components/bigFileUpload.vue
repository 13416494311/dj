<template>
  <div class="app-container">
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <div :style="bodyStyle">
        <uploader :key="uploader_key" :options="options" class="uploader-example"
                  @file-success="onFileSuccess">
          <uploader-unsupport></uploader-unsupport>
          <uploader-drop>
            <uploader-btn :attrs="attrs" :single="true">选择视频</uploader-btn>
          </uploader-drop>
          <uploader-list></uploader-list>
        </uploader>
      </div>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<style>
  .uploader-example {
    width: 90%;
    padding: 15px;
    margin: 40px auto 0;
    font-size: 12px;
    box-shadow: 0 0 10px rgba(0, 0, 0, .4);
  }
  .uploader-example .uploader-btn {
    margin-right: 4px;
  }
  .uploader-example .uploader-list {
    max-height: 440px;
    overflow: auto;
    overflow-x: hidden;
    overflow-y: auto;
  }
</style>
<script>
  export default {
    data() {
      return {
        uploader_key: new Date().getTime(),//这个用来刷新组件--解决不刷新页面连续上传的缓存上传数据（注：每次上传时，强制这个值进行更改---根据自己的实际情况重新赋值）
        options: {
          target: process.env.VUE_APP_BASE_API + "/system/upload/bigFile",//SpringBoot后台接收文件夹数据的接口
          testChunks: false//是否检测分片-不检测
        },
        attrs: {
          accept: '.mp4, .rmvb, .mkv, .wmv, .flv , .zip'
        },
        title:undefined,
        callback:undefined,
        bodyStyle:{
          overflowY:'auto',
          height: '',
          marginLeft:'2%' ,
          paddingRight:'2%',
        },
        // 是否显示弹出层
        open: false,
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    methods: {
      init(title,callback) {
        this.title =title;
        this.callback =callback;
        this.open = true;
      },
      cancel(){
        this.open = false;
      },
      /** 对话框自适应高度 */
      getHeight(){
        this.bodyStyle.height=window.innerHeight-281+'px';
      },
      onFileSuccess: function (rootFile, file, response, chunk) {
        //这里可以根据response（接口）返回的数据处理自己的实际问题（如：从response拿到后台返回的想要的数据进行组装并显示）
        //注，这里从文件夹每上传成功一个文件会调用一次这个方法

        console.log(JSON.stringify(rootFile))
        console.log("-----------")
        console.log(JSON.stringify(file))
        console.log("-----------")
        console.log(JSON.stringify(response))
        console.log("-----------")
        console.log(JSON.stringify(chunk))

        let result = eval('(' + response + ')');

        this.$confirm('是否确认上传该视频?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delMember(memberIds);
        }).then(() => {
          this.getJoinMemberList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });

        if (result.code === 200) {
          this.$confirm('是否确认上传该视频?', "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "info"
          }).then(() => {
            this.$emit("callback",result.msg,file.name);
            this.cancel();
            this.msgSuccess("上传完成!")
          }).catch(function () {
          });
        } else {
          this.msgError(result.msg);
        }


      },

    }
  }
</script>
