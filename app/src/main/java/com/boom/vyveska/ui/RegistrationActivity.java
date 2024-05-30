package com.boom.vyveska.ui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boom.vyveska.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity { EditText emailEditText , nameEditText , passwordEditText, phoneEditText;
    TextView btnRegistration ;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    private String uploadedImageUrl;

    Bitmap galleryPic = null;

    private static final int SELECT_PICTURE = 1;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_PASS = "pass";
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        emailEditText = findViewById(R.id.emailEditText);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.nameEditText2);
        passwordEditText = findViewById(R.id.passwordEditText);




        btnRegistration = findViewById(R.id.btnRegistration);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();







        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String phone = phoneEditText.getText().toString();


                if (!email.equals("") || !name.equals("") || !password.equals(""))
                {
                    createAccount(email , password ,name, phone);




                }
            }
        });


    }


    private void createAccount(String email, String password, String name, String phone) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            user = mAuth.getCurrentUser();
                            String uid = user.getUid();

                            Log.d("UID", uid);
                            setUserInfo( uid , email , name, phone);





                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Error"+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
        // [END create_user_with_email]
    }



    public  void setUserInfo (String uid , String email ,String name, String phone )
    {
        CollectionReference cities = db.collection("users");

        Map<String, Object> data1 = new HashMap<>();

        System.out.println(name+ email);
        Log.d("USERS", name+email);

        data1.put("email", email);
        data1.put("name",  name);
        data1.put("phone",  phone);
        data1.put("photo",  uploadedImageUrl);


        cities.document(uid).set(data1);

        Intent intent = new Intent(RegistrationActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();


    }










}