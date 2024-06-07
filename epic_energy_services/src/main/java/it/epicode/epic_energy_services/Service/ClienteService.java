package it.epicode.epic_energy_services.Service;

import com.cloudinary.Cloudinary;
import it.epicode.epic_energy_services.DTO.ClienteDto;
import it.epicode.epic_energy_services.DTO.IndirizzoDto;
import it.epicode.epic_energy_services.DTO.UserDTO;
import it.epicode.epic_energy_services.Enums.Role;
import it.epicode.epic_energy_services.Enums.TipoCliente;
import it.epicode.epic_energy_services.Enums.TipoIndirizzo;
import it.epicode.epic_energy_services.Exception.BadRequestException;
import it.epicode.epic_energy_services.Exception.ClienteNotFoundException;
import it.epicode.epic_energy_services.Exception.NotFoundException;
import it.epicode.epic_energy_services.Exception.UserNotFoundException;
import it.epicode.epic_energy_services.entity.*;
import it.epicode.epic_energy_services.repository.ClienteRepository;
import it.epicode.epic_energy_services.repository.ComuneRepository;
import it.epicode.epic_energy_services.repository.IndirizzoRepository;
import it.epicode.epic_energy_services.repository.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private IndirizzoService indirizzoService;

    @Autowired
    private ComuneRepository comuneRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private IndirizzoRepository indirizzoRepository;

    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;


    public List<Cliente> clientiOrderByProvinciaSedeLegale() {
        return clienteRepository.clientiOrderByProvinciaSedeLegale();
    }

    public List<Cliente> clientiFilteredByFatturatoAnnuale(double fatturatoMin, double fatturatoMax) {
        return clienteRepository.clientiFilteredByFatturatoAnnuale(fatturatoMin, fatturatoMax);
    }

    public List<Cliente> getClientiByDataInserimento(LocalDate dataInserimento) {
        return clienteRepository.findByDataInserimento(dataInserimento);
    }

    public List<Cliente> getClientiByDataUltimoContatto(LocalDate dataUltimoContatto) {
        return clienteRepository.findByDataUltimoContatto(dataUltimoContatto);
    }


    public List<Cliente> getClientiByNameContaining(String parteDelNome) {
        return clienteRepository.findByRagioneSocialeContaining(parteDelNome);
    }


    public Optional<Cliente> getClienteById(int id) {
        return clienteRepository.findById(id);
    }


    public Page<Cliente> getClientiConPaginazione(int page, int size, String sortBy, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
        return clienteRepository.findAll(pageable);
    }

    public List<Cliente> getClienti() {
        return clienteRepository.findAll();
    }


    public String saveCliente(ClienteDto clienteDto) {

        Optional<Cliente> clienteOptional = clienteRepository.findByEmail(clienteDto.getEmail());

        if (clienteOptional.isPresent()) {
            throw new BadRequestException("Questa email è già associata ad un account!");
        } else {
            Cliente cliente = new Cliente();

            // INFO CONTATTO
            cliente.setNomeContatto(clienteDto.getNomeContatto());
            cliente.setCognomeContatto(clienteDto.getCognomeContatto());
            cliente.setEmailContatto(clienteDto.getEmailContatto());
            cliente.setTelefonoContatto(clienteDto.getTelefonoContatto());

            // Dati azienda
            cliente.setTipoCliente(clienteDto.getTipoCliente());
            cliente.setDataInserimento(LocalDate.now());
            cliente.setEmail(clienteDto.getEmail());
            cliente.setRagioneSociale(clienteDto.getRagioneSociale());
            cliente.setPartitaIva(clienteDto.getPartitaIva());
            cliente.setFatturatoAnnuale(clienteDto.getFatturatoAnnuale());
            cliente.setPec(clienteDto.getPec());
            cliente.setTelefono(clienteDto.getTelefono());

            clienteRepository.save(cliente);

            // Controllo indirizzi
            long countIndirizziLegali = clienteDto.getIndirizzi().stream()
                    .filter(indirizzo -> indirizzo.getTipoIndirizzo() == TipoIndirizzo.LEGALE)
                    .count();

            if (countIndirizziLegali == 1) {
                List<Indirizzo> indirizzi = clienteDto.getIndirizzi().stream().map(indirizzoDto -> {
                    Indirizzo indirizzo = new Indirizzo();
                    indirizzo.setVia(indirizzoDto.getVia());
                    indirizzo.setCap(indirizzoDto.getCap());
                    indirizzo.setLocalita(indirizzoDto.getLocalita());
                    indirizzo.setCivico(indirizzoDto.getCivico());
                    indirizzo.setTipoIndirizzo(indirizzoDto.getTipoIndirizzo());
                    indirizzo.setCliente(cliente);

                    Optional<Comune> comuneOptional = comuneRepository.findByDenominazione(indirizzoDto.getComuneDenominazione());
                    if (comuneOptional.isPresent()) {
                        indirizzo.setComune(comuneOptional.get());
                        indirizzoRepository.save(indirizzo);
                        return indirizzo;
                    } else {
                        throw new NotFoundException("Comune non trovato");
                    }
                }).collect(Collectors.toList());

                LocalDate oraCorrente =  LocalDate.now();
                cliente.setIndirizzi(indirizzi);
                cliente.setDataUltimoContatto(oraCorrente);
                clienteRepository.save(cliente);

                sendMailClienteAggiunto(cliente.getEmail());
                sendMailClienteAggiunto(cliente.getPec());
                return "Cliente con id " + cliente.getId() + " salvato correttamente";
            } else {
                throw new BadRequestException("È possibile avere solo un indirizzo legale.");
            }
        }
    }


    public String patchPictureLogoAziendale(int id, MultipartFile foto) throws IOException {
        Optional<Cliente> clienteOptional = getClienteById(id);
        if (clienteOptional.isPresent()){
            String url =(String) cloudinary.uploader().upload(foto.getBytes(), Collections.emptyMap()).get("url");
            Cliente cliente = clienteOptional.get();
            cliente.setLogoAziendale(url);
            clienteRepository.save(cliente);
            return "Logo aziendale aggiornato!";
        }else{
            throw new ClienteNotFoundException("Impossibile impostare il logo aziendale, non è stato trovato nessun cliente con ID: " + id);
        }
    }


    public String deleteCliente(int id) {
        Optional<Cliente> clienteOptional = getClienteById(id);

        if (clienteOptional.isPresent()) {
            clienteRepository.delete(clienteOptional.get());
            return "Cliente con id: "+ id + " è stato eliminato";
        } else {
            throw new ClienteNotFoundException("Nessun cliente trovato con ID: "+id);
        }
    }


    public Cliente updateCliente(int id, ClienteDto clienteDto) {
        Optional<Cliente> clienteOptional = getClienteById(id);

        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();

            cliente.setNomeContatto(clienteDto.getNomeContatto());
            cliente.setCognomeContatto(clienteDto.getCognomeContatto());
            cliente.setEmailContatto(clienteDto.getEmailContatto());
            cliente.setTelefonoContatto(clienteDto.getTelefonoContatto());


            //dati azienda
            cliente.setTipoCliente(clienteDto.getTipoCliente());
            cliente.setDataInserimento(LocalDate.now());
            cliente.setEmail(clienteDto.getEmail());
            cliente.setRagioneSociale(clienteDto.getRagioneSociale());
            cliente.setPartitaIva(clienteDto.getPartitaIva());
            cliente.setFatturatoAnnuale(clienteDto.getFatturatoAnnuale());
            cliente.setPec(clienteDto.getPec());

            List<Indirizzo> indirizzi = clienteDto.getIndirizzi().stream().map(indirizzoDto -> {
                Indirizzo indirizzo = new Indirizzo();
                indirizzo.setVia(indirizzoDto.getVia());
                indirizzo.setCap(indirizzoDto.getCap());

                indirizzo.setLocalita(indirizzoDto.getLocalita());
                indirizzo.setCivico(indirizzoDto.getCivico());
                indirizzo.setTipoIndirizzo(indirizzoDto.getTipoIndirizzo());

                Optional<Comune> comuneOptional = comuneRepository.findByDenominazione(indirizzoDto.getComuneDenominazione());
                if (comuneOptional.isPresent()) {
                    indirizzo.setComune(comuneOptional.get());
                    indirizzoRepository.save(indirizzo);
                    return indirizzo;
                }
                else {throw new NotFoundException("Comune non trovato");}
            }).collect(Collectors.toList());

            cliente.setIndirizzi(indirizzi);
            clienteRepository.save(cliente);
            indirizzoRepository.saveAll(cliente.getIndirizzi());

            return cliente;
        } else {
            throw new ClienteNotFoundException("Nessun cliente trovato con ID: " + id);
        }
    }


    private void sendMailClienteAggiunto(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Benvenuto in Epic Energy System");
        message.setText("I dati della sua azienda sono stati inseriti con successo, il team Epic Energy System la ringrazia per la fiducia");

        javaMailSenderImpl.send(message);
    }
}
