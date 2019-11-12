package com.wz.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wz.dao.CheckGroupDao;
import com.wz.entity.PageResult;
import com.wz.entity.QueryPageBean;
import com.wz.pojo.CheckGroup;
import com.wz.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImp implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.showPagesByKeyWords(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(CheckGroup checkGroup, int[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
        System.out.println("dgfga");


    }

    @Override
    public CheckGroup findById(int id) {
       return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> getCheckItemIdsByCheckGroupId(int id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(int[] checkitemIds, CheckGroup checkGroup) {
        deleteCheckItemIdsByCheckGroupId(checkGroup.getId());
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
        checkGroupDao.edit(checkGroup);



    }

    @Override
    public void delete(int id) {
        /*
         * 外键冲突
         * 1 删除checkgroup-checkItem checkgroup
         * 2 删除setmeal_checkgroup  checkGroup
         * 3 删除checkgroup
         * */

        deleteCheckItemIdsByCheckGroupId(id);
        checkGroupDao.deletesetmeal_checkgroupByGroupId(id);
        checkGroupDao.deleteById(id);




    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    public void setCheckGroupAndCheckItem(int id,int[] checkitemIds){
       if (checkitemIds !=null && checkitemIds.length > 0){
           for (int checkitemId : checkitemIds) {
               HashMap<String, Integer> map = new HashMap<>();
               map.put("checkgroup_id",id);
               map.put("checkitem_id",checkitemId);
               checkGroupDao.checkGroupAndCheckItem(map);

           }
       }
    }

    public void deleteCheckItemIdsByCheckGroupId(int id){
        checkGroupDao.deleteCheckItemIdsById(id);

    }
}
