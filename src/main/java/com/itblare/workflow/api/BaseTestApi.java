package com.itblare.workflow.api;

import com.itblare.workflow.support.annotation.ResponseWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/7 15:24
 */
@RestController
@RequestMapping("/test/flowable")
public class BaseTestApi implements BaseApi {

    /**
     * 测试接口test
     *
     * @param a 参数a
     * @param b 参数b
     * @return {@link String}
     * @author Blare
     */
    @GetMapping("test")
    public String testApi(int a, int b) {
        return String.valueOf(a + b);
    }

    /**
     * 测试接口test2
     *
     * @param a 参数a
     * @param b 参数b
     * @return {@link String}
     * @author Blare
     */
    @GetMapping("test2")
    @ResponseWrapper
    public String testApi2(int a, int b) {
        return String.valueOf(a + b);
    }
}