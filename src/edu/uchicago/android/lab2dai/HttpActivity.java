package edu.uchicago.android.lab2dai;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HttpActivity extends Activity {
	public static final String tag = "HttpActivity";
	
	private HttpClient client;
	
	private EditText url_text;
	private TextView header;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.http);
		url_text = (EditText)findViewById(R.id.url_text);
		header = (TextView)findViewById(R.id.header);
		
		//assuming network connectivity
		// TODO check connectivity
		
		client = new DefaultHttpClient();
		
		((Button)findViewById(R.id.get_btn)).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						try {
							HttpGet request = new HttpGet(url_text.getText().toString());
							HttpResponse response = client.execute(request);
							HeaderIterator it = response.headerIterator();
							header.setText("");
							while (it.hasNext()) {
								header.append(it.nextHeader().toString());
							}
						}
						catch (Exception e) {
							Log.e(tag, e.toString());
							Toast.makeText(HttpActivity.this, "Error: Please check your URL.", Toast.LENGTH_SHORT).show();
						}
						
					}
				});

	}
}
