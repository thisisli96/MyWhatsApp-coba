package com.example.mywhatsapp.Chat;

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

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> { // get data from xml alliw ke user
    // ArrayList<ChatListAdapter> ChatList;
    ArrayList<ChatObject> ChatList;

    public ChatListAdapter(ArrayList<ChatObject> ChatList) {
        this.ChatList = ChatList;

    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        ChatListViewHolder rcv = new ChatListViewHolder(layoutView);
        return rcv;

    } // untuk call for user

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, final int position) {
        holder.mTitle.setText(ChatList.get(position).getChatId());

        holder.mlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        });

    }

    @Override


    public int getItemCount() {
        return ChatList.size();// untuk dapatkan semua user yang ada atau untuk nampilkan

    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public LinearLayout mlayout;
        public ChatListViewHolder(@NonNull View view) {
            super(view);
            mTitle = view.findViewById(R.id.title);

            mlayout = view.findViewById(R.id.mLayout);

        }
    }
}
