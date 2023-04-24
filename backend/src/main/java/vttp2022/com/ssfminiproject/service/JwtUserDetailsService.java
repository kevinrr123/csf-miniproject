package vttp2022.com.ssfminiproject.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vttp2022.com.ssfminiproject.model.User;
import vttp2022.com.ssfminiproject.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder pEncoder;
    
    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.getByUsername(username);
		//System.out.println("user: " + user.getUsername());
		if (user.getUsername() == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
		//System.out.println("user: " + user.getUsername());
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}

	public Boolean save(User user) {
		User nUser = new User();
		nUser.setUsername(user.getUsername());
		nUser.setPassword(pEncoder.encode(user.getPassword()));
		nUser.setEmail(user.getEmail());
		return userRepo.save(nUser);
	}
}
