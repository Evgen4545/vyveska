package com.boom.vyveska.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.boom.vyveska.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class CreatViewActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    private String uploadedImageUrl;
    StorageReference storageReference;
    FirebaseStorage storage;
    Bitmap galleryPic = null;
    ImageView trash_image;
    private static final int SELECT_PICTURE = 1;
    TextView bntCreat;
    EditText text, price;
    EditText fulltext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_view);
         bntCreat = findViewById(R.id.addView);
         text = findViewById(R.id.viewText);
         fulltext = findViewById(R.id.viewTextFull);
        trash_image = findViewById(R.id.addImageView);
        price = findViewById(R.id.editPrice);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();




        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();



        bntCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String editText = text.getText().toString();
                String editFullText = fulltext.getText().toString();
                String editPrice = price.getText().toString();
                if(!editText.equals("")||!editFullText.equals("")||!editPrice.equals("")){


                    try {
                        Log.d("qqqqq", uploadedImageUrl);
                        setPost(editText, editFullText,editPrice);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }



            }
        });


        trash_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PICTURE);

            }
        });
    }

    public  void setPost (String text, String fulltext, String price ) throws Exception {
        CollectionReference cities = db.collection("posts");

        Map<String, Object> data1 = new HashMap<>();



        data1.put("description", text);
        data1.put("price", price);
        data1.put("id", createTransactionID());
        data1.put("fulldescription", fulltext);
        data1.put("photo",  uploadedImageUrl);


        cities.document(createTransactionID()).set(data1);

        Intent intent = new Intent(CreatViewActivity.this, LoadActivity.class);
        startActivity(intent);
        finish();


    }

    public String createTransactionID() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        switch (requestCode) {
            case SELECT_PICTURE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        galleryPic = MediaStore.Images.Media.getBitmap(CreatViewActivity.this.getContentResolver(), selectedImage);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    uploadImage(selectedImage);
                    trash_image.setImageBitmap(galleryPic);
                }
        }
    }


    private void uploadImage(Uri selectedImage) {



        if(selectedImage != null) {
            final ProgressDialog progressDialog = new ProgressDialog(CreatViewActivity.this);
            progressDialog.setTitle("Загрузка...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());


            ref.putFile(selectedImage)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                        }

                    })

                    .addOnFailureListener(new OnFailureListener() {

                        @Override

                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();

                        }

                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override

                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot

                                    .getTotalByteCount());

                            progressDialog.setMessage("Загружено" + (int) progress + "%");

                        }

                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    uploadedImageUrl = task.getResult().toString();
                                    Log.d("map", uploadedImageUrl);
                                }
                            });
                        }
                    });

        }
    }


}