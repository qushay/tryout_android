package ta.qlay.tryout.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ta.qlay.tryout.library.UserFunctions;

public class LoginActivity extends Activity {
	Button btnLogin,btnReg;
	EditText inputEmail;
	EditText inputPassword;
	TextView loginErrorMsg;
	private static String KEY_SUCCESS = "success";
	SQLiteDatabase myDB= null;

	// Shared Preference
	public static final String TAG_PREF = "SharedPreferences";
	SharedPreferences.Editor spEdit;
	String DBName = "TRYOUT_LOCAL.db";
	String TableSoal = "tbSoal";
	String TableHasil = "tbHasil";

	String pid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		//Create DB & Table
		myDB = openOrCreateDatabase(DBName, MODE_PRIVATE, null);
    	myDB.execSQL("CREATE TABLE IF NOT EXISTS "+TableHasil+"(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,user VARCHAR(50)," +
    			"mapel VARCHAR(3),kode VARCHAR(3),jml_soal VARCHAR(3),benar VARCHAR(3)," +
    			"salah VARCHAR(3),kosong VARCHAR(3), nilai VARCHAR(5), tgl DATETIME);");
    	

	    //Creating Instance of SharedPreferences
        SharedPreferences sp = getSharedPreferences(TAG_PREF, 0);
        spEdit = sp.edit();
        
		// Importing all assets like buttons, text fields
//    	btnReg=(Button)findViewById(R.id.login_reg);
		inputEmail = (EditText) findViewById(R.id.loginEmail);
		inputPassword = (EditText) findViewById(R.id.loginPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);
		
		//Button daftar event
//		btnReg.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View view) {
//				Intent reg = new Intent(getApplicationContext(), RegActivity.class);
//				
//				startActivity(reg);
//			}
//		});
//		
		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
//				startActivity(new Intent(getApplicationContext(), HomeActivity.class));
				
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				UserFunctions userFunction = new UserFunctions();
				JSONObject json = userFunction.loginUser(email, password);

				// check for login response
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						loginErrorMsg.setText("");
						String res = json.getString(KEY_SUCCESS); 
						if(Integer.parseInt(res) == 1){
							spEdit.putString("spUser", email);
							spEdit.commit();
							Intent dashboard = new Intent(getApplicationContext(), HomeActivity.class);
							dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(dashboard);
							
							finish();
						}else{
							loginErrorMsg.setText("Incorrect username/password");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	
    if (keyCode == KeyEvent.KEYCODE_BACK) {
    	Intent intent = new Intent (Intent.ACTION_MAIN);
    	intent.addCategory(Intent.CATEGORY_HOME);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(intent);
	}
    return super.onKeyDown(keyCode, event);
}	
}
