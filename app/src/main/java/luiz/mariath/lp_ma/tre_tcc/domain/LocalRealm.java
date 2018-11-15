package luiz.mariath.lp_ma.tre_tcc.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LocalRealm extends RealmObject{

    @PrimaryKey
    private long id;
    private String nome;
    private String endereco;
    private double latitude;
    private double longitude;
    private String nivel_alerta;

    public String getNivel_alerta() {
        return nivel_alerta;
    }

    public void setNivel_alerta(String nivel_alerta) {
        this.nivel_alerta = nivel_alerta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
