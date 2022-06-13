package com.alkemy.ong.repository;

import com.alkemy.ong.model.Commentary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Long> {

    List<Commentary> findAllByOrderByCreationDate();

}
