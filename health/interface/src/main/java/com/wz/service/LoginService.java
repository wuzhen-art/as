package com.wz.service;

import com.wz.entity.Result;

import java.util.Map;

public interface LoginService {
    Result check(Map loginInfo);
}
