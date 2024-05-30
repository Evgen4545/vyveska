package com.boom.vyveska.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boom.vyveska.data.Adapter.OrderList;
import com.boom.vyveska.data.FireBaseLoad;
import com.boom.vyveska.data.PostFullInfo;
import com.boom.vyveska.data.PostInfo;
import com.boom.vyveska.data.profile_info;
import com.boom.vyveska.data.user_info;
import com.boom.vyveska.databinding.FragmentHomeBinding;
import com.boom.vyveska.ui.CreatViewActivity;
import com.boom.vyveska.ui.FullOrederActivity;
import com.boom.vyveska.ui.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ArrayList<PostFullInfo> Infos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        Log.d("ACTIVITY","home");
        FirebaseUser aUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = aUser.getUid();
        FireBaseLoad data = new FireBaseLoad(getContext());
        Log.d("USERS",uid);
        data.getOrderInfo();

      Infos = profile_info.getInstance().getOrder();
        RecyclerView recyclerView = binding.PostList;

        OrderList.OnStateClickListener stateClickListener = (partners, position) -> {

            Intent i = new Intent(getActivity(), FullOrederActivity.class);
            i.putExtra(PostInfo.class.getSimpleName(),  partners);
            // i.putExtra("name", partners.getName());
            ////   i.putExtra("voice1", partners.getVoice());

            startActivity(i);
        };

        OrderList adapter = new OrderList(stateClickListener,getActivity(), Infos);

        if (Infos.size()!=0) {
            recyclerView.setAdapter(adapter);
        }
        else Log.d("RECYCLER", "0");





        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}