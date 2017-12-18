package com.codelite.kr4k3rz.kotha6.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.kotha6.R;
import com.codelite.kr4k3rz.kotha6.holer.RoomListHolder;
import com.codelite.kr4k3rz.kotha6.model.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRoomRef = database.getReference().child("rooms");
    FirebaseRecyclerAdapter mAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_show_rooms);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        mAdapter = new FirebaseRecyclerAdapter<Room, RoomListHolder>(Room.class, R.layout.item_room_list, RoomListHolder.class, mRoomRef) {
            @Override
            protected void populateViewHolder(RoomListHolder viewHolder, Room model, int position) {
                viewHolder.setRoomRent(model.getRent_amt());
                viewHolder.setAvailableDate(model.getAvailable_date());
                viewHolder.setAddress(model.getAddress());
            }
        };

        recyclerView.setAdapter(mAdapter);


        return rootView;
    }


}
