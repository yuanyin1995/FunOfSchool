package com.funOfSchool.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funOfSchool.R;
import com.funOfSchool.ui.http.AsyncHttpMangers;
import com.funOfSchool.util.AppUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aiome on 2016/11/29.
 */

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper{
    private FrameLayout chooseFragment;
    private View chooseView;

    private TextView timeTv;
    private TextView remarkTv;
    private View acceptBtn;
    private View refuseBtn;

    private ImageView backIv;
    private ImageView infoIv;
    private TextView titleTv;
    private RelativeLayout rlTitle;


    private String title = "";
    private boolean type;

    private AsyncHttpResponseHandler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);
        backIv = (ImageView) view.findViewById(R.id.chat_back);
        infoIv = (ImageView) view.findViewById(R.id.chat_info);
        titleTv = (TextView) view.findViewById(R.id.chat_name);
        setTitleHeight(view);
        return view;
    }

    /**
     * 设置标题栏高度
     */
    private void setTitleHeight(View view) {
        rlTitle = (RelativeLayout)view.findViewById(R.id.chat_title);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int wHeight = wm.getDefaultDisplay().getHeight();
        int tHeight = wHeight /11;
        rlTitle.setMinimumHeight(tHeight);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        chooseFragment = (FrameLayout) getView().findViewById(R.id.fragment_isAccept);
        chooseView = LinearLayout.inflate(getContext(),R.layout.fragment_chat_choose,null);
        initChooseView();
        chooseFragment.addView(chooseView);

        hideTitleBar();
        hideRemark();
    }
    public void setTitle( String title){
        titleTv.setText(title);
    }


    @Override
    protected void setUpView() {
        setChatFragmentListener(this);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        infoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PersonInfoActivity.class);
                intent.putExtra("userId",getToChatWith());
                startActivity(intent);
            }
        });
        super.setUpView();
    }

    /**
     * 设置出游时间
     * @param time
     */
    public void setTime(String time){
        timeTv.setText(time);
    }
    public void setType(boolean b){
        type = b;
    }

    /**
     * 设置备注信息
     * @param remark
     */
    public void setRemark(String remark){
        remarkTv.setText(remark);
    }
    private void initChooseView() {
        timeTv = (TextView) chooseView.findViewById(R.id.fragment_chat_time);
        remarkTv = (TextView) chooseView.findViewById(R.id.fragment_chat_remark);
        refuseBtn = chooseView.findViewById(R.id.fragment_chat_refuse);
        acceptBtn = chooseView.findViewById(R.id.fragment_chat_accept);

        mHandler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 20){
                        AppUtils.showShort(getContext(),response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        refuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type){
                    AsyncHttpMangers.userChoose(getContext(),0,getToChatWith(),mHandler);
                    AppUtils.showShort(getContext(),"拒绝");
                }else {
                    AsyncHttpMangers.guiderChoose(getContext(),0,getToChatWith(),mHandler);
                    AppUtils.showShort(getContext(),"拒绝");
                }
            }
        });
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type){
                    AsyncHttpMangers.userChoose(getContext(),1,getToChatWith(),mHandler);
                    AppUtils.showShort(getContext(),"接受");
                }else {
                    AsyncHttpMangers.guiderChoose(getContext(),1,getToChatWith(),mHandler);
                    AppUtils.showShort(getContext(),"接受");
                }
            }
        });
    }

    /**
     * 显示匹配
     */
    public void showRemark(){
        if (chooseFragment != null){
            chooseFragment.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏
     */
    public void hideRemark(){
        if (chooseFragment != null){
            chooseFragment.setVisibility(View.GONE);
        }
    }

    public String getToChatWith(){
        return getArguments().getString(EaseConstant.EXTRA_USER_ID);
    }

    @Override
    protected void onMessageListInit() {
        super.onMessageListInit();
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if (!message.getBooleanAttribute("serverMsg",false)){
            message.setAttribute("avatar",AppUtils.AVATAR);
        }
    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {
        Intent intent = new Intent(getActivity(),PersonInfoActivity.class);
        intent.putExtra("userId",username);
        startActivity(intent);
    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }


    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {

            return 2;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if(message.getType() == EMMessage.Type.TXT){
                //服务器消息
                if (message.getBooleanAttribute("serverMsg", false)){
                    return message.direct() == EMMessage.Direct.RECEIVE ? 2 : 1;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if(message.getType() == EMMessage.Type.TXT){
                // 判断是不是服务器消息
                if (message.getBooleanAttribute("serverMsg", false)){
                    return new ServerMsgChatTow(getActivity(), message, position, adapter);
                }
            }
            return null;
        }

    }
}
