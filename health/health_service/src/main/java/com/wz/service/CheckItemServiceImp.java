package com.wz.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wz.dao.CheckItemDao;
import com.wz.entity.PageResult;
import com.wz.entity.QueryPageBean;
import com.wz.pojo.CheckItem;
import com.wz.utils.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service(interfaceClass = CheckItemService.class)

@Transactional
public class CheckItemServiceImp implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {


//        分页查询格式(limit (currentPage-1)*pageSize, pageSize)
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

//        PageHelper Page包含所有的分页内容和总页数.
        Page<CheckItem> page = checkItemDao.queryByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());


    }

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);

    }

    @Override
    public void delete(int id) {
        checkItemDao.deleteCheckGroupCheckItemBycheckItemId(id);
        checkItemDao.delete(id);

    }

    @Override
    public List<CheckItem> findAll() {
       return checkItemDao.findAll();
    }


    @Test
    public void tt(){
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        String encode = bCryptPasswordEncoder.encode("1234");
//        System.out.println(encode);
//        boolean matches = bCryptPasswordEncoder.matches("1234", "$2a$10$hC6oqrHvnTwlRBLjIutI0u.Qhr5IWGLnUHIerTe3MHekKwwQV1D3u");
//        System.out.println(matches);
//        Date date = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
//        String format = simpleDateFormat.format(date.getTime());
//        System.out.println(date.toString());



//        System.out.println();
    }

}
