package com.example.bouchef.tubolsillo.api.model;

public class CompraViewModelPOST {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdComercio() {
        return idComercio;
    }

    public void setIdComercio(int idComercio) {
        this.idComercio = idComercio;
    }
    public Boolean getCompraReal() { return compraReal;}

    public void setCompraReal(Boolean compraReal) {
        this.compraReal = compraReal;
    }

    public Double getLatitudInicio() {return latitudInicio;}

    public void setLatitudInicio(Double latitudInicio) {
        this.latitudInicio = latitudInicio;
    }

    public Double getLongitudInicio() {return longitudInicio;}

    public void setLongitudInicio(Double longitudInicio) {
        this.longitudInicio = longitudInicio;
    }

    public String getTiempoEstimadoDestino() {return tiempoEstimadoDestino;}

    public void setTiempoEstimadoDestino(String tiempoEstimadoDestino) {
        this.tiempoEstimadoDestino = tiempoEstimadoDestino;
    }

    public String getTiempoEstimadoRegreso() {return tiempoEstimadoRegreso;}

    public void setTiempoEstimadoRegreso(String tiempoEstimadoRegreso) {
        this.tiempoEstimadoRegreso = tiempoEstimadoRegreso;
    }

    private int id;
    private int idUsuario;
    private int idComercio;
    private Boolean compraReal = null;
    private Double latitudInicio = null;
    private Double longitudInicio = null;
    private String tiempoEstimadoDestino = null;
    private String tiempoEstimadoRegreso = null;
}
