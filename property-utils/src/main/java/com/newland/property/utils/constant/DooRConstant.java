package com.newland.property.utils.constant;

/**
 * 道尔道闸配置
 */
public class DooRConstant {

    public static final String DOOR_DOMAIN = "DOOR_CONFIG";

    public static final String CAR_IN_SYNC_TIME = "carInSyncTime";

    public static final String CAR_OUT_SYNC_TIME = "carOutSyncTime";
    public static final String CAR_IN_URL = "carInUrl";
    public static final String CAR_OUT_URL = "carOutUrl";
    public static final String TOKEN_URL = "tokenUrl";
    public static final String INOUT_IMG_URL = "inoutImgUrl";

    //创建月租车URL
    public static final String CREATE_MONTHLY_CAR_URL = "createMonthlyCarUrl";
    //续费月租车URL
    public static final String RENEW_MONTHLY_CAR_URL = "renewMonthlyCarUrl";
    //注销月租车URL
    public static final String DELETE_MONTHLY_CAR_URL = "deleteMonthlyCarUrl";
    //月租车信息URL
    public static final String DETAIL_MONTHLY_CAR_URL = "detailMonthlyCarUrl";
    //缴费历史URL
    public static final String FEE_HISTORY_URL = "feeHistoryUrl";
    //缴费套餐列表URL
    public static final String LONG_RENTAL_RATE = "longRentalRate";

    public static final String REDIS_PREFIX_SYNC_CAR_IN_TIME = "DOOR:SYNC_TIME:CAR_IN:";
    public static final String REDIS_PREFIX_SYNC_CAR_OUT_TIME = "DOOR:SYNC_TIME:CAR_OUT:";


    public static final String REDIS_PREFIX_TOKEN = "DOOR:TOKEN:";

}
