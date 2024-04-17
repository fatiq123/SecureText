package com.example.securetext.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.securetext.R;

public class AboutUsFragment extends Fragment {

    private WebView webView;


    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        webView = view.findViewById(R.id.githubWebView);

        String githubUrl = "https://github.com/fatiq123/SecureText";
        webView.loadUrl(githubUrl);

        return view;
    }
}