package com.example.gerardo.miestacionamiento.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Gerardo on 06/11/2016.
 */
public class ResponseAllEstacionamientos extends RealmObject {

    @PrimaryKey
    private String idR;

    @SerializedName("dueno")
    private Usuario usuario;

    @SerializedName("listaEstacionamientos")
    private RealmList<Estacionamiento> estacionamientos;

    public ResponseAllEstacionamientos() {
    }

    public String getIdR() {
        return idR;
    }

    public void setIdR(String idR) {
        this.idR = idR;
    }

    public List<Estacionamiento> getEstacionamientos() {
        return estacionamientos;
    }

    public void setEstacionamientos(RealmList<Estacionamiento> estacionamientos) {
        this.estacionamientos = estacionamientos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
