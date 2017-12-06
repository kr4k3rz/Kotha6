package com.codelite.kr4k3rz.kotha6;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.codelite.kr4k3rz.kotha6.model.Amenities;
import com.codelite.kr4k3rz.kotha6.model.AvailableFor;
import com.codelite.kr4k3rz.kotha6.model.Room;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class ListRoomActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = ListRoomActivity.class.getSimpleName();
    ImageButton mImageBtnToolbar, mImageBtnDone;

    Room room = new Room();
    AvailableFor mAvailableFor = new AvailableFor();
    Amenities mAmenities = new Amenities();

    ImageView mImage1, mImage2, mImage3, mImage4;
    EditText mAddress;
    SearchableSpinner mCity;
    EditText mRentAmt;
    EditText mCalender;
    EditText mDescRoom;
    Switch mSwitchMen, mSwitchWomen, mSwitchCouple, mSwitchStudent, mSwitchProfessionals;
    CheckBox mWifi, mParking, mSmoking, mParty, mPets;

    ArrayList<String> mImageUrls = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room);
        Paper.book().delete("ROOM");
        mImageBtnDone = findViewById(R.id.imgBtn_done);
        mImageBtnToolbar = findViewById(R.id.toolbar_back);

        mImage1 = findViewById(R.id.select_img1);
        mImage2 = findViewById(R.id.select_img2);
        mImage3 = findViewById(R.id.select_img3);
        mImage4 = findViewById(R.id.select_img4);

        mAddress = findViewById(R.id.input_address);
        mCity = findViewById(R.id.city_spinner);
        mRentAmt = findViewById(R.id.input_rent_amt);

        mCalender = findViewById(R.id.input_date);
        mDescRoom = findViewById(R.id.input_description);

        mSwitchMen = findViewById(R.id.switch_men);
        mSwitchWomen = findViewById(R.id.switch_women);
        mSwitchCouple = findViewById(R.id.switch_couple);
        mSwitchStudent = findViewById(R.id.switch_students);
        mSwitchProfessionals = findViewById(R.id.switch_professionals);

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

        mImage1.setOnClickListener(this);
        mImage2.setOnClickListener(this);
        mImage3.setOnClickListener(this);
        mImage4.setOnClickListener(this);

        getAvailableDate();

        mCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String city = getResources().getStringArray(R.array.city_entries)[i];
                room.setCity(city);

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
                    room.setRent_amt(rentAmt);
                }
            }
        });
        mAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String address = mAddress.getText().toString();
                    room.setAddress(address);
                }
            }
        });


        mDescRoom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String roomDesc = mDescRoom.getText().toString();
                    room.setRoom_description(roomDesc);
                    Log.d(TAG, "" + roomDesc);
                }
            }
        });

        mImageBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDescRoom.getText().toString().isEmpty() || mAddress.getText().toString().isEmpty() || mRentAmt.getText().toString().isEmpty()) {
                    mDescRoom.setError("required");
                    mAddress.setError("required");
                    mRentAmt.setError("required");

                } else {
                    room.setAmenities(mAmenities);
                    room.setAvailableFor(mAvailableFor);
                    room.setRoom_description(mDescRoom.getText().toString());
                    Paper.book().write("ROOM", room);
                    startActivityForResult(new Intent(ListRoomActivity.this, PreviewActivity.class), 13);

                }

            }
        });

        mImageBtnToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                        room.setAvailable_date(sdf.format(myCalendar.getTime()));
                    }

                };
                new DatePickerDialog(ListRoomActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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

        if (requestCode == 13)
            finish();

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {

                switch (type) {
                    case 1:
                        Glide.with(ListRoomActivity.this)
                                .load(imageFile).into(mImage1);
                        mImageUrls.add(imageFile.getAbsolutePath());
                        room.setImg_urls(mImageUrls);
                        break;
                    case 2:
                        Glide.with(ListRoomActivity.this)
                                .load(imageFile).into(mImage2);
                        mImageUrls.add(imageFile.getAbsolutePath());
                        room.setImg_urls(mImageUrls);
                        break;
                    case 3:
                        Glide.with(ListRoomActivity.this)
                                .load(imageFile).into(mImage3);
                        mImageUrls.add(imageFile.getAbsolutePath());
                        room.setImg_urls(mImageUrls);
                        break;
                    case 4:
                        Glide.with(ListRoomActivity.this)
                                .load(imageFile).into(mImage4);
                        mImageUrls.add(imageFile.getAbsolutePath());
                        room.setImg_urls(mImageUrls);
                        break;
                    default:
                        break;

                }

            }
        });
    }


}




