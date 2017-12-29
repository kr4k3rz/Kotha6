package com.codelite.kr4k3rz.kotha6.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codelite.kr4k3rz.kotha6.R;
import com.codelite.kr4k3rz.kotha6.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class EditProfileActivity extends AppCompatActivity {

    ImageView mProfile;
    TextView mName, mPhone, mEmail;
    Button mSaveProfile;
    ProgressBar mProgressBar;

    User user;
    String mProfileLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        user = new User();

        mProfile = findViewById(R.id.edit_ProfileImage);
        mName = findViewById(R.id.edit_name);
        mPhone = findViewById(R.id.edit_phone);
        mEmail = findViewById(R.id.edit_email);
        mSaveProfile = findViewById(R.id.btn_save_profile);
        mProgressBar = findViewById(R.id.edit_pr_progressbar);
        loadProfileData();


        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithGallery(EditProfileActivity.this, "Choose the option", 3);
            }
        });


        mSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                mUserRef.child("name").setValue(mName.getText().toString());
                mUserRef.child("email").setValue(mEmail.getText().toString());
                mUserRef.child("mobNum").setValue(mPhone.getText().toString());
                mUserRef.child("photoUrl").setValue(mProfileLink);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mProgressBar.setVisibility(View.VISIBLE);
        EasyImage.handleActivityResult(requestCode, resultCode, data, EditProfileActivity.this, new DefaultCallback() {
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                if (type == 3) {
                    final StorageReference mProfileRef = FirebaseStorage.getInstance().getReference().child("profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_pic");
                    try {
                        final File compressor = new Compressor(EditProfileActivity.this).compressToFile(imageFile); //compress the image taken from phone
                        mProfileRef.putFile(Uri.fromFile(compressor)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {   //Profile Image uploaded
                                mProfileLink = String.valueOf(taskSnapshot.getDownloadUrl()); //image link is saved in a String
                                mProgressBar.setVisibility(View.GONE);
                                Glide.with(EditProfileActivity.this)  //set compressed image
                                        .load(compressor).into(mProfile);

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private void loadProfileData() {
        DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                mProfileLink = user.getPhotoUrl();  //set default String URl
                Glide.with(getApplicationContext())
                        .load(user.getPhotoUrl()).into(mProfile);
                mName.setText(String.format("%s", user.getName()));
                mPhone.setText(user.getMobNum());
                mEmail.setText(user.getEmail());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //   Snackbar.make(rootView, "" + databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            case R.id.action_save:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
}
