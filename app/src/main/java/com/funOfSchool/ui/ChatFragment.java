package com.funOfSchool.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funOfSchool.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
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

    private String time = "";
    private String remark = "";

    private Boolean showChoose = true;
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

        if (showChoose){
            initChooseView();
            chooseFragment.addView(chooseView);
        }

    }
    public void setShowChoose(Boolean b){
        showChoose = b;
    }

    public void setTime(String time){
        this.time = time;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
    private void initChooseView() {
        timeTv = (TextView) chooseView.findViewById(R.id.fragment_chat_time);
        remarkTv = (TextView) chooseView.findViewById(R.id.fragment_chat_remark);
        timeTv.setText(time);
        remarkTv.setText(remark);
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
