package com.boom.vyveska.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.boom.vyveska.MainActivity;
import com.boom.vyveska.R;
import com.boom.vyveska.data.DBHelper;
import com.boom.vyveska.data.FireBaseLoad;
import com.boom.vyveska.data.MaskTransformation;
import com.boom.vyveska.data.PostFullInfo;
import com.boom.vyveska.data.PostInfo;
import com.boom.vyveska.data.profile_info;
import com.boom.vyveska.data.user_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.UUID;

public class FullOrederActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    TextView fullText, btnCart, price, btnDel;
    ImageView fullImage;
    EditText countCart;
    PostFullInfo postInfo;
    DBHelper dbHelper;
    user_info userInfo;

    FireBaseLoad fireBaseLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_oreder);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        fullText = findViewById(R.id.textFullOrder_admin);
        fullImage = findViewById(R.id.fullImage_admin);
        btnCart = findViewById(R.id.btnCart);
        countCart = findViewById(R.id.countCart);
        price = findViewById(R.id.textView12);
        btnDel = findViewById(R.id.textView14);
        userInfo = profile_info.getInstance().getMy_user_info();


        if(userInfo.getPost().equals("admin")){
            btnDel.setVisibility(View.VISIBLE);
        }

         dbHelper = new DBHelper(this);
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            dbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }



        Bundle arguments = getIntent().getExtras();

        if(arguments!=null) {
            postInfo = (PostFullInfo) arguments.getSerializable(PostInfo.class.getSimpleName());
            fullText.setText(postInfo.getFullText());
            price.setText(postInfo.getPrice()+"р.");

            final Transformation transformation = new MaskTransformation(FullOrederActivity.this, R.drawable.convers_transformation);
            Picasso.get()
                    .load(postInfo.getPhoto())
                    .resize(550, 350) // resizes the image to these dimensions (in pixel)
                    .centerCrop()
                    .transform(transformation)
                    .into(fullImage);
        }

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = countCart.getText().toString();

                if (!text.equals("")) {
                    try {
                        dbHelper.addData(postInfo.getText(),Integer.parseInt(text));
                        dbHelper.close();

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }




                    Intent i = new Intent(FullOrederActivity.this, MainActivity.class);
                    startActivity(i);
                }


            }
        });


        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("post", postInfo.getId());
                    removePost(postInfo.getId());

                   // fireBaseLoad.getPostInfo();
                   // fireBaseLoad.getCompanyInfo();






            }
        });



    }

    public String createTransactionID() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    private  void removePost(String s){
        db.collection("posts").document(s).delete();

        Intent i = new Intent(FullOrederActivity.this, LoadActivity.class);
        startActivity(i);
    }
}