package com.wz.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.wz.constants.MessageConstant;
import com.wz.dao.MemberDao;
import com.wz.dao.OrderDao;
import com.wz.dao.OrderSettingDao;
import com.wz.entity.Result;
import com.wz.pojo.Member;
import com.wz.pojo.Order;
import com.wz.pojo.OrderSetting;
import com.wz.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Transactional
@Service(interfaceClass = OrderService.class)
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;


    @Override
    public Result process(Map orderInfo) throws Exception {
        Integer setmealId = Integer.parseInt((String)orderInfo.get("setmealId")) ;
        String orderDate =(String) orderInfo.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        if (orderSetting.getNumber() <= orderSetting.getReservations()){
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        String telephone = (String) orderInfo.get("telephone");
        Member member = memberDao.getMemberByTelephone(telephone);
        if (member!=null){
            Order order = new Order();
            Integer id = member.getId();
            order.setMemberId(id);
            order.setOrderDate(date);
            order.setSetmealId(setmealId);
            List<Order> orderList = orderDao.findByOrder(order);
            if (orderList != null && orderList.size()>0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {
            member = new Member();
            member.setName((String)orderInfo.get("name"));
            member.setSex((String)orderInfo.get("sex"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String)orderInfo.get("idCard"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        Order order = new Order(member.getId(), date, Order.ORDERTYPE_WEIXIN, Order.ORDERSTATUS_NO, setmealId);

        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservations(orderSetting);



        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    @Override
    public Map findById(int id) {
        return orderDao.findById(id);
    }
    /**
     * 预约
     *
     * @param
     * @return { "setmealId": "12", "sex": "1", "name": "张三", "telephone": "15374518821",
     * "validateCode": "127890", "idCard": "512501197203035172",
     * "orderDate": "2019-07-22" }
     */

    /*日期进行转换
     * 1 判断日期是否在预约表中
     * 2 判断当前日期是否预约满
     * 3 判断是否是会员
     * 4.1 如果是会员,判断是否已经预定
     * 4.2 如果不是会员,注册为会员;
     * 5 进行order注册
     * 6 ordersetting reservation +1
     *
     * */
}
