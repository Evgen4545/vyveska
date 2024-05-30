package com.boom.vyveska.ui.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import com.boom.vyveska.R;
import com.boom.vyveska.data.FireBaseLoad;
import com.boom.vyveska.data.MaskTransformation;
import com.boom.vyveska.data.company_info;
import com.boom.vyveska.data.profile_info;
import com.boom.vyveska.data.user_info;
import com.boom.vyveska.databinding.FragmentProfileBinding;
import com.boom.vyveska.ui.CompanyActivity;
import com.boom.vyveska.ui.CreatPostActivity;
import com.boom.vyveska.ui.CreatViewActivity;
import com.boom.vyveska.ui.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseUser aUser;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    user_info userInfo;
    company_info companyInfo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView nameText = binding.nameText;
        TextView emailText = binding.emailText;
        TextView exit = binding.exit;
        ImageView photo = binding.photo;
        TextView companyName = binding.textView;
        TextView companyEmail = binding.textView3;
        TextView companyPhone = binding.textView9;
        ImageView companyWhatsApp = binding.imageView2;

        ImageView btnCompany = binding.imageView5;

        mAuth = FirebaseAuth.getInstance();
        aUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        FireBaseLoad data = new FireBaseLoad(getContext());



        userInfo = profile_info.getInstance().getMy_user_info();
        companyInfo = profile_info.getInstance().getCompanyInfo();
        System.out.println(userInfo.getName());
        nameText.setText(userInfo.getName());
        emailText.setText(userInfo.getEmail());

        companyName.setText(companyInfo.getName());
        companyPhone.setText(companyInfo.getPhone());
        companyEmail.setText(companyInfo.getEmail());



        // Picasso.get().load(userInfo.getPhoto()).into(photo);


       // Log.d("log",userInfo.getId() );

        if (userInfo.getPost().toString().equals("admin")){

            btnCompany.setVisibility(View.VISIBLE);

        }

        companyWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = companyInfo.getPhone().toString();


                if(!s.equals("")) {
                    String url = "https://api.whatsapp.com/send?phone=" + s;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }


            }

        });


        btnCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getActivity(), CompanyActivity.class);
                startActivity(i);}




        });



        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragmentExit myDialogFragment = new DialogFragmentExit();
                FragmentManager manager = getFragmentManager();
                //myDialogFragment.show(manager, "dialog");

                FragmentTransaction transaction = manager.beginTransaction();
                myDialogFragment.show(transaction, "dialog");


            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class DialogFragmentExit extends DialogFragment {




        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = "Вы уверены, что хотите завершить сеанс? ";
            String button1String = "Да, я уверен(а)";
            String button2String = "Отмена";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title);  // заголовок
            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
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
}