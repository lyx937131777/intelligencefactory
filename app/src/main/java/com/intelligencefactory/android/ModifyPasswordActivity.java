package com.intelligencefactory.android;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ModifyPasswordActivity extends AppCompatActivity {
    Button bt_old_pw_clear;
    Button bt_old_pw_eye;
    Button bt_new_pw_clear;
    Button bt_new_pw_eye;
    Button bt_re_pw_clear;
    Button bt_re_pw_eye;
    EditText et_old_pw;
    EditText et_new_pw;
    EditText et_re_pw;
    Button bt_summit;
    TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        bt_old_pw_clear=(Button)findViewById(R.id.bt_old_pwd_clear);
        bt_old_pw_eye=(Button)findViewById(R.id.bt_old_pwd_eye);
        bt_new_pw_clear=(Button)findViewById(R.id.bt_new_pwd_clear);
        bt_new_pw_eye=(Button)findViewById(R.id.bt_new_pwd_eye);
        bt_re_pw_clear=(Button)findViewById(R.id.bt_re_pwd_clear);
        bt_re_pw_eye=(Button)findViewById(R.id.bt_re_pwd_eye);
        et_new_pw=(EditText)findViewById(R.id.et_new_password);
        et_old_pw=(EditText)findViewById(R.id.et_old_password);
        et_re_pw=(EditText)findViewById(R.id.et_re_password);
        tv_message=(TextView) findViewById(R.id.tv_modify_password);

        if(bt_old_pw_clear==null) Log.e("ModifyPassword","bt_old_pw_clear is null");
        if(bt_old_pw_eye==null) Log.e("ModifyPassword","bt_old_pw_eye is null");
        bt_old_pw_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        Log.e("ModifyPassword","bt_old_pw:");
        init_passwordFrame(et_old_pw,bt_old_pw_clear,bt_old_pw_eye);
//        Log.e("ModifyPassword","bt_new_pw:");
        init_passwordFrame(et_new_pw,bt_new_pw_clear,bt_new_pw_eye);
//        Log.e("ModifyPassword","bt_re_pw:");
        init_passwordFrame(et_re_pw,bt_re_pw_clear,bt_re_pw_eye);

        bt_summit=(Button)findViewById(R.id.bt_summit);
        bt_summit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收起软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                //处理事务
                String old_pw=et_old_pw.getText().toString();
                String new_pw=et_new_pw.getText().toString();
                String re_pw=et_re_pw.getText().toString();
                if(!new_pw.equals(re_pw)){
                    tv_message.setText("新密码不一致！");
                }
                else{
                    tv_message.setText("");
                    // post userID old_pw new_pw to server
                }
            }
        });

    }

    private void init_passwordFrame(final EditText et_pw, final Button bt_clear, final Button bt_eye){
        // 监听文本框内容变化
//        if (bt_eye==null)Log.e("ModifyPassword","bt_eye is null");
//        else Log.e("ModifyPassword","bt_eye is not null");
        et_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 获得文本框中的用户
                String et_pw_text = et_pw.getText().toString().trim();
                if ("".equals(et_pw_text)) {
                    // 用户名为空,设置按钮不可见
                    bt_clear.setVisibility(View.INVISIBLE);
                } else {
                    // 用户名不为空，设置按钮可见
                    bt_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_pw.setText("");
            }
        });

        bt_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(et_pw.getInputType()== InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    bt_eye.setBackgroundResource(R.drawable.pwd_closed);
                    et_pw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else {

                    bt_eye.setBackgroundResource(R.drawable.pwd_open);
                    et_pw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


}
