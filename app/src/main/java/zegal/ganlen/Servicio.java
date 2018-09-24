package zegal.ganlen;

public class Servicio {

    private String fecGenerado;
    private String prestador;
    private String prestatario;
    private String finalidad;
    private double monto;
    private String facilidad;
    private int n_pagos;
    private String fecha_primer_pago;
    private String fotoIneR1;
    private String fotoIneR2;
    private String fotoIneP1;
    private String fotoIneP2;

    public Servicio(){

    }

    public Servicio(String fecGenerado, String prestador, String prestatario, String finalidad, double monto, String facilidad, int n_pagos, String fecha_primer_pago, String fotoIneR1, String fotoIneR2, String fotoIneP1, String fotoIneP2) {
        this.fecGenerado = fecGenerado;
        this.prestador = prestador;
        this.prestatario = prestatario;
        this.finalidad = finalidad;
        this.monto = monto;
        this.facilidad = facilidad;
        this.n_pagos = n_pagos;
        this.fecha_primer_pago = fecha_primer_pago;
        this.fotoIneR1 = fotoIneR1;
        this.fotoIneR2 = fotoIneR2;
        this.fotoIneP1 = fotoIneP1;
        this.fotoIneP2 = fotoIneP2;
    }

    public String getFecGenerado() {
        return fecGenerado;
    }

    public void setFecGenerado(String fecGenerado) {
        this.fecGenerado = fecGenerado;
    }

    public String getPrestador() {
        return prestador;
    }

    public void setPrestador(String prestador) {
        this.prestador = prestador;
    }

    public String getPrestatario() {
        return prestatario;
    }

    public void setPrestatario(String prestatario) {
        this.prestatario = prestatario;
    }

    public String getFinalidad() {
        return finalidad;
    }

    public void setFinalidad(String finalidad) {
        this.finalidad = finalidad;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFacilidad() {
        return facilidad;
    }

    public void setFacilidad(String facilidad) {
        this.facilidad = facilidad;
    }

    public int getN_pagos() {
        return n_pagos;
    }

    public void setN_pagos(int n_pagos) {
        this.n_pagos = n_pagos;
    }

    public String getFecha_primer_pago() {
        return fecha_primer_pago;
    }

    public void setFecha_primer_pago(String fecha_primer_pago) {
        this.fecha_primer_pago = fecha_primer_pago;
    }

    public String getFotoIneR1() {
        return fotoIneR1;
    }

    public void setFotoIneR1(String fotoIneR1) {
        this.fotoIneR1 = fotoIneR1;
    }

    public String getFotoIneR2() {
        return fotoIneR2;
    }

    public void setFotoIneR2(String fotoIneR2) {
        this.fotoIneR2 = fotoIneR2;
    }

    public String getFotoIneP1() {
        return fotoIneP1;
    }

    public void setFotoIneP1(String fotoIneP1) {
        this.fotoIneP1 = fotoIneP1;
    }

    public String getFotoIneP2() {
        return fotoIneP2;
    }

    public void setFotoIneP2(String fotoIneP2) {
        this.fotoIneP2 = fotoIneP2;
    }
}
