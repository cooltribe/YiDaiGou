package com.searun.yidaigou;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final static int SCANNIN_GREQUEST_CODE = 1;
	private Activity mActivity = null;
	private WebView mWebView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = this;
		
		showWebView();
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void showWebView(){		// webView与js交互代码
		try {
			mWebView = new WebView(this);
			setContentView(mWebView);
			
			mWebView.requestFocus();
			
			mWebView.setWebChromeClient(new WebChromeClient(){
				@Override
				public void onProgressChanged(WebView view, int progress){
					MainActivity.this.setTitle("Loading...");
					MainActivity.this.setProgress(progress);
					
					if(progress >= 80) {
						MainActivity.this.setTitle("JsAndroid Test");
					}
				}
			});
			
			mWebView.setOnKeyListener(new View.OnKeyListener() {		// webview can go back
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
						mWebView.goBack();
						return true;
					}
					return false;
				}
			});
			
			WebSettings webSettings = mWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setDefaultTextEncodingName("utf-8");

			mWebView.addJavascriptInterface(getHtmlObject(), "jsObj");
			mWebView.loadUrl("file:///android_asset/index.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Object getHtmlObject(){
		Object insertObj = new Object(){
//			public String HtmlcallJava(){
//				return "Html call Java";
//			}
			public void HtmlcallJava(){
				Intent intent = new Intent(MainActivity.this, CameraTestActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
			public String HtmlcallJava2(final String param){
				return "Html call Java : " + param;
			}
			
			public void JavacallHtml(){
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mWebView.loadUrl("javascript: showFromHtml()");
						Toast.makeText(MainActivity.this, "clickBtn", Toast.LENGTH_SHORT).show();
					}
				});
			}
			
			public void JavacallHtml2(){
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mWebView.loadUrl("javascript: showFromHtml2('IT-homer blog')");
						Toast.makeText(MainActivity.this, "clickBtn2", Toast.LENGTH_SHORT).show();
					}
				});
			}
		};
		
		return insertObj;
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				//显示扫描到的内容
//				mTextView.setText(bundle.getString("result"));
//				//显示
//				mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
				new AlertDialog.Builder(mActivity).setMessage(bundle.getString("result")).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				}).show();
			}
			break;
		}
    }	
}
