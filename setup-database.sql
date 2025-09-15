-- Setup Database Script untuk Job Type Service
-- Jalankan script ini sebagai user SYSTEM atau user dengan privileges yang cukup

-- 1. Buat sequence untuk P_JOB_TYPE_ID
CREATE SEQUENCE SEQ_P_JOB_TYPE
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- 2. Buat tabel P_JOB_TYPE
CREATE TABLE P_JOB_TYPE (
    P_JOB_TYPE_ID NUMBER(38,0) NOT NULL,
    CODE VARCHAR2(128) NOT NULL,
    DESCRIPTION VARCHAR2(512),
    UPDATE_DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATE_BY VARCHAR2(128) NOT NULL,
    CONSTRAINT PK_P_JOB_TYPE PRIMARY KEY (P_JOB_TYPE_ID)
);

-- 3. Buat unique index pada CODE
CREATE UNIQUE INDEX UK_P_JOB_TYPE_CODE ON P_JOB_TYPE (CODE);

-- 4. Buat package PACK_TEST
CREATE OR REPLACE PACKAGE PACK_TEST AS
    -- Insert procedure
    PROCEDURE insert_job_type (
        p_id          IN P_JOB_TYPE.P_JOB_TYPE_ID%TYPE,
        p_code        IN P_JOB_TYPE.CODE%TYPE,
        p_description IN P_JOB_TYPE.DESCRIPTION%TYPE,
        p_update_by   IN P_JOB_TYPE.UPDATE_BY%TYPE
    );

    -- Update procedure
    PROCEDURE update_job_type (
        p_id          IN P_JOB_TYPE.P_JOB_TYPE_ID%TYPE,
        p_code        IN P_JOB_TYPE.CODE%TYPE,
        p_description IN P_JOB_TYPE.DESCRIPTION%TYPE,
        p_update_by   IN P_JOB_TYPE.UPDATE_BY%TYPE
    );

    -- View procedure (ambil berdasarkan ID)
    PROCEDURE view_job_type (
        p_id          IN  P_JOB_TYPE.P_JOB_TYPE_ID%TYPE,
        p_code        OUT P_JOB_TYPE.CODE%TYPE,
        p_description OUT P_JOB_TYPE.DESCRIPTION%TYPE,
        p_update_date OUT P_JOB_TYPE.UPDATE_DATE%TYPE,
        p_update_by   OUT P_JOB_TYPE.UPDATE_BY%TYPE
    );
    
    PROCEDURE delete_job_type (
        p_id          IN  P_JOB_TYPE.P_JOB_TYPE_ID%TYPE
    );
END PACK_TEST;
/

-- 5. Buat package body PACK_TEST
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
/

-- 6. Insert sample data (opsional)
INSERT INTO P_JOB_TYPE (P_JOB_TYPE_ID, CODE, DESCRIPTION, UPDATE_DATE, UPDATE_BY)
VALUES (SEQ_P_JOB_TYPE.NEXTVAL, 'FULL_TIME', 'Full Time Employee', SYSTIMESTAMP, 'admin');

INSERT INTO P_JOB_TYPE (P_JOB_TYPE_ID, CODE, DESCRIPTION, UPDATE_DATE, UPDATE_BY)
VALUES (SEQ_P_JOB_TYPE.NEXTVAL, 'PART_TIME', 'Part Time Employee', SYSTIMESTAMP, 'admin');

INSERT INTO P_JOB_TYPE (P_JOB_TYPE_ID, CODE, DESCRIPTION, UPDATE_DATE, UPDATE_BY)
VALUES (SEQ_P_JOB_TYPE.NEXTVAL, 'CONTRACT', 'Contract Employee', SYSTIMESTAMP, 'admin');

-- Commit changes
COMMIT;

-- Verifikasi setup
SELECT 'Setup completed successfully!' as status FROM dual;
SELECT COUNT(*) as total_job_types FROM P_JOB_TYPE;
SELECT * FROM P_JOB_TYPE ORDER BY P_JOB_TYPE_ID;
