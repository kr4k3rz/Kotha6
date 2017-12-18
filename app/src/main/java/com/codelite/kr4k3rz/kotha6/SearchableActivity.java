package com.codelite.kr4k3rz.kotha6;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codelite.kr4k3rz.kotha6.holer.LocationListAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchableActivity extends AppCompatActivity {
    LocationListAdapter locationListAdapter;
    ArrayList<String> arrayList = new ArrayList<>();
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        mRecyclerView = findViewById(R.id.recycler_view_locations);
        arrayList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.city_entries)));
        Log.d("TAG", "" + arrayList.size() + "\n" + arrayList.get(2));
        locationListAdapter = new LocationListAdapter(this, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(locationListAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_list_action, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Intent intent = new Intent(SearchableActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return false;
            }
        });

        SearchView searchView;
        MenuItem searchMenuItem;
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchMenuItem = menu.findItem(R.id.action_search);
        searchMenuItem.expandActionView();
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint("Search location...");
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int textLength = newText.length();
                ArrayList<String> tempList = new ArrayList<>();
                for (String s : arrayList) {
                    if (textLength <= s.length()) {
                        if (s.toLowerCase().contains(newText.toLowerCase())) {
                            tempList.add(s);
                        }
                    }
                }
                locationListAdapter = new LocationListAdapter(SearchableActivity.this, tempList);
                mRecyclerView.setAdapter(locationListAdapter);
                return true;
            }
        });
        return true;
    }


}
