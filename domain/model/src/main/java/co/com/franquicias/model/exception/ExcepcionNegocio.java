package co.com.franquicias.model.exception;

import lombok.Getter;

@Getter
public class ExcepcionNegocio extends RuntimeException {

    private final ErrorDominio errorDominio;

    public ExcepcionNegocio(ErrorDominio errorDominio) {
        super(errorDominio.getMensaje());
        this.errorDominio = errorDominio;
    }

    public String getCodigo() {
        return errorDominio.getCodigo();
    }
}
