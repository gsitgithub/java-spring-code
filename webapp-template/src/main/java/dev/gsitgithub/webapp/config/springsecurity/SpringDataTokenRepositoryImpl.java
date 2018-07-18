package dev.gsitgithub.webapp.config.springsecurity;

import dev.gsitgithub.webapp.config.utils.DateUtil;
import dev.gsitgithub.webapp.entity.RememberMeToken;
import dev.gsitgithub.webapp.repository.RememberMeTookenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Slf4j
public class SpringDataTokenRepositoryImpl implements PersistentTokenRepository {

    @Inject
    RememberMeTookenRepository rememberMeTookenRepository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        RememberMeToken rememberMeToken = new RememberMeToken(token.getUsername(), token.getSeries(), token.getTokenValue(), DateUtil.toZonedDateTime(token.getDate()));
        rememberMeTookenRepository.save(rememberMeToken);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        RememberMeToken rememberMeToken = rememberMeTookenRepository.findBySeries(series);
        rememberMeToken.setToken(tokenValue);
        rememberMeToken.setLastUsed(DateUtil.toZonedDateTime(lastUsed));
        rememberMeTookenRepository.save(rememberMeToken);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {
        RememberMeToken rememberMeToken = rememberMeTookenRepository.findBySeries(series);
        if (rememberMeToken == null) {
            log.debug("No remember me token for series '{}' was found.", series);
            return null;
        }
        return new PersistentRememberMeToken(rememberMeToken.getUsername(), rememberMeToken.getSeries(), rememberMeToken.getToken(), DateUtil.toDate(rememberMeToken.getLastUsed()));
    }

    @Override
    public void removeUserTokens(String username) {
        List<RememberMeToken> rememberMeTookens = rememberMeTookenRepository.findByUsername(username);
        for (RememberMeToken rememberMeToken : rememberMeTookens) {
            rememberMeTookenRepository.delete(rememberMeToken);
        }
    }

}
