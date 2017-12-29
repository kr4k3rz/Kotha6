package com.codelite.kr4k3rz.kotha6.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.kotha6.R;
import com.codelite.kr4k3rz.kotha6.holer.RoomListHolder;
import com.codelite.kr4k3rz.kotha6.model.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRoomRef = database.getReference().child("rooms");
    FirebaseRecyclerAdapter mAdapter;
    RecyclerView recyclerView;
    String location;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = rootView.findViewById(R.id.rv_show_rooms);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        if (!Paper.book().exist("RESULT")) {
            location = "Kathmandu";
            Paper.book().write("RESULT", location);

        } else {
            location = Paper.book().read("RESULT");
        }

        getActivity().setTitle(location);

        mAdapter = new FirebaseRecyclerAdapter<Post, RoomListHolder>(Post.class, R.layout.item_room_list, RoomListHolder.class, mRoomRef.orderByChild("city").equalTo(location)) {
            @Override
            protected void populateViewHolder(RoomListHolder viewHolder, Post model, int position) {
                viewHolder.setRoomRent(model.getRent_amt());
                viewHolder.setAvailableDate(model.getAvailable_date());
                viewHolder.setAddress(model.getAddress());
                viewHolder.setRoomImg(getActivity(), model.getImg_urls().get(0));
            }


            @Override
            public RoomListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                RoomListHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new RoomListHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return viewHolder;
            }
        };


       /* //Show/Hide BottomNavigationBar

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && getActivity().findViewById(R.id.navigation).isShown()) {
                    getActivity().findViewById(R.id.navigation).setVisibility(View.GONE);
                } else if (dy < 0) {
                    getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });*/

        recyclerView.setAdapter(mAdapter);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(getContext(), SearchableActivity.class);
                assert getActivity() != null;
                startActivityForResult(intent, 3);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.action_filter:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FilterFragment newFragment = new FilterFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data); comment this unless you want to pass your result to the activity.
        if (requestCode == 3) {
            location = Paper.book().read("RESULT");
            getActivity().setTitle(location);
            mAdapter = new FirebaseRecyclerAdapter<Post, RoomListHolder>(Post.class, R.layout.item_room_list, RoomListHolder.class, mRoomRef.orderByChild("city").equalTo(location)) {
                @Override
                protected void populateViewHolder(RoomListHolder viewHolder, Post model, int position) {
                    viewHolder.setRoomRent(model.getRent_amt());
                    viewHolder.setAvailableDate(model.getAvailable_date());
                    viewHolder.setAddress(model.getAddress());
                    viewHolder.setRoomImg(getActivity(), model.getImg_urls().get(0));
                }
            };
            recyclerView.setAdapter(mAdapter);

        }
    }


}
