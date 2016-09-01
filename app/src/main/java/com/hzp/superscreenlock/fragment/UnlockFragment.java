package com.hzp.superscreenlock.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.locker.LockManager;
import com.hzp.superscreenlock.utils.SystemUtil;
import com.hzp.superscreenlock.view.Lock9View;


public class UnlockFragment extends Fragment {

    public static final int DISPLAY_TYPE_NONE = 1;//无类型解锁（后期可能改为滑动解锁）
    public static final int DISPLAY_TYPE_PASSWORD = 2;//密码解锁界面
    public static final int DISPLAY_TYPE_PATTERN = 3;//九宫格解锁界面

    public static final String ARGS_DISPLAY_TYPE = "ARGS_DISPLAY_TYPE";
    private int displayType = DISPLAY_TYPE_NONE;

    public UnlockFragment() {
        // Required empty public constructor
    }


    public static UnlockFragment newInstance(int type) {
        UnlockFragment fragment = new UnlockFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_DISPLAY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.displayType = getArguments().getInt(ARGS_DISPLAY_TYPE, DISPLAY_TYPE_NONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int resId = 0;
        //选择对应的资源文件
        switch (displayType) {
//            case DISPLAY_TYPE_NONE:
//                break;
            case DISPLAY_TYPE_PASSWORD:
                resId = R.layout.fragment_unlock_password;
                break;
            case DISPLAY_TYPE_PATTERN:
                resId = R.layout.fragment_unlock_pattern;
                break;
            default:
                throw new IllegalArgumentException("wrong unlock display type");
        }
        View view = inflater.inflate(resId, container, false);

        //选择进行对应的初始化
        switch (displayType) {
//            case DISPLAY_TYPE_NONE:
            case DISPLAY_TYPE_PASSWORD:
                initPasswordLock(view);
                break;
            case DISPLAY_TYPE_PATTERN:
                initPatternLock(view);
                break;
            default:
                throw new IllegalArgumentException("wrong unlock display type");
        }

        return view;
    }

    private void initPasswordLock(View view) {
        final EditText passwordEditText = (EditText) view.findViewById(R.id.editText_password_unlock);
        passwordEditText.setCursorVisible(false);
//        passwordEditText.getBackground().setAlpha(0);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s) || s.length()<4){
                    return;
                }
                if(LockManager.getInstance().verifyPassword(String.valueOf(s))){
                    LockManager.getInstance().unlockScreen();
                }else{
                    Toast.makeText(getContext(),"密码错误!",Toast.LENGTH_SHORT).show();
                    passwordEditText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initPatternLock(View view) {
        Lock9View lock9View = (Lock9View) view.findViewById(R.id.unlock_pattern_view);
        lock9View.setCallBack(new Lock9View.CallBack() {
            @Override
            public void onFinish(String password) {
                if(LockManager.getInstance().verifyPatternPassword(password)){
                    LockManager.getInstance().unlockScreen();
                }else{
                    Toast.makeText(getContext(),"手势错误!",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(displayType==DISPLAY_TYPE_PASSWORD){
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if(displayType==DISPLAY_TYPE_PASSWORD){
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken()
                    ,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
