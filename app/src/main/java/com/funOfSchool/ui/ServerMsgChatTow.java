package com.funOfSchool.ui;

import android.content.Context;
import android.widget.BaseAdapter;

import com.funOfSchool.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;

/**
 * Created by Aiome on 2016/12/19.
 */

public class ServerMsgChatTow extends EaseChatRow {

    public ServerMsgChatTow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
                //判断是server透传消息操作会话列表还是其他
        Boolean flag = message.getBooleanAttribute("serverMsg",false);
        if (flag) {
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    R.layout.ease_row_received_message : R.layout.temp, this);
        }
    }

    @Override
    protected void onFindViewById() {

    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {

    }

    @Override
    protected void onBubbleClick() {

    }
}
