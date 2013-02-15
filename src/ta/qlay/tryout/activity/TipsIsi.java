package ta.qlay.tryout.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ta.qlay.tryout.activity.R;
import ta.qlay.tryout.library.JSONParser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TipsIsi extends DashboardActivity 
{
	String[] isiList;
	TextView materiTitle,materiIsi;
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	// products JSONArray
	JSONArray products = null;

	
	String pid;
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "user";
	private static final String TAG_PID = "pid";
	private static final String TAG_TITLE= "title";
	private static final String TAG_ISI = "isi";
	
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView (R.layout.materi_isi);
	    setTitleFromActivityLabel (R.id.title_text);
	    // getting product details from intent
	 	Intent i = getIntent();
	 	// getting product id (pid) from intent
	 	pid = i.getStringExtra(TAG_PID);
	    
	    materiTitle=(TextView)findViewById(R.id.materi_title);
	    materiIsi=(TextView)findViewById(R.id.materi_isi);
	    
	    new LoadAllProducts().execute();
	}
	
	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadAllProducts extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TipsIsi.this);
			pDialog.setMessage("Loading tips. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... params) {
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
				// Check for success tag
				int success;
					try {
					// Building Parameters
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("id_tips", pid));
						// getting JSON string from URL
						JSONObject json = jParser.getJSONFromUrl("tips_isi", params);
						
						// Check your log cat for JSON reponse
						Log.d("All Products: ", json.toString());
				
						// Checking for SUCCESS TAG
						success = json.getInt(TAG_SUCCESS);
		
						if (success == 1) {
							// successfully received product details
							JSONArray productObj = json.getJSONArray(TAG_PRODUCT); // JSON Array
							
							// get first product object from JSON Array
							JSONObject product = productObj.getJSONObject(0);
		
							// display product data in EditText
							materiTitle.setText(product.getString(TAG_TITLE));
							materiIsi.setText(product.getString(TAG_ISI));
						} else {
							// no products found
							// Launch Add New product Activity
							Intent i = new Intent(getApplicationContext(),
									Materi.class);
							// Closing all previous activities
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(i);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
		}

	}
    
} // end class