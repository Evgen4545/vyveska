package com.boom.vyveska.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.boom.vyveska.MainActivity;
import com.boom.vyveska.R;
import com.boom.vyveska.data.profile_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CompanyInfoActivity extends AppCompatActivity {

    EditText editname, editemail, editphone, editwhatsapp;
    TextView btnSave;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        editname = findViewById(R.id.editNameCompany);
        editemail = findViewById(R.id.editTextTextEmailCompany);
        editphone = findViewById(R.id.editPhoneCompany);
        editwhatsapp = findViewById(R.id.editWhatsApp);
        btnSave = findViewById(R.id.textView5);

        mAuth = FirebaseAuth.getInstance();
         db = FirebaseFirestore.getInstance();
         user = mAuth.getCurrentUser();


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "", email="", phone="", whatsapp="";
                name= editname.getText().toString();
                email = editemail.getText().toString();
                phone = editphone.getText().toString();
                whatsapp = editwhatsapp.getText().toString();

                if(!name.equals("")||!email.equals("")||phone.equals("")||whatsapp.equals("")){


                    try {
                        saveForFirebase(name,email, phone, whatsapp);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }




            }
        });



    }

    private void saveForFirebase(String name, String email, String phone, String whatsapp) throws Exception {

        CollectionReference cities = db.collection("info");
        user = mAuth.getCurrentUser();
        String uid = user.getUid();



        Map<String, Object> data1 = new HashMap<>();



        data1.put("name", name);
        data1.put("email",  email);
        data1.put("phone",  phone);
        data1.put("whatsapp",  whatsapp);



        String idDocument = "3F78F698939E41B5834A1165A15361E3";
        cities.document(idDocument).set(data1);

        Intent intent = new Intent(CompanyInfoActivity.this, CompanyActivity.class);
        startActivity(intent);
        finish();
    }

    public String createTransactionID() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}