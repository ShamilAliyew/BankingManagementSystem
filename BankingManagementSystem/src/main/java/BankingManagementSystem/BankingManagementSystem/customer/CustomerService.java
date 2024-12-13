package BankingManagementSystem.BankingManagementSystem.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public String registerCustomer(CustomerRegistrationRequest request) {
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPassword(request.getPassword()); // Şifrə hashing'i əlavə etməyi unutmayın!
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        customerRepository.save(customer);
        return "Customer registered successfully!";
    }

}
