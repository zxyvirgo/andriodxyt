package com.example.xiaoyuantong;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class WriteLAndFActivity extends Activity {

	private RequestQueue requestQueue ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_land_f);
		//获取前台的东西提交到后台
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.write_land, menu);
		return true;
	}
	
	 private void init() {
		 requestQueue = Volley.newRequestQueue(this);    
		 getJson();
		 //获取填入的数据，然后提交到后台
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
