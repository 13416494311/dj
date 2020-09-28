<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="活动主题" prop="activityTheme">
        <el-input
          v-model="queryParams.activityTheme"
          placeholder="请输入活动主题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      <el-form-item>
        <span class="info-span info-span-left">预警颜色说明:</span>
        <i class="dot danger"/><span class="info-span info-span-left">已超期</span>
        <i class="dot warn"/> <span class="info-span info-span-left">即将超期</span>
        <span class="info-span">（已归档状态不参与预警）</span>
      </el-form-item>
    </el-form>


    <el-table v-loading="loading"
              :data="detailedList"
              :row-class-name="tableRowClassName">
      <el-table-column label="活动名称" align="center" prop="djActivityPlan.activityTheme"/>
      <el-table-column label="活动类型" align="center" prop="djActivityPlan.activityType"
                       :formatter="activityTypeFormat"/>
      <el-table-column label="活动党组织" align="center" prop="djPartyOrg.partyOrgFullName"/>
      <el-table-column label="负责人" align="center" prop="djPartyMember.memberName"/>
      <el-table-column v-if="pathType=='1'" label="计划开始时间" align="center" prop="planStartTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="pathType=='1'" label="计划截止时间" align="center" prop="planEndTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="pathType=='2'" label="活动计划召开时间" align="center" prop="planStartTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.activityPlanStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="pathType=='2'" label="活动计划结束时间" align="center" prop="planStartTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.activityPlanEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="活动状态" align="center" prop="status"
                       :formatter="detailedStatusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="text"
            icon="el-icon-search"
            @click="handleSee(scope.row)"
          >查看
          </el-button>
          <el-button
            v-if="scope.row.status !='5' && pathType=='1'"
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['activity:detailed:edit']"
          >活动管理
          </el-button>
          <el-button
            v-if="scope.row.status =='5'&&(scope.row.partyMemberId == user.partyMemberId || admin)"
            size="small"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >活动管理
          </el-button>
          <el-button
            v-if="scope.row.status =='5'"
            size="small"
            type="text"
            icon="el-icon-download"
            @click="handleExportArchives(scope.row)"
          >下载
          </el-button>
          <el-button
            v-if="scope.row.status !='5' && pathType=='2'"
            size="small"
            type="text"
            icon="el-icon-download"
            @click="handleOpenSupervise(scope.row)"
          >督办
          </el-button>
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

    <!-- 添加或修改活动详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body
               @open="getHeight" :close-on-click-modal="false">
      <div :style="bodyStyle">
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动进度</span>
          </div>
          <el-steps v-if="!disabled6" :active="activeStep" finish-status="success">
            <el-step title="活动计划"></el-step>
            <!--<el-step title="启动活动"></el-step>-->
            <el-step title="启动活动"></el-step>
           <!-- <el-step title="结束活动"></el-step>-->
            <el-step title="活动归档"></el-step>
          </el-steps>
          <el-steps v-if="disabled6" :active="activeStep" finish-status="success">
            <el-step title="完善活动信息"></el-step>
            <el-step title="启动活动"></el-step>
            <el-step title="活动取消"></el-step>
          </el-steps>
        </el-card>
        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动信息</span>
          </div>
          <el-form ref="form" :model="form" :rules="rules" label-width="150px">

            <el-row>
              <el-col :span="24">
                <el-form-item label="活动名称">
                  <el-input :disabled="true"
                            v-model="djActivityPlan.activityTheme" placeholder="请输入活动名称"/>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="活动类型">
                  <el-select :disabled="true"
                             v-model="djActivityPlan.activityType"
                             style="width: 100%"
                             placeholder="请选择党员活动类型">
                    <el-option
                      v-for="dict in activityTypeOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="负责人" prop="partyMemberName">
                  <el-input :disabled="true" v-model="form.partyMemberName"
                            placeholder="请输入负责人">
                    <el-button :disabled="disabled || !disabled1" slot="append" icon="el-icon-search"
                               @click="openMemberChoose"></el-button>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="计划开始日期" prop="planStartTime">
                  <el-date-picker :disabled="true"
                                  clearable size="small"
                                  style="width: 100%"
                                  v-model="form.planStartTime"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="选择计划召开时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计划截止日期" prop="planEndTime">
                  <el-date-picker :disabled="true"
                                  clearable size="small"
                                  style="width: 100%"
                                  v-model="form.planEndTime"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="选择计划结束时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>


            <el-row>
              <el-col :span="12">
                <el-form-item label="活动党组织">
                  <el-input :disabled="true" v-model="djPartyOrg.partyOrgFullName" placeholder="请输入活动党组织"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="活动地点" prop="venue">
                  <el-input :disabled="disabled || !disabled1" v-model="form.venue" placeholder="请输入活动地点">
                    <el-button :disabled="disabled || !disabled1" slot="append" icon="el-icon-map-location"
                               @click="openMap"></el-button>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="" prop="description" v-if="mapOpen" style="height: 500px">
                  <address-map ref="addressMap" @callbackAddress="setVenue"></address-map>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="活动内容" prop="activityContent">
                  <el-input :disabled="disabled || !disabled1  " v-model="form.activityContent"
                            type="textarea"
                            :autosize="{ minRows: 3, maxRows: 6}"
                            placeholder="请输入内容"/>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="活动计划召开时间" prop="activityPlanStartTime">
                  <el-date-picker :disabled="disabled  || !disabled1 "
                                  clearable size="small"
                                  style="width: 100%"
                                  v-model="form.activityPlanStartTime"
                                  type="datetime"
                                  format="yyyy-MM-dd HH:mm:ss"
                                  value-format="yyyy-MM-dd HH:mm:ss"
                                  placeholder="选择活动计划召开时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="活动计划结束时间" prop="activityPlanEndTime">
                  <el-date-picker :disabled="disabled  || !disabled1 "
                                  clearable
                                  size="small"
                                  style="width: 100%"
                                  v-model="form.activityPlanEndTime"
                                  type="datetime"
                                  format="yyyy-MM-dd HH:mm:ss"
                                  value-format="yyyy-MM-dd HH:mm:ss"
                                  placeholder="选择活动计划结束时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="活动实际开始时间" prop="actualStartTime"
                              :rules="[{required: actualRequired ,message: `请选择活动实际开始时间`,trigger: 'blur'}]">
                  <el-date-picker :disabled="disabled  || !disabled3 "
                                  clearable size="small"
                                  style="width: 100%"
                                  v-model="form.actualStartTime"
                                  type="datetime"
                                  format="yyyy-MM-dd HH:mm:ss"
                                  value-format="yyyy-MM-dd HH:mm:ss"
                                  placeholder="选择活动实际开始时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="活动实际结束时间" prop="actualEndTime"
                              :rules="[{required: actualRequired,message: `请选择活动实际结束时间`,trigger: 'blur'}]">
                  <el-date-picker :disabled="disabled || !disabled3"
                                  clearable size="small"
                                  style="width: 100%"
                                  v-model="form.actualEndTime"
                                  type="datetime"
                                  format="yyyy-MM-dd HH:mm:ss"
                                  value-format="yyyy-MM-dd HH:mm:ss"
                                  placeholder="选择活动实际结束时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="记录人" prop="recorder">
                  <el-input :disabled="disabled " v-model="form.recorder"
                            placeholder="请输入记录人"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到会指导人员" prop="mentors">
                  <el-input :disabled="disabled " v-model="form.mentors"
                            placeholder="请输入到会指导人员"/>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="主持人" prop="presenter">
                  <el-input :disabled="disabled " v-model="form.presenter"
                            placeholder="请输入主持人"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="主讲人及身份" prop="speaker">
                  <el-input :disabled="disabled " v-model="form.speaker"
                            placeholder="请输入主讲人及身份"/>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="活动资料">
                  <el-upload
                    action="#"
                    list-type="picture-card"
                    :file-list="planFileList"
                    :http-request="uploadFileNull"
                    :class="{hide:true}"
                    class="upload"
                    accept="image/*,.doc,.docx,.xls,.xlsx,.pdf,.ppt,.zip,.txt">
                    <i slot="default" class="el-icon-plus"></i>
                    <div slot="file" slot-scope="{file}" style="display: inline">
                      <div class="upload-file">
                        <img v-if="'jpeg,jpg,gif,png'.indexOf(file.name.split('.')[1]) != -1"
                             class="el-upload-list__item-thumbnail"
                             :src="file.url" :alt="file.name"/>
                        <img v-else
                             class="el-upload-list__item-thumbnail"
                             :src="defaultFilePicUrl" :alt="file.name"/>
                        <span class="el-upload-list__item-actions">
                          <span v-if="'jpeg,jpg,gif,png'.indexOf(file.name.split('.')[1]) != -1"
                                class="el-upload-list__item-preview"
                                @click="handlePictureCardPreview(file)">
                            <i class="el-icon-zoom-in"></i>
                          </span>
                          <span
                            class="el-upload-list__item-delete"
                            @click="handleDownload(file)">
                            <i class="el-icon-download"></i>
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
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动参与情况</span>
          </div>
          <div style="position: relative">
            <el-tabs v-model="activeTab" @tab-click="handleTabClick">

              <el-tab-pane label="活动参与人" name="1">
                <el-table v-loading="memberLoading" :data="memberList">

                  <el-table-column label="姓名" align="center" prop="djPartyMember.memberName"/>
                  <el-table-column label="所属党组织" align="center" prop="djPartyMember.djPartyOrg.partyOrgFullName"/>
                  <el-table-column label="联系方式" align="center" prop="djPartyMember.mobile"/>
                  <!--<el-table-column v-if="!disabled" label="操作" align="center" class-name="small-padding fixed-width">
                    <template slot-scope="scope">
                      <el-button
                        v-if="scope.row.type=='2'"
                        size="small"
                        type="text"
                        icon="el-icon-delete"
                        @click="handleMemberDelete(scope.row)"
                      >删除
                      </el-button>
                    </template>
                  </el-table-column>-->
                </el-table>
              </el-tab-pane>

              <el-tab-pane label="签到情况" name="2">
                <el-table v-loading="memberLoading" :data="memberList" @selection-change="handleSelectionChange">
                  <el-table-column type="selection" width="55" align="center"/>
                  <el-table-column label="姓名" align="center" prop="djPartyMember.memberName"/>
                  <el-table-column label="所属党组织" align="center" prop="djPartyMember.djPartyOrg.partyOrgFullName"/>
                  <el-table-column label="联系方式" align="center" prop="djPartyMember.mobile"/>
                  <el-table-column label="状态" align="center" prop="djPartyMember.status"
                                   :formatter="memberStatusFormat"/>
                  <el-table-column v-if="!disabled" label="操作" align="center" class-name="small-padding fixed-width">
                    <template slot-scope="scope">
                      <el-dropdown split-button size="small" type="primary">签到
                        <el-dropdown-menu slot="dropdown">
                          <el-dropdown-item
                            v-for="dict in memberStatusOptions"
                            :key="dict.dictValue"
                            @click.native="handleMemberUpdate(scope.row,dict.dictValue)"
                          >{{dict.dictLabel}}
                          </el-dropdown-item>
                        </el-dropdown-menu>
                      </el-dropdown>
                    </template>
                  </el-table-column>
                </el-table>

                <div v-if="!disabled" style="margin-top: 10px">
                  <el-radio-group v-model="memberStatus">
                    <el-radio
                      v-for="dict in memberStatusOptions"
                      :key="dict.dictValue"
                      :label="dict.dictValue"
                    >{{dict.dictLabel}}
                    </el-radio>
                  </el-radio-group>
                  <el-button
                    :disabled="multiple"
                    type="primary"
                    size="mini"
                    @click="handleMembersUpdate"
                    style="float:right;top: 5px"
                  >签到
                  </el-button>
                </div>

              </el-tab-pane>
              <el-tab-pane label="建言献策" name="3">
                <activity-suggestions ref="activitySuggestions"/>
              </el-tab-pane>
              <el-tab-pane label="心得体会" name="4">
                <activity-experience ref="activityExperience"/>
              </el-tab-pane>
              <el-tab-pane label="请假记录" name="5">
                <el-table v-loading="memberLoading" :data="leaveMemberList">
                  <el-table-column label="姓名" align="center" prop="djPartyMember.memberName"/>
                  <el-table-column label="所属党组织" align="center" prop="djPartyMember.djPartyOrg.partyOrgFullName"/>
                  <el-table-column label="联系方式" align="center" prop="djPartyMember.mobile"/>
                  <el-table-column label="状态" align="center" prop="djPartyMember.status"
                                   :formatter="memberStatusFormat"/>
                </el-table>
              </el-tab-pane>

            </el-tabs>

            <el-button
              v-if="!disabled&&activeTab=='1'"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="openOrgMemberTransfer('2')"
              style="position: absolute ;right: 10px;top: 5px"
            >新增
            </el-button>
          </div>
        </el-card>

        <activity-summary ref="activitySummary"/>

        <activity-resolution ref="activityResolution"/>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动图片</span>
          </div>
          <el-upload
            action="#"
            list-type="picture-card"
            :file-list="picFileList"
            :http-request="uploadPicFile"
            :class="{hide:disabled}"
            accept="image/*,">
            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{file}" style="display: inline">
              <img v-if="'jpeg,jpg,gif,png'.indexOf(file.name.split('.')[1]) != -1"
                   class="el-upload-list__item-thumbnail"
                   :src="file.url" :alt="file.name"/>
              <img v-else
                   class="el-upload-list__item-thumbnail"
                   :src="defaultFilePicUrl" :alt="file.name"/>
              <span class="el-upload-list__item-actions">
                    <span v-if="'jpeg,jpg,gif,png'.indexOf(file.name.split('.')[1]) != -1"
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
          </el-upload>
        </el-card>

        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动资料</span>
          </div>
          <el-upload
            action="#"
            list-type="picture-card"
            :file-list="fileList"
            :http-request="uploadFile"
            :class="{hide:disabled}"
            class="upload"
            accept="image/*,.doc,.docx,.xls,.xlsx,.pdf,.ppt,.zip,.txt">
            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{file}" style="display: inline">
              <div class="upload-file">
                <img v-if="'jpeg,jpg,gif,png'.indexOf(file.name.split('.')[1]) != -1"
                     class="el-upload-list__item-thumbnail"

                     :src="file.url" :alt="file.name"/>
                <img v-else
                     class="el-upload-list__item-thumbnail"
                     :src="defaultFilePicUrl" :alt="file.name"/>
                <span class="el-upload-list__item-actions">
                    <span v-if="'jpeg,jpg,gif,png'.indexOf(file.name.split('.')[1]) != -1"
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

        </el-card>


        <el-card shadow="always" style="margin-bottom: 30px;">
          <div slot="header" style="height: 25px">
            <span style="font-weight: bold;font-size: 16px">活动视频</span>
            <el-button
              v-if="!disabled"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAddVideo"
              style="float: right;margin-top: -5px"
            >上传
            </el-button>
          </div>

          <el-row v-for="(item,index) in videoFileList" :key="item.id" style="margin-bottom: 30px">
            <el-col :span="21">
              <div  :id="setVideoId(item)" />
            </el-col>
            <el-col :span="3" v-if="!disabled">
              <el-button
                type="primary"
                icon="el-icon-delete"
                size="mini"
                @click="handleDeleteVideo(item)"
                style="margin-left: 10px;"
              >删除
              </el-button>
             </el-col>
          </el-row>

        </el-card>
      </div>


      <div slot="footer" class="dialog-footer" :style="{textAlign:'center'}">
        <el-button  v-if="!disabled "type="primary" @click="submitForm()">保 存</el-button>
        <!--<el-button type="primary"
                   v-if="this.form.status =='1'" @click="submitForm('2')">启动活动
        </el-button>
        <el-button type="primary"
                   v-if="this.form.status =='2'" @click="submitForm('3')">开始活动
        </el-button>
        <el-button type="primary"
                   v-if="this.form.status =='2'||this.form.status =='3'" @click="submitForm('6')">取消活动
        </el-button>
        <el-button type="primary"
                   v-if="this.form.status =='6'" @click="submitForm('2')">重新启动
        </el-button>
        <el-button type="primary"
                   v-if="this.form.status =='3'" @click="submitForm('4')">结束启动
        </el-button>
        <el-button type="primary"
                   v-if="this.form.status =='4'" @click="submitForm('5')">活动归档
        </el-button>-->
        <el-button type="primary"
                   v-if="!disabled && this.form.status =='1'" @click="submitForm('3')">开始活动
        </el-button>
        <el-button type="primary"
                   v-if="!disabled && this.form.status =='3'" @click="submitForm('6')">取消活动
        </el-button>
        <el-button type="primary"
                   v-if="!disabled && this.form.status =='6'" @click="submitForm('3')">重新开始
        </el-button>
        <el-button type="primary"
                   v-if="!disabled && this.form.status =='3'" @click="submitForm('5')">活动归档
        </el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <party-member ref="partyMember" @callbackMember="setMember"/>
    <member-transfer ref="memberTransfer" @callback="getJoinMemberList"/>
    <el-dialog :visible.sync="dialogVisible"
               append-to-body
               :close-on-click-modal="false">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
    <activity-supervise ref="activitySupervise"/>
    <big-file-upload ref="bigFileUpload" @callback="uploadVideoFile"/>
  </div>
</template>

<style>
  .info-span{
    color:#909399;
  }
  .info-span-left{
    margin-left: 20px;
  }
  .dot{
    position:absolute;
    top:6px;
    width:15px;
    height:15px;
    border-radius:15px;
  }
  .danger{
    background: #F56C6C;
  }
  .warn{
    background: #E6A23C;
  }
</style>
<script>
  import {
    listDetailed,
    listDetailedByParam,
    getDetailed,
    delDetailed,
    addDetailed,
    updateDetailed,
    exportDetailed
  } from "@/api/activity/detailed";
  import {delMember, listMember, updateMember, updateMembers} from "@/api/activity/member";
  import {listFile, upload, delFile,addFile} from "@/api/system/file";
  import {downLoadZip} from "@/utils/zipdownload";
  import partyMember from "../../party/org/partyMemberChoose";
  import addressMap from "../../party/org/addressMap";
  import memberTransfer from "../arrange/memberTransfer";
  import activitySummary from "./activitySummary";
  import activityResolution from "./activityResolution";
  import activitySuggestions from "./activitySuggestions";
  import activityExperience from "./activityExperience";
  import activitySupervise from "./activitySupervise";
  import { getUserProfile } from "@/api/system/user";
  import bigFileUpload from "@/components/bigFileUpload";

  export default {
    name: "Detailed",
    components: {
      partyMember, addressMap, memberTransfer, activitySummary,
      activityResolution, activitySuggestions, activityExperience,
      activitySupervise,bigFileUpload
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        memberLoading: true,
        // 选中数组
        ids: [],
        memberIds: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 活动详情表格数据
        detailedList: [],
        memberList: [],
        leaveMemberList: [],
        memberStatusOptions: [],
        planFileList: [],
        picFileList: [],
        videoFileList:[],
        videoPlayer:[],
        fileList: [],
        dialogImageUrl: '',
        dialogVisible: false,
        defaultFilePicUrl: undefined,
        memberStatus: '1',
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          planUuid: undefined,
          cycle: undefined,
          partyMemberId: undefined,
          venue: undefined,
          activityContent: undefined,
          planStartTime: undefined,
          planEndTime: undefined,
          activityPlanStartTime: undefined,
          activityPlanEndTime: undefined,
          actualStartTime: undefined,
          actualEndTime: undefined,
          recorder: undefined,
          mentors: undefined,
          presenter: undefined,
          speaker: undefined,
          allowComment: undefined,
          activityVoteId: undefined,
          activityExamId: undefined,
          status: undefined,
        },
        // 表单参数
        form: {},
        djActivityPlan: {},
        djPartyOrg: {},
        // 表单校验
        rules: {
          partyMemberName: [
            {required: true, message: "负责人不能为空", trigger: "blur"}
          ],
          venue: [
            {required: true, message: "活动地点不能为空", trigger: "blur"}
          ],
          activityPlanStartTime: [
            {required: true, message: "活动计划召开时间不能为空", trigger: "blur"}
          ],
          activityPlanEndTime: [
            {required: true, message: "活动计划结束时间不能为空", trigger: "blur"}
          ],
        },
        bodyStyle: {
          overflowY: 'auto',
          height: '',
          marginLeft: '2%',
          paddingRight: '2%',
        },
        // 党员活动类型字典
        activityTypeOptions: [],
        //活动详情状态
        arrangeStatusOptions: [],
        activeTab: "1",
        activeStep: undefined,
        disabled: false,
        disabled1: false,
        disabled2: false,
        disabled3: false,
        disabled4: false,
        disabled5: false,
        disabled6: false,
        mapOpen: false,
        actualRequired: false,
        pathType: undefined,
        user: {},
        admin: false,
      };
    },
    mounted() {
      window.addEventListener('resize', this.getHeight);
    },
    created() {
      this.defaultFilePicUrl = require("@/assets/image/file.png");
      this.getDicts("activity_type").then(response => {
        this.activityTypeOptions = response.data;
      });
      this.getDicts("activity_detailed_status").then(response => {
        this.detailedStatusOptions = response.data;
      });
      this.getDicts("activity_member_status").then(response => {
        this.memberStatusOptions = response.data;
      });
      this.getPathType();
      this.getUser();
      setTimeout(()=>{
        this.getList()
      },10);
    },
    methods: {
      getPathType(){
        let path = this.$route.path;
        switch (path) {
          case "/activity/detailed/1" :
            this.pathType = "1";
            break;
          case "/activity/detailed/2" :
            this.pathType = "2";
            break;
          default:
            break;
        }
      },
      /**附件上传*/
      uploadPicFile(file) {
        const loading = this.$loading({
          lock: true,
          text: '上传中……',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        let formData = new FormData();
        formData.append("uuid", this.form.detailedUuid);
        formData.append("file", file.file);
        formData.append("fileType", "activityDetailed");
        formData.append("fileTypeValue", "pic");
        upload(formData).then(response => {
          if (response.code === 200) {
            this.getPicFileList();
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
      uploadFile(file) {
        const loading = this.$loading({
          lock: true,
          text: '上传中……',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        let formData = new FormData();
        formData.append("uuid", this.form.detailedUuid);
        formData.append("file", file.file);
        formData.append("fileType", "activityDetailed");
        formData.append("fileTypeValue", "file");
        upload(formData).then(response => {
          if (response.code === 200) {
            this.getFileList();
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
      handleTabClick(tab, event) {
        switch (tab.name) {
          case "1":
            break;
          case "2":
            break;
          case "3":
            break;
            break;
          case "4":
            break;
            break;
          case "5":
            this.getLeaveMemberList();
            break;
          default:
            break;
        }
      },
      uploadFileNull(file) {

      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      handleDownload(file) {
        //console.log(file);
        downLoadZip("/system/file/download/" + file.uid);

      },
      getPlanFileList() {
        this.planFileList = [];
        listFile({'uuid': this.form.planUuid}).then(response => {
          let files = response.rows;
          for (let i = 0; i < files.length; i++) {
            let file = {};
            file.name = files[i].fileName;
            file.url = process.env.VUE_APP_BASE_API + files[i].filePath;
            file.uid = files[i].id;
            this.planFileList.push(file);
          }
        });
      },
      getPicFileList() {
        this.picFileList = [];
        listFile({'uuid': this.form.detailedUuid, 'fileTypeValue': 'pic'}).then(response => {
          let files = response.rows;
          for (let i = 0; i < files.length; i++) {
            let file = {};
            file.name = files[i].fileName;
            file.url = process.env.VUE_APP_BASE_API + files[i].filePath;
            file.uid = files[i].id;
            this.picFileList.push(file);
          }
        });
      },
      getVideoFileList() {
        this.videoFileList = [];
        listFile({'uuid': this.form.detailedUuid, 'fileTypeValue': 'video'}).then(response => {
          this.videoFileList = response.rows;
          setTimeout(()=>{
            this.initVideo();
          },100)
        });
      },
      initVideo(){
        for(let i=0;i<this.videoFileList.length;i++){
          // 初始化播放器
          this.videoPlayer[i] = new Aliplayer({
            id: "video-"+this.videoFileList[i].id , // 容器id
            source: process.env.VUE_APP_BASE_API+ this.videoFileList[i].filePath,//视频地址
            // cover: "http://cdn.img.mtedu.com/images/assignment/day_3.jpg",  //播放器封面图
            autoplay: false,      // 是否自动播放
            width: "100%",       // 播放器宽度
            height: "550px",      // 播放器高度
            playsinline: true,
            "skinLayout": [
              {
                "name": "bigPlayButton",
                "align": "blabs",
                "x": 30,
                "y": 80
              },
              {
                "name": "H5Loading",
                "align": "cc"
              },
              {
                "name": "errorDisplay",
                "align": "tlabs",
                "x": 0,
                "y": 0
              },
              {
                "name": "infoDisplay"
              },
              {
                "name": "tooltip",
                "align": "blabs",
                "x": 0,
                "y": 56
              },
              {
                "name": "thumbnail"
              },
              {
                "name": "controlBar",
                "align": "blabs",
                "x": 0,
                "y": 0,
                "children": [
                  {
                    "name": "progress",
                    "align": "blabs",
                    "x": 0,
                    "y": 44
                  },
                  {
                    "name": "playButton",
                    "align": "tl",
                    "x": 15,
                    "y": 12
                  },
                  {
                    "name": "timeDisplay",
                    "align": "tl",
                    "x": 10,
                    "y": 7
                  },
                  {
                    "name": "fullScreenButton",
                    "align": "tr",
                    "x": 10,
                    "y": 12
                  },
                  {
                    "name": "volume",
                    "align": "tr",
                    "x": 5,
                    "y": 10
                  }
                ]
              }
            ]
          })
          //skinLayout设置只显示全屏和音量按钮
        }
      },
      getFileList() {
        this.fileList = [];
        listFile({'uuid': this.form.detailedUuid, 'fileTypeValue': 'file'}).then(response => {
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
      memberStatusFormat(row, column) {
        return this.selectDictLabel(this.memberStatusOptions, row.status);
      },
      getJoinMemberList() {
        this.memberList = [];
        this.memberLoading = true;
        listMember({"detailedUuid": this.form.detailedUuid}).then(response => {
          this.memberList = response.rows;
          this.memberLoading = false;
        });
      },
      getLeaveMemberList() {
        this.leaveMemberList = [];
        this.memberLoading = true;
        listMember({
          "detailedUuid": this.form.detailedUuid,
          "status": "5"
        }).then(response => {
          this.leaveMemberList = response.rows;
          this.memberLoading = false;
        });
      },
      openOrgMemberTransfer(type) {
        this.$refs.memberTransfer.open = true;
        this.$refs.memberTransfer.title = "选择活动参与人";
        this.$refs.memberTransfer.detailedUuid = this.form.detailedUuid;
        this.$refs.memberTransfer.type = type;
        this.$refs.memberTransfer.getPartyMemberSelect();
      },
      handleMemberUpdate(row, status) {
        row.status = status
        updateMember(row).then(response => {
          if (response.code === 200) {
            this.msgSuccess("修改成功");
            this.getJoinMemberList();
          } else {
            this.msgError(response.msg);
          }
        });
      },
      handleMembersUpdate() {
        let formData = new FormData();
        formData.append("status", this.memberStatus);
        formData.append("memberIds", this.memberIds.join(","));
        updateMembers(formData).then(response => {
          if (response.code === 200) {
            this.msgSuccess("修改成功");
            this.getJoinMemberList();
            this.getLeaveMemberList();
          } else {
            this.msgError(response.msg);
          }
        });

      },
      /** 删除按钮操作 */
      handleMemberDelete(row) {
        const memberIds = row.memberId || this.ids;
        this.$confirm('是否确认删除该参与人?', "警告", {
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
      },
      /**返回地址*/
      setVenue(val) {
        this.form.venue = '';
        this.form.venue = val;
        this.mapOpen = false;
      },
      /**显示地图 获取地址**/
      openMap() {
        this.mapOpen = !this.mapOpen
        this.$forceUpdate();
      },
      setMember(member) {
        this.form.partyMemberId = member.memberId;
        this.form.partyMemberName = member.memberName;
        this.$forceUpdate();
      },
      openMemberChoose() {
        this.$refs.partyMember.open = true;
        this.$refs.partyMember.title = "选择负责人";
        this.$refs.partyMember.queryParams.partyOrgId = this.form.partyOrgId;
      },
      detailedStatusFormat(row, column) {
        return this.selectDictLabel(this.detailedStatusOptions, row.status);
      },
      // 党员活动类型字典翻译
      activityTypeFormat(row, column) {
        return this.selectDictLabel(this.activityTypeOptions, row.djActivityPlan.activityType);
      },
      /** 对话框自适应高度 */
      getHeight() {
        this.bodyStyle.height = window.innerHeight - 281 + 'px';
      },
      /** 查询活动详情列表 */
      getList() {
        this.loading = true;
        listDetailedByParam(this.queryParams).then(response => {
          this.detailedList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.mapOpen = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          detailedId: undefined,
          detailedUuid: undefined,
          planUuid: undefined,
          cycle: undefined,
          partyMemberId: undefined,
          partyMemberName: undefined,
          venue: undefined,
          activityContent: undefined,
          planStartTime: undefined,
          planEndTime: undefined,
          activityPlanStartTime: undefined,
          activityPlanEndTime: undefined,
          actualStartTime: undefined,
          actualEndTime: undefined,
          recorder: undefined,
          mentors: undefined,
          presenter: undefined,
          speaker: undefined,
          allowComment: undefined,
          activityVoteId: undefined,
          activityExamId: undefined,
          status: "0",
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };

        this.djActivityPlan = {
          planId: undefined,
          planUuid: undefined,
          activitySources: undefined,
          activityTheme: undefined,
          activityType: undefined,
          conveneCycle: '2',
          cycleStartTime: undefined,
          cycleEndTime: undefined,
          cycleStartYear: undefined,
          cycleStartYearUnit: undefined,
          cycleEndYear: undefined,
          cycleEndYearUnit: undefined,
          actPlanRequirements: undefined,
          planPartyOrgIds: undefined,
          status: "0",
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };

        this.djPartyOrg = {
          partyOrgId: undefined,
          partyOrgUuid: undefined,
          parentId: undefined,
          ancestors: undefined,
          partyOrgName: undefined,
          partyOrgFullName: undefined,
          orgType: undefined,
          partyOrgType: undefined,
          buildTime: undefined,
          leader: undefined,
          phone: undefined,
          email: undefined,
          address: undefined,
          description: undefined,
          orderNum: undefined,
          status: "0",
          delFlag: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined
        };

        this.planFileList = [];
        this.picFileList = [];
        this.fileList = [];
        this.memberStatus = '1';
        this.activeTab = "1";
        this.mapOpen = false;

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
        this.memberIds = selection.map(item => item.memberId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.mapOpen = false;
        this.title = "添加活动详情";

      },
      handleSee(row) {
        this.reset();
        this.disabled = true;
        const detailedId = row.detailedId || this.ids
        getDetailed(detailedId).then(response => {
          this.form = response.data;
          this.form.partyMemberName = response.data.djPartyMember.memberName
          this.djActivityPlan = response.data.djActivityPlan;
          this.djPartyOrg = response.data.djPartyOrg;
          this.open = true;
          this.title = "活动管理";
        }).then(() => {
          this.changeByDetailedStatus();
          this.getPlanFileList();
          this.getJoinMemberList();
          this.getPicFileList();
          this.getVideoFileList();
          this.getFileList();
          this.$refs.activitySummary.disabled = this.disabled;
          this.$refs.activitySummary.init(this.form.detailedUuid);
          this.$refs.activityResolution.disabled = this.disabled;
          this.$refs.activityResolution.init(this.form.detailedUuid);

        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.disabled = false;
        const detailedId = row.detailedId || this.ids
        getDetailed(detailedId).then(response => {
          this.form = response.data;
          this.form.partyMemberName = response.data.djPartyMember.memberName
          this.djActivityPlan = response.data.djActivityPlan;
          this.djPartyOrg = response.data.djPartyOrg;
          this.open = true;
          this.title = "活动管理";
        }).then(() => {
          this.changeByDetailedStatus();
          this.getPlanFileList();
          this.getJoinMemberList();
          this.getPicFileList();
          this.getFileList();
          this.getVideoFileList();
          this.$refs.activitySummary.disabled = this.disabled;
          this.$refs.activitySummary.init(this.form.detailedUuid);
          this.$refs.activityResolution.disabled = this.disabled;
          this.$refs.activityResolution.init(this.form.detailedUuid);
          this.$refs.activitySuggestions.queryParams.detailedUuid= this.form.detailedUuid;
          this.$refs.activityExperience.queryParams.detailedUuid= this.form.detailedUuid;

        });
      },
      changeByDetailedStatus() {
        this.actualRequired = false;
        this.disabled1 = false;
        this.disabled2 = false;
        this.disabled3 = false;
        this.disabled4 = false;
        this.disabled5 = false;
        this.disabled6 = false;
        this.activeStep = undefined;
        switch (this.form.status) {
          case "1":
            this.activeStep = 1;
            this.disabled1 = true;
            break;
          case "2":
            this.activeStep = 2;
            this.actualRequired = true;
            this.disabled2 = true;
            break;
          case "3":
            this.activeStep = 2;
            this.actualRequired = true;
            this.disabled3 = true;
            break;
          case "4":
            this.activeStep = 4;
            this.disabled4 = true;
            break;
          case "5":
            this.activeStep = 3;
            this.disabled5 = true;
            break;
          case "6":
            this.activeStep = 3;
            this.disabled6 = true;
            break;
          default:
            break;
        }
      },
      /** 提交按钮 */
      submitForm: function (status) {
        if(status!='6'){
          this.$refs["form"].validate(valid => {
            if (valid) {
              this.form.status = status;
              if (this.form.detailedId != undefined) {
                updateDetailed(this.form).then(response => {
                  if (response.code === 200) {
                    this.msgSuccess("修改成功");
                    this.open = false;
                    this.getList();
                  } else {
                    this.msgError(response.msg);
                  }
                });
              } else {
                addDetailed(this.form).then(response => {
                  if (response.code === 200) {
                    this.msgSuccess("新增成功");
                    this.open = false;
                    this.getList();
                  } else {
                    this.msgError(response.msg);
                  }
                });
              }
            } else {
              setTimeout(() => {
                var isError = document.getElementsByClassName("is-error");
                isError[0].querySelector('input').focus();
              }, 100);
              return false;
            }
          });
        }else {
          this.form.status = status;
          updateDetailed(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            } else {
              this.msgError(response.msg);
            }
          });
        }

      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const detailedIds = row.detailedId || this.ids;
        this.$confirm('是否确认删除活动详情编号为"' + detailedIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delDetailed(detailedIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有活动详情数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportDetailed(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      handleExportArchives(row) {
        downLoadZip("/activity/detailed/exportArchives?detailedId=" + row.detailedId);
      },
      handleOpenSupervise(row){
        this.$refs.activitySupervise.init(row.detailedUuid);
      },
      getUser() {
        getUserProfile().then(response => {
          this.admin = false;
          this.user = response.data;
          let roles = this.user.roles;
          if(roles){
            for(let i=0;i<roles.length;i++){
              if(roles[i].roleId == 7){
                this.admin = true;
              }
            }
          }
        });
      },
      /**行颜色*/
      tableRowClassName({row, rowIndex}) {
        if(row.status != '5' && row.status != '6'){  //未归档  && 已取消
          if(this.pathType=="1"){  //活动详情
            let planEndTime = new Date(row.planEndTime.replace(/-/g, "/"));
            if(planEndTime.getTime() <= Date.now() ){
              return 'danger-row';
            }
            if(planEndTime.getTime()  <= Date.now() + 7 * 24 * 60 * 60 * 1000){
              return 'warn-row';
            }
          }
          if(this.pathType=="2"){  //活动督办
            let endTime;
            if(row.activityPlanEndTime){
              endTime= new Date(row.activityPlanEndTime.replace(/-/g, "/"));
            }else{
              endTime= new Date(row.planEndTime.replace(/-/g, "/"));
            }
            if(endTime.getTime() <= Date.now() ){
              return 'danger-row';
            }
            if(endTime.getTime()  <= Date.now() + 7 * 24 * 60 * 60 * 1000){
              return 'warn-row';
            }
          }
        }
        return '';
      },
      handleAddVideo(){
        this.$refs.bigFileUpload.init("活动视频上传");
      },
      handleDeleteVideo(video){
        this.$confirm('是否确认删除该视频?', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "info"
        }).then(() => {
          delFile(video.id).then(response => {
            this.msgSuccess(response.msg);
            this.getVideoFileList();
          });
        }).catch(function () {
        });
      },
      uploadVideoFile(filePath,fileName){
        var sysFile={
          uuid:undefined,
          fileType:undefined,
          fileTypeValue:undefined,
          filePath:undefined,
          fileName:undefined,
          };
        sysFile.uuid =this.form.detailedUuid;
        sysFile.fileType ="activityDetailed";
        sysFile.fileTypeValue = "video";
        sysFile.filePath = filePath ;
        sysFile.fileName = fileName

        addFile(sysFile).then(response => {
          if (response.code === 200) {
            this.getVideoFileList();
          } else {
            this.msgError(response.msg);
          }
        });
      },
      setVideoId(video){
        return "video-"+video.id;
      },
      setFileNameTip(file){
        return file.name;
      }
    }
  };
</script>
