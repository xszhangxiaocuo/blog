package com.github.xszhangxiaocuo.entity.Err;

import java.util.HashMap;
import java.util.Map;

public class ErrMessage {
    private static final Map<Integer, String> codeMsg = new HashMap<>();

    static {
        codeMsg.put(ErrCode.OK.getCode(), "OK");
        codeMsg.put(ErrCode.FAIL.getCode(), "FAIL");

        codeMsg.put(ErrCode.ERROR_REQUEST_PARAM.getCode(), "请求参数格式错误");
        codeMsg.put(ErrCode.ERROR_REQUEST_PAGE.getCode(), "分页参数错误");
        codeMsg.put(ErrCode.ERROR_INVALID_PARAM.getCode(), "不合法的请求参数");
        codeMsg.put(ErrCode.ERROR_DB_OPE.getCode(), "数据库操作异常");

        codeMsg.put(ErrCode.ERROR_FILE_UPLOAD.getCode(), "文件上传失败");
        codeMsg.put(ErrCode.ERROR_FILE_RECEIVE.getCode(), "文件接收失败");

        codeMsg.put(ErrCode.ERROR_USER_NAME_USED.getCode(), "用户名已存在");
        codeMsg.put(ErrCode.ERROR_PASSWORD_WRONG.getCode(), "密码错误");
        codeMsg.put(ErrCode.ERROR_USER_NOT_EXIST.getCode(), "该用户不存在");
        codeMsg.put(ErrCode.ERROR_USER_NO_RIGHT.getCode(), "该用户无权限");
        codeMsg.put(ErrCode.ERROR_OLD_PASSWORD.getCode(), "旧密码不正确");
        codeMsg.put(ErrCode.ERROR_EMAIL_SEND.getCode(), "邮件发送失败");
        codeMsg.put(ErrCode.ERROR_EMAIL_HAS_SEND.getCode(), "已向该邮箱发送验证码（有效期 15 分钟），请检查回收站");
        codeMsg.put(ErrCode.ERROR_VERIFICATION_CODE.getCode(), "验证码错误");

        codeMsg.put(ErrCode.ERROR_TOKEN_NOT_EXIST.getCode(), "TOKEN 不存在，请重新登陆");
        codeMsg.put(ErrCode.ERROR_TOKEN_RUNTIME.getCode(), "TOKEN 已过期，请重新登陆");
        codeMsg.put(ErrCode.ERROR_TOKEN_WRONG.getCode(), "TOKEN 不正确，请重新登陆");
        codeMsg.put(ErrCode.ERROR_TOKEN_TYPE_WRONG.getCode(), "TOKEN 格式错误，请重新登陆");
        codeMsg.put(ErrCode.ERROR_TOKEN_CREATE.getCode(), "TOKEN 生成失败");
        codeMsg.put(ErrCode.ERROR_PERMI_DENIED.getCode(), "权限不足");
        codeMsg.put(ErrCode.FORCE_OFFLINE.getCode(), "您已被强制下线");
        codeMsg.put(ErrCode.LOGOUT.getCode(), "您已退出登录");

        codeMsg.put(ErrCode.ERROR_ART_NOT_EXIST.getCode(), "文章不存在");
        codeMsg.put(ErrCode.ERROR_ART_IS_NULL.getCode(), "文章列表为空");
        codeMsg.put(ErrCode.ERROR_TAG_IS_NULL.getCode(), "标签列表为空");
        codeMsg.put(ErrCode.ERROR_CATEGORY_IS_NULL.getCode(), "分类列表为空");
        codeMsg.put(ErrCode.ERROR_COMMENT_IS_NULL.getCode(), "评论列表为空");

        codeMsg.put(ErrCode.ERROR_CATE_NAME_USED.getCode(), "操作失败，分类名已存在");
        codeMsg.put(ErrCode.ERROR_CATE_NOT_EXIST.getCode(), "操作失败，分类不存在");
        codeMsg.put(ErrCode.ERROR_CATE_ART_EXIST.getCode(), "删除失败，分类下存在文章");

        codeMsg.put(ErrCode.ERROR_TAG_EXIST.getCode(), "操作失败，标签名已存在");
        codeMsg.put(ErrCode.ERROR_TAG_NOT_EXIST.getCode(), "操作失败，标签不存在");
        codeMsg.put(ErrCode.ERROR_TAG_ART_EXIST.getCode(), "删除失败，标签下存在文章");

        codeMsg.put(ErrCode.ERROR_COMMENT_NOT_EXIST.getCode(), "评论不存在");

        codeMsg.put(ErrCode.ERROR_RESOURCE_NAME_EXIST.getCode(), "该资源名已经存在");
        codeMsg.put(ErrCode.ERROR_RESOURCE_NOT_EXIST.getCode(), "该资源不存在");
        codeMsg.put(ErrCode.ERROR_RESOURCE_USED_BY_ROLE.getCode(), "该资源正在被角色使用，无法删除");
        codeMsg.put(ErrCode.ERROR_RESOURCE_HAS_CHILDREN.getCode(), "该资源下存在子资源，无法删除");
        codeMsg.put(ErrCode.ERROR_MENU_NAME_EXIST.getCode(), "该菜单名已经存在");
        codeMsg.put(ErrCode.ERROR_MENU_NOT_EXIST.getCode(), "该菜单不存在");
        codeMsg.put(ErrCode.ERROR_MENU_USED_BY_ROLE.getCode(), "该菜单正在被角色使用，无法删除");
        codeMsg.put(ErrCode.ERROR_MENU_HAS_CHILDREN.getCode(), "该菜单下存在子菜单，无法删除");
        codeMsg.put(ErrCode.ERROR_ROLE_NAME_EXIST.getCode(), "该角色名已经存在");
        codeMsg.put(ErrCode.ERROR_ROLE_NOT_EXIST.getCode(), "该角色不存在");

        codeMsg.put(ErrCode.ERROR_PAGE_NAME_EXIST.getCode(), "该页面名称已经存在");
    }

    public static String getMsg(int code) {
        return codeMsg.getOrDefault(code, "Unknown error code");
    }
}

