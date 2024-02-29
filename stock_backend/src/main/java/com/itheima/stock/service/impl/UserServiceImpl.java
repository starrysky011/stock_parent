package com.itheima.stock.service.impl;

import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.service.UserService;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
public class UserServiceImpl implements UserService {


    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
