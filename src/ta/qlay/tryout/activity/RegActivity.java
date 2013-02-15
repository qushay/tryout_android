
package ta.qlay.tryout.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ta.qlay.tryout.library.JSONParser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegActivity extends DashboardActivity
{

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	// products JSONArray
	JSONArray products = null;

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "user";
	private static final String TAG_PESAN = "pesan";
	private static final String TAG_PID = "pid";
	
	private String vNama,vSekolah,vUser,vEmail,vPass,vRepass; 
    private Button btnDaftar;
    private TextView btnReg;
    private EditText eNama,eSekolah,eUser,eEmail,ePass,eRepass;
    TextView tvError;

protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView (R.layout.register);
    setTitleFromActivityLabel (R.id.title_text);
    
    tvError=(TextView)findViewById(R.id.reg_error);
    eNama=(EditText)findViewById(R.id.reg_nama);
    eSekolah=(EditText)findViewById(R.id.reg_sekolah);
    eUser=(EditText)findViewById(R.id.reg_user);
    eEmail=(EditText)findViewById(R.id.reg_email);
    ePass=(EditText)findViewById(R.id.reg_pass);
    eRepass=(EditText)findViewById(R.id.reg_repass);
    
    btnDaftar =(Button)findViewById(R.id.reg_daftar);
    btnDaftar.setOnClickListener(new Button.OnClickListener(){
        public void onClick(View v){
            vNama=eNama.getText().toString();
            vSekolah=eSekolah.getText().toString();
            vUser=eUser.getText().toString();
            vEmail=eEmail.getText().toString();
            vPass=ePass.getText().toString();
            vRepass=eRepass.getText().toString();
            
        	if(!vNama.equals("")&&!vSekolah.equals("")&&!vUser.equals("")&&
        		!vEmail.equals("")&&!vPass.equals("")&&!vRepass.equals("")){
            	if (vPass.equals(vRepass)){
            		int success;
	        			try {
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("nama", vNama));
							params.add(new BasicNameValuePair("sekolah", vSekolah));
							params.add(new BasicNameValuePair("user", vUser));
							params.add(new BasicNameValuePair("email", vEmail));
							params.add(new BasicNameValuePair("pass", vPass));
							// getting JSON string from URL
							JSONObject json = jParser.getJSONFromUrl("register", params);
							// Checking for SUCCESS TAG

							Log.d("All Products: ", json.toString());
							String res = json.getString(TAG_SUCCESS); 
							success=Integer.parseInt(res);
							if (success == 1) {
								Intent login = new Intent(getApplicationContext(), LoginActivity.class);
								startActivity(login);
							}else if(success == 2){
								tvError.setText("User / Email ini telah terdaftar");
							}else{
								tvError.setText("Gagal Daftar");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
        		}else{
        			tvError.setText("Password tidak sama!");
        		}
        	}else{
        		tvError.setText("Semua data belum diisi!");
        	}
        }
    });
    
}    
} // end class
