package com.codelite.kr4k3rz.kotha6.ui.profile;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codelite.kr4k3rz.kotha6.R;
import com.codelite.kr4k3rz.kotha6.helper.UtilHelper;
import com.codelite.kr4k3rz.kotha6.model.User;
import com.codelite.kr4k3rz.kotha6.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import io.paperdb.Paper;

public class ProfileFragment extends Fragment {
    ProgressBar progressBar;
    ImageView profileImageView;
    TextView nameText, emailText;
    CardView manageListingCard, mEditProfileCardView;

    FloatingActionButton mFabList;

    View rootView;

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
        assert getActivity() != null;
        getActivity().setTitle("My Profile");
        mEditProfileCardView = rootView.findViewById(R.id.profile_cardView);
        manageListingCard = rootView.findViewById(R.id.manage_listings);
        nameText = rootView.findViewById(R.id.name);
        profileImageView = rootView.findViewById(R.id.profileImage);
        emailText = rootView.findViewById(R.id.emailAddress);
        progressBar = rootView.findViewById(R.id.pr_progressbar);
        mFabList = rootView.findViewById(R.id.floating_action_button);

        DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                nameText.setText(String.format("%s", user.getName()));
                emailText.setText(user.getEmail());
                if (isAdded())
                    Glide.with(getActivity())
                            .load(user.getPhotoUrl()).into(profileImageView);
                Paper.book().write("USER", user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //   Snackbar.make(rootView, "" + databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });


        mEditProfileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        manageListingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ManageListings.class));
            }
        });


        mFabList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*To delete the Uploaded image there is the Flag which makes true at the end of PreviewActivity.class and sets it to true
                * if its not uploaded at PreviewActivity.class its will have false value,
                * whose condition we check to delete the images in Firebase*/

                if (UtilHelper.isOnline(getActivity())) {  //check if connected to internet
                    if (!Paper.book().exist("CHECK_IMAGES_UPLOADED_FLAG")) {  //check if created or not the Paper DB
                        Paper.book().write("CHECK_IMAGES_UPLOADED_FLAG", false);
                    } else {
                        //delete here all uploaded images
                        boolean flag_check = Paper.book().read("CHECK_IMAGES_UPLOADED_FLAG");
                        if (flag_check) {
                            Log.d("TAG", "Already uploaded get ready to upload new so no changes");
                        } else {
                            //delete the uploaded images as its not been uploaded
                            Log.d("TAG", "Checking condition to delete");

                            ArrayList<String> imgUrls = new ArrayList<String>();
                            imgUrls = Paper.book().read("ROOM");
                            for (String urls : imgUrls) {
                                FirebaseStorage.getInstance().getReferenceFromUrl(urls).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "delete success");
                                    }
                                });
                            }
                        }

                    }

                    Intent intent = new Intent(getContext(), ListRoomActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(rootView, "Check internet!", Snackbar.LENGTH_LONG).show();
                }

            }
        });
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}


