package com.wz.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.constants.MessageConstant;
import com.wz.constants.RedisConstant;
import com.wz.entity.PageResult;
import com.wz.entity.QueryPageBean;
import com.wz.entity.Result;
import com.wz.pojo.Setmeal;
import com.wz.service.SetMealService;
import com.wz.utils.QiniuUtils;
import com.wz.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {


    @Reference(interfaceClass = SetMealService.class)
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;



    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        /*
        * 1 获得文件名
        * 2 编码文件名
        * 3 上传文件
        * 4 jedis存储文件名
        * */

        try {
            String filename = imgFile.getOriginalFilename();
            filename = UploadUtils.getUUIDName(filename);
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,filename);
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,filename);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.PIC_UPLOAD_FAIL);


        }


    }



    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setMealService.findPage(queryPageBean);

    }

    @RequestMapping("/add")
    public Result add(int[] checkgroupIds, @RequestBody Setmeal setmeal){
        try {
            setMealService.add(checkgroupIds,setmeal);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }

    }

    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Setmeal setmeal = setMealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }

    }

    @RequestMapping("/findCheckGroupsBySetMealId")
    public List<Integer> findCheckGroupsBySetMealId(int id){
        return setMealService.findCheckGroupsBySetMealId(id);


    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal,int[] checkgroupIds){
        try {
            setMealService.edit(checkgroupIds,setmeal);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }

    }

    @RequestMapping("/delete")
    public Result delete(int id){
        try {
            setMealService.delete(id);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }

    }








}
