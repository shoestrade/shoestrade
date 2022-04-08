package com.study.shoestrade.repository;

import com.study.shoestrade.domain.member.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("select a from Address a join fetch a.member m where m.email = :email order by a.baseAddress desc")
    List<Address> findAddressByMemberEmailOrderByBaseAddress(@Param("email") String email);
}
