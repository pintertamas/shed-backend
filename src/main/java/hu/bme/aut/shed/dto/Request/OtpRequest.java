package hu.bme.aut.shed.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtpRequest {
    String email;
    String password;
    int otp;
}
