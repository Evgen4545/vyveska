package com.boom.vyveska.ui;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.boom.vyveska.MainActivity;
import com.boom.vyveska.R;
import com.boom.vyveska.data.FireBaseLoad;
import com.boom.vyveska.data.MaskTransformation;
import com.boom.vyveska.data.profile_info;
import com.boom.vyveska.data.user_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    private FirebaseUser aUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logoSplash);

        final Transformation transformation = new MaskTransformation(SplashActivity.this, R.drawable.rounded_convers_transformation);
        Picasso.get()
                .load(R.drawable.logo)
                .resize(200, 200) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .transform(transformation)
                .into(logo);

        aUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("ACTIVITY","splash");


        if (aUser != null) {
            // User is signed in
            String uid = aUser.getUid();
            FireBaseLoad data = new FireBaseLoad(this);
            Log.d("USERS",uid);
            data.getUserInfo(uid);
            data.getCompanyInfo();
            data.getPostInfo();
            data.getOrderInfo();
            data.getZakazInfo();




            if (isNetworkAvailable(this)) {

                new Handler().postDelayed(new Runnable() {

                    @Override

                    public void run() {



                            user_info userInfo = profile_info.getInstance().getMy_user_info();
                           //  Log.d("USERS", userInfo.getName());
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(i);
                        }










                }, 6000);




                //  Log.d("map", String.valueOf(data.getMarkersInfo()));




            }







        } else {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }

    }



    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


}

