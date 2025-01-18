package BankingManagementSystem.BankingManagementSystem.customer;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public CustomerDto registerCustomer(CustomerRegistrationDto customerRegistrationDto) {
        if (customerRepository.findByFin(customerRegistrationDto.getFin()).isPresent()) {
            throw new IllegalArgumentException("Fin is already in use!");
        }

        Customer customer = new Customer();
        customer.setFirstName(customerRegistrationDto.getFirstName());
        if(customerRegistrationDto.getFin().length()!=7){
            throw new IllegalArgumentException("Fin must consist of 7 characters");
        }
        customer.setLastName(customerRegistrationDto.getLastName());
        customer.setFin(customerRegistrationDto.getFin());
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
        customerDto.setFin(customer.getFin());
        customerDto.setCustomerRole(customer.getCustomerRole());
        customerDto.setPhone(customer.getPhone());
        customerDto.setAddress(customer.getAddress());
        customerDto.setRegistrationDate(customer.getRegistrationDate());
        customerDto.setDeleted(customer.isDeleted());

        return customerDto;
    }




}
