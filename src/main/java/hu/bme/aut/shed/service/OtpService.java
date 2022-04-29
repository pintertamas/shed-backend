package hu.bme.aut.shed.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private static final Integer EXPIRE_MINS = 10;
    private final LoadingCache<String, Integer> otpCache;

    public OtpService() {
        otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<>() {
            public Integer load(String email) {
                return 0;
            }
        });
    }

    public int generateOTP(String email) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(email, otp);
        return otp;
    }

    private int getOtp(String email) {
        try {
            return otpCache.get(email);
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).error(e.getMessage());
            return 0;
        }
    }

    public boolean validateOtp(String email, int otp) {
        if (otp >= 0) {
            int serverOtp = getOtp(email);
            if (serverOtp > 0) {
                if (otp == serverOtp) {
                    clearOtp(email);
                    return true;
                }
            }
        }
        return false;
    }

    private void clearOtp(String email) {
        otpCache.invalidate(email);
    }
}
