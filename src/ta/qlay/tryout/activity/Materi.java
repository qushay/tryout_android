
package ta.qlay.tryout.activity;

import ta.qlay.tryout.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Materi extends DashboardActivity 
{
	Button btnMtk,btnBin,btnBing,BtnIpa;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView (R.layout.materi);
	    setTitleFromActivityLabel (R.id.title_text);
	}
	
	
	public void getMapel(View view) {
		int id = view.getId ();
		Bundle bundle = new Bundle();
		Intent newIntent = new Intent(getApplicationContext(), MateriMapel.class);
	    switch (id) {
			case R.id.btn_mat :
		    	bundle.putString("pMapel", "MTK");
		    	newIntent.putExtras(bundle);
		    	startActivityForResult(newIntent, 0);
	           break;
			case R.id.btn_bin :
				bundle.putString("pMapel", "BIN");
		    	newIntent.putExtras(bundle);
		    	startActivityForResult(newIntent, 0);
		           break;
			case R.id.btn_big :
				bundle.putString("pMapel", "BIG");
		    	newIntent.putExtras(bundle);
		    	startActivityForResult(newIntent, 0);
		           break;
			case R.id.btn_ipa :
				bundle.putString("pMapel", "IPA");
		    	newIntent.putExtras(bundle);
		    	startActivityForResult(newIntent, 0);
		           break;
	        default:
	        	break;
	    }
	}
} // end class
