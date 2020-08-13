package com.newtec.company.common.common;

import com.newtec.myqdp.server.utils.StringUtils;
import com.newtec.myqdp.server.utils.exception.CustomException;
import com.newtec.myqdp.server.utils.properties.PropertiesFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyRParam {
	private static Map<String, String> paramValueMap = new HashMap();
	public static String PATH = "company-rpc-dev.properties";
	private static String COMPANY_DB = "companyDB";
	private static String PAY_NOTIFY_URL = "payNotifyUrl";
	private static String REFUND_NOTIFY_URL = "refundNotifyUrl";
	private static String ORDER_MANAGER_TIME = "orderManagerTime";
	private static String START_MP_PAYID = "startMpPayId";
	private static String WEIXIN_PATH = "weixinPath";
	private static String HTML_URL = "htmlUrl";
	private static String STATISTICS_ID = "statisticsId";
	private static String MAX_BET_MONEY = "maxBetMoney";
	private static String LOCATION_KEY = "locationKey";
	private static String BAIDU_LOCATION_KEY = "baiduLocationKey";
	private static String IS_BET_CENTRE = "isBetCentre";
	public static boolean betCentre = false;
	private static String CHANNEL_ID = "channelId";
	private static String URL = "url";
	private static String PFX_PATH = "pfxPath";
	private static String PASSWORD = "password";
	private static String DOWN_LOAD_FILE_PATH = "downloadFilePath";
	private static String TERMINAL_ID = "terminalId";
	private static String TERMINAL_LIST = "terminalList";
	private static List<String> termianlList = new ArrayList();
	private static String MA_TEMPLATE_ID_PAY_SUCCESS = "maTemplateIdPaySuccess";
	private static String MA_TEMPLATE_ID_OPEN_LOTTERY = "maTemplateIdOpenLottery";
	private static String MA_TEMPLATE_PAY_SUCCESS_URL = "maTemplateIdPaySuccessUrl";
	private static String MA_TEMPLATE_OPEN_lOTTERY_URL = "maTemplateIdOpenLotteryUrl";
	private static String MP_TEMPLATE_ID_PAY_SUCCESS = "mpTemplateIdPaySuccess";
	private static String MP_TEMPLATE_ID_PAY_FAIL = "mpTemplateIdPayFAIL";
	private static String MP_TEMPLATE_ID_PAY_SUCCESS_URL = "mpTemplateIdPaySuccessUrl";
	private static String MP_TEMPLATE_ID_RECHARGE_SUCCESS = "mpTemplateIdRechargeSuccess";
	private static String MP_TEMPLATE_ID_CASH_SUCCESS = "mpTemplateIdCashSuccess";

	public static void initParam(String path) {
		if (StringUtils.isStrNull(path)) {
			path = PATH;
		} else {
			PATH = path;
		}

		new HashMap();

		try {
			Map<String, String> tempParamMap = PropertiesFile.properties2Map(path);
			paramValueMap.putAll(tempParamMap);
		} catch (CustomException var7) {
			var7.printStackTrace();
		}

		COMPANY_DB = (String) paramValueMap.get(COMPANY_DB);
		PAY_NOTIFY_URL = (String) paramValueMap.get(PAY_NOTIFY_URL);
		REFUND_NOTIFY_URL = (String) paramValueMap.get(REFUND_NOTIFY_URL);
		ORDER_MANAGER_TIME = (String) paramValueMap.get(ORDER_MANAGER_TIME);
		START_MP_PAYID = (String) paramValueMap.get(START_MP_PAYID);
		WEIXIN_PATH = (String) paramValueMap.get(WEIXIN_PATH);
		HTML_URL = (String) paramValueMap.get(HTML_URL);
		STATISTICS_ID = (String) paramValueMap.get(STATISTICS_ID);
		MAX_BET_MONEY = (String) paramValueMap.get(STATISTICS_ID);
		CHANNEL_ID = (String) paramValueMap.get(CHANNEL_ID);
		URL = (String) paramValueMap.get(URL);
		PFX_PATH = (String) paramValueMap.get(PFX_PATH);
		PASSWORD = (String) paramValueMap.get(PASSWORD);
		DOWN_LOAD_FILE_PATH = (String) paramValueMap.get(DOWN_LOAD_FILE_PATH);
		TERMINAL_ID = (String) paramValueMap.get(TERMINAL_ID);
		TERMINAL_LIST = (String) paramValueMap.get(TERMINAL_LIST);
		if (!StringUtils.isStrNull(TERMINAL_LIST)) {
			String[] terminalStr = TERMINAL_LIST.split(",");
			String[] var6 = terminalStr;
			int var5 = terminalStr.length;

			for (int var4 = 0; var4 < var5; ++var4) {
				String termianl = var6[var4];
				termianlList.add(termianl);
			}
		}

		LOCATION_KEY = (String) paramValueMap.get(LOCATION_KEY);
		BAIDU_LOCATION_KEY = (String) paramValueMap.get(BAIDU_LOCATION_KEY);
		MA_TEMPLATE_ID_PAY_SUCCESS = (String) paramValueMap.get(MA_TEMPLATE_ID_PAY_SUCCESS);
		MA_TEMPLATE_ID_OPEN_LOTTERY = (String) paramValueMap.get(MA_TEMPLATE_ID_OPEN_LOTTERY);
		MA_TEMPLATE_PAY_SUCCESS_URL = (String) paramValueMap.get(MA_TEMPLATE_PAY_SUCCESS_URL);
		MA_TEMPLATE_OPEN_lOTTERY_URL = (String) paramValueMap.get(MA_TEMPLATE_OPEN_lOTTERY_URL);
		MP_TEMPLATE_ID_PAY_SUCCESS = (String) paramValueMap.get(MP_TEMPLATE_ID_PAY_SUCCESS);
		MP_TEMPLATE_ID_PAY_FAIL = (String) paramValueMap.get(MP_TEMPLATE_ID_PAY_FAIL);
		MP_TEMPLATE_ID_PAY_SUCCESS_URL = (String) paramValueMap.get(MP_TEMPLATE_ID_PAY_SUCCESS_URL);
		MP_TEMPLATE_ID_RECHARGE_SUCCESS = (String) paramValueMap.get(MP_TEMPLATE_ID_RECHARGE_SUCCESS);
		MP_TEMPLATE_ID_CASH_SUCCESS = (String) paramValueMap.get(MP_TEMPLATE_ID_CASH_SUCCESS);
		betCentre = StringUtils.isTrue((String) paramValueMap.get(IS_BET_CENTRE));
	}

	public static String getChannelId() {
		return CHANNEL_ID;
	}

	public static String getTerminalId() {
		return TERMINAL_ID;
	}

	public static String getUrl() {
		return URL;
	}

	public static String getPfxPath() {
		return PFX_PATH;
	}

	public static String getPassword() {
		return PASSWORD;
	}

	public static String getDownloadFilePath() {
		return DOWN_LOAD_FILE_PATH;
	}

	public static String getCompanyDB() {
		return COMPANY_DB;
	}

	public static String getPayNotifyUrl() {
		return PAY_NOTIFY_URL;
	}

	public static String getRefundNotifyUrl() {
		return REFUND_NOTIFY_URL;
	}

	public static String getMaTemplateIdPaySuccess() {
		return MA_TEMPLATE_ID_PAY_SUCCESS;
	}

	public static String getMpTemplateIdPaySuccess() {
		return MP_TEMPLATE_ID_PAY_SUCCESS;
	}

	public static String getMaTemplateIdOpenLottery() {
		return MA_TEMPLATE_ID_OPEN_LOTTERY;
	}

	public static String getMaTemplatePaySuccessUrl() {
		return MA_TEMPLATE_PAY_SUCCESS_URL;
	}

	public static String getMaTemplateOpenLotteryUrl() {
		return MA_TEMPLATE_OPEN_lOTTERY_URL;
	}

	public static String getMpTemplateIdPayFail() {
		return MP_TEMPLATE_ID_PAY_FAIL;
	}

	public static String getMpTemplateIdPaySuccessUrl() {
		return MP_TEMPLATE_ID_PAY_SUCCESS_URL;
	}

	public static String getMpTemplateIdRechargeSuccess() {
		return MP_TEMPLATE_ID_RECHARGE_SUCCESS;
	}

	public static String getMpTemplateIdCashSuccess() {
		return MP_TEMPLATE_ID_CASH_SUCCESS;
	}

	public static int getOrderManagerTime() {
		return StringUtils.toInt(ORDER_MANAGER_TIME, 60);
	}

	public static String getWeiXinPath() {
		return WEIXIN_PATH;
	}

	public static String getHtmlUrl() {
		return HTML_URL;
	}

	public static boolean isStartMpPayId() {
		return StringUtils.isTrue(START_MP_PAYID);
	}

	public static String getStatisticsId() {
		return STATISTICS_ID;
	}

	public static String getLOCATION_KEY() {
		return LOCATION_KEY;
	}

	public static String getBaiduLocationKey() {
		return BAIDU_LOCATION_KEY;
	}

	public static String getMaxBetMoney() {
		return MAX_BET_MONEY;
	}

	public static List<String> getTermianlList() {
		return termianlList;
	}
}