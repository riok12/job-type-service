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
