CREATE OR REPLACE PACKAGE BODY PACK_TEST AS

    PROCEDURE insert_job_type (
        p_id          IN P_JOB_TYPE.P_JOB_TYPE_ID%TYPE,
        p_code        IN P_JOB_TYPE.CODE%TYPE,
        p_description IN P_JOB_TYPE.DESCRIPTION%TYPE,
        p_update_by   IN P_JOB_TYPE.UPDATE_BY%TYPE
    ) IS
    BEGIN
        INSERT INTO P_JOB_TYPE (P_JOB_TYPE_ID, CODE, DESCRIPTION, UPDATE_DATE, UPDATE_BY)
        VALUES (p_id, p_code, p_description, SYSTIMESTAMP, p_update_by);
    END insert_job_type;


    PROCEDURE update_job_type (
        p_id          IN P_JOB_TYPE.P_JOB_TYPE_ID%TYPE,
        p_code        IN P_JOB_TYPE.CODE%TYPE,
        p_description IN P_JOB_TYPE.DESCRIPTION%TYPE,
        p_update_by   IN P_JOB_TYPE.UPDATE_BY%TYPE
    ) IS
    BEGIN
        UPDATE P_JOB_TYPE
        SET CODE        = p_code,
            DESCRIPTION = p_description,
            UPDATE_DATE = SYSTIMESTAMP,
            UPDATE_BY   = p_update_by
        WHERE P_JOB_TYPE_ID = p_id;
       
        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20001, 'Data dengan ID ' || p_id || ' tidak ditemukan.');
        END IF;

    END update_job_type;


    PROCEDURE view_job_type (
        p_id          IN  P_JOB_TYPE.P_JOB_TYPE_ID%TYPE,
        p_code        OUT P_JOB_TYPE.CODE%TYPE,
        p_description OUT P_JOB_TYPE.DESCRIPTION%TYPE,
        p_update_date OUT P_JOB_TYPE.UPDATE_DATE%TYPE,
        p_update_by   OUT P_JOB_TYPE.UPDATE_BY%TYPE
    ) IS
    BEGIN
        SELECT CODE, DESCRIPTION, UPDATE_DATE, UPDATE_BY
        INTO   p_code, p_description, p_update_date, p_update_by
        FROM   P_JOB_TYPE
        WHERE  P_JOB_TYPE_ID = p_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            p_code        := NULL;
            p_description := NULL;
            p_update_date := NULL;
            p_update_by   := NULL;
    END view_job_type;

    PROCEDURE delete_job_type (
        p_id          IN  P_JOB_TYPE.P_JOB_TYPE_ID%TYPE
    ) IS
    BEGIN
        DELETE FROM P_JOB_TYPE WHERE P_JOB_TYPE_ID = p_id;
        
        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20002, 'Data dengan ID ' || p_id || ' tidak ditemukan.');
        END IF;
    END delete_job_type;

END PACK_TEST;
