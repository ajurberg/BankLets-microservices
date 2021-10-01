package br.com.letscode.securitylogin.login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface LoginRepository extends JpaRepository<Login, Long> {

    Optional<Login> findLoginById(Long id);
    Optional<Login> findByLogin(String login);

}
