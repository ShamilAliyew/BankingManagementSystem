package BankingManagementSystem.BankingManagementSystem.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;


    private final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public String registerCustomer(CustomerRegistrationDto customerDto) {
        if (customerRepository.findByEmail(customerDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setEmail(customerDto.getEmail());
//        String hashedPassword = passwordEncoder.encode(customerDto.getPassword());
//        customer.setPassword(hashedPassword);
        customer.setPhone(customerDto.getPhone());
        customer.setAddress(customerDto.getAddress());
        customer.setRegistrationDate(LocalDateTime.now());
        customer.setDeleted(false);

        customerRepository.save(customer);
        return "Customer registered successfully!";
    }

}
