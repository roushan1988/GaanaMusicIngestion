package com.til.prime.timesSubscription.util;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.GenericResponse;
import com.til.prime.timesSubscription.enums.ResponseCode;
import com.til.prime.timesSubscription.enums.ValidationError;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * Created by roushan on 16/7/17.
 */
public class ResponseUtil {

	public static GenericResponse createFailureResponse(ValidationError validationError, String message){
		GenericResponse genericResponse = new GenericResponse();
		genericResponse.setSuccess(false);
		genericResponse.setResponseCode(ResponseCode.VALIDATION_ERROR);
		genericResponse.setResponseMessage((StringUtils.isEmpty(message))? validationError.getErrorMessage(): message);
		genericResponse.getValidationErrors().add(validationError);
		return genericResponse;
	}

	public static GenericResponse createExceptionResponse(String message){
		GenericResponse genericResponse = new GenericResponse();
		genericResponse.setSuccess(false);
		genericResponse.setResponseCode(ResponseCode.EXCEPTION);
		genericResponse.setResponseMessage(message);
		return genericResponse;
	}

	public static GenericResponse createExceptionResponse(GenericResponse genericResponse){
		genericResponse.setSuccess(false);
		genericResponse.setResponseCode(ResponseCode.EXCEPTION);
		genericResponse.setResponseMessage(GlobalConstants.EXCEPTION_MESSAGE);
		return genericResponse;
	}

	public static GenericResponse createSuccessResponse(String message){
		GenericResponse genericResponse = new GenericResponse();
		genericResponse.setSuccess(true);
		genericResponse.setResponseCode(ResponseCode.SUCCESS);
		genericResponse.setResponseMessage(message);
		return genericResponse;
	}

	public static GenericResponse createFailureResponse(GenericResponse genericResponse, ValidationError validationError, String message){
		genericResponse.setSuccess(false);
		genericResponse.setResponseCode(ResponseCode.VALIDATION_ERROR);
		genericResponse.setResponseMessage((StringUtils.isEmpty(message))? validationError.getErrorMessage(): message);
		genericResponse.getValidationErrors().add(validationError);
		return genericResponse;
	}

	public static GenericResponse createFailureResponse(GenericResponse genericResponse, Set<ValidationError> validationErrorSet, String message){
		genericResponse.setSuccess(false);
		genericResponse.setResponseCode(ResponseCode.VALIDATION_ERROR);
		genericResponse.setResponseMessage(message);
		genericResponse.getValidationErrors().addAll(validationErrorSet);
		return genericResponse;
	}

	public static GenericResponse createFailureResponse(GenericResponse genericResponse, Set<ValidationError> validationErrorSet){
		genericResponse.setSuccess(false);
		genericResponse.setResponseCode(ResponseCode.VALIDATION_ERROR);
		genericResponse.setResponseMessage(GlobalConstants.VALIDATION_FAILURE);
		genericResponse.getValidationErrors().addAll(validationErrorSet);
		return genericResponse;
	}

	public static GenericResponse createFailureResponse(GenericResponse genericResponse, String message){
		genericResponse.setSuccess(false);
		genericResponse.setResponseCode(ResponseCode.VALIDATION_ERROR);
		genericResponse.setResponseMessage(message);
		return genericResponse;
	}

	public static GenericResponse createExceptionResponse(GenericResponse genericResponse, String message){
		genericResponse.setSuccess(false);
		genericResponse.setResponseCode(ResponseCode.EXCEPTION);
		genericResponse.setResponseMessage(message);
		return genericResponse;
	}

	public static GenericResponse createSuccessResponse(GenericResponse genericResponse, String message){
		genericResponse.setSuccess(true);
		genericResponse.setResponseCode(ResponseCode.SUCCESS);
		genericResponse.setResponseMessage(message);
		return genericResponse;
	}

	public static GenericResponse createSuccessResponse(GenericResponse genericResponse){
		genericResponse.setSuccess(true);
		genericResponse.setResponseCode(ResponseCode.SUCCESS);
		genericResponse.setResponseMessage(GlobalConstants.SUCCESS);
		return genericResponse;
	}

	public static GenericResponse createResponse(GenericResponse genericResponse, ResponseCode responseCode, String message){
		genericResponse.setSuccess(responseCode==ResponseCode.SUCCESS? true: false);
		genericResponse.setResponseCode(responseCode);
		genericResponse.setResponseMessage(message);
		return genericResponse;
	}
}
