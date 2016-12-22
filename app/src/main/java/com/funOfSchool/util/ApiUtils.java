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
     * 导游确认出游者
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
    /**
     * 获取用户当前状态
     */
    public static final String API_USER_STATUS = "api/account/getStatus";
    /**
     * 获取学校列表
     */
    public static final String API_COLLEGE_NAMELIST = "api/college/searchCollege";
    /**
     * 获取所选学校的经纬度和ID和景点
     */
    public static final String API_COLLEGE_LALO_SCENE = "api/college/searchLaAndLoAndScene";
    /**
     * 出游者发送请求
     */
    public static final String API_MATCH_START_MATCH = "api/match/start";
    /**
     * 开始旅程（记录路径）
     */
    public static final String API_MATCH_START_TRAVEL_WITH_TRACE = "api/match/travelStartwithTrace";
    /**
     * 开始旅程（不记录路径）
     */
    public static final String API_MATCH_START_TRAVEL_WITHOUT_TRACE = "api/match/travelStartwithoutTrace";
    /**
     * 结束旅程
     */
    public static final String API_MATCH_END_TRAVEL = "api/match/travelEnd";
    /**
     * 查询专业
     */
    public static final String API_COLLEGE_MARJOR = "api/college/searchMajor";
    /**
     * 获得出游列表
     */
    public static final String API_TRAVEL_LIST = "api/travel/getTravelList";
    /**
     * 根据学校ID获得学校名称
     */
    public static final String API_COLLEGE_NAME = "api/college/schoolName";
    /**
     * 验证token是否有效
     */
    public static final String API_IS_TOKEN = "api/account/isToken";
    /**
     * 获得我的奖品列表
     */
    public static final String API_MY_PRIZE_LIST = "api/prize/getAllocatePrizeList";
    /**
     * 删除已使用的卡券
     */
    public static final String API_DELETE_PRIZE = "api/prize/manageUserPrize";
    /**
     * 获得学校评论列表
     */
    public static final String API_COLLEGE_COMMENT = "api/comment/getCollegeComment";
}
