package com.bezkoder.spring.jpa.h2.model;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table (name = "DeviceFields")
@Data
public class DeviceFields implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int deviceId;
	@Column(name = "DeviceTitle")
	private String deviceTitle;
	@Column(name = "DeviceDescription")
	private String deviceDescription;
	@Column(name = "Image")
	private Blob image;
	@Column(name = "Model")
	private String model;
	@Column(name = "Vendor")
	private String vendor;
	@Column(name = "Status")
	private String status;
	@Column(name = "CreatedDate")
	private Date createdDate;
	@Column(name = "RelatedDocumentation")
	private String relatedDocumentation;

	@OneToMany(mappedBy = "deviceFields")
	private List<RecentlyAdded> recentlyAdded;
	
	@OneToMany(mappedBy = "deviceFields")
	private List<RecentlyViewed> recentlyVieweds;
	
	@OneToMany(mappedBy = "deviceFields")
	private List<UsageFields> usageFields ;
}
