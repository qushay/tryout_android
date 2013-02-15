
package ta.qlay.tryout.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import ta.qlay.tryout.activity.R;

import android.R.string;
import android.content.Context;
import android.text.GetChars;

public class UserFunctions {
	
	private JSONParser jsonParser;
	private String url_server="http://192.168.43.35/tryout/client/";
	private String loginURL = url_server+"index.php";
    
    
	private String login_tag = "login";
	private String materi_tag = "materi";
	
	// constructor
	public UserFunctions(){
		jsonParser = new JSONParser();
	}
	
	public JSONObject loginUser(String email, String password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	
	public boolean isUserLoggedIn(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if(count > 0){
			// user logged in
			return true;
		}
		return false;
	}
	
	/**
	 * Function to logout user
	 * Reset Database
	 * */
	public boolean logoutUser(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}
	
}
