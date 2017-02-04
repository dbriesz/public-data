package com.teamtreehouse.public_data.dao;

import com.teamtreehouse.public_data.model.Country;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CountryDaoImpl implements CountryDao {
    private List<Country> countries;

    public CountryDaoImpl() {
        countries = new ArrayList<>();
        countries = fetchAllCountries();
    }

    // Hold a reusable reference to a SessionFactory
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @Override
    public void update(Country country) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to update the country
        session.update(country);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    @Override
    public void save(Country country) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to save the country
        session.save(country);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    @Override
    public void delete(Country country) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to delete the country
        session.delete(country);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    @Override
    public List<Country> fetchAllCountries() {
        // Open a session
        Session session = sessionFactory.openSession();

        // Create Criteria
        Criteria criteria = session.createCriteria(Country.class);

        // Get a list of Country objects according to the Criteria object
        List<Country> countries = criteria.list();

        // Close the session
        session.close();

        return countries;
    }

    @Override
    public Country findByCode(List<Country> countries, String code) {

        // Uses a stream to find a country by code
        return countries.stream()
                .filter(country -> country.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateCountries(List<Country> updatedCountries) {
        countries = updatedCountries;
    }

    @Override
    public Country minInternetUsers() {

        // Uses a stream to compare each country and find the country with the lowest percentage of internet users
        return countries.stream()
                .filter(country -> country.getInternetUsers() != null)
                .min(Comparator.comparingDouble(Country::getInternetUsers))
                .get();
    }

    @Override
    public Country maxInternetUsers() {

        // Uses a stream to compare each country and find the country with the highest percentage of internet users
        return countries.stream()
                .filter(country -> country.getInternetUsers() != null)
                .max(Comparator.comparingDouble(Country::getInternetUsers))
                .get();
    }

    @Override
    public Country minAdultLiteracy() {

        // Uses a stream to compare each country and find the country with the lowest adult literacy rate
        return countries.stream()
                .filter(country -> country.getAdultLiteracyRate() != null)
                .min(Comparator.comparingDouble(Country::getAdultLiteracyRate))
                .get();
    }

    @Override
    public Country maxAdultLiteracy() {

        // Uses a stream to compare each country and find the country with the highest adult literacy rate
        return countries.stream()
                .filter(country -> country.getAdultLiteracyRate() != null)
                .max(Comparator.comparingDouble(Country::getAdultLiteracyRate))
                .get();
    }

    @Override
    public Double avgInternetUsers() {
        Double avg;
        Double sum = 0.00;
        int count = 0;

        // Iterates through the countries and gets a total internet user percentage for all countries
        for (Country country : countries) {
            if (country.getInternetUsers() != null) {
                sum += country.getInternetUsers();
                count++;
            }
        }

        // Calculates the average percentage of internet users for all countries
        avg = sum / count;
        return avg;
    }

    @Override
    public Double avgAdultLiteracy() {
        Double avg;
        Double sum = 0.00;
        int count = 0;

        // Iterates through the countries and gets a total adult literacy rate for all countries
        for (Country country : countries) {
            if (country.getAdultLiteracyRate() != null) {
                sum += country.getAdultLiteracyRate();
                count++;
            }
        }

        // Calculates the average adult literacy rate for all countries
        avg = sum / count;
        return avg;
    }

    @Override
    public Double getCorrelationCoefficient() {
        int n = 0;
        Double internetUsers;
        Double adultLiteracy;
        Double correlationCoefficient;
        Double multiplied;
        Double internetSquared;
        Double literacySquared;

        Double sumInternetUsers = 0.00;
        Double sumAdultLiteracy = 0.00;
        Double sumMultiplied = 0.00;
        Double sumInternetSquared = 0.00;
        Double sumLiteracySquared = 0.00;

        // Iterates through all countries in order to calculate the corelation coefficient between internet users and adult literacy rate
        for (Country country : countries) {

            // If a country has a null value for internet users or adult literacy rate, that country is excluded from the calculation
            if (country.getInternetUsers() != null && country.getAdultLiteracyRate() != null) {

                // Stores internet users and adult literacy rate for the current country
                internetUsers = country.getInternetUsers();
                adultLiteracy = country.getAdultLiteracyRate();

                // Calculates internet users * adult literacy rate for each country
                multiplied = internetUsers * adultLiteracy;

                // Calculates internet users squared for each country
                internetSquared = internetUsers * internetUsers;

                // Calculates adult literacy rate squared for each country
                literacySquared = adultLiteracy * adultLiteracy;

                // Iterates the sum of internet user percentages
                sumInternetUsers += internetUsers;

                // Iterates the sum of adult literacy rates
                sumAdultLiteracy += adultLiteracy;

                // Iterates the sum of internet user percentage * adult literacy rate
                sumMultiplied += multiplied;

                // Iterates the sum of internet user percentages squared
                sumInternetSquared += internetSquared;

                // Iterates the sum of adult literacy rates squared
                sumLiteracySquared += literacySquared;
                n++;
            }
        }

        // Breaks the correlation coefficient equation into separate parts
        Double covalence = (n * sumMultiplied - (sumInternetUsers * sumAdultLiteracy));
        Double sigmax = sumInternetSquared - (sumInternetUsers * sumInternetUsers);
        Double sigmay = sumLiteracySquared - (sumAdultLiteracy * sumAdultLiteracy);

        // Final calculation of the correlation coefficient equation
        correlationCoefficient =  covalence / Math.sqrt(sigmax * sigmay);

        return correlationCoefficient;
    }

    // Returns a value of true if the correlation coefficient is positive
    @Override
    public boolean isPositive() {
        return getCorrelationCoefficient() >= 0;
    }
}
