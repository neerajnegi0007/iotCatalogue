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
@Table(name = "Region")
public class Region implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int regionId;
	
	@ManyToOne
	@JoinColumn(name = "usageId")
	private UsageFields usageFields;

	@OneToMany(mappedBy = "viewId")
	private List<RecentlyViewed> recentlyViewed;

	@Column(name = "Site")
	private String site;
	@Column(name = "Country")
	private String country;
	@Column(name = "Region")
	private String region;
	@Column(name = "CreatedDate")
	private Date createdDate;
	@Column(name = "Status")
	private String status;
}
