package com.hp.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class FilterActivity extends Activity {

	private Settings settings;
	private Spinner spinner_type;
	private Spinner spinner_size;
	private EditText etColor;
	private EditText etSite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		
		settings = new Settings();
		
		spinner_type = (Spinner) findViewById(R.id.spinner_type);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(this,
		        R.array.type_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner_type.setAdapter(adapter_type);
		
		
		spinner_size = (Spinner) findViewById(R.id.spinner_size);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter_size = ArrayAdapter.createFromResource(this,
		        R.array.size_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter_size.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner_size.setAdapter(adapter_size);
		
		etColor  = (EditText) findViewById(R.id.et_color);
		etSite = (EditText) findViewById(R.id.et_site);
		
		
		
		
		spinner_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				Object item = parent.getItemAtPosition(position);
//				settings.type = item.toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		
		spinner_size.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				Object item = parent.getItemAtPosition(position);
//				settings.size = item.toString();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}

	
	public void onApplyPress(View v){
		settings.color = etColor.getText().toString();
		settings.site = etSite.getText().toString();
		
		settings.type = spinner_type.getSelectedItem().toString();
		settings.size = spinner_size.getSelectedItem().toString();
		
		//Setup the result of the intent
		Intent data = new Intent();
		//data.putExtra(MainActivity.FOO_KEY, tvFoo.getText().toString());
		data.putExtra("settings", settings);
		setResult(RESULT_OK, data);
		finish();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter, menu);
		return true;
	}

}
