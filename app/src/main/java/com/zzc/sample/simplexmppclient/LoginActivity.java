package com.zzc.sample.simplexmppclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText mEdtName;
    private EditText mEdtPsd;
    public static String LOGINER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = (Button) findViewById(R.id.btn_login);
        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtPsd = (EditText) findViewById(R.id.edt_psd);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        final String name = mEdtName.getText().toString().trim();
        String psd = mEdtPsd.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(psd)) return;
        ConnectManager.getInstance().login(name, psd, new ConnectManager.ResultListener() {
            @Override
            public void result(boolean login) {
                if (login) {
                    LOGINER = name;
                    startActivity(new Intent(LoginActivity.this, ContactListActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
