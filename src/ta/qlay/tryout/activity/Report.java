
package ta.qlay.tryout.activity;

import java.util.ArrayList;
import java.util.HashMap;

import ta.qlay.tryout.activity.R;
import ta.qlay.tryout.library.SimpleGestureFilter;
import ta.qlay.tryout.library.SimpleGestureFilter.SimpleGestureListener;
import android.R.color;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Report extends ListActivity implements SimpleGestureListener
{
	
    ArrayList<HashMap<String, String>> productsList;

	private static final String TAG_WAKTU = "waktu";
	private static final String TAG_MAPEL = "mapel";
	private static final String TAG_NILAI = "nilai";
	private static final String TAG_KODE = "kode";
	private static final String TAG_ID = "id";

	// Shared Preference
	public static final String TAG_PREF = "SharedPreferences";
	//SQLite
	SQLiteDatabase myDB= null;
	SQLiteDatabase myDBTgl= null;
	SQLiteDatabase myDBDialog= null;
	String DBName = "TRYOUT_LOCAL.db";
	String TableSoal = "tbSoal";
	String TableHasil = "tbHasil";
	String vJml,vBenar,vSalah,vKosong,vNilai,vKode,vMapel;
	
	ArrayList<String> columnArray1 = new ArrayList<String>();
	String[] colStrArr1;
	int iTgl;
	int maxData=0;
	private SimpleGestureFilter detector; 
	
	String spUser;
	TextView tvTgl;
	Button btnMtk,btnBin,btnBig,btnIpa;
	ListAdapter adapter;
	

protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView (R.layout.report);
	TextView tv = (TextView) findViewById (R.id.title_text);
	if (tv != null) tv.setText ("Rapor");

    //Reading the Preferences File
    SharedPreferences s = getSharedPreferences(TAG_PREF, 0);
    spUser = s.getString("spUser", "");
	
	tvTgl=(TextView)findViewById(R.id.report_tgl);
	btnMtk=(Button)findViewById(R.id.report_btnmtk);
	btnBin=(Button)findViewById(R.id.report_btnbin);
	btnBig=(Button)findViewById(R.id.report_btnbig);
	btnIpa=(Button)findViewById(R.id.report_btnipa);
	btnMtk.setBackgroundColor(getResources().getColor(R.color.title_text));
	btnMtk.setTextColor(getResources().getColor(R.color.mapel_text_footer));
	detector = new SimpleGestureFilter(this,this);
	iTgl=0;
	vMapel="MTK";
	
	// Hashmap for ListView
	productsList = new ArrayList<HashMap<String, String>>();
	try{
		myDBTgl = this.openOrCreateDatabase(DBName,0, null);
		Cursor cur = myDBTgl.rawQuery("SELECT DISTINCT(DATE(tgl)) tgl FROM "+TableHasil+" WHERE user='"+spUser+"' ORDER BY tgl DESC "  , null);
		int Col1= cur.getColumnIndex("tgl");
		cur.moveToFirst();
		if (cur!=null){
			do {
				columnArray1.add(cur.getString(Col1));
			}
			while (cur.moveToNext());

			colStrArr1 = (String[]) columnArray1.toArray(new String[columnArray1.size()]);
			maxData= colStrArr1.length;
			tvTgl.setText(colStrArr1[iTgl]);
		}
		isiList("MTK");
		
	}
	catch (Exception e){
		Log.e("Error", "Error",e);
	}
	finally{
		if (myDBTgl!=null)
			myDBTgl.close();
	}
	
	
	
   adapter = new SimpleAdapter(
		Report.this, productsList,
		R.layout.list_item2, new String[] {TAG_WAKTU,TAG_MAPEL,TAG_NILAI},new int[] { R.id.report_waktu, R.id.report_mapel, R.id.report_nilai });
	// updating listview
	setListAdapter(adapter);
	
	 btnMtk.setOnClickListener(new View.OnClickListener() { // tab MTK
	    	public void onClick(View view) {
				isiList("MTK");
				vMapel="MTK";
				buttonColor();
				btnMtk.setBackgroundColor(getResources().getColor(R.color.title_text));
				btnMtk.setTextColor(getResources().getColor(R.color.mapel_text_footer));
			}
		});
	 btnBig.setOnClickListener(new View.OnClickListener() { // tab B. Indo
	    	public void onClick(View view) {
				isiList("BIG");vMapel="BIG";
				buttonColor();
				btnBig.setBackgroundColor(getResources().getColor(R.color.title_text));
				btnBig.setTextColor(getResources().getColor(R.color.mapel_text_footer));
			}
		});
	 btnBin.setOnClickListener(new View.OnClickListener() { // tab B. Ing
	    	public void onClick(View view) {
				isiList("BIN");vMapel="BIN";
				buttonColor();
				btnBin.setBackgroundColor(getResources().getColor(R.color.title_text));
				btnBin.setTextColor(getResources().getColor(R.color.mapel_text_footer));
			}
		});
	 btnIpa.setOnClickListener(new View.OnClickListener() { // tab IPA
	    	public void onClick(View view) {
				isiList("IPA");vMapel="IPA";
				buttonColor();
				btnIpa.setBackgroundColor(getResources().getColor(R.color.title_text));
				btnIpa.setTextColor(getResources().getColor(R.color.mapel_text_footer));
			}
		});
	 
	 // mengeset isi list
	 ListView list = getListView();
	 list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				String id=""+productsList.get(arg2).get(TAG_ID);
				
				isiDialog(id);
				onClick(arg1);
			}
		});	
}
	
	//jika list di klik akan muncul dialog window
	public void onClick(View arg0) {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setMessage("Jumlah Soal : "+vJml+"\nBenar : "+vBenar+"\nSalah : "+vSalah+"\nKosong : "+vKosong+"\nNilai : "+vNilai);
		alertbox.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
           }
       });
       alertbox.show();	    	
	}

@Override 
public boolean dispatchTouchEvent(MotionEvent me){ 
  this.detector.onTouchEvent(me);
 return super.dispatchTouchEvent(me); 
}

// aksi jika swipe (geser)
@Override
public void onSwipe(int direction) {
	String str = "";
	  switch (direction) {
	  	  case SimpleGestureFilter.SWIPE_RIGHT : 
	  		  str = "Swipe Right";
	  		  if (iTgl<maxData-1){
		  		  iTgl=iTgl+1;
		  		  str=colStrArr1[iTgl];
		  		  tvTgl.setText(str);
		  		  isiList(vMapel);

		  		  Toast.makeText(this, str+"", Toast.LENGTH_SHORT).show();
	  		  }
	      break;
	  	  case SimpleGestureFilter.SWIPE_LEFT :  
	  		  str = "Swipe Left";
	  		  if (iTgl>0){
		  		  iTgl=iTgl-1;
		  		  str=colStrArr1[iTgl];
		  		  tvTgl.setText(str);
		  		  isiList(vMapel);
		  		  Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	  		  }
	      break;
	  }

}

@Override
public void onDoubleTap() {
	// TODO Auto-generated method stub

}

// isi dari list
private void isiList(String mapel){
	try{
	myDB = this.openOrCreateDatabase(DBName,0, null);
	Cursor c = myDB.rawQuery("SELECT id,mapel,kode,nilai,TIME(tgl) waktu FROM "+TableHasil+" WHERE user='"+spUser+"' AND mapel='"+mapel+"' AND DATE(tgl)='"+colStrArr1[iTgl]+"'"  , null);
	
	String vWaktu,vMapel,vNilai,vId,vKode;
	productsList.clear();
	c.moveToFirst();
	if (c!=null&&c.getCount()!=0){
		int Coloumn1= c.getColumnIndex("waktu");
		int Coloumn2= c.getColumnIndex("mapel");
		int Coloumn3= c.getColumnIndex("nilai");
		int Coloumn4= c.getColumnIndex("id");
		int Coloumn5= c.getColumnIndex("kode");
		do {
			vId=c.getString(Coloumn4);
			vWaktu= c.getString(Coloumn1);
			vWaktu=vWaktu.substring(0,5);
			vMapel= c.getString(Coloumn2);
			if (vMapel.equals("MTK")){
				vMapel="Matematika";
			}else if (vMapel.equals("BIN")){
				vMapel="Bahasa Indonesia";
			}else if (vMapel.equals("BIG")){
				vMapel="Bahasa Inggris";
			}else if (vMapel.equals("IPA")){
				vMapel="IPA";
			}
			vKode=c.getString(Coloumn5);
			vNilai= c.getString(Coloumn3);
			
			HashMap<String, String> map= new HashMap<String, String>();

			// adding each child node to HashMap key => value

			map.put(TAG_ID, vId);
			map.put(TAG_WAKTU, vWaktu);
			map.put(TAG_MAPEL, vMapel);
			map.put(TAG_KODE, vKode);
			map.put(TAG_NILAI, vNilai);
			// adding HashList to ArrayList
			productsList.add(map);
		}
		while (c.moveToNext());
    	
	}
	setListAdapter(adapter);
	}
	catch (Exception e){
		Log.e("Error", "Error",e);
	}
	finally{
		if (myDB!=null)
			myDB.close();
	}
}

// isi dialog window jika list di klik
private void isiDialog(String id){
	try{
	myDBDialog = this.openOrCreateDatabase(DBName,0, null);
	Cursor c = myDBDialog.rawQuery("SELECT jml_soal,benar,salah,kosong,nilai FROM "+TableHasil+" WHERE id='"+id+"'"  , null);
	
	c.moveToFirst();
	if (c!=null&&c.getCount()!=0){
		int Coloumn1= c.getColumnIndex("jml_soal");
		int Coloumn2= c.getColumnIndex("benar");
		int Coloumn3= c.getColumnIndex("salah");
		int Coloumn4= c.getColumnIndex("kosong");
		int Coloumn5= c.getColumnIndex("nilai");
			vJml= c.getString(Coloumn1);
			vBenar= c.getString(Coloumn2);
			vSalah= c.getString(Coloumn3);
			vKosong= c.getString(Coloumn4);
			vNilai= c.getString(Coloumn5);

	}
	}catch (Exception e){
		Log.e("Error", "Error",e);
	}
	finally{
		if (myDBDialog!=null)
			myDBDialog.close();
	}
}

// jika tombol home di klik akan kemali ke menu utama
public void onClickHome (View v)
	{
		final Intent intent = new Intent(this, HomeActivity.class);
	    intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    this.startActivity (intent);
	}

private void buttonColor(){
	btnMtk.setTextColor(getResources().getColor(R.color.title_text));
	btnBin.setTextColor(getResources().getColor(R.color.title_text));
	btnBig.setTextColor(getResources().getColor(R.color.title_text));
	btnIpa.setTextColor(getResources().getColor(R.color.title_text));
	btnMtk.setBackgroundColor(getResources().getColor(R.color.mapel_footer));
	btnBin.setBackgroundColor(getResources().getColor(R.color.mapel_footer));
	btnBig.setBackgroundColor(getResources().getColor(R.color.mapel_footer));
	btnIpa.setBackgroundColor(getResources().getColor(R.color.mapel_footer));
}
} // end class
