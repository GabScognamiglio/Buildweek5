package it.epicode.epic_energy_services.Controller;

import it.epicode.epic_energy_services.Service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CsvController {

    @Autowired
    private CsvService csvService;




    @PostMapping("/update-csv")
    public String updateCsv(@RequestParam("comuniFilePath") String comuniFilePath, @RequestParam("provinceFilePath") String provinceFilePath) {
        try {
            csvService.loadCsvData(comuniFilePath, provinceFilePath);
            return "CSV files data loaded successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while loading CSV files data.";
        }
    }
}
