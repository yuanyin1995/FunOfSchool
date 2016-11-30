package com.funOfSchool.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funOfSchool.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;

import java.util.List;

/**
 * Created by Aiome on 2016/11/29.
 */

public class ChatFragment extends EaseChatFragment {
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
    }

    @Override
    protected void setUpView() {
        super.setUpView();
    }

    @Override
    protected void registerExtendMenuItem() {
        super.registerExtendMenuItem();
    }

    @Override
    protected void onConversationInit() {
        super.onConversationInit();
    }

    @Override
    protected void onMessageListInit() {
        super.onMessageListInit();
    }

    @Override
    protected void setListItemClickListener() {
        super.setListItemClickListener();
    }

    @Override
    protected void setRefreshLayoutListener() {
        super.setRefreshLayoutListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onChatRoomViewCreation() {
        super.onChatRoomViewCreation();
    }

    @Override
    protected void addChatRoomChangeListenr() {
        super.addChatRoomChangeListenr();
    }

    @Override
    protected void showChatroomToast(String toastContent) {
        super.showChatroomToast(toastContent);
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        super.onMessageReceived(messages);
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> messages) {
        super.onMessageReadAckReceived(messages);
    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> messages) {
        super.onMessageDeliveryAckReceived(messages);
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        super.onMessageChanged(emMessage, change);
    }

    @Override
    protected void inputAtUsername(String username, boolean autoAddAtSymbol) {
        super.inputAtUsername(username, autoAddAtSymbol);
    }

    @Override
    protected void inputAtUsername(String username) {
        super.inputAtUsername(username);
    }

    @Override
    protected void sendTextMessage(String content) {
        super.sendTextMessage(content);
    }

    @Override
    protected void sendBigExpressionMessage(String name, String identityCode) {
        super.sendBigExpressionMessage(name, identityCode);
    }

    @Override
    protected void sendVoiceMessage(String filePath, int length) {
        super.sendVoiceMessage(filePath, length);
    }

    @Override
    protected void sendImageMessage(String imagePath) {
        super.sendImageMessage(imagePath);
    }

    @Override
    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        super.sendLocationMessage(latitude, longitude, locationAddress);
    }

    @Override
    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        super.sendVideoMessage(videoPath, thumbPath, videoLength);
    }

    @Override
    protected void sendFileMessage(String filePath) {
        super.sendFileMessage(filePath);
    }

    @Override
    protected void sendMessage(EMMessage message) {
        super.sendMessage(message);
    }

    @Override
    public void resendMessage(EMMessage message) {
        super.resendMessage(message);
    }

    @Override
    protected void sendPicByUri(Uri selectedImage) {
        super.sendPicByUri(selectedImage);
    }

    @Override
    protected void sendFileByUri(Uri uri) {
        super.sendFileByUri(uri);
    }

    @Override
    protected void selectPicFromCamera() {
        super.selectPicFromCamera();
    }

    @Override
    protected void selectPicFromLocal() {
        super.selectPicFromLocal();
    }

    @Override
    protected void emptyHistory() {
        super.emptyHistory();
    }

    @Override
    protected void toGroupDetails() {
        super.toGroupDetails();
    }

    @Override
    protected void hideKeyboard() {
        super.hideKeyboard();
    }

    @Override
    protected void forwardMessage(String forward_msg_id) {
        super.forwardMessage(forward_msg_id);
    }

    @Override
    public void setChatFragmentListener(EaseChatFragmentHelper chatFragmentHelper) {
        super.setChatFragmentListener(chatFragmentHelper);
    }
}
