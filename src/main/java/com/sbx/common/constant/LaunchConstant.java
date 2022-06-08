package com.sbx.common.constant;


/**
 * <p>LaunchConstant class:</p>
 *
 * @author zhaijianchao
 * @version 1.0.0
 * @date 2020/3/23
 */
public interface LaunchConstant {

    /**
     * nacos注册地址
     */
    String NACOS_ADDRESS_PROD = "49.235.125.232:8848";

    String NACOS_ADDRESS_TEST = "49.235.125.232:8848";

    String NACOS_ADDRESS_DEV = "49.235.125.232:8848";

    String NAMESPACE_DEV = "58fa1211-4900-4562-b14f-e2d353818511";
    String NAMESPACE_TEST = "56921f44-c024-476d-b959-0d8bbcebfe89";
    String NAMESPACE_PRE = "e9fd7da1-9859-4e75-bcf0-406298d95188";
    String NAMESPACE_PROD = "a0a5210f-5e4d-4039-8341-09c93ad02954";


    String NACOS_PASSWORD_DEVTEST = "zhai6319.._";

    String NACOS_PASSWORD_PROD = "8ce0f9714ede537d";


    /**
     * 动态获取nacos地址
     *
     * @param profile 环境变量
     * @return addr
     */
    static String nacosAddr(String profile) {
        switch (profile) {
            case ("prod"):
                return NACOS_ADDRESS_PROD;
            case ("test"):
                return NACOS_ADDRESS_TEST;
            default:
                return NACOS_ADDRESS_DEV;
        }
    }

    static String namespace(String profile){
        switch (profile) {
            case ("prod"):
                return NAMESPACE_PROD;
            case ("test"):
                return NAMESPACE_TEST;
            case ("pre"):
                return NAMESPACE_PRE;
            default:
                return NAMESPACE_DEV;
        }
    }

    static String nacosPassword(String profile){
        switch (profile) {
            case ("prod"):
                return NACOS_PASSWORD_PROD;
            default:
                return NACOS_PASSWORD_DEVTEST;
        }
    }

}
