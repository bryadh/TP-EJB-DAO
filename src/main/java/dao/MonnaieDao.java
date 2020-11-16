package dao;

import converter.Monnaie;

import java.util.List;

public interface MonnaieDao {

    void ajouter(Monnaie utilisateur);
    List<Monnaie> lister();
    Monnaie lister(String code);
    boolean checkData(String code);

}
