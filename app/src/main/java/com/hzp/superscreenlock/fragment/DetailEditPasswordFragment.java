package com.hzp.superscreenlock.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.utils.LogUtil;
import com.hzp.superscreenlock.utils.PreferencesUtil;
import com.hzp.superscreenlock.utils.SystemUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailEditPasswordFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DetailEditPasswordFragment.class.getSimpleName();
    private EditText password1,password2;
    private Button okButton;

    public DetailEditPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_edit_password, container, false);

        password1 = (EditText) view.findViewById(R.id.password_edit_first);
        password2 = (EditText) view.findViewById(R.id.password_edit_second);
        okButton = (Button) view.findViewById(R.id.password_edit_ok);

        okButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        String p1 = password1.getText().toString();
        String p2 = password2.getText().toString();
        if(!p1.equals(p2)){
            password1.setText("");
            password2.setText("");
            Toast.makeText(getContext(),"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
        }else{
            String e = SystemUtil.encryptString(p1);
            PreferencesUtil.putString(getContext(),PreferencesUtil.KEY_ENV_PASSWORD,e);
            LogUtil.i(TAG,"Put temp password item{"+ e + " }");
            getActivity().finish();
        }
    }
}
