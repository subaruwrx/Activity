package cput.za.ac.ecommerce_clothingapp.factory;

import java.util.ArrayList;

import cput.za.ac.ecommerce_clothingapp.domain.Address;

/**
 * Created by Admin on 2016-06-06.
 */
public interface DatasourceDAO {
    public void createAddress(Address address);
    public void updateAddress(Address address);
    public Address findAddress(Long id);
    public void deleteAddress(Address address);
    public ArrayList<Address> getAddress();
}
