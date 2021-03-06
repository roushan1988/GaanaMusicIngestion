package com.til.prime.timesSubscription.util;

import com.til.prime.timesSubscription.dto.external.ValidationResponse;
import com.til.prime.timesSubscription.enums.ValidationError;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

public class PreConditions {
	private PreConditions() {

	}

	public static <T> void mustBeTrue(Boolean obj, ValidationError error, ValidationResponse validationResponse) {
		if (obj == null || obj==false) {
			validationResponse.addValidationError(error);
		}
	}

	public static <T> void mustBeFalse(Boolean obj, ValidationError error, ValidationResponse validationResponse) {
		if (obj == null || obj==true) {
			validationResponse.addValidationError(error);
		}
	}

	public static <T> void mustBeEqual(Object obj, Object obj2, ValidationError error, ValidationResponse validationResponse) {
		if (!((obj!=null && obj2!=null && obj.equals(obj2)) || (obj==null && obj2==null))) {
			validationResponse.addValidationError(error);
		}
	}

	public static <T> void bigDecimalComparisonMustBeEqual(BigDecimal obj, BigDecimal obj2, ValidationError error, ValidationResponse validationResponse) {
		if (!((obj!=null && obj2!=null && obj.compareTo(obj2)==0) || (obj==null && obj2==null))) {
			validationResponse.addValidationError(error);
		}
	}

	public static <T> void notNull(T obj, ValidationError error, ValidationResponse validationResponse) {
		if (obj == null) {
			validationResponse.addValidationError(error);
		}
	}

	public static <T> void mustBeNull(T obj, ValidationError error, ValidationResponse validationResponse) {
		if (obj != null) {
			validationResponse.addValidationError(error);
		}
	}

	public static void notEmpty(Collection obj, ValidationError error, ValidationResponse validationResponse) {
		if (CollectionUtils.isEmpty(obj)) {
			validationResponse.addValidationError(error);
		}
	}

	public static void notEmpty(CharSequence obj, ValidationError error, ValidationResponse validationResponse) {
		if (StringUtils.isEmpty(obj)) {
			validationResponse.addValidationError(error);
		}
	}

	public static void validMobile(CharSequence obj, ValidationError error, ValidationResponse validationResponse) {
		if(!GenericUtils.validMobile(obj)){
			validationResponse.addValidationError(error);
		}
	}

	public static void validEmail(CharSequence obj, ValidationError error, ValidationResponse validationResponse) {
		if (!GenericUtils.validEmail(obj)) {
			validationResponse.addValidationError(error);
		}
	}

	public static void notBefore(String date1, String date2, SimpleDateFormat sdf, ValidationError error, ValidationResponse validationResponse) {
		try {
			if (sdf.parse(date1).before(sdf.parse(date2))) {
				validationResponse.addValidationError(error);
			}
		} catch (ParseException e) {
			validationResponse.addValidationError(error);
		}
	}

	public static void notBefore(Date date1, Date date2, ValidationError error, ValidationResponse validationResponse) {
		if (date1.before(date2)) {
			validationResponse.addValidationError(error);
		}
	}

	public static void notAfter(String date1, String date2, SimpleDateFormat sdf, ValidationError error, ValidationResponse validationResponse) {
		try {
			if (sdf.parse(date1).after(sdf.parse(date2))) {
				validationResponse.addValidationError(error);
			}
		} catch (ParseException e) {
			validationResponse.addValidationError(error);
		}
	}

	public static void notGreater(Comparable arg1, Comparable arg2, ValidationError error, ValidationResponse validationResponse) {
		if (arg1.compareTo(arg2)>0) {
			validationResponse.addValidationError(error);
		}
	}

	public static void notAfter(Date date1, Date date2, ValidationError error, ValidationResponse validationResponse) {
		if (date1.after(date2)) {
			validationResponse.addValidationError(error);
		}
	}

	public static void notNullPositiveCheck(Number obj, ValidationError error, ValidationResponse validationResponse) {
		if (obj == null || obj.doubleValue()<=0d) {
			validationResponse.addValidationError(error);
		}
	}

	public static void notNullEnumCheck(String obj, Set<String> values, ValidationError error, ValidationResponse validationResponse) {
		if (StringUtils.isEmpty(obj) || !values.contains(obj)) {
			validationResponse.addValidationError(error);
		}
	}

	public static void notNullDateFormatCheck(String obj, SimpleDateFormat sdf, ValidationError error, ValidationResponse validationResponse) {
		try {
			if (StringUtils.isEmpty(obj)) {
				validationResponse.addValidationError(error);
			}else{
				sdf.parse(obj);
			}
		} catch (ParseException e) {
			validationResponse.addValidationError(error);
		}
	}
}
