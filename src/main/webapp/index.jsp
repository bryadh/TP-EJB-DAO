<%--
  Created by IntelliJ IDEA.
  User: ryadh
  Date: 04/11/2020
  Time: 09:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Converter</title>
</head>
<body>
    <!-- Imports  -->
    <%@page import="java.util.*" %>
    <%@page import="converter.ConverterBean" %>
    <%@ page import="converter.IConverter" %>
    <%@ page import="dao.MonnaieDao" %>
    <%@ page import="dao.DaoFactory" %>
    <%@ page import="converter.Monnaie" %>

    <!-- Injection de dependance  -->
    <jsp:useBean id="beanConv" class="converter.ConverterBean"/>

    <!-- Initialisation du DAO -->
    <%
        DaoFactory daoFactory = DaoFactory.getInstance();
        MonnaieDao monnaieDao = daoFactory.getMonnaieDao();
    %>

    <form action="index.jsp" method="get">

        <p>
            <label for="montant">Montant :</label>
            <input type="text" name="montant" id="montant">
        </p>

        <p>
            <label for="monnaie">Monnaie cible :</label>
            <select name="monnaie" id="monnaie">
                <option value=\"\">--Please choose an option--</option>
                <%
                    IConverter cb = new ConverterBean();
                    for (String s : cb.getCurrencies()){
                        out.println("<option value=\""+s+"\">"+s+"</option>");
                    }
                %>
            </select>
        </p>

        <p>
            <input type="submit">
        </p>

    </form>

    <!-- Affichage du resultat -->
    <%

        if (request.getParameter("montant") == null || request.getParameter("monnaie") == null){

            System.out.println("Not parameters yet");

        } else {

            List<Monnaie> monnaies = monnaieDao.lister();

            double amount  = Double.parseDouble(request.getParameter("montant"));
            String currency = request.getParameter("monnaie");

            // Si la monnaie a déja été sauvegardée dans la base de données
            if(monnaieDao.checkData(currency)){

                Monnaie monnaie = monnaieDao.lister(currency);
                out.println("<h4>Le montant converti en "+ currency +" est:"+ cb.euroToOtherCurrencyRest(amount, monnaie.getRate())+"</h4>");

            } else {

                double rate = cb.getRate().get(currency);

                double result = cb.euroToOtherCurrencyRest(amount, rate);

                out.println("<h4>Le montant converti en "+ currency +" est:"+ result +"</h4>");

                // ajouter la monnaie à la base de données
                Monnaie monn = new Monnaie();
                monn.setCode(currency);
                monn.setRate(rate);

                monnaieDao.ajouter(monn);

            }




        }

    %>

    <h3>Liste des monnaies dans la base de données: </h3>
    <ul>
        <%
            if (monnaieDao.lister().size() == 0){
                out.println("<li>AUCUNE</li>");
            } else {
                for (Monnaie m : monnaieDao.lister()){
                    out.println("<li>currency:"+m.getCode()+", rate:"+m.getRate()+"</li>");
                }
            }

        %>
    </ul>

</body>
</html>
