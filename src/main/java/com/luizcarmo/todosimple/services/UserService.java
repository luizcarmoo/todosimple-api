package com.luizcarmo.todosimple.services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luizcarmo.todosimple.models.User;
import com.luizcarmo.todosimple.models.enums.ProfileEnum;
import com.luizcarmo.todosimple.repositories.UserRepository;
import com.luizcarmo.todosimple.security.UserSpringSecurity;
import com.luizcarmo.todosimple.services.exceptions.AuthorizationException;
import com.luizcarmo.todosimple.services.exceptions.DataBindingViolationException;
import com.luizcarmo.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserRepository userRepository;

  public User findById(Long id){
    UserSpringSecurity userSpringSecurity = authenticated();
      if (!Objects.nonNull(userSpringSecurity) 
      || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId()))
      throw new AuthorizationException("Access denied!");

    Optional<User> user = this.userRepository.findById(id);
    return user.orElseThrow(() -> new ObjectNotFoundException(
      "User not found! Id: " + id + ", Type: " + User.class.getName()
    ));
  }

  @Transactional
    public User create(User obj) {
      obj.setId(null);
      obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
      obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
      obj = this.userRepository.save(obj);
      return obj;
    }
    
    @Transactional
    public User update(User obj) {
      User newObj = findById(obj.getId());
      newObj.setPassword(obj.getPassword());
      newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
    return this.userRepository.save(newObj);
  }

  public void delete(Long id) {
    findById(id);
    try {
      this.userRepository.deleteById(id);
    } catch (Exception e) {
      throw new DataBindingViolationException("Cannot delete because there are related entities!");
    }
  }

  public static UserSpringSecurity authenticated() {
    try {
      return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    } catch (Exception e) {
        return null;
    }
  }

}
