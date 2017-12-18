package com.codelite.kr4k3rz.kotha6.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codelite.kr4k3rz.kotha6.ListRoomActivity;
import com.codelite.kr4k3rz.kotha6.R;
import com.codelite.kr4k3rz.kotha6.holer.RoomListHolder;
import com.codelite.kr4k3rz.kotha6.model.Room;
import com.codelite.kr4k3rz.kotha6.model.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import io.paperdb.Paper;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class ProfileFragment extends Fragment {
    ImageView mProfile;
    ProgressBar mProgressBar;

    FirebaseRecyclerAdapter mAdapter;
    TextView mFirstName, mLastName, mPhone, mEmailAddress;
    FloatingActionButton mFabList;
    StorageReference mProfileRef;
    View rootView;

    FirebaseAuth mAuth = getInstance();
    FirebaseUser user = getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mUserRef = database.getReference().child("users").child(mAuth.getCurrentUser().getUid());

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        final RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewLists);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        mFirstName = rootView.findViewById(R.id.firstName);
        mLastName = rootView.findViewById(R.id.lastName);
        mProfile = rootView.findViewById(R.id.profileImage);
        mPhone = rootView.findViewById(R.id.phoneNumber);
        mEmailAddress = rootView.findViewById(R.id.emailAddress);
        mProgressBar = rootView.findViewById(R.id.pr_progressbar);
        mFabList = rootView.findViewById(R.id.floating_action_button);


        mAdapter = new FirebaseRecyclerAdapter<Room, RoomListHolder>(Room.class, R.layout.item_room_list, RoomListHolder.class, mUserRef.child("posts")) {
            @Override
            protected void populateViewHolder(RoomListHolder viewHolder, Room model, int position) {
                viewHolder.setRoomRent(model.getRent_amt());
                viewHolder.setAvailableDate(model.getAvailable_date());
                viewHolder.setRoomImg(getActivity(), model.getImg_urls().get(0));
                viewHolder.setAddress(model.getAddress());
                Log.d("TAG", "position : " + position);
            }

            @Override
            public RoomListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                RoomListHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new RoomListHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(getContext());
                        builder.setTitle("Delete");
                        builder.setMessage("Delete this Room Listing?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAdapter.getRef(position).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final Room room = dataSnapshot.getValue(Room.class);
                                        assert room != null;
                                        FirebaseDatabase.getInstance().getReference().child("rooms").orderByChild("uid").equalTo(room.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    Room room1 = snapshot.getValue(Room.class);
                                                    assert room1 != null;
                                                    if (room1.getPostId().equals(room.getPostId())) {
                                                        snapshot.getRef().removeValue();
                                                        mAdapter.getRef(position).removeValue();

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                    }
                });
                return viewHolder;
            }
        };

        recyclerView.setAdapter(mAdapter);

        mFabList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListRoomActivity.class);
                startActivity(intent);
            }
        });

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithGallery(ProfileFragment.this, "Choose the option", 3);
            }
        });

        if (user != null) {
            loadUserProfile(mUserRef);
        }
        return rootView;
    }

    private void loadUserProfile(DatabaseReference myRef) {

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mProfileRef = mStorageRef.child("profiles/" + mAuth.getCurrentUser().getUid());


        if (Paper.book().exist("profile")) {
            File savedImage = Paper.book().read("profile");
            Glide.with(getContext())
                    .load(savedImage).into(mProfile);
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                mFirstName.setText(users.getFirstName());
                mLastName.setText(users.getLastName());
                mPhone.setText(users.getMobNum());
                mEmailAddress.setText(users.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(rootView, "" + databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mProgressBar.setVisibility(View.VISIBLE);
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                if (type == 3) {
                    try {
                        final File compressor = new Compressor(getContext()).compressToFile(imageFile);
                        mProfileRef.putFile(Uri.fromFile(compressor)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Glide.with(getContext())
                                        .load(compressor).into(mProfile);
                                Paper.book().write("profile", compressor);
                                mProgressBar.setVisibility(View.INVISIBLE);

                            }
                        });


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ak, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}


