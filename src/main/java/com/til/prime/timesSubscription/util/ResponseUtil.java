package com.til.prime.timesSubscription.util;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.GenericResponse;
import com.til.prime.timesSubscription.enums.ResponseCode;
import com.til.prime.timesSubscription.enums.ValidationError;

import java.util.Set;

/**
 * Created by roushan on 16/7/17.
 */
public class ResponseUtil {

	public static GenericResponse createExceptionResponse(GenericResponse genericResponse, Integer validationErrorCategory){
		genericResponse.setSuccess(false);
		genericResponse.setResponseCode(ResponseCode.EXCEPTION);
		genericResponse.setResponseMessage(GlobalConstants.EXCEPTION_MESSAGE);
		genericResponse.setValidationErrorCategory(validationErrorCategory);
		return genericResponse;
	}

	public static GenericResponse createFailureResponse(GenericResponse genericResponse, Set<ValidationError> validationErrorSet, Integer validationErrorCategory){
		genericResponse.setSuccess(false);
		genericResponse.setResponseCode(ResponseCode.VALIDATION_ERROR);
		genericResponse.setResponseMessage(GlobalConstants.VALIDATION_FAILURE);
		genericResponse.getValidationErrors().addAll(validationErrorSet);
		genericResponse.setValidationErrorCategory(validationErrorCategory);
		return genericResponse;
	}

	public static GenericResponse createSuccessResponse(GenericResponse genericResponse){
		genericResponse.setSuccess(true);
		genericResponse.setResponseCode(ResponseCode.SUCCESS);
		genericResponse.setResponseMessage(GlobalConstants.SUCCESS);
		return genericResponse;
	}

	public static GenericResponse createResponse(GenericResponse genericResponse, ResponseCode responseCode, String message, Integer validationErrorCategory){
		genericResponse.setSuccess(responseCode==ResponseCode.SUCCESS? true: false);
		genericResponse.setResponseCode(responseCode);
		genericResponse.setResponseMessage(message);
		genericResponse.setValidationErrorCategory(validationErrorCategory);
		return genericResponse;
	}
}
