package com.pfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.jpa.h2.model.DeviceFields;
import com.bezkoder.spring.jpa.h2.repository.DeviceRepository;
import com.pfe.exception.DeviceFieldException;

@Service
public class DeviceFieldsService {

	private static final String SERVICE_NAME = "DeviceFieldsService";
	@Autowired
	DeviceRepository deviceRepository;
	
	public DeviceFields addDeviceData(DeviceFields deviceFields) {
		DeviceFields _deviceFields = null;
		try {
			 _deviceFields =  deviceRepository.save(new
					DeviceFields(deviceFields.getDeviceTitle(),
							deviceFields.getDeviceDescription(), deviceFields.getImage(),
							deviceFields.getModel(), deviceFields.getVendor(), deviceFields.getStatus(),
							deviceFields.getCreatedDate(), deviceFields.getRelatedDocumentation()));
		} catch (DeviceFieldException e) {
			throw new DeviceFieldException("401", SERVICE_NAME, "addDeviceData throw DeviceFieldException", e);
		}catch (Exception e) {
			throw new DeviceFieldException("500", SERVICE_NAME, "addDeviceData throw RunTimeException", e);
		}
		return _deviceFields;

	}
}
