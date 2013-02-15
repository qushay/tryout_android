
package ta.qlay.tryout.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import ta.qlay.tryout.activity.R;
import ta.qlay.tryout.library.KonversiBulan;
import ta.qlay.tryout.library.SimpleGestureFilter;
import ta.qlay.tryout.library.SimpleGestureFilter.SimpleGestureListener;

import android.R.color;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Statistic extends DashboardActivity implements SimpleGestureListener
{
	KonversiBulan konversiBulan= new KonversiBulan();

	// Shared Preference
	public static final String TAG_PREF = "SharedPreferences";
	//SQLite
	SQLiteDatabase myDBTgl= null;
	SQLiteDatabase myDBGraf= null;
	String DBName = "TRYOUT_LOCAL.db";
	String TableSoal = "tbSoal";
	String TableHasil = "tbHasil";
	Double nilaix,nilaiy;
	double[] nilaiMtk=null;
	double[] nilaiBin=null;
	double[] nilaiBig=null;
	double[] nilaiIpa=null;
	GraphicalView gvMtk,gvBin,gvBig,gvIpa;
	int jmlnilai;
	TextView tvBulan,tvKali,tvRata;
	String [] vBulan;
	LinearLayout chartMtk,chartBin,chartBig,chartIpa;

	ArrayList<String> columnArray1 = new ArrayList<String>();
	String[] colStrArr1;
	int iTgl=0;
	int iBulan=0;
	int maxData=0;
	int jmlNilaiMat,jmlNilaiBin,jmlNilaiBig,jmlNilaiIpa;
	double rataNilaiMat=0;
	double rataNilaiBin=0;
	double rataNilaiBig=0;
	double rataNilaiIpa=0;
	Button btnMtk,btnBin,btnBig,btnIpa;
	XYMultipleSeriesRenderer renderer;
	String judulGraf,spUser;
	XYMultipleSeriesDataset datasetMtk,datasetBin,datasetBig,datasetIpa;
	CategorySeries csMtk,csBin,csBig,csIpa;
	
protected void onCreate(Bundle savedInstanceState) 
{
	super.onCreate(savedInstanceState);
	setContentView (R.layout.statistic);
	setTitleFromActivityLabel (R.id.title_text);

    //Reading the Preferences File
    SharedPreferences s = getSharedPreferences(TAG_PREF, 0);
    spUser = s.getString("spUser", "");
	
	tvBulan=(TextView)findViewById(R.id.statistik_bulan);
	tvKali=(TextView)findViewById(R.id.statistik_kali);
	tvRata=(TextView)findViewById(R.id.statistik_rata);
	chartMtk=(LinearLayout)findViewById(R.id.statistik_chart_mtk);
	chartBig=(LinearLayout)findViewById(R.id.statistik_chart_big);
	chartBin=(LinearLayout)findViewById(R.id.statistik_chart_bin);
	chartIpa=(LinearLayout)findViewById(R.id.statistik_chart_ipa);

	btnMtk=(Button)findViewById(R.id.report_btnmtk);
	btnBin=(Button)findViewById(R.id.report_btnbin);
	btnBig=(Button)findViewById(R.id.report_btnbig);
	btnIpa=(Button)findViewById(R.id.report_btnipa);
	btnMtk.setBackgroundColor(getResources().getColor(R.color.title_text));
	btnMtk.setTextColor(getResources().getColor(R.color.mapel_text_footer));

    
		
	try{
		myDBTgl = this.openOrCreateDatabase(DBName,0, null);
		Cursor cur = myDBTgl.rawQuery("SELECT DISTINCT(strftime('%m', tgl)) As bulan,(strftime('%Y', tgl)) As tahun FROM "+TableHasil+" WHERE user='"+spUser+"' ORDER BY tgl DESC", null);
		int Col1= cur.getColumnIndex("bulan");
		int Col2= cur.getColumnIndex("tahun");
		String vBulanTahun,vBulanS;
		cur.moveToFirst();
		jmlnilai=cur.getCount();
		vBulan= new String[cur.getCount()];
		if (cur.getCount()!=0){
			do {
				vBulan[iBulan]=cur.getString(Col1);
				vBulanS=konversiBulan.Konversi(cur.getString(Col1));
				vBulanTahun=vBulanS+" "+cur.getString(Col2);
				columnArray1.add(vBulanTahun);
				iBulan++;
			}
			while (cur.moveToNext());

			colStrArr1 = (String[]) columnArray1.toArray(new String[columnArray1.size()]);
			maxData= colStrArr1.length;
			tvBulan.setText(colStrArr1[iTgl]);
		}
		
		
	}
	catch (Exception e){
		Log.e("Error", "Error",e);
	}
	finally{
		if (myDBTgl!=null)
			myDBTgl.close();
	}
//	nilai= new double[] {6,7,4,7,6};
	nilaiMtk=isiGrafik("02", "MTK"); jmlNilaiMat=nilaiMtk.length;
	nilaiBin=isiGrafik("02", "BIN"); jmlNilaiBin=nilaiBin.length;
	nilaiBig=isiGrafik("02", "BIG"); jmlNilaiBig=nilaiBig.length;
	nilaiIpa=isiGrafik("02", "IPA"); jmlNilaiIpa=nilaiIpa.length;
	chartMtk.setVisibility(View.VISIBLE);
	chartBin.setVisibility(View.GONE);
	chartBig.setVisibility(View.GONE);
	chartIpa.setVisibility(View.GONE);
	renderGrafikMtk();
	
	for (int i=0;i<jmlNilaiMat;i++){
		rataNilaiMat=rataNilaiMat+nilaiMtk[i];
	} rataNilaiMat=rataNilaiMat/jmlNilaiMat;
	rataNilaiMat=round(rataNilaiMat, 2);
	for (int i=0;i<jmlNilaiBin;i++){
		rataNilaiBin=rataNilaiBin+nilaiBin[i];
	} rataNilaiBin=rataNilaiBin/jmlNilaiBin;
	rataNilaiBin=round(rataNilaiBin, 2);
	for (int i=0;i<jmlNilaiBig;i++){
		rataNilaiBig=rataNilaiBig+nilaiBig[i];
	} rataNilaiBig=rataNilaiBig/jmlNilaiBig;
	rataNilaiBig=round(rataNilaiBig, 2);
	for (int i=0;i<jmlNilaiIpa;i++){
		rataNilaiIpa=rataNilaiIpa+nilaiIpa[i];
	} rataNilaiIpa=rataNilaiIpa/jmlNilaiIpa;
	rataNilaiIpa=round(rataNilaiIpa, 2);
	tvKali.setText("Jumlah tryout : "+jmlNilaiMat+"x");
	tvRata.setText("Rata-rata : "+rataNilaiMat);
	
	btnMtk.setOnClickListener(new View.OnClickListener() { // tab MTK
	   	public void onClick(View view) {
	   		buttonColor();
	   		btnMtk.setBackgroundColor(getResources().getColor(R.color.title_text));
			btnMtk.setTextColor(getResources().getColor(R.color.mapel_text_footer));
	   		chartMtk.setVisibility(View.VISIBLE);
	   		chartBin.setVisibility(View.GONE);
	   		chartBig.setVisibility(View.GONE);
	   		chartIpa.setVisibility(View.GONE);
	   		renderGrafikMtk();
	   		tvKali.setText("Jumlah tryout : "+jmlNilaiMat+"x");
	   		tvRata.setText("Rata-rata : "+rataNilaiMat);
//		    nilai = isiGrafik(vBulan[0], "MTK");
		}
	});
	btnBin.setOnClickListener(new View.OnClickListener() { // tab B. Indo
	   	public void onClick(View view) {
	   		buttonColor();
	   		btnBin.setBackgroundColor(getResources().getColor(R.color.title_text));
			btnBin.setTextColor(getResources().getColor(R.color.mapel_text_footer));
	   		chartMtk.setVisibility(View.GONE);
	   		chartBin.setVisibility(View.VISIBLE);
	   		chartBig.setVisibility(View.GONE);
	   		chartIpa.setVisibility(View.GONE);
	   		renderGrafikBin();
	   		tvKali.setText("Jumlah tryout : "+jmlNilaiBin+"x");
	   		tvRata.setText("Rata-rata : "+rataNilaiBin);
//		    nilai = isiGrafik(vBulan[0], "BIN");Series());
		}
	});
	btnBig.setOnClickListener(new View.OnClickListener() { // tab B. Ing
	   	public void onClick(View view) {
	   		buttonColor();
	   		btnBig.setBackgroundColor(getResources().getColor(R.color.title_text));
			btnBig.setTextColor(getResources().getColor(R.color.mapel_text_footer));
	   		chartMtk.setVisibility(View.GONE);
	   		chartBin.setVisibility(View.GONE);
	   		chartBig.setVisibility(View.VISIBLE);
	   		chartIpa.setVisibility(View.GONE);
	   		renderGrafikBig();
	   		tvKali.setText("Jumlah tryout : "+jmlNilaiBig+"x");
	   		tvRata.setText("Rata-rata : "+rataNilaiBig);
//		    nilai = isiGrafik(vBulan[0], "BIG");
		}
	});
	btnIpa.setOnClickListener(new View.OnClickListener() { // tab IPA
	   	public void onClick(View view) {
	   		buttonColor();
	   		btnIpa.setBackgroundColor(getResources().getColor(R.color.title_text));
			btnIpa.setTextColor(getResources().getColor(R.color.mapel_text_footer));
	   		chartMtk.setVisibility(View.GONE);
	   		chartBin.setVisibility(View.GONE);
	   		chartBig.setVisibility(View.GONE);
	   		chartIpa.setVisibility(View.VISIBLE);
	   		renderGrafikIpa();
	   		tvKali.setText("Jumlah tryout : "+jmlNilaiIpa+"x");
	   		tvRata.setText("Rata-rata : "+rataNilaiIpa);
//		    nilai = isiGrafik(vBulan[0], "IPA");
		}
	});
	 

}

private void rataNilai(int nilai){
	
}

private void renderGrafikMtk() {
	renderer = new XYMultipleSeriesRenderer();
	judulGraf = "nilai";
    // konfigurasi grafik
    renderer.setChartTitle("Grafik Nilai Ujian");
    renderer.setXTitle("Ujian Ke-");
    renderer.setYTitle("Nilai");
    renderer.setAxesColor(Color.DKGRAY);
    renderer.setApplyBackgroundColor(true);
    renderer.setBackgroundColor(Color.WHITE);
    renderer.setMarginsColor(Color.WHITE);
    renderer.setLabelsColor(Color.BLACK);
    renderer.setXLabelsColor(Color.BLACK);
    renderer.setYLabelsColor(0, Color.BLACK);
    renderer.setYLabelsAlign(Align.RIGHT, 0);
    renderer.setYLabels(10);
    renderer.setXAxisMax(5);
    renderer.setXAxisMin(0.5);
    renderer.setYAxisMax(100);
    renderer.setYAxisMin(0);
    renderer.setBarSpacing(0.2);
    renderer.setZoomRate(1.1f);
    renderer.setPanEnabled(true, false);
    renderer.setShowLegend(false);
    XYSeriesRenderer r = new XYSeriesRenderer();
    r.setColor(Color.parseColor("#ff355689"));
    r.setDisplayChartValues(true);
    renderer.addSeriesRenderer(r);
    datasetMtk = new XYMultipleSeriesDataset();
    csMtk = new CategorySeries(judulGraf);
    for(Double n : nilaiMtk) {
        csMtk.add(n);
    }
    datasetMtk.addSeries(csMtk.toXYSeries());
    gvMtk = ChartFactory.getBarChartView(this, datasetMtk,
            renderer, Type.STACKED);
    chartMtk.addView(gvMtk,new LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
    //startActivity(it);
}


private void renderGrafikBin() {
	renderer = new XYMultipleSeriesRenderer();
	judulGraf = "nilai";
    // konfigurasi grafik
    renderer.setChartTitle("Grafik Nilai Ujian");
    renderer.setXTitle("Ujian Ke-");
    renderer.setYTitle("Nilai");
    renderer.setAxesColor(Color.DKGRAY);
    renderer.setApplyBackgroundColor(true);
    renderer.setBackgroundColor(Color.WHITE);
    renderer.setMarginsColor(Color.WHITE);
    renderer.setLabelsColor(Color.BLACK);
    renderer.setXLabelsColor(Color.BLACK);
    renderer.setYLabelsColor(0, Color.BLACK);
    renderer.setYLabelsAlign(Align.RIGHT, 0);
    renderer.setYLabels(10);
    renderer.setXAxisMax(5);
    renderer.setXAxisMin(0.5);
    renderer.setYAxisMax(100);
    renderer.setYAxisMin(0);
    renderer.setBarSpacing(0.2);
    renderer.setZoomRate(1.1f);
    renderer.setPanEnabled(true, false);
    renderer.setShowLegend(false);
    XYSeriesRenderer r = new XYSeriesRenderer();
    r.setColor(Color.parseColor("#ff355689"));
    r.setDisplayChartValues(true);
    renderer.addSeriesRenderer(r);
    datasetBin = new XYMultipleSeriesDataset();
    csBin = new CategorySeries(judulGraf);
    for(Double n : nilaiBin) {
        csBin.add(n);
    }
    datasetBin.addSeries(csBin.toXYSeries());
    gvBin = ChartFactory.getBarChartView(this, datasetBin,
            renderer, Type.STACKED);
    chartBin.addView(gvBin,new LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
    //startActivity(it);
}
private void renderGrafikBig() {
	renderer = new XYMultipleSeriesRenderer();
	judulGraf = "nilai";
    // konfigurasi grafik
    renderer.setChartTitle("Grafik Nilai Ujian");
    renderer.setXTitle("Ujian Ke-");
    renderer.setYTitle("Nilai");
    renderer.setAxesColor(Color.DKGRAY);
    renderer.setApplyBackgroundColor(true);
    renderer.setBackgroundColor(Color.WHITE);
    renderer.setMarginsColor(Color.WHITE);
    renderer.setLabelsColor(Color.BLACK);
    renderer.setXLabelsColor(Color.BLACK);
    renderer.setYLabelsColor(0, Color.BLACK);
    renderer.setYLabelsAlign(Align.RIGHT, 0);
    renderer.setYLabels(10);
    renderer.setXAxisMax(5);
    renderer.setXAxisMin(0.5);
    renderer.setYAxisMax(100);
    renderer.setYAxisMin(0);
    renderer.setBarSpacing(0.2);
    renderer.setZoomRate(1.1f);
    renderer.setPanEnabled(true, false);
    renderer.setShowLegend(false);
    XYSeriesRenderer r = new XYSeriesRenderer();
    r.setColor(Color.parseColor("#ff355689"));
    r.setDisplayChartValues(true);
    renderer.addSeriesRenderer(r);
    datasetBig = new XYMultipleSeriesDataset();
    csBig = new CategorySeries(judulGraf);
    for(Double n : nilaiBig) {
        csBig.add(n);
    }
    datasetBig.addSeries(csBig.toXYSeries());
    gvBig = ChartFactory.getBarChartView(this, datasetBig,
            renderer, Type.STACKED);
    chartBig.addView(gvBig,new LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
    //startActivity(it);
}
private void renderGrafikIpa() {
	renderer = new XYMultipleSeriesRenderer();
	judulGraf = "nilai";
    // konfigurasi grafik
    renderer.setChartTitle("Grafik Nilai Ujian");
    renderer.setXTitle("Ujian Ke-");
    renderer.setYTitle("Nilai");
    renderer.setAxesColor(Color.DKGRAY);
    renderer.setApplyBackgroundColor(true);
    renderer.setBackgroundColor(Color.WHITE);
    renderer.setMarginsColor(Color.WHITE);
    renderer.setLabelsColor(Color.BLACK);
    renderer.setXLabelsColor(Color.BLACK);
    renderer.setYLabelsColor(0, Color.BLACK);
    renderer.setYLabelsAlign(Align.RIGHT, 0);
    renderer.setYLabels(10);
    renderer.setXAxisMax(5);
    renderer.setXAxisMin(0.5);
    renderer.setYAxisMax(100);
    renderer.setYAxisMin(0);
    renderer.setBarSpacing(0.2);
    renderer.setZoomRate(1.1f);
    renderer.setPanEnabled(true, false);
    renderer.setShowLegend(false);
    XYSeriesRenderer r = new XYSeriesRenderer();
    r.setColor(Color.parseColor("#ff355689"));
    r.setDisplayChartValues(true);
    renderer.addSeriesRenderer(r);
    datasetIpa = new XYMultipleSeriesDataset();
    csIpa = new CategorySeries(judulGraf);
    for(Double n : nilaiIpa) {
        csIpa.add(n);
    }
    datasetIpa.addSeries(csIpa.toXYSeries());
    gvIpa = ChartFactory.getBarChartView(this, datasetIpa,
            renderer, Type.STACKED);
    chartIpa.addView(gvIpa,new LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
    //startActivity(it);
}

public double[] isiGrafik(String vBulan,String vMapel) {
	double[] vNilai=null;
	List<Double> doubleList = new ArrayList<Double>();
	try{
		myDBGraf = this.openOrCreateDatabase(DBName,0, null);
		Cursor cur = myDBGraf.rawQuery("SELECT nilai FROM "+TableHasil+" WHERE user='"+spUser+"' AND mapel='"+vMapel+"' AND (strftime('%m', tgl))='"+vBulan+"'", null);
		int Col1= cur.getColumnIndex("nilai");
		cur.moveToFirst();
		vNilai=new double[cur.getCount()];

		if (cur.getCount()!=0){
			int i=0;
			do {
				vNilai[i]=cur.getDouble(Col1);
//				doubleList.add(cur.getDouble(Col1));
				i++;
			}
			while (cur.moveToNext());
//			vNilai= new double[doubleList.size()];
		}
		
	}
	catch (Exception e){
		Log.e("Error", "Error",e);
	}
	finally{
		if (myDBGraf!=null)
			myDBGraf.close();
	}
	return vNilai;
}

public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
}

@Override
public void onSwipe(int direction) {
	String str = "";
	  switch (direction) {
	  	  case SimpleGestureFilter.SWIPE_RIGHT : 
	  		  str = "Swipe Right";
	  		  if (iTgl<maxData-1){
		  		  iTgl=iTgl+1;
		  		  str=colStrArr1[iTgl];
		  		  tvBulan.setText(str);
		  		  //isiList("MTK");

		  		  Toast.makeText(this, str+"", Toast.LENGTH_SHORT).show();
	  		  }
	      break;
	  	  case SimpleGestureFilter.SWIPE_LEFT :  
	  		  str = "Swipe Left";
	  		  if (iTgl>0){
		  		  iTgl=iTgl-1;
		  		  str=colStrArr1[iTgl];
		  		  tvBulan.setText(str);
		  		  //isiList("MTK");
		  		  Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	  		  }
	      break;
	  }
}

@Override
public void onDoubleTap() {

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
