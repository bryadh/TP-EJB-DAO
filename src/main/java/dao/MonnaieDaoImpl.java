package dao;

import converter.Monnaie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonnaieDaoImpl implements MonnaieDao {

    private DaoFactory daoFactory;

    public MonnaieDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void ajouter(Monnaie monnaie) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = daoFactory.getConnection();

            preparedStatement = connection.prepareStatement("INSERT INTO monnaie(code,rate) VALUES(?,?);");
            preparedStatement.setString(1, monnaie.getCode());
            preparedStatement.setDouble(2,monnaie.getRate());

            preparedStatement.executeUpdate();

            System.out.println("DEBUG ---> successeful insert");
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("DEBUG ---> SQLException when trying to INSERT");
        }

    }

    @Override
    public List<Monnaie> lister() {

        List<Monnaie> monnaies = new ArrayList<Monnaie>();

        Connection connection = null;
        Statement statement = null;
        ResultSet result =  null;

        try {
            connection = daoFactory.getConnection();
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT code, rate FROM monnaie;");

            while (result.next()){
                String code = result.getString("code");
                double rate = result.getDouble("rate");

                Monnaie monnaie = new Monnaie();
                monnaie.setCode(code);
                monnaie.setRate(rate);

                monnaies.add(monnaie);
            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("DEBUG ---> SQLException when trying to SELECT");
        }

        return monnaies;
    }

    @Override
    public Monnaie lister(String codeMonnaie) {

        Monnaie monnaie = new Monnaie();

        Connection connection = null;
        Statement statement = null;
        ResultSet result =  null;

        try {
            connection = daoFactory.getConnection();
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT code, rate FROM monnaie where code='"+codeMonnaie+"';");

            while (result.next()){
                String code = result.getString("code");
                double rate = result.getDouble("rate");

                monnaie.setCode(code);
                monnaie.setRate(rate);
            }

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("DEBUG ---> SQLException when trying to SELECT WHERE");
        }

        return monnaie;
    }

    @Override
    public boolean checkData(String code) {
        boolean check = false;

        if(lister().size() != 0){
            for(Monnaie m : lister()){
                if(m.getCode().equals(code)) {
                    check = true;
                    break;
                }
            }
        }

        return check;
    }
}
