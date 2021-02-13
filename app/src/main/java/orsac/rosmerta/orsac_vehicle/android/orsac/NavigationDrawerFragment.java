package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import orsac.rosmerta.orsac_vehicle.android.LoginActivity;
import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {
    List<String> navigationItems = new ArrayList<String>() {{
        //  add("Home");  //0
        add("Circle Dashboard");   //0
        add("Live Status"); //1
        add("About Us");  //2
        add("Terms & Conditions");  //3
        add("Rate Us");  //4
        add("Feedback");  //5
        //add("Share");  //6
        add("Near By Mines");
        add("Near By Me");
        add("Log Out");  //7

    }};

    List<Integer> navigationIcons = new ArrayList<Integer>() {{
        //add(R.drawable.ic_action_home); //0
        add(R.drawable.ic_circle); //1
        add(R.drawable.ic_live_news_report); //2
        add(R.drawable.ic_about_us); //3
        add(R.drawable.ic_air_conditioner); //4
        add(R.drawable.ic_mark_as_favorite_star); //5
        add(R.drawable.ic_feedback); //6
        //add(R.drawable.ic_action_contact_us); //7
        add(R.drawable.ic_miner);
        add(R.drawable.ic_searc_vehicle);
        add(R.drawable.ic_logout); //8

    }};

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;
    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private LinearLayout fragmentLayout;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);

        DrawerAdapter adapter = new DrawerAdapter(getActivity(), navigationItems, navigationIcons);
        mDrawerListView = (ListView) fragmentLayout.findViewById(R.id.sideMenu);
        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectItem(position);
                // mDrawerLayout.setBackgroundResource(R.color.bill_back_color);

            }
        });

        return fragmentLayout;
    }

    /*
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (position % 2 == 1) {
                view.setBackgroundColor(Color.BLUE);
            } else {
                view.setBackgroundColor(Color.CYAN);
            }

            return view;
        }
    */
    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mDrawerListView = (ListView) fragmentLayout.findViewById(R.id.sideMenu);
        SharedPreferences userDetails;
        DrawerAdapter adapter = new DrawerAdapter(getActivity(), navigationItems, navigationIcons);
        mDrawerListView.setAdapter(adapter);

        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        /*if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }*/

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            //inflater.inflate(R.menu.menu_main, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    public List<String> getNavigationItems() {
        return navigationItems;
    }

    public void setNavigationItems(List<String> navigationItems) {
        this.navigationItems = navigationItems;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void navigationItemSelection(int position, Activity activity) {
        SharedPreferences userDetails;
        if (position == 0) {  //Profile
          /*  Intent inty = new Intent(getActivity(), Demo_CircleActivity.class);
            startActivity(inty);*/
  Intent inty = new Intent(getActivity(), ViewPagerActivity.class);
            startActivity(inty);

        } else if (position == 1) {
            Intent intent = new Intent(getActivity(), LivestatusActivity.class);
            startActivity(intent);
            //finish();

        } else if (position == 2) {
            Intent inty = new Intent(getActivity(), Orsac_About_us.class);
            startActivity(inty);
        } else if (position == 3) {
            Intent intent = new Intent(getActivity(), TermsAndConditionActivity_Orsac.class);
            startActivity(intent);
            //finish();
        } else if (position == 4) {
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store"));
            startActivity(rateIntent);
           // getActivity().finish();

        } else if (position == 5) {
            Intent inty = new Intent(getActivity(), Orsac_feedback.class);
            startActivity(inty);
        } else if (position == 6) {
          //  Toast.makeText(getActivity(), "This is nearby button  ", Toast.LENGTH_SHORT).show();
            Intent inty = new Intent(getActivity(), NearByMines.class);
            startActivity(inty);
        } else if (position == 7) {

            Intent inty = new Intent(getActivity(), NearByMeVehicle.class);
            startActivity(inty);
        } else if (position == 7) {
            getActivity().finish();
            Intent inty = new Intent(getActivity(), LoginActivity.class);
            startActivity(inty);
            getActivity().finish();
        }



/*
        else if (position == 5) {  //Share the app
            */
/*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Rackloot");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Hey, Check out the new Rackloot app on Android Play Store.  https://play.google.com/store/apps/details?id=com.acmatics.rackloot&hl=en");
            startActivity(Intent.createChooser(sharingIntent, "Share via"));*//*


            String[] blacklist = new String[]{"com.android.bluetooth", "com.google.android.apps.docs", "com.dropbox.android", "com.estrongs.android.pop", "com.sec.android.app.memo", "com.google.provider.NotePad", "com.socialnmobile.dictapps.notepad.color.note"};
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Hey, Check out the new Rackloot app on Android Play Store.  https://play.google.com/store/apps/details?id=com.acmatics.rackloot&hl=en");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Rackloot");
            startActivity(generateCustomChooserIntent(intent, blacklist));


        } else if (position == 2) {  //favourites
        } else if (position == 3) {  //Notifications
        } else if (position == 4) {//Rate App
            Uri uri = Uri.parse("market://details?id=" + "com.acmatics.rackloot");
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity, " Unable to find market app", Toast.LENGTH_LONG).show();

            }
        } else if (position == 5) {  //feedback
        } else if (position == 6) {  //Contact Us
        } else if (position == 7) {  //Login/Logout
        }
*/
    }

    private Intent generateCustomChooserIntent(Intent prototype, String[] forbiddenChoices) {
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        List<HashMap<String, String>> intentMetaInfo = new ArrayList<HashMap<String, String>>();
        Intent chooserIntent;
        Intent dummy = new Intent(prototype.getAction());
        dummy.setType(prototype.getType());
        List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(dummy, 0);

        if (!resInfo.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfo) {
                if (resolveInfo.activityInfo == null || Arrays.asList(forbiddenChoices).contains(resolveInfo.activityInfo.packageName))
                    continue;

                HashMap<String, String> info = new HashMap<String, String>();
                info.put("packageName", resolveInfo.activityInfo.packageName);
                info.put("className", resolveInfo.activityInfo.name);
                info.put("simpleName", String.valueOf(resolveInfo.activityInfo.loadLabel(getActivity().getPackageManager())));
                intentMetaInfo.add(info);
            }

            if (!intentMetaInfo.isEmpty()) {
// sorting for nice readability
                Collections.sort(intentMetaInfo, new Comparator<HashMap<String, String>>() {
                    @Override
                    public int compare(HashMap<String, String> map, HashMap<String, String> map2) {
                        return map.get("simpleName").compareTo(map2.get("simpleName"));
                    }
                });

// create the custom intent list
                for (HashMap<String, String> metaInfo : intentMetaInfo) {
                    Intent targetedShareIntent = (Intent) prototype.clone();
                    targetedShareIntent.setPackage(metaInfo.get("packageName"));
                    targetedShareIntent.setClassName(metaInfo.get("packageName"), metaInfo.get("className"));
                    targetedShareIntents.add(targetedShareIntent);
                }

                chooserIntent = Intent.createChooser(targetedShareIntents.remove(targetedShareIntents.size() - 1), "Share via");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                return chooserIntent;
            }
        }

        return Intent.createChooser(prototype, "rackloot");
    }
}
