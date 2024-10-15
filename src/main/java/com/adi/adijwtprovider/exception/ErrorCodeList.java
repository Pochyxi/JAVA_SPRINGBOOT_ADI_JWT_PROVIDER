package com.adi.adijwtprovider.exception;

import lombok.Getter;

@Getter
public class ErrorCodeList {
    //RISORSA NON TROVATA
    public static final String NF404 = "NF404";

    //USERNAME O PASS O EMAIL ERRATE
    public static final String BADCREDENTIALS = "BADCREDENTIALS";

    // AL MOMENTO DELLA REGISTRAZIONE SE L'UTENTE NON E' ABILITATO
    public static final String NOTUSERENABLED = "NOTUSERENABLED";
}
