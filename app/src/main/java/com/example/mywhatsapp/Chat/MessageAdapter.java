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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> { // get data from xml alliw ke user
    // ArrayList<MessageAdapter> Message;
    ArrayList<MessageObject> MessageList;

    public MessageAdapter(ArrayList<MessageObject> MessageList) {
        this.MessageList = MessageList;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        MessageViewHolder rcv = new MessageViewHolder(layoutView);
        return rcv;

    } // untuk call for user

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, final int position) {
        holder.mMessage.setText(MessageList.get(position).getMessage());
        holder.mSender.setText(MessageList.get(position).getSenderId());

    }

    @Override


    public int getItemCount() {
        return MessageList.size();// untuk dapatkan semua user yang ada atau untuk nampilkan

    }

     class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView mMessage , mSender;
         LinearLayout mlayout;
         MessageViewHolder(@NonNull View view) {
            super(view);


            mlayout = view.findViewById(R.id.mLayout);
             mMessage = view.findViewById(R.id.message);
             mSender = view.findViewById(R.id.sender);

        }
    }
}
