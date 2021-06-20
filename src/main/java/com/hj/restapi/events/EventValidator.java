package com.hj.restapi.events;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {

	public void validate(EventDTO eventDto, Errors errors) {
		if(eventDto.getMaxPrice() < eventDto.getBasePrice() && eventDto.getMaxPrice() != 0) {
			errors.rejectValue("basePrice","worngValue","BasePrice is worng");
			errors.rejectValue("maxPrice","worngValue","maxPrice is worng");		//필드 에러
			errors.reject("wrongPrices", "Values for prices are wrong");		//글로벌 에러
		}

		LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
		if(endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||
				endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
				endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())) {
			errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
		}

		// TODO BeginEnrollmentDateTime
		// TODO BeginEventDateTime
	}
}
