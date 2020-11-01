<template>
  <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
             @open="getHeight" :close-on-click-modal="false">
    <div :style="bodyStyle" class="m_xl">
      <div class="m_xl_m">
        <div class="m_xl_t">{{item.noticeTitle}}</div>
        <div class="m_xl_z">
          <span>{{item.createTime}}</span>
        </div>
        <div class="m_xl_c" v-html="item.noticeContent">
          {{item.noticeContent}}
        </div>
        <div class="m_xl_f">
          <div v-for="(item, index) in fileList" :key="index">
            <el-row>
              <el-button
                size="small"
                type="text"
                icon="el-icon-paperclip"
                @click="handleDownload(item)"
                style="float:left;font-size: 16px"
              >{{item.fileName}}
              </el-button>
              <br/>
            </el-row>
          </div>
        </div>
      </div>
    </div>
    <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
      <el-button @click="cancel">关 闭</el-button>
    </div>
  </el-dialog>

</template>

<style scoped>
  @import '../../../assets/styles/notice/notice.css';
</style>

<script>
  import {listFile, upload, delFile} from "@/api/system/file";
  import {downLoadZip} from "@/utils/zipdownload";
  export default {
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
        item:{},
        fileList: [],
      }
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {

    },
    methods: {

      init(item){
        console.log(JSON.stringify(item))
        this.item=item;
        if(item.createTime ==undefined){
          this.item.createTime =this.getNowFormatDate();
        }
        this.getFileList();
        this.open = true;
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },

      // 取消按钮
      cancel() {
        this.open = false;
      },
      handleDownload(file) {
        downLoadZip("/system/file/download/" + file.id);
      },
      getFileList() {
        this.fileList = [];
        listFile({'uuid': this.item.noticeUuid}).then(response => {
          this.fileList = response.rows;
        });
      },

    },

  }
</script>
