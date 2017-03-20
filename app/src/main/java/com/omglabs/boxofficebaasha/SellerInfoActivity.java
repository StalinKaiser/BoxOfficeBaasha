package com.omglabs.boxofficebaasha;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.List;
import java.util.Map;

/**
 * Created by anusha on 30/05/16.
 */
public class SellerInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_details);
        initToolbar();
        findViewById(R.id.loadingPanelLoginseller).setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        RowItem activeRow = (RowItem)intent.getExtras().getSerializable("rowitem");
        SetValues(activeRow);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sellerdetails_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (null != toolbar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Seller Details");
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_back));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }
    private void SetValues(RowItem activeRow)
    {
        TextView movieName = (TextView)findViewById(R.id.seller_movienamevalue);
        movieName.setText(activeRow.getTitle());

        TextView theatreName = (TextView)findViewById(R.id.seller_theatrenamevalue);
        theatreName.setText(activeRow.getTheatre());

        TextView date = (TextView)findViewById(R.id.seller_datevalue);
        date.setText(activeRow.getDate());

        TextView time = (TextView)findViewById(R.id.seller_timevalue);
        time.setText(activeRow.getTime());

        TextView count = (TextView)findViewById(R.id.seller_countvalue);
        count.setText(activeRow.getCount());

        TextView tickettype = (TextView)findViewById(R.id.seller_tickettypevalue);
        tickettype.setText(activeRow.getTicketType());

        TextView ticketprice = (TextView)findViewById(R.id.seller_ratevalue);
        ticketprice.setText(activeRow.getTicketprice());

        String whereClause = "userid = '" + activeRow.getPostedUser() +"'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );
        dataQuery.setPageSize(100);
        Backendless.Persistence.of( "sellerdetails" ).find(dataQuery, new AsyncCallback<BackendlessCollection<Map>>() {
            @Override
            public void handleResponse(BackendlessCollection<Map> mapBackendlessCollection) {
                List<Map> listMap= mapBackendlessCollection.getData();
                for (Map map:listMap) {
                    String sellerName = (String) map.get("sellername");
                    TextView postedUser = (TextView) findViewById(R.id.seller_name);
                    postedUser.setText(sellerName);
                    String mobileNo=(String) map.get("mobileno");
                    HandleCallSeller(mobileNo);
                    findViewById(R.id.loadingPanelLoginseller).setVisibility(View.GONE);
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                findViewById(R.id.loadingPanelLoginseller).setVisibility(View.GONE);
                Toast.makeText(SellerInfoActivity.this,"Unable to load Seller details.Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void HandleCallSeller(final String mobileNo)
    {
        Button callBtn =(Button)findViewById(R.id.seller_call);
        callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+mobileNo));
                    ActivityCompat.requestPermissions(SellerInfoActivity.this,
                            new String[]     {Manifest.permission.CALL_PHONE},
                            1);
                    if (ActivityCompat.checkSelfPermission(SellerInfoActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);
                }
        });
    }
}
