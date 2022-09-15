package com.pte.Activity;

import android.widget.Button;
import android.widget.EditText;

import com.pte.R;

public class RegisterActivity extends BaseActivity{
    private Button register;
    private EditText etAccount,etPwd;
    private static volatile Boolean flag = false;

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        register = findViewById(R.id.btn_register);
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
    }

    @Override
    protected void initData() {

    }
}
