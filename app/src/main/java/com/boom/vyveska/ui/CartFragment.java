package com.boom.vyveska.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boom.vyveska.data.Adapter.CartList;
import com.boom.vyveska.data.CartInfo;
import com.boom.vyveska.data.DBHelper;
import com.boom.vyveska.data.profile_info;
import com.boom.vyveska.data.user_info;
import com.boom.vyveska.databinding.FragmentCartBinding;
import com.boom.vyveska.ui.profile.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private boolean data = false;
    CartInfo cartInfo;
    CartList adapter;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    user_info userInfo;

    ArrayList<CartInfo> cart = new ArrayList<CartInfo>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.CartList;
        TextView btnOrderCart = binding.orderCart;
        TextView infoText = binding.infoText;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        setInitialData(getContext());
        if (data){
            btnOrderCart.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.GONE);
        }

        CartList.OnStateClickListener stateClickListener = (partners, position) -> {

            DialogFragmentDel myDialogFragment = new DialogFragmentDel(getActivity(), partners.getId(), adapter,cart, position);
            FragmentManager manager = getFragmentManager();
            //myDialogFragment.show(manager, "dialog");

            FragmentTransaction transaction = manager.beginTransaction();
            myDialogFragment.show(transaction, "dialog");




        };
        adapter = new CartList(getActivity(), cart, stateClickListener);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnOrderCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragmentCart myDialogFragment = new DialogFragmentCart(getActivity());
                FragmentManager manager = getFragmentManager();
                //myDialogFragment.show(manager, "dialog");

                FragmentTransaction transaction = manager.beginTransaction();
                myDialogFragment.show(transaction, "dialog");


            }




        });





        return root;
    }


    private void setInitialData(Context context) {
        DBHelper db = new DBHelper(context);


        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.openDataBase();

        ArrayList<ArrayList<Object>> a = db.getAllData();
        if (a.size()>0){
            data=true;
        }
        db.close();
        int check []= new int[a.size()];
        for (int i = 0; i < a.size(); i++) {


            cart.add(new CartInfo(String.valueOf(a.get(i).get(1)), String.valueOf(a.get(i).get(2)), String.valueOf(a.get(i).get(0))));

        }






    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public static class DialogFragmentDel extends DialogFragment {


        Context context;
        String idDB;
        CartList adapter;
        ArrayList<CartInfo> cart = new ArrayList<CartInfo>();
        int position;


        public  DialogFragmentDel(Context context, String idDB, CartList adapter,ArrayList<CartInfo> cart, int position){
            this.context= context;
            this.idDB=idDB;
            this.adapter = adapter;
            this.cart = cart;
            this.position = position;

        }
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = "Вы уверены, что хотите удалить из корзины? ";
            String button1String = "Да, я уверен(а)";
            String button2String = "Отмена";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title);  // заголовок
            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    DBHelper db = new DBHelper(context);



                    try {
                        db.createDataBase();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    db.openDataBase();
                    db.deleteData(Integer.parseInt(idDB));
                    db.close();
                    cart.remove(position);
                    adapter.notifyItemRemoved(position);




                }
            });
            builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.setCancelable(true);

            return builder.create();
        }


    }

    public static class DialogFragmentCart extends DialogFragment {


        Context context;

        user_info userInfo;
        private FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();



        public  DialogFragmentCart(Context context){
            this.context= context;

        }
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = "Подтвердить заказ";
            String button1String = "Да, я уверен(а)";
            String button2String = "Отмена";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title);  // заголовок
            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    DBHelper db = new DBHelper(context);



                    try {
                        db.createDataBase();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    db.openDataBase();

                    String text ="";

                    ArrayList<ArrayList<Object>> a = db.getAllData();
                    if (a.size()>0){
                        for (int i = 0; i < a.size(); i++) {

                            text= text+String.valueOf(a.get(i).get(1))+" "+String.valueOf(a.get(i).get(2))+" шт\n";



                        }

                        try {
                            setFirebaseDataOrder(text);
                            db.deleteAllData();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    db.close();
                    int check []= new int[a.size()];





                }
            });
            builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.setCancelable(true);

            return builder.create();
        }


        private void  setFirebaseDataOrder(String order) throws Exception {


            CollectionReference cities = db.collection("order");
            user = mAuth.getCurrentUser();
            String uid = user.getUid();

            userInfo = profile_info.getInstance().getMy_user_info();

            Map<String, Object> data1 = new HashMap<>();

            System.out.println(order);


            data1.put("userId", uid);
            data1.put("order",  order);
            data1.put("phone",  userInfo.getPhone());
            data1.put("name",  userInfo.getName());



            cities.document(createTransactionID()).set(data1);

            Intent intent = new Intent(getActivity(), LoadActivity.class);
            startActivity(intent);


        }
        public String createTransactionID() throws Exception{
            return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        }

        public void show(android.app.FragmentTransaction transaction, String dialog) {
        }
    }





}