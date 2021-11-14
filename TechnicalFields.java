package com.bezkoder.spring.jpa.h2.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TechnicalFields")
public class TechnicalFields implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int technicalId;
	@ManyToOne
	@JoinColumn(name = "usageId")
	private UsageFields usageFields;
	@Column(name = "OperatingSystem")
	private String operatingSystem;
	@Column(name = "SoftwareVersion")
	private String softwareVersion;
	@Column(name = "ConnectivityType")
	private String connectivityType;
	@Column(name = "PowerSource")
	private String powerSource;
	@Column(name = "CreatedDate")
	private Date createdDate;
	@Column(name = "Status")
	private String status;
}
