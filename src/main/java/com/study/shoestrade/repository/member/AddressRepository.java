package com.study.shoestrade.repository.member;

import com.study.shoestrade.domain.member.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

//    @Query("select a from Address a join fetch a.member m where m.email = :email order by a.baseAddress desc")
//    List<Address> findAddressList(@Param("email") String email);

    @Query("select a from Address a join a.member m where m.email = :email and a.id = :id")
    Optional<Address> findAddressByIdAndEmail(@Param("email") String email, @Param("id") Long id);

    @Query("select a from Address a join a.member m where m.email = :email and a.baseAddress = true")
    Optional<Address> findBaseAddress(@Param("email") String email);

    @Query("select a from Address a join a.member m where m.email = :email and a.baseAddress = false")
    Page<Address> findAddressList(@Param("email") String email, Pageable pageable);
}
