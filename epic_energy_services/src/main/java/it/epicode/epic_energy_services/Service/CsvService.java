package it.epicode.epic_energy_services.Service;

import com.opencsv.exceptions.CsvValidationException;
import it.epicode.epic_energy_services.Exception.NotFoundException;
import it.epicode.epic_energy_services.entity.Comune;
import it.epicode.epic_energy_services.entity.Provincia;
import it.epicode.epic_energy_services.repository.ComuneRepository;
import it.epicode.epic_energy_services.repository.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvService {

    @Autowired
    private ComuneRepository comuneRepository;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    public void loadCsvData(String comuniFilePath, String provinceFilePath) throws IOException {
        Map<String, Provincia> provinciaMap = loadProvinceFromCsv(provinceFilePath);
        List<Comune> comuniList = loadComuniFromCsv(comuniFilePath, provinciaMap);
        provinciaRepository.saveAll(provinciaMap.values());
        comuneRepository.saveAll(comuniList);
    }

    private Map<String, Provincia> loadProvinceFromCsv(String filePath) throws IOException {
        Map<String, Provincia> provinciaMap = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                Provincia provincia = new Provincia();
                provincia.setSigla(line[0]);
                provincia.setProvincia(line[1]);
                provincia.setRegione(line[2]);
                provinciaMap.put(line[1], provincia);
            }
        } catch (CsvValidationException e) {
            throw new NotFoundException("Impossibile caricare le province nel file");
        }
        return provinciaMap;
    }

    private List<Comune> loadComuniFromCsv(String filePath, Map<String, Provincia> provinciaMap) throws IOException {
        List<Comune> comuniList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                Comune comune = new Comune();
                comune.setCodiceProvincia(line[0]);
                comune.setProgressivoComune(line[1]);
                comune.setDenominazione(line[2]);
                Provincia provincia = provinciaMap.get(line[3]);
                if (provincia != null) {
                    comune.setProvincia(provincia);
                    provincia.getComuni().add(comune);
                }
                comuniList.add(comune);
            }
        } catch (CsvValidationException e) {
            throw new NotFoundException("Impossibile caricare i comuni nel file");
        }
        return comuniList;
    }
}
