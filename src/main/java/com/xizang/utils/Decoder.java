package com.xizang.utils;

import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author ： 杨冲
 * @DateTime ： 2023/6/12 15:16
 */
public class Decoder {
    public static void main(String[] args) throws IOException {
        String cod = "YzJWc1pXTjBJR1pwWld4a2JtRnRaU3dnWkdGMFlYUjVjR1VnWm5KdmJTQnplWE5mWm1sbGJHUWdkMmhsY21VZ1ptbGxiR1JwWkNBOUlERTBNVEl3TWc9PQ==";

        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] bytes = base64Decoder.decodeBuffer(cod);
        String s = new String(base64Decoder.decodeBuffer(new String(bytes)), StandardCharsets.UTF_8);
        System.out.println(s);
    }
}
