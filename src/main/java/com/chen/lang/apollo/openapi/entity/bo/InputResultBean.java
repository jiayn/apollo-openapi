package com.chen.lang.apollo.openapi.entity.bo;

/**
 * create by czq@chen.cn
 * DATE:2019/1/4
 * TIME:15:26
 *
 * @author czq@chen.cn
 * PRJ:simpeasy
 **/
public class InputResultBean {
    private String inputStr;
    private boolean exit;
    private boolean end;

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

}
