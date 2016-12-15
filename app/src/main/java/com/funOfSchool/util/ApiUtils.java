package com.funOfSchool.util;

/**
 * Created by Aiome on 2016/12/12.
 */

public class ApiUtils {
    /**
     * 出游者确认导游
     */
    public static final String API_MATCH_USER_CONFIRMATION = "api/match/matchEnd";

    /**
     * 出游者确认导游
     */
    public static final String API_MATCH_GUIDER_CONFIRMATION = "api/match/guider";

    /**
     * 获取出游者聊天列表
     */
    public static final String API_MATCH_USER_CHAT_LIST = "api/match/userChatList";

    /**
     * 获取导游聊天列表
     */
    public static final String API_MATCH_GUIDER_CHAT_LIST = "api/match/guiderChatList";

    /**
     * 出游者删除导游聊天列表
     */
    public static final String API_MATCH_USER_CHAT_DELETE = "api/match/userDelGuider";

    /**
     * 导游删除出游者聊天列表
     */
    public static final String API_MATCH_GUIDER_CHAT_DELETE = "api/match/guiderDelUser";
    /**
     * 获取出游者remark
     */
    public static final String API_MATCH_USER_CHAT_REMARK = "api/match/userGetRemark";
    /**
     * 获取导游remark
     */
    public static final String API_MATCH_GUIDER_CHAT_REMARK = "api/match/guiderGetRemark";
}
