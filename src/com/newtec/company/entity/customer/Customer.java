package com.newtec.company.entity.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.newtec.myqdp.server.utils.StringUtils;

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
public class Customer {

	@Id
	@Column(name = "id")
	private String id;

	/*
	 * 企业数据库ID
	 */
	@Column(name = "bus_own_id")
	private String busOwnId;

	/*
	 * 国别
	 */
	@Column(name = "country")
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
	@Column(name = "province")
	private String province;
	/*
	 * 市（省级）
	 */
	@Column(name = "city")
	private String city;
	/*
	 * 县/直辖市区/市（省级）区
	 */
	@Column(name = "county")
	private String county;
	/*
	 * 街道/镇
	 */
	@Column(name = "town")
	private String town;
	
	
	/*
	 * 组织机构类型一级
	 */
	@Column(name = "institutional_type_super")
	private String institutionalTypeSuper;
	
	/*
	 * 组织机构类型二级
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
	 * 集团客户类型
	 */
	@Column(name = "customer_classification")
	private String customerClassification;
	/*
	 * 客户分类（集团专用）
	 */
	@Column(name = "customer_classification_group")
	private String customerGroup;
	/*
	 * 代码类型
	 */
	@Column(name = "code_type")
	private String codeType;
	/*
	 * 信用代码
	 */
	@Column(name = "credit_code")
	private String creditCode;

	/*
	 * 中检客户识别编码
	 */
	@Column(name = "ccic_customer_no")
	private String ccicCustomerNo;
	/*
	 * 企业网址
	 */
	@Column(name = "customer_website")
	private String customerWebsite;
	/*
	 * 人员规模
	 */
	@Column(name = "staff_size")
	private String staffSize;
	/*
	 * 法定代表人
	 */
	@Column(name = "legal_representative")
	private String legalRepresentative;

	/*
	 * 成立日期
	 */
	@Column(name = "establishment_date")
	private String establishmentDate;

	/*
	 * 注册资本
	 */
	@Column(name = "registered_capital")
	private String registeredCapital;
	/*
	 * 注册资本-币种
	 */
	@Column(name = "currency")
	private String currency;
	/*
	 * 经营范围
	 */
	@Column(name = "bussiness_scope")
	private String bussinessScope;
	/*
	 * 国民经济行业分类
	 */
	@Column(name = "national_industries_classification")
	private String classification;

	/*
	 * 行业分类
	 */
	@Column(name = "classification_industry")
	private String classificationIndustry;
	/*
	 * 行业代码
	 */
	@Column(name = "industries_code")
	private String industriesCode;
	/*
	 * 企业登记注册地址
	 */
	@Column(name = "registered_address")
	private String registeredAddress;
	/*
	 * 客户联络地址
	 */
	@Column(name = "contact_address")
	private String contactAddress;
	/*
	 * 详细地址
	 */
	@Column(name = "detail_address")
	private String detailAddress;
	/*
	 * 注册地址（英文）
	 */
	@Column(name = "registered_address_english")
	private String addressEnglish;
	/*
	 * 注册地址邮编
	 */
	@Column(name = "zip_code")
	private String zipCode;
	/*
	 * 上级客户Id
	 */
	@Column(name = "superior_customer_id")
	private String superiorId;
	/*
	 * 上级客户名称
	 */
	@Column(name = "superior_customer_name")
	private String superiorName;
	/*
	 * 上级客户代码
	 */
	@Column(name = "superior_customer_code")
	private String superiorCode;

	/*
	 * 企业海关编码
	 */
	@Column(name = "enterprise_customs_code")
	private String enterpriseCode;
	/*
	 * 风险等级
	 */
	@Column(name = "risk_level")
	private String riskLevel;
	/*
	 * 记录日期
	 */
	@Column(name = "record_date")
	private String recordDate;
	
	/*
	 * 修改时间
	 */
	@Column(name = "update_date")
	private String updateDate;
	
	
	/*
	 * 速查码
	 */
	@Column(name = "quick_code")
	private String quickCode;
	
	/*
	 * 公司id
	 */
	@Column(name = "cmpany_id")
	private String cmpanyId;
	
	/*
	 * 公司名称
	 */
	@Column(name = "cmpany_name")
	private String cmpanyName;

	/*
	 * 公司图片
	 */
	@Column(name = "cmpany_pic")
	private String cmpanyPic;
	/*
	 * 人员ID
	 */
	@Column(name = "staff_id")
	private String staffId;
	
	/*
	 * 人员名称
	 */
	@Column(name = "staff_name")
	private String staffName;
	
	/*
	 * 部门名称
	 */
	@Column(name = "dept_name")
	private String deptName;
	
	/*
	 * 标识
	 */
	@Column(name = "alive")
	private String alive;

	/*
	 * 标签
	 */
	@Column(name = "lable")
	private String lable;
	
	/*
	 * 緯度
	 */
	@Column(name = "lat")
	private String lat;
	
	/*
	 * 經度
	 */
	@Column(name = "lng")
	private String lng;
	
	/*
	 * 是否为共享客户
	 */
	@Column(name = "is_share")
	private String hasShare;
	
	/*
	 * 是否为产品线限制
	 */
	@Column(name = "is_power")
	private String hasPower;

	/*
	 * 创建人部门Id
	 */
	@Column(name = "dept_id")
	private String deptId;

	
	/*
	 *集团内外部分类 
	 */
	@Column(name= "group_type")
	private String groupType;
	
	@Column(name = "customer_code")
	private String customerCode;
	
	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
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

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getInstitutionalType() {
		return institutionalType;
	}

	public void setInstitutionalType(String institutionalType) {
		this.institutionalType = institutionalType;
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

	public String getCustomerClassification() {
		return customerClassification;
	}

	public void setCustomerClassification(String customerClassification) {
		this.customerClassification = customerClassification;
	}

	public String getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(String customerGroup) {
		this.customerGroup = customerGroup;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}

	public String getCcicCustomerNo() {
		return ccicCustomerNo;
	}

	public void setCcicCustomerNo(String ccicCustomerNo) {
		this.ccicCustomerNo = ccicCustomerNo;
	}

	public String getCustomerWebsite() {
		return customerWebsite;
	}

	public void setCustomerWebsite(String customerWebsite) {
		this.customerWebsite = customerWebsite;
	}

	public String getStaffSize() {
		return staffSize;
	}

	public void setStaffSize(String staffSize) {
		this.staffSize = staffSize;
	}

	public String getLegalRepresentative() {
		return legalRepresentative;
	}

	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}

	public String getRegisteredCapital() {
		return registeredCapital;
	}

	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBussinessScope() {
		return bussinessScope;
	}

	public void setBussinessScope(String bussinessScope) {
		this.bussinessScope = bussinessScope;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getIndustriesCode() {
		return industriesCode;
	}

	public void setIndustriesCode(String industriesCode) {
		this.industriesCode = industriesCode;
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getAddressEnglish() {
		return addressEnglish;
	}

	public void setAddressEnglish(String addressEnglish) {
		this.addressEnglish = addressEnglish;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getSuperiorName() {
		return superiorName;
	}

	public void setSuperiorName(String superiorName) {
		this.superiorName = superiorName;
	}

	public String getSuperiorCode() {
		return superiorCode;
	}

	public void setSuperiorCode(String superiorCode) {
		this.superiorCode = superiorCode;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	public String getQuickCode() {
		return quickCode;
	}

	public void setQuickCode(String quickCode) {
		this.quickCode = quickCode;
	}

	public String getBusOwnId() {
		return busOwnId;
	}

	public void setBusOwnId(String busOwnId) {
		this.busOwnId = busOwnId;
	}

	public String getEnterpriseType() {
		return enterpriseType;
	}

	public void setEnterpriseType(String enterpriseType) {
		this.enterpriseType = enterpriseType;
	}

	public String getEstablishmentDate() {
		return establishmentDate;
	}

	public void setEstablishmentDate(String establishmentDate) {
		this.establishmentDate = establishmentDate;
	}

	public String getClassificationIndustry() {
		return classificationIndustry;
	}

	public void setClassificationIndustry(String classificationIndustry) {
		this.classificationIndustry = classificationIndustry;
	}

	public String getCmpanyPic() {
		return cmpanyPic;
	}

	public void setCmpanyPic(String cmpanyPic) {
		this.cmpanyPic = cmpanyPic;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public String getAlive() {
		return alive;
	}

	public void setAlive(String alive) {
		this.alive = alive;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getSuperiorId() {
		return superiorId;
	}

	public void setSuperiorId(String superiorId) {
		this.superiorId = superiorId;
	}

	

	public Customer(String id, String busOwnId, String country, String countryName, String countryNo, String province,
			String city, String county, String town, String institutionalTypeSuper, String institutionalType,
			String enterpriseType, String customerName, String englishName, String customerClassification,
			String customerGroup, String codeType, String creditCode, String ccicCustomerNo, String customerWebsite,
			String staffSize, String legalRepresentative, String establishmentDate, String registeredCapital,
			String currency, String bussinessScope, String classification, String classificationIndustry,
			String industriesCode, String registeredAddress, String contactAddress, String detailAddress,
			String addressEnglish, String zipCode, String superiorId, String superiorName, String superiorCode,
			String enterpriseCode, String riskLevel, String recordDate, String updateDate, String quickCode,
			String cmpanyId, String cmpanyName, String cmpanyPic, String staffId, String staffName, String deptName,
			String alive, String lable, String lat, String lng, String hasShare, String hasPower, String deptId,
			String groupType, String customerCode) {
		super();
		this.id = id;
		this.busOwnId = busOwnId;
		this.country = country;
		this.countryName = countryName;
		this.countryNo = countryNo;
		this.province = province;
		this.city = city;
		this.county = county;
		this.town = town;
		this.institutionalTypeSuper = institutionalTypeSuper;
		this.institutionalType = institutionalType;
		this.enterpriseType = enterpriseType;
		this.customerName = customerName;
		this.englishName = englishName;
		this.customerClassification = customerClassification;
		this.customerGroup = customerGroup;
		this.codeType = codeType;
		this.creditCode = creditCode;
		this.ccicCustomerNo = ccicCustomerNo;
		this.customerWebsite = customerWebsite;
		this.staffSize = staffSize;
		this.legalRepresentative = legalRepresentative;
		this.establishmentDate = establishmentDate;
		this.registeredCapital = registeredCapital;
		this.currency = currency;
		this.bussinessScope = bussinessScope;
		this.classification = classification;
		this.classificationIndustry = classificationIndustry;
		this.industriesCode = industriesCode;
		this.registeredAddress = registeredAddress;
		this.contactAddress = contactAddress;
		this.detailAddress = detailAddress;
		this.addressEnglish = addressEnglish;
		this.zipCode = zipCode;
		this.superiorId = superiorId;
		this.superiorName = superiorName;
		this.superiorCode = superiorCode;
		this.enterpriseCode = enterpriseCode;
		this.riskLevel = riskLevel;
		this.recordDate = recordDate;
		this.updateDate = updateDate;
		this.quickCode = quickCode;
		this.cmpanyId = cmpanyId;
		this.cmpanyName = cmpanyName;
		this.cmpanyPic = cmpanyPic;
		this.staffId = staffId;
		this.staffName = staffName;
		this.deptName = deptName;
		this.alive = alive;
		this.lable = lable;
		this.lat = lat;
		this.lng = lng;
		this.hasShare = hasShare;
		this.hasPower = hasPower;
		this.deptId = deptId;
		this.groupType = groupType;
		this.customerCode = customerCode;
	}

	public String getHasShare() {
		return hasShare;
	}

	public void setHasShare(String hasShare) {
		if(StringUtils.isNull(hasShare)) {
			hasShare = "0";
		}
		this.hasShare = hasShare;
	}

	public String getHasPower() {
		return hasPower;
	}

	public void setHasPower(String hasPower) {
		if(StringUtils.isNull(hasPower)) {
			hasPower = "0";
		}
		this.hasPower = hasPower;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Customer() {
		super();
	}
	
	public String getInstitutionalTypeSuper() {
		return institutionalTypeSuper;
	}

	public void setInstitutionalTypeSuper(String institutionalTypeSuper) {
		this.institutionalTypeSuper = institutionalTypeSuper;
	}

	public String getCmpanyId() {
		return cmpanyId;
	}

	public void setCmpanyId(String cmpanyId) {
		this.cmpanyId = cmpanyId;
	}

	public String getCmpanyName() {
		return cmpanyName;
	}

	public void setCmpanyName(String cmpanyName) {
		this.cmpanyName = cmpanyName;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", busOwnId=" + busOwnId + ", country=" + country + ", countryName=" + countryName
				+ ", countryNo=" + countryNo + ", province=" + province + ", city=" + city + ", county=" + county
				+ ", town=" + town + ", institutionalTypeSuper=" + institutionalTypeSuper + ", institutionalType="
				+ institutionalType + ", enterpriseType=" + enterpriseType + ", customerName=" + customerName
				+ ", englishName=" + englishName + ", customerClassification=" + customerClassification
				+ ", customerGroup=" + customerGroup + ", codeType=" + codeType + ", creditCode=" + creditCode
				+ ", ccicCustomerNo=" + ccicCustomerNo + ", customerWebsite=" + customerWebsite + ", staffSize="
				+ staffSize + ", legalRepresentative=" + legalRepresentative + ", establishmentDate="
				+ establishmentDate + ", registeredCapital=" + registeredCapital + ", currency=" + currency
				+ ", bussinessScope=" + bussinessScope + ", classification=" + classification
				+ ", classificationIndustry=" + classificationIndustry + ", industriesCode=" + industriesCode
				+ ", registeredAddress=" + registeredAddress + ", contactAddress=" + contactAddress + ", detailAddress="
				+ detailAddress + ", addressEnglish=" + addressEnglish + ", zipCode=" + zipCode + ", superiorId="
				+ superiorId + ", superiorName=" + superiorName + ", superiorCode=" + superiorCode + ", enterpriseCode="
				+ enterpriseCode + ", riskLevel=" + riskLevel + ", recordDate=" + recordDate + ", updateDate="
				+ updateDate + ", quickCode=" + quickCode + ", cmpanyId=" + cmpanyId + ", cmpanyName=" + cmpanyName
				+ ", cmpanyPic=" + cmpanyPic + ", staffId=" + staffId + ", staffName=" + staffName + ", deptName="
				+ deptName + ", alive=" + alive + ", lable=" + lable + ", lat=" + lat + ", lng=" + lng + ", hasShare="
				+ hasShare + ", hasPower=" + hasPower + ", deptId=" + deptId + ", groupType=" + groupType + "]";
	}
	
	
}
