package com.codelite.kr4k3rz.kotha6.ui.profile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.codelite.kr4k3rz.kotha6.R;
import com.codelite.kr4k3rz.kotha6.helper.UtilHelper;
import com.codelite.kr4k3rz.kotha6.model.Post;
import com.codelite.kr4k3rz.kotha6.model.User;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

import io.paperdb.Paper;
import mehdi.sakout.fancybuttons.FancyButton;

public class PreviewActivity extends AppCompatActivity {
    TextView mRoomAmt, mAvailableFrom, mAddress, mDesc;
    ImageView mWifi, mParking, mSmoking, mParty, mPets;
    FancyButton mMen, mWomen, mCouple, mStudents, mProfessionals;
    SliderLayout mSliderLayout;
    FloatingActionButton mFabUpload;
    ProgressBar mProgressBar;
    CoordinatorLayout layout;

    Post post;
    ArrayList<String> imgCache = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        mSliderLayout = findViewById(R.id.slider);
        mFabUpload = findViewById(R.id.fab_upload);
        layout = findViewById(R.id.main_content);

        post = (Post) getIntent().getSerializableExtra("ROOM");
        imgCache = getIntent().getStringArrayListExtra("CACHE_IMAGE");

        mRoomAmt = findViewById(R.id.set_rent_amt);
        mAvailableFrom = findViewById(R.id.set_available_date);
        mAddress = findViewById(R.id.set_address);
        mDesc = findViewById(R.id.set_room_desc);

        mProgressBar = findViewById(R.id.progressbar);

        mWifi = findViewById(R.id.iv_wifi);
        mParking = findViewById(R.id.iv_parking);
        mSmoking = findViewById(R.id.iv_smoking);
        mParty = findViewById(R.id.iv_party);
        mPets = findViewById(R.id.iv_pets);

        mMen = findViewById(R.id.btn_men);
        mWomen = findViewById(R.id.btn_women);
        mCouple = findViewById(R.id.btn_couple);
        mStudents = findViewById(R.id.btn_student);
        mProfessionals = findViewById(R.id.btn_professional);

        mFabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UtilHelper.isOnline(getApplicationContext())) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    post.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    post.setPostId(UUID.randomUUID().toString());
                    User user = Paper.book().read("USER");
                    post.setPoster(user);
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("posts").push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseDatabase.getInstance().getReference().child("rooms").push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    Paper.book().delete("CHECK_IMAGES_UPLOADED_FLAG");
                                }
                            });
                        }
                    });
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Snackbar.make(layout, "Check Internet!", Snackbar.LENGTH_LONG).show();
                }


            }
        });


        if (!post.getAmenities().isWifi())
            mWifi.setVisibility(View.INVISIBLE);
        if (!post.getAmenities().isParking())
            mParking.setVisibility(View.INVISIBLE);
        if (!post.getAmenities().isSmoking())
            mSmoking.setVisibility(View.INVISIBLE);
        if (!post.getAmenities().isParty())
            mParty.setVisibility(View.INVISIBLE);
        if (!post.getAmenities().isPets())
            mPets.setVisibility(View.INVISIBLE);

        mMen.setEnabled(post.getAvailableFor().isMen());
        mWomen.setEnabled(post.getAvailableFor().isWomen());
        mCouple.setEnabled(post.getAvailableFor().isCouple());
        mStudents.setEnabled(post.getAvailableFor().isStudents());
        mProfessionals.setEnabled(post.getAvailableFor().isProfessionals());

        mRoomAmt.setText(post.getRent_amt());
        mAvailableFrom.setText(post.getAvailable_date());
        mAddress.setText(post.getAddress());
        mDesc.setText(post.getRoom_description());

        for (String s : imgCache) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            sliderView.image(s).setBitmapTransformation(new CenterCrop());
            mSliderLayout.addSlider(sliderView);
        }
    }

    @Override
    protected void onStop() {
        mSliderLayout.stopAutoCycle();
        super.onStop();
    }

}
