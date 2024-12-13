package BankingManagementSystem.BankingManagementSystem.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Shamil {
    @GetMapping
    public String shamil() {
        return "index  ";
    }
}
