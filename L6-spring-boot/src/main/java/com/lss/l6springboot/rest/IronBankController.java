package com.lss.l6springboot.rest;

import com.lss.l6springboot.dao.MoneyDao;
import com.lss.l6springboot.service.TransferMoneyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IronBankController {

    private final TransferMoneyServiceImpl transferMoney;
    private final MoneyDao moneyDao;

    @GetMapping("/credit")
    public String credit(@RequestParam String name, @RequestParam Long amount) {
        final long resultedDeposit = transferMoney.transfer(name, amount);
        if (resultedDeposit == -1) {
            return "Rejected<br/>" + name + "<b>will't</b> survive this winter";
        }
        return String.format(
                "<i>Credit approved for %s</i> <br/> Current bank balance: <b>%s</b>", name, resultedDeposit);
    }

    @GetMapping("/state")
    public double currentState(){
        return moneyDao.findAll().get(0).getTotalAmount();
    }

}
