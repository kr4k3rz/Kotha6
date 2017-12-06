package com.codelite.kr4k3rz.kotha6;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.codelite.kr4k3rz.kotha6.model.Room;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.DefaultSliderView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.paperdb.Paper;
import mehdi.sakout.fancybuttons.FancyButton;

public class PreviewActivity extends AppCompatActivity {
    String TAG = PreviewActivity.class.getSimpleName();
    Room room = Paper.book().read("ROOM");
    TextView mRoomAmt, mAvailableFrom, mAddress, mDesc;
    ImageView mWifi, mParking, mSmoking, mParty, mPets;
    FancyButton mMen, mWomen, mCouple, mStudents, mProfessionals;
    SliderLayout sliderLayout;
    FloatingActionButton fab_upload;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        sliderLayout = findViewById(R.id.slider);
        fab_upload = findViewById(R.id.fab_upload);
        ArrayList<String> mImageUrl = room.getImg_urls();
        mRoomAmt = findViewById(R.id.set_rent_amt);
        mAvailableFrom = findViewById(R.id.set_available_date);
        mAddress = findViewById(R.id.set_address);
        mDesc = findViewById(R.id.set_room_desc);


        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);

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


        fab_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users/" + mAuth.getCurrentUser().getUid());
                DatabaseReference roomsRef = database.getReference("rooms/" + mAuth.getCurrentUser().getUid());
                myRef.child("rooms").push().setValue(room);
                roomsRef.push().setValue(room);
                finish();


            }
        });


        if (!room.getAmenities().isWifi())
            mWifi.setVisibility(View.INVISIBLE);
        if (!room.getAmenities().isParking())
            mParking.setVisibility(View.INVISIBLE);
        if (!room.getAmenities().isSmoking())
            mSmoking.setVisibility(View.INVISIBLE);
        if (!room.getAmenities().isParty())
            mParty.setVisibility(View.INVISIBLE);
        if (!room.getAmenities().isPets())
            mPets.setVisibility(View.INVISIBLE);

        Log.d(TAG, "" + room.getAvailableFor().isMen());
        Log.d(TAG, "" + room.getAvailableFor().isWomen());
        Log.d(TAG, "" + room.getAvailableFor().isCouple());
        Log.d(TAG, "" + room.getAvailableFor().isStudents());
        Log.d(TAG, "" + room.getAvailableFor().isProfessionals());


        mMen.setEnabled(room.getAvailableFor().isMen());
        mWomen.setEnabled(room.getAvailableFor().isWomen());
        mCouple.setEnabled(room.getAvailableFor().isCouple());
        mStudents.setEnabled(room.getAvailableFor().isStudents());
        mProfessionals.setEnabled(room.getAvailableFor().isProfessionals());

        mRoomAmt.setText(room.getRent_amt());
        mAvailableFrom.setText(room.getAvailable_date());
        mAddress.setText(room.getAddress());
        mDesc.setText(room.getRoom_description());


        for (String s : mImageUrl) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            sliderView.image(s).setBitmapTransformation(new CenterCrop());
            sliderLayout.addSlider(sliderView);
        }

    }

    @Override
    protected void onStop() {
        // make sure to call stopAutoCycle() on the slider before activity
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

}
