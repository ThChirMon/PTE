package com.pte.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pte.R;
import com.pte.Util.StringUtils;

public class LoginActivity extends BaseActivity{
    private EditText etAccount,etPwd;
    private Button btnLogin;
    private String re_account,re_pwd;
    SharedPreferences sprfMain;

    SharedPreferences.Editor editorMain;


    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        sprfMain= PreferenceManager.getDefaultSharedPreferences(this);
        editorMain=sprfMain.edit();
        if(sprfMain.getBoolean("main",false)){

            Intent intent=new Intent(LoginActivity.this,PhotoActivity.class);

            startActivity(intent);

            LoginActivity.this.finish();

        }

        setContentView(R.layout.activity_login);



        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            re_account = bundle.getString("account");
            re_pwd = bundle.getString("pwd");
        }
        etAccount.setText(re_account);
        etPwd.setText(re_pwd);


        Btnlistener btnlistener = new Btnlistener();
        btnLogin.setOnClickListener(btnlistener);
    }
    private class Btnlistener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_login:
                    String account = etAccount.getText().toString().trim();
                    String pwd = etPwd.getText().toString().trim();
                    login(account, pwd);

                    Intent intent=new Intent(LoginActivity.this,PhotoActivity.class);
                    editorMain.putBoolean("main",true);
                    editorMain.commit();
                    startActivity(intent);
                    LoginActivity.this.finish();
            }
        }
    }

    private  void login(String account, String pwd){
        if(StringUtils.isEmpty(account)){
            showToast("请输入账号！");
            return;
        }
        if(StringUtils.isEmpty(pwd)){
            showToast("请输入密码！");
            return;
        }
        System.out.println(account);
        if(account.equals("123456") && pwd.equals("123456"))
        {
            System.out.println("成功");
            navigateTo(PhotoActivity.class);
        }else{
            showToast("用户名或密码错误！");
        }
            /*
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user", account);
        params.put("password", pwd);
        Api.config(ApiConfig.LOGIN, params).getRequest(this,new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                if (loginResponse.getCode() == 200 && loginResponse.getTotal() == 1) {
                    navigateTo(PhotoActivity.class);
                    showToastSync("登录成功");
                } else {
                    showToastSync("用户名或密码错误！");
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

             */
    }


}
