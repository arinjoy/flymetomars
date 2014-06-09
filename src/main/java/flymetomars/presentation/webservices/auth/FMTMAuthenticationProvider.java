package flymetomars.presentation.webservices.auth;

import flymetomars.business.services.AuthService;
import flymetomars.business.services.NoSuchPersonExistsException;
import java.util.Arrays;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Provides a basic AuthenticationProvider which internally wraps the business
 * layer AuthService via dependency injected JavaBean
 * 
 * @author Lawrence Colman
 */
public class FMTMAuthenticationProvider implements AuthenticationProvider {

    private AuthService authService;
    
    public FMTMAuthenticationProvider(AuthService authService) {
        this.authService = authService;
    }
    
    @Override
    public Authentication authenticate(Authentication aAuth) throws AuthenticationException {
        if(null==this.authService) {
            throw new AuthenticationServiceException("Required component not found");
        }
        UsernamePasswordAuthenticationToken auth;
        try {
            auth=(UsernamePasswordAuthenticationToken)aAuth;
        } catch (ClassCastException cce) {
            return null;
        }
        String username,plainPass;
        try {
            username=(String)auth.getPrincipal();
            if(null==username) { throw new ClassCastException(); }
        } catch (ClassCastException cce) {
            throw new UsernameNotFoundException("null or invalid-typed username given", cce);
        }
        try {
            plainPass=(String)auth.getCredentials();
            if(null==plainPass) { throw new ClassCastException(); }
        } catch (ClassCastException cce) {
            throw new BadCredentialsException("null or invalid-typed password given", cce);
        }
        boolean result;
        try {
            result = authService.authenticate(username, plainPass);
        } catch (NoSuchPersonExistsException ex) {
            throw new UsernameNotFoundException("Not Registered",ex);
        }
        if(result) {
            auth=new UsernamePasswordAuthenticationToken(username,plainPass,
                Arrays.asList(new GrantedAuthority[]{
                    new GrantedAuthorityImpl("User")
                })
            );
            return auth;
        }
        throw new AuthenticationException("Invalid Username or Password"){};
    }

    @Override
    public boolean supports(Class<?> type) {
        final Class<UsernamePasswordAuthenticationToken> desired=UsernamePasswordAuthenticationToken.class;
        if(type.equals(desired)) { return true; }
        try {
            return null!=type.asSubclass(desired);
        } catch (ClassCastException cce) {
            return false;
        }
    }

}
