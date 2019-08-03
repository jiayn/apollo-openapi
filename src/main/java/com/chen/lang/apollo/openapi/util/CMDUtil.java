package com.chen.lang.apollo.openapi.util;

import java.util.Scanner;

import com.chen.lang.apollo.openapi.entity.bo.InputResultBean;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-26 15:03
 */
public class CMDUtil {
    private final static String[] EXIT_ARRAY = {"quit", "exit"};

    public static boolean CheckIfExit(String inputStr) {
        for (String exit : EXIT_ARRAY) {
            if (exit.equals(inputStr)) {
                return true;
            }
        }
        return false;
    }

    public static void printPicture() {
        String subject = " ____ ____ ____ ____ ____ ____ \n" + "||A |||P |||O |||L |||L |||O ||\n"
            + "||__|||__|||__|||__|||__|||__||\n" + "|/__\\|/__\\|/__\\|/__\\|/__\\|/__\\|\n";
        System.out.println(subject);
    }

    /**
     * 获得输入值
     *
     * @param promptMessage 提示信息
     * @param sc
     * @return
     */
    public static InputResultBean inputValue(String promptMessage, Scanner sc) {
        System.out.println(promptMessage);
        InputResultBean result = new InputResultBean();
        while (true) {
            String inputPrivateToken = sc.nextLine();
            boolean exit = CheckIfExit(inputPrivateToken);
            if (exit) {
                System.out.println("quit,exit,触发退出！");
                result.setExit(exit);
                return result;
            }
            result.setExit(false);
            result.setInputStr(inputPrivateToken);
            return result;

        }
    }

}
