package vttp2022.com.ssfminiproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vttp2022.com.ssfminiproject.config.JwtTokenUtil;
import vttp2022.com.ssfminiproject.model.JwtRequest;
import vttp2022.com.ssfminiproject.model.JwtResponse;
import vttp2022.com.ssfminiproject.model.User;
import vttp2022.com.ssfminiproject.repository.UserRepository;
import vttp2022.com.ssfminiproject.service.JwtUserDetailsService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtUtil;

	@Autowired
	JwtUserDetailsService uSvc;

	@Autowired
	UserRepository uRepo;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest user) throws Exception {
			
			authenticate(user.getUsername(), user.getPassword());
			final UserDetails userDetails = uSvc
				.loadUserByUsername(user.getUsername());
			System.out.println();
			final String token = jwtUtil.generateToken(userDetails);
			System.out.println(token);
			return ResponseEntity.ok(new JwtResponse(token));
		
	}

	@RequestMapping(value = "/api/register", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody User nUser) throws Exception {
		System.out.println("email:" + nUser.getEmail());
		if (uRepo.getByUsername(nUser.getUsername()) != null || uRepo.getByEmail(nUser.getEmail()) != null) {
			System.out.println("Email and username already in use!");
			return new ResponseEntity<String>("Email or username already in use!", HttpStatus.FORBIDDEN);
			
		} else {
			uSvc.save(nUser);
			return ResponseEntity.ok(HttpStatus.CREATED);
		}

	}   

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
