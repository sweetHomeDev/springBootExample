package jp.co.sb.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sb.sample.entity.ResultInfo;

@Repository
public interface ResultMsninfoRepository extends JpaRepository<ResultInfo, Integer> {
    /**
     * <ul>
     *     <li>The alias "department_name" corresponds to ExtendedEmployee2.departmentName.</li>
     *     <li>Returning value "ExtendedEmployee2" must match the first type parameter of JpaRepository,
     *     or it is returned as Object[].</li>
     * </ul>
     */
    @Query(value = "SELECT accesskeyinfo.api_key, msninfo.msn "
        + "FROM accesskeyinfo, msninfo "
        + "WHERE accesskeyinfo.companyid = msninfo.companyid "
        + "AND accesskeyinfo.accesskey = :accessKey "
        + "AND msninfo.msn = :msn ",
        nativeQuery = true)
    ResultInfo findByCheckMsn(@Param("accessKey")String accessKey, @Param("msn")String msn);
}
