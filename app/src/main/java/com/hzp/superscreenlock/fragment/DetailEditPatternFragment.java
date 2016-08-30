package com.hzp.superscreenlock.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.utils.LogUtil;
import com.hzp.superscreenlock.utils.PreferencesUtil;
import com.hzp.superscreenlock.utils.SystemUtil;
import com.hzp.superscreenlock.view.Lock9View;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailEditPatternFragment extends Fragment implements Lock9View.CallBack {

    private static final String TAG = DetailEditPatternFragment.class.getSimpleName();
    private String hint1,hint2;
    private Lock9View lock9View;
    private TextView hintTextView;

    private String password;

    public DetailEditPatternFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hint1 = getString(R.string.pattern_edit_hint_first);
        hint2=getString(R.string.pattern_edit_hint_second);

        View view =  inflater.inflate(R.layout.fragment_detail_edit_pattern, container, false);

        lock9View = (Lock9View) view.findViewById(R.id.pattern_edit_lock9view);
        lock9View.setCallBack(this);

        hintTextView = (TextView) view.findViewById(R.id.pattern_edit_hint_text);

        return view;
    }

    @Override
    public void onFinish(String password) {
        if(this.password==null){//第一次输入
            this.password = password;
            hintTextView.setText(hint2);
        }else{//第二次检查
            if(!this.password.equals(password)){
                Toast.makeText(getContext(),"两次设置的手势不一致，请重新设置!",Toast.LENGTH_SHORT).show();
                this.password = null;
                hintTextView.setText(hint1);
            }else{
                String e = SystemUtil.encryptString(this.password);
                PreferencesUtil.putString(getContext(),
                        PreferencesUtil.KEY_ENV_PATTERN,
                        e
                );
                LogUtil.i(TAG,"Put temp pattern item{"+ e + " }");
                getActivity().finish();
            }
        }
    }
}
