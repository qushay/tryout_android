package ta.qlay.tryout.activity;

import ta.qlay.tryout.activity.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class HomeActivity extends DashboardActivity 
{

protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
}
    
protected void onDestroy ()
{
   super.onDestroy ();
}

protected void onPause ()
{
   super.onPause ();
}

protected void onRestart ()
{
   super.onRestart ();
}

protected void onResume ()
{
   super.onResume ();
}

protected void onStart ()
{
   super.onStart ();
}

protected void onStop ()
{
   super.onStop ();
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	
    if (keyCode == KeyEvent.KEYCODE_BACK) {
        	AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
    		alertDialog.setTitle("Logout");
            alertDialog.setMessage("Anda ingin logout?");
            alertDialog.setIcon(R.drawable.flag_finish);
            alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                	startActivity(new Intent(getApplicationContext(),LoginActivity.class));
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
    if (keyCode == KeyEvent.KEYCODE_HOME) {
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
		alertDialog.setTitle("Logout");
        alertDialog.setMessage("Anda ingin logout?");
        alertDialog.setIcon(R.drawable.flag_finish);
        alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	startActivity(new Intent(getApplicationContext(),LoginActivity.class));
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
