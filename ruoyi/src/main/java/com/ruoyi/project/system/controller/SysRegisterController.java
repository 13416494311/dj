package com.ruoyi.project.system.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/system/register")
public class SysRegisterController {

    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    @Autowired
    private RedisCache redisCache;

    /**
     * 手机app 党员注册
     */
    @Log(title = "党员注册", businessType = BusinessType.INSERT)
    @PostMapping("/addForApp")
    public AjaxResult register(@RequestBody Map<String,Object> params)
    {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + params.get("uuid").toString() ;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if(!params.get("code").toString().equalsIgnoreCase(captcha)){
            return AjaxResult.success("验证码错误");
        }
        String password =  params.get("password").toString();
        //党员变更表
        DjPartyMember djPartyMember = JSON.parseObject(JSON.toJSONString(params), DjPartyMember.class);
        return AjaxResult.success(djPartyMemberService.insertDjPartyMemberForApp(djPartyMember,password));
    }
}
