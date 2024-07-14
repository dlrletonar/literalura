package com.alura.literalura.models;


public enum Idioma {
    ES("es"), //  idioma Español
    FR("fr"), //  idioma Frances
    EN("en"), //  idioma Ingles
    PT("pt"), //  idioma Portugues
    DE("de"), //  idioma Alemán
    IT("it"), //  idioma Italiano
    RU("ru"), //  idioma Ruso
    JA("ja"), //  idioma Japonés
    ZH("zh"); //  idioma Chino

    private String idioma;

    Idioma(String idioma) {
        this.idioma = idioma;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public static Idioma fromString(String text){
        for(Idioma idioma : Idioma.values()){
            if (idioma.idioma.equalsIgnoreCase(text)){
                return idioma;
            }
        }
        throw new IllegalArgumentException("Idioma no válido.. "+ text );
    }
}
