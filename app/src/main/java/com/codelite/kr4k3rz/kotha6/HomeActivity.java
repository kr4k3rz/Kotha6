package com.codelite.kr4k3rz.kotha6;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.codelite.kr4k3rz.kotha6.fragments.FabFragment;
import com.codelite.kr4k3rz.kotha6.fragments.HomeFragment;
import com.codelite.kr4k3rz.kotha6.fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity {
    ImageView searchList;
    ImageView filterList;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchList = findViewById(R.id.searchList);
        filterList = findViewById(R.id.filterList);
        layout = findViewById(R.id.item_layout);
        searchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchableActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
        filterList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initializing a bottom sheet
               /* BottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomDialog();
                //show it
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
*/
                FragmentManager fragmentManager = getSupportFragmentManager();
                AKDialogFragment newFragment = new AKDialogFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();

            }
        });
        setupNavigationBar();
    }

    private void setupNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.home_action:
                                layout.setVisibility(View.VISIBLE);
                                selectedFragment = HomeFragment.newInstance();
                                break;
                            case R.id.fab_action:
                                layout.setVisibility(View.INVISIBLE);
                                selectedFragment = FabFragment.newInstance();
                                break;
                            case R.id.profile_action:
                                layout.setVisibility(View.INVISIBLE);
                                selectedFragment = ProfileFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        transaction.commit();
        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

}
