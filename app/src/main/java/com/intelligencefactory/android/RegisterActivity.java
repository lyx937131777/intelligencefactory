package com.intelligencefactory.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements OnClickListener
{
    private EditText username, password, confirm_password, nickname;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_confirm_pwd_clear;
    private Button bt_nickname_clear;
    private Button bt_register;
    private Button bt_go_back;
    String username_text, password_text, confirm_password_text, nickname_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }
    private void initView() {
        username = (EditText) findViewById(R.id.username);
        // 监听文本框内容变化
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 获得文本框中的用户
                username_text = username.getText().toString();
                if ("".equals(username_text)) {
                    // 用户名为空,设置按钮不可见
                    bt_username_clear.setVisibility(View.INVISIBLE);
                } else {
                    // 用户名不为空，设置按钮可见
                    bt_username_clear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        password = (EditText) findViewById(R.id.password);
        // 监听文本框内容变化
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 获得文本框中的用户
                password_text = password.getText().toString();
                if ("".equals(password_text)) {
                    // 用户名为空,设置按钮不可见
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                } else {
                    // 用户名不为空，设置按钮可见
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        // 监听文本框内容变化
        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 获得文本框中的用户
                confirm_password_text = confirm_password.getText().toString();
                if ("".equals(confirm_password_text)) {
                    // 用户名为空,设置按钮不可见
                    bt_confirm_pwd_clear.setVisibility(View.INVISIBLE);
                } else {
                    // 用户名不为空，设置按钮可见
                    bt_confirm_pwd_clear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        nickname = (EditText) findViewById(R.id.nickname);
        // 监听文本框内容变化
        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 获得文本框中的用户
                nickname_text = nickname.getText().toString();
                if ("".equals(nickname_text)) {
                    // 用户名为空,设置按钮不可见
                    bt_nickname_clear.setVisibility(View.INVISIBLE);
                } else {
                    // 用户名不为空，设置按钮可见
                    bt_nickname_clear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_clear.setOnClickListener(this);
        bt_confirm_pwd_clear =(Button) findViewById(R.id.bt_confirm_pwd_clear);
        bt_confirm_pwd_clear.setOnClickListener(this);
        bt_nickname_clear = (Button) findViewById(R.id.bt_nickname_clear);
        bt_nickname_clear.setOnClickListener(this);
        bt_register = (Button) findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);
        bt_go_back = (Button) findViewById(R.id.bt_go_back);
        bt_go_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 清除
            case R.id.bt_username_clear:
                username.setText("");
                break;
            case R.id.bt_pwd_clear:
                password.setText("");
                break;
            case R.id.bt_confirm_pwd_clear:
                confirm_password.setText("");
                break;
            case R.id.bt_nickname_clear:
                nickname.setText("");
                break;

            // TODO 注册按钮
            case R.id.bt_register:
                username_text = username.getText().toString();
                password_text = password.getText().toString();
                confirm_password_text = confirm_password.getText().toString();
                nickname_text = nickname.getText().toString();
                //确认密码不正确、邮箱格式不正确、昵称已被占用
                if(username_text.matches(Patterns.EMAIL_ADDRESS.toString()) != true) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的邮箱", Toast.LENGTH_LONG).show();
                }
                else if (password_text.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "请输入至少6位的密码", Toast.LENGTH_LONG).show();
                }
                else if (!password.getText().toString().equals(confirm_password.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.bt_go_back:
                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(login);
                break;

            default:
                break;
        }
    }
}