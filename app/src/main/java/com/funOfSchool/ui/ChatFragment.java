package com.funOfSchool.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funOfSchool.R;
import com.funOfSchool.util.AppUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

import java.util.List;

/**
 * Created by Aiome on 2016/11/29.
 */

public class ChatFragment extends EaseChatFragment {
    private FrameLayout chooseFragment;
    private View chooseView;

    private TextView timeTv;
    private TextView remarkTv;
    private View acceptBtn;
    private View refuseBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat,container,false);
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
        hideRemark();
    }

    @Override
    protected void setUpView() {
        super.setUpView();
    }

    /**
     * 设置出游时间
     * @param time
     */
    public void setTime(String time){
        timeTv.setText(time);
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

        refuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showShort(getContext(),"refuse");
            }
        });
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showShort(getContext(),"accept");
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
    public void onMessageReceived(List<EMMessage> messages) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername);
        Long last = Long.valueOf(conversation.getLastMessage().getMsgId()) - 1;
        if(!conversation.getMessage(last.toString(),false).getBooleanAttribute("show",true)){
//            Toast.makeText(getContext(),conversation.getLastMessage().getMsgId(),Toast.LENGTH_SHORT).show();
            conversation.removeMessage(last.toString());

        }
        super.onMessageReceived(messages);

    }
    @Override
    protected void sendTextMessage(String content) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername);

        if(!conversation.getLastMessage().getBooleanAttribute("show",true)){
//            Toast.makeText(getContext(),conversation.getLastMessage().getMsgId(),Toast.LENGTH_SHORT).show();
            conversation.removeMessage(conversation.getLastMessage().getMsgId());

        }
        super.sendTextMessage(content);
    }


}
