package com.hp.imagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	
	private SearchView mSearchView;
	private GridView mGridView;
	private ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	private ImageResultsArrayAdapter imageAdapter;
	private String queryStr = "";
	private Settings mSettings;
	
	public static final int SETTINGS_REQUEST = 123;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		mSettings = new Settings();
		
		
		mGridView = (GridView) findViewById(R.id.gv_image_holder);
		imageAdapter = new ImageResultsArrayAdapter(this, imageResults);
		mGridView.setAdapter(imageAdapter);
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		
		});
		
		// Attach the listener to the AdapterView onCreate
		mGridView.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// TODO Auto-generated method stub
				
				makeAjaxCall(queryStr, totalItemsCount);
			}
		});
		
		
		
	}

	

	public void onFilterPress(MenuItem miSave){
		Intent i = new Intent(getApplicationContext(), FilterActivity.class);
		startActivityForResult(i, SETTINGS_REQUEST);		
	}
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		final Context mObject = this;
		
		getMenuInflater().inflate(R.menu.search, menu);
		
		mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
		mSearchView.setIconifiedByDefault(false);
        mSearchView.setSubmitButtonEnabled(false); 
        mSearchView.setQueryHint("Search Here");
        
        mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				Toast.makeText(mObject, "... "+ query, Toast.LENGTH_LONG).show();
				queryStr = query;
				//imageAdapter.clear();
				makeAjaxCall(queryStr, 0);
				
				return true;
			}

			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		return super.onCreateOptionsMenu(menu);
	}
	

	private void makeAjaxCall(String query, int page) {
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "https://ajax.googleapis.com/ajax/services/search/images?" +
		          "v=1.0&q="+ Uri.encode(query) + "&rsz=8&start="+page;
		
		if(mSettings != null && mSettings.size != null){
			url = url + "&imgsz="+mSettings.size + "";
		}
		
		if(mSettings!= null && mSettings.type != null){
			url = url + "&imgtype="+mSettings.type;
		}
		
		if(mSettings!= null && mSettings.color != null){
			url = url + "&imgcolor="+mSettings.color;
		}
		
		if(mSettings!= null && mSettings.site != null){
			url = url + "&as_sitesearch="+mSettings.site;
		}
		
		
		
		client.get(url, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response){
				JSONArray imageJsonResults = null;
				
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					//imageResults.clear();
					imageAdapter.addAll(ImageResult.fromJsonArray(imageJsonResults));
					
					Log.d("DEBUG", imageResults.toString());
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SETTINGS_REQUEST){
			if(resultCode == RESULT_OK){
				mSettings = (Settings) data.getSerializableExtra("settings");
				Toast.makeText(this, mSettings.toString() , Toast.LENGTH_LONG).show();
			}
		}
	}
 
}
