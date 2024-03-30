package com.example.securetext.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.securetext.Adapter.MessageAdapter;
import com.example.securetext.Database.RoomDB;
import com.example.securetext.Model.Message;
import com.example.securetext.R;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {

    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    RoomDB database;
    List<Message> messageList = new ArrayList<>();
    List<Message> tempList = null;

    EditText txtSearch;
    RelativeLayout relativeLayout;


    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_message, container, false);


        // initializing all variables
        relativeLayout = view.findViewById(R.id.layout_message);
        recyclerView = view.findViewById(R.id.rvMessage);
        txtSearch = view.findViewById(R.id.txtSearch);

        // getting instance of room db
        database = RoomDB.getInstance(getActivity());

        // getting all messages from database
        messageList = database.mainDao().getAllMessages();

        // Setting up RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        messageAdapter = new MessageAdapter(messageList, getContext(), database);
        recyclerView.setAdapter(messageAdapter);



        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempList = new ArrayList<>();
                for (int data=0; data<messageList.size(); data++) {
                    if (messageList.get(data).getPtext().toLowerCase().startsWith(txtSearch.getText().toString().toLowerCase())){
                        tempList.add(messageList.get(data));
                    }
                }
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
                messageAdapter = new MessageAdapter(tempList, getActivity(), database);
                recyclerView.setAdapter(messageAdapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }
}