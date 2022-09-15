package com.pte.Activity;

import android.widget.Button;
import android.widget.EditText;

import com.pte.R;

public class LoginActivity extends BaseActivity{
    private EditText etAccount,etPwd;
    private Button btnLogin;
    private String re_account,re_pwd;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
    }

    @Override
    protected void initData() {

    }
}
