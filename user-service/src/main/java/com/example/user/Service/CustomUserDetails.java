package com.example.user.Service;

import com.example.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.example.user.constants.UserConstants.AUTHORITIES_DELIMITER;

@Service
public class CustomUserDetails implements UserDetails {


   private User user;

  public CustomUserDetails(User user) {
       this.user = user;
   }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] allAuthorities = this.user.getAuthorities().split(AUTHORITIES_DELIMITER);
        return Arrays.stream(allAuthorities)
                .map(x -> new SimpleGrantedAuthority(x))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
