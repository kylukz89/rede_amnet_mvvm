package com.meuamericanet.redetelecom.repository;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.meuamericanet.redetelecom.model.Usuario;
import java.lang.reflect.Type;

public class UsuarioJSON implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonElement jsonAutenticacao = json.getAsJsonObject();

        if (json.getAsJsonObject().size() > 0){
            jsonAutenticacao = json.getAsJsonObject();
        }

        return new Gson().fromJson(jsonAutenticacao, Usuario.class);
    }
}