package com.til.prime.timesSubscription.util;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.BackendSubscriptionResponse;
import com.til.prime.timesSubscription.dto.external.GenericResponse;
import com.til.prime.timesSubscription.dto.external.ValidationResponse;
import com.til.prime.timesSubscription.enums.ResponseCode;

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

	public static GenericResponse createFailureResponse(GenericResponse genericResponse, ValidationResponse validationResponse, Integer validationErrorCategory){
		genericResponse.setSuccess(false);
		genericResponse.setResponseCode(ResponseCode.VALIDATION_ERROR);
		genericResponse.setResponseMessage(validationResponse.getMessage());
		genericResponse.getValidationErrors().addAll(validationResponse.getValidationErrorSet());
		genericResponse.setValidationErrorCategory(validationErrorCategory);
		return genericResponse;
	}

	public static BackendSubscriptionResponse createFailureResponse(BackendSubscriptionResponse genericResponse){
		genericResponse.setSuccess(false);
		genericResponse.setResponseCode(ResponseCode.VALIDATION_ERROR);
		genericResponse.setResponseMessage(GlobalConstants.VALIDATION_FAILURE);
		return genericResponse;
	}

	public static GenericResponse createSuccessResponse(GenericResponse genericResponse){
		genericResponse.setSuccess(true);
		genericResponse.setResponseCode(ResponseCode.SUCCESS);
		genericResponse.setResponseMessage(GlobalConstants.SUCCESS);
		return genericResponse;
	}
}
