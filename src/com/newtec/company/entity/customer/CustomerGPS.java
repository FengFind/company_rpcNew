package com.newtec.company.entity.customer;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author
 * @Description 客户基础信息
 * @date 2019年9月2日
 * @version 1.0
 *
 */
@Entity
@Table(name = "t_customer")
public class CustomerGPS {
	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;
	/*
	 * 国别
	 */
	private String country;
	/*
	 * 国家名称
	 */
	@Column(name = "country_name")
	private String countryName;
	/*
	 * 国家代码
	 */
	@Column(name = "country_no")
	private String countryNo;

	/*
	 * 省、直辖市
	 */
	private String province;
	/*
	 * 市（省级）
	 */
	private String city;
	/*
	 * 县/直辖市区/市（省级）区
	 */
	private String county;
	/*
	 * 组织机构类型
	 */
	@Column(name = "institutional_type")
	private String institutionalType;
	/*
	 * 客户类型
	 */
	@Column(name = "enterprise_type")
	private String enterpriseType;
	/*
	 * 客户名称（企业名称/个人名称）
	 */
	@Column(name = "customer_name")
	private String customerName;
	/*
	 * 英文名称
	 */
	@Column(name = "english_name")
	private String englishName;

	/*
	 * 详细地址
	 */
	@Column(name = "detail_address")
	private String detailAddress;
	/*
	 * 风险等级
	 */
	@Column(name = "risk_level")
	private String riskLevel;
	/*
	 * 公司图片
	 */
	@Column(name = "cmpany_pic")
	private String cmpanyPic;
	
	/*
	 * 标签
	 */
	private String lable;

	private BigDecimal lat;
	
	private BigDecimal lng;


	public CustomerGPS() {
		super();
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getCountry() {
		return country;
	}



	public void setCountry(String country) {
		this.country = country;
	}



	public String getCountryName() {
		return countryName;
	}



	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}



	public String getCountryNo() {
		return countryNo;
	}



	public void setCountryNo(String countryNo) {
		this.countryNo = countryNo;
	}



	public String getProvince() {
		return province;
	}



	public void setProvince(String province) {
		this.province = province;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public String getCounty() {
		return county;
	}



	public void setCounty(String county) {
		this.county = county;
	}



	public String getInstitutionalType() {
		return institutionalType;
	}



	public void setInstitutionalType(String institutionalType) {
		this.institutionalType = institutionalType;
	}



	public String getEnterpriseType() {
		return enterpriseType;
	}



	public void setEnterpriseType(String enterpriseType) {
		this.enterpriseType = enterpriseType;
	}



	public String getCustomerName() {
		return customerName;
	}



	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}



	public String getEnglishName() {
		return englishName;
	}



	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}



	public String getDetailAddress() {
		return detailAddress;
	}



	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}



	public String getRiskLevel() {
		return riskLevel;
	}



	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}



	public String getCmpanyPic() {
		return cmpanyPic;
	}



	public void setCmpanyPic(String cmpanyPic) {
		this.cmpanyPic = cmpanyPic;
	}



	public String getLable() {
		return lable;
	}



	public void setLable(String lable) {
		this.lable = lable;
	}



	public BigDecimal getLat() {
		return lat;
	}



	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}



	public BigDecimal getLng() {
		return lng;
	}



	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

}
