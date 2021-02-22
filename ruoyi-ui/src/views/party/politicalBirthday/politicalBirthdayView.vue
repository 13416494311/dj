<template>
  <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body
             @open="getHeight" :close-on-click-modal="false">
    <div :style="bodyStyle" class="m_xl">
      <div class="m_xl_m">
        <div class="m_xl_t">{{item.theme}}</div>
        <div class="m_xl_z">
          <span>{{item.sendTime}}</span>
        </div>
        <div class="m_xl_c" v-html="item.content">
          {{item.content}}
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
      <comment ref="comment" :businessUuid="birthdayUuid" :authorId="userId"/>
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
  import Comment from '@/components/Comment';

  export default {
    components: {
      Comment
    },
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
        birthdayUuid:undefined,
        userId:undefined,
      }
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {

    },
    watch:{
      // 根据名称筛选部门树
      'open'(val) {
        if(!val ){
          this.$refs.comment.closeWebsocket();
      }
      },
    },
    methods: {

      init(item){
        console.log(JSON.stringify(item))
        this.item=item;
        this.birthdayUuid = item.birthdayUuid
        if(this.$refs.comment){
          this.$refs.comment.init();
        }
        this.userId = item.partyMember.sysUser.userId
        if(item.sendTime ==undefined){
          this.item.sendTime =this.getNowFormatDate();
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
        this.$refs.comment.closeWebsocket();
      },
      handleDownload(file) {
        downLoadZip("/system/file/download/" + file.id);
      },
      getFileList() {
        this.fileList = [];
        listFile({'uuid': this.item.birthdayUuid}).then(response => {
          this.fileList = response.rows;
        });
      },

    },

  }
</script>
