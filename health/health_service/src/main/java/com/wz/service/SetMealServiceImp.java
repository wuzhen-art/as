package com.wz.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wz.constants.RedisConstant;
import com.wz.dao.SetMealDao;
import com.wz.entity.PageResult;
import com.wz.entity.QueryPageBean;
import com.wz.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;


@Transactional
@Service(interfaceClass = SetMealService.class)
public class SetMealServiceImp implements SetMealService {
    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private JedisPool jedisPool;


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Setmeal> page = setMealDao.findPagesByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(int[] checkgroupIds, Setmeal setmeal) {
        setMealDao.add(setmeal);
        set_t_setmeal_checkgroup(checkgroupIds,setmeal.getId());
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());


    }

    @Override
    public List<Setmeal> listAll() {
        return setMealDao.findAll();
    }

    @Override
    public Setmeal getSetMealById(int id) {
        return setMealDao.getSetMealById(id);
    }

    @Override
    public Setmeal findById(int id) {
        Setmeal setmeal = setMealDao.findById(id);

        return setmeal;
    }

    @Override
    public List<Integer> findCheckGroupsBySetMealId(int id) {
        return setMealDao.getCheckGroupsBySetMealId(id);
    }

    @Override
    public void edit(int[] checkgroupIds, Setmeal setmeal) {
        Integer id = setmeal.getId();
//        删除关联表的id
//        设置新的id
//        编辑新的setmeal;
        deleteLink(id);

        set_t_setmeal_checkgroup(checkgroupIds,id);

        setMealDao.edit(setmeal);




    }

    @Override
    public void delete(int id) {
        deleteLink(id);
        setMealDao.deleteSetMeal(id);

    }

    public void deleteLink(int id){
        setMealDao.delete(id);
    }

    public void set_t_setmeal_checkgroup(int[] checkgroupIds,int setmealId){
        if (checkgroupIds !=null && checkgroupIds.length > 0){
            for (int checkgroupId : checkgroupIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("setmeal_id",setmealId);
                map.put("checkgroup_id",checkgroupId);
                setMealDao.checkGroupAndSetmeal(map);

            }
        }

    }
}
