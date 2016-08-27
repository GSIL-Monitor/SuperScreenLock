package com.hzp.superscreenlock.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hzp.superscreenlock.R;
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
        switch (displayType) {
            case DISPLAY_TYPE_NONE:
//                break;
            case DISPLAY_TYPE_PASSWORD:
                // TODO: 2016/8/26 初始化密码解锁
//                break;
            case DISPLAY_TYPE_PATTERN:
                resId = R.layout.fragment_unlock_pattern;

                break;
            default:
                throw new IllegalArgumentException("wrong unlock display type");
        }
        View view = inflater.inflate(resId, container, false);

        switch (displayType) {
            case DISPLAY_TYPE_NONE:
//                break;
            case DISPLAY_TYPE_PASSWORD:
                // TODO: 2016/8/26 初始化密码解锁
//                break;
            case DISPLAY_TYPE_PATTERN:
                initPatternLock(view);
                break;
            default:
                throw new IllegalArgumentException("wrong unlock display type");
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }

    private void initPatternLock(View view) {
        Lock9View lock9View = (Lock9View) view.findViewById(R.id.unlock_pattern_view);
        lock9View.setCallBack(new Lock9View.CallBack() {
            @Override
            public void onFinish(String password) {

                Toast.makeText(getActivity(), password, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
