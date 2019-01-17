package org.municipal.myapplication.sr.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.municipal.myapplication.R;

public class FragmentComplain extends Fragment{
    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_complain,container,false);
        Toast.makeText(getActivity(),"Fragment",Toast.LENGTH_LONG).show();
        return v;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
