package data.repository;

import data.object.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    boolean existsByManagerEmailAndPassword(String email,String password);
    Optional<MemberEntity> findByManagerEmail(String email);
    Optional<MemberEntity> findByMemberId(Long memberId);
}
