package com.funOfSchool.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funOfSchool.R;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

/**
 * Created by Aiome on 2016/12/2.
 */

public class ConversationListFragment extends EaseConversationListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }
}
