package com.itheima.stock.controller;

import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.service.UserService;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ClassName: UserController
 * Package: com.itheima.stock.controller
 * Description:
 *
 * @Author starry
 * @Create 2024/2/22 17:12
 * @Version 1.0
 * 定义用户web层接口资源bean
 */
@RestController
@RequestMapping("/api")
@Api(value = "用户认证相关接口定义",tags = "用户功能-用户登录功能")
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * 根据用户名称查询用户信息
     * @param name
     * @return
     */
    @GetMapping("/user/{userName}")
    @ApiOperation(value = "根据用户名查询用户信息",notes = "用户信息查询",response = SysUser.class)
    @ApiImplicitParam(paramType = "path",name = "userName",value = "用户名",required = true)
    public SysUser getUserByUserName(@PathVariable("userName") String name){
        return userService.findByUserName(name);
    }

    /**
     * 用户登录功能实现
     * @param vo
     * @return
     */
    @ApiOperation(value = "用户登录功能",notes = "用户登录",response = R.class)
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){
        R<LoginRespVo> r=this.userService.login(vo);
        return r;
    }

    /**
     * 生成图片验证码功能
     * @return
     */
    @GetMapping("/captcha")
    @ApiOperation(value = "验证码生成功能",response = R.class)
    public R<Map> getCaptchaCode(){
        return userService.getCaptchaCode();
    }
}
