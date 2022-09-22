package com.pte.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.pte.API.Api;
import com.pte.API.ApiConfig;
import com.pte.API.TtitCallback;
import com.pte.Entity.LoginResponse;
import com.pte.Entity.RegisterResponse;
import com.pte.R;
import com.pte.Util.StringUtils;

import java.util.HashMap;

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
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
    }

    @Override
    protected void initData() {
        Btnlistener btnlistener = new Btnlistener();
        register.setOnClickListener(btnlistener);
    }
    private class Btnlistener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                /*
                case R.id.btn_register:
                    String account = etAccount.getText().toString().trim();
                    String pwd = etPwd.getText().toString().trim();
                    register(account, pwd);

                 */
            }
        }
    }
    private void register(String account, String pwd) {
        String regex_username = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
        String regex_password = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{6,20}$";
        if(StringUtils.isEmpty(account)){
            showToast("账号不能为空！");
            return;
        }
        if(StringUtils.isEmpty(pwd)){
            showToast("密码不能为空！");
            return;
        }
        if(!account.matches(regex_username))
        {
            showToast("用户名必须是手机号！");
            return;
        }
        if (!pwd.matches(regex_password))
        {
            showToast("密码必须包含数字、英文、字符中的两种以上，长度6-20");
            return;
        }
        HashMap<String, Object> params_search = new HashMap<String, Object>();
        params_search.put("user", account);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user", account);
        params.put("password", pwd);
        Api.config(ApiConfig.LOGIN, params_search).getRequest(this,new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                if (loginResponse.getTotal() == 0) {
                    flag = true;
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        Api.config(ApiConfig.REGISTER, params).postparmsRequest(this, new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                if (flag == true) {
                    Log.e("onSuccess", res);
                    Gson gson = new Gson();
                    RegisterResponse registerResponse = gson.fromJson(res, RegisterResponse.class);
                    if (registerResponse.getTotal() == 1) {
                        Bundle bundle = new Bundle();
                        bundle.putString("account",etAccount.getText().toString().trim());
                        bundle.putString("pwd",etPwd.getText().toString().trim());
                        navigateToWithBundle(LoginActivity.class,bundle);
                        showToastSync("注册成功！");
                    } else {
                        showToastSync("注册失败！");
                    }
                    flag = false;
                }else {
                    showToastSync("该用户已经被注册！");
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        });

    }
}
