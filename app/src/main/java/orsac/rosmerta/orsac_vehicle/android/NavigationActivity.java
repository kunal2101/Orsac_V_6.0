package orsac.rosmerta.orsac_vehicle.android;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.Dialogs.PrettyDialog;
import orsac.rosmerta.orsac_vehicle.android.Dialogs.PrettyDialogCallback;
import orsac.rosmerta.orsac_vehicle.android.Orsac.AllMines;
import orsac.rosmerta.orsac_vehicle.android.Orsac.AndyUtils;
import orsac.rosmerta.orsac_vehicle.android.Orsac.BarGraphChart;
import orsac.rosmerta.orsac_vehicle.android.Orsac.CircleActivity;
import orsac.rosmerta.orsac_vehicle.android.Orsac.Demo_CircleActivity;
import orsac.rosmerta.orsac_vehicle.android.Orsac.Model.Pojo_Device_Management;
import orsac.rosmerta.orsac_vehicle.android.Orsac.NavigationDrawerFragment;
import orsac.rosmerta.orsac_vehicle.android.Orsac.Orsac_Admin_sec_Trip_count;
import orsac.rosmerta.orsac_vehicle.android.Orsac.StackedbarChart;
import orsac.rosmerta.orsac_vehicle.android.Orsac.ViewPagerActivity;

public class NavigationActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private RecyclerView recyclerView;
    private List<Admin_model> array_admin;
    private List<Pojo_Device_Management> array_admin_;
    Admin_model admin_model;
    private Myadapter mAdapter;

    Map map;
    private ViewPager viewPager;
    private LinearLayout dotsLayout, lyn_active_vtus;
    private int[] layouts;
    private TextView[] dots;
    private AdminPage.MyViewPagerAdapter myViewPagerAdapter;
    String imageUri, imageUri_one, imageUri_two, imageUri_three, imageUri_four;
    //int[] imags = {R.drawable.arya_three, R.drawable.atlanata_three,R.drawable.ca_sysy,R.drawable. etrans_two,R.drawable.fasttra_two,R.drawable.itrangle,R.drawable.ordinio,R.drawable.rose_six, R.drawable.trimmble_three};
    int[] imags = {R.drawable.arya_three, R.drawable.ca_sysy,R.drawable. etrans_two,R.drawable.fasttra_two,R.drawable.itrangle,R.drawable.atlanata_three,R.drawable.ordinio,R.drawable.rose_six, R.drawable.trimmble_three};
    ImageView arraow;
    static int i = 0;
    ArrayList<HashMap<String, String>> GetCompanyDetail;
    private GridLayoutManager lLayout;
    private ArrayList<Admin_model> companyList;
    String totalcompanies, totaltripcountcnt, totaldevices, totalinstalled, installedDevices, companyName, totalDevices, availabledevices, tripcount;
    private MyTextView tool_title,etp_text;
    private ImageView tool_back_icon;
    TextView tot_com, tot_trip, tot_dev, veh_trac ,sea_tot_ac,sea_tot_insa,loin_name,tot_vs_active,T_ETP_vs_T_Active,i3ms_redg,live_etp,etp_in_180,live_vtu;
    TextView  now, HRs_24, hrs_48, days_7, day_15, day_30, day_90, day_180;
    TextView active_trucks,operational_truck,inactive_trucks;
    LinearLayout piechart, stackedbar, bargraph, circlepie,all_mine;
    FrameLayout navi_frame,frame_circle;
    DrawerLayout drawer;
    float value_avai[];
    float value_inst[];
    String value_comap[];
    double[] tripCount;
    String[] compy_name;
    Handler mHandler = new Handler();
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private final static int INTERVAL = 1000 * 60 *30  ;
    int tot_device, tot_install;
    int tot_active_test=0,tot_inactive_test=0;
    FrameLayout frame_floating;
    String totalActive;
    PreferenceHelper preferenceHelper;

    // Float[] value_inst;
    private ArrayList<HashMap<String, String>> getData;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    View myView;
    LinearLayout line_tile;
    boolean isUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        i3ms_redg =(TextView)findViewById(R.id.i3ms_redg);
        etp_in_180=(TextView) findViewById(R.id.etp_in_180);
        live_vtu=(TextView) findViewById(R.id.live_vtu);
        active_trucks=(TextView) findViewById(R.id.active_trucks);
        operational_truck=(TextView) findViewById(R.id.operational_truck);
        inactive_trucks= (TextView) findViewById(R.id.inactive_trucks);
        myView = findViewById(R.id.my_view);
        line_tile =(LinearLayout) findViewById(R.id.my_view);
        lyn_active_vtus = (LinearLayout) findViewById(R.id.lyn_active_vtus);
      //  prettyDialog_report();

        isUp = false;
        preferenceHelper = new PreferenceHelper(this);


        arraow =(ImageView)findViewById(R.id.arraow);
        arraow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUp) {
                   // slideDown(myView);
                  //  myButton.setText("Slide up");
                    line_tile.setVisibility(View.VISIBLE);
                    arraow.setBackground(ContextCompat.getDrawable(NavigationActivity.this, R.drawable.ic_arrow_up));

                } else {
                    //slideUp(myView);
                    //myButton.setText("Slide down");
                    line_tile.setVisibility(View.GONE);
                    arraow.setBackground(ContextCompat.getDrawable(NavigationActivity.this, R.drawable.ic_arrow));

                }
                isUp = !isUp;
            }
        });
        preferenceHelper = new PreferenceHelper(this);


        tot_com = (TextView) findViewById(R.id.tot_com);
        etp_text =(MyTextView)findViewById(R.id.etp_text);

        loin_name= (TextView) findViewById(R.id.loin_name);
        sea_tot_ac = (TextView) findViewById(R.id.sea_tot_ac);
        frame_floating= (FrameLayout)findViewById(R.id.frame_floating);
        frame_circle= (FrameLayout)findViewById(R.id.frame_circle);
        sea_tot_insa = (TextView) findViewById(R.id.sea_tot_insa);

        //live_etp=(TextView) findViewById(R.id.live_etp);
        tot_trip = (TextView) findViewById(R.id.tot_trip);
        tot_dev = (TextView) findViewById(R.id.tot_dev);
        veh_trac = (TextView) findViewById(R.id.veh_trac);
        piechart = (LinearLayout) findViewById(R.id.piechart);
        stackedbar = (LinearLayout) findViewById(R.id.stackedbar);
        bargraph = (LinearLayout) findViewById(R.id.bargraph);
        circlepie = (LinearLayout) findViewById(R.id.circlepie);
        all_mine= (LinearLayout)findViewById(R.id.all_mine);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        T_ETP_vs_T_Active= (TextView)findViewById(R.id.T_ETP_vs_T_Active);
        tot_vs_active=(TextView)findViewById(R.id.tot_vs_active);
        now = (TextView)findViewById(R.id.now);
        HRs_24 = (TextView)findViewById(R.id.HRs_24);
        hrs_48 = (TextView)findViewById(R.id.hrs_48);
        days_7 = (TextView)findViewById(R.id.days_7);
        day_15 = (TextView)findViewById(R.id.day_15);
        day_30 = (TextView)findViewById(R.id.day_30);
        day_90 = (TextView)findViewById(R.id.day_90);
        day_180 = (TextView)findViewById(R.id.day_180);


        final Handler handler = new Handler();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        if(isConnection()){
            AndyUtils.showCustomProgressDialog(NavigationActivity.this,
                    "Fetching Information Please wait...", false, null);

            new preProcessDash().execute();
        }else{
            Toast.makeText(NavigationActivity.this,"No Internet , Please Try After sometime",Toast.LENGTH_LONG).show();
        }


        map = new HashMap();
        companyList = new ArrayList<>();
        //login_info();
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        tool_title.setText("Admin Page");
        frame_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inty = new Intent(NavigationActivity.this, CircleActivity.class);
                startActivity(inty);
            }
        });
        frame_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Document document = new Document();
                    // write the document content
                    String directory_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ORSAC/";
                    File file = new File(directory_path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    String targetPdf = directory_path+"Vendor_Detail.pdf";
                    File filePath = new File(targetPdf);
                    try {

                        PdfWriter.getInstance(document, new FileOutputStream(filePath));
                        document.open();

                        addMetaData(document);
                        //addTitlePage(document);
                        addContent(document, getData);

                        Toast.makeText(NavigationActivity.this, "PDF Download Sucessfully ", Toast.LENGTH_LONG).show();
                       // File files = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                       //         "/ORSAC/Vendor_Detail.pdf");
                      //  String path_ = "file:///storage/e/ORSAC/Vendor_Detail.pdf";
                        if(Build.VERSION.SDK_INT>=24){
                            try{
                                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                m.invoke(null);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }

                        openPdf(NavigationActivity.this,targetPdf);
                       // Uri path = Uri.fromFile(files);
                       // Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                       // pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      //  pdfOpenintent.setDataAndType(path, "application/pdf");
                        try {
                       //     startActivity(pdfOpenintent);
                        }
                        catch (ActivityNotFoundException e) {

                        }
                    } catch (IOException e) {
                        Log.e("main", "error "+e.toString());
                        Toast.makeText(NavigationActivity.this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
                    }
                    // close the document
                    document.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            PackageInfo pInfo = NavigationActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            loin_name.setText("Powered By ORSAC - V-"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

      /*  try{
       Intent iin= getIntent();
       Bundle b = iin.getExtras();
       String j =(String) b.get("username");
       loin_name.setText("Logged in  as -- "+j);
   }catch (Exception e){
       e.getMessage();
   }*/

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
/*
        recyclerView.setHasFixedSize(true);
       // RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);*/
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        array_admin = new ArrayList<>();
        array_admin_ = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navi_frame = (FrameLayout) findViewById(R.id.navi_frame);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

       /* NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
        navi_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        ImageView imgview = (ImageView) findViewById(R.id.navigationIcon);
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.openDrawer(GravityCompat.START);

            }
        });



        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now.setBackgroundResource(R.drawable.button_selecte);
                HRs_24.setBackgroundResource(R.drawable.button_bg);
                hrs_48.setBackgroundResource(R.drawable.button_bg);
                days_7.setBackgroundResource(R.drawable.button_bg);
                day_15.setBackgroundResource(R.drawable.button_bg);
                day_30.setBackgroundResource(R.drawable.button_bg);
                day_90.setBackgroundResource(R.drawable.button_bg);
                day_180.setBackgroundResource(R.drawable.button_bg);
                etp_text.setText("Current ETPs Issued");
                if(isConnection()){
                    AndyUtils.showCustomProgressDialog(NavigationActivity.this,
                            "Fetching Information Please wait...", false, null);
                  //  array_admin.clear();
                    new preProcessDash().execute();
                }else{
                    Toast.makeText(NavigationActivity.this,"No Internet , Please Try After sometime",Toast.LENGTH_LONG).show();
                }
            }
        });

        HRs_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now.setBackgroundResource(R.drawable.button_bg);
                HRs_24.setBackgroundResource(R.drawable.button_selecte);
                hrs_48.setBackgroundResource(R.drawable.button_bg);
                days_7.setBackgroundResource(R.drawable.button_bg);
                day_15.setBackgroundResource(R.drawable.button_bg);
                day_30.setBackgroundResource(R.drawable.button_bg);
                day_90.setBackgroundResource(R.drawable.button_bg);
                day_180.setBackgroundResource(R.drawable.button_bg);
                etp_text.setText("ETPs Issued in 1 Days");
                if(isConnection()){
                    AndyUtils.showCustomProgressDialog(NavigationActivity.this,
                            "Fetching Information Please wait...", false, null);
                    //array_admin.clear();
                    new preProcessDash_daywise().execute("1");
                }else{
                    Toast.makeText(NavigationActivity.this,"No Internet , Please Try After sometime",Toast.LENGTH_LONG).show();
                }

            }
        });

        hrs_48.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now.setBackgroundResource(R.drawable.button_bg);
                HRs_24.setBackgroundResource(R.drawable.button_bg);
                hrs_48.setBackgroundResource(R.drawable.button_selecte);
                days_7.setBackgroundResource(R.drawable.button_bg);
                day_15.setBackgroundResource(R.drawable.button_bg);
                day_30.setBackgroundResource(R.drawable.button_bg);
                day_90.setBackgroundResource(R.drawable.button_bg);
                day_180.setBackgroundResource(R.drawable.button_bg);
                etp_text.setText("ETPs Issued in 2 Days");
                if(isConnection()){
                    AndyUtils.showCustomProgressDialog(NavigationActivity.this,
                            "Fetching Information Please wait...", false, null);
                   // array_admin.clear();
                    new preProcessDash_daywise().execute("2");
                }else{
                    Toast.makeText(NavigationActivity.this,"No Internet , Please Try After sometime",Toast.LENGTH_LONG).show();
                }
            }
        });

        days_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now.setBackgroundResource(R.drawable.button_bg);
                HRs_24.setBackgroundResource(R.drawable.button_bg);
                hrs_48.setBackgroundResource(R.drawable.button_bg);
                days_7.setBackgroundResource(R.drawable.button_selecte);
                day_15.setBackgroundResource(R.drawable.button_bg);
                day_30.setBackgroundResource(R.drawable.button_bg);
                day_90.setBackgroundResource(R.drawable.button_bg);
                day_180.setBackgroundResource(R.drawable.button_bg);
                etp_text.setText("ETPs Issued in 7 Days");
                if(isConnection()){
                    AndyUtils.showCustomProgressDialog(NavigationActivity.this,
                            "Fetching Information Please wait...", false, null);
                   // array_admin.clear();
                    new preProcessDash_daywise().execute("7");
                }else{
                    Toast.makeText(NavigationActivity.this,"No Internet , Please Try After sometime",Toast.LENGTH_LONG).show();
                }
            }
        });

        day_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now.setBackgroundResource(R.drawable.button_bg);
                HRs_24.setBackgroundResource(R.drawable.button_bg);
                hrs_48.setBackgroundResource(R.drawable.button_bg);
                days_7.setBackgroundResource(R.drawable.button_bg);
                day_15.setBackgroundResource(R.drawable.button_selecte);
                day_30.setBackgroundResource(R.drawable.button_bg);
                day_90.setBackgroundResource(R.drawable.button_bg);
                day_180.setBackgroundResource(R.drawable.button_bg);
                etp_text.setText("ETPs Issued in 15 Days");
                if(isConnection()){
                    AndyUtils.showCustomProgressDialog(NavigationActivity.this,
                            "Fetching Information Please wait...", false, null);
                   // array_admin.clear();
                    new preProcessDash_daywise().execute("15");
                }else{
                    Toast.makeText(NavigationActivity.this,"No Internet , Please Try After sometime",Toast.LENGTH_LONG).show();
                }
            }
        });

        day_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now.setBackgroundResource(R.drawable.button_bg);
                HRs_24.setBackgroundResource(R.drawable.button_bg);
                hrs_48.setBackgroundResource(R.drawable.button_bg);
                days_7.setBackgroundResource(R.drawable.button_bg);
                day_15.setBackgroundResource(R.drawable.button_bg);
                day_30.setBackgroundResource(R.drawable.button_selecte);
                day_90.setBackgroundResource(R.drawable.button_bg);
                day_180.setBackgroundResource(R.drawable.button_bg);
                etp_text.setText("ETPs Issued in 30 Days");
                if(isConnection()){
                    AndyUtils.showCustomProgressDialog(NavigationActivity.this,
                            "Fetching Information Please wait...", false, null);
                   // array_admin.clear();
                    new preProcessDash_daywise().execute("30");
                }else{
                    Toast.makeText(NavigationActivity.this,"No Internet , Please Try After sometime",Toast.LENGTH_LONG).show();
                }
            }
        });
        //simulator me chalate h

        day_90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now.setBackgroundResource(R.drawable.button_bg);
                HRs_24.setBackgroundResource(R.drawable.button_bg);
                hrs_48.setBackgroundResource(R.drawable.button_bg);
                days_7.setBackgroundResource(R.drawable.button_bg);
                day_15.setBackgroundResource(R.drawable.button_bg);
                day_30.setBackgroundResource(R.drawable.button_bg);
                day_90.setBackgroundResource(R.drawable.button_selecte);
                day_180.setBackgroundResource(R.drawable.button_bg);
                etp_text.setText("ETPs Issued in 90 Days");
                if(isConnection()){
                    AndyUtils.showCustomProgressDialog(NavigationActivity.this,
                            "Fetching Information Please wait...", false, null);
                    //array_admin.clear();
                    new preProcessDash_daywise().execute("90");
                }else{
                    Toast.makeText(NavigationActivity.this,"No Internet , Please Try After sometime",Toast.LENGTH_LONG).show();
                }
            }
        });

        day_180.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now.setBackgroundResource(R.drawable.button_bg);
                HRs_24.setBackgroundResource(R.drawable.button_bg);
                hrs_48.setBackgroundResource(R.drawable.button_bg);
                days_7.setBackgroundResource(R.drawable.button_bg);
                day_15.setBackgroundResource(R.drawable.button_bg);
                day_30.setBackgroundResource(R.drawable.button_bg);
                day_90.setBackgroundResource(R.drawable.button_bg);
                day_180.setBackgroundResource(R.drawable.button_selecte);
                etp_text.setText("ETPs Issued in 180 Days");
                if(isConnection()){
                    AndyUtils.showCustomProgressDialog(NavigationActivity.this,
                            "Fetching Information Please wait...", false, null);
                   // array_admin.clear();
                    new preProcessDash_daywise().execute("180");
                }else{
                    Toast.makeText(NavigationActivity.this,"No Internet , Please Try After sometime",Toast.LENGTH_LONG).show();
                }
               // Toast.makeText(NavigationActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }
        });

/*
        rating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NavigationActivity.this,"Under Development...",Toast.LENGTH_LONG);
            }
        });
*/


        lyn_active_vtus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnection()){
                    new getStaticDynamicCount().execute();
                }
            }
        });
    }


    class getStaticDynamicCount extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog = new ProgressDialog(NavigationActivity.this, R.style.mProgressBar);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isConnection()) {
                Toast.makeText(getApplicationContext(), getString(R.string.dialog_no_inter_message), Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = ProgressDialog.show(NavigationActivity.this, "", "Please Wait...", true);

        }

        @Override
        protected String doInBackground(String... params) {

            String getResponse = null;


            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet(UrlContants.STATIC_DYNAMIC);
                HttpResponse httpResponse = httpClient.execute(httppost);
                getResponse = EntityUtils.toString(httpResponse.getEntity());
            } catch (Exception ev) {
                ev.getStackTrace();
            }

            return getResponse;
        }

        @Override
        protected void onPostExecute(final String getResponse) {
            try{
                JSONArray jsonArray_   =  new JSONArray(getResponse);

                // for (int i = 0; i < jsonArray_.length(); i++) {

                JSONObject jsonObj = jsonArray_.getJSONObject(0);

                String dynamic = jsonObj.getString("sum");
                String etp_active = tot_vs_active.getText().toString();
                getDynamic(dynamic, totalActive, etp_active);

                //}
            }catch (Exception ev){
                System.out.print(ev.getMessage());
            }
            progressDialog.dismiss();
        }
    }
    public void getDynamic(String dynamic, String active, String etp_active){
        final Dialog myDialog = new Dialog(NavigationActivity.this);
        myDialog.getWindow();
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.dialogbox);

        TextView txt_title               = (TextView) myDialog.findViewById(R.id.txt_title);
        TextView txt_dash_totalvehicle       = (TextView)myDialog.findViewById(R.id.txt_dash_totalvehicle);
        TextView  txt_dash_idlevehicle        = (TextView)myDialog.findViewById(R.id.txt_dash_idlevehicle);
        TextView  txt_dash_runnvehicle        = (TextView)myDialog.findViewById(R.id.txt_dash_runnvehicle);
        TextView txt_dash_stopvehicle        = (TextView)myDialog.findViewById(R.id.txt_dash_stopvehicle);
        TextView txt_dash_nonpollvehicle     = (TextView)myDialog.findViewById(R.id.txt_dash_nonpollvehicle);
        TextView txtRefreshTimestamp         = (TextView)myDialog.findViewById(R.id.txtRefreshTimestamp);
        LinearLayout total_veh                   = (LinearLayout)myDialog.findViewById(R.id.total_veh);
        LinearLayout running_veh                   = (LinearLayout)myDialog.findViewById(R.id.running_veh);
        LinearLayout stop_veh                   = (LinearLayout)myDialog.findViewById(R.id.stop_veh);
        LinearLayout halt_veh                   = (LinearLayout)myDialog.findViewById(R.id.halt_veh);
        LinearLayout nonpolling_veh                   = (LinearLayout)myDialog.findViewById(R.id.nonpolling_veh);
        TextView ok_close = (TextView)myDialog.findViewById(R.id.ok_close);

        txt_dash_totalvehicle.setText(etp_active);
        txt_title.setText("ETPs/Active VTUs");

        if(Integer.parseInt ( dynamic )< 1){
            txt_dash_stopvehicle.setText("---");

        }else{
            txt_dash_stopvehicle.setText(dynamic);

        }

        int v_static =  Integer.parseInt(active) - Integer.parseInt(dynamic);

        if(v_static< 1){
            txt_dash_runnvehicle.setText("---");

        }else{
            txt_dash_runnvehicle.setText(""+v_static);

        }



        ok_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        /*
        total_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("All");
            }
        });
        running_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("running");
            }
        });
        halt_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("idle");
            }
        });
        stop_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("stop");
            }
        });
        nonpolling_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("nonpolling");
            }
        });*/

        /*txt_dash_totalvehicle.setText(totalVehicles);
        txt_dash_idlevehicle.setText(nonpollingVehicles);
        txt_dash_runnvehicle.setText(runningVehicles);
        txt_dash_stopvehicle.setText(stopVehicles);
        txt_dash_nonpollvehicle.setText(temperVehicles);*/


       /* Calendar c = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        txtRefreshTimestamp.setText(formattedDate);*/
        myDialog.show();
    }

    public void openPdf(Context context, String path){
        File file = new File(path);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            PackageManager pm = context.getPackageManager();
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setType("application/pdf");
            Intent openInChooser = Intent.createChooser(intent, "Choose");
            List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
            if (resInfo.size() > 0) {
                try {
                    context.startActivity(openInChooser);
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(context, "PDF apps are not installed", Toast.LENGTH_SHORT).show();
                    // PDF apps are not installed

                }
            } else {
                Toast.makeText(context, "PDF apps are not installed", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        myView.setVisibility(View.VISIBLE);
        line_tile.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Up", Toast.LENGTH_SHORT).show();
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
        myView.setVisibility(View.GONE);
        line_tile.setVisibility(View.GONE);
        Toast.makeText(this, "Down", Toast.LENGTH_SHORT).show();

    }

    class preProcessDash_daywise extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords,last_90_days_st,active_etp,inactive_etp;
        int total_count =0,total_i3ms=0,total_etp=0;
        int total_active=0;
        int total_inactive = 0;
        int total_i3ms_s=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;
            AndyUtils.removeCustomProgressDialog();




            try {
                HttpClient httpclient = new DefaultHttpClient();
                //HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getMobDashDataSpecific?dataRange="+args[0]);
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+UrlContants.GET_MOB_DASH_DATA_SPECIFIC+"?dataRange="+args[0]);
                // Add your data
                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;//strStatus;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //Toast.makeText(ActivityLogin.this,""+getResponse,Toast.LENGTH_LONG).show();
                    try {
                        JSONArray jsonArray_   =  new JSONArray(getResponse);
                        getData = new ArrayList<HashMap<String, String>>();
                        getData.clear();
                        if(jsonArray_.length()>0) {
                            try {
                                for (int i = 0; i < jsonArray_.length(); i++) {

                                    JSONObject jsonObj1 = jsonArray_.getJSONObject(i);
                                    Admin_model admin_bean = new Admin_model();


                                    String posi = String.valueOf(i + 1);
                                    admin_bean.setActive(jsonObj1.getString("active"));
                                    admin_bean.setInactive(jsonObj1.getString("inactive"));

                                    total_i3ms = total_i3ms + Integer.parseInt(jsonObj1.getString("i3msactive"));
                                    last_90_days_st = jsonObj1.getString("last90");
                                    total_etp = Integer.parseInt(jsonObj1.getString("okstatus")) + Integer.parseInt(jsonObj1.getString("notokstatus"));
                                    active_etp = jsonObj1.getString("okstatus");
                                    inactive_etp = jsonObj1.getString("notokstatus");


                                    total_count = total_count + Integer.parseInt(jsonObj1.getString("active")) + Integer.parseInt(jsonObj1.getString("inactive"));
                                    total_active = total_active + Integer.parseInt(jsonObj1.getString("active"));
                                    total_inactive = total_inactive + Integer.parseInt(jsonObj1.getString("inactive"));


                                    admin_bean.setCounts(posi);
                                    admin_bean.setTrip_count(jsonObj1.getString("tripcont"));

                                    admin_bean.setInstall_device(jsonObj1.getString("installeddevice"));
                                    admin_bean.setName(jsonObj1.getString("com_name"));
                                    admin_bean.setImage_url(imags[i]);

                                   // array_admin.add(admin_bean);

                                    HashMap<String, String> map = new HashMap<String, String>();

                                    map.put("KEY_CNAME", jsonObj1.getString("com_name"));
                                    map.put("KEY_TRIPCOUNT", jsonObj1.getString("tripcont"));
                                    map.put("KEY_ACTIVECOUNT", jsonObj1.getString("active"));
                                    map.put("KEY_INACTIVECOUNT", jsonObj1.getString("inactive"));
                                    map.put("KEY_INSTALLCOUNT", jsonObj1.getString("installeddevice"));
                                   // getData.add(map);
                                }
                                i3ms_redg.setText("" + total_i3ms);
                                etp_in_180.setText(last_90_days_st);
                                live_vtu.setText("" + total_etp);
                                active_trucks.setText("" + total_active);
                                operational_truck.setText(total_i3ms - Integer.parseInt(last_90_days_st) + "");

                                tot_vs_active.setText(active_etp + "/" + total_active);

                                T_ETP_vs_T_Active.setText(inactive_etp+"/"+(total_i3ms  - total_active)+ "");
                                inactive_trucks.setText(total_i3ms  - total_active+"");


                                System.out.println("--------------------------------------------------------------");
                                System.out.println("Total Count :" + total_count + " Total active :" + total_active);
                                //ter_con(total_count, total_active);

                                total_i3ms_s = total_i3ms;
                               /* mAdapter = new Myadapter();
                                recyclerView.setAdapter(mAdapter);*/

                            } catch (Exception ev) {
                                System.out.print(ev.getMessage());
                            }
                        }else{
                            Toast.makeText(NavigationActivity.this,"Date Out Of Range .....",Toast.LENGTH_LONG).show();

                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    progressDialog.dismiss();
                }

            });
        }

    }
    //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getMobDashDataSpecific?dataRange=2
    class preProcessDash extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords,last_90_days_st,active_etp,inactive_etp;
        int total_count =0,total_i3ms=0,total_etp=0;
        int total_active=0;
        int total_inactive = 0;
        int total_i3ms_s=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            now.setBackgroundResource(R.drawable.button_selecte);

            // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;
            AndyUtils.removeCustomProgressDialog();




            try {
                HttpClient httpclient = new DefaultHttpClient();
              //  HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getMobDashDataNew?dataRange=0");
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+UrlContants.GET_MOB_DASH_DATA+"?dataRange=0");

                // Add your data
                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;//strStatus;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //Toast.makeText(ActivityLogin.this,""+getResponse,Toast.LENGTH_LONG).show();
                    try {
                        JSONArray jsonArray_   =  new JSONArray(getResponse);
                        getData = new ArrayList<HashMap<String, String>>();
                        getData.clear();
                        try {
                            for (int i = 0; i < jsonArray_.length(); i++) {

                                JSONObject jsonObj1 = jsonArray_.getJSONObject(i);
                                Admin_model admin_bean = new Admin_model();


                                String posi = String.valueOf(i + 1);
                                admin_bean.setActive(jsonObj1.getString("active"));
                                admin_bean.setInactive(jsonObj1.getString("inactive"));

                                total_i3ms =total_i3ms+Integer.parseInt(jsonObj1.getString("i3msactive"));
                                last_90_days_st = jsonObj1.getString("last90");
                                total_etp = Integer.parseInt(jsonObj1.getString("okstatus")) + Integer.parseInt(jsonObj1.getString("notokstatus"));
                                active_etp =jsonObj1.getString("okstatus");
                                inactive_etp=jsonObj1.getString("notokstatus");


                                        total_count = total_count + Integer.parseInt(jsonObj1.getString("active")) + Integer.parseInt(jsonObj1.getString("inactive"));
                                total_active = total_active + Integer.parseInt(jsonObj1.getString("active"));
                                total_inactive = total_inactive + Integer.parseInt(jsonObj1.getString("inactive"));


                                        admin_bean.setCounts(posi);
                                admin_bean.setTrip_count(jsonObj1.getString("tripcont"));

                                admin_bean.setInstall_device(jsonObj1.getString("installeddevice"));
                                admin_bean.setName(jsonObj1.getString("com_name"));
                                admin_bean.setImage_url(imags[i]);
                                if(jsonObj1.isNull("i3msinactive")) {
                                    admin_bean.setBg_validity("0");
                                } else {
                                    admin_bean.setBg_validity(jsonObj1.getString("i3msinactive"));
                                }


                                array_admin.add(admin_bean);

                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put("KEY_CNAME", jsonObj1.getString("com_name"));
                                map.put("KEY_TRIPCOUNT", jsonObj1.getString("tripcont"));
                                map.put("KEY_ACTIVECOUNT", jsonObj1.getString("active"));
                                map.put("KEY_INACTIVECOUNT", jsonObj1.getString("inactive"));
                                map.put("KEY_INSTALLCOUNT", jsonObj1.getString("installeddevice"));
                                getData.add(map);
                            }
                            totalActive = String.valueOf(active_etp);
                            i3ms_redg.setText(""+total_i3ms);
                            etp_in_180.setText(last_90_days_st);
                            live_vtu.setText(""+total_etp);
                            active_trucks.setText(""+total_active);
                            operational_truck.setText(total_i3ms - Integer.parseInt(last_90_days_st)+"");
                            tot_vs_active.setText(active_etp+"/"+total_active);

                            T_ETP_vs_T_Active.setText(inactive_etp+"/"+(total_i3ms  - total_active)+ "");
                            inactive_trucks.setText(total_i3ms  - total_active+"");





                            System.out.println("--------------------------------------------------------------");
                            System.out.println("Total Count :" +total_count+" Total active :"+total_active);
                            //ter_con(total_count, total_active);

                            total_i3ms_s =total_i3ms;
                            mAdapter = new Myadapter();
                            recyclerView.setAdapter(mAdapter);
                           // new getAllVendorDetail().execute();

                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }
/*
    class getAllVendorDetail extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords,last_90_days_st,active_etp,inactive_etp;
        int total_count =0,total_i3ms=0,total_etp=0;
        int total_active=0;
        int total_inactive = 0;
        int total_i3ms_s=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;
            AndyUtils.removeCustomProgressDialog();




            try {
                HttpClient httpclient = new DefaultHttpClient();
                //HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getMobDashDataSpecific?dataRange="+args[0]);
                HttpGet httppost = new HttpGet(UrlContants.BASE_URL_OMVTS+UrlContants.GET_BG_DETAIL+"?loginid=all");
                // Add your data
                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;//strStatus;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //Toast.makeText(ActivityLogin.this,""+getResponse,Toast.LENGTH_LONG).show();
                    try {
                        JSONArray jsonArray_   =  new JSONArray(getResponse);

                        if(jsonArray_.length()>0) {
                            try {
                                for (int i = 0; i < jsonArray_.length(); i++) {

                                    JSONObject jsonObj1 = jsonArray_.getJSONObject(i);
                                    Pojo_Device_Management admin_bean_ = new Pojo_Device_Management();

                                    admin_bean_.setCom_name(jsonObj1.getString("compName"));
                                    admin_bean_.setBg_amont(jsonObj1.getString("bgamount"));

                                    admin_bean_.setBg_device(jsonObj1.getString("vehicleallowed"));
                                    admin_bean_.setInstall_device(jsonObj1.getString("installedCnt"));
                                    admin_bean_.setLoginid(jsonObj1.getString("loginid"));
                                    admin_bean_.setBgStatus(jsonObj1.getString("bgstatus"));

                                    array_admin_.add(admin_bean_);
                                }

                                mAdapter = new Myadapter();
                                recyclerView.setAdapter(mAdapter);

                            } catch (Exception ev) {
                                System.out.print(ev.getMessage());
                            }
                        }else{
                            Toast.makeText(NavigationActivity.this,"Date Out Of Range .....",Toast.LENGTH_LONG).show();

                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    progressDialog.dismiss();
                }

            });
        }

    }
*/




    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            //new UpdateAdmin().execute();
            mHandler.postDelayed(mHandlerTask, INTERVAL);
           // Toast.makeText(NavigationActivity.this,"Refreshing",Toast.LENGTH_LONG).show();
        }
    };
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
       // Toast.makeText(NavigationActivity.this,"stop updating",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRepeatingTask();
       // Toast.makeText(NavigationActivity.this,"stop updating",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRepeatingTask();
      //  Toast.makeText(NavigationActivity.this,"start updating",Toast.LENGTH_LONG).show();
    }

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mHandlerTask);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        drawer.openDrawer(GravityCompat.START);
        mNavigationDrawerFragment.navigationItemSelection(position, NavigationActivity.this);

    }


    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
       // Admin_model adminModel;bg_status
        @Override
        public Myadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(Myadapter.ViewHolder holder, int position) {
            final Admin_model adminModel = array_admin.get(position);
//            final Pojo_Device_Management adminModel_ = array_admin_.get(position);

            holder.avai_devce.setText(adminModel.getInstall_device());
            holder.trip_value.setText(Integer.parseInt(adminModel.getInstall_device())/Integer.parseInt(adminModel.getInstall_device())*100+"");
            holder.item_title.setText(adminModel.getName());

            holder.ins_device.setText( adminModel.getActive()+"/"+adminModel.getInactive());
          String count_active_inactive = adminModel.getActive()+"/"+adminModel.getInactive();
          int aa__ = count_active_inactive.length ();
          //  Toast.makeText ( NavigationActivity.this , aa__+"" , Toast.LENGTH_SHORT ).show ( );
          if( count_active_inactive.length () > 10)
          {
              holder.ins_device.setTextSize ( 20 );
          }
            holder.num_head.setText(adminModel.getCounts());
            holder.tot_device.setText(/*"Total Devices = "+*/adminModel.getTot_device());
            holder.bg_amount.setText(adminModel.getBg_validity());
            try {
                if(Integer.parseInt(adminModel.getInstall_device())< Integer.parseInt(adminModel.getBg_validity())){
                    holder.bg_status.setText("BG Status -  Valid " );
                    holder.bg_status.setTextColor(Color.parseColor("#2E816A"));

                }else {
                    holder.bg_status.setText("BG Status -  Invalid " );
                    holder.bg_status.setTextColor(Color.parseColor("#BF1A21"));

                }

            }catch (Exception e){
                e.getMessage();
            }

            //holder.trip_value.setText(adminModel.getTrip_count());



            holder.layout_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NavigationActivity.this, Orsac_Admin_sec_Trip_count.class);

                    intent.putExtra("key_com_name",adminModel.getName());
                    intent.putExtra("key_active",adminModel.getActive());
                    intent.putExtra("key_inactive",adminModel.getInactive());
                    intent.putExtra("key_trip",adminModel.getTrip_count());
                    intent.putExtra("key_insta",adminModel.getInstall_device());
                    startActivity(intent);
                }
            });
            if(adminModel.getName().equalsIgnoreCase("eTrans Solutions Private Limited")){
                holder.item_icon.setImageResource(imags[2]);
            }else if(adminModel.getName().equalsIgnoreCase("Fastrackerz")){
                holder.item_icon.setImageResource(imags[3]);
            }else if(adminModel.getName().equalsIgnoreCase("iTriangle Infotech Pvt. Ltd")){
                holder.item_icon.setImageResource(imags[4]);
            }else if(adminModel.getName().equalsIgnoreCase("Orduino Labs")){
                holder.item_icon.setImageResource(imags[6]);
            }else if(adminModel.getName().equalsIgnoreCase("Arya Omnitalk Wireless Solution Pvt Ltd.")){
                holder.item_icon.setImageResource(imags[0]);
            }else if(adminModel.getName().equalsIgnoreCase("Atlanta")){
                holder.item_icon.setImageResource(imags[5]);
            }else if(adminModel.getName().equalsIgnoreCase("CE Info Systems Pvt. Ltd.")){
                holder.item_icon.setImageResource(imags[1]);
            }else if(adminModel.getName().equalsIgnoreCase("Rosmerta Autotech Private Ltd.")){
                holder.item_icon.setImageResource(imags[7]);
            }else if(adminModel.getName().equalsIgnoreCase("Trimble Mobility Solutions India Pvt. Ltd.")){
                holder.item_icon.setImageResource(imags[8]);
            }
            if(adminModel.getName().equalsIgnoreCase("eTrans Solutions Private Limited")){
                holder.item_icon.setImageResource(imags[2]);
            }else if(adminModel.getName().equalsIgnoreCase("Fastrackerz")){
                holder.item_icon.setImageResource(imags[3]);
            }else if(adminModel.getName().equalsIgnoreCase("iTriangle Infotech Pvt. Ltd")){
                holder.item_icon.setImageResource(imags[4]);
            }else if(adminModel.getName().equalsIgnoreCase("Orduino Labs")){
                holder.item_icon.setImageResource(imags[6]);
            }else if(adminModel.getName().equalsIgnoreCase("Arya Omnitalk Wireless Solution Pvt Ltd.")){
                holder.item_icon.setImageResource(imags[0]);
            }else if(adminModel.getName().equalsIgnoreCase("Atlanta")){
                holder.item_icon.setImageResource(imags[5]);
            }else if(adminModel.getName().equalsIgnoreCase("CE Info Systems Pvt. Ltd.")){
                holder.item_icon.setImageResource(imags[1]);
            }else if(adminModel.getName().equalsIgnoreCase("Rosmerta Autotech Private Ltd.")){
                holder.item_icon.setImageResource(imags[7]);
            }else if(adminModel.getName().equalsIgnoreCase("Trimble Mobility Solutions India Pvt. Ltd.")){
                holder.item_icon.setImageResource(imags[8]);
            }

        }
       // int[] imags = {R.drawable.arya_three, R.drawable.ca_sysy,R.drawable. etrans_two,R.drawable.fasttra_two,R.drawable.itrangle,R.drawable.atlanata_three,R.drawable.ordinio,R.drawable.rose_six, R.drawable.trimmble_three};

        @Override
        public int getItemCount() {
           // AndyUtils.removeCustomProgressDialog();

            return array_admin.size();



        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView item_title;
            protected ImageView item_icon;
            protected TextView num_head;
            protected TextView tot_device;
            protected TextView avai_devce;
            protected TextView ins_device;
            protected TextView trip_value;
            protected  TextView bg_amount;
            protected  TextView bg_status;
            protected LinearLayout layout_one;


            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                num_head = (TextView) itemLayputView.findViewById(R.id.num_head);
                item_title = (TextView) itemLayputView.findViewById(R.id.item_title);
                item_icon = (ImageView) itemLayputView.findViewById(R.id.item_icon);
                tot_device = (TextView) itemLayputView.findViewById(R.id.Total_Devices);
                avai_devce = (TextView) itemLayputView.findViewById(R.id.title_avai);
                ins_device = (TextView) itemLayputView.findViewById(R.id.title_use);
                layout_one = (LinearLayout) itemLayputView.findViewById(R.id.layout_one);
                trip_value = (TextView) itemLayputView.findViewById(R.id.trip_value);
                bg_amount =(TextView) itemLayputView.findViewById(R.id.bg_amount);
                bg_status =(TextView) itemLayputView.findViewById(R.id.bg_status);
            }
        }
    }


    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) NavigationActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }
    class etp_detail extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;


            try {
                HttpClient httpclient = new DefaultHttpClient();
                //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getlivevehicles?vehicleno=49179
              //  HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/etptriplivesKunal?circle=All&company=All&vehicleno=All&query=NA");
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+UrlContants.ETP_MOB_DASH_DATA_SPECIFIC+"?circle=All&company=All&vehicleno=All&query=NA");

                // Add your data
                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;//strStatus;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(getResponse);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                       // JSONObject jsonObject = new JSONObject(getResponse);
                      //  int total_etp = Integer.parseInt(jsonObject.getString("notOkStatus"))+ Integer.parseInt(jsonObject.getString("okStatus"));
                      //  tot_vs_active.setText(jsonObject.getString("okStatus")+"/"+ total_active);

                       // live_etp.setText(jsonObject.getString("total"));
                        new Last_90_days().execute();
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                }
            });
        }
    }

    /*class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;
        int total_count =0,total_i3ms=0;
int total_active=0;
    int total_inactive = 0;
    int total_i3ms_s=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;
            AndyUtils.removeCustomProgressDialog();




            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsactest/orsacwebservice/rest/CallService/companywisedevicedetails");

                // Add your data
                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;//strStatus;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //Toast.makeText(ActivityLogin.this,""+getResponse,Toast.LENGTH_LONG).show();
                    try {
                        JSONArray jsonArray_   =  new JSONArray(getResponse);
                        getData = new ArrayList<HashMap<String, String>>();
                        getData.clear();
                        try {
                            for (int i = 0; i < jsonArray_.length(); i++) {

                                JSONObject jsonObj1 = jsonArray_.getJSONObject(i);
                                Admin_model admin_bean = new Admin_model();


                                String posi = String.valueOf(i + 1);
                                admin_bean.setActive(jsonObj1.getString("active"));
                                admin_bean.setInactive(jsonObj1.getString("inactive"));

                                total_i3ms =total_i3ms+Integer.parseInt(jsonObj1.getString("i3msactive"));
                                total_count = total_count + Integer.parseInt(jsonObj1.getString("active")) + Integer.parseInt(jsonObj1.getString("inactive"));
                                total_active = total_active + Integer.parseInt(jsonObj1.getString("active"));
                                total_inactive = total_inactive + Integer.parseInt(jsonObj1.getString("inactive"));

                                admin_bean.setCounts(posi);
                               // admin_bean.setTrip_count(jsonObj1.getString("tripcount"));

                                admin_bean.setInstall_device(jsonObj1.getString("installedDevices"));
                                admin_bean.setName(jsonObj1.getString("companyName"));
                                admin_bean.setImage_url(imags[i]);

                                array_admin.add(admin_bean);

                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put("KEY_CNAME", jsonObj1.getString("companyName"));
                                //map.put("KEY_TRIPCOUNT", jsonObj1.getString("tripcount"));
                                map.put("KEY_ACTIVECOUNT", jsonObj1.getString("active"));
                                map.put("KEY_INACTIVECOUNT", jsonObj1.getString("inactive"));
                                map.put("KEY_INSTALLCOUNT", jsonObj1.getString("installedDevices"));
                                getData.add(map);
                            }

                            System.out.println("--------------------------------------------------------------");
                            System.out.println("Total Count :" +total_count+" Total active :"+total_active);
                            //ter_con(total_count, total_active);
                         //   sea_tot_ac.setText(""+total_active +"/"+total_inactive);
                            //sea_tot_insa.setText(""+total_i3ms);
                            i3ms_redg.setText(""+total_i3ms);
                            total_i3ms_s =total_i3ms;
                            mAdapter = new Myadapter();
                            recyclerView.setAdapter(mAdapter);
                           // new etp_detail().execute();

                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }
*/
/*PDF CREATOR*/
private static void addMetaData(Document document) {
    document.addTitle("VendorDetail PDF");
    document.addSubject("ORSAC Vendor Detail");
    // document.addKeywords("Java, PDF, iText");
    document.addAuthor("ORSAC");
    document.addCreator("ORSAC");
    document.addHeader("ORSAC","Date : "+ new Date());

}

    private static void addTitlePage(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("ORSAC Vendor Details", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                "Report generated by: " + System.getProperty("user.name") + ", " + new Date(),
                smallBold));

        /*addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "This document describes something which is very important ",
                smallBold));*/

       /* addEmptyLine(preface, 8);

        preface.add(new Paragraph(
                "This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).",
                redFont));*/

        document.add(preface);
        // Start a new page
        //document.newPage();
    }

    private static void addContent(Document document , ArrayList<HashMap<String, String>> myDataset) throws DocumentException {
        Anchor anchor = new Anchor("ORSAC Vendor Detail", catFont);
        anchor.setName("ORSAC Vendor Detail");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        //Paragraph subPara = new Paragraph("", subFont);
        Paragraph subPara = new Paragraph();
        Section subCatPart = catPart.addSection(subPara);
        //subCatPart.add(new Paragraph("Hello"));

        // subPara = new Paragraph("Subcategory 2", subFont);
        // subCatPart = catPart.addSection(subPara);
        //subCatPart.add(new Paragraph("Paragraph 1"));
        //subCatPart.add(new Paragraph("Paragraph 2"));
        // subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        // createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 3);
        subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart, myDataset);

        // now add all this to the document
        document.add(catPart);
        document.close();

        /*
        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));
*/
        // now add all this to the document
        //document.add(catPart);

    }

    private static void createTable(Section subCatPart, ArrayList<HashMap<String, String>> myDataset) throws BadElementException {
        PdfPTable table = new PdfPTable(5);

        //t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Company Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

      /*  c1 = new PdfPCell(new Phrase("Date"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);*/

        c1 = new PdfPCell(new Phrase("Total Install"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total Active"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total inactive"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Trip Count"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        HashMap<String,String> map = new HashMap<>();
        for (int i=0; i<myDataset.size(); i++){
            map = myDataset.get(i);
            table.addCell(map.get("KEY_CNAME"));
            table.addCell(map.get("KEY_INSTALLCOUNT"));
            table.addCell(map.get("KEY_ACTIVECOUNT"));
            table.addCell(map.get("KEY_INACTIVECOUNT"));
            table.addCell(map.get("KEY_TRIPCOUNT"));



        }
        subCatPart.add(table);

    }



    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
/*End PDF CREATORE*/
    public void ter_con(int totalCount, int totactive){

        new AlertDialog.Builder(NavigationActivity.this)
                .setTitle("VTU device")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // finish();
                        dialog.dismiss();
                    }
                })

                .setMessage("Total VTU installed : "+totalCount+"\n\nTotal active : "+totactive)
                .setCancelable(false)
                .show();
        // }
    }

    /*class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AndyUtils.showCustomProgressDialog(NavigationActivity.this,
                    "Fetching Information Please wait...", false, null);
         *//*   progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();*//*
            // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;

            *//*PreferenceHelper preferenceHelper = new PreferenceHelper(ActivityLogin.this);
            try{
                getDeviceToken = preferenceHelper.getGcm_id();
            }catch (Exception ev){
                System.out.print(ev.getMessage());
                getDeviceToken = "";
            }*//*

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("userid", "10001"));
            //nameValuePairs.add(new BasicNameValuePair("device_user", "android"));
            //nameValuePairs.add(new BasicNameValuePair("device_token", getDeviceToken));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(UrlContants.DASHBORAD);

                // Add your data
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;//strStatus;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //Toast.makeText(ActivityLogin.this,""+getResponse,Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObj = new JSONObject(getResponse);
                        HashMap<String, String> map = new HashMap<>();
                        try {
                            // getStatus = Boolean.parseBoolean(jsonObj.getString("Success"));

                            tot_com.setText(jsonObj.getString("totalcompanies"));
                            tot_trip.setText(jsonObj.getString("totaltripcountcnt"));
                            tot_dev.setText(jsonObj.getString("totaldevices"));
                            veh_trac.setText(jsonObj.getString("totalinstalled"));
                            tot_device= Integer.parseInt(jsonObj.getString("totalinstalled"));
                          tot_install=Integer.parseInt(jsonObj.getString("totaldevices"));
                            JSONArray jsonArray = jsonObj.getJSONArray("companydetails");
                           value_avai = new float[jsonArray.length()];
                            value_comap= new String[jsonArray.length()];
                            value_inst = new float[jsonArray.length()];
                            tripCount = new double[jsonArray.length()];
                            compy_name = new String[jsonArray.length()];
                       // value_inst = new Float[jsonArray.length()];
                            GetCompanyDetail = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObj1 = jsonArray.getJSONObject(i);
                                Admin_model admin_bean = new Admin_model();
                                String posi = String.valueOf(i + 1);
                                admin_bean.setInstall_device(jsonObj1.getString("installedDevices"));
                                admin_bean.setName(jsonObj1.getString("companyName"));
                                admin_bean.setImag_url_web(jsonObj1.getString("image"));
                                admin_bean.setImage_url(imags[i]);
                                admin_bean.setTot_device(jsonObj1.getString("totalDevices"));
                                admin_bean.setAvail_device(jsonObj1.getString("availabledevices"));
                                admin_bean.setTrip_count(jsonObj1.getString("tripcount"));
                                value_avai[i] = Float.valueOf(jsonObj1.getString("availabledevices"));
                                value_inst[i] =Float.valueOf(jsonObj1.getString("installedDevices"));
                                value_comap[i] = jsonObj1.getString("companyName");

                                admin_bean.setCounts(posi);
                                companyList.add(admin_bean);
                                array_admin.add(admin_bean);
                            *//*    map.put("KEY_installedDevices", jsonObj1.getString("installedDevices"));
                                map.put("KEY_companyName", jsonObj1.getString("companyName"));
                                map.put("KEY_totalDevices", jsonObj1.getString("totalDevices"));
                                map.put("KEY_availabledevices", jsonObj1.getString("availabledevices"));
                               map.put("KEY_tripcount", jsonObj1.getString("tripcount"));*//*
                                installedDevices = jsonObj1.getString("installedDevices");
                                companyName = jsonObj1.getString("companyName");
                                totalDevices = jsonObj1.getString("totalDevices");
                                availabledevices = jsonObj1.getString("availabledevices");
                                tripcount = jsonObj1.getString("tripcount");
                                tripCount[i] = Double.valueOf(tripcount);
                                compy_name[i] = companyName;
                                GetCompanyDetail.add(map);
                            }
                            *//*SwipeCardAdapter_5_may swipeCardAdapter = new SwipeCardAdapter_5_may(NavigationActivity.this,array_admin,NavigationActivity.this);
                            LinearLayoutManager layoutManager
                                    = new LinearLayoutManager(NavigationActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(swipeCardAdapter);
*//*
                            mAdapter = new Myadapter();
                            recyclerView.setAdapter(mAdapter);

                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }

*//*
                        if (getStatus == true) {

                            String getMessage = jsonObj.getString("Message");
                            JSONArray arrData = jsonObj.getJSONArray("VehicleDetails");
                            JSONObject jsonData = arrData.getJSONObject(0);

                            String getUserId = jsonData.getString("userid");


                            //preferenceHelper.putUser_id(getUserId);


                            Toast.makeText(AdminPage.this, "" + getMessage, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AdminPage.this, AdminPage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AdminPage.this, "" + jsonObj.getString("Message"), Toast.LENGTH_LONG).show();
                        }
*//*
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                   // progressDialog.dismiss();
                }
            });
        }

    }
    */


    /*Testing Asyn same work like Login_info method */
    class UpdateAdmin extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*AndyUtils.showCustomProgressDialog(NavigationActivity.this,
                    "Fetching Information Please wait...", false, null);
        */ /*   progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();*/
            // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;

            /*PreferenceHelper preferenceHelper = new PreferenceHelper(ActivityLogin.this);
            try{
                getDeviceToken = preferenceHelper.getGcm_id();
            }catch (Exception ev){
                System.out.print(ev.getMessage());
                getDeviceToken = "";
            }*/

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("userid", "10001"));
            //nameValuePairs.add(new BasicNameValuePair("device_user", "android"));
            //nameValuePairs.add(new BasicNameValuePair("device_token", getDeviceToken));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(UrlContants.DASHBORAD);

                // Add your data
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;//strStatus;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //Toast.makeText(ActivityLogin.this,""+getResponse,Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObj = new JSONObject(getResponse);
                        HashMap<String, String> map = new HashMap<>();
                        array_admin.clear();
                        try {
                            // getStatus = Boolean.parseBoolean(jsonObj.getString("Success"));

                            tot_com.setText(jsonObj.getString("totalcompanies"));
                            tot_trip.setText(jsonObj.getString("totaltripcountcnt"));
                            tot_dev.setText(jsonObj.getString("totaldevices"));
                            veh_trac.setText(jsonObj.getString("totalinstalled"));
                            tot_device = Integer.parseInt(jsonObj.getString("totalinstalled"));
                            tot_install = Integer.parseInt(jsonObj.getString("totaldevices"));
                            JSONArray jsonArray = jsonObj.getJSONArray("companydetails");
                            value_avai = new float[jsonArray.length()];
                            value_comap = new String[jsonArray.length()];
                            value_inst = new float[jsonArray.length()];
                            tripCount = new double[jsonArray.length()];
                            compy_name = new String[jsonArray.length()];
                            // value_inst = new Float[jsonArray.length()];
                            int contss = jsonArray.length();
                            GetCompanyDetail = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObj1 = jsonArray.getJSONObject(i);
                                Admin_model admin_bean = new Admin_model();
                                String posi = String.valueOf(i + 1);
                                admin_bean.setInstall_device(jsonObj1.getString("installedDevices"));
                                admin_bean.setName(jsonObj1.getString("companyName"));
                                //   admin_bean.setImage_url(jsonObj1.getString("image"));
                                admin_bean.setImag_url_web(jsonObj1.getString("image"));
                                admin_bean.setImage_url(imags[i]);
                                admin_bean.setTot_device(jsonObj1.getString("totalDevices"));
                                admin_bean.setAvail_device(jsonObj1.getString("availabledevices"));
                                admin_bean.setTrip_count(jsonObj1.getString("tripcount"));
                                value_avai[i] = Float.valueOf(jsonObj1.getString("availabledevices"));
                                value_inst[i] = Float.valueOf(jsonObj1.getString("installedDevices"));

                                value_comap[i] = jsonObj1.getString("companyName");

                                admin_bean.setCounts(posi);
                                companyList.add(admin_bean);
                                array_admin.add(admin_bean);
                            /*    map.put("KEY_installedDevices", jsonObj1.getString("installedDevices"));
                                map.put("KEY_companyName", jsonObj1.getString("companyName"));
                                map.put("KEY_totalDevices", jsonObj1.getString("totalDevices"));
                                map.put("KEY_availabledevices", jsonObj1.getString("availabledevices"));

                                map.put("KEY_tripcount", jsonObj1.getString("tripcount"));*/
                                installedDevices = jsonObj1.getString("installedDevices");
                                companyName = jsonObj1.getString("companyName");
                                totalDevices = jsonObj1.getString("totalDevices");
                                availabledevices = jsonObj1.getString("availabledevices");
                                tripcount = jsonObj1.getString("tripcount");
                                tripCount[i] = Double.valueOf(tripcount);
                                compy_name[i] = companyName;
                                GetCompanyDetail.add(map);
                            }
/*
                            SwipeCardAdapter_5_may swipeCardAdapter = new SwipeCardAdapter_5_may(NavigationActivity.this,array_admin,NavigationActivity.this);
                            LinearLayoutManager layoutManager
                                    = new LinearLayoutManager(NavigationActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(swipeCardAdapter);*/
//                            mAdapter = new Myadapter();
//                            recyclerView.setAdapter(mAdapter);


                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }

/*
                        if (getStatus == true) {

                            String getMessage = jsonObj.getString("Message");
                            JSONArray arrData = jsonObj.getJSONArray("VehicleDetails");
                            JSONObject jsonData = arrData.getJSONObject(0);

                            String getUserId = jsonData.getString("userid");


                            //preferenceHelper.putUser_id(getUserId);


                            Toast.makeText(AdminPage.this, "" + getMessage, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AdminPage.this, AdminPage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AdminPage.this, "" + jsonObj.getString("Message"), Toast.LENGTH_LONG).show();
                        }
*/
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    // AndyUtils.removeCustomProgressDialog();
                    // progressDialog.dismiss();
                }
            });
        }
    }
    class Last_90_days extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;
        int total_active=0;
        int total_inactive = 0;
        int total_i3ms_s=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;


            try {
                HttpClient httpclient = new DefaultHttpClient();
                //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getlivevehicles?vehicleno=49179
             //   HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getMobDashDataNew?dataRange=0");
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+UrlContants.GET_MOB_DASH_DATA+"?dataRange=0");

                // Add your data
                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;//strStatus;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(getResponse);
                        if(jsonArray.length() > 0) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            live_vtu.setText(total_i3ms_s  -Integer.parseInt(jsonObject.getString("last90"))+"");

                            Toast.makeText(NavigationActivity.this,total_i3ms_s  -Integer.parseInt(jsonObject.getString("last90"))+"",Toast.LENGTH_LONG).show();
                            AndyUtils.removeCustomProgressDialog();
                        }else {
                            Toast.makeText(NavigationActivity.this, "Invalid History Data ..", Toast.LENGTH_LONG).show();
                            AndyUtils.removeCustomProgressDialog();
                        }

                    } catch (Exception ev) {
                        AndyUtils.removeCustomProgressDialog();
                        System.out.print(ev.getMessage());
                    }
                }
            });
        }
    }


    class day_wise extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;
        int total_active=0;
        int total_inactive = 0;
        int total_i3ms_s=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;


            try {
                HttpClient httpclient = new DefaultHttpClient();
                //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getlivevehicles?vehicleno=49179
               // HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getMobDashData?dataRange="+args[0]);
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+UrlContants.GET_MOB_DASH_DATA_NAVI+"?dataRange="+args[0]);
                // Add your data
                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;//strStatus;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(getResponse);
                        if(jsonArray.length() > 0) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            etp_in_180.setText(jsonObject.getString("last90"));
                            live_vtu.setText(total_i3ms_s  -Integer.parseInt(jsonObject.getString("last90"))+"");
                            T_ETP_vs_T_Active.setText( (total_i3ms_s  - Integer.parseInt(jsonObject.getString("last90"))) -  total_active+"");

                            Toast.makeText(NavigationActivity.this,total_i3ms_s  -Integer.parseInt(jsonObject.getString("last90"))+"",Toast.LENGTH_LONG).show();
                            AndyUtils.removeCustomProgressDialog();
                        }else {
                            Toast.makeText(NavigationActivity.this, "Invalid History Data ..", Toast.LENGTH_LONG).show();
                            AndyUtils.removeCustomProgressDialog();
                        }

                    } catch (Exception ev) {
                    AndyUtils.removeCustomProgressDialog();
                        System.out.print(ev.getMessage());
                    }
                }
            });
        }
    }

    private void prettyDialog_report(){
        final PrettyDialog dialog = new PrettyDialog(this);
        dialog
                .setTitle("Select Report Option")
                .setMessage("")
                .setIcon(R.drawable.pdlg_icon_info,R.color.pdlg_color_blue,null)
                .addButton("Daily Distance", R.color.pdlg_color_white, R.color.pdlg_color_green, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {

                       /* Intent intent = new Intent(TestActivity.this, ReportDailyDistanceActivity.class);
                        startActivity(intent);*/
                        dialog.dismiss();
                        //Toast.makeText(Language_Setting.this,"OK selected",Toast.LENGTH_SHORT).show();
                        return;


                    }
                })
                .addButton("Over Speed", R.color.pdlg_color_white, R.color.pdlg_color_red, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {

                      /*  Intent intent = new Intent(TestActivity.this, ReportOverspeedActivity.class);
                        startActivity(intent);*/
                        dialog.dismiss();
                        //Toast.makeText(Language_Setting.this,"OK selected",Toast.LENGTH_SHORT).show();
                        return;


                    }
                })
                .addButton("Idle Report", R.color.pdlg_color_white, R.color.pdlg_color_blue, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {

                      /*  Intent intent = new Intent(TestActivity.this, KK_Idle_Search_Activity.class);
                        startActivity(intent);*/
                        dialog.dismiss();
                        //Toast.makeText(Language_Setting.this,"OK selected",Toast.LENGTH_SHORT).show();
                        return;


                    }
                })
                .addButton("Vehicle History", R.color.pdlg_color_white, R.color.pdlg_color_red, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                      /*  Intent intent = new Intent(TestActivity.this, KK_HistoryActivity.class);
                        startActivity(intent);*/

                        dialog.dismiss();
                        //Toast.makeText(Language_Setting.this,"Cancel selected",Toast.LENGTH_SHORT).show();
                        return;
                    }

                });

        dialog.show();
    }




}
