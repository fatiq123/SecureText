package com.example.securetext.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.securetext.Dao.KeyDao;
import com.example.securetext.Database.RoomDB;
import com.example.securetext.EncryptDecrypt;
import com.example.securetext.R;
import com.example.securetext.Utility.BDUtility;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class EncryptionFragment extends Fragment {

    private Button btnDecryptText;
    private Button btnPlainText;
    private TextInputEditText txtEncrypt;

    RoomDB database;
    String key;

    private Timer timer = new Timer();

    public EncryptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_encryption, container, false);

        btnDecryptText = view.findViewById(R.id.btnDecryptText);
        btnPlainText = view.findViewById(R.id.btnPlainText);
        txtEncrypt = view.findViewById(R.id.txtEncrypt);


        // initialize room database
        database = RoomDB.getInstance(getActivity());

        btnDecryptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*try {

                    String enteredText = txtEncrypt.getText().toString();
                    if (enteredText != null && !enteredText.isEmpty()) {
                        key = database.keyDao().getAllKey().get(0).getKey();
                        String decryptedText = EncryptDecrypt.decrypt(enteredText, key);
                        txtEncrypt.setText(decryptedText);
                        // to copy automatically on clipboard
                        BDUtility.setClipBoard(getContext(), txtEncrypt.getText().toString());
                        Toast.makeText(getContext(), "Decrypted text copied to clipboard", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Please enter text. Field empty!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Invalid encrypted data", Toast.LENGTH_SHORT).show();
                }*/

                decryptText();

            }
        });


        txtEncrypt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*String plainText = s.toString();
                if (!plainText.isEmpty()) {
                    database = RoomDB.getInstance(getActivity());
                    key = database.keyDao().getAllKey().get(0).getKey();
                    String encryptedText = EncryptDecrypt.encrypt(plainText, key);
                    txtEncrypt.setText(encryptedText);

                }*/

                encryptText(s.toString());
            }
        });

        return view;
    }




    // Rx Java Code for Async Encryption and Decryption tasks
    Disposable disposable;

    private void decryptText() {
        disposable = Observable.just(txtEncrypt.getText().toString())
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        key = database.keyDao().getAllKey().get(0).getKey();
                        return EncryptDecrypt.decrypt(s, key);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        txtEncrypt.setText(s);
                        BDUtility.setClipBoard(getContext(), s);
                        Toast.makeText(getContext(), "Decrypted text copied to clipboard", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getContext(), "Invalid encrypted data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*private void encryptText(final String plainText) {
        disposable = Observable.just(plainText)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        key = database.keyDao().getAllKey().get(0).getKey();
                        return EncryptDecrypt.encrypt(s, key);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        txtEncrypt.setText(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getContext(), "Error encrypting text", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/


    private void encryptText(final String plainText) {
        disposable = Observable.just(plainText)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        key = database.keyDao().getAllKey().get(0).getKey();
                        StringBuilder encryptedText = new StringBuilder();
                        for (char c : s.toCharArray()) {
                            encryptedText.append(EncryptDecrypt.encrypt(String.valueOf(c), key));
                        }
                        return encryptedText.toString();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        txtEncrypt.setText(txtEncrypt.getText() + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getContext(), "Error encrypting text", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }


}