package com.funOfSchool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.funOfSchool.R;
import com.funOfSchool.ui.http.AsyncHttpMangers;
import com.funOfSchool.util.AppUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Aiome on 2016/12/2.
 */

public class ConversationListActivity extends AppCompatActivity {
    private EMMessageListener emMessageListener;
    private ConversationListFragment conversationListFragment;
    private List<ConversationListFragment> mFragmentList;
    private AsyncHttpResponseHandler handler;
    private TextView mTouristTv;
    private TextView mGuiderTv;

    private ImageView mBackBtn;
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
                        serverMsg.setAttribute("serverMsg",true);
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
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        handler = new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                AppUtils.showShort(getApplicationContext(), "网络连接失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                AppUtils.Log("网络请求成功");
                try {
                    int code = response.getInt("code");
                    if (422 == code){
                        TokenError.Login(getApplicationContext());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        AsyncHttpMangers.getUserList(getApplicationContext(),handler);
    }

    private void initView() {
        mTouristTv = (TextView) findViewById(R.id.fragment_conversation_tourist);
        mGuiderTv = (TextView) findViewById(R.id.fragment_conversation_guider);
        mBackBtn = (ImageView) findViewById(R.id.receive_back);
    }

    //初始化导游会话列表
    private void initGuiderFragment() {
        conversationListFragment = new ConversationListFragment();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent = new Intent(ConversationListActivity.this, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName());
                intent.putExtra("type","guider");
                startActivity(intent);
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
                Intent intent = new Intent(ConversationListActivity.this, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName());
                intent.putExtra("type","user");
                startActivity(intent);
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.activity_conversation, conversationListFragment).commit();
    }

    /**
     * tab点击事件,切换会话列表
     */
    View.OnClickListener tabListener = new View.OnClickListener() {
        private void resetTextView(){
            mTouristTv.setTextColor(0xff010101);
            mGuiderTv.setTextColor(0xff010101);
        }

        @Override
        public void onClick(View v) {
            resetTextView();
            Map<String,EMConversation> list = EMClient.getInstance().chatManager().getAllConversations();
            Iterator<EMConversation> iter = list.values().iterator();
            switch (v.getId()){
                case R.id.fragment_conversation_tourist:
                    mTouristTv.setTextColor(0xff4E6CEF);
                    while (iter.hasNext()){
                        EMConversation conversation = iter.next();
                        EMClient.getInstance().chatManager().deleteConversation(conversation.getUserName(),false);
                    }
                    initTouristFragment();
                    AsyncHttpMangers.getUserList(getApplicationContext(),handler);
                    break;
                case R.id.fragment_conversation_guider:
                    mGuiderTv.setTextColor(0xff4E6CEF);
                    while (iter.hasNext()){
                        EMConversation conversation = iter.next();
                        EMClient.getInstance().chatManager().deleteConversation(conversation.getUserName(),false);
                    }
                    initGuiderFragment();
                    AsyncHttpMangers.getGuiderList(getApplicationContext(),handler);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        initTouristFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
