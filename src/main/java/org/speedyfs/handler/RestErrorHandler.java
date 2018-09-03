package org.speedyfs.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import org.speedyfs.dto.ErrorDTO;
import org.speedyfs.dto.FieldErrorDTO;
import org.speedyfs.dto.ResponseDTO;
import org.speedyfs.enums.MessageType;
import org.speedyfs.exception.AlbumDataException;
import org.speedyfs.exception.AlbumException;
import org.speedyfs.exception.DeviceDataException;
import org.speedyfs.exception.ImageDataException;
import org.speedyfs.exception.MediaException;
import org.speedyfs.exception.VideoException;

/**
 * Common Exception Handler for whole application.
 * @author rahul.sharma3
 *
 */
@ControllerAdvice
public class RestErrorHandler {

	private MessageSource messageSource;
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	public RestErrorHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseDTO> processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
		responseDTO.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
		responseDTO.setErrors(processFieldErrors(fieldErrors));
		log.error(HttpStatus.BAD_REQUEST.value(), ex);
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IOException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processIOException(IOException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		responseDTO.setMessage("Some error occured while reading or writting the file on server file system.");
		log.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex);
		return new ResponseEntity<ResponseDTO>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(FileNotFoundException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processFileNotFoundException(FileNotFoundException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		responseDTO.setMessage("Could not find the file on server file system");
		log.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex);
		return new ResponseEntity<ResponseDTO>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(DataAccessException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processDataAccessException(DataAccessException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		responseDTO.setMessage("Some error occured while accessing data from server database");
		log.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex);
		return new ResponseEntity<ResponseDTO>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(HibernateException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processHibernateException(HibernateException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		responseDTO.setMessage("Some error occured while querying from server database");
		log.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex);
		return new ResponseEntity<ResponseDTO>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(NullPointerException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processNullPointerException(NullPointerException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		responseDTO.setMessage("Some unexpected error has been occured, Please check logs on server.");
		log.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex);
		return new ResponseEntity<ResponseDTO>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processNoHandlerFoundException(NoHandlerFoundException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		responseDTO.setMessage(ex.getMessage());
		log.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex);
		return new ResponseEntity<ResponseDTO>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(NamingException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processNamingException(NamingException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.PRECONDITION_FAILED.value());
		responseDTO.setMessage("Some error occured while connecting with LDAP server");
		log.error(HttpStatus.PRECONDITION_FAILED.value(), ex);
		return new ResponseEntity<ResponseDTO>(HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(AlbumException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processAlbumException(AlbumException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		responseDTO.setMessage(ex.getMessage());
		log.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex);
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(VideoException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processVideoException(VideoException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		responseDTO.setMessage(ex.getMessage());
		log.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex);
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	
	@ExceptionHandler(AlbumDataException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processAlbumDataException(AlbumDataException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.NO_CONTENT.value());
		responseDTO.setMessage(ex.getMessage());
		log.error(HttpStatus.NO_CONTENT.value(), ex);
		return new ResponseEntity<ResponseDTO>(HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(ImageDataException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processImageDataException(ImageDataException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.NO_CONTENT.value());
		responseDTO.setMessage(ex.getMessage());
		log.error(HttpStatus.NO_CONTENT.value(), ex);
		return new ResponseEntity<ResponseDTO>(HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(MediaException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processImageException(MediaException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		responseDTO.setMessage(ex.getMessage());
		log.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex);
		return new ResponseEntity<ResponseDTO>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(DeviceDataException.class)
	public @ResponseBody ResponseEntity<ResponseDTO> processDeviceDataException(DeviceDataException ex) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		responseDTO.setMessage("No device found registered in your network");
		log.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex);
		log.error("NO DEVICE FOUND REGISTERED IN YOUR NETWORK");
		return new ResponseEntity<ResponseDTO>(HttpStatus.UNPROCESSABLE_ENTITY);
	}
	private ErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
		ErrorDTO dto = new ErrorDTO();
		List<FieldErrorDTO> errorDTOs = new ArrayList<FieldErrorDTO>();
		log.error("---------------VALIDATION ERRORS FOUND------------");
		for (FieldError fieldError : fieldErrors) {
			String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
			FieldErrorDTO error = new FieldErrorDTO();
			error.setField(fieldError.getField());
			error.setMessage(localizedErrorMessage);
			error.setMessageType(MessageType.ERROR);
			errorDTOs.add(error);
			log.error(error.toString());
		}
		dto.setFieldErrors(errorDTOs);
		return dto;
	}

	private String resolveLocalizedErrorMessage(FieldError fieldError) {
		Locale currentLocale = LocaleContextHolder.getLocale();
		String localizedErrorMessage = messageSource.getMessage(fieldError.getDefaultMessage(), null, currentLocale);
		if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
			String[] fieldErrorCodes = fieldError.getCodes();
			localizedErrorMessage = fieldErrorCodes[0];
		}
		return localizedErrorMessage;
	}
}
