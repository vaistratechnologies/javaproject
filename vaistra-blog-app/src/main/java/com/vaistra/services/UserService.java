package com.vaistra.services;

import com.vaistra.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);

    //public List<UserDTO> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);

    public UserDTO addUser(String userDtoStr, MultipartFile file) throws IOException;

    public  String eml(int id);

    public UserDTO verification(String newpwd, long token, int id);

    public UserDTO getById(int id);
}
