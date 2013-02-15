package ta.qlay.tryout.activity;

import ta.qlay.tryout.activity.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

public class Tryout extends DashboardActivity 
{

	// Shared Preference
	public static final String TAG_PREF = "SharedPreferences";
	
	String vMapel;
	String spUser;
	int[] vUdah,vBelum;
	Bundle bundle;
	Intent newIntent;
	
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView (R.layout.tryout);
    setTitleFromActivityLabel (R.id.title_text);
    //Reading the Preferences File
    SharedPreferences s = getSharedPreferences(TAG_PREF, 0);
    spUser = s.getString("spUser", "");


	bundle = new Bundle();
	newIntent = new Intent(getApplicationContext(), TryoutMapel.class);
    
}
public void getMapel(View view) {
	int id = view.getId ();
    switch (id) {
    case R.id.btn_mat :
    	vMapel="MTK";
    	bundle.putString("pMapel", vMapel);
    	bundle.putString("pKode", 1+"");
	    newIntent.putExtras(bundle);
	    startActivityForResult(newIntent, 0);
       break;
	case R.id.btn_bin :
    	vMapel="BIN";
    	bundle.putString("pMapel", vMapel);
    	bundle.putString("pKode", 1+"");
	    newIntent.putExtras(bundle);
	    startActivityForResult(newIntent, 0);
           break;
	case R.id.btn_big :
    	vMapel="BIG";
    	bundle.putString("pMapel", vMapel);
    	bundle.putString("pKode", 1+"");
	    newIntent.putExtras(bundle);
	    startActivityForResult(newIntent, 0);
           break;
	case R.id.btn_ipa :
    	vMapel="IPA";
    	bundle.putString("pMapel", vMapel);
    	bundle.putString("pKode", 1+"");
	    newIntent.putExtras(bundle);
	    startActivityForResult(newIntent, 0);
           break;
    default:
    	break;
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

} // end class
