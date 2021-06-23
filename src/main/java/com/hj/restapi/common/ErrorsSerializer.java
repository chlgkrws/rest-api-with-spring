package com.hj.restapi.common;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors>{

	@Override
	public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		test
		gen.writeStartArray();
		errors.getFieldErrors().stream().forEach(e -> {
			try {
				gen.writeStartObject();
				gen.writeStringField("field", e.getField());
				gen.writeStringField("code", e.getCode());
				gen.writeStringField("objectName", e.getObjectName());
				gen.writeStringField("defaultMessage", e.getDefaultMessage());

				Object rejectedValue = e.getRejectedValue();
				if(rejectedValue != null) {
					gen.writeStringField("rejectedValue", rejectedValue.toString());
				}
				gen.writeEndObject();
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		errors.getGlobalErrors().stream().forEach(e -> {
			try {
				gen.writeStartObject();
				gen.writeStringField("code", e.getCode());
				gen.writeStringField("objectName", e.getObjectName());
				gen.writeStringField("defaultMessage", e.getDefaultMessage());
				gen.writeEndObject();
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		gen.writeEndArray();
	}


}
