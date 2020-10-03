package com.apicloud.sdk.api;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.apicloud.sdk.utils.HttpUtils;

/**
 * 操作数据云api
 * @author wangjinzhen
 * @time 21/05/2015
 * @version 0.0.1
 */
public class Resource{

	//headers参数
	private Map<String,String> headers = new HashMap<String,String>();
	
	private String domain = "https://d.apicloud.com";
	
	/**
	 * @param appId
	 * @param appKey
	 * @param domain 为空或者null为默认https
	 */
	public Resource(String appId,String appKey,String domain){
		
		if(null!=domain&&!"".equals(domain)){
			this.domain = domain;
		}
		headers.put("X-APICloud-AppId", appId);
		headers.put("X-APICloud-AppKey", HttpUtils.encrypt(appId,appKey,"SHA-1"));
	}
	
	@SuppressWarnings("unused")
	private Resource() {}

	/** 对象 ------begin------ **/
	
	/**
	 * 创建对象
	 * @param object 对象名称
	 * @param property 对象所具有的属性
	 * @return
	 */
	public JSONObject createObject(String object,JSONObject property){
		
		//校验是否传递参数
		if(property==null){
			property = new JSONObject();
		}
		
		handleFile(property);
		
		headers.put("Content-Type", "application/json");
		
		String url = domain+"/mcm/api/"+object;
		
		return HttpUtils.doPost(url, headers, null,property.toJSONString());
	}
	
	/**
	 * 根据id获取对象
	 * @param object 对象名称
	 * @param id 对象Id
	 * @return
	 */
	public JSONObject getObject(String object,String id){
		
		String url = domain+"/mcm/api/"+object+"/"+id;
		
		return HttpUtils.doGet(url, headers);
	}
	
	/**
	 * 获取所有对象
	 * @param object 对象名称
	 * @return
	 */
	public JSONObject getObjects(String object){
		
		String url = domain+"/mcm/api/"+object;
		
		return HttpUtils.doGet(url, headers);
	}
	
	/**
	 * 更新对象
	 * @param object
	 * @param id
	 * @param property
	 * @return
	 */
	public JSONObject updateObject(String object,String id,JSONObject property){
		
		headers.put("Content-Type", "application/json");

		if(property==null||property.size()==0){
			return JSONObject.parseObject("{status:0,msg:\"请至少更新一个字段\"}");
		}
		
		String url = domain+"/mcm/api/"+object+"/"+id;
		
		return HttpUtils.doPut(url,headers,property.toJSONString());
	}
	
	/**
	 * 删除对象
	 * @param object 对象名称
	 * @param id 对象Id
	 * @return
	 */
	public JSONObject deleteObject(String object,String id){
		
		String url = domain+"/mcm/api/"+object+"/"+id;
		
		return HttpUtils.doDelete(url, headers);
	}
	
	/**
	 * 统计对象数量
	 * @param object 对象名称
	 * @return
	 */
	public JSONObject getObjectCount(String object){
		
		String url = domain+"/mcm/api/"+object+"/count";
		
		return HttpUtils.doGet(url, headers);
	}
	
	/**
	 * 判断对象是否存在
	 * @param object 对象名称
	 * @param id 对象Id
	 * @return
	 */
	public JSONObject checkObjectExists(String object,String id){
		
		String url = domain+"/mcm/api/"+object+"/"+id+"/exists";
		
		return HttpUtils.doGet(url, headers);
	}
	/** 对象 ------end------ **/

	/** Relation对象 ------begin------ **/
	
	/**
	 * 获取关联对象
	 * @param object
	 * @param id
	 * @param relationObject
	 * @return
	 */
	public JSONObject getRelationObject(String object,String id,String relationObject){
		
		String url = domain+"/mcm/api/"+object+"/"+id+"/"+relationObject;
		
		return HttpUtils.doGet(url, headers);	
	}
	
	/**
	 * 创建关联对象
	 * @param object
	 * @param id
	 * @param relationObject
	 * @return
	 */
	public JSONObject createRelationObject(String object,String id,String relationObject,JSONObject property){
		
		//处理文件参数
		handleFile(property);
		
		String url = domain+"/mcm/api/"+object+"/"+id+"/"+relationObject;
		
		Map<String,String> propertyMap = new HashMap<String,String>();
		Set<String> propertySet = property.keySet();
		Iterator<String> iterProperty = propertySet.iterator();
		while(iterProperty.hasNext()){
			String key = iterProperty.next();
			propertyMap.put(key, property.getString(key));
		}
		
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		
		return HttpUtils.doPost(url, headers,propertyMap,"");	
	}
	
	/**
	 * 统计关联对象数量
	 * @param object
	 * @param id
	 * @param relationObject
	 * @return
	 */
	public JSONObject getRelationObjectCount(String object,String id,String relationObject){
		
		String url = domain+"/mcm/api/"+object+"/"+id+"/"+relationObject+"/count";
		
		return HttpUtils.doGet(url, headers);	
	}
	
	/**
	 * 删除所有关联对象
	 * @param object
	 * @param id
	 * @param relationObject
	 * @return
	 */
	public JSONObject deleteRelationObject(String object,String id,String relationObject){
		
		String url = domain+"/mcm/api/"+object+"/"+id+"/"+relationObject;
		
		return HttpUtils.doDelete(url, headers);	
	}
	/** Relation对象 ------end------ **/

	/** 用户 ------begin------ **/
	
	/**
	 * 创建用户
	 * @param property
	 * @return
	 */
	public JSONObject createUser(JSONObject property){
		
		//校验是否传递参数
		if(property==null){
			return JSONObject.parseObject("{status:0,msg:\"请传递参数\"}");
		}
		
		String userName = property.getString("username");
		if(null==userName||"".equals(userName)){
			return JSONObject.parseObject("{status:0,msg:\"姓名不能为空\"}");
		}
		
		String password = property.getString("password");
		if(null==password||"".equals(password)){
			return JSONObject.parseObject("{status:0,msg:\"密码不能为空\"}");
		}
		
		//处理文件参数
		handleFile(property);
		
		headers.put("Content-Type", "application/json");
		
		String url = domain+"/mcm/api/user";
		
		return HttpUtils.doPost(url, headers, null,property.toJSONString());
	}
	
	/**
	 * 用户登录
	 * @param userName
	 * @param password
	 * @return
	 */
	public JSONObject userLogin(String userName,String passWord){
		
		headers.put("Content-Type", "application/json");
		
		if(null==userName||"".equals(userName)){
			return JSONObject.parseObject("{status:0,msg:\"姓名不能为空\"}");
		}
		
		if(null==passWord||"".equals(passWord)){
			return JSONObject.parseObject("{status:0,msg:\"密码不能为空\"}");
		}
		
		String url = domain+"/mcm/api/user/login";
		
		JSONObject property = new JSONObject();
		property.put("username", userName);
		property.put("password", passWord);
		
		JSONObject returnJson = HttpUtils.doPost(url, headers, null,property.toJSONString());
		
		handleAuthorization(returnJson);
		
		return returnJson;
	}
	
	/**
	 * 请求验证Email
	 * @param property
	 * @return
	 */
	public JSONObject verifyEmail(JSONObject property){
		
		headers.put("Content-Type", "application/json");
		
		//校验是否传递参数
		if(property==null){
			return JSONObject.parseObject("{status:0,msg:\"请传递参数\"}");
		}
		
		String userName = property.getString("username");
		if(null==userName||"".equals(userName)){
			return JSONObject.parseObject("{status:0,msg:\"姓名不能为空\"}");
		}
		
		String email = property.getString("email");
		if(null==email||"".equals(email)){
			return JSONObject.parseObject("{status:0,msg:\"邮箱不能为空\"}");
		}
		
		String url = domain+"/mcm/api/user/verifyEmail";
		
		return HttpUtils.doPost(url, headers, null,property.toJSONString());
	}
	
	/**
	 * 密码重置
	 * @param property
	 * @return
	 */
	public JSONObject resetRequest(JSONObject property){
		
		headers.put("Content-Type", "application/json");
		
		//校验是否传递参数
		if(property==null){
			return JSONObject.parseObject("{status:0,msg:\"请传递参数\"}");
		}
		
		String userName = property.getString("username");
		if(null==userName||"".equals(userName)){
			return JSONObject.parseObject("{status:0,msg:\"姓名不能为空\"}");
		}
		
		String email = property.getString("email");
		if(null==email||"".equals(email)){
			return JSONObject.parseObject("{status:0,msg:\"邮箱不能为空\"}");
		}
		
		String url = domain+"/mcm/api/user/resetRequest";
		
		return HttpUtils.doPost(url, headers, null,property.toJSONString());
	}
	
	/**
	 * 获取用户
	 * @param authorization login 返回的id
	 * @param userId
	 * @return
	 */
	public JSONObject getUser(String userId){
		
		headers.put("Content-Type", "application/json");

		String url = domain+"/mcm/api/user/"+userId;
		
		return HttpUtils.doGet(url, headers);
	}
	
	/**
	 * 更新用户
	 * @param authorization
	 * @param userId
	 * @param property 需要更新的属性
	 * @return
	 */
	public JSONObject updateUser(String userId,JSONObject property){
		
		headers.put("Content-Type", "application/json");

		if(null==property){
			property = new JSONObject();
		}
		
		String url = domain+"/mcm/api/user/"+userId;
		
		return HttpUtils.doPut(url, headers, property.toJSONString());
	}
	
	/**
	 * 删除用户
	 * @param authorization
	 * @param userId
	 * @return
	 */
	public JSONObject deleteUser(String userId){
		
		headers.put("Content-Type", "application/json");
		
		String url = domain+"/mcm/api/user/"+userId;
		
		return HttpUtils.doDelete(url, headers);
	}
	
	/**
	 * 登出
	 * @param authorization
	 * @return
	 */
	public JSONObject loginOut(){
		
		headers.put("Content-Type", "application/json");
		
		String url = domain+"/mcm/api/user/logout";
		
		return HttpUtils.doPost(url, headers, null, "");
	}
	
	/** 用户 ------end------ **/
	
	/** 角色 ------end------ **/
	
	/**
	 * 创建角色
	 * @param property
	 * @return
	 */
	public JSONObject createRole(JSONObject property){
		
		//校验是否传递参数
		if(property==null){
			return JSONObject.parseObject("{status:0,msg:\"请传递参数\"}");
		}
		
		//处理文件参数
		handleFile(property);
		
		headers.put("Content-Type", "application/json");
		
		String url = domain+"/mcm/api/role";
		
		return HttpUtils.doPost(url, headers, null,property.toJSONString());
	}
	
	/**
	 * 根据Id获取角色
	 * @param id
	 * @return
	 */
	public JSONObject getRole(String id){
		
		String url = domain+"/mcm/api/role/"+id;
		
		return HttpUtils.doGet(url, headers);	
	}
	
	/**
	 * 根据id更新角色
	 * @param id
	 * @param property
	 * @return
	 */
	public JSONObject updateRole(String id,JSONObject property){
		
		//校验是否传递参数
		if(property==null){
			return JSONObject.parseObject("{status:0,msg:\"请传递参数\"}");
		}
		
		headers.put("Content-Type", "application/json");
		
		String url = domain+"/mcm/api/role/"+id;
		
		return HttpUtils.doPut(url, headers,property.toJSONString());
	}
	
	/**
	 * 根据Id删除角色
	 * @param id
	 * @return
	 */
	public JSONObject deleteRole(String id){
		
		String url = domain+"/mcm/api/role/"+id;
		
		return HttpUtils.doDelete(url, headers);
	}
	/** 角色 ------end------ **/
		
	/** 批量操作------begin------ **/
	
	/**
	 * @param prams
	 * @return
	 */
	public JSONObject batch(JSONObject params){
		
		//校验是否传递参数
		if(params==null){
			return JSONObject.parseObject("{status:0,msg:\"请传递参数\"}");
		}
		
		String url = domain+"/mcm/api/batch";
		headers.put("Content-Type", "application/json");

		return HttpUtils.doPost(url, headers, null, params.toJSONString());
	}
	
	/** 批量操作------end------ **/

	/** 文件(file)------begin------ **/
	
	/**
	 * 文件上传
	 * @param fileName
	 * @return
	 */
	public JSONObject upload(String filePath){
		
		if(null==filePath||"".equals(filePath)){
			return JSONObject.parseObject("{status:0,msg:\"路径不能为空\"}");
		}
		
		String url = domain+"/mcm/api/file";
		
		return HttpUtils.doUpload(url, filePath , headers, new HashMap<String,String>());
	}
	
	/** 文件(file)------end------ **/
	
	/** 更新操作符------begin------ **/
	
	/**
	 * @param id
	 * @param params
	 * @return
	 */
	public JSONObject updateModel(String object,String id,JSONObject params){
		
		//校验是否传递参数
		if(params==null){
			return JSONObject.parseObject("{status:0,msg:\"请传递参数\"}");
		}
		
		String url = domain+"/mcm/api/"+object+"/"+id;
		
		return HttpUtils.doPut(url, headers, params.toJSONString());
	}
	
	/** 更新操作符------end------ **/
	
	/** 条件过滤------begin------ **/
	
	/**
	 * 按照条件过滤
	 * @param object
	 * @param filter
	 * @return
	 */
	public JSONObject doFilterSearch(String object,String filter){
		
		if(null==object||"".equals(object)){
			return JSONObject.parseObject("{status:0,msg:\"请确定查询对象\"}");
		}
		
		String url = domain+"/mcm/api/"+object+"?filter=";
		try {
			url += URLEncoder.encode(filter, "utf-8");
		} catch (UnsupportedEncodingException e) {
			
		}
		return HttpUtils.doGet(url, headers);
	}
	
	/** 条件过滤------end------ **/
	
	/** 安全相关------begin------**/
	 
	/**
	 * 设置权限验证码
	 * @param authorization
	 */
	public void setAuthorization(String authorization){
		if(null!=authorization&&!"".equals(authorization)){
			headers.put("authorization", authorization);
		}
	}
	
	/** 安全相关------end------**/
	/**
	 * 查看参数中是否含有file对象，如果有的话，先上传，在将返回的信息替换掉原来的file对象
	 * @param property
	 */
	private void handleFile(JSONObject property) {
		//查看values中是否有file对象
		Set<String> keySet = property.keySet();
		Iterator<String> keyIter = keySet.iterator();
		while(keyIter.hasNext()){
			String key = keyIter.next();
			Object obj = property.get(key);
			if(null!=obj&&obj instanceof File){
				File file = (File)obj;
				if(file.exists()&&file.isFile()){
					JSONObject fileJson = upload(file.getPath());
					property.put(key, fileJson.toJSONString());
				}
			}
		}
	}
	
	/**
	 * @param returnJson
	 * 处理login返回来的authorization,登录以后缓存用户的校验码
	 */
	private void handleAuthorization(JSONObject returnJson) {
		String key = returnJson.getString("id");
		if(null!=key&&!"".equals(key)){
			headers.put("authorization", key);
		}
	}
}
