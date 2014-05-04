package com.example.xiaoyuantong;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent; 
import android.content.SharedPreferences; 
import android.content.SharedPreferences.Editor; 
import android.text.TextUtils; 
import android.view.View; 
import android.view.View.OnClickListener; 
import android.widget.Button; 
import android.widget.CheckBox; 
import android.widget.EditText; 
import android.widget.ImageButton; 
import android.widget.Toast; 


public class LoginActivity extends Activity {
	
	private EditText username, pass; 
	CheckBox remberPass, autoLogin; 
	Button login; 
	SharedPreferences sp; 
	Editor ed;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) { 
    	super.onCreate(savedInstanceState); 
    	setContentView(R.layout.activity_login);
    	username = (EditText) findViewById(R.id.etUsername); 
    	pass = (EditText) findViewById(R.id.etPassword);
    	remberPass = (CheckBox) findViewById(R.id.remember); 
    	autoLogin = (CheckBox) findViewById(R.id.autoLogin); 
    	login = (Button) findViewById(R.id.login);
    	sp = getSharedPreferences("users", MODE_WORLD_READABLE); 
    	// 从SharedPreferences里边取出 记住密码的状态 
    	if (sp.getBoolean("ISCHECK", false)) { 
    	// 将记住密码设置为被点击状态 
    	remberPass.setChecked(true); 
    	// 然后将值赋值给EditText 
    	username.setText(sp.getString("oa_name", "")); 
    	pass.setText(sp.getString("oa_pass", ""));
    	// 获取自动登录按钮的状态 
    	if (sp.getBoolean("AUTO_ISCHECK", false)) { 
    	// 设置自动登录被点击 然后实现跳转 
    	autoLogin.setChecked(true); 
    	Intent intent1 = new Intent(LoginActivity.this, 
    	MainActivity.class); 
    	startActivity(intent1); 
    	} 
    	} 
    	login.setOnClickListener(new OnClickListener() { 

    	@Override 
    	public void onClick(View v) { 
    	LoginMain(); 
    	} 
    	}); 
    	// 将点击的checkBOx存入到users中 
    	remberPass.setOnClickListener(new OnClickListener() { 

    	@Override 
    	public void onClick(View v) { 
    	Boolean isChecked1 = remberPass.isChecked(); 
    	ed.putBoolean("ISCHECK", isChecked1); 
    	ed.commit(); 
    	} 
    	}); 
    	// 设置自动登录默认为不点击 
    	Boolean value1 = sp.getBoolean("AUTO_ISCHECK", false); 
    	autoLogin.setChecked(value1); 
    	autoLogin.setOnClickListener(new OnClickListener() { 

    	@Override 
    	public void onClick(View v) { 
    	Boolean isChecked2 = autoLogin.isChecked(); 
    	ed.putBoolean("AUTO_ISCHECK", isChecked2); 
    	ed.commit(); 
    	} 
    	}); 
    	// 如果记住密码跟自动登录都被选中就选择登录跳转 
    	if (remberPass.isChecked() && autoLogin.isChecked()) { 
    	LoginMain(); 
    	} 
    }

    protected void LoginMain() { 
		// 将信息存入到users里面 
		ed.putString("oa_name", username.getText().toString()); 
		ed.putString("oa_pass", pass.getText().toString()); 
		ed.commit(); 
		if (TextUtils.isEmpty(username.getText().toString())) { 
		Toast.makeText(this, "请输入用户名", Toast.LENGTH_LONG).show(); 
		return; 
		} 
		if (TextUtils.isEmpty(pass.getText().toString())) { 
		Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show(); 
		return; 
		} 
		Intent intent = new Intent(LoginActivity.this, MainActivity.class); 
		startActivity(intent); 

		} 
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    
}
