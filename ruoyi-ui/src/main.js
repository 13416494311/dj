import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import permission from './directive/permission'

import './assets/icons' // icon
import './permission' // permission control
import { getDicts } from "@/api/system/dict/data";
import { getConfigKey } from "@/api/system/config";
import { parseTime, resetForm, addDateRange, selectDictLabel,
  download, handleTree,treeInitData,getDateYearSub,getNowFormatDate,getTreeNode,
  uuid,cityTreeData,getBasePath,getDaysBetween } from "@/utils/ruoyi";
import { getDifDate } from "@/utils/workHours";
import Pagination from "@/components/Pagination";
import Vue2OrgTree from 'vue2-org-tree';
import 'vue2-org-tree/src/styles/org-tree.less';
import VueAMap from 'vue-amap'
import uploader from 'vue-simple-uploader'
import echarts from "echarts";
import VueLazyload from 'vue-lazyload'  // 引入这个懒加载插件

Vue.use(VueLazyload) // 直接使用
Vue.use(uploader)
Vue.use(VueAMap)
VueAMap.initAMapApiLoader({
  key: '8516d119353dc5890b59169dd05e28d6',
  plugin: ['AMap.Scale', 'AMap.OverView', 'AMap.ToolBar', 'AMap.MapType', 'AMap.PlaceSearch', 'AMap.Geolocation', 'AMap.Geocoder'],
  v: '1.4.4',
  uiVersion: '1.0'})

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree
Vue.prototype.treeInitData = treeInitData
Vue.prototype.getNowFormatDate = getNowFormatDate
Vue.prototype.getDateYearSub = getDateYearSub
Vue.prototype.getTreeNode = getTreeNode
Vue.prototype.uuid = uuid
Vue.prototype.$echarts = echarts;
Vue.prototype.cityTreeData = cityTreeData
Vue.prototype.getBasePath = getBasePath
Vue.prototype.getDifDate = getDifDate
Vue.prototype.getDaysBetween = getDaysBetween

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}

// 全局组件挂载
Vue.component('Pagination', Pagination)

Vue.use(Vue2OrgTree)

Vue.use(permission)

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'mini' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
