package com.codelite.kr4k3rz.kotha6.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codelite.kr4k3rz.kotha6.ListRoomActivity;
import com.codelite.kr4k3rz.kotha6.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseRecyclerAdapter adapter;
    Button _searchRoom, _searchRoomMates, _listRooms;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        _searchRoom = rootView.findViewById(R.id.searchRoomBtn);
        _searchRoomMates = rootView.findViewById(R.id.searchRoomMatesBtn);
        _listRooms = rootView.findViewById(R.id.listRoomsBtn);


        _listRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ListRoomActivity.class));
            }
        });


       /*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rooms/");*/
/*
        Button addListBtn = (Button) rootView.findViewById(R.id.addSpareRoomBtn);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewSearch);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);



        adapter = new FirebaseRecyclerAdapter<Rooms, RoomListHolder>(Rooms.class, R.layout.room_list_item, RoomListHolder.class, myRef) {
            @Override
            protected void populateViewHolder(RoomListHolder viewHolder, Rooms model, int position) {
                Log.d("TAG", model.getFistName() + "\n" + model.getLastName() + "\n" + model.getAbout());
                viewHolder.setTv_firstName(model.getFistName());
                viewHolder.setTv_lastName(model.getLastName());
                viewHolder.setTv_location(model.getLocation());
                viewHolder.setTv_description(model.getDescription());
                viewHolder.setNo_of_roomMates(model.getNo_of_roomMates());
                viewHolder.setTv_typeOfBed(model.getType_of_bed());
                viewHolder.setTv_about(model.getAbout());

            }


        };
        recyclerView.setAdapter(adapter);

        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ListRoomActivity.class));
            }
        });*/

        return rootView;
    }


}
