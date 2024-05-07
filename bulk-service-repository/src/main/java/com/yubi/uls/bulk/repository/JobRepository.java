package com.yubi.uls.bulk.repository;

import com.yubi.uls.bulk.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JobRepository extends JpaRepository<JobEntity, String> {


}
