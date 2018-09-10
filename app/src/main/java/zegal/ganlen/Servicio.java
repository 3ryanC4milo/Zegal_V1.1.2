package zegal.ganlen;

public class Servicio {

    private String prestador;
    private String prestatario;
    private String finalidad;
    private double monto;
    private String facilidad;
    private int n_pagos;
    private String fecha_primer_pago;

    public Servicio(){

    }

    public Servicio(String prestador, String prestatario, String finalidad, double monto, String facilidad, int n_pagos, String fecha_primer_pago) {
        this.prestador = prestador;
        this.prestatario = prestatario;
        this.finalidad = finalidad;
        this.monto = monto;
        this.facilidad = facilidad;
        this.n_pagos = n_pagos;
        this.fecha_primer_pago = fecha_primer_pago;
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
}
