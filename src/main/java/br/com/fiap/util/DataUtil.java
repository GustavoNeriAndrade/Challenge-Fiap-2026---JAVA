package br.com.fiap.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataUtil {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Impede instanciar a classe: só possui métodos estáticos
    private DataUtil() {
    }

    public static Date paraSqlDate(String dataTexto) {
        LocalDate data = LocalDate.parse(dataTexto, FORMATO);
        return Date.valueOf(data);
    }

    public static String paraTexto(Date data) {
        return data.toLocalDate().format(FORMATO);
    }
}