package com.codelite.kr4k3rz.kotha6.view.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.codelite.kr4k3rz.kotha6.HomeActivity;
import com.codelite.kr4k3rz.kotha6.R;
import com.codelite.kr4k3rz.kotha6.model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    EditText _firstNameText, _lastNameText, _emailText, _passwordText, _phoneNumber;
    Button _signupButton;
    TextView _loginLink;
    ScrollView view;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        _firstNameText = findViewById(R.id.item_firstName);
        _lastNameText = findViewById(R.id.item_lastName);
        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        _phoneNumber = findViewById(R.id.input_mob);
        view = findViewById(R.id.signup_main);

        _signupButton = findViewById(R.id.btn_signup);
        _loginLink = findViewById(R.id.link_login);

        //Instantiate Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String firstName = _firstNameText.getText().toString();
        final String lastName = _lastNameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String mobile = _phoneNumber.getText().toString();

        // TODO: Implement your own signup logic here.

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(SignupActivity.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("users/" + mAuth.getCurrentUser().getUid());
                        Users name = new Users();
                        name.setFirstName(firstName);
                        name.setLastName(lastName);
                        name.setMobNum(mobile);
                        name.setEmail(email);

                        myRef.setValue(name);
                        Paper.book().write("user", name);
                        onSignupSuccess();
                        startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                        progressDialog.hide();
                        finish();
                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.hide();
                onSignupFailed();
            }
        });


    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Snackbar.make(view, "Login failed", Snackbar.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String phone = _phoneNumber.getText().toString();

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {

            _phoneNumber.setError("enter a valid phone number");
            valid = false;
        } else {
            _phoneNumber.setError(null);
        }

        if (firstName.isEmpty() || lastName.length() < 3) {
            _firstNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _firstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            _lastNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _lastNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

