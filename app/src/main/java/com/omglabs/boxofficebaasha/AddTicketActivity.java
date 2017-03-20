package com.omglabs.boxofficebaasha;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anusha on 28/05/16.
 */
public class AddTicketActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner movieNamesSpinner;
    private Spinner theatreNamesSpinner;
    private Spinner ticketTypeSpinner;
    private EditText datePicker;
    private EditText timePicker;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        String isLoggedIn = getIntent().getExtras().getString("isLoggedIn");
        if(isLoggedIn.equals("false"))
        {
            Toast.makeText(AddTicketActivity.this,"Login to post your tickets.",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AddTicketActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        initToolbar();

        if (Build.VERSION.SDK_INT >= 21)
        {
            EditText dateText;
            dateText = (EditText) findViewById(R.id.addTK_date_edit);
            dateText.setShowSoftInputOnFocus(false);

            EditText timeText;
            timeText = (EditText) findViewById(R.id.addTK_time_edit);
            timeText.setShowSoftInputOnFocus(false);
        }

        findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.VISIBLE);
        ticketTypeSpinner = (Spinner) findViewById(R.id.addTK_ticketType_spinner);
        ticketTypeSpinner.setOnItemSelectedListener(this);
        List<String> ticketType = new ArrayList<String>();
        ticketType.add("Online");
        ticketType.add("Offline");
        ArrayAdapter<String> ticketTypeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,ticketType);
        ticketTypeSpinner.setAdapter(ticketTypeAdapter);

        datePicker = (EditText) findViewById(R.id.addTK_date_edit);
        timePicker = (EditText) findViewById(R.id.addTK_time_edit);
        datePicker.setOnClickListener(this);
        timePicker.setOnClickListener(this);

        movieNamesSpinner = (Spinner) findViewById(R.id.addTK_movie_name_spinner);
        movieNamesSpinner.setOnItemSelectedListener(this);

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        List<String> sortBy = new ArrayList<String>();
        sortBy.add("moviename");
        queryOptions.setSortBy( sortBy );
        dataQuery.setQueryOptions( queryOptions );
        dataQuery.setPageSize(100);
        Backendless.Persistence.of("MovieList").find(dataQuery,new AsyncCallback<BackendlessCollection<Map>>() {
            @Override
            public void handleResponse(BackendlessCollection<Map> mapBackendlessCollection) {
                List<String> movieNameList = new ArrayList<String>();
                List<Map> listMap= mapBackendlessCollection.getData();
                for (Map map:listMap) {
                    movieNameList.add ((String)map.get("moviename"));
                }
                BindSpinner(movieNamesSpinner,movieNameList);
                findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
                Toast.makeText(AddTicketActivity.this,"Unable to load the Movie List.Please try again later.",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.VISIBLE);

        BackendlessDataQuery dataQuery1 = new BackendlessDataQuery();
        QueryOptions queryOptions1 = new QueryOptions();
        List<String> sortBy1 = new ArrayList<String>();
        sortBy1.add("theatrename");
        queryOptions1.setSortBy( sortBy1 );
        dataQuery1.setQueryOptions( queryOptions1 );
        dataQuery1.setPageSize(100);
        theatreNamesSpinner = (Spinner) findViewById(R.id.addTK_theatre_name_spinner);
        theatreNamesSpinner.setOnItemSelectedListener(this);
        Backendless.Persistence.of("TheatreList").find(dataQuery1,new AsyncCallback<BackendlessCollection<Map>>() {
            @Override
            public void handleResponse(BackendlessCollection<Map> mapBackendlessCollection) {
                List<String> theatreNameList = new ArrayList<String>();
                List<Map> listMap= mapBackendlessCollection.getData();
                for (Map map:listMap) {
                    theatreNameList.add ((String)map.get("theatrename"));
                }
                BindSpinner(theatreNamesSpinner,theatreNameList);
                findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
                Toast.makeText(AddTicketActivity.this,"Unable to load the theatre List.Please try again later.",Toast.LENGTH_SHORT).show();
            }
        });

        addBtn =(Button)findViewById(R.id.addTk_addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostTicket();
            }
        });

    }

    private void PostTicket(){
        findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.VISIBLE);
        String movieName = movieNamesSpinner.getSelectedItem().toString();
        if(movieName.compareTo("")==0)
        {
            findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
            Toast.makeText(AddTicketActivity.this,"Movie List not loaded.Please try again later.",Toast.LENGTH_SHORT).show();
            return;
        }

        String theatreName = theatreNamesSpinner.getSelectedItem().toString();
        if(theatreName.compareTo("")==0)
        {
            findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
            Toast.makeText(AddTicketActivity.this,"Theatre List not loaded.Please try again later.",Toast.LENGTH_SHORT).show();
            return;
        }

        String ticketType = ticketTypeSpinner.getSelectedItem().toString();
        if(ticketType.compareTo("")==0)
        {
            findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
            Toast.makeText(AddTicketActivity.this,"Ticket types not loaded.Please try again later.",Toast.LENGTH_SHORT).show();
            return;
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObject=null;
        String dateVal =datePicker.getText().toString();
        if(dateVal.compareTo("")==0)
        {
            findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
            Toast.makeText(AddTicketActivity.this,"Invalid Date.",Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            dateObject = formatter.parse(dateVal);
        }
        catch(ParseException e)
        {
            // Date currDate = new Date();
        }
        Date currDate = new Date();
        Date currentDate=null;
        try {
         currentDate= formatter.parse(currDate.getDate()+"/"+(currDate.getMonth()+1)+"/"+ (currDate.getYear()+1900));
        }
        catch(ParseException e)
        {
            // Date currDate = new Date();
        }
        if(currentDate.after(dateObject))
        {
            findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
            Toast.makeText(AddTicketActivity.this,"Invalid Date.",Toast.LENGTH_SHORT).show();
            return;
        }

        String timeVal =timePicker.getText().toString();
        if(timeVal.compareTo("")==0)
        {
            findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
            Toast.makeText(AddTicketActivity.this,"Invalid Time.",Toast.LENGTH_SHORT).show();
            return;
        }

        EditText ticketCount;
        ticketCount = (EditText) findViewById(R.id.addTK_count_edit);
        String ticketCountVal =ticketCount.getText().toString();
        if(ticketCountVal.trim().compareTo("")==0 )
        {
            findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
            Toast.makeText(AddTicketActivity.this,"Invalid Ticket Count.",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            int finalValue=Integer.parseInt(ticketCountVal);
            if(finalValue>10)
            {
                findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
                Toast.makeText(AddTicketActivity.this,"Invalid Ticket Count.",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        EditText ticketPrice;
        ticketPrice = (EditText) findViewById(R.id.addTK_price_edit);
        String ticketPriceVal =ticketPrice.getText().toString();
        if(ticketPriceVal.compareTo("")==0)
        {
            findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
            Toast.makeText(AddTicketActivity.this,"Invalid Ticket Price.",Toast.LENGTH_SHORT).show();
            return;
        }

        String  currentUserID;
        BackendlessUser user = Backendless.UserService.CurrentUser();
        if( user != null )
        {
            currentUserID=user.getEmail();
        }
        else
        {
            findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
            Toast.makeText(AddTicketActivity.this,"Unable to post ticket. Please try again later.",Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.MONTH, mMonth);
        cal.set(Calendar.DAY_OF_MONTH, mDay);
        cal.set(Calendar.HOUR_OF_DAY,mHour);
        cal.set(Calendar.MINUTE,mMinute);

        HashMap ticketDetail = new HashMap();
        ticketDetail.put("location","Chennai");
        ticketDetail.put("moviename",movieName);
        ticketDetail.put("theatrename",theatreName);
        ticketDetail.put("tickettype",ticketType);
        ticketDetail.put("ticketcount",ticketCountVal);
        ticketDetail.put("ticketprice",ticketPriceVal);
        ticketDetail.put("movie_date",cal);
        ticketDetail.put("posteduser",currentUserID);

        Backendless.Persistence.of( "SwapTicketDetails" ).save(ticketDetail, new AsyncCallback<Map>() {
            @Override
            public void handleResponse(Map map) {
                findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
                Toast.makeText(AddTicketActivity.this,"Posted Successfully",Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(AddTicketActivity.this,MainActivity.class);
                //startActivity(i);
                //finish();
                onBackPressed();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                findViewById(R.id.loadingPanelAddTKPage).setVisibility(View.GONE);
                Toast.makeText(AddTicketActivity.this,"Unable to post ticket. Please try again later.",Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }

    private void BindSpinner(Spinner spinner,List<String> list) {
        ArrayAdapter<String> theatreNameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
        spinner.setAdapter(theatreNameAdapter);
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.addTk_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (null != toolbar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Post Your Ticket");
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_back));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // String selected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v == datePicker) {
            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(datePicker.getWindowToken(), 0);

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            mDay=dayOfMonth;
                            mMonth=monthOfYear;
                            mYear=year;
                            datePicker.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == timePicker) {
            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(timePicker.getWindowToken(), 0);

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            mHour=hourOfDay;
                            mMinute=minute;
                            timePicker.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

    }
}
