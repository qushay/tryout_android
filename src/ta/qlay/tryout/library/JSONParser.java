package ta.qlay.tryout.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	public String url_server="http://192.168.43.35/tryout/client/";
	
    private String url_tryout = url_server+"tryout.php";
    public String url_gbr_soal = url_server+"soal/";
    public String url_gbr_pil = url_server+"jawaban/";
    public String url_gbr_solusi = url_server+"solusi/";
    public String url_gbr_materi = url_server+"materi/";
    private String url_tryout_set = url_server+"tryout_set.php";
    
    private String url_materi_isi = url_server+"materi_isi.php";
    private String url_materi_mapel = url_server+"materi.php";
    private String url_tips = url_server+"tips.php";
    private String url_tips_isi = url_server+"tips_isi.php";

    private String url_register = url_server+"register.php";
    
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(String fungsi, List<NameValuePair> params) {
		String url = url_server;
		if (fungsi.equals("tryout")){
			url=url_tryout;
		}else if (fungsi.equals("materi_isi")){
			url=url_materi_isi;
		}else if (fungsi.equals("materi_mapel")){
			url=url_materi_mapel;
		}else if (fungsi.equals("tips")){
			url=url_tips;
		}else if (fungsi.equals("tips_isi")){
			url=url_tips_isi;
		}else if (fungsi.equals("tryout_set")){
			url=url_tryout_set;
		}else if (fungsi.equals("register")){
			url=url_register;
		}
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("JSON", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);			
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}
}
