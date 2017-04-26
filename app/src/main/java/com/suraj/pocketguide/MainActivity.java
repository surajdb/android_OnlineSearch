package com.suraj.pocketguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.suraj.pocketguide.Adapters.ScreenshotAdapter;
import com.suraj.pocketguide.Adapters.SearchResultAdapter;
import com.suraj.pocketguide.Adapters.SpeedDialAdapter;
import com.suraj.pocketguide.DBhandlers.DatabaseHandler;
import com.suraj.pocketguide.Models.SearchResultModel;
import com.suraj.pocketguide.Models.SpeedDialModel;
import com.suraj.pocketguide.Utility.AppConstants;
import com.suraj.pocketguide.Utility.AppUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private WebView mainWebView;
    private EditText editTextSearch;
    private TextView GO;
    private ImageView imgHome;
    private ImageView imgAddSites;
    private ImageView imgForward;
    private int searchToggle = 0; // to avoid next click button

    GridView mainGridView;
    ListView mainListView;

    String URL;
    String URL2;

    GoogleApiClient mLocationClient;
    Location currentLocation;

    //private int SearchStartIndex = 1 ;
    private int SearchStartIndexPer = 1 ;
    private int SearchStartIndexNonPer = 1 ;
    List<SpeedDialModel> speedDialList;
    List <SearchResultModel.Items> list = new ArrayList<SearchResultModel.Items>();
    SpeedDialAdapter sdAdapter;
    ScreenshotAdapter ssAdapter;
    RequestParams params = new RequestParams();

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            getPermission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            GO = (TextView) findViewById(R.id.txtGo);
            GO.setOnClickListener(this);
            editTextSearch = (EditText) findViewById(R.id.editTextSearch);

            imgHome = (ImageView) findViewById(R.id.imgHome);
            imgAddSites = (ImageView) findViewById(R.id.imgAddSites);
            imgForward = (ImageView) findViewById(R.id.imgForward);

            imgHome.setOnClickListener(this);
            imgAddSites.setOnClickListener(this);
            imgForward.setOnClickListener(this);
            mLocationClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mLocationClient.connect();

            setupSpeedDial(AppConstants.kSpeedDialType);// for displaying the Grid view for adapter

            increaseClickableSize(GO);
        }


    public void setupSpeedDial(String type)
    {
        //if gridview is already set initialise it again
        clearForMainGridView();

        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        final String thisType = type;

        speedDialList = db.getAllSpeedDial(thisType);

        //create a + add a new speedial
        SpeedDialModel addNewSd = new SpeedDialModel();
        addNewSd.setBookmarkLink("Add New");
        addNewSd.setBookmarkType("New");
        //adding the new tab to the adapter
        speedDialList.add(addNewSd);
        sdAdapter = new SpeedDialAdapter(
                this, R.layout.speed_dial, speedDialList);
        mainGridView = (GridView) findViewById(R.id.mainGridView);
        getVisibleMainGridView();// enable view

        mainGridView.setAdapter(sdAdapter);
        mainGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (position == speedDialList.size()-1)// for add button
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Enter Web Address");

                    final EditText input = new EditText(v.getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
                    input.setText("http://www.");
                    builder.setView(input);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!input.getText().toString().equals("http://www."))
                            {
                                SpeedDialModel addNewSd = new SpeedDialModel();
                                addNewSd.setBookmarkLink( input.getText().toString().trim());
                                addNewSd.setBookmarkImage("Image link will come here");
                                addNewSd.setBookmarkType(thisType);
                                db.addBookMark(addNewSd);
                                sdAdapter.insert(addNewSd,sdAdapter.getCount()-1);
                                sdAdapter.setNotifyOnChange(true);
                                // change image since data changes but image remains the same
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                else
                {
                    LoadUrl(speedDialList.get(position).getBookmarkLink());//
                    clearFocus(getCurrentFocus());
                }
            }
        });

        mainGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                if(position < sdAdapter.getCount()-1)
                {
                    Snackbar snackbar = Snackbar
                            .make(view, "Do you want to delete the Speed Dial ?", Snackbar.LENGTH_LONG)
                            .setAction("Delete", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                    SpeedDialModel sdModelDelete = speedDialList.get(position);
                                    db.deleteBookMark(sdModelDelete.getBookmarkId());
                                    speedDialList.remove(position);
                                    sdAdapter.notifyDataSetChanged();
                                    Snackbar snackbar1 = Snackbar.make(view, "Speed Dial Deleted !!", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();
                                }
                            });
                    snackbar.show();
                }
                return true;
            }
        });

        // set listview invisible if home is called when a list is already in place
        clearForMainGridView();
        searchToggle = 0;
        db.close();
    }

    public void setupSearch() {
        //if listview has been set already reset it
        clearForMainListView();

        String query = editTextSearch.getText().toString().trim().replace(" ", "+");
        String sites = getSites();
        String countryCode = getLocation();
        //Personalised URL
        URL = AppConstants.kcseLink + "q=" + query + "+" + sites
                + AppConstants.kcsecx + AppConstants.kcseStart + Integer.toString(SearchStartIndexPer) + countryCode + AppConstants.kcseAPIKey;
        //Non Personalised URL
        URL2 = AppConstants.kcseLink + "q=" + query
                + AppConstants.kcsecx + AppConstants.kcseStart + Integer.toString(SearchStartIndexNonPer) + countryCode + AppConstants.kcseAPIKey;

        progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme);
        progressBar();
        getDetails(URL, 0);
    }

    public void getDetails(String URL, final int a )
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, null, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), AppConstants.kERROR_MSG, Toast.LENGTH_SHORT).show();
                Log.d("error", String.valueOf(statusCode));
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                SearchResultModel model = new SearchResultModel();
                model = gson.fromJson(String.valueOf(responseString), SearchResultModel.class);

                final SearchResultModel.Items[] modelWebItems = model.getWebItems();
                //String[] both = (String[])ArrayUtils.addAll(first, second);
                // Just to obatin the number of results so fouund in google cse
                if(modelWebItems != null) {
                    SearchResultModel.Queries queries = model.getQueries();
                    SearchResultModel.Items m = new SearchResultModel().new Items();
                    if (a == 0) {
                        m.setWebURLTitle("Personalised Result");
                        AppConstants.kTtlNumofResultPers = queries.getRequests()[0].getTotalResults();
                    } else {
                        m.setWebURLTitle("General Results");
                        AppConstants.kTtlNumofResultNonPers = queries.getRequests()[0].getTotalResults();
                    }
                    list.add(m);

                    for (int i = 0; i < modelWebItems.length; i++) {
                        list.add(modelWebItems[i]);
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"No Result Found",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                if(a == 1)
                {
                    prepareSearchList();
                }
                else  {
                    getDetails(URL2, 1); // call the second url which show general search result
                }
            }
        });
    }
    public void prepareSearchList(){

        SearchResultAdapter searchResultAdapter = new SearchResultAdapter (
                getApplicationContext(), R.layout.search_result, list );
        mainListView = (ListView) findViewById(R.id.mainListView);
        getVisibleMainListView();// enable List view
        mainListView.setAdapter(searchResultAdapter);

        mainListView.setAdapter(searchResultAdapter);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String URL = list.get(position).getWebURL();
                if(URL != null){
                    LoadUrl(URL);
                }
            }
        });
        // Long click Listner to add as bookmark or speedial
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (list.get(position).getWebURL() != null) {
                Snackbar snackbar = Snackbar
                        .make(view, "Do you want to add it to the site list ?", Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                                SpeedDialModel addNewSd = new SpeedDialModel();
                                addNewSd.setBookmarkLink("http://"+list.get(position).getWebURLToShow());
                                addNewSd.setBookmarkImage("Image link will come here");
                                addNewSd.setBookmarkType(AppConstants.kAddSitesToSearch); // A  for Add sites
                                db.addBookMark(addNewSd);
                                Snackbar snackbar1 = Snackbar.make(view, "Site  added", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }
                        });
                snackbar.show();
            }
                return true;
            }
        });
        clearFocus(getCurrentFocus());
        progressDialog.dismiss();
        // hide other views.
        clearForMainListView();
        setImage(4);
    searchToggle = 1;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {

                if(mainWebView != null)
                {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        if (mainWebView.canGoBack()) {
    //                    if (x == 4) {
                            mainWebView.goBack();
                        } else {
                            setupSpeedDial(AppConstants.kSpeedDialType);
                        }
                        return true;
                     }
                 }
            }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.imgHome):
            {
                clearForMainGridView();
                setupSpeedDial(AppConstants.kSpeedDialType);
                setImage(1);
                break;
            }
            case (R.id.imgAddSites):
            {
                clearForMainGridView();
                setupSpeedDial(AppConstants.kAddSitesToSearch);
                setImage(2);
                break;
            }
            case (R.id.imgForward):
            {
                if(searchToggle == 1) {
                    if (editTextSearch.getText().toString().equals("")) {
                        Toast.makeText(this, "Hello, I'm not a Specialised AI. Enter a text to search :)", Toast.LENGTH_SHORT).show();
                    } else {
                        if (((AppConstants.kTtlNumofResultPers - AppConstants.kTtlNumofResultPers % 10) > SearchStartIndexPer)||
                           ((AppConstants.kTtlNumofResultPers - AppConstants.kTtlNumofResultPers % 10) > SearchStartIndexNonPer))
                            {
                                if((AppConstants.kTtlNumofResultPers - AppConstants.kTtlNumofResultPers % 10) > SearchStartIndexPer)
                                {
                                    SearchStartIndexPer = SearchStartIndexPer + 10;
                                }
                                if((AppConstants.kTtlNumofResultNonPers - AppConstants.kTtlNumofResultNonPers % 10) > SearchStartIndexNonPer)
                                {
                                    SearchStartIndexNonPer = SearchStartIndexNonPer + 10;
                                    setupSearch();
                                }
                                list.clear();
                            }
                        else
                                Toast.makeText(this, "No more results to display", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Please search for something", Toast.LENGTH_SHORT).show();
                }
                setImage(3);
                break;
            }
            case R.id.txtGo: {
                if(editTextSearch.getText().toString().equals(""))
                {
                    Toast.makeText(this,"Hello, I'm not a Specialised AI. Enter a text to search :)",Toast.LENGTH_SHORT).show();
                }
                else {
                    list.clear();
                    setupSearch();
                    SearchStartIndexPer = 1;
                    SearchStartIndexNonPer=1;
                }
                break;
            }
        }
    }

//
    public void progressBar()
    {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending data...");
        progressDialog.show();
    }

    public void getVisibleMainGridView()
    {
        if(mainGridView != null) {
            if (mainGridView.getVisibility() == View.GONE)
                mainGridView.setVisibility(View.VISIBLE);
                mainGridView.setEnabled(true);
        }
    }

    public void getVisibleMainListView()
    {
        if(mainListView != null) {
            if (mainListView.getVisibility() == View.GONE)
                mainListView.setVisibility(View.VISIBLE);
                mainListView.setEnabled(true);
        }
    }

    public void getVisibleMainWebView()
        {
            if(mainWebView != null) {
                if (mainWebView.getVisibility() == View.GONE) {
                    mainWebView.setVisibility(View.VISIBLE);
                }
                    mainWebView.setEnabled(true);
            }
        }

    public void LoadUrl(String URL)
    {
        clearForMainWebView();
        mainWebView = (WebView)findViewById(R.id.mainWebView);
        mainWebView.getSettings().setJavaScriptEnabled(true);
        mainWebView.setWebViewClient(new WebViewClient());
        mainWebView.loadUrl(URL);
        getVisibleMainWebView();
        setImage( 4 ); // set default image for buttons
    }

    public void clearForMainWebView()
    {
        if(mainGridView != null)
            mainGridView.setVisibility(View.GONE);
            mainGridView = null;
        if(mainListView != null)
            mainListView.setVisibility(View.GONE);
            mainListView = null ;
    }

    public void clearForMainListView()
    {
        if(mainGridView != null)
            mainGridView.setVisibility(View.GONE);
            mainGridView = null;
        if(mainWebView != null)
            mainWebView.setVisibility(View.GONE);
            mainWebView = null;
    }

    public void clearForMainGridView()
    {
        if(mainWebView != null)
            mainWebView.setVisibility(View.GONE);
            mainWebView = null; // to completly remove webview
        if(mainListView != null)
            mainListView.setVisibility(View.GONE);
            mainListView = null;
    }

    public void clearFocus(View view )
    {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public String getLocation() {// returns country code to have a better location wise search

        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
        String countryCode = "";

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<android.location.Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                Address result;

                if (addresses != null && !addresses.isEmpty()) {
                    countryCode= addresses.get(0).getCountryCode();
                    countryCode = "&gl="+countryCode;// gl for google cse api
                }
            } catch (IOException ignored) {
                Toast.makeText(this,"Not able to get Country",Toast.LENGTH_SHORT).show();
            }
        return countryCode;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public void getPermission() {

            if(!AppUtility.isPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, this))
            AppUtility.getPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, AppConstants.REQUEST_CODE_WGALLERY);
            if(!AppUtility.isPermissionGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE, this))
            AppUtility.getPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE, AppConstants.REQUEST_CODE_GALLERY);
             if(!AppUtility.isPermissionGranted(android.Manifest.permission.INTERNET, this))
            AppUtility.getPermission(this, android.Manifest.permission.INTERNET, AppConstants.REQUEST_CODE_INTERNET);
             if(!AppUtility.isPermissionGranted(android.Manifest.permission.ACCESS_NETWORK_STATE, this))
            AppUtility.getPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE, AppConstants.REQUEST_ACCESS_NETWORK_STATE);
             if(!AppUtility.isPermissionGranted(android.Manifest.permission.ACCESS_COARSE_LOCATION, this))
            AppUtility.getPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION, AppConstants.REQUEST_ACCESS_COARSE_LOCATION);
             if(!AppUtility.isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION, this))
            AppUtility.getPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION, AppConstants.REQUEST_ACCESS_FINE_LOCATION);

    }
    public void increaseClickableSize(View view)
    {
        final View parent = (View) GO.getParent();  // button: the view you want to enlarge hit area
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                GO.getHitRect(rect);
                rect.top -= 500;    // increase top hit area
                rect.left -= 500;   // increase left hit area
                rect.bottom += 500; // increase bottom hit area
                rect.right += 500;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect, GO));
            }
        });
    }

    public String getSites() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String sites = "";
        List<SpeedDialModel> sitesToSearchList = new ArrayList<SpeedDialModel>();
        sitesToSearchList = db.getAllSpeedDial(AppConstants.kAddSitesToSearch);

        for (int i = 0; i < sitesToSearchList.size(); i++)
        {
            if( sitesToSearchList.size() == 0)
            {
                //do nothing
            }
            if( sitesToSearchList.size() == 1) {
                sites = "+site%3A" + sitesToSearchList.get(i).getBookmarkLink().replace(":", "%3A").replace("/", "%2F");
            }
            else {
                if (i == sitesToSearchList.size() - 1){
                    sites = sites+"site%3A" + sitesToSearchList.get(i).getBookmarkLink().replace(":", "%3A").replace("/", "%2F");}
                else
                {sites = sites+"site%3A" + sitesToSearchList.get(i).getBookmarkLink().replace(":", "%3A").replace("/", "%2F") + "+OR+";}
            }
        }
         db.close();
         return sites;
    }

    public void setImage(int buttonPressed )
    {
        if( buttonPressed == 1) {
            imgHome.setImageDrawable(getResources().getDrawable(R.drawable.main_startpage_select));
            imgAddSites.setImageDrawable(getResources().getDrawable(R.drawable.main_speed_dial));
            imgForward.setImageDrawable(getResources().getDrawable(R.drawable.main_forward));
          }
        if( buttonPressed == 2) {
            imgHome.setImageDrawable(getResources().getDrawable(R.drawable.main_startpage));
            imgAddSites.setImageDrawable(getResources().getDrawable(R.drawable.main_speed_dial_select));
            imgForward.setImageDrawable(getResources().getDrawable(R.drawable.main_forward));
        }
        if( buttonPressed == 3) {
            imgHome.setImageDrawable(getResources().getDrawable(R.drawable.main_startpage));
            imgAddSites.setImageDrawable(getResources().getDrawable(R.drawable.main_speed_dial));
            imgForward.setImageDrawable(getResources().getDrawable(R.drawable.main_forward_select));
        }
        if( buttonPressed == 4) {
            imgHome.setImageDrawable(getResources().getDrawable(R.drawable.main_startpage));
            imgAddSites.setImageDrawable(getResources().getDrawable(R.drawable.main_speed_dial));
            imgForward.setImageDrawable(getResources().getDrawable(R.drawable.main_forward));
        }

    }
}
