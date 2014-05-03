package com.example.xiaoyuantong;


import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class PersonalInfoActivity extends Activity {
	
	private EditText userNameEditText;
	private String userName;
	private Spinner sexSpinner;
	//private ArrayAdapter sexAdapter;
	private Spinner academySpinner;
	private Spinner majorSpinner;
	private Spinner gradeSpinner;
	private EditText emailEditText;
	private String Email;
	private EditText phoneNumberEditText;
	private String phoneNumber;
	private EditText homeTownEditText;
	private String homeTown;
	private EditText QQEditText;
	private String QQ;
	private Spinner graduatePlanSpinner;
	private Button reset;
	private Button register;
	
	private String serverIp = "192.168.1.79";
	private String serverPort = "8080";
	private Bundle bundle;
	private String httpStr;
	private String postStr;
	private String questStr;
	
	private RequestQueue requestQueue ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);

		userNameEditText=(EditText)findViewById(R.id.etUserName);
		sexSpinner = (Spinner) findViewById(R.id.sex);
		//将可选内容与ArrayAdapter连接起来
		//sexAdapter = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
		//将sexAdapter 添加到spinner中
		//sexSpinner.setAdapter(sexAdapter);
		academySpinner = (Spinner) findViewById(R.id.academy);
		majorSpinner = (Spinner) findViewById(R.id.major);
		gradeSpinner = (Spinner) findViewById(R.id.grade);
		emailEditText=(EditText)findViewById(R.id.etEmail);
		phoneNumberEditText=(EditText)findViewById(R.id.etPhoneNumber);
		homeTownEditText=(EditText)findViewById(R.id.etHomeTown);
		QQEditText=(EditText)findViewById(R.id.etQQ);
		graduatePlanSpinner = (Spinner) findViewById(R.id.graduatePlan);
		reset = (Button) findViewById(R.id.reset);
		register = (Button) findViewById(R.id.register);
		
		//添加事件Spinner事件监听  
		sexSpinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		academySpinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		majorSpinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		gradeSpinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		graduatePlanSpinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		
		//设置默认值
		sexSpinner.setVisibility(View.VISIBLE);
		academySpinner.setVisibility(View.VISIBLE);
		majorSpinner.setVisibility(View.VISIBLE);
		gradeSpinner.setVisibility(View.VISIBLE);
		graduatePlanSpinner.setVisibility(View.VISIBLE);
		
		reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				userNameEditText.setText("");
				sexSpinner.setSelection(0);
				academySpinner.setSelection(0);
				majorSpinner.setSelection(0);
				gradeSpinner.setSelection(0);
				emailEditText.setText("");
				phoneNumberEditText.setText("");
				homeTownEditText.setText("");
				QQEditText.setText("");
				graduatePlanSpinner.setSelection(0);
			}
		});
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (registerIsSuccsee()) {
					register();
				}
				
			}

		});
		
		init();
	}

	//使用XML形式操作
	class SpinnerXMLSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			//view2.setText("你使用什么样的手机："+adapter2.getItem(arg2));
			//Toast.makeText(PersonalInfoActivity.this, sexAdapter.getItem(arg2).toString(), Toast.LENGTH_SHORT).show();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
		
	}
	
	/*
	 * 判断用户注册输入是否规范 录入信息验证 验证是否通过
	 */

	private boolean registerIsSuccsee(){
    	//获取用户输入的信息
    	userName=userNameEditText.getText().toString();
    	Email=emailEditText.getText().toString();
    	if("".equals(userName)||"".equals(Email)){
    		//用户输入用户名为空
    		Toast.makeText(PersonalInfoActivity.this, "用户名或邮箱不能为空，为便于联系，请大家谅解!", Toast.LENGTH_SHORT).show();
    		return false;
    	}else 
    		return true;
    }
	

	/**
	 * 注册
	 */
	private void register() {
		userName = userNameEditText.getText().toString();
		
		httpStr = "http://";
		postStr = httpStr + serverIp + ":" + serverPort
				+ "/RegisterAndLogin/register";
		questStr = "{REGISTER:{username:'" + userName + "'}}";
		System.out.println("====questStr====" + questStr);
		System.out.println("====postStr====" + postStr);
		try {
			HttpParams httpParams = new BasicHttpParams();
			// 设置连接超时
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParams,
					timeoutConnection);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpPost httpPost = new HttpPost(postStr);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("username", userName));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			InputStream is=entity.getContent();
			String isUser = ConvertStreamToString(is);
			
			System.out.println(isUser);
			if("success".equals(isUser)){
				//表示用户注册成功
				AlertDialog.Builder builder=new Builder(PersonalInfoActivity.this);
				builder.setMessage("恭喜你！注册成功");
				builder.setTitle("提示");
				 builder.setPositiveButton("确认", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i=new Intent(PersonalInfoActivity.this,MainActivity.class);
						dialog.dismiss();
						startActivity(i);
					}
				}).show();
			}else if("userExist".equals(isUser)){
				//用户已经被注册
				AlertDialog.Builder builder=new Builder(PersonalInfoActivity.this);
				builder.setMessage("抱歉！该用户已被注册！");
				builder.setTitle("提示");
				builder.setPositiveButton("确认", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		
		}
	}
	// 读取字符流
	public String ConvertStreamToString(InputStream is) {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String returnStr = "";
		try {
			while ((returnStr = br.readLine()) != null) {
				sb.append(returnStr);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		final String result = sb.toString();

		System.out.println(result);
		return result;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.personal_info, menu);
		return true;
	}
	
	private void init() {	 
		 requestQueue = Volley.newRequestQueue(this);    
		 getJson();
		 
		 //获取输入的数据，然后提交到后台
	 }
	 
	
	private void getJson(){
    	//��ʼ��volley
    		 
    	 String url = "http://192.168.20.1:8080/xiaoyuantong/userAction!reg.action";
    	 
    	  JsonObjectRequest jsonObjectRequest ;
//������ʹ��volley
    	  JSONObject jsonObject=new JSONObject() ;
    	  
        // Map<String, String> map = new HashMap<String, String>();
    	  try {
			jsonObject.put("name", "张三");
			jsonObject.put("sex", "女");
			jsonObject.put("college", "会计学院");
			jsonObject.put("major", "会计学");
			jsonObject.put("grade", "大三");
			jsonObject.put("mail", "女");
			jsonObject.put("tel", "女");
			jsonObject.put("qq", "女");
			jsonObject.put("hometown", "女");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	Log.e("post",jsonObject.toString());
        
         try{
    	 jsonObjectRequest = new JsonObjectRequest(
                   Request.Method.POST, url, jsonObject,
                   new Response.Listener<JSONObject>() {
                       @Override
                       public void onResponse(JSONObject response) {
                           //���?�ص�JSON���
                           Log.e("bbb", response.toString());
                      
                           //根据前面的标注获取字符串
							Object it = response.opt("success");
							
							Log.e("success",it.toString());

                               }
                       
                   }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError arg0) {
                           // System.out.println("sorry,Error");
                           Log.e("aaa", arg0.toString());
                       }
                   });
    	 
    	    requestQueue.add(jsonObjectRequest);
         } catch (Exception e) {
             e.printStackTrace();
             System.out.println(e + ""); 
         } 
         requestQueue.start();
       
       
    	
    }

}
