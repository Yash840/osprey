package org.cross.osprey.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

    @NativeQuery("UPDATE jobs\n" +
            "SET\n" +
            "    status = 'CLAIMED',\n" +
            "    claimed_by = :osprey_instance_id,\n" +
            "    attempt = attempt + 1\n" +
            "WHERE id IN (\n" +
            "    SELECT id\n" +
            "    FROM jobs\n" +
            "    WHERE\n" +
            "        (\n" +
            "            status = 'PENDING'\n" +
            "\n" +
            "            OR (\n" +
            "                status = 'WAIT_RETRY'\n" +
            "                AND next_run_at <= CURRENT_TIMESTAMP\n" +
            "                AND retries < max_retries\n" +
            "            )\n" +
            "\n" +
            "            OR (\n" +
            "                status = 'WAIT_NEXT'\n" +
            "                AND next_run_at <= CURRENT_TIMESTAMP\n" +
            "            )\n" +
            "        )\n" +
            "        AND claimed_by IS NULL\n" +
            "    ORDER BY\n" +
            "        priority DESC,\n" +
            "        next_run_at ASC NULLS FIRST,\n" +
            "        created_at ASC\n" +
            "    LIMIT ?1\n" +
            "    FOR UPDATE SKIP LOCKED\n" +
            ")\n" +
            "RETURNING *;")
    List<Job> claimExecutableJobs(int limit);
}