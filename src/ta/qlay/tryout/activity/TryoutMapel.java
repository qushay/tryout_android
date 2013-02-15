package ta.qlay.tryout.activity;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ta.qlay.tryout.activity.DashboardActivity;
import ta.qlay.tryout.activity.R;
import ta.qlay.tryout.library.JSONParser;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import android.widget.ImageView;

public class TryoutMapel extends DashboardActivity 
{
		TextView tvnomer_soal,ttimer,tbenar,tsalah,tkosong,tnilai,tv,tsoal,tpage,tpil1,tpil2,tpil3,tpil4,tsolusi_benar;
		LinearLayout laySolusi,layHasil,alert,laySoal,layFooter,layPilihan,layPil1,layPil2,layPil3,layPil4;
		Button btnSelesai,btnSolusi;
		CheckBox cb1,cb2,cb3,cb4;
		String gTimer;
		ImageButton btnBack,btnNext,btnFinish;
		public NotificationManager nm;
		Notification notif;
		PendingIntent contentIntent;
		String tickerText;
		String pMapel,pKode,spUser;
		String [] vId,vSoal,vSolusi,vSolusibenar,vGbrsoal,vGbrsolusi,vGbrsolusibenar,vPil1,vGbrpil1,vPil2,vGbrpil2,vPil3,vGbrpil3,vPil4,vGbrpil4,vPilihan,vJwbPil1,vJwbPil2,vJwbPil3,vJwbPil4,vJawaban;
		int k=1;int maxSoal;
		ImageView tsoal_gbr,tpil1_gbr,tpil2_gbr,tpil3_gbr,tpil4_gbr,tsolusi_gbr,tsolusi_gbrbenar;
		Drawable [] drawSoal,drawPil1,drawPil2,drawPil3,drawPil4,drawSolusi,drawSolusiBenar;
		CountDownTimer timer;
		private ProgressDialog pDialog;
		JSONParser jParser = new JSONParser();
		Array productsList;
		
		// Shared Preference
		public static final String TAG_PREF = "SharedPreferences";
		// JSON Node names
		private static final String TAG_SUCCESS = "success";
		private static final String TAG_PRODUCTS = "user";
		private static final String TAG_PID = "pid";
		private static final String TAG_SOAL = "soal";
		private static final String TAG_GBR_SOAL = "gbr_soal";
		private static final String TAG_PIL1 = "pil1";
		private static final String TAG_GBR_PIL1 = "gbr_pil1";
		private static final String TAG_PIL2 = "pil2";
		private static final String TAG_GBR_PIL2 = "gbr_pil2";
		private static final String TAG_PIL3 = "pil3";
		private static final String TAG_GBR_PIL3 = "gbr_pil3";
		private static final String TAG_PIL4 = "pil4";
		private static final String TAG_GBR_PIL4 = "gbr_pil4";
		private static final String TAG_SOLUSI = "solusi";
		private static final String TAG_GBR_SOLUSI = "gbr_solusi";
		private static final String TAG_STAT1= "stat1";
		private static final String TAG_STAT2= "stat2";
		private static final String TAG_STAT3= "stat3";
		private static final String TAG_STAT4= "stat4";
		// products JSONArray
		JSONArray products = null;
		List<NameValuePair> params;
		JSONObject json;
		//Simpan Image to SDCard
		String extStorageDirectory;
		Bitmap bm;
		OutputStream outStream;
		File file;
		//SQLite
		SQLiteDatabase myDB= null;
		String DBName = "TRYOUT_LOCAL.db";
		String TableSoal = "tbSoal";
		String TableHasil = "tbHasil";
		
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView (R.layout.tryout_mapel);
	    setTitleFromActivityLabel (R.id.title_text);
	    //Reading the Preferences File
        SharedPreferences s = getSharedPreferences(TAG_PREF, 0);
        spUser = s.getString("spUser", "");
	    
	    ttimer =(TextView)findViewById(R.id.timer);
	    layHasil = (LinearLayout)findViewById(R.id.layout_hasil);
	    laySolusi = (LinearLayout)findViewById(R.id.layout_solusi);
	    laySoal = (LinearLayout)findViewById(R.id.layout_soal);
	    layPilihan = (LinearLayout)findViewById(R.id.layout_pilihan);
	    layFooter = (LinearLayout)findViewById(R.id.tryout_footer);
	    tsoal=(TextView)findViewById(R.id.tryout_soal);//soal
	    tvnomer_soal=(TextView)findViewById(R.id.tryout_nomer_soal);
	    tsoal_gbr =(ImageView)findViewById(R.id.gbr_soal);
	    tsolusi_benar=(TextView)findViewById(R.id.tryout_jawaban);//solusi
	    tsolusi_gbrbenar=(ImageView)findViewById(R.id.gbr_jawaban);
	    layPil1=(LinearLayout)findViewById(R.id.lay_pil1);//pilihan
	    layPil2=(LinearLayout)findViewById(R.id.lay_pil2);
	    layPil3=(LinearLayout)findViewById(R.id.lay_pil3);
	    layPil4=(LinearLayout)findViewById(R.id.lay_pil4);
	    tpil1=(TextView)findViewById(R.id.tryout_pilihan1);
	    tpil1_gbr =(ImageView)findViewById(R.id.gbr_pilihan1);
	    cb1=(CheckBox)findViewById(R.id.chk_pil1);
	    tpil2=(TextView)findViewById(R.id.tryout_pilihan2);
	    tpil2_gbr =(ImageView)findViewById(R.id.gbr_pilihan2);
	    cb2=(CheckBox)findViewById(R.id.chk_pil2);
	    tpil3=(TextView)findViewById(R.id.tryout_pilihan3);
	    tpil3_gbr =(ImageView)findViewById(R.id.gbr_pilihan3);
	    cb3=(CheckBox)findViewById(R.id.chk_pil3);
	    tpil4=(TextView)findViewById(R.id.tryout_pilihan4);
	    tpil4_gbr =(ImageView)findViewById(R.id.gbr_pilihan4);
	    cb4=(CheckBox)findViewById(R.id.chk_pil4);
	    tpage=(TextView)findViewById(R.id.tryout_counter);
	    alert = (LinearLayout)findViewById(R.id.tryout_alert);
	    tbenar=(TextView)findViewById(R.id.jawaban_benar);
	    tsalah=(TextView)findViewById(R.id.jawaban_salah);
	    tkosong=(TextView)findViewById(R.id.jawaban_kosong);
	    tnilai=(TextView)findViewById(R.id.jawaban_nilai);
	    btnSelesai=(Button)findViewById(R.id.btn_selesai);
	    btnSolusi=(Button)findViewById(R.id.btn_solusi);
	    btnFinish=(ImageButton)findViewById(R.id.tryout_finish);
	    btnBack=(ImageButton)findViewById(R.id.btn_back);
	    btnBack.setEnabled(false);
		btnBack.setImageResource(R.drawable.title_back_selected);
	    btnNext=(ImageButton)findViewById(R.id.btn_next);
	    
	    extStorageDirectory= Environment.getExternalStorageDirectory().toString();
	    
	    Bundle bundle = this.getIntent().getExtras();
	    pMapel = bundle.getString("pMapel");
	    pKode = bundle.getString("pKode");
	    
	    tv = (TextView) findViewById (R.id.title_text);
	    if (pMapel.equals("MTK")){
			if (tv != null) tv.setText ("Matematika");
	    }else if (pMapel.equals("BIN")){
			if (tv != null) tv.setText ("B. Indonesia");
	    }else if (pMapel.equals("BIG")){
			if (tv != null) tv.setText ("B. Inggris");
	    }else if (pMapel.equals("IPA")){
			if (tv != null) tv.setText ("IPA");
	    }
	    new LoadAllProducts().execute();
	    
	    btnSelesai.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
				startActivity(new Intent(getApplicationContext(),Tryout.class));
			}
		});
	    btnSolusi.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
	    		k=-1;
	    		nextSoal();
	    		layHasil.setVisibility(View.GONE);
	    		laySoal.setVisibility(View.VISIBLE);
	    		laySolusi.setVisibility(View.VISIBLE);
	    		layFooter.setVisibility(View.VISIBLE);
	    		if (vSolusibenar[0].equals("null")){
	    			tsolusi_benar.setVisibility(View.GONE);
	    			tsolusi_gbrbenar.setVisibility(View.VISIBLE);
	    			tsolusi_gbrbenar.setImageDrawable(drawSolusiBenar[0]);
	    		}else{
	    			tsolusi_gbrbenar.setVisibility(View.GONE);
	    			tsolusi_benar.setVisibility(View.VISIBLE);
	    			tsolusi_benar.setText(vSolusibenar[0]);
	    		}
			}
		});
	    alert.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
				alert.setVisibility(View.INVISIBLE);
				layHasil.setVisibility(View.VISIBLE);
			}
		});
	    layPil1.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
	    		vPilihan[k]="a";
	    		cbCheckSelect();
	    		cb1.setSelected(true);
	    		cb1.setChecked(true);
	    		nextSoal();
			}
		});
	    layPil2.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
	    		vPilihan[k]="b";
	    		cbCheckSelect();
	    		cb2.setSelected(true);
	    		cb2.setChecked(true);
	    		nextSoal();
			}
		});
	    layPil3.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
	    		vPilihan[k]="c";
	    		cbCheckSelect();
	    		cb3.setSelected(true);
	    		cb3.setChecked(true);
	    		nextSoal();
			}
		});
	    layPil4.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
	    		vPilihan[k]="d";
	    		cbCheckSelect();
	    		cb4.setSelected(true);
	    		cb4.setChecked(true);
	    		nextSoal();
			}
		});
	    btnNext.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
	    		nextSoal();
	    	}
		});
	    btnBack.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
	    		if(k>=0){
	    			if(k==maxSoal-1){
						btnNext.setEnabled(true);
		    			btnNext.setImageResource(R.drawable.next_button);
					}
	    			if(k==1){
	    				btnBack.setEnabled(false);
		    			btnBack.setImageResource(R.drawable.title_back_selected);
	    			}
	    			k=k-1;
	    			isiNavigasi();
	    			tpage.setText(k+1+"/"+maxSoal);
	    		}
				
			}
		});
	    btnFinish.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
	    		AlertDialog.Builder alertDialog = new AlertDialog.Builder(TryoutMapel.this);
	    		alertDialog.setTitle("Confirm Finish...");
	            alertDialog.setMessage("Yakin pekerjaan kamu sudah selesai?");
	            alertDialog.setIcon(R.drawable.flag_finish);
	            alertDialog.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog,int which) {
	                	if(timer != null) {
	                        timer.cancel();
	                        timer = null;
	                    }
	                	timerFinish();
	    				layHasil.setVisibility(View.VISIBLE);
	                }
	            });
	            alertDialog.setNegativeButton("Belum", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                	dialog.cancel();
	                }
	            });
	            alertDialog.show();
	    	}
		});
	    
	    nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	    	    
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
	
	public void Timer(){
		timer = new CountDownTimer(2700000, 1000) {
			public void onTick(long millisUntilFinished) {
				gTimer=formatTime(millisUntilFinished);
				ttimer.setText(gTimer);
				showNotification(R.drawable.icon,gTimer);
			}
			public void onFinish() {
				timerFinish();	     
                alert.setVisibility(View.VISIBLE); 
			}
		}.start();
	}
	
	class LoadAllProducts extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TryoutMapel.this);
			pDialog.setMessage("Lagi download soal dulu..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) {
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mapel", pMapel));
			params.add(new BasicNameValuePair("kode", pKode));
			json = jParser.getJSONFromUrl("tryout", params);
			Log.d("All Products: ", json.toString());
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					products = json.getJSONArray(TAG_PRODUCTS);
					vId = new String[products.length()];
					vSoal = new String[products.length()];
					vGbrsoal = new String[products.length()];
					vSolusibenar = new String[products.length()];
					vGbrsolusibenar = new String[products.length()];
					vSolusi = new String[products.length()];
					vGbrsolusi = new String[products.length()];
					vPil1 = new String[products.length()];
					vPil2 = new String[products.length()];
					vPil3 = new String[products.length()];
					vPil4 = new String[products.length()];
					vGbrpil1 = new String[products.length()];
					vGbrpil2 = new String[products.length()];
					vGbrpil3 = new String[products.length()];
					vGbrpil4 = new String[products.length()];
					drawSoal=new Drawable[products.length()];
					drawSolusiBenar=new Drawable[products.length()];
					drawSolusi=new Drawable[products.length()];
					drawPil1=new Drawable[products.length()];
					drawPil2=new Drawable[products.length()];
					drawPil3=new Drawable[products.length()];
					drawPil4=new Drawable[products.length()];
					vJwbPil1 = new String[products.length()];
					vJwbPil2 = new String[products.length()];
					vJwbPil3 = new String[products.length()];
					vJwbPil4 = new String[products.length()];
					vPilihan = new String[products.length()];
					vJawaban = new String[products.length()];

					outStream = null;
					
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						// Storing each json item in variable
						vId[i]= c.getString(TAG_PID);
						vSoal[i] = formatDegree(c.getString(TAG_SOAL));//soal
						vGbrsoal[i] = c.getString(TAG_GBR_SOAL);
						drawSoal[i] = LoadImageFromWebOperations(jParser.url_gbr_soal+vGbrsoal[i]+".png");
						vPil1[i]=formatDegree(c.getString(TAG_PIL1));//pilihan1
						vGbrpil1[i]=c.getString(TAG_GBR_PIL1);
						drawPil1[i] = LoadImageFromWebOperations(jParser.url_gbr_pil+vGbrpil1[i]+".png");
						vJwbPil1[i]=c.getString(TAG_STAT1);
						vPil2[i]=formatDegree(c.getString(TAG_PIL2));//pilihan2
						vGbrpil2[i]=c.getString(TAG_GBR_PIL2);
						drawPil2[i] = LoadImageFromWebOperations(jParser.url_gbr_pil+vGbrpil2[i]+".png");
						vJwbPil2[i]=c.getString(TAG_STAT2);
						vPil3[i]=formatDegree(c.getString(TAG_PIL3));//pilihan3
						vGbrpil3[i]=c.getString(TAG_GBR_PIL3);
						drawPil3[i] = LoadImageFromWebOperations(jParser.url_gbr_pil+vGbrpil3[i]+".png");
						vJwbPil3[i]=c.getString(TAG_STAT3);
						vPil4[i]=formatDegree(c.getString(TAG_PIL4));//pilihan4
						vGbrpil4[i]=c.getString(TAG_GBR_PIL4);
						drawPil4[i] = LoadImageFromWebOperations(jParser.url_gbr_pil+vGbrpil4[i]+".png");
						vJwbPil4[i]=c.getString(TAG_STAT4);
						vSolusi[i]=formatDegree(c.getString(TAG_SOLUSI));//solusi
						vGbrsolusi[i]=c.getString(TAG_GBR_SOLUSI);
						drawSolusi[i] = LoadImageFromWebOperations(jParser.url_gbr_solusi+vGbrsolusi[i]+".png");
						

						vPilihan[i]="";
						if (vJwbPil1[i].equals("b")){
							vJawaban[i]="a";
							vSolusibenar[i]=vPil1[i];
							drawSolusiBenar[i]=drawPil1[i];
						}else if (vJwbPil2[i].equals("b")){
							vJawaban[i]="b";
							vSolusibenar[i]=vPil2[i];
							drawSolusiBenar[i]=drawPil2[i];
						}else if (vJwbPil3[i].equals("b")){
							vJawaban[i]="c";
							vSolusibenar[i]=vPil3[i];
							drawSolusiBenar[i]=drawPil3[i];
						}else if (vJwbPil4[i].equals("b")){
							vJawaban[i]="d";
							vSolusibenar[i]=vPil4[i];
							drawSolusiBenar[i]=drawPil4[i];
						}
						
						if (vSoal[i].equals("null")){
							bm=((BitmapDrawable)drawSoal[i]).getBitmap();
							file = new File(extStorageDirectory, vGbrsoal[i].toString()+".png");
							try {
								outStream = new FileOutputStream(file);
								bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
								outStream.flush();
								outStream.close();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
					}
					maxSoal=products.length();

				} else {
					Intent i = new Intent(getApplicationContext(),Tryout.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}
		
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						int success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							tpage.setText("1/"+maxSoal);
							k=0;
							if (vSoal[0].equals("null")||vSoal[0].equals("")){
								tsoal_gbr.setImageDrawable(drawSoal[0]);
							}else{
								tsoal.setText(vSoal[0]);
							}
		
					        layPilihan.setVisibility(View.VISIBLE);
							tpil1.setText(vPil1[0]);
							tpil2.setText(vPil2[0]);
							tpil3.setText(vPil3[0]);
							tpil4.setText(vPil4[0]);
							
							MediaPlayer mp = MediaPlayer.create(TryoutMapel.this, R.raw.ting);
				             mp.setOnCompletionListener(new OnCompletionListener() {
				                 @Override
				                 public void onCompletion(MediaPlayer mp) { 
				                     mp.release();
				                 }
				             });   
				             mp.start();

				 			Timer(); //timer berjalan setelah soal selesai terdownload
						}else{
							Toast.makeText(getApplicationContext(), "Soal tidak ada !!",
									Toast.LENGTH_LONG).show();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

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
        	if(timer != null) {
	        	AlertDialog.Builder alertDialog = new AlertDialog.Builder(TryoutMapel.this);
	    		alertDialog.setTitle("Confirm Cancel Test...");
	            alertDialog.setMessage("Yakin mau batal test?");
	            alertDialog.setIcon(R.drawable.flag_finish);
	            alertDialog.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog,int which) {
	                	timer.cancel();
                        timer = null;
	                    nm.cancelAll();
	                	startActivity(new Intent(getApplicationContext(),Tryout.class));
	                }
	            });
	            alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                	dialog.cancel();
	                }
	            });
	            alertDialog.show();
	            return false;
        	}else{
        		startActivity(new Intent(getApplicationContext(),Tryout.class));
        	}
		}
        if (keyCode == KeyEvent.KEYCODE_HOME) {
        	AlertDialog.Builder alertDialog = new AlertDialog.Builder(TryoutMapel.this);
    		alertDialog.setTitle("Confirm Close ...");
            alertDialog.setMessage("Yakin mau keluar dan batal test?");
            alertDialog.setIcon(R.drawable.flag_finish);
            alertDialog.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                	if(timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    nm.cancelAll();
                    startActivity(new Intent(getApplicationContext(),Tryout.class));
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
	
	// ketika waktu timer habis
	private void timerFinish(){
		MediaPlayer mp = MediaPlayer.create(TryoutMapel.this, R.raw.ding);
        mp.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) { 
                mp.release();
            }
        });   
        mp.start(); // membunyikan bunyi teng
   	 	ttimer.setVisibility(View.GONE);
        nm.cancelAll();
        timer=null;
        int nBenar = 0;int nSalah = 0;int nKosong = 0;double nNilai= 0;
        for (int n=0;n<maxSoal;n++){ // membandingkan jawaban user dengan kunci jawaban
       	 if (vPilihan[n].equals(vJawaban[n])){
       		 nBenar++;
       	 }else{
       		 if(vPilihan[n]==null||vPilihan[n].equals("")){
       			 nKosong++;
       		 }else{
       			 nSalah++;
       		 }
       	 }
        }
        nNilai=(100/maxSoal)*nBenar;
        nNilai=round(nNilai, 0);
        tbenar.setText("Benar : "+nBenar);
        tsalah.setText("Salah  : "+nSalah);
        tkosong.setText("Kosong : "+nKosong);
        tnilai.setText(""+nNilai);
        
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String nWaktu = sdf.format(new Date());// merubah format waktu java ke format SQL
		
    	myDB = openOrCreateDatabase(DBName, MODE_PRIVATE, null);// simpan ke SQLite
    	myDB.execSQL("INSERT INTO "+TableHasil+"(user,mapel,kode,jml_soal,benar,salah,kosong,nilai,tgl) VALUES('"+spUser+"','"+pMapel+"','"+pKode+"','"+maxSoal+"','"+nBenar+"','"+nSalah+"','"+nKosong+"','"+nNilai+"','"+nWaktu+"');");
		    	
        laySoal.setVisibility(View.GONE);
        layPilihan.setVisibility(View.GONE);
        layFooter.setVisibility(View.GONE);
        btnFinish.setVisibility(View.GONE);
	}
	
	//merubah 'drjt' menjadi format simbol derajat
	private String formatDegree(String s){
		String output=null;
		String textIsi = s.replaceAll("#","\n");
		if (textIsi.contains("drjt")){
			output = textIsi.replace("drjt", ""+(char) 0x00B0);
			//output="sa";
		}else{
			output=textIsi;
		}
		return output;
	}
	
	//ke soal berikutnya
	private void nextSoal(){
		if(k<maxSoal){
			if(k<0){
				btnBack.setEnabled(false);
    			btnBack.setImageResource(R.drawable.title_back_selected);
    			btnNext.setEnabled(true);
    			btnNext.setImageResource(R.drawable.next_button);
			}
			if(k>=0){
				btnBack.setEnabled(true);
    			btnBack.setImageResource(R.drawable.back_button);
			}
			if(k==maxSoal-2){
    			btnNext.setEnabled(false);
    			btnNext.setImageResource(R.drawable.title_next_selected);
    		}
			if (k!=maxSoal-1){
				k=k+1;
				isiNavigasi();
				tpage.setText(k+1+"/"+maxSoal);
			} 
		}
	}
	
	// navigasi (ganti soal)
	private void isiNavigasi() {
		if (vSoal[k].equals("null")||vSoal[k].equals("")){
			tsoal.setVisibility(View.GONE);
			tsoal_gbr.setVisibility(View.VISIBLE);
			tsoal_gbr.setImageDrawable(drawSoal[k]);
			tvnomer_soal.setText(k+1+". ");
		}else{
			tsoal_gbr.setVisibility(View.GONE);
			tsoal.setVisibility(View.VISIBLE);
			tsoal.setText(vSoal[k]);
			tvnomer_soal.setText(k+1+". ");
		}
		if (vPil1[k].equals("null")||vPil1[k].equals("")){
			tpil1.setVisibility(View.GONE);
			tpil1_gbr.setVisibility(View.VISIBLE);
			tpil1_gbr.setImageDrawable(drawPil1[k]);
		}else{
			tpil1_gbr.setVisibility(View.GONE);
			tpil1.setVisibility(View.VISIBLE);
			tpil1.setText(vPil1[k]);
		}
		if (vPil2[k].equals("null")||vPil2[k].equals("")){
			tpil2.setVisibility(View.GONE);
			tpil2_gbr.setVisibility(View.VISIBLE);
			tpil2_gbr.setImageDrawable(drawPil2[k]);
		}else{
			tpil2_gbr.setVisibility(View.GONE);
			tpil2.setVisibility(View.VISIBLE);
			tpil2.setText(vPil2[k]);
		}
		if (vPil3[k].equals("null")||vPil3[k].equals("")){
			tpil3.setVisibility(View.GONE);
			tpil3_gbr.setVisibility(View.VISIBLE);
			tpil3_gbr.setImageDrawable(drawPil3[k]);
		}else{
			tpil3_gbr.setVisibility(View.GONE);
			tpil3.setVisibility(View.VISIBLE);
			tpil3.setText(vPil3[k]);
		}
		if (vPil4[k].equals("null")||vPil4[k].equals("")){
			tpil4.setVisibility(View.GONE);
			tpil4_gbr.setVisibility(View.VISIBLE);
			tpil4_gbr.setImageDrawable(drawPil4[k]);
		}else{
			tpil4_gbr.setVisibility(View.GONE);
			tpil4.setVisibility(View.VISIBLE);
			tpil4.setText(vPil4[k]);
		}
		if (vSolusibenar[k].equals("null")||vSolusibenar[k].equals("")){
			tsolusi_benar.setVisibility(View.GONE);
			tsolusi_gbrbenar.setVisibility(View.VISIBLE);
			tsolusi_gbrbenar.setImageDrawable(drawSolusiBenar[k]);
		}else{
			tsolusi_gbrbenar.setVisibility(View.GONE);
			tsolusi_benar.setVisibility(View.VISIBLE);
			tsolusi_benar.setText(vJawaban[k]+". "+vSolusibenar[k]);
		}
		if (vPilihan[k].equals("a")){
			cb1.setChecked(true);
			cb2.setChecked(false);
			cb3.setChecked(false);
			cb4.setChecked(false);
		}else if (vPilihan[k].equals("b")){
			cb2.setChecked(true);
			cb1.setChecked(false);
			cb3.setChecked(false);
			cb4.setChecked(false);
		}else if (vPilihan[k].equals("c")){
			cb3.setChecked(true);
			cb1.setChecked(false);
			cb2.setChecked(false);
			cb4.setChecked(false);
		}else if (vPilihan[k].equals("d")){
			cb4.setChecked(true);
			cb1.setChecked(false);
			cb2.setChecked(false);
			cb3.setChecked(false);
		}else {
			cb4.setChecked(false);
			cb1.setChecked(false);
			cb2.setChecked(false);
			cb3.setChecked(false);
		}
	}
	
	//merubah detik jadi format waktu
	public String formatTime(long millis) {  
	    String output = "00:00";  
	    long seconds = millis / 1000;  
	    long minutes = seconds / 60;  

	    seconds = seconds % 60;  
	    minutes = minutes % 60;  

	    String sec = String.valueOf(seconds);  
	    String min = String.valueOf(minutes);  

	    if (seconds < 10)  
	        sec = "0" + seconds;  
	    if (minutes < 10)  
	        min= "0" + minutes;  

	    output = min + " : " + sec;  
	    return output;
	}
	
	//default combobox di pilgan
	private void cbCheckSelect(){
		cb1.setSelected(false);
		cb1.setChecked(false);
		cb2.setSelected(false);
		cb2.setChecked(false);
		cb3.setSelected(false);
		cb3.setChecked(false);
		cb4.setSelected(false);
		cb4.setChecked(false);
	}
	
	//notifikasi
	private void showNotification(int moodId,String nTimer) {
        CharSequence text = "Tryout "+tv.getText();
        Notification notification = new Notification(moodId, null, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, TryoutMapel.class), 0);
        notification.setLatestEventInfo(this, nTimer,text, contentIntent);
        nm.notify(1, notification);
    }
	
	//pembulatan bilangan cacah
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
} // end class
