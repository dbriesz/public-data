package com.teamtreehouse.public_data;

import com.teamtreehouse.public_data.controller.Prompter;
import com.teamtreehouse.public_data.dao.CountryDao;
import com.teamtreehouse.public_data.dao.CountryDaoImpl;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        CountryDao dao = new CountryDaoImpl();
        Prompter prompter = new Prompter(dao);
        prompter.loadMenuOptions();
    }
}
