package org.example.attributeconverter18052024.feature.userdetail;

import org.example.attributeconverter18052024.domain.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail,String> {
}
