package it.epicode.epic_energy_services;

import it.epicode.epic_energy_services.Service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@EnableJpaRepositories
public class CsvMain {
@Autowired
private CsvService csvService;


    public void csvLoad(String comuniFilePath, String provinceFilePath) throws IOException {
        csvService.loadCsvData(comuniFilePath, provinceFilePath);
    }

    public static void main(String[] args) throws IOException {
        ApplicationContext context = SpringApplication.run(CsvMain.class, args);
        CsvMain csvMain = context.getBean(CsvMain.class);

        // Esempio di utilizzo del metodo csvLoad con file di esempio
        String comuniFilePath = "./comuni-italiani.csv";
        String provinceFilePath = "./province-italiane.csv";
        csvMain.csvLoad(comuniFilePath, provinceFilePath);
    }



}
