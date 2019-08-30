package com.wz.service;



import com.alibaba.dubbo.config.annotation.Service;
import com.wz.constants.MessageConstant;
import com.wz.constants.RedisMessageConstant;
import com.wz.dao.MemberDao;
import com.wz.entity.Result;
import com.wz.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;


@Transactional
@Service(interfaceClass = LoginService.class)
public class LoginServiceImp implements LoginService {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private MemberDao memberDao;



    @Override
    public Result check(Map loginInfo) {
        String validateCode = (String)loginInfo.get("validateCode");
        String telephone = (String) loginInfo.get("telephone");
//        判断logInInfo是否为空
//        判断验证码是否正确
//        判断是否为会员
//        不是会员自动添加
        if (validateCode == null || telephone == null){
            return new Result(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
        }
        String standards = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + ":" + telephone);
        if (!validateCode.equals(standards)){
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }

        Member member = memberDao.getMemberByTelephone(telephone);
        if (member == null){
//            空member无数据;
            member = new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            memberDao.add(member);
        }
        return new Result(true,MessageConstant.LOGIN_SUCCESS,member);
    }
}
