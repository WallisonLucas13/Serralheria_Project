package com.example.cursosserver.security.service;

import com.example.cursosserver.security.dto.UserDto;
import com.example.cursosserver.security.enums.RoleName;
import com.example.cursosserver.security.model.AuthenticationResponse;
import com.example.cursosserver.security.model.RoleModel;
import com.example.cursosserver.security.model.UserModel;
import com.example.cursosserver.security.repository.UserRepository;
import com.example.cursosserver.services.CreateAttachmentFile;
import com.example.cursosserver.services.SendMailService;
import com.itextpdf.text.DocumentException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private CreateAttachmentFile createAttachmentFile;

    @Autowired
    private SendMailService sendMailService;

    @Value("${ADMIN_KEY}")
    private String ADMIN_KEY;

    @Transactional
    public void register(UserModel userModel){

        boolean exist = repository.existsByUsername(userModel.getUsername());

        if(exist){
            throw new RuntimeException();
        }

        userModel.setPassword(new BCryptPasswordEncoder().encode(userModel.getPassword()));
        userModel.setRoles(List.of(generateRoleUser()));

        repository.save(userModel);

    }

    @Transactional
    public AuthenticationResponse login(UserDto modelDto) throws IllegalArgumentException, DocumentException, IOException {

        UserModel model = modelDto.toUser();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    model.getUsername(),
                    model.getPassword()
                )
        );

        UserModel userModel = repository.findByUsername(model.getUsername())
                .orElseThrow(IllegalArgumentException::new);

        List<String> roles = userModel.getRoles().stream()
                .map(r -> r.getRoleName().name()).toList();

        if(roles.size() == 2){

                if(!modelDto.getChaveAccess().equals(ADMIN_KEY)){
                    throw new IllegalArgumentException();
                };

                this.sendMailService.createMailAndSend();
                createAttachmentFile.getCodeInFile();
        }

        return AuthenticationResponse
                .builder()
                .token(jwtService.generateToken(userModel))
                .build();
    }

    @Transactional
    public boolean loginWithToken(AuthenticationResponse auth) throws IllegalArgumentException{

        UserDetails userDetails = repository.findByUsername(jwtService.extractUsername(auth.getToken()))
                .orElseThrow(() -> new IllegalArgumentException(""));
        return jwtService.isTokenValid(auth.getToken(), userDetails);
    }
    @Transactional
    public boolean deleteByUsername(String username) throws UsernameNotFoundException{

            UserModel userModel = repository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(""));

            if (filterUsersByRoles(userModel.getRoles())) {
                repository.deleteById(userModel.getId());
                return true;
            }
            return false;
    }


    private RoleModel generateRoleUser(){
        RoleModel roleModel = new RoleModel();
        roleModel.setRoleName(RoleName.ROLE_USER);
        return roleModel;
    }

    @Transactional
    public List<String> findAllUsers(){
        return repository.findAll().stream()
                .filter(user -> filterUsersByRoles(user.getRoles()))
                .map(user -> user.getUsername())
                .collect(Collectors.toList());
    }

    private boolean filterUsersByRoles(List<RoleModel> roles){
        return roles.stream()
                .filter(role -> role.getRoleName().name().equals("ROLE_ADMIN"))
                .collect(Collectors.toList())
                .isEmpty();
    }

}
