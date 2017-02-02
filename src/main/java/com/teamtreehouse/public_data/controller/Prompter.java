package com.teamtreehouse.public_data.controller;

import com.teamtreehouse.public_data.dao.CountryDao;
import com.teamtreehouse.public_data.model.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Prompter {
    private CountryDao dao;
    private List<Country> countries;
    private BufferedReader reader;
    private Map<Integer, String> menu;

    public Prompter(CountryDao countryDao) {
        dao = countryDao;
        countries = new ArrayList<>();
        countries = dao.fetchAllCountries();
        reader = new BufferedReader(new InputStreamReader(System.in));
        menu = new HashMap<>();
        menu.put(1, "Edit a country");
        menu.put(2, "Add a country");
        menu.put(3, "Delete a country");
        menu.put(4, "Quit");
    }

    private int promptAction() throws IOException {
        System.out.println("\n\nYour options are:");
        for (Map.Entry<Integer, String> option : menu.entrySet()) {
            System.out.printf("%n%d - %s %n",
                    option.getKey(),
                    option.getValue());
        }
        System.out.print("\n\nWhat do you want to do: ");

        return Integer.parseInt(reader.readLine());
    }

    public void loadMenuOptions() throws IOException {
        Integer choice;
        do{
            try {
                choice = promptAction();
                switch (choice) {
                    case 1:
                        editCountryData();
                        break;
                    case 2:
                        addCountry();
                        break;
                    case 3:
                        deleteCountry();
                        break;
                    case 4:
                        System.out.println("Goodbye!");
                        break;
                        default:
                            System.out.printf("%nUnknown choice:  '%d'. Try again.  %n%n",
                                    choice);
                }
            } catch (IOException ioe) {
                System.out.print("%nProblem with input%n%n");
                ioe.printStackTrace();
            }
        } while (promptAction() != (4));
    }

    private Country promptForCountry(List<Country> countries) throws IOException {
        String choice;
        Country countryChoice = null;
        for (Country country : countries) {
            System.out.printf("Country Code: %s \tCountry: %s %n", country.getCode(), country.getName());
        }

        do {
            try {
                System.out.print("\nSelect a country by its 3 letter code: ");
                choice = reader.readLine();
                if (choice.length() != 3) {
                    System.out.print("\nSelect a country by its 3 letter code): ");
                    choice = reader.readLine();
                }
                countryChoice = dao.findByCode(countries, choice.toUpperCase());
            } catch (IOException e) {
                System.out.println("\nInvalid input.  Please enter a 3 letter country code.");
            }
        } while (!countries.contains(countryChoice) || countryChoice == null);

        return countryChoice;
    }

    private String promptForCode() throws IOException {
        String code = "";
        do {
            try {
                System.out.print("\nPlease enter a 3 letter country code: ");
                code = reader.readLine();
            } catch (IOException e) {
                System.out.println("\nInvalid input.  Please enter a 3 letter country code.");
            }
        } while (code.length() != 3 || code.equals(""));

        return code.toUpperCase();
    }

    private String promptForName() throws IOException {
        String name = "";

        do {
            try {
                System.out.print("\nPlease enter the country name: ");
                name = reader.readLine();
            } catch (IOException e) {
                System.out.println("\nInvalid input.  Please enter a country name.");
            }
        } while (name.length() > 32 || name.equals(""));

        return name;
    }

    private Double promptForInternetUsers() throws IOException {
        Double internetUsers = -1.0;

        do {
            try {
                System.out.print("\nPlease enter the number of internet users: ");
                internetUsers = Double.valueOf(reader.readLine());
            } catch (IOException e) {
                System.out.println("\nInvalid input.  Please enter a number.");
            }
        } while (internetUsers < 0);

        return internetUsers;
    }

    private Double promptForAdultLiteracy() throws IOException {
        Double adultLiteracy = -1.0;

        do {
            try {
                System.out.print("\nPlease enter the adult literacy rate: ");
                adultLiteracy = Double.valueOf(reader.readLine());
            } catch (IOException e) {
                System.out.println("\nInvalid input.  Please enter a number.");
            }
        } while (adultLiteracy < 0);

        return adultLiteracy;
    }

    private void editCountryData() throws IOException {
        Country country = promptForCountry(countries);

        country.setName(promptForName());
        country.setInternetUsers(promptForInternetUsers());
        country.setAdultLiteracyRate(promptForAdultLiteracy());

        dao.update(country);

        System.out.println("\nUpdating...");
    }

    private void addCountry() throws IOException {
        Country country = new Country.CountryBuilder(promptForCode())
                .withName(promptForName())
                .withInternetUsers(promptForInternetUsers())
                .withAdultLiteracy(promptForAdultLiteracy())
                .build();
        System.out.printf("Adding %s to the database...%n", country.getName());
        countries.add(country);
        dao.save(country);
        System.out.println("Country added!");
    }

    private void deleteCountry() throws IOException {
        Country country = promptForCountry(countries);
        System.out.printf("Removing %s from the database...%n", country.getName());
        dao.delete(country);
        countries.remove(country);
        System.out.println("Country deleted!");
    }
}
