-- Insert 200 claims for the users in the 'users' table
DO $$
    DECLARE
        user_id UUID;
        i INT;
        claim_id UUID;
        status claim_status;
        exam_date TIMESTAMP;
        documents TEXT[];
        receiver_banking_info TEXT[];
    BEGIN
        FOR i IN 1..200 LOOP
                -- Randomly select a user ID
                SELECT id INTO user_id FROM users WHERE role_id = 11 OR role_id = 12 ORDER BY RANDOM() LIMIT 1;

                -- Randomly select a status
                status := CASE
                              WHEN RANDOM() < 0.2 THEN 'FILED'
                              WHEN RANDOM() < 0.4 THEN 'PROCESSING'
                              WHEN RANDOM() < 0.6 THEN 'ACCEPTED'
                              WHEN RANDOM() < 0.8 THEN 'REJECTED'
                              ELSE 'DONE'
                    END;

                -- Set exam_date only if the status is 'ACCEPTED' or 'REJECTED'
                IF status = 'ACCEPTED' OR status = 'REJECTED' THEN
                    exam_date := NOW() - (INTERVAL '1 day' * (RANDOM() * 10)::int);
                ELSE
                    exam_date := NULL;
                END IF;

                -- Generate documents
                documents := ARRAY[
                    i || '_Document1.png',
                    i || '_Document2.png',
                    i || '_Document3.png'
                    ];

                -- Generate receiver_banking_info
                receiver_banking_info := ARRAY[
                    'Bank' || i,
                    'Account' || i,
                    '1234567890' || i
                    ];

                -- Insert claim
                INSERT INTO claims (insured_person_id, claim_status, exam_date, documents, amount, receiver_banking_info)
                VALUES (user_id, status, exam_date, documents, RANDOM() * 10000, receiver_banking_info);
            END LOOP;
    END $$;
