package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.CRD;
import hu.bme.aut.shed.model.CRD_Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CRDRepository extends JpaRepository<CRD, CRD_Key> {
}
