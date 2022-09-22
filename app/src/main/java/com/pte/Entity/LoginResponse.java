package com.pte.Entity;

import java.util.List;

public class LoginResponse {
    private int total;
    private List<Rows> rows;
    private int code;
    private String msg;
    public void setTotal(int total) {
        this.total = total;
    }
    public int getTotal() {
        return total;
    }

    public void setRows(List<Rows> rows) {
        this.rows = rows;
    }
    public List<Rows> getRows() {
        return rows;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

}
