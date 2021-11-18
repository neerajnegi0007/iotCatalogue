package com.bezkoder.spring.jpa.h2.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.Deflater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.spring.jpa.h2.model.DeviceFields;
import com.bezkoder.spring.jpa.h2.model.Tutorial;
import com.bezkoder.spring.jpa.h2.repository.DeviceRepository;
import com.bezkoder.spring.jpa.h2.repository.TutorialRepository;
import com.pfe.exception.DeviceFieldException;
import com.pfe.service.DeviceFieldsService;

@RestController
@RequestMapping("/api")
public class TutorialController {

	private static final Logger log = LoggerFactory.getLogger(TutorialController.class);

	@Autowired
	TutorialRepository tutorialRepository;

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	DeviceFieldsService deviceFieldsService;

	@GetMapping("/tutorials")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
		try {
			List<Tutorial> tutorials = new ArrayList<Tutorial>();

			if (title == null)
				tutorialRepository.findAll().forEach(tutorials::add);
			else
				tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

		if (tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/tutorials")
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
		try {
			Tutorial _tutorial = tutorialRepository
					.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@PostMapping("/deviceFields")
	public ResponseEntity<Object> createDevicefields(@RequestParam("file") MultipartFile file,
			@RequestParam("deviceTitle") String deviceTitle, @RequestParam("deviceDescription") String deviceDescription,
			@RequestParam("model") String model, @RequestParam("vendor") String vendor,
			@RequestParam("status") String status, @RequestParam("createdDate") String createdDate,
			@RequestParam("relatedDocumentation") String relatedDocumentation) throws IOException { 

		log.info(" create deviceFields method called ..");
		DeviceFields deviceFields = null;
		try {
			deviceFields = new DeviceFields(deviceTitle, deviceDescription, file.getBytes(),
					model, vendor, status, Date.valueOf(createdDate), relatedDocumentation);
		} catch (Exception e) {
			DeviceFieldException deviceFieldException = new DeviceFieldException("406", "Device Controller ", "Illegal Argument found in Parameters");
			return new ResponseEntity<>(deviceFieldException.getMessage(),HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<>(deviceFieldsService.addDeviceData(deviceFields),HttpStatus.CREATED); 

	}

	public static byte[] compressBytes(byte[] data) {

		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
		return outputStream.toByteArray();
	}


	@GetMapping(value = "/deviceFields/{deviceId}")
	public  ResponseEntity<Object>  getDeviceDetails(@PathVariable Long deviceId) {
		
		Optional<DeviceFields> deviceField = deviceRepository.findById(deviceId);

		if (!deviceField.isPresent()) {
			log.error("No value present for device ID {} ",deviceId);
			return new ResponseEntity<>("No value present for device ID "+ deviceId ,HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(deviceField,HttpStatus.OK);
	}

	@PutMapping("/deviceFields/{deviceId}")
	public ResponseEntity<DeviceFields> updateDevice(@PathVariable("deviceId") long deviceId,
			@RequestParam("file") MultipartFile file,
			@RequestParam("deviceTitle") String deviceTitle, @RequestParam("deviceDescription") String deviceDescription,
			@RequestParam("model") String model, @RequestParam("vendor") String vendor,
			@RequestParam("status") String status, @RequestParam("createdDate") String createdDate,
			@RequestParam("relatedDocumentation") String relatedDocumentation) throws IOException {

		Optional<DeviceFields> deviceData = deviceRepository.findById(deviceId);

		if (deviceData.isPresent()) { 
			DeviceFields deviceFields = deviceData.get();
			deviceFields.setDeviceTitle(deviceTitle);
			deviceFields.setCreatedDate(Date.valueOf(createdDate));
			deviceFields.setDeviceDescription(deviceDescription);
			deviceFields.setImage(compressBytes(file.getBytes()));
			deviceFields.setModel(model);
			deviceFields.setStatus(status);
			deviceFields.setVendor(vendor);
			deviceFields.setRelatedDocumentation(relatedDocumentation);
			return new ResponseEntity<>(deviceRepository.save(deviceFields), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} 
	}


	@PutMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

		if (tutorialData.isPresent()) {
			Tutorial _tutorial = tutorialData.get();
			_tutorial.setTitle(tutorial.getTitle());
			_tutorial.setDescription(tutorial.getDescription());
			_tutorial.setPublished(tutorial.isPublished());
			return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
		try {
			tutorialRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/tutorials")
	public ResponseEntity<HttpStatus> deleteAllTutorials() {
		try {
			tutorialRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/tutorials/published")
	public ResponseEntity<List<Tutorial>> findByPublished() {
		try {
			List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
