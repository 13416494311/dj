package com.apicloud.sdk.api;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.apicloud.sdk.utils.HttpUtils;
import com.ruoyi.framework.config.AppConfig;

/**
 * 推送云API
 * @author wangjinzhen
 * @time 25/05/2015
 * @version 0.0.1
 */
public class Push {

	//headers参数
	private static Map<String,String> headers = new HashMap<String,String>();

	private static String domain = "https://p.apicloud.com";

    //private static String appId = "A6044929225267";
    private static String appId = AppConfig.getAppId();

    //private static String appKey = "B535493D-6C7B-487A-CEF0-20F08DD32A74";
    private static String appKey =  AppConfig.getAppKey();

	//params参数
	private static Map<String,String> params = new HashMap<String,String>();



	/**
	 * 向某个推送组所有的成员推送消息
	 * @param title 消息标题
	 * @param content 消息内容
	 * @param type 消息类型，1:消息 2:通知
	 * @param platform 0:全部平台，1：ios, 2：android
	 * @param groupName 推送组名，多个组用英文逗号隔开.默认:全部组。eg.group1,group2
	 * @param userIds 推送用户id, 多个用户用英文逗号分隔，eg. user1,user2
	 * @return
	 */
	public static void pushMessage(String title,String content,int type,int platform,String groupName,String userIds){

		//设置参数
		params.clear();
		params.put("title", title);
		params.put("content", content);
		params.put("type", type+"");

		params.put("platform", platform+"");
		params.put("groupName", groupName);
		params.put("userIds", userIds);
        headers.put("X-APICloud-AppId", appId);
        headers.put("X-APICloud-AppKey", HttpUtils.encrypt(appId,appKey,"SHA-1"));
		String url = domain+"/api/push/message";
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        new Thread(()->{
            HttpUtils.doPost(url, headers, params, "");
        }).start();

	}
}
