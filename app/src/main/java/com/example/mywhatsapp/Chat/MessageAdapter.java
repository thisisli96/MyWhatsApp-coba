package com.example.mywhatsapp.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywhatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

 public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> { // get data from xml alliw ke user
    // ArrayList<MessageAdapter> Message;
    ArrayList<MessageObject> messageList;

    public MessageAdapter(ArrayList<MessageObject> messageList) {
        this.messageList = messageList;

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
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {
        holder.mMessage.setText(messageList.get(position).getMessage());
        holder.mSender.setText(messageList.get(position).getSenderId());

        // 3:49: 00 displaying images w/frescoImageViewer

        if (messageList.get(holder.getAdapterPosition()).getMediaUriList().isEmpty()) // untuk menampilkan button hanya yang ada data fotonya
            holder.mViewMedia.setVisibility(View.GONE);  // untuk menampilkan button hanya yang ada data fotonya
            holder.mViewMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ImageViewer.Builder(v.getContext(), messageList.get(holder.getAdapterPosition()).getMediaUriList())
                            .setStartPosition(0)
                            .show();
                }
            });

    }

    @Override


    public int getItemCount() {
        return messageList.size();// untuk dapatkan semua user yang ada atau untuk nampilkan

    }

     class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView mMessage , mSender;

        Button mViewMedia;
         LinearLayout mlayout;
         MessageViewHolder(@NonNull View view) {
            super(view);


            mlayout = view.findViewById(R.id.mLayout);
             mMessage = view.findViewById(R.id.message);
             mSender = view.findViewById(R.id.sender);
             mViewMedia = view.findViewById(R.id.viewMedia);

        }
    }
}
