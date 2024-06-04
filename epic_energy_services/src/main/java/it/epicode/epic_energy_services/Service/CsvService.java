package it.epicode.epic_energy_services.Service;

import com.opencsv.exceptions.CsvValidationException;
import it.epicode.epic_energy_services.CsvMain;
import it.epicode.epic_energy_services.Exception.NotFoundException;
import it.epicode.epic_energy_services.entity.Comune;
import it.epicode.epic_energy_services.entity.Provincia;
import it.epicode.epic_energy_services.repository.ComuneRepository;
import it.epicode.epic_energy_services.repository.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class CsvService {

    @Autowired
    private ComuneRepository comuneRepository;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private ProvinciaService provinciaService;


    public void loadCsvData(String comuniFilePath, String provinceFilePath) throws IOException {
        Map<String, Provincia> provinciaMap = loadProvinceFromCsv(provinceFilePath);
        provinciaRepository.saveAll(provinciaMap.values());
        List<Comune> comuniList = loadComuniFromCsv(comuniFilePath, provinciaMap);
        comuneRepository.saveAll(comuniList);
    }

    private Map<String, Provincia> loadProvinceFromCsv(String filePath) throws IOException {

        Map<String, Provincia> provinciaMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";"); // Assuming ';' as delimiter

                // Validate line length before processing
                if (parts.length < 3) {
                    throw new IOException("Invalid line format: Line must have at least 3 parts (Sigla, Provincia, Regione)");
                }

                // Create a Provincia object using parts
                Provincia provincia = new Provincia();
                provincia.setSigla(parts[0]);
                provincia.setProvincia(parts[1]);
                provincia.setRegione(parts[2]);

                // Add the Provincia object to the map
                provinciaMap.put(provincia.getProvincia().toLowerCase(), provincia); // Case-insensitive lookup
            }
        } catch (IOException e) {
            throw new IOException("Error loading provinces from CSV: " + e.getMessage());
        }

        return provinciaMap;
    }

    private List<Comune> loadComuniFromCsv(String filePath, Map<String, Provincia> provinciaMap) throws IOException {

        List<Comune> comuniList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length < 4) {
                    throw new IOException("Invalid line format: Line must have at least 4 parts");
                }
                Provincia provincia = provinciaService.findByProvincia(parts[3]);
                System.out.println(provincia);

                    Comune comune = new Comune();
                    comune.setCodiceProvincia(parts[0]);
                    if(parts[1].startsWith("#")){
                        comune.setProgressivoComune(null);
                    }else{
                        comune.setProgressivoComune(parts[1]);
                       }
                    comune.setDenominazione(parts[2]);
                    comune.setProvincia(provincia);

                    provincia.getComuni().add(comune);

                    comuniList.add(comune);
                }
        } catch (IOException e) {
            throw new IOException("Error loading comuni from CSV: " + e.getMessage());
        }
        return comuniList;
    }


    public static void main(String[] args) throws IOException {
        CsvService csvService = new CsvService();
        csvService.loadCsvData("./comuni-italiani.csv","./province-italiane.csv");
    }
}
