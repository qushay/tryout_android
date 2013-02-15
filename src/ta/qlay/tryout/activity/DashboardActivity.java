package ta.qlay.tryout.activity;

import ta.qlay.tryout.activity.LoginActivity;
import ta.qlay.tryout.library.UserFunctions;

import ta.qlay.tryout.activity.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public abstract class DashboardActivity extends Activity 
{
	UserFunctions userFunctions;
	Button btnLogout;
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_default);
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

public void onClickHome (View v)
{
    goHome (this);
}

public void onClickLogout (View v)
{
	userFunctions = new UserFunctions();
    if(userFunctions.isUserLoggedIn(getApplicationContext())){
    	
				// TODO Auto-generated method stub
				userFunctions.logoutUser(getApplicationContext());
				Intent login = new Intent(getApplicationContext(), LoginActivity.class);
	        	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(login);
	        	// Closing dashboard screen
	        	finish();
    	
    }else{
    	// user is not logged in show login screen
    	Intent login = new Intent(getApplicationContext(), LoginActivity.class);
    	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(login);
    	// Closing dashboard screen
    	finish();
    }
}

public void onClickFeature (View v)
{
    int id = v.getId ();
    switch (id) {
      case R.id.home_btn_feature1 :
           startActivity (new Intent(getApplicationContext(), Materi.class));
           break;
      case R.id.home_btn_feature2 :
           startActivity (new Intent(getApplicationContext(), Tryout.class));
           break;
      case R.id.home_btn_feature3 :
           startActivity (new Intent(getApplicationContext(), Tips.class));
           break;
      case R.id.home_btn_feature4 :
           startActivity (new Intent(getApplicationContext(), Statistic.class));
           break;
      case R.id.home_btn_feature5 :
           startActivity (new Intent(getApplicationContext(), Report.class));
           break;
      default: 
    	   break;
    }
}

public void goHome(Context context) 
{
    final Intent intent = new Intent(context, HomeActivity.class);
    intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
    context.startActivity (intent);
}

public void setTitleFromActivityLabel (int textViewId)
{
    TextView tv = (TextView) findViewById (textViewId);
    if (tv != null) tv.setText (getTitle ());
} // end setTitleText

public void toast (String msg)
{
    Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
} // end toast

public void trace (String msg) 
{
    Log.d("Demo", msg);
    toast (msg);
}

} // end class
