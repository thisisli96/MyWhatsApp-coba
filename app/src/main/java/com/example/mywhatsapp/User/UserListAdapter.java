package com.example.mywhatsapp.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywhatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> { // get data from xml alliw ke user
    // ArrayList<UserListAdapter> userList;
    ArrayList<UserObject> userList;

    public UserListAdapter(ArrayList<UserObject> userList) {
        this.userList = userList;

    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        UserListViewHolder rcv = new UserListViewHolder(layoutView);
        return rcv;

    } // untuk call for user

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, final int position) {
        holder.mName.setText(userList.get(position).getName());
        holder.mPhone.setText(userList.get(position).getPhone());

        holder.mlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

                FirebaseDatabase.getInstance().getReference().child("user")
                        .child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true); //membuat primary atau database saat ingin memulai chat


                FirebaseDatabase.getInstance().getReference().child("user")
                        .child(userList.get(position).getUid()).child("chat").child(key).setValue(true);





            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();// untuk dapatkan semua user yang ada atau untuk nampilkan

    }

    public class UserListViewHolder extends RecyclerView.ViewHolder{
        public TextView mName, mPhone ;
        public LinearLayout mlayout;
        public UserListViewHolder(@NonNull View view) {
            super(view);
            mName = view.findViewById(R.id.name);
            mPhone = view.findViewById(R.id.phone);
            mlayout = view.findViewById(R.id.mLayout);

        }
    }
}
