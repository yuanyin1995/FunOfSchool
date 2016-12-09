package com.funOfSchool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.funOfSchool.R;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aiome on 2016/12/2.
 */

public class ConversationListActivity extends AppCompatActivity {
    private EMMessageListener emMessageListener;
    private ConversationListFragment conversationListFragment;
    private List<ConversationListFragment> mFragmentList;
    private TextView mTouristTv;
    private TextView mGuiderTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        emMessageListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息----刷新一下当前页面喽
                conversationListFragment.refresh();
                EMClient.getInstance().chatManager().importMessages(messages);//保存到数据库
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                String userName = null;
                List<EMMessage> msg = new ArrayList<>();
                EMMessage serverMsg = null;
                for (EMMessage message : messages) {
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    String action = cmdMsgBody.action();//获取自定义action
                    if (action.equals("show")){
                        Log.i("tag",cmdMsgBody.action());
                        userName = message.getFrom();
                        serverMsg = EMMessage.createTxtSendMessage("   ",userName);
                        serverMsg.setAttribute("show",false);
                        msg.add(serverMsg);
                    }
                }
                Log.i("tag",""+EMClient.getInstance().chatManager().getAllConversations().size());
                EMConversation conversation = EMClient.getInstance().chatManager()
                        .getConversation(userName, EMConversation.EMConversationType.Chat,true);
                conversation.insertMessage(serverMsg);
                conversationListFragment.addConversation(conversation);
//                EMClient.getInstance().chatManager().importMessages(msg);
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
        initView();
        initTouristFragment();
        setUpView();
    }

    private void setUpView() {
        mTouristTv.setOnClickListener(tabListener);
        mGuiderTv.setOnClickListener(tabListener);
    }

    private void initView() {
        mTouristTv = (TextView) findViewById(R.id.fragment_conversation_tourist);
        mGuiderTv = (TextView) findViewById(R.id.fragment_conversation_guider);
    }

    //初始化导游会话列表
    private void initGuiderFragment() {
        conversationListFragment = new ConversationListFragment();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(ConversationListActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
            }
        });
        conversationListFragment.isGuiderConversationList(true);
        getSupportFragmentManager().beginTransaction().add(R.id.activity_conversation, conversationListFragment).commit();
    }
    //初始化出游者列表
    private void initTouristFragment() {
        conversationListFragment = new ConversationListFragment();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(ConversationListActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.activity_conversation, conversationListFragment).commit();
    }

    View.OnClickListener tabListener = new View.OnClickListener() {
        private void resetTextView(){
            mTouristTv.setTextColor(0xff010101);
            mGuiderTv.setTextColor(0xff010101);
        }


        @Override
        public void onClick(View v) {
            resetTextView();
            switch (v.getId()){
                case R.id.fragment_conversation_tourist:
                    mTouristTv.setTextColor(0xff4E6CEF);
                    initTouristFragment();
                    break;
                case R.id.fragment_conversation_guider:
                    mGuiderTv.setTextColor(0xff4E6CEF);
                    initGuiderFragment();
                    break;
                default:
                    break;
            }
        }


    };
}
