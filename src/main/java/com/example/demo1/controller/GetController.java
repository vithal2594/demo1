package com.example.demo1.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;

@RestController
@RequestMapping("/getMappig/apis")
public class GetController {

    public GetController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Autowired
    private  final UserRepository userRepository;
    
  @GetMapping
  public List<User> getAllUsers(){
    return userRepository.findAll();
  }
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserbyid (@PathVariable Long id){
    return userRepository.findById(id)
    .map(ResponseEntity::ok).
    orElse(ResponseEntity.notFound().build());
  }
  @GetMapping("/re/{id}")
  public ResponseEntity<User> getUserbyidre (@RequestParam(required = true) Long id){
    return userRepository.findById(id)
    .map(ResponseEntity::ok).
    orElse(ResponseEntity.notFound().build());
  }
  @GetMapping("/rtrue/search")
  public ResponseEntity<List<User>> searchuse(@RequestParam(required=true) String name){
    List<User>users;
    if(name == null || name.isBlank()){
      users=userRepository.findAll();
    }else{
      users=userRepository.findByNameContainingIgnoreCase(name);
    }
    if(users.isEmpty()){
      return ResponseEntity.notFound().build();
    }else{
      return ResponseEntity.ok(users);
    }
  }
  @GetMapping("/rfalse/search")
  public ResponseEntity<List<User>> serachuserf(@RequestParam(required=false) String name){
     List<User>users;
     if(name == null || name.isEmpty()){
        users=userRepository.findAll();
     }else{
      users=userRepository.findByNameContainingIgnoreCase(name);
     }
     if(users.isEmpty()){
      return ResponseEntity.notFound().build();
     }else{
      return ResponseEntity.ok(users);
     }
  }
  @GetMapping("/rfa/filter")
  public List<User>filetrusersnyor(@RequestParam(required=false) String name, @RequestParam(required=false) String email){
    return userRepository.findByNameOrEmail(name,email);
  }

  @GetMapping("/rtr/filter")
  public List<User> filteruserbyand(@RequestParam(required=true) String name, @RequestParam(required=true) String email){
     return userRepository.findByNameAndEmail(name,email);
  }
  @GetMapping("/filter")
public List<User> filterUsers(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String email) {

    if (name != null && email != null) {
        return userRepository.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(name, email);
    } else if (name != null) {
        return userRepository.findByNameContainingIgnoreCase(name);
    } else if (email != null) {
        return userRepository.findByEmailContainingIgnoreCase(email);
    } else {
        return userRepository.findAll();
    }
  }

  // GET with RequestParam Map
    @GetMapping("/filter-map")
    public List<User> filterUsersMap(@RequestParam Map<String, String> params) {
        String name = params.get("name");
        String email = params.get("email");
        return userRepository.findByNameAndEmail(name, email);
    }

    @GetMapping("/by-ids")
    public List<User> getUsersByIds(@RequestParam List<Long> ids) {
        return userRepository.findAllById(ids);
    }
    // @GetMapping("/header")
    // public List<User> getUsersByHeader(@RequestHeader("X-Role") String role) {
    //     return userRepository.findByRole(role);
    // }

        //  GET with Pagination & Sorting

//         {
//   "page": 0,
//   "size": 2,
//   "sort": ["name,asc"]
// }

    @GetMapping("/paged")
    public Page<User> getUsersPaged(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // 9. GET with ResponseEntity - no stream, explicit logic
    @GetMapping("/{id}/optional")
    public ResponseEntity<User> getUserOptional(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

}
