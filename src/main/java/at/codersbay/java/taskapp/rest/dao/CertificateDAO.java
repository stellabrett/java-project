package at.codersbay.java.taskapp.rest.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.security.cert.Certificate;

public interface CertificateDAO extends JpaRepository<Certificate, Long> {
}
