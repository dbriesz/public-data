package com.teamtreehouse.public_data;

import com.teamtreehouse.public_data.controller.Prompter;
import com.teamtreehouse.public_data.dao.CountryDao;
import com.teamtreehouse.public_data.dao.CountryDaoImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Application {

    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        String newUserName = "sa";
        cfg.getProperties().setProperty("hibernate.connection.username",newUserName);
        SessionFactory sessionFactory = cfg.buildSessionFactory();

        CountryDao dao = new CountryDaoImpl();

        Prompter prompter = new Prompter();



    }
}
