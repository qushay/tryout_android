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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TryoutSetSoal extends ListActivity {


	//SQLite
	SQLiteDatabase myDB= null;
	SQLiteDatabase myDBTgl= null;
	String DBName = "TRYOUT_LOCAL.db";
	String TableSoal = "tbSoal";
	String TableHasil = "tbHasil";
	ArrayList<String> columnArray1 = new ArrayList<String>();
	String[] colStrArr1;
	
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> productsList;

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "user";
	private static final String TAG_KODE = "kode";
	private static final String TAG_NAME = "nama";
	private static final String TAG_JML = "jml";
	private static final String TAG_CEK = "cek";
	private static final String TAG_LOCK = "lock";

	// products JSONArray
	JSONArray products = null;

	//get parameter mapel dari Materi.java
	String pMapel;
	ListAdapter adapter;
	
	ImageView imgCek;
	int[] cekLock;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tryout_set);
		
		Bundle bundle = this.getIntent().getExtras();
	    pMapel = bundle.getString("pMapel");
	    imgCek=(ImageView)findViewById(R.id.kode_cek);
	    
		TextView  tv = (TextView) findViewById (R.id.title_text);
	    if (pMapel.equals("MTK")){
			if (tv != null) tv.setText ("Matematika");
	    }else if (pMapel.equals("BIN")){
			if (tv != null) tv.setText ("B. Indonesia");
	    }else if (pMapel.equals("BIG")){
			if (tv != null) tv.setText ("B. Inggris");
	    }else if (pMapel.equals("IPA")){
			if (tv != null) tv.setText ("IPA");
	    }
	    
	    cekLock = new int[]{
	        R.drawable.check,
	        R.drawable.lock
	    };
	    	
		productsList = new ArrayList<HashMap<String, String>>();


		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mapel",pMapel));
		JSONObject json = jParser.getJSONFromUrl("tryout_set", params);
		
		try {
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1) {
				products = json.getJSONArray(TAG_PRODUCTS);
				HashMap<String, String> map;
				String id,nama,jml,jmlmin,sKodemin,vLock;
				int iKode;
				String cek=Integer.toString(cekLock[1]);
				for (int i = 0; i < products.length(); i++) {
					JSONObject c = products.getJSONObject(i);

					id = c.getString(TAG_KODE);
					iKode=c.getInt(TAG_KODE);
					iKode=iKode-1;
					sKodemin=iKode+"";
					nama="Kode soal "+c.getString(TAG_KODE);
					jml=setJml(id);
					jmlmin=setJml(sKodemin);
					vLock="lock";
					
					
					if (jmlmin.equals("")&&jml.equals("")&&i==0){
						cek=Integer.toString(cekLock[0]);
						vLock="unlock";
					}else if (!jmlmin.equals("")&&jml.equals("")){
						cek=Integer.toString(cekLock[0]);
						jml="0";
						vLock="unlock";
					}else if(jmlmin.equals("")&&!jml.equals("")){
						cek=Integer.toString(cekLock[0]);
						vLock="unlock";
					}else if (jmlmin.equals("")&&jml.equals("")){
						cek=Integer.toString(cekLock[1]);
						vLock="lock";
					}else{
						cek=Integer.toString(cekLock[0]);
						vLock="unlock";
					}
					
					map = new HashMap<String, String>();

					map.put(TAG_KODE, id);
					map.put(TAG_LOCK, vLock);
					map.put(TAG_NAME, nama);
					map.put(TAG_JML, jml);
					map.put(TAG_CEK, cek);

					productsList.add(map);
				}
			} else {
				Intent i = new Intent(getApplicationContext(),Tryout.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		adapter = new SimpleAdapter(
				TryoutSetSoal.this, productsList,
				R.layout.list_kode, new String[] { TAG_KODE,TAG_LOCK,TAG_NAME,TAG_JML,TAG_CEK},
				new int[] { R.id.pid,R.id.kode_lock, R.id.kode_name, R.id.kode_jml,R.id.kode_cek });
		setListAdapter(adapter);	
	
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();
				String lock= ((TextView) view.findViewById(R.id.kode_lock)).getText().toString();
				
				if (!lock.equals("lock")){
					Bundle bundle = new Bundle();
					Intent newIntent = new Intent(getApplicationContext(), TryoutMapel.class);
					bundle.putString("pKode", pid);
					bundle.putString("pMapel", pMapel);
			    	newIntent.putExtras(bundle);
			    	startActivityForResult(newIntent, 0);
				}else{
					Toast.makeText(getApplicationContext(), "Terkunci!. Kerjakan soal sebelumnya untuk membuka.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
	}
	
	public void onClickHome (View v)
	{
		final Intent intent = new Intent(this, HomeActivity.class);
	    intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    this.startActivity (intent);
	}

	private String setJml(String kode) {
		//ambil data set soal (kode) dan jumlah dari SQlite berdasarkan mapel yg dipilih
		String jml = null;
		try{
			myDB = this.openOrCreateDatabase(DBName,0, null);
			Cursor cur = myDB.rawQuery("SELECT COUNT(kode) AS jml FROM "+TableHasil+" WHERE mapel='"+pMapel+"' AND kode="+kode+""  , null);
			cur.moveToFirst();
			String vJml;
			cur.moveToFirst();
			if (cur!=null&&cur.getCount()!=0){
				int Col2= cur.getColumnIndex("jml");
				do {
					vJml=cur.getString(Col2);
					if (vJml.equals("0")){
						jml=""; 
					}else{
						jml=vJml;
					}
				}
				while (cur.moveToNext());
			}
		}
		catch (Exception e){
			Log.e("Error", "Error",e);
		}
		finally{
			if (myDB!=null)
				myDB.close();
		}
		return jml;
	}
}