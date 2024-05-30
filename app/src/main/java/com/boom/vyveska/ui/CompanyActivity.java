package com.boom.vyveska.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.boom.vyveska.MainActivity;
import com.boom.vyveska.R;
import com.boom.vyveska.data.Adapter.CartList;
import com.boom.vyveska.data.Adapter.ZakaztList;
import com.boom.vyveska.data.CartInfo;
import com.boom.vyveska.data.DBHelper;
import com.boom.vyveska.data.FireBaseLoad;
import com.boom.vyveska.data.ZakazInfo;
import com.boom.vyveska.data.profile_info;
import com.boom.vyveska.data.user_info;
import com.boom.vyveska.ui.dashboard.DashboardFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CompanyActivity extends AppCompatActivity {

    ZakaztList adapter;
    FireBaseLoad load;
    ArrayList<ZakazInfo> cart = new ArrayList<ZakazInfo>();
    SwipeRefreshLayout swipeRefreshLayout = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        TextView btnCompany = findViewById(R.id.textView10);
        RecyclerView recyclerView = findViewById(R.id.ListClient);
        TextView btnNews = findViewById(R.id.companyEdit);
        TextView btnUsluga = findViewById(R.id.textView8);
        TextView refresh = findViewById(R.id.textView15);




        cart = profile_info.getInstance().getZakaz();


        ZakaztList.OnStateClickListener stateClickListener = (partners, position) -> {


            DialogFragmentDel myDialogFragment = new DialogFragmentDel(CompanyActivity.this,partners.getId());
            FragmentManager manager = getSupportFragmentManager();
            //myDialogFragment.show(manager, "dialog");

            FragmentTransaction transaction = manager.beginTransaction();
            myDialogFragment.show(transaction, "dialog");



        };


        adapter = new ZakaztList(CompanyActivity.this, cart, stateClickListener);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CompanyActivity.this));


       refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(CompanyActivity.this,LoadActivityCompany.class);
                startActivity(i);

            }
        });
        btnCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(CompanyActivity.this, CompanyInfoActivity.class);
                startActivity(i);

            }
        });

        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(CompanyActivity.this, CreatPostActivity.class);
                startActivity(i);

            }
        });

        btnUsluga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(CompanyActivity.this, CreatViewActivity.class);
                startActivity(i);

            }
        });
    }




    public static class DialogFragmentDel extends DialogFragment {


        Context context;
        ZakaztList adapter;
        String idDB;


        FirebaseFirestore  db = FirebaseFirestore.getInstance();


        public  DialogFragmentDel(Context context, String idDB){
            this.context= context;
            this.idDB=idDB;

        }
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = "Вы уверены, что хотите удалить? ";
            String button1String = "Да, я уверен(а)";
            String button2String = "Отмена";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title);  // заголовок
            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {



                    removePost(String.valueOf(idDB));



                }
            });
            builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.setCancelable(true);

            return builder.create();
        }

        private  void removePost(String s){
            db.collection("order").document(s).delete();
            // Log.d("post",id);

            Intent i = new Intent(getActivity(), LoadActivityCompany.class);
            startActivity(i);

        }


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent i = new Intent(CompanyActivity.this, MainActivity.class);
        startActivity(i);

    }

}