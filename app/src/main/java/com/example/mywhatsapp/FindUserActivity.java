package com.example.mywhatsapp;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywhatsapp.Utils.CountryToPhonePrefix;
import com.example.mywhatsapp.User.UserListAdapter;
import com.example.mywhatsapp.User.UserObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserObject> userList, contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        contactList= new ArrayList<>(); // to fix the contact
        userList= new ArrayList<>();   //  to update contact in recyleview

        initializeRecylerView();
        getContactList();

    }// akhir oncreate

    private void getContactList(){ //3.getting contatcs informasion
        // untuk memdapatkan contact kita menggunakan cursor dari contact hp

        String ISOPrefix = getCountryISO();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        // sekarang buat perulangan untuk menampilkan semua data kontak di cursor
        while (phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ","");
            phone = phone.replace("-","");
            phone = phone.replace("(","");
            phone = phone.replace(")","");
            //phone = phone.replace("","");

            if (!String.valueOf(phone.charAt(0)).equals("+"))
                phone = ISOPrefix + phone;

            UserObject mContact = new UserObject(name, phone);
            contactList.add(mContact);
            //Log.i("ini contact", contactList.toString());
            //Log.i("ini ISO data ", phone);
            //userList.add(mContact);

           // mUserListAdapter.notifyDataSetChanged();
            getUserDetails(mContact);

        }


    }

    private  String getCountryISO() { //
        String iso = null;
        TelephonyManager telephonyManager= (TelephonyManager)  getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);

        if (telephonyManager.getNetworkCountryIso() != null)
            if (!telephonyManager.getNetworkCountryIso().toString().equals(""))
                iso = telephonyManager.getNetworkCountryIso().toString();

        return  CountryToPhonePrefix.getPhone(iso);
    }
    private void getUserDetails(UserObject mContact) {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhone());
        Log.i("ini kontak pertama", query.toString());


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                    String coba;
//                    coba = dataSnapshot.child("phone").getValue().toString();
//                    Log.i("ini kontak pertama", coba);

                if (dataSnapshot.exists()){

                    String phone = "",
                            name = "";
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){ // untuk menampilkan semua data
                        if(childSnapshot.child("phone").getValue()!=null)
                            Log.i("ada data ", childSnapshot.child("phone").getValue().toString());
                        phone = childSnapshot.child("phone").getValue().toString();

                        if(childSnapshot.child("name").getValue()!=null)
                            name = childSnapshot.child("name").getValue().toString();


                        UserObject mUser = new UserObject(name, phone);

                        if (name.equals(phone)){

                            for (UserObject mContacIterator : contactList) {

                                if(mContacIterator.getPhone().equals(mUser.getPhone())){
                                    mUser.setName(mContacIterator.getName());
                                }

                            }
                        }
                        userList.add(mUser);
                        mUserListAdapter.notifyDataSetChanged();
                        return;
                    }
                } else {
                    Log.i("tidak ada databos", "salahko");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("WrongConstant")
    private void initializeRecylerView() {

        mUserList = findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL , false);
        mUserList.setLayoutManager(mUserListLayoutManager);
        mUserListAdapter = new UserListAdapter(userList);
        mUserList.setAdapter(mUserListAdapter);
    } // akhir inicial


}
