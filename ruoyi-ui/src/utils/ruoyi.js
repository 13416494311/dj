/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */

const baseURL = process.env.VUE_APP_BASE_API

// 日期格式化
export function parseTime(time, pattern) {
	if (arguments.length === 0 || !time) {
		return null
	}
	const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
	let date
	if (typeof time === 'object') {
		date = time
	} else {
		if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
			time = parseInt(time)
		} else if (typeof time === 'string') {
			time = time.replace(new RegExp(/-/gm), '/');
		}
		if ((typeof time === 'number') && (time.toString().length === 10)) {
			time = time * 1000
		}
		date = new Date(time)
	}
	const formatObj = {
		y: date.getFullYear(),
		m: date.getMonth() + 1,
		d: date.getDate(),
		h: date.getHours(),
		i: date.getMinutes(),
		s: date.getSeconds(),
		a: date.getDay()
	}
	const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
		let value = formatObj[key]
		// Note: getDay() returns 0 on Sunday
		if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
		if (result.length > 0 && value < 10) {
			value = '0' + value
		}
		return value || 0
	})
	return time_str
}

// 表单重置
export function resetForm(refName) {
	if (this.$refs[refName]) {
		this.$refs[refName].resetFields();
	}
}

// 添加日期范围
export function addDateRange(params, dateRange) {
	var search = params;
	search.beginTime = "";
	search.endTime = "";
	if (null != dateRange && '' != dateRange) {
		search.beginTime = this.dateRange[0];
		search.endTime = this.dateRange[1];
	}
	return search;
}

// 回显数据字典
export function selectDictLabel(datas, value) {
	var actions = [];
	Object.keys(datas).map((key) => {
		if (datas[key].dictValue == ('' + value)) {
			actions.push(datas[key].dictLabel);
			return false;
		}
	})
	return actions.join('');
}

// 通用下载方法
export function download(fileName) {
	window.location.href = baseURL + "/common/download?fileName=" + encodeURI(fileName) + "&delete=" + true;
}

// 字符串格式化(%s )
export function sprintf(str) {
	var args = arguments, flag = true, i = 1;
	str = str.replace(/%s/g, function () {
		var arg = args[i++];
		if (typeof arg === 'undefined') {
			flag = false;
			return '';
		}
		return arg;
	});
	return flag ? str : '';
}

// 转换字符串，undefined,null等转化为""
export function praseStrEmpty(str) {
    if (!str || str == "undefined" || str == "null") {
        return "";
    }
    return str;
}

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 * @param {*} rootId 根Id 默认 0
 */
export function handleTree(data, id, parentId, children, rootId) {
	id = id || 'id'
	parentId = parentId || 'parentId'
	children = children || 'children'
	rootId = rootId || 0
	//对源数据深度克隆
	const cloneData = JSON.parse(JSON.stringify(data))
	//循环所有项
	const treeData =  cloneData.filter(father => {
	  let branchArr = cloneData.filter(child => {
		//返回每一项的子级数组
		return father[id] === child[parentId]
	  });
	  branchArr.length > 0 ? father.children = branchArr : '';
	  //返回第一层
	  return father[parentId] === rootId;
	});
	return treeData != '' ? treeData : data;
  }

export function treeInitData(data){
  // 循环遍历json数据
  for (var i = 0; i < data.length; i++) {
    if (data[i].children.length < 1) {
      // children若为空数组，则将children设为undefined
      data[i].children = undefined;
    } else {
      // children若不为空数组，则继续 递归调用 本方法
      this.treeInitData(data[i].children);
    }
  }
  return data;
}

//获取当前时间
export function getNowFormatDate() {
  let date = new Date();
  let seperator1 = "-";
  let year = date.getFullYear();
  let month = date.getMonth() + 1;
  let strDate = date.getDate();
  if (month >= 1 && month <= 9) {
    month = "0" + month;
  }
  if (strDate >= 0 && strDate <= 9) {
    strDate = "0" + strDate;
  }
  let currentdate = year + seperator1 + month + seperator1 + strDate ;

  let seperator2 = ":";
  let hour = date.getHours();
  let minute = date.getMinutes() ;
  let second = date.getSeconds();
  let currenttime = hour + seperator2 + minute + seperator2 + second ;
  return currentdate + " "+currenttime;
}

//计算年份
export function getDateYearSub(startDateStr, endDateStr) {
  let day = 24 * 60 * 60 *1000;
  let sDate = new Date(Date.parse(startDateStr.replace(/-/g, "/")));
  let eDate = new Date(Date.parse(endDateStr.replace(/-/g, "/")));
  //得到前一天(算头不算尾)
  sDate = new Date(sDate.getTime() - day);
  //获得各自的年、月、日
  let sY  = sDate.getFullYear();
  let sM  = sDate.getMonth()+1;
  let sD  = sDate.getDate();
  let eY  = eDate.getFullYear();
  let eM  = eDate.getMonth()+1;
  let eD  = eDate.getDate();

  if(eY > sY && sM == eM && sD == eD) {
    return eY - sY;
  } else {
    return 0;
  }
}

export function getTreeNode(json,nodeId){
  let node = null;
  //第一层 root 深度遍历整个JSON
  for (let i = 0; i < json.length; i++) {
    let obj = json[i];
    //没有就下一个
    if (!obj || !obj.id) {
      continue;
    }
    //有节点就开始找，一直递归下去
    if (obj.id == Number(nodeId)) {
      //找到了与nodeId匹配的节点，结束递归
      node = obj;
      return node;
    }
    if (obj.children) {
      //递归往下找
      let node = getTreeNode(obj.children, nodeId);
      if(node){
        return node;
      }
    }
  }
}

export function uuid(){
  var s = [];
  var hexDigits = "0123456789abcdef";
  for (var i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }
  s[14] = "4";
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
  s[8] = s[13] = s[18] = s[23] = "-";

  let uuidA = s.join("");
  return uuidA;
}


export function cityTreeData(data,level){
  // 循环遍历json数据
  for (var i = 0; i < data.length; i++) {
    if (data[i].children.length < 1||level==2) {
      // children若为空数组，则将children设为undefined
      //data[i].children = undefined;
      delete data[i].children;
    } else {
      // children若不为空数组，则继续 递归调用 本方法
      this.cityTreeData(data[i].children,2);
    }
  }
  return data;
}

export function getBasePath(){
  let url = window.location.href;
  let path = this.$route.path;
  let basePath =url.replace(path,'')
  return basePath
}

/**
 * 计算两个日期之间的天数
 * @param dateString1  开始日期 yyyy-MM-dd
 * @param dateString2  结束日期 yyyy-MM-dd
 * @returns {number} 如果日期相同 返回一天 开始日期大于结束日期，返回0
 */
export function  getDaysBetween(dateString1,dateString2){
  var  startDate = Date.parse(dateString1);
  var  endDate = Date.parse(dateString2);
  if (startDate>endDate){
    return 0;
  }
  if (startDate==endDate){
    return 1;
  }
  var days=(endDate - startDate)/(1*24*60*60*1000);
  return  days;
}

