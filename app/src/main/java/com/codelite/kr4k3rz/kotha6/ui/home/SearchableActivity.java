package com.codelite.kr4k3rz.kotha6.ui.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.codelite.kr4k3rz.kotha6.R;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;

public class SearchableActivity extends AppCompatActivity {
    ArrayList<String> arrayList = new ArrayList<>();
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        mSearchView = findViewById(R.id.searchView);
        List<SearchItem> suggestionsList = new ArrayList<>();
        mSearchView.setNavigationIcon(R.drawable.ic_search);

        arrayList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.city_entries)));
        for (String s : arrayList) {
            suggestionsList.add(new SearchItem(s));
        }


        if (mSearchView != null) {
            mSearchView.setVersionMargins(SearchView.VersionMargins.MENU_ITEM);
            mSearchView.setHint(R.string.search);
            SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
            searchAdapter.setOnSearchItemClickListener(new SearchAdapter.OnSearchItemClickListener() {
                @Override
                public void onSearchItemClick(View view, int position, String text) {
                    Paper.book().write("RESULT", text);
                    Log.d("TAG", "text: " + text);
                    mSearchView.close(true);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }
            });
            mSearchView.setAdapter(searchAdapter);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSearchView.close(true);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


    }
}
