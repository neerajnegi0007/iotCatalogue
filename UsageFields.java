package com.bezkoder.spring.jpa.h2.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "UsageFields")
public class UsageFields implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int usageId;
	@ManyToOne
	@JoinColumn(name = "deviceId")
	private DeviceFields deviceFields;
	@OneToMany(mappedBy = "technicalId")
	private List<TechnicalFields> technicalFields;
	@OneToMany(mappedBy = "regionId")
	private List<Region> region;
	@Column(name = "Category")
	private String category;
	@Column(name = "Benefits")
	private String benefits;
	@Column(name = "TestCase")
	private String testCase;
	@Column(name = "Tags")
	private String tags;
	@Column(name = "BusinessUnit")
	private String businessUnit;
	@Column(name = "ProductOwner")
	private String productOwner;
	@Column(name = "ClientPartner")
	private String clientPartner;
	@Column(name = "BusinessSponsor")
	private String businessSponsor;
	@Column(name = "VendorContact")
	private String vendorContact;
	@Column(name = "CreatedDate")
	private Date createdDate;
	@Column(name = "Status")
	private String status;
}
