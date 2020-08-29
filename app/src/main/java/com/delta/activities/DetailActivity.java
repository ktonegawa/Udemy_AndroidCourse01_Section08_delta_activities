package com.delta.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class DetailActivity extends Activity {

    private Button mReturnButton = null;
    private Button mPerformButton = null;
    private Spinner mSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Display data sent to us
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            String detailValue = extras.getString("KeyForSending");
            if(detailValue != null){
                Toast.makeText(this, detailValue, Toast.LENGTH_SHORT).show();
            }
        }

        //prepare rest of UI
        mSpinner = (Spinner) findViewById(R.id.spinnerSelection);

        mReturnButton = (Button) findViewById(R.id.returnToSecondActivity);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                String mySelection = mSpinner.getSelectedItem().toString();
                returnIntent.putExtra("KeyForReturning", mySelection);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        mPerformButton = (Button) findViewById(R.id.performImplicit);
        mPerformButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mSpinner.getSelectedItemPosition();
                Intent implicitIntent = null;
                switch(position) {
                    case 0:
                        //nothing selected
                        break;
                    case 1:
                        //deltaprogram.us
                        implicitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://deltaprogram.us"));
                        break;
                    case 2:
                        //call someone
                        implicitIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:(+604)2944338"));
                        break;
                    case 3:
                        //map a YETspace using geo intent
                        implicitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:49.15818123,-122.80843499"));
                        break;
                    case 4:
                        //take a picture (not returning it here though)
                        implicitIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                        break;
                    case 5:
                        implicitIntent = new Intent(Intent.ACTION_EDIT, Uri.parse("content://contacts/people/1"));
                        break;

                }
                if(implicitIntent != null){
                    //is intent available?
                    if(isIntentAvailable(implicitIntent) == true){
                        startActivity(implicitIntent);
                    }
                    else{
                        Toast.makeText(view.getContext(),"no application available",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public boolean isIntentAvailable(Intent intent){
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent,0);
        boolean isIntentSafe = activities.size() > 0;
        return isIntentSafe;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
