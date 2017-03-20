package com.omglabs.boxofficebaasha;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener  {
    RecyclerView recyclerView;
    ArrayList<RowItem> itemsList = new ArrayList<>();
    RowViewAdapter adapter;
    Context context;
    boolean backButtonPressedOnce = false;
    String loginValue;
    SwipeRefreshLayout swipeRLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddTicketActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("isLoggedIn", loginValue);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loginValue = getIntent().getExtras().getString("isLoggedIn");
        swipeRLayout= (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getData("");
                    }
                }
        );



        getData("");
    }

    private void setVisibilty(boolean login,boolean logout)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        toolbar.getMenu().findItem(R.id.action_loginmenu).setVisible(login);

        toolbar.getMenu().findItem(R.id.action_logoutmenu).setVisible(logout);
    }

    private static final int TIME_INTERVAL=2000;
    private long BackPressed;
    @Override
    public void onBackPressed()
    {
        if(BackPressed+TIME_INTERVAL>System.currentTimeMillis())
        {
            finish();
        }
        else
        {
            Toast.makeText(MainActivity.this,"Press the back button once again to exit.",Toast.LENGTH_SHORT).show();
        }
        BackPressed=System.currentTimeMillis();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if(loginValue.equals("true")){
            setVisibilty(false,true);
        }
        else
        {
            setVisibilty(true,false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        itemsList.clear();
        switch(id)
        {
            case R.id.sort_movie_name:
                getData("moviename");
                return true;
            case R.id.sort_theatre:
                getData("theatrename");
                return true;
            case R.id.sort_date:
                getData("movie_date DESC");
                return true;
            case R.id.sort_ticket_count:
                getData("ticketcount DESC");
                return true;
            case R.id.sort_posted_time:
                getData("updated DESC");
                return true;
            case R.id.action_logoutmenu:
                Toast.makeText(this,"Logging out...",Toast.LENGTH_LONG);
                findViewById(R.id.loadingPanelMainPage).setVisibility(View.VISIBLE);

                Backendless.UserService.logout( new AsyncCallback<Void>()
                {
                    public void handleResponse( Void response )
                    {
                        loginValue="false";
                        findViewById(R.id.loadingPanelMainPage).setVisibility(View.GONE);
                        setVisibilty(true,false);
                    }

                    public void handleFault( BackendlessFault fault )
                    {
                        findViewById(R.id.loadingPanelMainPage).setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this,"Unable to log out.Check your internet connection",Toast.LENGTH_LONG);
                    }
                });
                return true;
            case R.id.action_loginmenu:
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getData(String sortColumn)
    {
        findViewById(R.id.loadingPanelMainPage).setVisibility(View.VISIBLE);
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        List<String> sortBy = new ArrayList<String>();
        if(sortColumn.isEmpty()) {
            sortBy.add("created DESC");
        }
        else
        {
            sortBy.add(sortColumn);
        }
        queryOptions.setSortBy( sortBy );
        dataQuery.setQueryOptions( queryOptions );
        dataQuery.setPageSize(100);
        final ArrayList<RowItem> arrayList = new ArrayList<RowItem>();
        Backendless.Persistence.of("SwapTicketDetails").find(dataQuery,new AsyncCallback<BackendlessCollection<Map>>() {
            @Override
            public void handleResponse(BackendlessCollection<Map> mapBackendlessCollection) {
                List<Map> listMap= mapBackendlessCollection.getData();
                for (Map map:listMap) {
                    RowItem rowItem = new RowItem();
                    rowItem.setTitle((String)map.get("moviename"));
                    rowItem.setTheatre((String)map.get("theatrename"));
                    rowItem.setCount((String)map.get("ticketcount"));
                    rowItem.setPostedUser((String)map.get("posteduser"));
                    rowItem.setTicketType((String)map.get("tickettype"));
                    rowItem.setTicketprice((String)map.get("ticketprice"));
                    Date moviedate = (Date)map.get("movie_date");
                    Date currDate = new Date();
                    if(moviedate.before(currDate))
                    {
                        continue;
                    }
                    rowItem.setDate(moviedate.getDate()+"/"+(moviedate.getMonth()+1)+"/"+ (moviedate.getYear()+1900));
                    rowItem.setTime(moviedate.getHours()+":"+ (moviedate.getMinutes()<10?"0":"") +moviedate.getMinutes());
                    arrayList.add(rowItem);
                }
                addItems(arrayList);
                findViewById(R.id.loadingPanelMainPage).setVisibility(View.GONE);
                swipeRLayout.setRefreshing(false);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                swipeRLayout.setRefreshing(false);
                findViewById(R.id.loadingPanelMainPage).setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"Unable to load the tickets.Please try again later.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addItems(ArrayList<RowItem> newItems)
    {
        if(newItems.size()==0)
        {
            Toast.makeText(MainActivity.this,"Sorry :( No tickets available right now",Toast.LENGTH_SHORT).show();

        }
        adapter = new RowViewAdapter(MainActivity.this, newItems);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this,SellerInfoActivity.class);
                        RowViewAdapter adapterRow= (RowViewAdapter)recyclerView.getAdapter();
                        RowItem rowIntent= adapterRow.itemsList.get(position);
                        intent.putExtra("rowitem",rowIntent);
                        startActivity(intent);
                    }
                })
        );
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
