package BankingManagementSystem.BankingManagementSystem.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customer")
public class CustomerController {

    private final CustomerService customerService;
    
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerDto> registerCustomer(@RequestBody CustomerRegistrationDto customerRegistrationDto) {
        try {
            CustomerDto customerDto = customerService.registerCustomer(customerRegistrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
