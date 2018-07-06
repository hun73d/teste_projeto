package com.vaadin.starter.beveragebuddy.backend;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

class StaticData {

    private static final String grupo3ao5 = "Grupo 3 ao grupo 5";
    private static final String  PrimAQuintEF = "1º ao 5º ano EF";
    private static final String SEXTOEF = "6º ano EF";
    private static final String SETIMOEF = "7º ano EF";
    private static final String OITAVOEF = "8º ano EF";
    private static final String NONOEF = "9º ano EF";
    private static final String ENSINO_MEDIO = "Ensino Médio";
    private static final String DIDATICO = "Didático";
    private static final String PARADIDATICO = "Paradidático";

    public static final String UNDEFINED = "Undefined";
    
    static final Map<String, String> LIVROS = new LinkedHashMap<>();

    static {
        Stream.of("grupo","fundamental1","fundamental 1")
                .forEach(name -> LIVROS.put(name, grupo3ao5));

        Stream.of("primeira","segunda","terceira","quarta","quinta","primeiro","segundo","terceiro","quarto")
                .forEach(name -> LIVROS.put(name, PrimAQuintEF));

        Stream.of("sexta","sexto")
                .forEach(name -> LIVROS.put(name, SEXTOEF));

        Stream.of("setima","setima serie","setimo","setimo ano")
                .forEach(name -> LIVROS.put(name, SETIMOEF));

       Stream.of("didatico","Didático","Didatico","DIDATICO").forEach(name -> LIVROS.put(name,DIDATICO));

       Stream.of("paradidatico","Paradidático","paradidatico","PARADIDATICO").forEach(name -> LIVROS.put(name,PARADIDATICO));


        Stream.of("nona","nona serie","nono"
                )
                .forEach(name -> LIVROS.put(name, NONOEF));

        Stream.of("Biologia,"
        		 )
                .forEach(name -> LIVROS.put(name, ENSINO_MEDIO));

        Stream.of("oitava","oitava serie","oitavo")
                .forEach(name -> LIVROS.put(name, OITAVOEF));


        LIVROS.put("", UNDEFINED);
    }
    //Esta classe não é feita para ser instanciada, por isso construtor PRIVATE;
    private StaticData() {    
    }
}
