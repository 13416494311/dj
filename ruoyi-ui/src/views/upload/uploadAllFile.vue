<template>
  <div>
    <el-upload
      action="#"
      list-type="picture-card"
      :file-list="fileList"
      multiple
      :http-request="uploadFile"
      :class="{hide:disabled}"
      class="upload"
      accept="image/*,.doc,.docx,.xls,.xlsx,.pdf,.ppt,.zip,.txt">
      <i slot="default" class="el-icon-plus"></i>
      <div slot="file" slot-scope="{file}" style="display: inline">
        <div class="upload-file">
          <img v-if="'jpeg,jpg,gif,png,JPEG,JPG,GIF,PNG'.indexOf(file.name.split('.')[1]) != -1"
               class="el-upload-list__item-thumbnail"
               :src="file.url" :alt="file.name"/>
          <img v-else
               class="el-upload-list__item-thumbnail"
               :src="defaultFilePicUrl" :alt="file.name"/>
          <span class="el-upload-list__item-actions">
                    <span v-if="'jpeg,jpg,gif,png,JPEG,JPG,GIF,PNG'.indexOf(file.name.split('.')[1]) != -1"
                          class="el-upload-list__item-preview"
                          @click="handlePictureCardPreview(file)">
                       <i class="el-icon-zoom-in"></i>
                    </span>
                    <span
                      class="el-upload-list__item-delete"
                      @click="handleDownload(file)">
                      <i class="el-icon-download"></i>
                    </span>
                    <span
                      v-if="!disabled"
                      class="el-upload-list__item-delete"
                      @click="handleRemove(file)">
                      <i class="el-icon-delete"></i>
                    </span>
                  </span>
        </div>
        <div class="upload-text">
          <el-tooltip class="item" effect="dark" :content="setFileNameTip(file)" placement="top">
            <span>{{file.name}}</span>
          </el-tooltip>
        </div>
      </div>
    </el-upload>
    <el-image-viewer
      v-if="dialogVisible"
      :on-close="closeViewer"
      :url-list="srcList"/>
  </div>
</template>

<script>
  import {listFile, upload, delFile} from "@/api/system/file";
  import {downLoadZip} from "@/utils/zipdownload";
  import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
  export default {
    name: 'UploadAllFile',
    components: {
      ElImageViewer
    },
    props: {
      disabled: {
        type: Boolean,
        default: () => {
          return true
        }
      },
    },
    data() {
      return {
        fileList: [],
        defaultFilePicUrl: undefined,
        srcList: [],
        dialogVisible: false,
      }
    },
    created(){
      this.defaultFilePicUrl = require("@/assets/image/file.png");
    },
    methods: {
      init(uuid,fileType){
        this.uuid = uuid ;
        this.fileType = fileType ;
        this.getFileList();
      },
      uploadFile(file) {
        const loading = this.$loading({
          lock: true,
          text: '上传中……',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        let formData = new FormData();
        formData.append("uuid", this.uuid);
        formData.append("file", file.file);
        formData.append("fileType", this.fileType);
        upload(formData).then(response => {
          if (response.code === 200) {
            let file = {};
            file.name = response.data.fileName;
            file.url = process.env.VUE_APP_BASE_API + response.data.filePath;
            file.uid = response.data.id;
            this.fileList.push(file);
            loading.close();
            this.msgSuccess("上传成功！")
          } else {
            loading.close();
            this.msgError(response.msg);
          }
        }).catch(function (err) {
          loading.close();
        });
      },

      setFileNameTip(file) {
        return file.name;
      },
      handlePictureCardPreview(file) {
        this.dialogVisible = true;
        this.srcList.push(file.url)

      },
      closeViewer() {
        this.dialogVisible = false;
        this.srcList = [];
      },
      handleDownload(file) {
        //console.log(file);
        downLoadZip("/system/file/download/" + file.uid);

      },
      handleRemove(file) {
        //console.log(file);
        this.$confirm('是否确认删除该附件?', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "info"
        }).then(() => {
          delFile(file.uid).then(response => {
            this.msgSuccess(response.msg);
            this.getFileList();
          });
        }).catch(function () {
        });
      },
      getFileList() {
        this.fileList = [];
        listFile({'uuid': this.uuid}).then(response => {
          let files = response.rows;
          for (let i = 0; i < files.length; i++) {
            let file = {};
            file.name = files[i].fileName;
            file.url = process.env.VUE_APP_BASE_API + files[i].filePath;
            file.uid = files[i].id;
            this.fileList.push(file);
          }
        });
      },

    }
  }
</script>

<style scoped>

</style>
