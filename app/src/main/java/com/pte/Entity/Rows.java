package com.pte.Entity;

public class Rows {
    private String searchValue;
    private String createBy;
    private String createTime;
    private String updateBy;
    private String updateTime;
    private String remark;
    private Params params;
    private int id;
    private String user;
    private String password;
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }
    public String getSearchValue() {
        return searchValue;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateTime() {
        return updateTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRemark() {
        return remark;
    }

    public void setParams(Params params) {
        this.params = params;
    }
    public Params getParams() {
        return params;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }


}
