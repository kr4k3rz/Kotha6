package com.codelite.kr4k3rz.kotha6.ui.profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.codelite.kr4k3rz.kotha6.R;
import com.codelite.kr4k3rz.kotha6.helper.UtilHelper;
import com.codelite.kr4k3rz.kotha6.model.Amenities;
import com.codelite.kr4k3rz.kotha6.model.AvailableFor;
import com.codelite.kr4k3rz.kotha6.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import id.zelory.compressor.Compressor;
import io.paperdb.Paper;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class ListRoomActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = ListRoomActivity.class.getSimpleName();

    Post post = new Post();
    AvailableFor mAvailableFor = new AvailableFor();
    Amenities mAmenities = new Amenities();

    ProgressBar mProgressbar;
    ImageView mImage1, mImage2, mImage3, mImage4;
    SearchableSpinner mCity;
    EditText mAddress;
    EditText mRentAmt;
    EditText mCalender;
    Switch mSwitchMen, mSwitchWomen, mSwitchCouple, mSwitchStudent, mSwitchProfessionals;
    CheckBox mWifi, mParking, mSmoking, mParty, mPets;
    EditText mDescRoom;

    CoordinatorLayout rootView;
    ArrayList<String> mImageUrls = new ArrayList<>();
    ArrayList<String> mImageCache = new ArrayList<>();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Room Listings");
        mProgressbar = findViewById(R.id.list_progressbar);
        rootView = findViewById(R.id.list_coordinatorLayout);


        mImage1 = findViewById(R.id.select_img1);
        mImage2 = findViewById(R.id.select_img2);
        mImage3 = findViewById(R.id.select_img3);
        mImage4 = findViewById(R.id.select_img4);

        mAddress = findViewById(R.id.input_address);
        mCity = findViewById(R.id.city_spinner);
        mRentAmt = findViewById(R.id.input_rent_amt);
        mCalender = findViewById(R.id.input_date);
        mDescRoom = findViewById(R.id.input_description);

        initSwitches();
        initCheckedBoxes();

        mImage1.setOnClickListener(this);
        mImage2.setOnClickListener(this);
        mImage3.setOnClickListener(this);
        mImage4.setOnClickListener(this);

        getAvailableDate();

        mCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String city = getResources().getStringArray(R.array.city_entries)[i];
                post.setCity(city);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mRentAmt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String rentAmt = mRentAmt.getText().toString();
                    post.setRent_amt(rentAmt);
                }
            }
        });
        mAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String address = mAddress.getText().toString();
                    post.setAddress(address);
                }
            }
        });


        mDescRoom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String roomDesc = mDescRoom.getText().toString();
                    post.setRoom_description(roomDesc);
                }
            }
        });


    }

    private void initSwitches() {
        mSwitchMen = findViewById(R.id.switch_men);
        mSwitchWomen = findViewById(R.id.switch_women);
        mSwitchCouple = findViewById(R.id.switch_couple);
        mSwitchStudent = findViewById(R.id.switch_students);
        mSwitchProfessionals = findViewById(R.id.switch_professionals);

        mSwitchMen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAvailableFor.setMen(b);
                Log.d(TAG, "" + b);

            }
        });

        mSwitchWomen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAvailableFor.setWomen(b);
                Log.d(TAG, "" + b);


            }
        });
        mSwitchCouple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAvailableFor.setCouple(b);
                Log.d(TAG, "" + b);


            }
        });

        mSwitchStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, "" + b);
                mAvailableFor.setStudents(b);
            }
        });


        mSwitchProfessionals.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAvailableFor.setProfessionals(b);
                Log.d(TAG, "" + b);
            }
        });
    }

    private void initCheckedBoxes() {
        mWifi = findViewById(R.id.checked_wifi);
        mParking = findViewById(R.id.checked_parking);
        mSmoking = findViewById(R.id.checked_smoking);
        mParty = findViewById(R.id.checked_party);
        mPets = findViewById(R.id.checked_pet);

        mWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAmenities.setWifi(b);
            }
        });

        mParking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAmenities.setParking(b);

            }
        });
        mSmoking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAmenities.setSmoking(b);

            }
        });
        mParty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAmenities.setParty(b);

            }
        });

        mPets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAmenities.setPets(b);

            }
        });


    }

    private void getAvailableDate() {
        mCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd MMMM yyyy"; // your format
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        mCalender.setText(sdf.format(myCalendar.getTime()));
                        post.setAvailable_date(sdf.format(myCalendar.getTime()));
                    }

                };
                new DatePickerDialog(ListRoomActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_img1:
                EasyImage.openChooserWithGallery(ListRoomActivity.this, "Choose the option", 1);
                break;
            case R.id.select_img2:
                EasyImage.openChooserWithGallery(ListRoomActivity.this, "Choose the option", 2);
                break;
            case R.id.select_img3:
                EasyImage.openChooserWithGallery(ListRoomActivity.this, "Choose the option", 3);
                break;
            case R.id.select_img4:
                EasyImage.openChooserWithGallery(ListRoomActivity.this, "Choose the option", 4);
                break;
            default:
                break;

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 13) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                switch (type) {
                    case 1:
                        picUpload(imageFile, mImage1);
                        mImage2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        picUpload(imageFile, mImage2);
                        mImage3.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        picUpload(imageFile, mImage3);
                        mImage4.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        picUpload(imageFile, mImage4);
                        break;
                    default:
                        break;

                }

            }

            private void picUpload(File imageFile, ImageView imageView) {
                if (UtilHelper.isOnline(ListRoomActivity.this)) {
                    mProgressbar.setVisibility(View.VISIBLE);  //shows the progressbar
                    String randomID = UUID.randomUUID().toString();  //generates Random UUID
                    File compressor = null;
                    try {
                        compressor = new Compressor(getApplication()).compressToFile(imageFile);  //compress the picture of Post
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide.with(ListRoomActivity.this)
                            .load(compressor).into(imageView);  //sets the image into imageView
                    assert compressor != null;
                    mImageCache.add(compressor.getAbsolutePath());
                    StorageReference mRoomPicRef = FirebaseStorage.getInstance().getReference().child("profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("posts").child(randomID);
                    mRoomPicRef.putFile(Uri.fromFile(compressor)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mImageUrls.add(taskSnapshot.getDownloadUrl().toString());   //adds the uploaded Image links to an ArrayList
                            mProgressbar.setVisibility(View.GONE);  //Hides the Progressbar
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(rootView, "Connect to the internet! ", Snackbar.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listing, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_next:
                checkFieldsValid();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkFieldsValid() {
        if (mDescRoom.getText().toString().isEmpty() || mAddress.getText().toString().isEmpty() || mRentAmt.getText().toString().isEmpty()) {
            mDescRoom.setError("required");
            mAddress.setError("required");
            mRentAmt.setError("required");
        } else {
            post.setAmenities(mAmenities);
            post.setAvailableFor(mAvailableFor);
            post.setRoom_description(mDescRoom.getText().toString());
            post.setImg_urls(mImageUrls);
            Intent intent = new Intent(ListRoomActivity.this, PreviewActivity.class);
            intent.putExtra("ROOM", post);
            Paper.book().write("ROOM", mImageUrls);
            intent.putExtra("CACHE_IMAGE", mImageCache);
            startActivityForResult(intent, 13);
        }
    }


}






