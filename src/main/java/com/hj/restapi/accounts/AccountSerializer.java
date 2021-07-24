package com.hj.restapi.accounts;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class AccountSerializer extends JsonSerializer<Account> {
    @Override
    public void serialize(Account account, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeNumberField("id", account.getId());
        gen.writeEndObject();
    }
}
