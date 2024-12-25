package BankingManagementSystem.BankingManagementSystem.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public CustomerDto registerCustomer(CustomerRegistrationDto customerRegistrationDto) {
        if (customerRepository.findByEmail(customerRegistrationDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        Customer customer = new Customer();
        customer.setFirstName(customerRegistrationDto.getFirstName());
        customer.setLastName(customerRegistrationDto.getLastName());
        customer.setEmail(customerRegistrationDto.getEmail());
        customer.setCustomerRole(customerRegistrationDto.getCustomerRole());
        customer.setPhone(customerRegistrationDto.getPhone());
        customer.setAddress(customerRegistrationDto.getAddress());
        customer.setRegistrationDate(LocalDateTime.now());
        customer.setDeleted(false);

        Customer savedCustomer=customerRepository.save(customer);
        return convertToCustomerDto(savedCustomer);

    }
    public CustomerDto convertToCustomerDto(Customer customer) {
        CustomerDto customerDto=new CustomerDto();
        customerDto.setCustomerId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setCustomerRole(customer.getCustomerRole());
        customerDto.setPhone(customer.getPhone());
        customerDto.setAddress(customer.getAddress());
        customerDto.setRegistrationDate(customer.getRegistrationDate());
        customerDto.setDeleted(customer.isDeleted());

        return customerDto;
    }




}
