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

@Entity
@Table(name = "RecentlyAdded")
@Data
public class RecentlyAdded implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int recentlyAddedId;
	@ManyToOne
	@JoinColumn(name = "deviceId")
	private DeviceFields deviceFields;
	@Column(name = "RecentlyAddedDate")
	private Date recentlyAddedDates;
	@Column(name = "Status")
	private String status;
}
