
package ta.qlay.tryout.activity;

import ta.qlay.tryout.activity.R;

import android.os.Bundle;

public class AboutActivity extends DashboardActivity 
{

protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);

    setContentView (R.layout.activity_about);
    setTitleFromActivityLabel (R.id.title_text);
}
    
} // end class
