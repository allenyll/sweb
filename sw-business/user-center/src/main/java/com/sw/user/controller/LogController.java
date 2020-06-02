package com.sw.user.controller;


import com.sw.common.entity.user.Log;
import com.sw.common.entity.user.User;
import com.sw.common.util.DataResponse;
import com.sw.client.controller.BaseController;
import com.sw.user.service.impl.LogServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 记录日志 前端控制器
 * </p>
 *
 * @author yu.leilei
 * @since 2018-12-23
 */
@Api(value = "日志相关接口", tags = "日志管理")
@Controller
@RequestMapping("log")
public class LogController extends BaseController<LogServiceImpl, Log> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

    @ApiOperation(value = "日志记录")
    @ResponseBody
    @PostMapping("saveLog")
    public DataResponse saveLog(@RequestBody Log log) {
        User user = new User();
        user.setPkUserId("登录");
        return super.add(user, log);
    }

}
