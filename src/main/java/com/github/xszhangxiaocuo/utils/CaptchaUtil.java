package com.github.xszhangxiaocuo.utils;


import java.util.Random;

public class CaptchaUtil {

    public static String generateCaptcha(int n){
        Random rand = new Random();
        // 计算开始值和结束值
        int start = (int) Math.pow(10, n - 1);
        int end = (int) Math.pow(10, n) - 1;

        // 生成 n 位随机数
        int randomNumber = start + rand.nextInt(end - start + 1);
        return Integer.toString(randomNumber);
    }
}
