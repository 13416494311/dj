<template>
  <div>
    <el-table v-loading="loading"
              :data="noticeList"
              :show-header="false"
              @row-dblclick="handleSee">
      <el-table-column
        label="标题"
        align="left"
        prop="noticeTitle"
        :show-overflow-tooltip="true"
      >
      <template slot-scope="scope">
        <el-link :underline="false" @click="handleSee(scope.row)"> {{scope.row.noticeTitle}}</el-link>
      </template>
      </el-table-column>
      <!--<el-table-column
        label="类型"
        align="left"
        prop="noticeType"
        :formatter="typeFormat"
        width="80"
      />-->
      <el-table-column label="创建者" align="left" prop="createUser.userName" width="80" />
      <el-table-column label="创建时间" align="left" prop="createTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pre-view ref="preView"></pre-view>
  </div>

</template>

<script>
  import { listNotice, getNotice, delNotice, addNotice, updateNotice, exportNotice } from "@/api/system/notice";
  import Editor from '@/components/Editor';
  import {listFile, upload, delFile} from "@/api/system/file";
  import {downLoadZip} from "@/utils/zipdownload";
  import PreView from './preView';


  export default {
    name: "Notice",
    components: {
      Editor,PreView
    },
    props:{
      noticeType:String,
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 公告表格数据
        noticeList: [],
        // 类型数据字典
        statusOptions: [],
        // 状态数据字典
        typeOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 5,
          status:0,
          noticeTitle: undefined,
          createBy: undefined,
          status: undefined,
        },
        fileList: [],
      };
    },
    created() {
      this.queryParams.noticeType= this.noticeType
      this.getList();
      this.getDicts("sys_notice_status").then(response => {
        this.statusOptions = response.data;
      });
      this.getDicts("sys_notice_type").then(response => {
        this.typeOptions = response.data;
      });
    },
    methods: {
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

      handleSee(item){
        this.$refs.preView.init(item);
      },

    }
  };
</script>
