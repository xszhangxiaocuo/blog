package com.github.xszhangxiaocuo.entity.Err;

public enum ErrCode {
    OK(0),
    FAIL(500),
    /**
     * code= 90... 通用错误
     */
    ERROR_REQUEST_PARAM(9001),
    ERROR_REQUEST_PAGE(9002),
    ERROR_INVALID_PARAM(9003),
    ERROR_DB_OPE(9004),
    /**
     * code 91... 文件相关错误
     */
    ERROR_FILE_UPLOAD(9100),
    ERROR_FILE_RECEIVE(9101),
    /**
     * code= 10... 用户模块的错误
     */
    ERROR_USER_NAME_USED(1001),
    ERROR_PASSWORD_WRONG(1002),
    ERROR_USER_NOT_EXIST(1003),
    ERROR_USER_NO_RIGHT(1009),
    ERROR_OLD_PASSWORD(1010),
    ERROR_EMAIL_SEND(1011),
    ERROR_EMAIL_HAS_SEND(1012),
    ERROR_VERIFICATION_CODE(1013),
    ERROR_VOID_ACCOUNT_PASSWORD(1014),
    /**
     *code = 12.. 鉴权相关错误
     */
    ERROR_TOKEN_NOT_EXIST(1201),
    ERROR_TOKEN_RUNTIME(1202),
    ERROR_TOKEN_WRONG(1203),
    ERROR_TOKEN_TYPE_WRONG(1204),
    ERROR_TOKEN_CREATE(1205),
    ERROR_PERMI_DENIED(1206),
    FORCE_OFFLINE(1207),
    LOGOUT(1208),
    ERROR_TOKEN_PARSE(1209),
    /**
     * code= 2000... 文章模块的错误
     */
    ERROR_ART_NOT_EXIST(2001),
    ERROR_ART_IS_NULL(2002),
    /**
     * code= 3000... 分类模块的错误
     */
    ERROR_CATE_NAME_USED(3001),
    ERROR_CATE_NOT_EXIST(3002),
    ERROR_CATE_ART_EXIST(3003),
    ERROR_CATEGORY_IS_NULL(3004),

    /**
     * code= 40... 标签模块的错误
     */
    ERROR_TAG_EXIST(4001),
    ERROR_TAG_NOT_EXIST(4002),
    ERROR_TAG_ART_EXIST(4003),
    ERROR_TAG_IS_NULL(4004),
    /**
     * code=50... 评论模块的错误
     */
    ERROR_COMMENT_NOT_EXIST(5001),
    ERROR_COMMENT_IS_NULL(5002);

    private final int code;

    ErrCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

