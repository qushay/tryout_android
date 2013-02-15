package ta.qlay.tryout.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ta.qlay.tryout.activity.R;
import ta.qlay.tryout.library.JSONParser;

import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class Tryout extends DashboardActivity 
{

	// Shared Preference
	public static final String TAG_PREF = "SharedPreferences";
	//SQLite
	SQLiteDatabase myDB= null;
	SQLiteDatabase myDBTgl= null;
	String DBName = "TRYOUT_LOCAL.db";
	String TableSoal = "tbSoal";
	String TableHasil = "tbHasil";
	
	JSONParser jParser = new JSONParser();
	String vMapel;
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "user";
	private static final String TAG_KODE = "kode";

	JSONArray products = null;
	Random r;
	String spUser;
	int randKode;
	int[] kodeYgUdah;
	int[] vUdah,vBelum;

protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView (R.layout.tryout);
    setTitleFromActivityLabel (R.id.title_text);
    r=new Random();
    //Reading the Preferences File
    SharedPreferences s = getSharedPreferences(TAG_PREF, 0);
    spUser = s.getString("spUser", "");
    
    
}
public void getMapel(View view) {
	int id = view.getId ();
    switch (id) {
    case R.id.btn_mat :
    	vMapel="MTK";
    	kodeYgUdah();
    	randomKode();
       break;
	case R.id.btn_bin :
    	vMapel="BIN";
    	kodeYgUdah();
    	randomKode();
           break;
	case R.id.btn_big :
    	vMapel="BIG";
    	kodeYgUdah();
    	randomKode();
           break;
	case R.id.btn_ipa :
    	vMapel="IPA";
    	kodeYgUdah();
    	randomKode();
           break;
    default:
    	break;
    }
}

private void randomKode(){

	List<NameValuePair> params = new ArrayList<NameValuePair>();
	params.add(new BasicNameValuePair("mapel",vMapel));
	JSONObject json = jParser.getJSONFromUrl("tryout_set", params);

	Bundle bundle = new Bundle();
	Intent newIntent = new Intent(getApplicationContext(), TryoutMapel.class);
	
	try {
		int success = json.getInt(TAG_SUCCESS);
		if (success == 1) {
			products = json.getJSONArray(TAG_PRODUCTS);
			int vKode;
			int[] iKode =new int[products.length()];
			int iBelum=0;
			for (int i = 0; i < products.length(); i++) {
				JSONObject c = products.getJSONObject(i);
				iKode[i]=c.getInt(TAG_KODE); 
			}
			
			vBelum=new int[products.length()-kodeYgUdah.length];
			for (int i=0; i<products.length();i++) {
				int iSama=0;
				for (int j=0;j<kodeYgUdah.length;j++){
					if (iKode[i]==kodeYgUdah[j]){
						iSama=iSama+1;
					}
				}
				if (iSama==0){
					vBelum[iBelum]=
						iKode[i];
					iBelum=iBelum+1;;
				}
			}
			if (vBelum.length>0){
				Random r = new Random();
				vKode=r.nextInt(vBelum.length);
				randKode=vBelum[vKode];
		    	bundle.putString("pMapel", vMapel);
		    	bundle.putString("pKode", randKode+"");
			    newIntent.putExtras(bundle);
			    startActivityForResult(newIntent, 0);
			}else{
				Toast.makeText(getApplicationContext(), "Semua soal yang tersedia telah dikerjakan.", Toast.LENGTH_SHORT).show();
			}

		} else {
			
			Intent i = new Intent(getApplicationContext(),Tryout.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}
	} catch (JSONException e) {
		e.printStackTrace();
	}
}
@Override
public void onAttachedToWindow() {
	super.onAttachedToWindow();
    this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);           
}
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
    	startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        return false;
    }
    if (keyCode == KeyEvent.KEYCODE_HOME) {
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(Tryout.this);
		alertDialog.setTitle("Confirm Close ...");
        alertDialog.setMessage("Yakin mau keluar?");
        alertDialog.setIcon(R.drawable.flag_finish);
        alertDialog.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            	Intent intent = new Intent(Intent.ACTION_MAIN);
            	intent.addCategory(Intent.CATEGORY_HOME);
            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.cancel();
            }
        });
        alertDialog.show();
            return false;
    }
    return super.onKeyDown(keyCode, event);
}	
private String kodeYgUdah() {
	//ambil data set soal (kode) dan jumlah dari SQlite berdasarkan mapel yg dipilih
	String jml = null;
	try{
		myDB = this.openOrCreateDatabase(DBName,0, null);
		Cursor cur = myDB.rawQuery("SELECT DISTINCT(kode) AS kode FROM "+TableHasil+" WHERE user='"+spUser+"' AND mapel='"+vMapel+"'" , null);
		cur.moveToFirst();
		kodeYgUdah=new int[cur.getCount()];
		if (cur!=null&&cur.getCount()!=0){
			int Col2= cur.getColumnIndex("kode");
			int i=0;
			do {
				kodeYgUdah[i]=cur.getInt(Col2);
				i++;
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
} // end class
