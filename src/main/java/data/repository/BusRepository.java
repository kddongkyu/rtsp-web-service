package data.repository;

import data.object.entity.BusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusRepository extends JpaRepository<BusEntity,Long> {
    List<BusEntity> findAllByMember_MemberId(Long memberId);
    Optional<BusEntity> findByName(String name);
}
