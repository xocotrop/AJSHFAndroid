package br.com.irweb.ajshf.Entities;

/**
 * Created by Igor on 25/09/2017.
 */

public class Neighborhood {
    public int Id;
    public String Bairro;

    @Override
    public String toString() {
        return Bairro;
    }
}
