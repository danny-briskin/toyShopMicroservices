package ua.com.univerpulse.toyshop.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
@AllArgsConstructor
@Data
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;
}
