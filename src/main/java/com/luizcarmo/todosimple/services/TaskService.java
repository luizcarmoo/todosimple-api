package com.luizcarmo.todosimple.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luizcarmo.todosimple.models.Task;
import com.luizcarmo.todosimple.models.User;
import com.luizcarmo.todosimple.models.enums.ProfileEnum;
import com.luizcarmo.todosimple.repositories.TaskRepository;
import com.luizcarmo.todosimple.security.UserSpringSecurity;
import com.luizcarmo.todosimple.services.exceptions.AuthorizationException;
import com.luizcarmo.todosimple.services.exceptions.DataBindingViolationException;
import com.luizcarmo.todosimple.services.exceptions.ObjectNotFoundException;


@Service
public class TaskService {
  
  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private UserService userService;

  public Task findById(Long id) {
    Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
      "Task not found! Id " + id + ", Tipo: " + Task.class.getName()));

    UserSpringSecurity userSpringSecurity = UserService.authenticated();
    if (Objects.isNull(userSpringSecurity) 
          || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && userHasTask(userSpringSecurity, task))
      throw new AuthorizationException("Access denied!");

    return task;
  }

  public List<Task> findAllByUser() {
    UserSpringSecurity userSpringSecurity = UserService.authenticated();
    if (Objects.isNull(userSpringSecurity))
      throw new AuthorizationException("Access denied!");
      
    List<Task> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
    return tasks;
  }

  @Transactional
  public Task create(Task obj) {
    UserSpringSecurity userSpringSecurity = UserService.authenticated();
    if (Objects.isNull(userSpringSecurity))
      throw new AuthorizationException("Access denied!");

    User user = this.userService.findById(userSpringSecurity.getId());
    obj.setId(null);
    obj.setUser(user);
    obj = this.taskRepository.save(obj);
    return obj;
  }

  @Transactional
  public Task update(Task obj) {
    Task newObj = findById(obj.getId());
    newObj.setDescription(obj.getDescription());
    return this.taskRepository.save(newObj);
  }

  public void delete(Long id) {
    findById(id);
    try {
      this.taskRepository.deleteById(id);
    } catch (Exception e) {
      throw new DataBindingViolationException("Cannot delete because there are related entities!");
    }
  }

  private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
    return task.getUser().getId().equals(userSpringSecurity.getId());
  }

}
