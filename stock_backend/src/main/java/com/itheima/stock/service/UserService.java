package com.itheima.stock.service;

import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;

/**
 * ClassName: UserService
 * Package: com.itheima.stock.service
 * Description:
 *
 * @Author starry
 * @Create 2024/2/22 17:05
 * @Version 1.0
 */
public interface UserService {
    /**
     * 根据用户名称查询用户信息
     * @param userName
     * @return
     */
    SysUser findByUserName(String userName);

    /**
     * 用户登录
     * @param vo
     * @return
     */
    R<LoginRespVo> login(LoginReqVo vo);
}
