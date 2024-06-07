package it.epicode.epic_energy_services;

import it.epicode.epic_energy_services.Service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EpicEnergyServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpicEnergyServicesApplication.class, args);
	}

}
