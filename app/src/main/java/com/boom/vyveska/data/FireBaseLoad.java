package com.boom.vyveska.data;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FireBaseLoad {

    Context context;

    user_info user = new user_info();

    ArrayList<PostInfo> postInfos;
    ArrayList<PostFullInfo> orderInfos;
    ArrayList<ZakazInfo> zakazInfos;

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser userDB= mAuth.getCurrentUser();




    FirebaseFirestore db = FirebaseFirestore.getInstance();
    profile_info profileInfo = new profile_info();

      public FireBaseLoad(Context context){
          this.context=context;
      }





    public user_info getUserInfo(String UserToken) {
        user_info userInfo = new user_info();

        DocumentReference docRef = db.collection("users").document(UserToken);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        String email = notNullStrForUsers(document, "email");
                        String name = notNullStrForUsers(document, "name");
                        String post = notNullStrForUsers(document, "post");
                        String phone = notNullStrForUsers(document, "phone");
                       // String photo = notNullStrForUsers(document, "photo");




                        userInfo.setEmail(email);
                        userInfo.setName(name);
                        userInfo.setPost(post);
                        userInfo.setPhone(phone);
                      //  userInfo.setPhoto(photo);
                        userInfo.setId(document.getId());
                        profileInfo.getInstance().setMy_user_info(userInfo);
                        Log.d("map", "setMy_user_info");

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });
        return userInfo;

    }


    public void getCompanyInfo() {
        company_info companyInfo = new company_info();

        DocumentReference docRef = db.collection("info").document("3F78F698939E41B5834A1165A15361E3");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        String email = notNullStrForUsers(document, "email");
                        String name = notNullStrForUsers(document, "name");
                        String whatsapp = notNullStrForUsers(document, "whatsapp");
                        String phone = notNullStrForUsers(document, "phone");
                        // String photo = notNullStrForUsers(document, "photo");




                        companyInfo.setEmail(email);
                        companyInfo.setName(name);
                        companyInfo.setWhatsapp(whatsapp);
                        companyInfo.setPhone(phone);
                        //  userInfo.setPhoto(photo);

                        profileInfo.getInstance().setCompanyInfo(companyInfo);
                        Log.d("map", "setMy_user_info");

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });

    }


    public void getPostInfo() {
        postInfos = new ArrayList<>();
        String uid = userDB.getUid();

        db.collection("company")
                .get()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                PostInfo pInfo = new PostInfo();
                                String photo = notNullStr(document, "photo");
                                String text = notNullStr(document, "text");


                                Log.d("PART", text);



                                pInfo.setText(text);
                                pInfo.setPhoto(photo);
                                pInfo.setId(document.getId());


                                postInfos.add(pInfo);
                                Log.d(TAG, document.getId() + " => " + document.getData());


                            }

                            profileInfo.getInstance().setPost(postInfos);
                            Log.d("POSTADD", String.valueOf(postInfos.size()));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Log.d("map", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    public void getOrderInfo() {
        orderInfos = new ArrayList<>();
        String uid = userDB.getUid();

        db.collection("posts")
                .get()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                PostFullInfo pInfo = new PostFullInfo();
                                String id = notNullStr(document, "id");
                                String photo = notNullStr(document, "photo");
                                String price = notNullStr(document, "price");
                                String text = notNullStr(document, "description");
                                String fulltext = notNullStr(document, "fulldescription");


                                Log.d("PART", text);



                                pInfo.setText(text);
                                pInfo.setPhoto(photo);
                                pInfo.setFullText(fulltext);
                                pInfo.setId(document.getId());
                                pInfo.setPrice(price);


                               orderInfos.add(pInfo);
                                Log.d(TAG, document.getId() + " => " + document.getData());


                            }

                            profileInfo.getInstance().setOrder(orderInfos);
                            Log.d("ORDERADD", String.valueOf(orderInfos.size()));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Log.d("map", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    public void getZakazInfo() {
        zakazInfos = new ArrayList<>();
        String uid = userDB.getUid();

        db.collection("order")
                .get()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                ZakazInfo pInfo = new ZakazInfo();
                                String name = notNullStr(document, "name");
                                String order = notNullStr(document, "order");
                                String phone = notNullStr(document, "phone");
                                String userId = notNullStr(document, "userId");






                                pInfo.setName(name);
                                pInfo.setOrder(order);
                                pInfo.setPhone(phone);
                                pInfo.setUserId(userId);
                                pInfo.setId(document.getId());


                                zakazInfos.add(pInfo);
                                Log.d(TAG, document.getId() + " => " + document.getData());


                            }

                            profileInfo.getInstance().setZakazInfo(zakazInfos);
                            Log.d("ORDERADD", String.valueOf(orderInfos.size()));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Log.d("map", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }



    public List<String> stringToArrList(String s){
        s = s.replaceAll("[\\[\\]]", "");
        List<String> arr = new ArrayList<String>(Arrays.asList(s.split(",")));
          return arr;
    }


    public String notNullStr(QueryDocumentSnapshot document, String tokenName) {
        String dd = "";
        try {
            dd = document.getData().get(tokenName).toString();
        } catch (Exception e) {

        }
        return dd;
    }


    public String notNullStrForUsers(DocumentSnapshot document, String tokenName) {
        String dd = "";
        try {
            dd = document.getData().get(tokenName).toString();
        } catch (Exception e) {

        }
        return dd;
    }

    public void removeVoice (String s) {

        db.collection("voice").document(s).delete();
    }


}
