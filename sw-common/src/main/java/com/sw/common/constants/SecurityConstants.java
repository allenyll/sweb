package com.sw.common.constants;

/**
 * Security 权限常量
 */
public interface SecurityConstants {
    /**
     * 用户信息分隔符
     */
    String USER_SPLIT = ":";

    /**
     * 用户信息头
     */
    String USER_HEADER = "x-user-header";
    String USER_ID_HEADER = "x-userId-header";

    /**
     * 角色信息头
     */
    String ROLE_HEADER = "x-role-header";

    /**
     * 基础角色
     */
    String BASE_ROLE = "ROLE_USER";

    /**
     * 授权码模式
     */
    String AUTHORIZATION_CODE = "authorization_code";

    /**
     * 密码模式
     */
    String PASSWORD = "password";

    /**
     * 刷新token
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * oauth token
     */
    String OAUTH_TOKEN_URL = "/auth";

    /**
     * 默认的处理验证码的url前缀
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/validata/code";

    /**
     * 手机号的处理验证码的url前缀
     */
    String MOBILE_VALIDATE_CODE_URL_PREFIX = "/validata/smsCode";

    /**
     * 默认生成图形验证码宽度
     */
    String DEFAULT_IMAGE_WIDTH = "100";

    /**
     * 默认生成图像验证码高度
     */
    String DEFAULT_IMAGE_HEIGHT = "35";

    /**
     * 默认生成图形验证码长度
     */
    String DEFAULT_IMAGE_LENGTH = "4";

    /**
     * 默认生成图形验证码过期时间
     */
    int DEFAULT_IMAGE_EXPIRE = 60*5;

    /**
     * 边框颜色，合法值： r,g,b (and optional alpha) 或者 white,black,blue.
     */
    String DEFAULT_COLOR_FONT = "blue";

    /**
     * 图片边框
     */
    String DEFAULT_IMAGE_BORDER = "no";

    /**
     * 默认图片间隔
     */
    String DEFAULT_CHAR_SPACE = "5";

    /**
     * 默认保存code的前缀
     */
    String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY";

    /**
     * 验证码文字大小
     */
    String DEFAULT_IMAGE_FONT_SIZE = "30";

    /**
     * 登录方式
     */
    String LOGIN_TYPE = "LOGIN_TYPE";

}
