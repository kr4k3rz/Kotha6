package com.codelite.kr4k3rz.kotha6.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.google.android.gms.tasks.OnFailureListener;
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

import io.paperdb.Paper;

import static com.google.firebase.auth.FirebaseAuth.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    public static final int GET_FROM_GALLERY = 3;
    Uri selectedImage;
    ImageView profileImageView;
    ProgressBar progressBar;

    FirebaseRecyclerAdapter adapter;

    TextView _firstName, _lastName, _phoneNumber, _emailAddress;
    FloatingActionButton fab_addListing;

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    StorageReference riversRef;


    private RecyclerView recyclerView;


    public ProfileFragment() {
        // Required empty public constructor
        selectedImage = null;
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewLists);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

       /* FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("users/" + mAuth.getCurrentUser().getUid()).child("rooms");*/


        _firstName = rootView.findViewById(R.id.firstName);
        _lastName = rootView.findViewById(R.id.lastName);
        profileImageView = rootView.findViewById(R.id.profileImage);
        _phoneNumber = rootView.findViewById(R.id.phoneNumber);
        _emailAddress = rootView.findViewById(R.id.emailAddress);
        progressBar = rootView.findViewById(R.id.pr_progressbar);
        fab_addListing = rootView.findViewById(R.id.floating_action_button);

        FirebaseUser user = getInstance().getCurrentUser();
        mAuth = getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + mAuth.getCurrentUser().getUid());

        riversRef = mStorageRef.child("profiles/" + mAuth.getCurrentUser().getUid());


        adapter = new FirebaseRecyclerAdapter<Room, RoomListHolder>(Room.class, R.layout.item_room_list, RoomListHolder.class, myRef.child("rooms")) {
            @Override
            protected void populateViewHolder(RoomListHolder viewHolder, Room model, int position) {
                viewHolder.setRoomRent(model.getRent_amt());
                viewHolder.setAvailableDate(model.getAvailable_date());
                //Log.d("TAG", model.getAddress());
            }
        };

        recyclerView.setAdapter(adapter);

        fab_addListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListRoomActivity.class);
                startActivity(intent);
            }
        });

        if (user != null) {
            selectedImage = Paper.book().read("profilePic");
            Glide.with(getActivity())
                    .load(selectedImage).into(profileImageView);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Users users = dataSnapshot.getValue(Users.class);
                    _firstName.setText(users.getFirstName());
                    _lastName.setText(users.getLastName());
                    _phoneNumber.setText(users.getMobNum());
                    _emailAddress.setText(users.getEmail());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            profileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                }
            });

        }

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            Log.d("TAG", "imageURi" + selectedImage);

            Glide.with(getActivity())
                    .load(selectedImage).into(profileImageView);
            Paper.book().write("profilePic", selectedImage);

            /*Start to upload the Selected Image Profile*/

            progressBar.setVisibility(View.VISIBLE);
            riversRef.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //Log.d("TAG", taskSnapshot.getDownloadUrl() + "");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });

        }
    }

}
