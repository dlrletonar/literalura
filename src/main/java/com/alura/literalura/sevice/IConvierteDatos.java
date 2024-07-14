package com.alura.literalura.sevice;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
