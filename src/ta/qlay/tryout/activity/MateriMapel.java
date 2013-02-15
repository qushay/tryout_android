package ta.qlay.tryout.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ta.qlay.tryout.library.JSONParser;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
public class MateriMapel extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> productsList;

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "user";
	private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "bab";

	// products JSONArray
	JSONArray products = null;

	//get parameter mapel dari Materi.java
	String pMapel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.materi_mapel);
		
		//get parameter mapel dari Materi.java
		Bundle bundle = this.getIntent().getExtras();
	    pMapel = bundle.getString("pMapel");
	    
	    TextView tv = (TextView) findViewById (R.id.title_text);
	    if (pMapel.equals("MTK")){
			if (tv != null) tv.setText ("Matematika");
	    }else if (pMapel.equals("BIN")){
			if (tv != null) tv.setText ("B. Indonesia");
	    }else if (pMapel.equals("BIG")){
			if (tv != null) tv.setText ("B. Inggris");
	    }else if (pMapel.equals("IPA")){
			if (tv != null) tv.setText ("IPA");
	    }
		// Hashmap for ListView
		productsList = new ArrayList<HashMap<String, String>>();

		// Loading products in Background Thread
		new LoadAllProducts().execute();

		// Get listview
		ListView lv = getListView();

		// on seleting single product
		// launching Edit Product Screen
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),MateriIsi.class);
				// sending pid to next activity
				in.putExtra(TAG_PID, pid);
				
				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
			}
		});

	}
	
	public void onClickHome (View v)
	{
		final Intent intent = new Intent(this, HomeActivity.class);
	    intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    this.startActivity (intent);
	}

	class LoadAllProducts extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MateriMapel.this);
			pDialog.setMessage("Loading materi. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mapel", pMapel));
			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl("materi_mapel", params);
			
			// Check your log cat for JSON reponse
			Log.d("All Products: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of Products
					products = json.getJSONArray(TAG_PRODUCTS);

					// looping through All Products
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_PID);
						String name = c.getString(TAG_NAME);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_PID, id);
						map.put(TAG_NAME, name);

						// adding HashList to ArrayList
						productsList.add(map);
					}
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

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							MateriMapel.this, productsList,
							R.layout.list_item, new String[] { TAG_PID,
									TAG_NAME},
							new int[] { R.id.pid, R.id.name });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}