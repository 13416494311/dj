<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="标题" prop="noticeTitle">
        <el-input
          v-model="queryParams.noticeTitle"
          placeholder="请输入标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作人员" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入操作人员"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:notice:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:notice:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:notice:remove']"
        >删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="noticeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="标题"
        align="center"
        prop="noticeTitle"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="类型"
        align="center"
        prop="noticeType"
        :formatter="typeFormat"
        width="100"
      />
      <el-table-column
        label="状态"
        align="center"
        prop="status"
        :formatter="statusFormat"
        width="100"
      />
      <el-table-column label="创建者" align="center" prop="createUser.userName" width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:notice:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
          >预览</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:notice:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改公告对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" :style="bodyStyle" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="标题" prop="noticeTitle">
              <el-input v-model="form.noticeTitle" placeholder="请输入标题" />
            </el-form-item>
          </el-col>
<!--          <el-col :span="12">
            <el-form-item label="类型" prop="noticeType">
              <el-select v-model="form.noticeType" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="dict in typeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>-->
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="附件">
              <el-upload
                action="#"
                list-type="picture-card"
                :file-list="fileList"
                multiple
                :http-request="uploadFile"
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
              <!--<el-dialog :visible.sync="dialogVisible"
                         append-to-body
                         :close-on-click-modal="false">
                <img width="100%" :src="dialogImageUrl" alt="">
              </el-dialog>-->
              <el-image-viewer
                v-if="dialogVisible"
                :on-close="closeViewer"
                :url-list="srcList" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="内容">
              <Editor v-model="form.noticeContent" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button type="primary" @click="preview">预 览</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <pre-view ref="preView"></pre-view>
  </div>

</template>

<script>
import { listNotice, getNotice, delNotice, addNotice, updateNotice, exportNotice } from "@/api/system/notice";
import Editor from '@/components/Editor';
import {listFile, upload, delFile} from "@/api/system/file";
import {downLoadZip} from "@/utils/zipdownload";
import PreView from './preView';
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'


export default {
  name: "Notice",
  components: {
    Editor,PreView,ElImageViewer,
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
      // 公告表格数据
      noticeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 类型数据字典
      statusOptions: [],
      // 状态数据字典
      typeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        createBy: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        noticeTitle: [
          { required: true, message: "标题不能为空", trigger: "blur" }
        ],
      },
      bodyStyle: {
        overflowY: 'auto',
        height: '',
        marginLeft: '2%',
        paddingRight: '2%',
      },
      fileList: [],
      defaultFilePicUrl: undefined,
      dialogImageUrl: '',
      dialogVisible: false,
      srcList:[],
      noticeUuid:undefined,
      noticeType:undefined,
    };
  },
  created() {
    this.getList();
    this.defaultFilePicUrl = require("@/assets/image/file.png");
    this.getDicts("sys_notice_status").then(response => {
      this.statusOptions = response.data;
    });
    this.getDicts("sys_notice_type").then(response => {
      this.typeOptions = response.data;
    });
    window.addEventListener('resize', this.getHeight);
    this.getHeight()
    this.getNoticeTypeByPath()
  },
  methods: {
    getNoticeTypeByPath(){
      let path = this.$route.path;
      switch (path) {
        case "/office/notice/1" :
          this.noticeType = "1";
          break;
        case "/office/notice/2" :
          this.noticeType = "2";
          break;
        case "/office/notice/3" :
          this.noticeType = "3";
          break;
        default:
          break;
      }
      this.queryParams.noticeType = this.noticeType;
    },
    /**附件上传*/
    uploadFile(file) {
      const loading = this.$loading({
        lock: true,
        text: '上传中……',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      let formData = new FormData();
      formData.append("uuid", this.form.noticeUuid);
      formData.append("file", file.file);
      formData.append("fileType", "sysNotice");
      upload(formData).then(response => {
        if (response.code === 200) {
          //this.getFileList();
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
    setFileNameTip(file){
      return file.name;
    },
    handlePictureCardPreview(file) {
      //this.dialogImageUrl = file.url;
      this.dialogVisible = true;
      this.srcList.push(file.url)

    },
    closeViewer() {
      this.dialogVisible = false;
      this.srcList=[];
    },
    handleDownload(file) {
      //console.log(file);
      downLoadZip("/system/file/download/" + file.uid);

    },
    getFileList() {
      this.fileList = [];
      listFile({'uuid': this.form.noticeUuid}).then(response => {
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
    /** 对话框自适应高度 */
    getHeight() {
      this.bodyStyle.height = window.innerHeight - 281 + 'px';
    },
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      listNotice(this.queryParams).then(response => {
        this.noticeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 公告状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 公告状态字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.noticeType);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: undefined,
        noticeContent: undefined,
        status: "0"
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.noticeId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增";
      this.form.noticeUuid = this.uuid();
      this.getFileList();
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const noticeId = row.noticeId || this.ids
      getNotice(noticeId).then(response => {
        this.form = response.data;
        this.getFileList();
        this.open = true;
        this.title = "修改";
      });
    },
    handleSee(item){
      this.$refs.preView.init(item);
    },
    preview(){
      this.$refs.preView.init(this.form);
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.noticeType = this.noticeType
          if (this.form.noticeId != undefined) {
            updateNotice(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addNotice(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const noticeIds = row.noticeId || this.ids
      this.$confirm('是否确认删除编号为"' + noticeIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delNotice(noticeIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    }
  }
};
</script>
