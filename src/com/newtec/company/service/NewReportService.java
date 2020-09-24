package com.newtec.company.service;

import java.util.Map;

import com.newtec.router.request.FetchWebRequest;

public interface NewReportService {
	//中检一天所有公司本年经营情况总计
	Map<String, Object> findTotalByZhongjian(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception;  
    //各个公司本年经营情况总计
	Map<String, Object> findTotalByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	//各个区域本年经营情况总计
    Map<String, Object> findTotalByQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //中检一天营业收入同比
    Map<String, Object> findYysrTbByZhongjian(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //中检一天营业成本同比
    Map<String, Object> findYycbTbByZhongjian(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //中检一天营业利润同比
    Map<String, Object> findYylrTbByZhongjian(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //各个公司营业收入同比
    Map<String, Object> findYysrTbByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //各个公司营业成本同比
    Map<String, Object> findYycbTbByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //各个公司营业利润同比
    Map<String, Object> findYylrTbByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //各个区域营业收入同比
    Map<String, Object> findYysrTbByQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //各个公司营业成本同比
    Map<String, Object> findYycbTbByQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //各个公司营业利润同比
    Map<String, Object> findYylrTbByQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //中检一天业务实况
    Map<String, Object> findYyskByZhongjian(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //公司业务实况
    Map<String, Object> findYyskByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //区域业务实况
    Map<String, Object> findYyskByQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    //在京单位业务收入排名
    Map<String, Object> findYwsrByZj(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
  //在京单位业务成本排名
    Map<String, Object> findYwcbByZj(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
  //国内公司业务收入排名
    Map<String, Object> findYwsrByGn(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
  //国内公司业务成本排名
    Map<String, Object> findYwcbByGn(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
  //境外公司业务收入排名
    Map<String, Object> findYwsrByJw(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
  //境外公司业务成本排名
    Map<String, Object> findYwcbByJw(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findCompanyMsgByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findCompanyMsgByArea(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findCompanyMsgByCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findCompanyMsgByCpxName(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findCpxMsgByJsfl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findCpxMsgByDxfl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findCpxTableKh(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findJtgsTableKh(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findJtgsTableGs(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findJtgsTableCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYyjyqk(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskByCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskByCpxYhWtdl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

    public Map<String, Object> findYwskByCpxYhKgdl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

    public Map<String, Object> findYwskByCpxYhWgdl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

    public Map<String, Object> findYwskByCpxYhKhsl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

    public Map<String, Object> findYwskByCpxYhGyssl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

    public Map<String, Object> findYwskByCpxYhCzsl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findSzskByCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findSzskByCpxYhWtje(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

    public Map<String, Object> findSzskByCpxYhKgje(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

    public Map<String, Object> findSzskByCpxYhYcsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

    public Map<String, Object> findSzskByCpxYhYwsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

    public Map<String, Object> findSzskByCpxYhYwcb(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

    public Map<String, Object> findSzskByCpxYhMll(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

    public Map<String, Object> findSzskByCpxYhCb(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findSzByCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findFwjsByCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findJtgsTableGsQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findJtgsTableCpxQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findJtgsTableKhQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findCompanyMsgForChange(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findCompanyMsgByCompanyYh(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql1(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql2(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql3(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql4(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql5(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;    

    public Map<String, Object> findYwskMsgSql1JtWtdl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql1JtKgdl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql1JtWgdl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql1JtWtje(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql1JtYcsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
        
    public Map<String, Object> findYwskMsgSql2Jt(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql3Jt(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql4Jt(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql5Jt(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql1Quyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql2Quyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql3Quyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql4Quyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYwskMsgSql5Quyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYyjyqkJtgs(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
    
    public Map<String, Object> findYyjyqkJtgsQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
}
