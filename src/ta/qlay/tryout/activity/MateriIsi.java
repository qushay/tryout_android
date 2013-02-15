package ta.qlay.tryout.activity;

import java.io.InputStream;
import java.net.URL;
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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MateriIsi extends DashboardActivity 
{
	String[] isiList;
	TextView materiTitle,materiIsi;
	// Progress Dialog
	private ProgressDialog pDialog;
	

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	// products JSONArray
	JSONArray products = null;
	Drawable drawMateri;ImageView materiGbr;

	
	String pid;
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "user";
	private static final String TAG_PID = "pid";
	private static final String TAG_TITLE= "bab";
	private static final String TAG_ISI = "materi";
	private static final String TAG_GBR_MATERI = "gbr_materi";
	
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView (R.layout.materi_isi);
	    setTitleFromActivityLabel (R.id.title_text);
	 	Intent i = getIntent();
	 	pid = i.getStringExtra(TAG_PID);
	    
	    materiTitle=(TextView)findViewById(R.id.materi_title);
	    materiIsi=(TextView)findViewById(R.id.materi_isi);
	    materiGbr=(ImageView)findViewById(R.id.materi_gambar);
	    
	    new LoadAllProducts().execute();
	}
	
	class LoadAllProducts extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MateriIsi.this);
			pDialog.setMessage("Loading materi. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		protected String doInBackground(String... params) {
			runOnUiThread(new Runnable() {
				public void run() {
				int success;
					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("id_materi", pid));
						// getting JSON string from URL
						JSONObject json = jParser.getJSONFromUrl("materi_isi", params);
						
						// Check your log cat for JSON reponse
						Log.d("All Products: ", json.toString());
				
						// Checking for SUCCESS TAG
						success = json.getInt(TAG_SUCCESS);
		
						if (success == 1) {
							JSONArray productObj = json.getJSONArray(TAG_PRODUCT); // JSON Array
							JSONObject product = productObj.getJSONObject(0);
		
							// display product data in EditText
							materiTitle.setText(product.getString(TAG_TITLE));
							String textIsi=product.getString(TAG_ISI);

							String gbrMateri=product.getString(TAG_GBR_MATERI);
							drawMateri = LoadImageFromWebOperations(jParser.url_gbr_materi+gbrMateri+".png");
							
							if (textIsi.equals("null")||textIsi.equals("")){
								materiGbr.setImageDrawable(drawMateri);
							}else{
								textIsi = textIsi.replaceAll("#","\n");
								materiIsi.setText(textIsi);
							}
							
						} else {
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

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			
		}
		
		private Drawable LoadImageFromWebOperations(String url){
			try{
				InputStream is = (InputStream) new URL(url).getContent();
				Drawable d = Drawable.createFromStream(is, "src name");
				return d;
			}catch (Exception e) {
				System.out.println("Exc="+e);
				return null;
			}
		}

	}
    
} // end class