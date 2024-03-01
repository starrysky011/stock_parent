package com.itheima.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.itheima.stock.constant.StockConstant;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.service.UserService;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: UserServiceImpl
 * Package: com.itheima.stock.service.impl
 * Description:
 *
 * @Author starry
 * @Create 2024/2/22 17:07
 * @Version 1.0
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 分布式环境生成唯一id
     */
    @Autowired
    private IdWorker idWorker;

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    @Override
    public SysUser findByUserName(String userName) {
        return sysUserMapper.findUserInfoUserName(userName);
    }

    /**
     * 用户登录
     * @param vo
     * @return
     */
    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
        //判断参数是否合法
        if(vo==null || StringUtils.isBlank(vo.getUsername()) || StringUtils.isBlank(vo.getPassword())){
            return R.error(ResponseCode.DATA_ERROR);
        }
        //校验码和sessionId是否有效
        if(StringUtils.isBlank(vo.getCode()) || StringUtils.isBlank(vo.getSessionId())){
            return R.error(ResponseCode.DATA_ERROR);
        }

        //3.根据Rkey从redis中获取缓存的校验码
        String rCode =(String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX + vo.getSessionId());
        //判断获取的验证码是否存在，以及是否与输入的验证码相同
        if (StringUtils.isBlank(rCode) || !rCode.equalsIgnoreCase(vo.getCode())){
            //验证码输入有误
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        //根据用户名查询用户信息
        SysUser user = this.sysUserMapper.findUserInfoUserName(vo.getUsername());
        if (user==null) {
            //用户不存在
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        //调用密码匹配器匹配输入的明文密码和数据库的密文密码
        if (!passwordEncoder.matches(vo.getPassword(),user.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //响应
        LoginRespVo respVo = new LoginRespVo();
        //属性名称与类型必须相同，否则属性值无法copy
        BeanUtils.copyProperties(user,respVo);
        return R.ok(respVo);
    }

    /**
     * 登录校验吗方法实现
     * @return
     */
    @Override
    public R<Map> getCaptchaCode() {
        //1生成图片验证码
        //参数分别是宽、高、验证码长度、干扰线数量
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        //设置背景颜色清灰
        captcha.setBackground(Color.lightGray);

        //获取图片验证码，默认生成的校验码包含文字和数字，长度为4
        String checkCode = captcha.getCode();

        //获取经过base64编码处理的图片数据
        String imageData = captcha.getImageBase64();

        //生成sessionId 避免长度过长精度丢失
        String sessionId = String.valueOf(idWorker.nextId());

        log.info("生成验证码：{},会话id:{}",checkCode,sessionId);
        //将sessionId和校验码保存在redis下，并设置缓存中数据存活时间一分钟
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX+sessionId,checkCode,5, TimeUnit.MINUTES);
        //组装数据
        Map<String,String> data = new HashMap();
        data.put("imageData",imageData);
        data.put("sessionId",sessionId);
        //响应数据
        return R.ok(data);
    }
}
