package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import com.atguigu.util.ValidateCodeUtils;
import com.atguigu.vo.LoginVo;
import com.atguigu.vo.RegisterVo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//所有的请求都是异步请求
@RestController
@RequestMapping("/userInfo")
public class UserInfoController extends BaseController {
    @Reference
    private UserInfoService userInfoService;

    /**
     * 发送验证码功能
     *
     * @param phone
     * @param request
     * @return
     */
    //查看页面发现，请求的地址是/userInfo/senCode
    @RequestMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable String phone, HttpServletRequest request) {
        //使用工具类生成一个随机验证码，并将验证码放到Request域里面
        String code = ValidateCodeUtils.generateValidateCode4String(6);
        //查看页面，验证码的key是code
        request.getSession().setAttribute("code", code);
        return Result.ok(code);
    }

    //注册用户，注册信息已经封装成了vo
    @RequestMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo, HttpServletRequest request) {
        //获取手机号，验证码，昵称，密码
        String nickName = registerVo.getNickName();
        String code = registerVo.getCode();
        String password = registerVo.getPassword();
        String phone = registerVo.getPhone();
        //验空，使用Spring的String工具类
        if (StringUtils.isEmpty(nickName) ||
                StringUtils.isEmpty(phone) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        //判断验证码
        String currentCode = (String) request.getSession().getAttribute("code");
        if (!code.equals(currentCode)) {
            //  CODE_ERROR(210, "验证码不正确"),
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        //获取UserInfo实体，因为注册是对于userindo操作的
        UserInfo userInfo = userInfoService.getUserByPhone(phone);
        if (null != userInfo) {
            // 统一返回结果状态信息的枚举类，PHONE_REGISTER_ERROR(210, "手机号已注册"),
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        userInfo = new UserInfo();
        userInfo.setNickName(nickName);
        userInfo.setPhone(phone);
        //使用MD5工具类加密密码
        userInfo.setPassword(MD5.encrypt(password));
        userInfo.setStatus(1);
        userInfoService.insert(userInfo);
        return Result.ok();
    }

    /**
     * 登录的逻辑判断，需要查询数据库里面的user_info表，页面的是表单
     *
     * @param loginVo
     * @param request
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo, HttpServletRequest request) {
        //@RequestBody LoginVo loginVo 表单的请求参数直接封装
        String phone = loginVo.getPhone();
        String password = loginVo.getPassword();
        //通过表单提交的手机号去数据里面找user_info对象,注入UserInfoService
        UserInfo userInfo = userInfoService.getUserByPhone(phone);
        //非空判断
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);

        }
        if (userInfo == null) {
            //ACCOUNT_ERROR(210, "账号不正确"),
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }
        //校验密码
        if (!MD5.encrypt(password).equals(userInfo.getPassword())) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        }

        //校验是否被禁用,状态（0：锁定 1：正常）    ACCOUNT_LOCK_ERROR(210, "该账户已被锁定"),
        if (userInfo.getStatus() == 0) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }
        //将Userinfo放到Request域里面
        request.getSession().setAttribute("USER", userInfo);
        //用户登录的页面回显
        Map<String, Object> map = new HashMap<>();
        map.put("nickName", userInfo.getNickName());
        return Result.ok(map);
    }

    /**
     * 退出登录，移除session就行了
     *
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().removeAttribute("USER");
        return Result.ok();
    }
}
