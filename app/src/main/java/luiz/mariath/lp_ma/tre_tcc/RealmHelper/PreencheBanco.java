package luiz.mariath.lp_ma.tre_tcc.RealmHelper;

import io.realm.Realm;
import luiz.mariath.lp_ma.tre_tcc.domain.LocalRealm;

public class PreencheBanco {
    private Realm realm;

    public PreencheBanco(Realm realm) {
        this.realm = realm;
    }

    public void preencheRealm() {
        realm.beginTransaction();

        LocalRealm local = realm.createObject(LocalRealm.class, 1);
        local.setNome("Escola Municipal Cidade Praiana");
        local.setEndereco("Avenida Rio Grande do Sul, s/n - Cidade Praiana");
        local.setLatitude(-22.551356);
        local.setLongitude(-41.976834);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 2);
        local.setNome("INSG/Castelo");
        local.setEndereco("Rua Pará, 181 - Cidade Beira Mar");
        local.setLatitude(-22.544618);
        local.setLongitude(-41.973679);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 3);
        local.setNome("Escola Municipal Rio das Ostras");
        local.setEndereco("Rua Santa Catarina, s/n - Cidade Praiana");
        local.setLatitude(-22.542355);
        local.setLongitude(-41.983351);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 4);
        local.setNome("Escola Municipal Maria Teixeira de Paula");
        local.setEndereco("Alameda Campomar, 600 - Jardim Campomar");
        local.setLatitude(-22.539707);
        local.setLongitude(-41.968355);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 5);
        local.setNome("Escola Municipal Alzir David Pereira");
        local.setEndereco("Av. Serramar, S/N - Extensão Serrramar");
        local.setLatitude(-22.533345);
        local.setLongitude(-41.979943);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 6);
        local.setNome("Escola Municipal Maria da Penha de Oliveira");
        local.setEndereco("Rua Domingos Farias da Motta -Palmital");
        local.setLatitude(-22.531928);
        local.setLongitude(-41.988676);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 7);
        local.setNome("CIEP Brizolão 349 Mestre Marçal Municipalizado");
        local.setEndereco("Rua Desembargador Elis E. Figueira, R. Seis, s/n - Jardim Campomar");
        local.setLatitude(-22.534515);
        local.setLongitude(-41.965424);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 8);
        local.setNome("Fórum de Rio das Ostras");
        local.setEndereco("Rua Desembargador Elis Hermidyo Figueira, 1990 - Jardim Campomar");
        local.setLatitude(-22.535249);
        local.setLongitude(-41.964802);
        local.setNivel_alerta("1");

        local = realm.createObject(LocalRealm.class, 9);
        local.setNome("Escola Municipal Prefeito Cláudio Ribeiro");
        local.setEndereco("R. Abel Siqueira - Extensão do Bosque");
        local.setLatitude(-22.526351);
        local.setLongitude(-41.966594);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 10);
        local.setNome("Colégio Jacintho");
        local.setEndereco("R. Rio Grande do Norte, s/n - Extensão do Bosque");
        local.setLatitude(-22.526833);
        local.setLongitude(-41.957099);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 11);
        local.setNome("Escola Municipal Vereador Pedro Moreira dos Santos");
        local.setEndereco("Rua Rio Grande do Norte, s/n - Extensão do Bosque");
        local.setLatitude(-22.523981);
        local.setLongitude(-41.957909);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 12);
        local.setNome("Secretaria de Desenvolvimento Econômico e Turismo");
        local.setEndereco("Praça Pref. Claudio Ribeiro, s/n - Extensão do Bosque");
        local.setLatitude(-22.527350);
        local.setLongitude(-41.950195);
        local.setNivel_alerta("1");

        local = realm.createObject(LocalRealm.class, 13);
        local.setNome("Posto de Saúde Operário");
        local.setEndereco("R. Cantagalo - Casa Grande");
        local.setLatitude(-22.522066);
        local.setLongitude(-41.952859);
        local.setNivel_alerta("1");

        local = realm.createObject(LocalRealm.class, 14);
        local.setNome("Teatro Popular de Rio das Ostras");
        local.setEndereco("Av. Amazonas, s/n - Centro");
        local.setLatitude(-22.521850);
        local.setLongitude(-41.946858);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 15);
        local.setNome("Casulo Centro Educacional");
        local.setEndereco("Rua Denise Vidal, Lote 10 a 15, S/Nº - Village Sol e Mar");
        local.setLatitude(-22.515807);
        local.setLongitude(-41.946432);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 16);
        local.setNome("Centro Educacional Mundo Encantado");
        local.setEndereco("R. Flamengo, 607 - Centro");
        local.setLatitude(-22.523355);
        local.setLongitude(-41.942728);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 17);
        local.setNome("Escola Estadual Esmeralda da Costa Porto");
        local.setEndereco("R. Leni Pereira Mello, 55 - Centro");
        local.setLatitude(-22.526430);
        local.setLongitude(-41.944860);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 18);
        local.setNome("Secretaria Municipal De Fazenda");
        local.setEndereco("Rua Jandira Moraes Pimentel, 504 - Centro");
        local.setLatitude(-22.527012);
        local.setLongitude(-41.943616);
        local.setNivel_alerta("1");

        local = realm.createObject(LocalRealm.class, 19);
        local.setNome("Secretaria de Estado de Trabalho e Renda");
        local.setEndereco("R. Jovem Viana, 74 - Centro");
        local.setLatitude(-22.525632);
        local.setLongitude(-41.940552);
        local.setNivel_alerta("1");

        local = realm.createObject(LocalRealm.class, 20);
        local.setNome("Pólo Universitário de Rio das Ostras");
        local.setEndereco("Rua Recife, Lotes 1-7 - Jardim Bela Vista");
        local.setLatitude(-22.503580);
        local.setLongitude(-41.923902);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 21);
        local.setNome("Prefeitura Municipal de Rio das Ostras");
        local.setEndereco("Rua Campo do Albacora, 75 - Loteamento Atlântica");
        local.setLatitude(-22.498454);
        local.setLongitude(-41.922072);
        local.setNivel_alerta("1");

        local = realm.createObject(LocalRealm.class, 22);
        local.setNome("C.E Pequeno Aprendiz");
        local.setEndereco("R. Campo de Bicudo, 468 - Jardim Atlântico");
        local.setLatitude(-22.497343);
        local.setLongitude(-41.927064);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 23);
        local.setNome("Centro integrado de Ensino Educarte");
        local.setEndereco("R. Resende, 1400 - Jardim Mariléa");
        local.setLatitude(-22.498671);
        local.setLongitude(-41.928619);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 24);
        local.setNome("Paróquia São Benedito");
        local.setEndereco("Rua Niterói, Quadra 18, Lote 01, s/n - Atlantica");
        local.setLatitude(-22.500376);
        local.setLongitude(-41.929821);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 25);
        local.setNome("Escola Municipal Nilton Balthazar");
        local.setEndereco("R. Valença, s/n - Jardim Mariléa");
        local.setLatitude(-22.500510);
        local.setLongitude(-41.930438);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 26);
        local.setNome("Primeira Igreja Batista em Jardim Mariléa");
        local.setEndereco("Rua Barra do Piraí, Q 83, Lotes 24/25, s/n - Jardim Mariléa");
        local.setLatitude(-22.501457);
        local.setLongitude(-41.925031);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 27);
        local.setNome("Escola Municipal Acerbal Pinto Malheiros");
        local.setEndereco("R. Nova Friburgo, s/n - Jardim Mariléa");
        local.setLatitude(-22.505565);
        local.setLongitude(-41.928469);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 28);
        local.setNome("Creche e Escola CENA");
        local.setEndereco("R. Teresópolis, 634 - Recreio");
        local.setLatitude(-22.513836);
        local.setLongitude(-41.921605);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 29);
        local.setNome("CECRIN Comunidade Evangélica Cristo Em Nós");
        local.setEndereco("R. Pompeu Corrêa da Gama, 120 - Jardim Mariléa");
        local.setLatitude(-22.513009);
        local.setLongitude(-41.931379);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 30);
        local.setNome("Colégio Mosaico");
        local.setEndereco("R. São Fidélis, 555 - Jardim Mariléa");
        local.setLatitude(-22.509883);
        local.setLongitude(-41.935136);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 31);
        local.setNome("Colégio Mosaico");
        local.setEndereco("R. São Fidélis, 555 - Jardim Mariléa");
        local.setLatitude(-22.509883);
        local.setLongitude(-41.935136);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 32);
        local.setNome("Chopperia Tijuca");
        local.setEndereco("teste");
        local.setLatitude(-22.919531);
        local.setLongitude(-43.218040);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 33);
        local.setNome("Praça Afonso Pena");
        local.setEndereco("teste");
        local.setLatitude(-22.917712);
        local.setLongitude(-43.218823);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 34);
        local.setNome("Ponto aleatório");
        local.setEndereco("teste");
        local.setLatitude(-22.916189);
        local.setLongitude(-43.219542);
        local.setNivel_alerta("2");

        local = realm.createObject(LocalRealm.class, 35);
        local.setNome("Rua do trabalho");
        local.setEndereco("teste");
        local.setLatitude(-22.913284);
        local.setLongitude(-43.222833);
        local.setNivel_alerta("2");

        realm.commitTransaction();
    }
}
