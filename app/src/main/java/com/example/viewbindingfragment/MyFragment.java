package com.example.viewbindingfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.viewbindingfragment.databinding.FragmentMyBinding;

public class MyFragment extends Fragment {
    // đối tượng View Binding
    private FragmentMyBinding mFragmentMyBinding;

    // đối tượng instance View của Fragment
    private View mView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentMyBinding = FragmentMyBinding.inflate(inflater, container, false);

        mFragmentMyBinding.tvName.setText("TEST VIEW BINDING IN FRAGMENT");

        // khởi tạo đối tượng View và return về cho Fragment
        mView = mFragmentMyBinding.getRoot();
        return mView;
    }
}