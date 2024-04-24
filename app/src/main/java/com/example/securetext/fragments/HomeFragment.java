package com.example.securetext.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.securetext.Database.RoomDB;
import com.example.securetext.EncryptDecrypt;
import com.example.securetext.Model.Key;
import com.example.securetext.Model.Message;
import com.example.securetext.R;
import com.example.securetext.Utility.BDUtility;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private MaterialButton btnEncrypt, btnDecrypt;
    private TextInputEditText textEncrypt, textDecrypt;

    // Button to go to Encryption Fragment
    private Button btnNext;


    RoomDB database;
    String key;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        database = RoomDB.getInstance(getActivity());
        textEncrypt = view.findViewById(R.id.textEncrypt);
        textDecrypt = view.findViewById(R.id.textDecrypt);
        btnEncrypt = view.findViewById(R.id.btnEncrypt);
        btnDecrypt = view.findViewById(R.id.btnDecrypt);


        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = textEncrypt.getText().toString();
                if (value != null && !value.isEmpty()) {
                    key = database.keyDao().getAllKey().get(0).getKey();
                    String encryptedText = EncryptDecrypt.encrypt(textEncrypt.getText().toString(), key);
                    textDecrypt.setText(encryptedText);

                    List<Key> keyList = database.keyDao().getAllKey();
                    Boolean saveMessage = keyList.get(0).getMessagebackup();

                    if (saveMessage) {
                        saveEncryptedMessage(encryptedText, textEncrypt.getText().toString());
                    }
                    BDUtility.setClipBoard(getContext(), textDecrypt.getText().toString());

                    textEncrypt.getText().clear();
                    Toast.makeText(getContext(), "Encrypted text copied to clipboard", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Field empty", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String value = textDecrypt.getText().toString();
                    if (value != null && !value.isEmpty()) {
                        key = database.keyDao().getAllKey().get(0).getKey();
                        String decryptedText = EncryptDecrypt.decrypt(value, key);
                        textEncrypt.setText(decryptedText);
                        textDecrypt.getText().clear();

                        BDUtility.setClipBoard(getContext(), textEncrypt.getText().toString());
                        Toast.makeText(getContext(), "Decrypted text copied to clipboard", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Field empty", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                    Toast.makeText(getContext(), "Invalid encrypted data", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view;
    }


    private void saveEncryptedMessage(String eText, String pText) {
        Message message = new Message();
        message.setEtext(eText);
        message.setPtext(pText);
        message.setCreationTime(new Date());
        database.mainDao().saveItem(message);
    }
}