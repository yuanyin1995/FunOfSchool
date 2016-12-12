package com.funOfSchool.ui;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funOfSchool.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Aiome on 2016/12/2.
 */

public class ConversationListFragment extends EaseConversationListFragment {
    private TextView errorText;
    private RelativeLayout searchBar;

    private Boolean isGuider = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }

    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(),R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        //隐藏titlebar
        hideTitleBar();

    }


    @Override
    protected void setUpView() {
        super.setUpView();
    }

    public void isGuiderConversationList(Boolean b){
        isGuider = b;
    }

    @Override
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        Iterator<String> iter = conversations.keySet().iterator();
        Log.i("tag",conversations.size()+"");
        //区分导游与出游者聊天会话
        if (isGuider){
            while (iter.hasNext()){
                String key = iter.next();
                List<EMMessage> msgs = conversations.get(key).loadMoreMsgFromDB(conversations.get(key).getLastMessage().getMsgId(),
                        500);
                int guiderMsg = 0;
                for (int i = 0; i < msgs.size(); i++){
                    Log.i("tag",(i+1) + ":" + msgs.get(i).getStringAttribute("guider","0") + "----msgSize:" +  msgs.size());
                    if (msgs.get(i).getStringAttribute("guider","0").equals("1")){
                        guiderMsg++;
                    }
                }
                //如果msg中包不含 1 就删除
                if (guiderMsg <= 0){
                    iter.remove();
                }
                Log.i("tag","guiderSize:" + guiderMsg);
            }
        } else {
            while (iter.hasNext()){
                String key = iter.next();
                List<EMMessage> msgs = conversations.get(key).loadMoreMsgFromDB(conversations.get(key).getLastMessage().getMsgId(),
                        500);
                int guiderMsg = 0;
                for (int i = 0; i < msgs.size(); i++){
                    Log.i("tag",(i+1) + ":" + msgs.get(i).getStringAttribute("guider","0") + "----msgSize:" +  msgs.size());
                    if (msgs.get(i).getStringAttribute("guider","0").equals("1")){
                        guiderMsg++;
                    }
                }
                //如果msg中包含 1 就删除
                if (guiderMsg > 0){
                    iter.remove();
                }
                Log.i("tag","guiderSize:" + guiderMsg);
            }
        }
        Log.i("tag",conversations.size()+"");

        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())){
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }
}
