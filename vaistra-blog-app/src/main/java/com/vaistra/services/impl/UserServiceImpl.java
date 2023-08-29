package com.vaistra.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaistra.dto.UserDTO;
import com.vaistra.entities.User;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.NoDataFoundException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.UserRepository;
import com.vaistra.services.EmailService;
import com.vaistra.services.UserService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final String FOLDER_PATH = "C:\\Users\\admin07\\Desktop\\mavendemo\\vaistra-blog-app\\rec\\Images\\";

    private final ObjectMapper mapper;

    private final AppUtils appUtilsComment;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, ObjectMapper mapper, AppUtils appUtilsComment) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.mapper = mapper;
        this.appUtilsComment = appUtilsComment;
    }

    @Override
    public List<UserDTO> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {

        return appUtilsComment.usersToDtos(userRepository.findAll());
    }

   /* @Override
    public List<UserDTO> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
        return null;
    }*/

    @Override
    public UserDTO addUser(String userDtoStr, MultipartFile file) throws IOException {

        UserDTO dto = mapper.readValue(userDtoStr,UserDTO.class);

        User user = appUtilsComment.dtoToUser(dto);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        String name = FOLDER_PATH+file.getOriginalFilename();

        user.setImagePath(file.getOriginalFilename());

        user.setCreatedAt(LocalDateTime.now());

        if(!userRepository.existsByUsername(dto.getUsername()) && !userRepository.existsByEmail(dto.getEmail())) {

            if(!file.isEmpty())
                userRepository.save(user);
            else
                throw new NoDataFoundException("Please Upload File");
        }

        else {
            throw new DuplicateEntryException("Username or Email is Already defined Please Enter Some Unique Value");
        }

        file.transferTo(new File(name));

        return appUtilsComment.userToDto(user);
    }

    @Override
    public String eml(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No User Found with ID + " + id));

        emailService.sendSimpleMailMessage(user.getUsername(),user.getEmail());

        return "An Email is sent to your Email address for Verification Please check it and Update your Password";
    }

    @Override
    public UserDTO verification(String newpwd, long token, int id) {
        if(appUtilsComment.verify(token)){
            User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No User Found with Id : " + id));

            user.setPassword(newpwd);

            userRepository.save(user);

            return appUtilsComment.userToDto(user);
        }
        else {
            throw new ResourceNotFoundException("Otp doesn't match");
        }
    }

    @Override
    public UserDTO getById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No User Found with ID : "+id));
        return appUtilsComment.userToDto(user);
    }

}