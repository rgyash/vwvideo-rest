package com.vwtest.video.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.ActuatorMetricWriter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vwtest.video.domain.File;
import com.vwtest.video.domain.Users;
import com.vwtest.video.repositories.FileRepository;
import com.vwtest.video.storage.FileContentStore;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/")
@ActuatorMetricWriter
@Api(value = "Video Upload Service")
public class FileUploadController {

	@Autowired
	private FileRepository filesRepo;

	@Autowired
	private FileContentStore contentStore;

	@Value("${video.uploadtype}")
	private String fileExtType;

	@Value("${video.fileDurationSec}")
	private Integer fileDurationSec;

	@ApiOperation(value = "Mp4 Video File Upload")
	@RequestMapping(value = "/file", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<?> setContent(@RequestParam("fileName") String fileName,
			@RequestParam("file") MultipartFile file, Authentication auth) throws IOException {

		// detecting the file type
		BodyContentHandler handler = new BodyContentHandler();
		ParseContext pcontext = new ParseContext();
		Metadata metadata = new Metadata();
		MP4Parser MP4Parser = new MP4Parser();
		try {
			MP4Parser.parse(file.getInputStream(), handler, metadata, pcontext);
			if (!"video/mp4".equals(metadata.get("Content-Type"))) {
				return new ResponseEntity<Object>(
						"Only files of these types are allowed to be uploaded: " + fileExtType, HttpStatus.BAD_REQUEST);
			}
			Double duration = Double.parseDouble(metadata.get("xmpDM:duration"));
			if (duration > fileDurationSec) {
				return new ResponseEntity<Object>("Only files of total seconds are allowed : " + fileDurationSec,
						HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<Object>("Only files of these types are allowed to be uploaded: " + fileExtType,
					HttpStatus.BAD_REQUEST);
		}

		Optional<File> fileTemp = filesRepo.findByName(fileName);
		File userFile = fileTemp.orElseGet(File::new);
		final Users user = (Users) auth.getPrincipal();
		userFile.setUserId(user.getId());
		userFile.setName(fileName);
		userFile.setMimeType(file.getContentType());

		contentStore.setContent(userFile, file.getInputStream());
		filesRepo.save(userFile);
		ObjectMapper mapperObj = new ObjectMapper();
		return new ResponseEntity<Object>(mapperObj.writeValueAsString(convertToMap(metadata)), HttpStatus.OK);
	}

	@ApiOperation(value = "Mp4 Video File Download")
	@RequestMapping(value = "/file/{fileName}", method = RequestMethod.GET, headers = "accept=multipart/form-data")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<?> getContent(@PathVariable("fileName") String fileName, Authentication auth) {
		final Users user = (Users) auth.getPrincipal();
		Optional<File> fileTemp = filesRepo.findByNameAndUserId(fileName, user.getId());
		if (!fileTemp.isPresent())
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		InputStreamResource inputStreamResource = new InputStreamResource(contentStore.getContent(fileTemp.get()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentLength(fileTemp.get().getContentLength());
		headers.set("Content-Type", fileTemp.get().getMimeType());
		return new ResponseEntity<Object>(inputStreamResource, headers, HttpStatus.OK);
	}
	
	protected Map<String,String> convertToMap(Metadata metadata) {
		Map<String,String> result = new HashMap<>();
		for (String name: metadata.names()) {
			result.put(name, (metadata.get(name)));
		}
		return result;
	}
}