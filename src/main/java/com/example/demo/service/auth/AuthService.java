package com.example.demo.service.auth;

import com.example.demo.infra.AuthUserRepository;
import com.example.demo.infra.SnsAccountRepository;
import com.example.demo.model.common.auth.AuthUser;
import com.example.demo.model.common.auth.SnsAccount;
import com.example.demo.model.common.token.UserPrincipal;
import com.example.demo.web.exception.model.CredentialNotMatchException;
import com.example.demo.web.exception.model.DuplicatedEntityException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthUserRepository authUserRepository;
    private final SnsAccountRepository snsAccountRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthUserInfo find(Long id) throws CredentialNotMatchException {

        return authUserRepository.findById(id)
                .map(AuthUser::toInfo)
                .orElseThrow(CredentialNotMatchException::new);
    }

    public AuthUserInfo register(AuthUser user) {

        return authUserRepository.save(user).toInfo();
    }

    public SnsAccountInfo register(String email, SnsAccount snsAccount) throws DuplicatedEntityException {
        AuthUser authUser = authUserRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        boolean isDuplicated = authUser.getSnsAccounts().stream()
                .anyMatch(account -> account.getType().equals(snsAccount.getType()));

        if (isDuplicated) {
            throw new DuplicatedEntityException("이미 연동 계정이 존재합니다.");
        }

        SnsAccount param = SnsAccount.builder()
                .uid(snsAccount.getUid())
                .type(snsAccount.getType())
                .authUser(authUser)
                .build();

        return snsAccountRepository.save(param).toInfo();
    }

    public void deregister(String email, SnsAccount snsAccount) {
        AuthUser authUser = authUserRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        boolean isMatchAccount = authUser.getSnsAccounts().stream()
                .anyMatch(account -> account.getUid().equals(snsAccount.getUid()));

        if (!isMatchAccount) {
            throw new RuntimeException("존재하지 않는 SNS 계정입니다.");
        }

        snsAccountRepository.deleteByUid(snsAccount.getUid());
    }

    public UserPrincipal authenticate(String email, String password) throws CredentialNotMatchException {
        return authUserRepository.findByEmail(email)
                .filter(entity -> entity.matchPassword(passwordEncoder, password))
                .map(user -> new UserPrincipal(user.getId(), user.getEmail()))
                .orElseThrow(CredentialNotMatchException::new);
    }

    public UserPrincipal authenticate(String uid) {
        Optional<SnsAccount> snsAccount = snsAccountRepository.findByUid(uid);

        AuthUser authUser = snsAccount.map(SnsAccount::getAuthUser)
                .orElseThrow(EntityNotFoundException::new);

        return new UserPrincipal(authUser.getId(), authUser.getEmail());
    }
}
