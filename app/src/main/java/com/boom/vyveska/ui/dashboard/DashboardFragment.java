package com.boom.vyveska.ui.dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boom.vyveska.data.Adapter.CartList;
import com.boom.vyveska.data.Adapter.PostList;
import com.boom.vyveska.data.FireBaseLoad;
import com.boom.vyveska.data.PostInfo;
import com.boom.vyveska.data.profile_info;
import com.boom.vyveska.data.user_info;
import com.boom.vyveska.databinding.FragmentDashboardBinding;
import com.boom.vyveska.ui.FullOrederActivity;
import com.boom.vyveska.ui.LoadActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    user_info userInfo;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        Log.d("ACTIVITY","home");
        FirebaseUser aUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = aUser.getUid();
        FireBaseLoad data = new FireBaseLoad(getContext());
        Log.d("USERS",uid);
        data.getPostInfo();
        userInfo = profile_info.getInstance().getMy_user_info();


        PostList.OnStateClickListener stateClickListener = (partners, position) -> {
            if (userInfo.getPost().toString().equals("admin")){

                DialogFragmentDel myDialogFragment = new DialogFragmentDel(getActivity(), partners.getId());

                FragmentManager manager = getFragmentManager();
                //myDialogFragment.show(manager, "dialog");

                FragmentTransaction transaction = manager.beginTransaction();
                myDialogFragment.show(transaction, "dialog");

            }





        };


        ArrayList<PostInfo> Infos = profile_info.getInstance().getPost();
        RecyclerView recyclerView = binding.PostListCom;


        PostList adapter = new PostList(getActivity(),Infos,stateClickListener);

        if (Infos.size()!=0) {
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else Log.d("RECYCLER", "0");
        return root;
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
            db.collection("company").document(s).delete();
           // Log.d("post",id);

            Intent i = new Intent(getActivity(), LoadActivity.class);
            startActivity(i);
        }

    }
}