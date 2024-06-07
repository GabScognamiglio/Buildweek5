package it.epicode.epic_energy_services.Service;

import com.cloudinary.Cloudinary;
import it.epicode.epic_energy_services.DTO.UserDTO;
import it.epicode.epic_energy_services.Enums.Role;
import it.epicode.epic_energy_services.Exception.BadRequestException;
import it.epicode.epic_energy_services.Exception.NotFoundException;
import it.epicode.epic_energy_services.Exception.UserNotFoundException;
import it.epicode.epic_energy_services.entity.User;
import it.epicode.epic_energy_services.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

   @Autowired(required = false)
  private UserRepository userRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserByUsername(String username){
        Optional<User> userOptional =  userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            return userOptional.get();
        }else{
            throw new NotFoundException("User with username: "+username+" not found");
        }
    }

    public User getUserByEmail(String email){
        Optional<User> userOptional =  userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            return userOptional.get();
        }else{
            throw new NotFoundException("User with email: "+email+" not found");
        }
    }

    public Optional<User> getUserById(int id){
        Optional<User> userOptional =  userRepository.findById(id);
        if(userOptional.isPresent()){
            return userRepository.findById(id);
        }
       else{
            throw new NotFoundException("User with id: "+id+" not found");
        }
    }


    public Page<User> getUserConPaginazione(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy));
        return  userRepository.findAll(pageable);
    }
    public List<User> getUsers(){
        return  userRepository.findAll();
    }



    public String saveUser(UserDTO userDTO){


        Optional<User> userOptional =  userRepository.findByEmail(userDTO.getEmail());

        if(userOptional.isPresent()){
            throw new BadRequestException("Questa email è già associata ad un account!");

        }else{
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setName(userDTO.getName());
            user.setSurname(userDTO.getSurname());

            //non è obbligatorio inserire foto in fase di registrazione
            user.setPictureProfile(userDTO.getPictureProfile());

            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            // aggiungo ruolo ceh di default sara USER
            user.setRole(Role.USER);
            userRepository.save(user);
            sendMailCreazioneProfilo(user.getEmail());
            return "Utente aggiunto, id: " + user.getId()+" ,ruolo: "+user.getRole();
        }

    }

    public User updateUser(UserDTO userUpdate, int id) throws UserNotFoundException {
        Optional<User> userOpt = getUserById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(userUpdate.getUsername());
            user.setName(userUpdate.getName());
            user.setSurname(userUpdate.getSurname());
            user.setEmail(userUpdate.getEmail());
            user.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException("Non risulta nessun utente con il seguente id: "+ id);
        }
    }


    public User updateUserParse(int id, Map<String, Object> update) throws UserNotFoundException {
        Optional<User> userOpt = getUserById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            update.forEach((key, value) -> {
                switch (key) {
                    case "username":
                        user.setUsername((String) value);
                        break;
                    case "name":
                        user.setName((String) value);
                        break;
                    case "surname":
                        user.setSurname((String) value);
                        break;
                    case "email":
                        user.setEmail((String) value);
                        break;
                    case "pictureProfile":
                        user.setPictureProfile((String) value);
                        break;
                    case "password":
                    user.setPassword(passwordEncoder.encode((CharSequence) value));
                    sendMailModificaPassword(user.getEmail());
                    default:
                        throw new IllegalArgumentException("Campo non valido: " + key);
                }
            });

            userRepository.save(user);

            return user;
        } else {
            throw new UserNotFoundException("Nessun utente trovato con id: " + id);
        }
    }



    public String deleteUser(int id) throws UserNotFoundException {
        Optional<User> userOpt = getUserById(id);

        if (userOpt.isPresent()) {
            userRepository.delete(userOpt.get());
            return "Utente con id: "+ id + " eliminato dal database";
        } else {
            throw new UserNotFoundException("Nessun dipendente trovato con matricola: "+id);
        }


    }



    public String patchPictureProfileUser(int id, MultipartFile foto) throws UserNotFoundException, IOException {
        Optional<User> userOptional = getUserById(id);
        if (userOptional.isPresent()){
            String url =(String) cloudinary.uploader().upload(foto.getBytes(), Collections.emptyMap()).get("url");
            User user = userOptional.get();
            user.setPictureProfile(url);
            userRepository.save(user);
            return "Immagine profilo aggiornata!";
        }else{
            throw new UserNotFoundException("Impossibile impostare immagine del profilo, non è stato trovato nessun utente con matricola: "+id);
        }
    }




    private void sendMailCreazioneProfilo(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Account creato correttamente");
        message.setText("Ora puo accedere al sistema");

        javaMailSenderImpl.send(message);
    }


    private void sendMailModificaPassword(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Richiesta modifica password");
        message.setText("Attenzione, la tua password è stata modificata, se non sei stato tu a richiederlo, invia una segnalazione!");

        javaMailSenderImpl.send(message);
    }



}
